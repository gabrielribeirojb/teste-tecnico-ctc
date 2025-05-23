package com.ctc.garage_manager.infra.client;

import com.ctc.garage_manager.infra.client.dto.GarageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GarageApiClient {

    private final RestTemplate restTemplate;

    private static final String GARAGE_URL = "http://localhost:3000/garage";

    public GarageResponse getGarageData() {
        return restTemplate.getForObject(GARAGE_URL, GarageResponse.class);
    }
}
