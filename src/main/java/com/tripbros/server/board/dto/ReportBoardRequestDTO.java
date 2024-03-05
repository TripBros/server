package com.tripbros.server.board.dto;

import java.time.LocalDateTime;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.domain.ReportedBoard;
import com.tripbros.server.user.domain.User;

import jakarta.validation.constraints.NotBlank;

public record ReportBoardRequestDTO(Long boardId,
									@NotBlank(message = "신고 사유는 필수 작성입니다.") String reason) {

	public ReportedBoard toEntity(User user, Board board){
		return ReportedBoard.builder()
			.user(user)
			.board(board)
			.reason(reason)
			.reportedAt(LocalDateTime.now())
			.build();
	}
}
