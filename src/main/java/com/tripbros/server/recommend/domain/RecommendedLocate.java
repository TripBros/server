package com.tripbros.server.recommend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RecommendedLocate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "locate_id")
	private Locate locate;

	private String image;

	private boolean quarter1Flag;
	private boolean quarter2Flag;
	private boolean quarter3Flag;
	private boolean quarter4Flag;

	@Builder
	public RecommendedLocate(Locate locate, String image,
		boolean quarter1Flag, boolean quarter2Flag, boolean quarter3Flag, boolean quarter4Flag) {
		this.locate = locate;
		this.image = image;
		this.quarter1Flag = quarter1Flag;
		this.quarter2Flag = quarter2Flag;
		this.quarter3Flag = quarter3Flag;
		this.quarter4Flag = quarter4Flag;
	}
}
