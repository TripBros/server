package com.tripbros.server.user.dto;

import com.tripbros.server.enumerate.Role;
import com.tripbros.server.enumerate.Sex;
import com.tripbros.server.user.domain.TravelStyle;
import com.tripbros.server.user.domain.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(@NotBlank(message = "이메일은 필수 항목입니다.") @Email String email,
							  @NotBlank(message = "비밀번호는 필수 항목입니다.") String password,
							  @NotBlank(message = "닉네임은 필수 항목입니다.") String nickname,
							  @NotNull(message = "나이는 필수 항목입니다.") Long age,
							  Sex sex,
							  @NotNull Boolean leisurely_flag,
							  @NotNull Boolean planner_flag,
							  @NotNull Boolean adventurous_flag,
							  @NotNull Boolean vehicle_travel_flag,
							  @NotNull Boolean photo_preference_flag,
							  String profile_image // nullable 일까요?
							  ) {

	public User toEntity(String encryptedPassword, TravelStyle travelStyle, Role role){
		return User.builder()
			.email(email)
			.password(password)
			.nickname(nickname)
			.age(age)
			.sex(sex)
			.travelStyle(travelStyle)
			.profileImage(profile_image.getBytes())
			.role(role)
			.build();
	}

}
