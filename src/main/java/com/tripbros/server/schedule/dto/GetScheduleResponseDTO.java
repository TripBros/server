package com.tripbros.server.schedule.dto;

import java.time.LocalDate;

import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.schedule.domain.Schedule;

import jakarta.validation.constraints.NotNull;

public record GetScheduleResponseDTO(Long id,
									 @NotNull(message = "일정 제목은 필수 입력 값입니다.") String title,
									 @NotNull(message = "일정 지역은 필수 입력 값입니다.") Locate locate,
									 @NotNull(message = "시작 날짜는 필수 입력 값입니다.") LocalDate startDate,
									 @NotNull(message = "끝 날짜는 필수 입력 값입니다.") LocalDate endDate,
									 String memo){
	public static GetScheduleResponseDTO toDTO(Schedule schedule){
		return new GetScheduleResponseDTO(
			schedule.getId(),
			schedule.getTitle(),
			schedule.getLocate(),
			schedule.getStartDate(),
			schedule.getEndDate(),
			schedule.getMemo()
		);
	}
}
