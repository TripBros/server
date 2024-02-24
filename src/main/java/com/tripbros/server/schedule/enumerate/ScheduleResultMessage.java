package com.tripbros.server.schedule.enumerate;

public enum ScheduleResultMessage {
	CREATE_SCHEDULE_SUCCESS("일정 생성 성공"),
	EDIT_SCHEDULE_SUCCESS("일정 수정 성공"),
	GET_SCHEDULE_SUCCESS("일정 조회 성공"),
	DELETE_SCHEDULE_SUCCESS("일정 삭제 성공"),
	ADD_WAITING_SUCCESS("일정 확정 대기열 추가 성공"),
	CONFIRM_WAITING_SUCCESS("일정 확정 성공")
	;

	private final String message;

	ScheduleResultMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
