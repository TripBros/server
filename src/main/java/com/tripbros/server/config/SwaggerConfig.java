package com.tripbros.server.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

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
}