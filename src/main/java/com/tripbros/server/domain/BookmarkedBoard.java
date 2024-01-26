package com.tripbros.server.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
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
public class BookmarkedBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "board_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "board_id")
	private Board board;

	private LocalDateTime bookmarked_at;

	@Builder
	public BookmarkedBoard(User user, Board board, LocalDateTime bookmarked_at) {
		this.user = user;
		this.board = board;
		this.bookmarked_at = bookmarked_at;
	}
}
