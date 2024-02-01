package com.tripbros.server.schedule.service;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.schedule.dto.ScheduleRequestDTO;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

	@Autowired
	private ScheduleService scheduleService;
	private ScheduleRequestDTO requestDTO;

	@BeforeEach
	void setUp() {
		requestDTO = new ScheduleRequestDTO(
			"발리여행",
			new Locate(Country.KOREA, City.SEOUL),
			LocalDate.parse("2024-01-01"),
			LocalDate.parse("2024-01-05"),
			"MEMO"
		);
	}

	@Test
	@DisplayName("일정 등록")
	void saveSchedule() {
		ResponseEntity<BaseResponse<Object>> response = scheduleService.saveSchedule(requestDTO);
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
}