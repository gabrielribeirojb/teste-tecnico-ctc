package com.ctc.garage_manager.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@Builder
public class SpotStatusResponse {

    @JsonProperty("ocupied")
    private boolean occupied;

    @JsonProperty("entry_time")
    private ZonedDateTime entryTime;

    @JsonProperty("time_parked")
    private ZonedDateTime timeParked;
}
