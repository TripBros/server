package com.tripbros.server.user.dto;

import java.time.LocalDate;

import com.tripbros.server.enumerate.Role;
import com.tripbros.server.enumerate.Sex;
import com.tripbros.server.user.domain.TravelStyle;
import com.tripbros.server.user.domain.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest( @NotBlank(message = "이메일은 필수 항목입니다.") @Email String email,
							  @NotBlank(message = "비밀번호는 필수 항목입니다.") String password,
							  @NotBlank(message = "닉네임은 필수 항목입니다.") String nickname,
							  @NotNull(message = "나이는 필수 항목입니다.") Long age, // 출생 년도임
							  Sex sex,
							  @NotNull Boolean leisurePreferFlag,
							  @NotNull Boolean planPreferFlag,
							  @NotNull Boolean adventurePreferFlag,
							  @NotNull Boolean vehiclePreferFlag,
							  @NotNull Boolean photoPreferFlag
							  ) {

	public User toEntity(String encryptedPassword, String profileImageUrl,TravelStyle travelStyle, Role role){
		return User.builder()
			.email(email)
			.password(encryptedPassword)
			.nickname(nickname)
			.age(LocalDate.now().getYear() - age + 1) //한국식 세는나이 적용
			.sex(sex)
			.travelStyle(travelStyle)
			.profileImage(profileImageUrl)
			.role(role)
			.build();
		//TODO: 이미지 널이면 기본이미지로 설정할 수 있게 해야 할 듯
	}

}
