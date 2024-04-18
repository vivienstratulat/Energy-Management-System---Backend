package com.sd.emsperson.external.device;

import com.sd.emsperson.config.WebClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


@Configuration
@RequiredArgsConstructor
public class DeviceWebClientConfig {
    private final WebClientProperties webClientProperties;

    @Bean
    public WebClient DeviceWebClient() {
        HttpClient httpClient = HttpClient.create().responseTimeout(java.time.Duration.ofMillis(5000));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://ems-device:8081/device/deleteWithClient")
                .build();
    }
}
