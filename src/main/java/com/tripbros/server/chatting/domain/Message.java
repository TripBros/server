package com.tripbros.server.chatting.domain;

import java.time.LocalDateTime;

import com.tripbros.server.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne
	@JoinColumn(name = "chatroom_id")
	private Chatroom chatroom;

	@Column(columnDefinition = "TEXT")
	private String content;
	private LocalDateTime sendedAt;

	@Builder
	public Message(User user, Chatroom chatroom, String content, LocalDateTime sendedAt) {
		this.user = user;
		this.chatroom = chatroom;
		this.content = content;
		this.sendedAt = sendedAt;
	}
}
