package com.sd.emsperson.external.device;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceRestClient {
    private final WebClient DeviceWebClient;
    private final Logger logger = LoggerFactory.getLogger(DeviceRestClient.class);

    public HttpStatus deleteDevice(UUID clientId) {
        try {
           DeviceWebClient.post()
                    .uri("/{id}", clientId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .timeout(Duration.ofSeconds(5))
                    .block();
            logger.info("Device with ID {} deleted successfully.", clientId);
            return HttpStatus.OK;
        } catch (Exception e) {
            logger.error("Error deleting device with ID {}: {}", clientId, e.getMessage(), e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
