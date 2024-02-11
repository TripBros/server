package com.tripbros.server.schedule.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripbros.server.common.exception.UserPermissionException;
import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.recommend.repository.LocateRepository;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.schedule.dto.GetScheduleResponseDTO;
import com.tripbros.server.schedule.exception.SchedulePermissionException;
import com.tripbros.server.schedule.repository.ScheduleRepository;
import com.tripbros.server.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final LocateRepository locateRepository;

	public Schedule createSchedule(User user, CreateScheduleRequestDTO createScheduleRequestDTO){
		Schedule schedule = createScheduleRequestDTO.toEntity(user, locateRepository);
		scheduleRepository.save(schedule);

		log.info("success to create schedule");
		return schedule;
	}

	public Schedule editSchedule(User user, EditScheduleRequestDTO editScheduleRequestDTO){
		// TODO : 동행 일정 수정 시, 푸시 알림 전송 및 동기화 작업 필요
		Optional<Schedule> target = scheduleRepository.findById(editScheduleRequestDTO.id());

		Schedule schedule = target.orElseThrow(
			() -> new SchedulePermissionException("존재하지 않은 일정입니다."));
		if(user != null)
			checkUserPermission(user, schedule);

		Locate modifiedLocate = locateRepository.findByCountryAndCity(editScheduleRequestDTO.country(), editScheduleRequestDTO.city());
		Schedule result = target.get().editSchedule(editScheduleRequestDTO, modifiedLocate);

		log.info("success to edit schedule");
		return result;
	}

	public List<GetScheduleResponseDTO> getSchedules(User user){
		List<Schedule> scheduleList = scheduleRepository.findAllByUser(user);
		List<GetScheduleResponseDTO> responseList = scheduleList.stream()
			.map(GetScheduleResponseDTO::toDTO)
			.toList();

		log.info("success to get schedules");
		return responseList;
	}

	public void deleteSchedule(User user, Long scheduleId) {
		Optional<Schedule> target = scheduleRepository.findById(scheduleId);

		Schedule schedule = target.orElseThrow(
			() -> new SchedulePermissionException("존재하지 않은 일정입니다."));
		checkUserPermission(user, schedule);

		scheduleRepository.deleteById(scheduleId);

		log.info("success to delete schedule");
	}

	private static void checkUserPermission(User user, Schedule schedule) {
		if (!schedule.getUser().getId().equals(user.getId()))
			throw new UserPermissionException();
	}

}