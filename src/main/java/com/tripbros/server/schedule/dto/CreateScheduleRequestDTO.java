package com.tripbros.server.schedule.dto;

import java.time.LocalDate;

import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.recommend.repository.LocateRepository;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.exception.SchedulePermissionException;
import com.tripbros.server.user.domain.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateScheduleRequestDTO(@NotBlank(message = "일정 제목은 필수 입력 값입니다.") String title,
									   @NotNull(message = "일정 국가는 필수 입력 값입니다.") Country country,
									   @NotNull(message = "일정 지역은 필수 입력 값입니다.") City city,
									   Long placeId,
									   String placeName,
									   String placeUrl,
									   @NotNull(message = "시작 날짜는 필수 입력 값입니다.") LocalDate startDate,
									   @NotNull(message = "끝 날짜는 필수 입력 값입니다.") LocalDate endDate,
									   String memo
						  ){

	public Schedule toEntity(User user, String locateImage, LocateRepository locateRepository){
		Locate locate = locateRepository.findByCountryAndCity(country, city)
			.orElseThrow(() -> new SchedulePermissionException("존재하지 않는 국가/도시 조합입니다."));

		return Schedule.builder()
			.user(user)
			.title(title)
			.locate(locate)
			.locateImage(locateImage)
			.placeId(placeId)
			.placeName(placeName)
			.placeUrl(placeUrl)
			.startDate(startDate)
			.endDate(endDate)
			.hostFlag(true)
			.memo(memo)
			.build();
	}
}
