package com.tripbros.server.chatting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.tripbros.server.enumerate.City;

public record ChatroomResponse(
	UUID uuid,
	String title,
	String chatroomImage,
	String lastMessage,
	LocalDateTime updatedAt,
	Boolean isGroupChat,
	Boolean isHost,
	Boolean isConfirmed,
	City city,
	LocalDate startDate,
	LocalDate endDate,
	Integer requiredHeadCount
) {
}
