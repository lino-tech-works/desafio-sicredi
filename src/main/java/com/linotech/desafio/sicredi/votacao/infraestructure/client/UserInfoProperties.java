package com.linotech.desafio.sicredi.votacao.infraestructure.client;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client.user-info")
public record UserInfoProperties(
        String baseUrl,
        Duration connectTimeout,
        Duration readTimeout
) {
}