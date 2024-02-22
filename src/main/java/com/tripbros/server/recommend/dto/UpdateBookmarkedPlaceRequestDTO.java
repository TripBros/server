package com.tripbros.server.recommend.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateBookmarkedPlaceRequestDTO(@NotNull(message = "지역 id는 필수 값입니다.") Long locateId,
											  @NotNull(message = "장소 id는 필수 값입니다.") Long placeId,
											  String placeName,
											  String placeUrl,
											  String placeImage) {
}
