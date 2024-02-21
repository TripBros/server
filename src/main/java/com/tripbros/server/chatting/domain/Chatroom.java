package com.tripbros.server.chatting.domain;

import java.time.LocalDateTime;

import com.tripbros.server.board.domain.Board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Chatroom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "board_id")
	private Board board;

	private boolean status;
	private LocalDateTime createAt;
	private String title;

	private String lastMessage;

	//TODO: 프로필

	@Builder

	public Chatroom(Board board, boolean status, LocalDateTime createAt, String title) {
		this.board = board;
		this.status = status;
		this.createAt = createAt;
		this.title = title;
	}
}
