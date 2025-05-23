package com.ctc.garage_manager.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Builder
public class PlateStatusResponse {

    @JsonProperty("license_plate")
    private String licensePlate;

    @JsonProperty("price_until_now")
    private BigDecimal priceUntilNow;

    @JsonProperty("entry_time")
    private ZonedDateTime entryTime;

    @JsonProperty("time_parked")
    private ZonedDateTime timeParked;
}
