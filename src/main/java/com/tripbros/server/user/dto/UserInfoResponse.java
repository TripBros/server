package com.tripbros.server.user.dto;

import com.tripbros.server.enumerate.Sex;
import com.tripbros.server.user.domain.TravelStyle;
import com.tripbros.server.user.domain.User;

import lombok.Builder;

@Builder
public record UserInfoResponse(
	String email,
	String nickname,
	Integer age,
	String profileImage,
	Sex sex,
	TravelStyle travelStyle
) {

	public static UserInfoResponse from(User user) {
		return UserInfoResponse.builder()
			.email(user.getEmail())
			.nickname(user.getNickname())
			.age(user.getAge())
			.profileImage(user.getProfileImage())
			.sex(user.getSex())
			.travelStyle(user.getTravelStyle())
			.build();
	}
}
