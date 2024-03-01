package com.tripbros.server.recommend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.domain.Locate;

public interface LocateRepository extends JpaRepository<Locate, Long> {
	Optional<Locate> findByCountryAndCity(Country country, City city);
}
