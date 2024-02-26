package com.tripbros.server.recommend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripbros.server.board.exception.BoardPermissionException;
import com.tripbros.server.common.exception.UserPermissionException;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.domain.BookmarkedPlace;
import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.recommend.domain.RecommendedLocate;
import com.tripbros.server.recommend.dto.GetBookmarkedPlaceResponseDTO;
import com.tripbros.server.recommend.dto.GetRecommendedLocateResponseDTO;
import com.tripbros.server.recommend.dto.UpdateBookmarkedPlaceRequestDTO;
import com.tripbros.server.recommend.repository.BookmarkedPlaceRepository;
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
	private final BookmarkedPlaceRepository bookmarkedPlaceRepository;
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
		Optional<BookmarkedPlace> target = bookmarkedPlaceRepository.findByUserAndPlaceId(user,
			requestDTO.placeId());

		// 북마크가 안되어 있던 경우 -> 북마크
		if(target.isEmpty()){
			Locate locate = locateRepository.findById(requestDTO.locateId()).orElseThrow(
				() -> new BoardPermissionException("유효하지 않은 지역입니다.")
			);
			BookmarkedPlace bookmarked = new BookmarkedPlace(user,
				locate,
				requestDTO.placeId(),
				requestDTO.placeName(),
				requestDTO.placeUrl(),
				requestDTO.placeImage(),
				LocalDateTime.now());
			bookmarkedPlaceRepository.save(bookmarked);

			return "북마크 완료";
		}
		// 북마크 되어 있던 경우 -> 취소
		else{
			BookmarkedPlace bookmarked = target.get();
			checkUserPermission(user, bookmarked.getUser().getId());
			bookmarkedPlaceRepository.delete(bookmarked);

			return "북마크 취소 완료";
		}

	}

	// 북마크 한 맛집 데이터 조회
	public List<GetBookmarkedPlaceResponseDTO> getBookmarkedPlace(User user){
		List<BookmarkedPlace> bookmarked = bookmarkedPlaceRepository.findByUser(user);
		List<GetBookmarkedPlaceResponseDTO> result = bookmarked.stream()
			.map(GetBookmarkedPlaceResponseDTO::toDTO)
			.toList();
		log.info("success to get bookmarked places");
		return result;
	}

	private static void checkUserPermission(User user, Long userId) {
		if (!userId.equals(user.getId()))
			throw new UserPermissionException();
	}

	public Map<Country, List<City>> getCountryWithCity() {

		List<Locate> locates = locateRepository.findAll();
		Map<Country, List<City>> countryListMap = new HashMap<>();
		locates.forEach(locate -> countryListMap.computeIfAbsent(locate.getCountry(), k -> new ArrayList<>()).add(locate.getCity()));
		return (countryListMap);
	}
}
