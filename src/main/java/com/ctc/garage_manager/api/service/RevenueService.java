package com.ctc.garage_manager.api.service;

import com.ctc.garage_manager.api.dto.RevenueResponse;
import com.ctc.garage_manager.domain.repository.RevenueRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RevenueService {

    private final RevenueRepository revenueRepository;

    public RevenueResponse getRevenue(String sector, LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);

        var revenue = revenueRepository
                .findBySectorAndTimestampBetween(sector, startOfDay, endOfDay)
                .orElseThrow(() -> new EntityNotFoundException("Nenhuma receita encontrada para o setor informado."));

        return RevenueResponse.builder()
                .amount(revenue.getAmount())
                .currency("BRL")
                .timestamp(revenue.getTimestamp())
                .build();
    }
}
