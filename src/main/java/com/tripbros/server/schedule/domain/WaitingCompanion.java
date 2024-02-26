package com.tripbros.server.schedule.domain;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.user.domain.User;

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
public class WaitingCompanion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opponent_id")
	private User opponent;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	boolean isDeleted;

	@Builder
	public WaitingCompanion(Board board, User opponent, User user) {
		this.board = board;
		this.opponent = opponent;
		this.user = user;
		this.isDeleted = false;
	}

	public void setDeleted(boolean flag) {
		this.isDeleted = flag;
	}
}
