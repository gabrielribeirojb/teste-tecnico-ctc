package com.ctc.garage_manager.api.service;

import com.ctc.garage_manager.api.dto.WebhookEventRequest;
import com.ctc.garage_manager.domain.entity.*;
import com.ctc.garage_manager.domain.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final RevenueRepository revenueRepository;

    @Transactional
    public void processEvent(WebhookEventRequest request) {
        switch (request.getEventType()) {
            case "ENTRY" -> processEntry(request);
            case "PARKED" -> processParked(request);
            case "EXIT" -> processExit(request);
            default -> throw new IllegalArgumentException("Evento desconhecido: " + request.getEventType());
        }
    }

    private void processEntry(WebhookEventRequest request) {
        log.info("[WebhookService] - Processando entrada: {}", request.getLicensePlate());

        Vehicle vehicle = Vehicle.builder()
                .licensePlate(request.getLicensePlate())
                .entryTime(request.getEntryTime().toLocalDateTime())
                .build();

        vehicleRepository.save(vehicle);
    }

    private void processParked(WebhookEventRequest request) {
        log.info("[WebhookService] - Processando estacionamento: {}", request.getLicensePlate());

        ParkingSpot spot = parkingSpotRepository.findByLatAndLng(request.getLat(), request.getLng())
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada."));

        Vehicle vehicle = vehicleRepository.findByLicensePlate(request.getLicensePlate())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado."));

        vehicle.setParkingSpot(spot);
        vehicle.setTimeParked(ZonedDateTime.now());

        vehicleRepository.save(vehicle);
    }

    private void processExit(WebhookEventRequest request) {
        log.info("[WebhookService] - Processando saída: {}", request.getLicensePlate());

        Vehicle vehicle = vehicleRepository.findByLicensePlate(request.getLicensePlate())
                .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado."));

        ZonedDateTime entryTime = vehicle.getEntryTime().atZone(ZoneId.systemDefault());
        ZonedDateTime exitTime = request.getExitTime();

        long minutes = Duration.between(entryTime, exitTime).toMinutes();

        Sector sector = Optional.ofNullable(vehicle.getParkingSpot())
                .map(ParkingSpot::getSector)
                .orElseThrow(() -> new EntityNotFoundException("Setor da vaga não encontrado."));

        int capacity = sector.getMaxCapacity();
        long occupied = vehicleRepository.countByParkingSpotSector(sector);
        BigDecimal dynamicPrice = calculateDynamicPrice(sector.getBasePrice(), occupied, capacity);

        BigDecimal amount = dynamicPrice.multiply(BigDecimal.valueOf(minutes)).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);

        Revenue revenue = Revenue.builder()
                .sector(sector.getName())
                .amount(amount)
                .timestamp(exitTime.toLocalDateTime())
                .build();

        revenueRepository.save(revenue);

        vehicleRepository.delete(vehicle);
    }

    private BigDecimal calculateDynamicPrice(BigDecimal basePrice, long occupied, int capacity) {
        double ratio = (double) occupied / capacity;

        if (ratio < 0.25) return basePrice.multiply(BigDecimal.valueOf(0.90));
        if (ratio < 0.50) return basePrice;
        if (ratio < 0.75) return basePrice.multiply(BigDecimal.valueOf(1.10));
        return basePrice.multiply(BigDecimal.valueOf(1.25));
    }
}
