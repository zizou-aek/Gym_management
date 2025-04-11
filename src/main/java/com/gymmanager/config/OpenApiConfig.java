package com.gymmanager.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi gymManagerApi() {
        return GroupedOpenApi.builder()
                .group("gymmanager-api")
                .packagesToScan("com.gymmanager.controller")
                .build();
    }

    @Bean
    public OpenAPI gymManagerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("GymManager API")
                        .description("API for managing a gym")
                        .version("v1.0"));
    }
}