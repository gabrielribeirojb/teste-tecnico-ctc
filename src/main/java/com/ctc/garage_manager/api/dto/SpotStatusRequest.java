package com.ctc.garage_manager.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SpotStatusRequest {
    @NotNull
    private Double lat;

    @NotNull
    private Double lng;
}
