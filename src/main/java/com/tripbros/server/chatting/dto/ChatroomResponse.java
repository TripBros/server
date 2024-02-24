package com.tripbros.server.chatting.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatroomResponse(
	UUID uuid,
	String title,
	String chatroomImage,
	String lastMessage,
	LocalDateTime updatedAt,
	Boolean isGroupChat
) {
}
