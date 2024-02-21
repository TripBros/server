package com.tripbros.server.chatting.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record MessageResponse(
	String userName,
	String profileImage,
	String content,
	LocalDateTime sentAt,
	Boolean isSystemMessage
) {
}
