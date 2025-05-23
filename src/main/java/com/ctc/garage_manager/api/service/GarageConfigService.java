package com.ctc.garage_manager.api.service;

import com.ctc.garage_manager.domain.entity.ParkingSpot;
import com.ctc.garage_manager.domain.entity.Sector;
import com.ctc.garage_manager.domain.repository.ParkingSpotRepository;
import com.ctc.garage_manager.domain.repository.SectorRepository;
import com.ctc.garage_manager.infra.client.GarageApiClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GarageConfigService {

    private final SectorRepository sectorRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final GarageApiClient garageApiClient;

    @Value("${garage.init.enabled:true}")
    private boolean garageInitEnabled;

    @PostConstruct
    public void initGarage() {
        if (!garageInitEnabled) {
            log.warn("[GarageConfigService] - Init desabilitado via flag.");
            return;
        }

        log.info("[GarageConfigService] - Buscando configuração inicial da garagem...");

        try {
            var response = garageApiClient.getGarageData();

            response.getGarage().forEach(sectorDTO -> {
                Sector sector = Sector.builder()
                        .name(sectorDTO.getSector())
                        .basePrice(sectorDTO.getBasePrice())
                        .maxCapacity(sectorDTO.getMax_capacity())
                        .openHour(sectorDTO.getOpen_hour())
                        .closeHour(sectorDTO.getClose_hour())
                        .durationLimitMinutes(sectorDTO.getDuration_limit_minutes())
                        .build();
                sectorRepository.save(sector);
            });

            response.getSpots().forEach(spotDTO -> {
                Sector sector = sectorRepository.findByName(spotDTO.getSector()).orElseThrow();
                ParkingSpot spot = ParkingSpot.builder()
                        .id(spotDTO.getId())
                        .lat(spotDTO.getLat())
                        .lng(spotDTO.getLng())
                        .sector(sector)
                        .build();
                parkingSpotRepository.save(spot);
            });

            log.info("[GarageConfigService] - Configuração da garagem inicializada com sucesso.");
        } catch (Exception e) {
            log.error("[GarageConfigService] - Erro ao buscar configuração da garagem", e);
        }
    }
}
