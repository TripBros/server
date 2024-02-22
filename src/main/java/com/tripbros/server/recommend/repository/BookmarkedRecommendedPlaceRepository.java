package com.tripbros.server.recommend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.recommend.domain.BookmarkedRecommendedPlace;
import com.tripbros.server.user.domain.User;

public interface BookmarkedRecommendedPlaceRepository extends JpaRepository<BookmarkedRecommendedPlace, Long> {
	Optional<BookmarkedRecommendedPlace> findByUserAndPlaceId(User user, Long placeId);
}
