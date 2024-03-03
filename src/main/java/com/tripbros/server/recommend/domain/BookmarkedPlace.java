package com.tripbros.server.recommend.domain;

import java.time.LocalDateTime;

import com.tripbros.server.user.domain.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class BookmarkedPlace {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "locate_id")
	private Locate locate;

	private String placeId;
	private String placeName;
	private String placeUrl;
	private Float placeRating;
	private String placeImage;

	private LocalDateTime bookmarkedAt;

	@Builder
	public BookmarkedPlace(User user, Locate locate, String placeId, String placeName, String placeUrl,
		Float placeRating,
		String placeImage, LocalDateTime bookmarkedAt) {
		this.user = user;
		this.locate = locate;
		this.placeId = placeId;
		this.placeName = placeName;
		this.placeUrl = placeUrl;
		this.placeRating = placeRating;
		this.placeImage = placeImage;
		this.bookmarkedAt = bookmarkedAt;
	}
}
