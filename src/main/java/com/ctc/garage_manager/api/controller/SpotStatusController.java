package com.ctc.garage_manager.api.controller;

import com.ctc.garage_manager.api.dto.SpotStatusRequest;
import com.ctc.garage_manager.api.dto.SpotStatusResponse;
import com.ctc.garage_manager.api.service.SpotStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spot-status")
@RequiredArgsConstructor
public class SpotStatusController {

    private final SpotStatusService service;

    @PostMapping
    public ResponseEntity<SpotStatusResponse> getSpotStatus(@RequestBody @Valid SpotStatusRequest request) {
        return ResponseEntity.ok(service.getStatus(request));
    }
}
