package com.tripbros.server.schedule.exception;

import org.springframework.validation.Errors;

public class ScheduleRequestException extends RuntimeException{
	public Errors errors;

	public ScheduleRequestException(Errors errors) {
		this.errors = errors;
	}
}
