package com.leets.xcellentbe.global.config.swagger;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;

@OpenAPIDefinition(info = @Info(
	title = "Xcellent API",
	description = "Xcellent API 명세서",
	version = "v1.0.0"))
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		SecurityScheme securityScheme = getSecurityScheme();
		SecurityRequirement securityRequirement = getSecurityRequireMent();

		Server server = new Server();
		server.setUrl("/");

		return new OpenAPI()
			.servers(List.of(server))
			.components(new Components().addSecuritySchemes("jwt token", securityScheme))
			.security(List.of(securityRequirement));
	}

	private SecurityScheme getSecurityScheme() {
		return new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER).name("Authorization");
	}

	private SecurityRequirement getSecurityRequireMent() {
		return new SecurityRequirement().addList("bearer");
	}
}
