package com.ctc.garage_manager.infra.client.dto;

import lombok.Data;

@Data
public class ParkingSpotDTO {
    private Long id;
    private String sector;
    private Double lat;
    private Double lng;
}
