package com.tripbros.server.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(info = @Info(title = "TripBros API 명세서"))
public class SwaggerConfig {

	@Bean
	public GroupedOpenApi openApi(){
		return GroupedOpenApi.builder()
			.group("TripBros API V1")
			.pathsToMatch("/**")
			.build();
	}

	@Bean
	public OpenAPI api() {
		io.swagger.v3.oas.models.security.SecurityScheme apiKey = new io.swagger.v3.oas.models.security.SecurityScheme()
			.type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
			.scheme("bearer").bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name("Authorization");

		SecurityRequirement securityRequirement = new SecurityRequirement()
			.addList("Bearer Token");

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("Bearer Token", apiKey))
			.addSecurityItem(securityRequirement);
	}
}