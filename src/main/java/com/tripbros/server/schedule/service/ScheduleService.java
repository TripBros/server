package com.tripbros.server.schedule.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.schedule.dto.GetScheduleResponseDTO;
import com.tripbros.server.schedule.enumerate.ScheduleResultMessage;
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

	public ResponseEntity<BaseResponse<Object>> createSchedule(User user, CreateScheduleRequestDTO createScheduleRequestDTO){
		Schedule schedule = createScheduleRequestDTO.toEntity(user);
		scheduleRepository.save(schedule);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			ScheduleResultMessage.CREATE_SCHEDULE_SUCCESS.getMessage(), null);

		log.info("success to create schedule");
		return ResponseEntity.ok().body(response);
	}

	public ResponseEntity<BaseResponse<Object>> editSchedule(User user, EditScheduleRequestDTO editScheduleRequestDTO){
		scheduleRepository.deleteById(editScheduleRequestDTO.id());

		Schedule schedule = editScheduleRequestDTO.toEntity(user);
		scheduleRepository.save(schedule);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			ScheduleResultMessage.UPDATE_SCHEDULE_SUCCESS.getMessage(), null);

		log.info("success to edit schedule");
		return ResponseEntity.ok().body(response);
	}

	public ResponseEntity<BaseResponse<List<GetScheduleResponseDTO>>> getSchedules(User user){
		List<Schedule> scheduleList = scheduleRepository.findAllByUser(user);

		List<GetScheduleResponseDTO> responseList = scheduleList.stream()
			.map(GetScheduleResponseDTO::toDTO)
			.collect(Collectors.toList());

		BaseResponse<List<GetScheduleResponseDTO>> response = new BaseResponse<>(true, HttpStatus.OK,
			ScheduleResultMessage.GET_SCHEDULE_SUCCESS.getMessage(), responseList);

		log.info("success to get schedules");
		return ResponseEntity.ok().body(response);
	}

	public ResponseEntity<BaseResponse<Object>> deleteSchedule(Long scheduleId){
		scheduleRepository.deleteById(scheduleId);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			ScheduleResultMessage.DELETE_SCHEDULE_SUCCESS.getMessage(), null);

		log.info("success to delete schedule");
		return ResponseEntity.ok().body(response);
	}
}
