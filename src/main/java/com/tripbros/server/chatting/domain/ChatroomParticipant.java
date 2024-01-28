package com.tripbros.server.chatting.domain;

import com.tripbros.server.user.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(ChatroomParticipantId.class)
@Getter
@NoArgsConstructor
public class ChatroomParticipant {

	@Id
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Id
	@ManyToOne
	@JoinColumn(name = "chatroom_id")
	private Chatroom chatroom;

	@Builder
	public ChatroomParticipant(User user, Chatroom chatroom) {
		this.user = user;
		this.chatroom = chatroom;
	}

}
