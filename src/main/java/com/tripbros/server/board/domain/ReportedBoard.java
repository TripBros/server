package com.tripbros.server.board.domain;

import java.time.LocalDateTime;

import com.tripbros.server.user.domain.User;

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
public class ReportedBoard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "reporter_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "reported_board_id")
	private Board board;

	private String reason;
	private LocalDateTime reportedAt;

	@Builder
	public ReportedBoard(User user, Board board, String reason, LocalDateTime reportedAt) {
		this.user = user;
		this.board = board;
		this.reason = reason;
		this.reportedAt = reportedAt;
	}
}
