package com.tripbros.server.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripbros.server.common.dto.BaseResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtExceptionHandlerFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (UnauthorizedAccessException e) {
			handleJwtErrorResponse(response, e.getMessage());
		}
	}

	private void handleJwtErrorResponse(HttpServletResponse response, String message) {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		BaseResponse<Object> baseResponse = new BaseResponse<>(false, HttpStatus.BAD_REQUEST, message, null);
		try {
			response.getWriter().write(new ObjectMapper().writeValueAsString(baseResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
