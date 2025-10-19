package com.glamora_store.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
  @Bean
  public OpenAPI productServiceAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("Glamora Store API")
        .description("This is the REST API for Glamora Store")
        .version("v0.0.1"))
      .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
      .components(new Components()
        .addSecuritySchemes(
          "Bearer Authentication",
          new SecurityScheme()
            .name("Bearer Authentication")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .description("Enter JWT token")));
  }
}
