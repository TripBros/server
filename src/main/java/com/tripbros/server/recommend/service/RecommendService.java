package com.tripbros.server.recommend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tripbros.server.recommend.domain.RecommendedLocate;
import com.tripbros.server.recommend.dto.GetRecommendedLocateResponseDTO;
import com.tripbros.server.recommend.repository.BookmarkedRecommendedPlaceRepository;
import com.tripbros.server.recommend.repository.RecommendedLocateRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

	private final RecommendedLocateRepository recommendedLocateRepository;

	// 분기 상관 없이 전체 데이터 조회
	public List<GetRecommendedLocateResponseDTO> getAllRecommendLocate(){
		List<RecommendedLocate> locates = recommendedLocateRepository.findAll();
		List<GetRecommendedLocateResponseDTO> result = locates.stream().map(GetRecommendedLocateResponseDTO::toDTO).toList();

		log.info("success to recommend all locates");
		return result;
	}

	// 맛집 데이터 북마크

	// 북마크 한 맛집 데이터 조회

}
