package com.tripbros.server.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.common.exception.ValidationFailException;
import com.tripbros.server.security.AuthUser;
import com.tripbros.server.security.JwtDTO;
import com.tripbros.server.security.SecurityUser;
import com.tripbros.server.user.dto.EditUserInfoRequest;
import com.tripbros.server.user.dto.RegisterRequest;
import com.tripbros.server.user.dto.SignInRequest;
import com.tripbros.server.user.enumerate.UserResultMessage;
import com.tripbros.server.user.service.UserRegisterService;
import com.tripbros.server.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "유저 컨트롤러", description = "유저에 관한 행위의 API입니다.")
public class UserController {
	private final UserRegisterService registerService;
	private final UserService userService;

	@PostMapping(value = "/register", consumes = {
		MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "회원 가입")
	public ResponseEntity<BaseResponse<Object>> register(
		@Valid @RequestPart RegisterRequest registerRequest,
		Errors errors,
		@RequestPart MultipartFile image) {
		if (errors.hasErrors())
			throw new ValidationFailException(errors);
		registerService.register(registerRequest, image);
		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			UserResultMessage.REGISTER_SUCCESS.getMessage(), null);
		return ResponseEntity.ok().body(response);
	}

	@PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	@Operation(summary = "회원 정보 수정")
	public ResponseEntity<BaseResponse<Object>> editInfo(@Valid @RequestPart EditUserInfoRequest editUserInfoRequest,
		Errors errors,
		@RequestPart(required = false) MultipartFile image,
		@AuthUser SecurityUser user) {
		if (errors.hasErrors())
			throw new ValidationFailException(errors);
		registerService.editInfo(editUserInfoRequest, image, user.getUser());
		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			UserResultMessage.EDIT_USERINFO_SUCCESS.getMessage(), null);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/email-check")
	@Operation(summary = "이메일 중복 검사")
	public ResponseEntity<BaseResponse<Object>> checkEmail(@RequestParam String email){
		registerService.checkEmailDuplication(email); //중복시 Exception Throw
		return ResponseEntity.ok().body(new BaseResponse<>(true, HttpStatus.OK, null, null));
	}

	@GetMapping("/nickname-check")
	@Operation(summary = "닉네임 중복 검사")
	public ResponseEntity<BaseResponse<Object>> checkNickname(@RequestParam String nickname){
		registerService.checkEmailDuplication(nickname); //중복시 Exception Throw
		return ResponseEntity.ok().body(new BaseResponse<>(true, HttpStatus.OK, null, null));
	}

	@PostMapping("/sign-in")
	@Operation(summary = "로그인")
	public ResponseEntity<BaseResponse<JwtDTO>> signIn(@RequestBody SignInRequest request) {
		JwtDTO token = userService.signIn(request);
		return ResponseEntity.ok().body(new BaseResponse<>(true, HttpStatus.OK, null, token));
	}

	@DeleteMapping
	@Operation(summary = "회원 탈퇴")
	public ResponseEntity<BaseResponse<Object>> resignUser(@AuthUser SecurityUser user ,@RequestParam String password){
		registerService.deleteUser(user.getUser(), password);
		return ResponseEntity.ok().body(new BaseResponse<>(true, HttpStatus.OK, null, null));
	}

	@GetMapping("/test") // 테스트 온리 컨트롤러
	@Operation(summary = "백엔드 자체 테스트 전용 API 입니다.")
	public String  test(@AuthUser SecurityUser user) {
		return user.getUser().getProfileImage();
	}
}
