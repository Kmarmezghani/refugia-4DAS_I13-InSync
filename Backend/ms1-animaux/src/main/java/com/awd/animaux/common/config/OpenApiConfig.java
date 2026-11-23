package com.awd.animaux.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI refugiaAnimauxOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Refugia · MS1 Animaux")
                .version("0.1.0")
                .description("Fiches des animaux, statuts, photos et historique. Service de référence pour MS2 à MS6."));
    }
}
