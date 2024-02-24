package com.tripbros.server.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.board.domain.PreferAgeRange;

public interface PreferAgeRangeRepository extends JpaRepository<PreferAgeRange, Long> {
}
