package com.tripbros.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TravelStyle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private boolean leisurePreferFlag;
	private boolean planPreferFlag;
	private boolean adventurePreferFlag;
	private boolean vehiclePreferFlag;
	private boolean photoPreferFlag;

	@Builder
	public TravelStyle(boolean leisurePreferFlag, boolean planPreferFlag, boolean adventurePreferFlag,
		boolean vehiclePreferFlag, boolean photoPreferFlag) {
		this.leisurePreferFlag = leisurePreferFlag;
		this.planPreferFlag = planPreferFlag;
		this.adventurePreferFlag = adventurePreferFlag;
		this.vehiclePreferFlag = vehiclePreferFlag;
		this.photoPreferFlag = photoPreferFlag;
	}
}
