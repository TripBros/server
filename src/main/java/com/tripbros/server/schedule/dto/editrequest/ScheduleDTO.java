package com.tripbros.server.schedule.dto.editrequest;

import java.time.LocalDate;

import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.schedule.domain.Schedule;

import lombok.Builder;

@Builder
public record ScheduleDTO(String title,
						  Country country,
						  City city,
						  String placeName,
						  LocalDate startDate,
						 LocalDate endDate
) {
	public static ScheduleDTO from(Schedule schedule) {
		return ScheduleDTO.builder()
			.title(schedule.getTitle())
			.country(schedule.getLocate().getCountry())
			.city(schedule.getLocate().getCity())
			.placeName(schedule.getPlaceName())
			.startDate(schedule.getStartDate())
			.endDate(schedule.getEndDate())
			.build();
	}
}
