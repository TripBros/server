package com.tripbros.server.recommend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.recommend.dto.GetRecommendedLocateResponseDTO;
import com.tripbros.server.recommend.enumerate.RecommendResultMessage;
import com.tripbros.server.recommend.service.RecommendService;
import com.tripbros.server.security.AuthUser;
import com.tripbros.server.security.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

}
