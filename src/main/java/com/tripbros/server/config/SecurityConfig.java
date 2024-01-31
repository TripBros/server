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

import com.tripbros.server.security.JwtFilter;
import com.tripbros.server.security.TokenProvider;
import com.tripbros.server.security.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final TokenProvider tokenProvider;
	private final UserDetailsServiceImpl userDetailsService;


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)  throws Exception{
		httpSecurity
			.authorizeHttpRequests((authorize) -> authorize
				.requestMatchers("/", "/css/**", "/fonts/**").permitAll()
				.requestMatchers("/api/register").permitAll()
				.anyRequest().permitAll())
			// .formLogin(form -> form
			// 	.permitAll().defaultSuccessUrl("/", true)
			// 	.usernameParameter("email")) // User entity에서, username을 email로서 사용하였습니다.
			.csrf((csrf) -> csrf.disable())
			.sessionManagement((manage) -> manage.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
			.userDetailsService(userDetailsService);
		return httpSecurity.build();

	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


}
