package com.tripbros.server.user.service;

import com.tripbros.server.security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbros.server.user.dto.SignInRequest;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final TokenProvider tokenProvider;
	private final AuthenticationManagerBuilder managerBuilder;

	public JwtDTO signIn(SignInRequest request) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			request.email(), request.password());

		Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

		return tokenProvider.createToken(authentication);
	}
}
