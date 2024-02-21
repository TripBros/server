package com.tripbros.server.board.domain;

import com.tripbros.server.board.dto.EditBoardRequestDTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class PreferAgeRange {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private boolean twentiesFlag;
	private boolean thirtiesFlag;
	private boolean fortiesFlag;
	private boolean fiftiesFlag;
	private boolean sixtiesAboveFlag;
	private boolean unrelatedFlag;

	@Builder
	public PreferAgeRange(boolean twentiesFlag, boolean thirtiesFlag, boolean fortiesFlag, boolean fiftiesFlag,
		boolean sixtiesAboveFlag, boolean unrelatedFlag) {
		this.twentiesFlag = twentiesFlag;
		this.thirtiesFlag = thirtiesFlag;
		this.fortiesFlag = fortiesFlag;
		this.fiftiesFlag = fiftiesFlag;
		this.sixtiesAboveFlag = sixtiesAboveFlag;
		this.unrelatedFlag = unrelatedFlag;
	}

	public PreferAgeRange editPreferAgeRange(EditBoardRequestDTO editBoardRequestDTO){
		this.twentiesFlag = editBoardRequestDTO.twentiesFlag();
		this.thirtiesFlag = editBoardRequestDTO.thirtiesFlag();
		this.fortiesFlag = editBoardRequestDTO.fortiesFlag();
		this.fiftiesFlag = editBoardRequestDTO.fiftiesFlag();
		this.sixtiesAboveFlag = editBoardRequestDTO.sixtiesAboveFlag();
		this.unrelatedFlag = editBoardRequestDTO.unrelatedFlag();

		return this;
	}
}
