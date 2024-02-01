package com.tripbros.server.user.domain;

import org.hibernate.annotations.CollectionId;

import com.tripbros.server.enumerate.Role;
import com.tripbros.server.enumerate.Sex;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
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

	@Column(unique = true)
	private String email;
	private String password; //BCryptEncoder 사용
	private String nickname;
	private Long age;

	@Lob
	private byte[] profileImage;

	@Enumerated(EnumType.STRING)
	private Sex sex;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "travel_style_id")
	private TravelStyle travelStyle;

	@Enumerated(EnumType.STRING)
	private Role role;

	@Builder
	public User(String email, String password, String nickname, Long age, byte[] profileImage, Sex sex, TravelStyle travelStyle, Role role) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.age = age;
		this.profileImage = profileImage;
		this.sex = sex;
		this.role = role;
		this.travelStyle = travelStyle;
	}
}
