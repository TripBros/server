package com.tripbros.server.board.exception;

import org.springframework.validation.Errors;

public class BoardrequestException extends RuntimeException {
	public Errors errors;
	public BoardrequestException(Errors errors) {
		this.errors = errors;
	}
}
