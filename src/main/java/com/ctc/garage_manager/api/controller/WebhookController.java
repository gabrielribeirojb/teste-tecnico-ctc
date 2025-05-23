package com.ctc.garage_manager.api.controller;

import com.ctc.garage_manager.api.dto.WebhookEventRequest;
import com.ctc.garage_manager.api.service.WebhookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping
    public ResponseEntity<Void> receiveEvent(@RequestBody @Valid WebhookEventRequest request) {
        log.info("[WebhookController] - Evento recebido: {}", request);
        webhookService.processEvent(request);
        return ResponseEntity.ok().build();
    }
}
