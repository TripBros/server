package com.tripbros.server.chatting.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.tripbros.server.board.domain.Board;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Chatroom {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	private boolean status;
	private LocalDateTime updatedAt;
	private String title;

	private String lastMessage;
	private Boolean isGroupChat;
	//TODO: 프로필

	@Builder

	public Chatroom(Board board, boolean status, LocalDateTime updatedAt, String title, String lastMessage,
		Boolean isGroupChat) {
		this.board = board;
		this.status = status;
		this.updatedAt = updatedAt;
		this.title = title;
		this.lastMessage = lastMessage;
		this.isGroupChat = isGroupChat;
	}

	public void updateLastMessageWithTime(String lastMessage) {
		this.lastMessage = lastMessage;
		this.updatedAt = LocalDateTime.now();
	}
}
