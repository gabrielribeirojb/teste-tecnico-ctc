package com.ctc.garage_manager.api.service;

import com.ctc.garage_manager.api.dto.SpotStatusRequest;
import com.ctc.garage_manager.api.dto.SpotStatusResponse;
import com.ctc.garage_manager.domain.entity.ParkingSpot;
import com.ctc.garage_manager.domain.entity.Vehicle;
import com.ctc.garage_manager.domain.repository.ParkingSpotRepository;
import com.ctc.garage_manager.domain.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpotStatusServiceTest {

    private SpotStatusService spotStatusService;
    private ParkingSpotRepository parkingSpotRepository;
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        parkingSpotRepository = mock(ParkingSpotRepository.class);
        vehicleRepository = mock(VehicleRepository.class);
        spotStatusService = new SpotStatusService(parkingSpotRepository, vehicleRepository);
    }

    @Test
    void shouldReturnSpotStatus() {
        SpotStatusRequest request = new SpotStatusRequest();
        request.setLat(-23.561684);
        request.setLng(-46.655981);

        ParkingSpot spot = ParkingSpot.builder()
                .id(1L)
                .lat(-23.561684)
                .lng(-46.655981)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .parkingSpot(spot)
                .entryTime(ZonedDateTime.now().minusHours(1).toLocalDateTime())
                .timeParked(ZonedDateTime.now())
                .build();

        when(parkingSpotRepository.findByLatAndLng(anyDouble(), anyDouble())).thenReturn(Optional.of(spot));
        when(vehicleRepository.findByParkingSpot(spot)).thenReturn(Optional.of(vehicle));

        SpotStatusResponse response = spotStatusService.getStatus(request);

        assertTrue(response.isOccupied());
        assertNotNull(response.getEntryTime());
        assertNotNull(response.getTimeParked());
    }
}
