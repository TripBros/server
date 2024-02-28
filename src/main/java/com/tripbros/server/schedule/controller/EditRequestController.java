package com.tripbros.server.schedule.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.schedule.dto.editrequest.EditRequestResponse;
import com.tripbros.server.schedule.service.EditRequestService;
import com.tripbros.server.security.AuthUser;
import com.tripbros.server.security.SecurityUser;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "동행 일정 변경 요청 컨트롤러")
@RequestMapping("/api/edit-request")
public class EditRequestController {
	private final EditRequestService requestService;

	@GetMapping
	public ResponseEntity<BaseResponse<List<EditRequestResponse>>> getAllRequest(@AuthUser SecurityUser user) {
		List<EditRequestResponse> responses = requestService.getAllRequest(user.getUser());
		return ResponseEntity.ok().body(new BaseResponse<>(true, HttpStatus.OK, "로드 성공", responses));
	}

	@PostMapping
	public ResponseEntity<Object> confirmRequest(@AuthUser SecurityUser user, @RequestParam Long requestId) {
		requestService.confirmEditRequest(user.getUser(), requestId);
		return ResponseEntity.ok(new BaseResponse<>(true, HttpStatus.OK, "변경 성공", null));
	}



}
