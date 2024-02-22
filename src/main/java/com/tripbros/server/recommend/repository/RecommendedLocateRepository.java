package com.tripbros.server.recommend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.recommend.domain.RecommendedLocate;

public interface RecommendedLocateRepository extends JpaRepository<RecommendedLocate, Long> {
}
