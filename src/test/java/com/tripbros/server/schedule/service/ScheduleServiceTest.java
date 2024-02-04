package com.tripbros.server.schedule.service;

import java.time.LocalDate;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.recommend.repository.LocateRepository;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.schedule.dto.GetScheduleResponseDTO;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private UserRepository userRepository;

	private CreateScheduleRequestDTO requestDTO;
	private Locate locate;
	private User user = new User();

	@BeforeEach
	void set(){
		locate = new Locate(Country.KOREA, City.SEOUL);

		 requestDTO = new CreateScheduleRequestDTO(
			"발리여행",
			locate.getCountry(),
			locate.getCity(),
			LocalDate.parse("2024-01-01"),
			LocalDate.parse("2024-01-05"),
			"MEMO"
		);
		 userRepository.save(user);
	}
	//
	// @Test
	// void createSchedule() {
	// 	// given
	// 	// when
	// 	ResponseEntity<BaseResponse<Object>> response = scheduleService.createSchedule(user, requestDTO);
	// 	// then
	// 	Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	// }
	//
	// @Test
	// void editSchedule() {
	// 	// given
	// 	scheduleService.createSchedule(user, requestDTO);
	//
	// 	List<GetScheduleResponseDTO> data = scheduleService.getSchedules(user).getBody().data();
	// 	Long schedId = data.get(data.size() - 1).id(); // 임시
	// 	// when
	// 	EditScheduleRequestDTO editRequest = new EditScheduleRequestDTO(schedId,
	// 		"몽골 여행!!",
	// 		Country.KOREA,
	// 		City.SEOUL,
	// 		LocalDate.parse("2024-03-01"),
	// 		LocalDate.parse("2024-05-05"),
	// 		"nope");
	//
	// 	ResponseEntity<BaseResponse<Object>> response = scheduleService.editSchedule(user, editRequest);
	// 	// then
	// 	getTest();
	// 	Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	// }
	//
	// private void getTest() {
	// 	ResponseEntity<BaseResponse<List<GetScheduleResponseDTO>>> schedules = scheduleService.getSchedules(user);
	// 	System.out.println("test schedules = " + schedules.getBody().data());
	// }
	//
	// @Test
	// void getSchedules() {
	// 	// given
	// 	scheduleService.createSchedule(user, requestDTO);
	// 	// when
	// 	ResponseEntity<BaseResponse<List<GetScheduleResponseDTO>>> response = scheduleService.getSchedules(user);
	//
	// 	// then
	// 	System.out.println("Get response Test = " + response.getBody().data());
	// 	Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	// }
	//
	// @Test
	// void deleteSchedule() {
	// 	// given
	// 	scheduleService.createSchedule(user,requestDTO);
	// 	List<GetScheduleResponseDTO> data = scheduleService.getSchedules(user).getBody().data();
	// 	Long schedId = data.get(data.size() - 1).id();
	// 	getTest();
	// 	// when
	// 	ResponseEntity<BaseResponse<Object>> response = scheduleService.deleteSchedule(schedId);
	// 	// then
	// 	getTest();
	// 	Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	// }
}