package com.ctc.garage_manager.api.service;

import com.ctc.garage_manager.api.dto.PlateStatusRequest;
import com.ctc.garage_manager.api.dto.PlateStatusResponse;
import com.ctc.garage_manager.domain.entity.ParkingSpot;
import com.ctc.garage_manager.domain.entity.Sector;
import com.ctc.garage_manager.domain.entity.Vehicle;
import com.ctc.garage_manager.domain.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class PlateStatusService {

    private final VehicleRepository vehicleRepository;

    public PlateStatusResponse getStatus(PlateStatusRequest request) {
        Vehicle vehicle = vehicleRepository.findByLicensePlate(request.getLicensePlate())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado."));

        ParkingSpot spot = vehicle.getParkingSpot();
        if (spot == null) throw new RuntimeException("Vaga não associada ao veículo.");

        Sector sector = spot.getSector();
        if (sector == null) throw new RuntimeException("Setor não encontrado.");

        ZonedDateTime entryTime = vehicle.getEntryTime().atZone(ZoneId.systemDefault());
        ZonedDateTime now = ZonedDateTime.now();

        long minutes = Duration.between(entryTime, now).toMinutes();
        BigDecimal amount = sector.getBasePrice().multiply(BigDecimal.valueOf(minutes))
                .divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);

        return PlateStatusResponse.builder()
                .licensePlate(vehicle.getLicensePlate())
                .priceUntilNow(amount)
                .entryTime(entryTime)
                .timeParked(vehicle.getTimeParked())
                .build();
    }
}