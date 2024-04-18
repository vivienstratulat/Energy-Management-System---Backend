package com.sd.emsperson.external.keycloak;

import com.sd.emsperson.config.WebClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class KeycloakWebClientConfig {
    private final WebClientProperties webClientProperties;

    @Bean
    public WebClient keycloakWebClient() {
        HttpClient httpClient = HttpClient.newConnection().responseTimeout(Duration.ofSeconds(30)).compress(true);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(webClientProperties.getKeycloak())
                .build();
    }
}