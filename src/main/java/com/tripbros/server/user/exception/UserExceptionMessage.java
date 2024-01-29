package com.tripbros.server.user.exception;

public enum UserExceptionMessage {

	EMAIL_ALREADY_EXIST("이미 존재하는 이메일입니다."),
	NICKNAME_ALREADY_EXIST("이미 존재하는 닉네임입니다.")
	;

	private final String message;

	UserExceptionMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
