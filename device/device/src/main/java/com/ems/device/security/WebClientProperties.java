package com.ems.device.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "external.webclient")
public class WebClientProperties {
    private String keycloak;
    private String device;
}
