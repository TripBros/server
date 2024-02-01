package com.tripbros.server.schedule.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.common.exception.ValidationFailException;
import com.tripbros.server.schedule.dto.ScheduleRequestDTO;
import com.tripbros.server.schedule.service.ScheduleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@Tag(name = "일정 컨트롤러", description = "일정 관련 API")
public class ScheduleController {
	private final ScheduleService scheduleService;

	@PostMapping("/new")
	@Operation(summary = "일정 추가")
	public ResponseEntity<BaseResponse<Object>> createSchedule(@RequestBody ScheduleRequestDTO scheduleRequestDTO, Errors errors){
		if (errors.hasErrors())
			throw new ValidationFailException(errors);

		return scheduleService.saveSchedule(scheduleRequestDTO);
	}

	// @PostMapping("/modify")
	// @Operation(summary = "일정 수정")
	// public ResponseEntity<BaseResponse<Object>> updateSchedule(@RequestBody SaveScheduleRequestDTO saveScheduleRequestDTO, Errors errors){
	// 	if (errors.hasErrors())
	// 		throw new ValidationFailException(errors);
	//
	// 	return scheduleService.saveSchedule(saveScheduleRequestDTO);
	// }

}
