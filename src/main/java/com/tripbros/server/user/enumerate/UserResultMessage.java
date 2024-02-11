package com.tripbros.server.user.enumerate;

public enum UserResultMessage {

	REGISTER_SUCCESS("회원 가입 성공"),
	EDIT_USERINFO_SUCCESS("정보 변경 성공"),
	LOGIN_SUCCESS("로그인 성공"),
	RESIGN_SUCCESS("회원 탈퇴 성공")

	;

	final String message;

	UserResultMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
