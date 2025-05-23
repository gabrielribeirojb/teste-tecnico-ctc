package com.ctc.garage_manager.api.controller;

import com.ctc.garage_manager.api.dto.PlateStatusRequest;
import com.ctc.garage_manager.api.dto.PlateStatusResponse;
import com.ctc.garage_manager.api.service.PlateStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plate-status")
@RequiredArgsConstructor
public class PlateStatusController {

    private final PlateStatusService plateStatusService;

    @PostMapping
    public ResponseEntity<PlateStatusResponse> getPlateStatus(@RequestBody @Valid PlateStatusRequest request) {
        return ResponseEntity.ok(plateStatusService.getStatusByPlate(request.getLicensePlate()));
    }
}
