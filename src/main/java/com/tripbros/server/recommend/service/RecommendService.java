package com.tripbros.server.recommend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.tripbros.server.board.exception.BoardPermissionException;
import com.tripbros.server.common.exception.UserPermissionException;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.domain.BookmarkedPlace;
import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.recommend.domain.RecommendedLocate;
import com.tripbros.server.recommend.dto.GetBookmarkedPlaceResponseDTO;
import com.tripbros.server.recommend.dto.GetRecommendedLocateResponseDTO;
import com.tripbros.server.recommend.dto.GetRecommendedPlacesResponseDTO;
import com.tripbros.server.recommend.dto.UpdateBookmarkedPlaceRequestDTO;
import com.tripbros.server.recommend.exception.GoogleApiException;
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

	@Value("${google.api.key}")
	private String apiKey;

	// 분기 상관 없이 전체 데이터 조회
	public List<GetRecommendedLocateResponseDTO> getAllRecommendLocate(){
		List<RecommendedLocate> locates = recommendedLocateRepository.findAll();
		List<GetRecommendedLocateResponseDTO> result = locates.stream().map(GetRecommendedLocateResponseDTO::toDTO).toList();

		log.info("success to recommend all locates");
		return result;
	}

	public List<GetRecommendedPlacesResponseDTO> getAllRecommendedPlace(String country, String city){
		List<GetRecommendedPlacesResponseDTO> result = new ArrayList<>();

		String searchKeyword = country.concat(" ").concat(city).concat(" 맛집");

		GeoApiContext context = new GeoApiContext.Builder()
			.apiKey(apiKey)
			.build();

		try{
			PlacesSearchResponse response = PlacesApi.textSearchQuery(context, searchKeyword).await();

			if(response.results != null && response.results.length > 0){
				for(PlacesSearchResult res : response.results){
					GetRecommendedPlacesResponseDTO place = getPlaceDetails(res.placeId);
					if(place != null) result.add(place);
				}
			}
			else log.info("No place found : "+searchKeyword);
		}
		catch (Exception e){
			throw new GoogleApiException();
		}

		return result;
	}

	private GetRecommendedPlacesResponseDTO getPlaceDetails(String placeId){
		GeoApiContext context = new GeoApiContext.Builder()
			.apiKey(apiKey)
			.build();
		String photoUrl;

		try{
			PlaceDetails details = PlacesApi.placeDetails(context, placeId).language("ko").await();
			if(details.photos == null)
				return null;
			else {
				photoUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=500&photoreference="
					.concat(details.photos[1].photoReference)
					.concat("&key=")
					.concat(apiKey);
			}

			return new GetRecommendedPlacesResponseDTO(
				details.placeId,
				details.name,
				details.url.toString(),
				details.rating,
				photoUrl
			);
		}catch (Exception e){
			throw new GoogleApiException();
		}
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
				requestDTO.placeRating(),
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
