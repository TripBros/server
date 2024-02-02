package com.tripbros.server.schedule.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.common.exception.ValidationFailException;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.schedule.dto.GetScheduleResponseDTO;
import com.tripbros.server.schedule.service.ScheduleService;
import com.tripbros.server.security.AuthUser;
import com.tripbros.server.security.SecurityUser;
import com.tripbros.server.user.domain.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@Tag(name = "일정 컨트롤러", description = "일정 관련 API")
public class ScheduleController {
	private final ScheduleService scheduleService;

	@PostMapping("/create")
	@Operation(summary = "일정 추가")
	public ResponseEntity<BaseResponse<Object>> createSchedule(@Valid @AuthUser SecurityUser user, @RequestBody CreateScheduleRequestDTO createScheduleRequestDTO, Errors errors){
		if (errors.hasErrors())
			throw new ValidationFailException(errors);

		return scheduleService.createSchedule(user.getUser(), createScheduleRequestDTO);
	}

	@PostMapping("/edit")
	@Operation(summary = "일정 수정")
	public ResponseEntity<BaseResponse<Object>> updateSchedule(@Valid @AuthUser SecurityUser user, @RequestBody EditScheduleRequestDTO editScheduleRequestDTO, Errors errors){
		if (errors.hasErrors())
			throw new ValidationFailException(errors);

		return scheduleService.editSchedule(user.getUser(), editScheduleRequestDTO);
	}

	@GetMapping("/list")
	@Operation(summary = "일정 조회")
	public ResponseEntity<BaseResponse<List<GetScheduleResponseDTO>>> getSchedules(@Valid @AuthUser SecurityUser user, Errors errors){
		if (errors.hasErrors())
			throw new ValidationFailException(errors);

		return scheduleService.getSchedules(user.getUser());
	}

	@DeleteMapping("/delete")
	@Operation(summary = "일정 삭제")
	public ResponseEntity<BaseResponse<Object>> deleteSchedule(@Valid @AuthUser SecurityUser user, @RequestBody Long scheduleId, Errors errors){
		if (errors.hasErrors())
			throw new ValidationFailException(errors);

		return scheduleService.deleteSchedule(scheduleId);
	}
}
