package com.tripbros.server.user.enumerate;

public enum UserResultMessage {

	REGISTER_SUCCESS("회원 가입 성공"),
	EDIT_USERINFO_SUCCESS("정보 변경 성공")

	;

	final String message;

	UserResultMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
