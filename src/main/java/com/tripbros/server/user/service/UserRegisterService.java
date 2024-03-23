package com.tripbros.server.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tripbros.server.enumerate.Role;
import com.tripbros.server.security.SecurityExceptionMessage;
import com.tripbros.server.security.UnauthorizedAccessException;
import com.tripbros.server.user.domain.TravelStyle;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.user.dto.EditUserInfoRequest;
import com.tripbros.server.user.dto.RegisterRequest;
import com.tripbros.server.user.dto.UserInfoResponse;
import com.tripbros.server.user.exception.RegisterException;
import com.tripbros.server.user.exception.UserExceptionMessage;
import com.tripbros.server.user.repository.TravelStyleRepository;
import com.tripbros.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserRegisterService {

	private final UserRepository userRepository;
	private final TravelStyleRepository styleRepository;
	private final PasswordEncoder passwordEncoder;
	private final ImageService imageService;

	public User register(RegisterRequest request, MultipartFile image) {
		validateRequest(request);
		User user = request.toEntity(passwordEncoder.encode(request.password()),
			imageService.uploadImageAndGetName(image), saveTravelStyle(request),
			Role.ROLE_USER);
		return userRepository.save(user);
	}

	private TravelStyle saveTravelStyle(RegisterRequest request) {
		TravelStyle style = TravelStyle.builder()
			.leisurePreferFlag(request.leisurePreferFlag())
			.adventurePreferFlag(request.adventurePreferFlag())
			.photoPreferFlag(request.photoPreferFlag())
			.planPreferFlag(request.planPreferFlag())
			.vehiclePreferFlag(request.vehiclePreferFlag())
			.build();
		return styleRepository.save(style);
	}

	//변경 감지 되나요
	public User editInfo(EditUserInfoRequest request, MultipartFile image, User detachedUser) {
		User user = userRepository.findByIdWithTravelStyle(detachedUser.getId());
		if (request.password() != null && !request.password().isBlank())
			user.editPassword(passwordEncoder.encode(request.password()));
		if (request.nickname() != null && !request.nickname().isBlank()){
			checkNicknameDuplication(request.nickname());
			user.editNickname(request.nickname());
		}
		if (image != null) {
			imageService.deleteImage(user.getProfileImage());
			user.editProfileImage(imageService.uploadImageAndGetName(image));
		}
		user.getTravelStyle()
			.editStyle(request.leisurePreferFlag(), request.planPreferFlag(), request.adventurePreferFlag(),
				request.vehiclePreferFlag(), request.photoPreferFlag());
		return user;
	}

	private void validateRequest(RegisterRequest request) {
		checkEmailDuplication(request.email());
		checkNicknameDuplication(request.nickname());
		//TODO : password-rule체크 ?
	}

	public void checkEmailDuplication(String email){
		log.info("email : {}", email);
		if (userRepository.existsByEmail(email))
			throw new RegisterException(UserExceptionMessage.EMAIL_ALREADY_EXIST.getMessage());
	}

	public void checkNicknameDuplication(String nickname){
		if (userRepository.existsByNickname(nickname))
			throw new RegisterException(UserExceptionMessage.NICKNAME_ALREADY_EXIST.getMessage());
	}

	public void deleteUser(User user, String password) {
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new UnauthorizedAccessException(SecurityExceptionMessage.UNAUTHORIZED.getMessage());
		}
		imageService.deleteImage(user.getProfileImage());
		userRepository.delete(user);
	}

	public UserInfoResponse getUserInfoWithoutPassword(User user) {
		User result = userRepository.findByIdWithTravelStyle(user.getId());
		return UserInfoResponse.from(result);
	}



}
