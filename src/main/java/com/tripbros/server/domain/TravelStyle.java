package com.tripbros.server.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class TravelStyle {
	@Id
	@OneToOne(mappedBy = "user")
	@JoinColumn(name = "user_id")
	private User user;

	private boolean leisurePreferFlag;
	private boolean planPreferFlag;
	private boolean adventurePreferFlag;
	private boolean vehiclePreferFlag;
	private boolean photoPreferFlag;

	@Builder
	public TravelStyle(User user, boolean leisurePreferFlag, boolean planPreferFlag, boolean adventurePreferFlag,
		boolean vehiclePreferFlag, boolean photoPreferFlag) {
		this.user = user;
		this.leisurePreferFlag = leisurePreferFlag;
		this.planPreferFlag = planPreferFlag;
		this.adventurePreferFlag = adventurePreferFlag;
		this.vehiclePreferFlag = vehiclePreferFlag;
		this.photoPreferFlag = photoPreferFlag;
	}
}
