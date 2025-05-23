package com.ctc.garage_manager.api.service;

import com.ctc.garage_manager.api.dto.PlateStatusRequest;
import com.ctc.garage_manager.api.dto.PlateStatusResponse;
import com.ctc.garage_manager.domain.entity.ParkingSpot;
import com.ctc.garage_manager.domain.entity.Sector;
import com.ctc.garage_manager.domain.entity.Vehicle;
import com.ctc.garage_manager.domain.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlateStatusServiceTest {

    private PlateStatusService plateStatusService;
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        vehicleRepository = mock(VehicleRepository.class);
        plateStatusService = new PlateStatusService(vehicleRepository);
    }

    @Test
    void shouldReturnPlateStatus() {
        PlateStatusRequest request = new PlateStatusRequest();
        request.setLicensePlate("ABC1234");

        Sector sector = Sector.builder()
                .basePrice(BigDecimal.valueOf(60))
                .build();

        ParkingSpot spot = ParkingSpot.builder()
                .sector(sector)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .licensePlate("ABC1234")
                .entryTime(LocalDateTime.now().minusHours(1))
                .timeParked(ZonedDateTime.now())
                .parkingSpot(spot)
                .build();

        when(vehicleRepository.findByLicensePlate("ABC1234")).thenReturn(Optional.of(vehicle));

        PlateStatusResponse response = plateStatusService.getStatus(request);

        assertEquals("ABC1234", response.getLicensePlate());
        assertNotNull(response.getPriceUntilNow());
        assertNotNull(response.getEntryTime());
        assertNotNull(response.getTimeParked());
    }
}
