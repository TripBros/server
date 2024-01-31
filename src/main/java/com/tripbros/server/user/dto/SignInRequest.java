package com.tripbros.server.user.dto;

public record SignInRequest(
	String email,
	String password
) {
}
