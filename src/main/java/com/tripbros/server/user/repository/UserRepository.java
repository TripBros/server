package com.tripbros.server.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Boolean existsByEmail(String email);

	Boolean existsByNickname(String nickname);
}
