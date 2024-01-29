package com.tripbros.server.common.exception;

import org.springframework.validation.Errors;

public class ValidationFailException extends RuntimeException{
	Errors errors;

	public ValidationFailException(Errors errors) {
		this.errors = errors;
	}
}
