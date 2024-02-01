package com.tripbros.server.schedule.enumerate;

public enum ScheduleResultMessage {
	SAVE_SCHEDULE_SUCCESS("일정 저장 성공"),
	DELETE_SCHEDULE_SUCCESS("일정 삭제 성공");

	final String message;

	ScheduleResultMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
