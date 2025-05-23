package com.ctc.garage_manager.api.controller;

import com.ctc.garage_manager.api.dto.RevenueResponse;
import com.ctc.garage_manager.api.service.RevenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/revenue")
public class RevenueController {

    private final RevenueService revenueService;

    @GetMapping
    public ResponseEntity<RevenueResponse> getRevenue(
            @RequestParam String sector,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(revenueService.getRevenue(sector, date));
    }
}
