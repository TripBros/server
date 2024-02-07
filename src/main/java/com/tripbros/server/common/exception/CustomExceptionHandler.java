package com.tripbros.server.common.exception;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.tripbros.server.schedule.exception.SchedulePermissionException;
import com.tripbros.server.schedule.exception.ScheduleRequestException;
import com.tripbros.server.security.UnauthorizedAccessException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

	@ExceptionHandler({UnauthorizedAccessException.class, AuthenticationException.class})
	public ResponseEntity<BaseResponse<Object>> handler(RuntimeException e){
		return ResponseEntity.badRequest()
				.body(new BaseResponse<>(false, HttpStatus.BAD_REQUEST, e.getMessage(), null));
	}

	@ExceptionHandler(ScheduleRequestException.class)
	public ResponseEntity<BaseResponse<Object>> handler(ScheduleRequestException e){
		ArrayList<String> messages = e.errors.getFieldErrors()
			.stream()
			.map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
			.collect(Collectors.toCollection(ArrayList::new));
		return ResponseEntity.badRequest()
			.body(new BaseResponse<>(false, HttpStatus.BAD_REQUEST, "잘못된 입력입니다.", messages));
	}

	@ExceptionHandler(SchedulePermissionException.class)
	public ResponseEntity<BaseResponse<Object>> handler(SchedulePermissionException e){
		return ResponseEntity.badRequest()
			.body(new BaseResponse<>(false, HttpStatus.BAD_REQUEST, e.getMessage(), null));
	}

	@ExceptionHandler(UserPermissionException.class)
	public ResponseEntity<BaseResponse<Object>> handler(UserPermissionException e){
		StackTraceElement[] stackTrace = e.getStackTrace();
		String className = stackTrace[0].getClassName();
		String methodName = stackTrace[0].getMethodName();
		String message = "예외 발생 위치: "+className+"."+methodName;

		return ResponseEntity.badRequest()
			.body(new BaseResponse<>(false, HttpStatus.BAD_REQUEST, "사용자 권한이 유효하지 않습니다.", message));
	}

}
