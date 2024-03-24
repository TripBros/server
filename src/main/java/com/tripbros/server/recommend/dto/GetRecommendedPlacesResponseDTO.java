package com.tripbros.server.recommend.dto;

public record GetRecommendedPlacesResponseDTO(String placeId,
											  String placeName,
											  String placeUrl,
											  Float placeRating,
											  String placeImage,
											  Boolean isBookmarked) {
}
