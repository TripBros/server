package com.tripbros.server.recommend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.dto.GetBookmarkedPlaceResponseDTO;
import com.tripbros.server.recommend.dto.GetRecommendedLocateResponseDTO;
import com.tripbros.server.recommend.dto.GetRecommendedPlacesResponseDTO;
import com.tripbros.server.recommend.dto.UpdateBookmarkedPlaceRequestDTO;
import com.tripbros.server.recommend.enumerate.RecommendResultMessage;
import com.tripbros.server.recommend.service.RecommendService;
import com.tripbros.server.schedule.exception.ScheduleRequestException;
import com.tripbros.server.security.AuthUser;
import com.tripbros.server.security.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
@Tag(name = "추천 여행지 컨트롤러", description = "추천 여행지 관련 API")
public class RecommendController {
	private final RecommendService recommendService;

	@GetMapping
	@Operation(summary = "분기 상관 없이 모든 추천 여행지를 조회")
	public ResponseEntity<BaseResponse<List<GetRecommendedLocateResponseDTO>>> getAllRecommendedLocate() {
		List<GetRecommendedLocateResponseDTO> result = recommendService.getAllRecommendLocate();
		BaseResponse<List<GetRecommendedLocateResponseDTO>> response = new BaseResponse<>(true, HttpStatus.OK,
			RecommendResultMessage.GET_ALL_RECOMMEND_LOCATES_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/random-one")
	@Operation(summary = "선택한 분기에 적합한 추천 여행지를 랜덤으로 한 곳 조회")
	public ResponseEntity<BaseResponse<GetRecommendedLocateResponseDTO>> getRandomRecommendedLocate(@RequestParam Boolean quarter1,
		@RequestParam Boolean quarter2, @RequestParam Boolean quarter3, @RequestParam Boolean quarter4){

		GetRecommendedLocateResponseDTO result
			= recommendService.getRandomRecommendedLocate(quarter1, quarter2, quarter3, quarter4);

		BaseResponse<GetRecommendedLocateResponseDTO> response = new BaseResponse<>(true, HttpStatus.OK,
			RecommendResultMessage.GET_A_RANDOM_RECOMMEND_LOCATE_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}


	@GetMapping("/places")
	@Operation(summary = "선택한 지역에 대해 모든 추천 맛집을 조회")
	public ResponseEntity<BaseResponse<List<GetRecommendedPlacesResponseDTO>>> getAllRecommendedPlace(@RequestParam Country country, @RequestParam City city){
		List<GetRecommendedPlacesResponseDTO> result = recommendService.getAllRecommendedPlace(country, city);

		BaseResponse<List<GetRecommendedPlacesResponseDTO>> response = new BaseResponse<>(true ,HttpStatus.OK,
			RecommendResultMessage.GET_ALL_RECOMMEND_PLACES_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/bookmark")
	@Operation(summary = "추천 맛집 북마크 반영")
	public ResponseEntity<BaseResponse<Object>> updateBookmarkPlace(@AuthUser SecurityUser user,
		@RequestBody @Valid UpdateBookmarkedPlaceRequestDTO requestDTO, Errors errors) {
		if (errors.hasErrors())
			throw new ScheduleRequestException(errors); // TODO : 에러 새로 만들기

		String result = recommendService.updateBookmarkPlace(user.getUser(), requestDTO);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			RecommendResultMessage.UPDATE_BOOKMARK_PLACE_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/bookmark")
	@Operation(summary = "북마크 한 맛집 전체 조회")
	public ResponseEntity<BaseResponse<List<GetBookmarkedPlaceResponseDTO>>> getBookmarkedPlace(
		@AuthUser SecurityUser user) {
		List<GetBookmarkedPlaceResponseDTO> result = recommendService.getBookmarkedPlace(user.getUser());

		BaseResponse<List<GetBookmarkedPlaceResponseDTO>> response = new BaseResponse<>(true, HttpStatus.OK,
			RecommendResultMessage.GET_BOOKMARKED_PLACE_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/cities")
	public ResponseEntity<BaseResponse<Map<Country, List<City>>>> getCountryWithCities() {
		return ResponseEntity.ok(new BaseResponse<>(true, HttpStatus.OK, "성공", recommendService.getCountryWithCity()));
	}
}
