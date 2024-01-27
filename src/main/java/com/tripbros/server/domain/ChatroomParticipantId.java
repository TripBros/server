package com.tripbros.server.domain;

import java.io.Serializable;
import java.util.Objects;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ChatroomParticipantId implements Serializable {
	private User user;
	private Chatroom chatroom;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ChatroomParticipantId that = (ChatroomParticipantId)o;
		return Objects.equals(user, that.user) && Objects.equals(chatroom, that.chatroom);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, chatroom);
	}
}
