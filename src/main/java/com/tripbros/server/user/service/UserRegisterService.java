package com.tripbros.server.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.enumerate.Role;
import com.tripbros.server.user.domain.TravelStyle;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.user.dto.RegisterRequest;
import com.tripbros.server.user.enumerate.UserResultMessage;
import com.tripbros.server.user.exception.RegisterException;
import com.tripbros.server.user.exception.UserExceptionMessage;
import com.tripbros.server.user.repository.TravelStyleRepository;
import com.tripbros.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegisterService {

	private final UserRepository userRepository;
	private final TravelStyleRepository styleRepository;
	private final PasswordEncoder passwordEncoder;

	public ResponseEntity<BaseResponse<Object>> register(RegisterRequest request) {
		validateRequest(request);
		User user = request.toEntity(passwordEncoder.encode(request.password()), saveTravelStyle(request),
			Role.ROLE_USER);
		userRepository.save(user);
		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			UserResultMessage.REGISTER_SUCCESS.getMessage(), null);
		return ResponseEntity.ok().body(response);
	}

	private TravelStyle saveTravelStyle(RegisterRequest request) {
		TravelStyle style = TravelStyle.builder()
			.leisurePreferFlag(request.leisurely_flag())
			.adventurePreferFlag(request.adventurous_flag())
			.photoPreferFlag(request.photo_preference_flag())
			.planPreferFlag(request.planner_flag())
			.vehiclePreferFlag(request.vehicle_travel_flag())
			.build();
		return styleRepository.save(style);
	}

	private void validateRequest(RegisterRequest request) {
		if (userRepository.existsByEmail(request.email()))
			throw new RegisterException(UserExceptionMessage.EMAIL_ALREADY_EXIST.getMessage());
		else if (userRepository.existsByNickname(request.nickname()))
			throw new RegisterException(UserExceptionMessage.NICKNAME_ALREADY_EXIST.getMessage());
		//TODO : password-rule체크 ?
	}

}
