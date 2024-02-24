package com.tripbros.server.chatting.exception;

public enum ChatExceptionMessage {
	CANNOT_CHAT_WITH_SELF("자기 자신과 채팅 할 수 없습니다."),
	CANNOT_ACCESS_CHATROOM("접근 권한이 없는 채팅방입니다.")
	;

	private final String message;

	ChatExceptionMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
