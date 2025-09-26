package com.ashwin.weatherapi.weatherapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Weather API")
                        .version("1.0.0")
                        .description("A REST API for retrieving weather data from various providers")
                        .contact(new Contact()
                                .name("Ashwin")
                                .email("ashwin@example.com")))
                .components(new Components()
                        .addSchemas("ErrorResponse", new Schema<>()
                                .type("object")
                                .addProperty("error", new Schema<>().type("string").description("Error message"))));
    }
}
