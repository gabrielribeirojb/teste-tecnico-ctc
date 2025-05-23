package com.ctc.garage_manager.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PlateStatusRequest {
    @NotBlank
    @JsonProperty("license_plate")
    private String licensePlate;
}
