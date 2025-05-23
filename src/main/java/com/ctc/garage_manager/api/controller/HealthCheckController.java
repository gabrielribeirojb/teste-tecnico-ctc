package com.ctc.garage_manager.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> check() {
        return ResponseEntity.ok("Garage Manager is up and running!");
    }
}