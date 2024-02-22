package com.tripbros.server.chatting.enumerate;

public enum ChatResultMessage {
	CHATROOM_CREATE_SUCCESS("채팅방 생성 / 로드 성공"),
	MESSAGES_LOAD_SUCCESS("메세지 로드 성공")

	;

	private final String message;

	ChatResultMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
