package com.tripbros.server.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.enumerate.Purpose;
import com.tripbros.server.enumerate.Sex;

import com.tripbros.server.board.domain.PreferAgeRange;
import com.tripbros.server.user.util.AgeUtil;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetBookmarkedBoardResponseDTO {
	private Long id;
	private String nickname;
	private String profileImage;
	private Long hit;
	private String age;
	private Sex sex;
	private String title;
	private String content;
	private Purpose purpose;
	private Country country;
	private City city;
	private Long bookmarkedCount;
	private Sex preferSex;
	private PreferAgeRange preferAgeRange;
	private LocalDate startDate;
	private LocalDate endDate;
	private Integer requiredHeadCount;
	private Integer nowHeadCount;
	private String placeId;
	private String placeName;
	private Double placeLatitude;
	private Double placeLongitude;
	private Integer chatCount;
	private LocalDateTime createdAt;

	public GetBookmarkedBoardResponseDTO(Long id, String nickname, String profileImage, Long hit, Integer age,
		Sex sex, String title, String content, Purpose purpose, Country country, City city, Long bookmarkedCount,
		Sex preferSex, LocalDate startDate, LocalDate endDate, Integer requiredHeadCount, Integer nowHeadCount,
		String placeId, String placeName, Double placeLatitude, Double placeLongitude, Integer chatCount, LocalDateTime createdAt,
		PreferAgeRange preferAgeRange) {
		this.id = id;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.hit = hit;
		this.age = AgeUtil.ageRange(age);
		this.sex = sex;
		this.title = title;
		this.content = content;
		this.purpose = purpose;
		this.country = country;
		this.city = city;
		this.bookmarkedCount = bookmarkedCount;
		this.preferSex = preferSex;
		this.startDate = startDate;
		this.endDate = endDate;
		this.requiredHeadCount = requiredHeadCount;
		this.nowHeadCount = nowHeadCount;
		this.placeId = placeId;
		this.placeName = placeName;
		this.placeLatitude = placeLatitude;
		this.placeLongitude = placeLongitude;
		this.chatCount = chatCount;
		this.createdAt = createdAt;
		this.preferAgeRange = preferAgeRange;
	}
}
