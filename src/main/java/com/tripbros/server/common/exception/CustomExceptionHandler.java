package com.tripbros.server.common.exception;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.user.exception.RegisterException;

@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(RegisterException.class)
	public ResponseEntity<BaseResponse<Object>> handler(RegisterException e){
		return ResponseEntity.badRequest()
			.body(new BaseResponse<>(false, HttpStatus.BAD_REQUEST, e.getMessage(), null));
	}

	@ExceptionHandler(ValidationFailException.class)
	public ResponseEntity<BaseResponse<Object>> handler(ValidationFailException e){
		ArrayList<String> messages = e.errors.getFieldErrors().stream().map(error -> error.getField()+" : "+error.getDefaultMessage()).collect(
			Collectors.toCollection(ArrayList::new));
		return ResponseEntity.badRequest()
			.body(new BaseResponse<>(false, HttpStatus.BAD_REQUEST, "검증에 실패하였습니다", messages));
	}
}
