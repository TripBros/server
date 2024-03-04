package com.tripbros.server.recommend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.recommend.domain.RecommendedLocate;

public interface RecommendedLocateRepository extends JpaRepository<RecommendedLocate, Long> {
	List<RecommendedLocate> findByQuarter1FlagAndQuarter2FlagAndQuarter3FlagAndQuarter4Flag(
		Boolean quarter1,Boolean quarter2,Boolean quarter3,Boolean quarter4);
}
