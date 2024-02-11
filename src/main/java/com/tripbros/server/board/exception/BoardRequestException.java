package com.tripbros.server.board.exception;

import org.springframework.validation.Errors;

public class BoardRequestException extends RuntimeException {
	public Errors errors;
	public BoardRequestException(Errors errors) {
		this.errors = errors;
	}
}
