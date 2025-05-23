package com.ctc.garage_manager.infra.client.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SectorDTO {
    private String sector;
    private BigDecimal basePrice;
    private Integer max_capacity;
    private String open_hour;
    private String close_hour;
    private Integer duration_limit_minutes;
}
