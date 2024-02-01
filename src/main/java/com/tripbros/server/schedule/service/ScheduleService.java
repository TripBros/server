package com.tripbros.server.schedule.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.ScheduleRequestDTO;
import com.tripbros.server.schedule.enumerate.ScheduleResultMessage;
import com.tripbros.server.schedule.repository.ScheduleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;

	public ResponseEntity<BaseResponse<Object>> saveSchedule(ScheduleRequestDTO scheduleRequestDTO){
		Schedule schedule = scheduleRequestDTO.toEntity();
		scheduleRepository.save(schedule);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			ScheduleResultMessage.SAVE_SCHEDULE_SUCCESS.getMessage(), null);

		log.info("success to save schedule");
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
