package com.tripbros.server.security;

import lombok.Builder;

@Builder
public record JwtDTO(
	String grantType,
	String accessToken
) {
}
