package com.tripbros.server.schedule.service;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.tripbros.server.common.exception.UserPermissionException;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.recommend.repository.LocateRepository;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.schedule.dto.GetScheduleResponseDTO;
import com.tripbros.server.schedule.repository.ScheduleRepository;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ScheduleServiceTest {

	@InjectMocks
	private ScheduleService scheduleService;
	@Mock
	private UserRepository userRepository;

	@Mock
	private ScheduleRepository scheduleRepository;

	@Mock
	private LocateRepository locateRepository;

	private CreateScheduleRequestDTO requestDTO;
	private Locate locate;
	private User user = Mockito.mock(User.class);

	@BeforeEach
	void set() throws NoSuchFieldException, IllegalAccessException {
		locate = new Locate(Country.일본 , City.도쿄);

		 requestDTO = new CreateScheduleRequestDTO(
			"도쿄 여행",
			locate.getCountry(),
			locate.getCity(),
			431L,
			"도쿄맛집",
			"https://도쿄맛집.com",
			LocalDate.parse("2024-01-01"),
			LocalDate.parse("2024-01-05"),
			"MEMO"
		);

		Field id = user.getClass().getDeclaredField("id");
		id.setAccessible(true);
		id.set(user, 1020L); //PK 설정
		// userRepository.save(user);
	}

	@Test
	@DisplayName("일정 생성 성공")
	void createSchedule() {
		// given
		doReturn(Optional.ofNullable(locate)).when(locateRepository).findByCountryAndCity(any(Country.class), any(City.class));
		// when
		Schedule schedule = scheduleService.createSchedule(user, requestDTO);
		// then
		Assertions.assertThat(schedule.getTitle()).isEqualTo(requestDTO.title());
		verify(scheduleRepository, times(1)).save(any(Schedule.class));
	}

	@Test
	@DisplayName("일정 수정 성공")
	void editSchedule() {
		// given
		doAnswer(invocation -> {
			ReflectionTestUtils.setField((Schedule)invocation.getArgument(0), "id", 123L);
			return null;
		}).when(scheduleRepository).save(any(Schedule.class));

		doReturn(Optional.ofNullable(locate)).when(locateRepository).findByCountryAndCity(any(Country.class), any(City.class));
		Schedule schedule = scheduleService.createSchedule(user, requestDTO);

		doReturn(Stream.of(schedule).toList()).when(scheduleRepository).findAllByUser(any(User.class));
		doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any(Long.class));
		doReturn(Optional.ofNullable(makeLocate(Country.괌, City.괌))).when(locateRepository)
			.findByCountryAndCity(any(Country.class), any(
				City.class));

		List<GetScheduleResponseDTO> schedules = scheduleService.getSchedules(user);
		Long schedId = schedules.get(0).id();
		// when
		EditScheduleRequestDTO editRequest = getEditRequest(schedId);

		Schedule editedSchedule = scheduleService.editSchedule(user, editRequest);
		// then
		Assertions.assertThat(editedSchedule.getLocate().getCity()).isEqualTo(City.괌);
	}

	@Test
	@DisplayName("일정 수정 실패 _ 퍼미션")
	void editScheduleFailByPermission() {
		// given
		doAnswer(invocation -> {
			ReflectionTestUtils.setField((Schedule)invocation.getArgument(0), "id", 123L);
			return null;
		}).when(scheduleRepository).save(any(Schedule.class));

		doReturn(Optional.ofNullable(locate)).when(locateRepository).findByCountryAndCity(any(Country.class), any(City.class));
		Schedule schedule = scheduleService.createSchedule(user, requestDTO);

		doReturn(Stream.of(schedule).toList()).when(scheduleRepository).findAllByUser(any(User.class));
		doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any(Long.class));


		List<GetScheduleResponseDTO> schedules = scheduleService.getSchedules(user);
		Long schedId = schedules.get(0).id();
		// when
		EditScheduleRequestDTO editRequest = getEditRequest(schedId);

		// then
		Assertions.assertThatThrownBy(() -> scheduleService.editSchedule(new User(), editRequest))
			.isInstanceOf(UserPermissionException.class);
	}

	private static EditScheduleRequestDTO getEditRequest(Long schedId) {
		EditScheduleRequestDTO editRequest = new EditScheduleRequestDTO(schedId,
			"괌 여행!!",
			Country.괌,
			City.괌,
			432L,
			"괌맛집",
			"https://괌맛집.com",
			LocalDate.parse("2024-03-01"),
			LocalDate.parse("2024-05-05"),
			"nope");
		return editRequest;
	}

	private static Locate makeLocate(Country country, City city) {
		return new Locate(country, city);
	}

	@Test
	@DisplayName("일정 삭제 성공")
	void deleteSchedule() {
		// given
		doAnswer(invocation -> {
			ReflectionTestUtils.setField((Schedule)invocation.getArgument(0), "id", 123L);
			return null;
		}).when(scheduleRepository).save(any(Schedule.class));

		doReturn(Optional.ofNullable(locate)).when(locateRepository).findByCountryAndCity(any(Country.class), any(City.class));
		Schedule schedule = scheduleService.createSchedule(user, requestDTO);
		doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any(Long.class));
		// when
		scheduleService.deleteSchedule(user ,schedule.getId());
		// then
		verify(scheduleRepository, times(1)).deleteById(any(Long.class));
	}

	@Test
	@DisplayName("일정 삭제 실패")
	void deleteScheduleFailByPermission() {
		// given
		doAnswer(invocation -> {
			ReflectionTestUtils.setField((Schedule)invocation.getArgument(0), "id", 123L);
			return null;
		}).when(scheduleRepository).save(any(Schedule.class));

		doReturn(Optional.ofNullable(locate)).when(locateRepository).findByCountryAndCity(any(Country.class), any(City.class));
		Schedule schedule = scheduleService.createSchedule(user, requestDTO);
		doReturn(Optional.ofNullable(schedule)).when(scheduleRepository).findById(any(Long.class));

		// when
		Assertions.assertThatThrownBy(() -> scheduleService.deleteSchedule(new User(), schedule.getId()))
			.isInstanceOf(UserPermissionException.class);
	}

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
}