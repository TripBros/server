package com.tripbros.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)  throws Exception{
		httpSecurity
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/", "/css/**", "/fonts/**").permitAll()
				.requestMatchers("/api/register").permitAll()
				.anyRequest().permitAll())
			.formLogin(form -> form
				.loginPage("/api/login")
				.permitAll().defaultSuccessUrl("/", true))
			.csrf((csrf) -> csrf.disable())
			.sessionManagement((manage) -> manage.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));
			// .userDetailsService(userDetailsService);
		return httpSecurity.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
