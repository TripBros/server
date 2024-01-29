package com.tripbros.server.common.dto;

import org.springframework.http.HttpStatus;

public record BaseResponse<T> (boolean success,
							   HttpStatus statusCode,
							   String message,
							   T data) {
}
