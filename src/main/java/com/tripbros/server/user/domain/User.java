package com.tripbros.server.user.domain;

import com.tripbros.server.enumerate.Role;
import com.tripbros.server.enumerate.Sex;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	private String nickname;
	private Long age;

	@Lob
	private byte[] profileImage;

	@Enumerated(EnumType.STRING)
	private Sex sex;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Builder
	public User(String email, String nickname, Long age, byte[] profileImage, Sex sex, Role role) {
		this.email = email;
		this.nickname = nickname;
		this.age = age;
		this.profileImage = profileImage;
		this.sex = sex;
		this.role = role;
	}
}
