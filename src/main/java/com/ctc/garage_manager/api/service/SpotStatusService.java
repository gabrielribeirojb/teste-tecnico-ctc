package com.ctc.garage_manager.api.service;

import com.ctc.garage_manager.api.dto.SpotStatusRequest;
import com.ctc.garage_manager.api.dto.SpotStatusResponse;
import com.ctc.garage_manager.domain.entity.ParkingSpot;
import com.ctc.garage_manager.domain.entity.Vehicle;
import com.ctc.garage_manager.domain.repository.ParkingSpotRepository;
import com.ctc.garage_manager.domain.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpotStatusService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final VehicleRepository vehicleRepository;

    public SpotStatusResponse getStatus(SpotStatusRequest request) {
        ParkingSpot spot = parkingSpotRepository.findByLatAndLng(request.getLat(), request.getLng())
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada."));

        Optional<Vehicle> vehicleOpt = vehicleRepository.findByParkingSpot(spot);

        if (vehicleOpt.isEmpty()) {
            return SpotStatusResponse.builder()
                    .occupied(false)
                    .build();
        }

        Vehicle vehicle = vehicleOpt.get();
        return SpotStatusResponse.builder()
                .occupied(true)
                .entryTime(vehicle.getEntryTime().atZone(ZoneId.systemDefault()))
                .timeParked(vehicle.getTimeParked())
                .build();
    }
}
