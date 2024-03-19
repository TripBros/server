package com.tripbros.server.recommend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tripbros.server.recommend.domain.RecommendedLocate;

public interface RecommendedLocateRepository extends JpaRepository<RecommendedLocate, Long> {

	List<RecommendedLocate> findAllByQuarter1FlagTrue();
	List<RecommendedLocate> findAllByQuarter2FlagTrue();
	List<RecommendedLocate> findAllByQuarter3FlagTrue();
	List<RecommendedLocate> findAllByQuarter4FlagTrue();

}
