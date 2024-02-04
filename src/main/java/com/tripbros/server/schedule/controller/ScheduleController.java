package com.tripbros.server.schedule.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.schedule.dto.GetScheduleResponseDTO;
import com.tripbros.server.schedule.enumerate.ScheduleResultMessage;
import com.tripbros.server.schedule.exception.ScheduleRequestException;
import com.tripbros.server.schedule.service.ScheduleService;
import com.tripbros.server.security.AuthUser;
import com.tripbros.server.security.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@Tag(name = "일정 컨트롤러", description = "일정 관련 API")
public class ScheduleController {
	private final ScheduleService scheduleService;

	@PostMapping("/create")
	@Operation(summary = "일정 추가")
	public ResponseEntity<BaseResponse<Object>> createSchedule(@AuthUser SecurityUser user, @RequestBody @Valid CreateScheduleRequestDTO createScheduleRequestDTO, Errors errors){
		if (errors.hasErrors())
			throw new ScheduleRequestException(errors);

		scheduleService.createSchedule(user.getUser(), createScheduleRequestDTO);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			ScheduleResultMessage.CREATE_SCHEDULE_SUCCESS.getMessage(), null);

		return ResponseEntity.ok().body(response);
	}

	// @PostMapping("/edit")
	// @Operation(summary = "일정 수정")
	// public ResponseEntity<BaseResponse<Object>> editSchedule(@AuthUser SecurityUser user, @RequestBody @Valid EditScheduleRequestDTO editScheduleRequestDTO, Errors errors){
	// 	if (errors.hasErrors())
	// 		throw new ValidationFailException(errors);
	//
	// 	return scheduleService.editSchedule(user.getUser(), editScheduleRequestDTO);
	// }

	@GetMapping("/list")
	@Operation(summary = "일정 조회")
	public ResponseEntity<BaseResponse<List<GetScheduleResponseDTO>>> getSchedules(@AuthUser SecurityUser user){
		List<GetScheduleResponseDTO> responseList = scheduleService.getSchedules(user.getUser());
		BaseResponse<List<GetScheduleResponseDTO>> response = new BaseResponse<>(true, HttpStatus.OK,
			ScheduleResultMessage.GET_SCHEDULE_SUCCESS.getMessage(), responseList);

		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/delete")
	@Operation(summary = "일정 삭제")
	public ResponseEntity<BaseResponse<Object>> deleteSchedule(@AuthUser SecurityUser user, @RequestBody @Valid Map<String,Long> request, Errors errors){
		if (errors.hasErrors())
			throw new ScheduleRequestException(errors);

		Long scheduleId = request.get("id");
		scheduleService.deleteSchedule(user.getUser(), scheduleId);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			ScheduleResultMessage.DELETE_SCHEDULE_SUCCESS.getMessage(), null);

		return ResponseEntity.ok().body(response);
	}
}
