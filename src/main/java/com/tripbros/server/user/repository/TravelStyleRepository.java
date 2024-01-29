package com.tripbros.server.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.user.domain.TravelStyle;

public interface TravelStyleRepository extends JpaRepository<TravelStyle, Long> {
}
