package com.tripbros.server.user.enumerate;

public enum UserResultMessage {

	REGISTER_SUCCESS("회원 가입 성공");

	final String message;

	UserResultMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
