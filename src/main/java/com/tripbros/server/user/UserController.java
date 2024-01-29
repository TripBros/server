package com.tripbros.server.user;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.common.exception.ValidationFailException;
import com.tripbros.server.user.dto.RegisterRequest;
import com.tripbros.server.user.service.UserRegisterService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	private final UserRegisterService registerService;

	@PostMapping("/register")
	public ResponseEntity<BaseResponse<Object>> register(@Valid @RequestBody RegisterRequest request, Errors errors) {
		if (errors.hasErrors())
			throw new ValidationFailException(errors);
		return registerService.register(request);
	}
}
