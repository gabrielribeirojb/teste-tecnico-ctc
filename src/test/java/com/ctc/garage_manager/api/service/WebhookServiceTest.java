package com.ctc.garage_manager.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ctc.garage_manager.api.dto.WebhookEventRequest;
import com.ctc.garage_manager.domain.entity.ParkingSpot;
import com.ctc.garage_manager.domain.entity.Sector;
import com.ctc.garage_manager.domain.entity.Vehicle;
import com.ctc.garage_manager.domain.repository.ParkingSpotRepository;
import com.ctc.garage_manager.domain.repository.RevenueRepository;
import com.ctc.garage_manager.domain.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

class WebhookServiceTest {

    private WebhookService webhookService;
    private VehicleRepository vehicleRepository;
    private ParkingSpotRepository parkingSpotRepository;
    private RevenueRepository revenueRepository;

    @BeforeEach
    void setUp() {
        vehicleRepository = mock(VehicleRepository.class);
        parkingSpotRepository = mock(ParkingSpotRepository.class);
        revenueRepository = mock(RevenueRepository.class);
        webhookService = new WebhookService(vehicleRepository, parkingSpotRepository, revenueRepository);
    }

    @Test
    void shouldProcessEntryEventAndSaveVehicle() {
        WebhookEventRequest request = new WebhookEventRequest();
        request.setLicensePlate("ABC1234");
        request.setEntryTime(ZonedDateTime.now());
        request.setEventType("ENTRY");

        webhookService.processEvent(request);

        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(captor.capture());

        Vehicle savedVehicle = captor.getValue();
        assertEquals("ABC1234", savedVehicle.getLicensePlate());
        assertNotNull(savedVehicle.getEntryTime());
    }

    @Test
    void shouldProcessParkedEventAndAssociateSpot() {
        WebhookEventRequest request = new WebhookEventRequest();
        request.setLicensePlate("DEF5678");
        request.setLat(-23.561684);
        request.setLng(-46.655981);
        request.setEventType("PARKED");

        ParkingSpot mockSpot = ParkingSpot.builder().id(1L).build();
        Vehicle mockVehicle = Vehicle.builder().licensePlate("DEF5678").build();

        when(parkingSpotRepository.findByLatAndLng(anyDouble(), anyDouble())).thenReturn(Optional.of(mockSpot));
        when(vehicleRepository.findByLicensePlate("DEF5678")).thenReturn(Optional.of(mockVehicle));

        webhookService.processEvent(request);

        assertEquals(mockSpot, mockVehicle.getParkingSpot());
        assertNotNull(mockVehicle.getTimeParked());
        verify(vehicleRepository).save(mockVehicle);
    }

    @Test
    void shouldProcessExitEventAndRegisterRevenue() {
        ZonedDateTime entryTime = ZonedDateTime.now().minusHours(2);
        ZonedDateTime exitTime = ZonedDateTime.now();

        Sector sector = Sector.builder()
                .name("A")
                .basePrice(BigDecimal.TEN)
                .maxCapacity(100)
                .build();

        ParkingSpot spot = ParkingSpot.builder().sector(sector).build();

        Vehicle vehicle = Vehicle.builder()
                .licensePlate("XYZ9999")
                .entryTime(entryTime.toLocalDateTime())
                .parkingSpot(spot)
                .build();

        WebhookEventRequest request = new WebhookEventRequest();
        request.setLicensePlate("XYZ9999");
        request.setExitTime(exitTime);
        request.setEventType("EXIT");

        when(vehicleRepository.findByLicensePlate("XYZ9999")).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.countByParkingSpotSector(sector)).thenReturn(10L);

        webhookService.processEvent(request);

        verify(revenueRepository).save(any());
        verify(vehicleRepository).delete(vehicle);
    }

}