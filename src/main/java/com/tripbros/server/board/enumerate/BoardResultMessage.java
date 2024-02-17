package com.tripbros.server.board.enumerate;

public enum BoardResultMessage {
	CREATE_BOARD_SUCCESS("게시글 작성 성공"),
	EDIT_BOARD_SUCCESS("게시글 수정 성공"),
	GET_BOARD_SUCCESS("게시글 조회 성공"),
	DELETE_BOARD_SUCCESS("게시글 삭제 성공"),
	UPDATE_BOOKMARK_BOARD_SUCCESS("게시글 북마크 반영 성공");

	private final String message;

	BoardResultMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
