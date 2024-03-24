package com.tripbros.server.recommend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.recommend.domain.BookmarkedPlace;
import com.tripbros.server.user.domain.User;

public interface BookmarkedPlaceRepository extends JpaRepository<BookmarkedPlace, Long> {
	Boolean existsByUserAndPlaceId(User user, String placeId);
	Optional<BookmarkedPlace> findByUserAndPlaceId(User user, String placeId);
	List<BookmarkedPlace> findByUser(User user);
}
