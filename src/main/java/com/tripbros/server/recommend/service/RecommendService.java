package com.tripbros.server.recommend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
import com.tripbros.server.recommend.util.LocateUtil;
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
	private String googleApiKey;

	@Value("${pixabay.api.key}")
	private String pixabayApiKey;

	public GetRecommendedLocateResponseDTO getRandomRecommendedLocate(Integer quarter){
		List<RecommendedLocate> locates;

		if(quarter == 1)
			locates = recommendedLocateRepository.findAllByQuarter1FlagTrue();
		else if (quarter == 2)
			locates = recommendedLocateRepository.findAllByQuarter2FlagTrue();
		else if (quarter == 3)
			locates = recommendedLocateRepository.findAllByQuarter3FlagTrue();
		else if (quarter == 4)
			locates = recommendedLocateRepository.findAllByQuarter4FlagTrue();
		else throw new RuntimeException("잘못 된 분기 선택입니다. (1,2,3,4 중 선택 가능) ");

		Collections.shuffle(locates);
		RecommendedLocate randomLocate = locates.get(0);

		String image = LocateUtil.getLocateImage(
			pixabayApiKey,
			randomLocate.getLocate().getCountry(),
			randomLocate.getLocate().getCity());

		return GetRecommendedLocateResponseDTO.toDTO(randomLocate, image);
	}

	public List<GetRecommendedPlacesResponseDTO> getAllRecommendedPlace(Country country, City city){
		String searchKeyword = country.toString().concat(" ").concat(city.toString()).concat(" 맛집");

		GeoApiContext context = new GeoApiContext.Builder()
			.apiKey(googleApiKey)
			.build();

		try{
			PlacesSearchResponse response = PlacesApi.textSearchQuery(context, searchKeyword).await();

			if (response.results != null && response.results.length > 0) {
				List<CompletableFuture<GetRecommendedPlacesResponseDTO>> results = new ArrayList<>();
				for(PlacesSearchResult res : response.results)
					results.add(CompletableFuture.supplyAsync(() -> getPlaceDetails(res.placeId)));

				CompletableFuture.allOf(results.toArray(new CompletableFuture[0])).join(); // 비동기 작업이 모두 끝날 때까지 대기

				return results.stream().map(CompletableFuture::join).filter(Objects::nonNull).toList();
			}
			else{
				log.info("No place found : "+searchKeyword);
				return null;
			}
		}
		catch (Exception e){
			throw new GoogleApiException();
		}
	}

	private GetRecommendedPlacesResponseDTO getPlaceDetails(String placeId){
		GeoApiContext context = new GeoApiContext.Builder()
			.apiKey(googleApiKey)
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
					.concat(googleApiKey);
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
