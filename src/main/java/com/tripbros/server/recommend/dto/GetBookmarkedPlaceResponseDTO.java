package com.tripbros.server.recommend.dto;

import java.time.LocalDateTime;

import com.tripbros.server.board.dto.GetBookmarkedBoardResponseDTO;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.domain.BookmarkedPlace;
import com.tripbros.server.recommend.domain.RecommendedLocate;

import jakarta.validation.constraints.NotNull;

public record GetBookmarkedPlaceResponseDTO(Country country,
											City city,
											Long placeId,
											String placeName,
											String placeUrl,
											String placeImage,
											LocalDateTime bookmarkedAt){

	public static GetBookmarkedPlaceResponseDTO toDTO(BookmarkedPlace place){
		return new GetBookmarkedPlaceResponseDTO(
			place.getLocate().getCountry(),
			place.getLocate().getCity(),
			place.getPlaceId(),
			place.getPlaceName(),
			place.getPlaceUrl(),
			place.getPlaceImage(),
			place.getBookmarkedAt());
	}
}
