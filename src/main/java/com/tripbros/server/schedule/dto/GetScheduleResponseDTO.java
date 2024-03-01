package com.tripbros.server.schedule.dto;

import java.time.LocalDate;

import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.schedule.domain.Schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record GetScheduleResponseDTO(Long id,
									 @NotBlank(message = "일정 제목은 필수 입력 값입니다.") String title,
									 @NotNull(message = "일정 국가는 필수 입력 값입니다.") Country country,
									 @NotNull(message = "일정 지역은 필수 입력 값입니다.") City city,
									 Long placeId,
									 String placeName,
									 String placeUrl,
									 @NotNull(message = "시작 날짜는 필수 입력 값입니다.") LocalDate startDate,
									 @NotNull(message = "끝 날짜는 필수 입력 값입니다.") LocalDate endDate,
									 String memo){
	public static GetScheduleResponseDTO toDTO(Schedule schedule){ //FIXME : N+1 발생 검사
		return new GetScheduleResponseDTO(
			schedule.getId(),
			schedule.getTitle(),
			schedule.getLocate().getCountry(),
			schedule.getLocate().getCity(),
			schedule.getPlaceId(),
			schedule.getPlaceName(),
			schedule.getPlaceUrl(),
			schedule.getStartDate(),
			schedule.getEndDate(),
			schedule.getMemo()
		);
	}
}
