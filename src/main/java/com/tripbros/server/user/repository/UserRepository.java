package com.tripbros.server.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tripbros.server.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Boolean existsByEmail(String email);

	Boolean existsByNickname(String nickname);


	Optional<User> findByEmail(String email);

	@Query("select u from User u join fetch u.travelStyle where u.id = :id")
	User findByIdWithTravelStyle(Long id);
}
