package com.tripbros.server.domain;

import java.time.LocalDateTime;

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
	private ChatRoom chatRoom;

	@Column(columnDefinition = "TEXT")
	private String content;
	private LocalDateTime sendedAt;

	@Builder
	public Message(User user, ChatRoom chatRoom, String content, LocalDateTime sendedAt) {
		this.user = user;
		this.chatRoom = chatRoom;
		this.content = content;
		this.sendedAt = sendedAt;
	}
}
