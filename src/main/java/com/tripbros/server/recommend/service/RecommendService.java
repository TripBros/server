package com.tripbros.server.recommend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripbros.server.board.exception.BoardPermissionException;
import com.tripbros.server.common.exception.UserPermissionException;
import com.tripbros.server.recommend.domain.BookmarkedRecommendedPlace;
import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.recommend.domain.RecommendedLocate;
import com.tripbros.server.recommend.dto.GetRecommendedLocateResponseDTO;
import com.tripbros.server.recommend.dto.UpdateBookmarkedPlaceRequestDTO;
import com.tripbros.server.recommend.repository.BookmarkedRecommendedPlaceRepository;
import com.tripbros.server.recommend.repository.LocateRepository;
import com.tripbros.server.recommend.repository.RecommendedLocateRepository;
import com.tripbros.server.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RecommendService {

	private final RecommendedLocateRepository recommendedLocateRepository;
	private final BookmarkedRecommendedPlaceRepository bookmarkedRecommendedPlaceRepository;
	private final LocateRepository locateRepository;

	// 분기 상관 없이 전체 데이터 조회
	public List<GetRecommendedLocateResponseDTO> getAllRecommendLocate(){
		List<RecommendedLocate> locates = recommendedLocateRepository.findAll();
		List<GetRecommendedLocateResponseDTO> result = locates.stream().map(GetRecommendedLocateResponseDTO::toDTO).toList();

		log.info("success to recommend all locates");
		return result;
	}

	// 맛집 데이터 북마크
	public String updateBookmarkPlace(User user, UpdateBookmarkedPlaceRequestDTO requestDTO){
		Optional<BookmarkedRecommendedPlace> target = bookmarkedRecommendedPlaceRepository.findByUserAndPlaceId(user,
			requestDTO.placeId());

		// 북마크가 안되어 있던 경우 -> 북마크
		if(target.isEmpty()){
			Locate locate = locateRepository.findById(requestDTO.locateId()).orElseThrow(
				() -> new BoardPermissionException("유효하지 않은 지역입니다.")
			);
			BookmarkedRecommendedPlace bookmarked = new BookmarkedRecommendedPlace(user,
				locate,
				requestDTO.placeId(),
				requestDTO.placeName(),
				requestDTO.placeUrl(),
				requestDTO.placeImage(),
				LocalDateTime.now());
			bookmarkedRecommendedPlaceRepository.save(bookmarked);

			return "북마크 완료";
		}
		// 북마크 되어 있던 경우 -> 취소
		else{
			BookmarkedRecommendedPlace bookmarked = target.get();
			checkUserPermission(user, bookmarked.getUser().getId());
			bookmarkedRecommendedPlaceRepository.delete(bookmarked);

			return "북마크 취소 완료";
		}

	}

	// 북마크 한 맛집 데이터 조회


	private static void checkUserPermission(User user, Long userId) {
		if (!userId.equals(user.getId()))
			throw new UserPermissionException();
	}
}
