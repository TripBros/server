package com.tripbros.server.recommend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.recommend.dto.GetRecommendedLocateResponseDTO;
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
	public ResponseEntity<BaseResponse<List<GetRecommendedLocateResponseDTO>>> getAllRecommendedLocate(@AuthUser SecurityUser user){
		List<GetRecommendedLocateResponseDTO> result = recommendService.getAllRecommendLocate();
		BaseResponse<List<GetRecommendedLocateResponseDTO>> response = new BaseResponse<>(true, HttpStatus.OK,
			RecommendResultMessage.GET_ALL_RECOMMEND_LOCATES_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/bookmark")
	@Operation(summary = "추천 맛집 북마크 반영")
	public ResponseEntity<BaseResponse<Object>> updateBookmarkPlace(@AuthUser SecurityUser user,
		@RequestBody @Valid UpdateBookmarkedPlaceRequestDTO requestDTO, Errors errors){
		if(errors.hasErrors())
			throw new ScheduleRequestException(errors); // TODO : 에러 새로 만들기

		String result = recommendService.updateBookmarkPlace(user.getUser(), requestDTO);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			RecommendResultMessage.UPDATE_BOOKMARK_PLACE_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}

}
