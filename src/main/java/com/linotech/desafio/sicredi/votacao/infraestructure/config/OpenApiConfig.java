package com.linotech.desafio.sicredi.votacao.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI votacaoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Votação - Sicredi")
                        .description("API para gerenciamento de pautas, sessões de votação e votos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Linotech")
                                .url("https://github.com/orgs/lino-tech-works/repositories")));
    }
}

