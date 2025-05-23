package com.ctc.garage_manager.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class WebhookEventRequest {

    @JsonProperty("license_plate")
    private String licensePlate;

    @JsonProperty("entry_time")
    private ZonedDateTime entryTime;

    @JsonProperty("exit_time")
    private ZonedDateTime exitTime;

    private Double lat;
    private Double lng;

    @JsonProperty("event_type")
    private String eventType;
}
