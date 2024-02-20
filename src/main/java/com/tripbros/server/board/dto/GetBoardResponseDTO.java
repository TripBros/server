package com.tripbros.server.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tripbros.server.board.domain.PreferAgeRange;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.enumerate.Purpose;
import com.tripbros.server.enumerate.Sex;
import com.tripbros.server.user.util.AgeUtil;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetBoardResponseDTO {
	private Long id;
	private String nickname;
	private String profileImage;
	private Long hit;
	private Boolean isBookmarked;
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
	private Long placeId;
	private String placeName;
	private String placeUrl;
	private Integer chatCount;
	private LocalDateTime createdAt;

	public GetBoardResponseDTO(Long id, String nickname, String profileImage, Long hit, Boolean isBookmarked,
		Integer age,
		Sex sex, String title, String content, Purpose purpose, Country country, City city, Long bookmarkedCount,
		Sex preferSex, LocalDate startDate, LocalDate endDate, Integer requiredHeadCount, Integer nowHeadCount,
		Long placeId, String placeName, String placeUrl, Integer chatCount, LocalDateTime createdAt,
		PreferAgeRange preferAgeRange) {
		this.id = id;
		this.nickname = nickname;
		this.profileImage = profileImage;
		this.hit = hit;
		this.isBookmarked = isBookmarked;
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
		this.placeUrl = placeUrl;
		this.chatCount = chatCount;
		this.createdAt = createdAt;
		this.preferAgeRange = preferAgeRange;
	}
}
