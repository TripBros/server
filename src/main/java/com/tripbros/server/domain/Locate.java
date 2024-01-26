package com.tripbros.server.domain;

import com.tripbros.server.domain.enumerate.City;
import com.tripbros.server.domain.enumerate.Country;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Locate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private Country country;
	@Enumerated(EnumType.STRING)
	private City city;

	@Builder
	public Locate(Country country, City city) {
		this.country = country;
		this.city = city;
	}
}
