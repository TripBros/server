package com.tripbros.server.recommend.dto;

import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.domain.RecommendedLocate;

public record GetRecommendedLocateResponseDTO(Long id,
											  Country country,
											  City city,
											  String image,
											  Boolean quarter1,
											  Boolean quarter2,
											  Boolean quarter3,
											  Boolean quarter4) {

	public static GetRecommendedLocateResponseDTO toDTO(RecommendedLocate recommendedLocate){
		return new GetRecommendedLocateResponseDTO(
			recommendedLocate.getId(),
			recommendedLocate.getLocate().getCountry(),
			recommendedLocate.getLocate().getCity(),
			recommendedLocate.getImage(),
			recommendedLocate.isQuarter1Flag(),
			recommendedLocate.isQuarter2Flag(),
			recommendedLocate.isQuarter3Flag(),
			recommendedLocate.isQuarter4Flag()
		);
	}
}
