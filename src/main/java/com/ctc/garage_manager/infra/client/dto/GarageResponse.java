package com.ctc.garage_manager.infra.client.dto;

import lombok.Data;
import java.util.List;

@Data
public class GarageResponse {
    private List<SectorDTO> garage;
    private List<ParkingSpotDTO> spots;
}
