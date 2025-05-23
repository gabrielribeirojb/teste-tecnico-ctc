package com.ctc.garage_manager.api.service;

import com.ctc.garage_manager.api.dto.RevenueResponse;
import com.ctc.garage_manager.domain.entity.Revenue;
import com.ctc.garage_manager.domain.repository.RevenueRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RevenueServiceTest {

    private RevenueService revenueService;
    private RevenueRepository revenueRepository;

    @BeforeEach
    void setUp() {
        revenueRepository = mock(RevenueRepository.class);
        revenueService = new RevenueService(revenueRepository);
    }

    @Test
    void shouldReturnRevenueForDateAndSector() {
        String sector = "A";
        LocalDate date = LocalDate.of(2025, 1, 1);
        LocalDateTime timestamp = LocalDateTime.of(2025, 1, 1, 12, 0);

        Revenue revenue = Revenue.builder()
                .sector(sector)
                .amount(BigDecimal.TEN)
                .timestamp(timestamp)
                .build();

        when(revenueRepository.findBySectorAndTimestampBetween(eq(sector), any(), any()))
                .thenReturn(Optional.of(revenue));

        RevenueResponse response = revenueService.getRevenue(sector, date);

        assertEquals(BigDecimal.TEN, response.getAmount());
        assertEquals("BRL", response.getCurrency());
        assertEquals(timestamp, response.getTimestamp());
    }
}
