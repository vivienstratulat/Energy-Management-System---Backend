package com.sd.emsperson.external.keycloak;

import com.sd.emsperson.dtos.*;
import com.sd.emsperson.dtos.CreatePersonRegister;
import com.sd.emsperson.exceptions.ExternalServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KeycloakRestClient {
    private final WebClient keycloakWebClient;

    public AccessToken getAccessToken() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", "ems-person");
        body.add("grant_type", "client_credentials");
        body.add("client_secret", "EdSnHUtjGlsIAsv4GRnyVu2AjZia2pRS");
        return keycloakWebClient
                .post()
                .uri("/realms/ems/protocol/openid-connect/token")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AccessToken.class)
                .timeout(Duration.ofSeconds(5))
                .onErrorResume(e -> Mono.error(new ExternalServiceException(e.getMessage())))
                .block();
    }

    public HttpStatus saveUser(CreatePersonRegister personDTO){
        String accessToken = getAccessToken().getAccess_token();
        System.out.println("TOKEN=="+accessToken);

        CredentialRepresentationRequest credentialRepresentationRequest = CredentialRepresentationRequest.builder()
                .type("password")
                .value(personDTO.getPassword())
                .temporary(false)
                .build();

        UserRepresentationRequest userRepresentationRequest = UserRepresentationRequest.builder()
                .username(personDTO.getEmail())
                .enabled(true)
                .email(personDTO.getEmail())
                .credentials(Collections.singletonList(credentialRepresentationRequest))
                .groups(Collections.singletonList("client_roles"))
                .build();

        return keycloakWebClient
                .post()
                .uri("admin/realms/ems/users")
                .bodyValue(userRepresentationRequest)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(HttpStatus.class)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> Mono.error(new ExternalServiceException(e.getMessage())))
                .block();
    }

    public KeycloakUserDto getUserDetailsDto(UUID userExternalId) {
        String accessToken = getAccessToken().getAccess_token();

        return keycloakWebClient
                .get()
                .uri("admin/realms/ems/users/" + userExternalId)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(KeycloakUserDto.class)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> Mono.error(new ExternalServiceException(e.getMessage())))
                .block();
    }

    public List<KeycloakUserDto> getAllUserDetails() {
        String accessToken = getAccessToken().getAccess_token();

        return keycloakWebClient
                .get()
                .uri("admin/realms/ems/users")
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<KeycloakUserDto>>() {
                })
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> Mono.error(new ExternalServiceException(e.getMessage())))
                .block();
    }

    public HttpStatus deleteUser(UUID externalId) {
        String accessToken = getAccessToken().getAccess_token();

        return keycloakWebClient
                .delete()
                .uri("admin/realms/ems/users/" + externalId)
                .headers(h -> h.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(HttpStatus.class)
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> Mono.error(new ExternalServiceException(e.getMessage())))
                .block();
    }

}
