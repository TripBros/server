package com.tripbros.server.common.dto;

import org.springframework.http.HttpStatus;

/**
 *
 * @param success
 * @param statusCode
 * @param message
 * @param data
 * @param <T>
 */
public record BaseResponse<T> (boolean success,
							   HttpStatus statusCode,
							   String message,
							   T data) {
}
