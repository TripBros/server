package com.tripbros.server.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.board.dto.CreateBoardRequestDTO;
import com.tripbros.server.board.enumerate.BoardResultMessage;
import com.tripbros.server.board.exception.BoardrequestException;
import com.tripbros.server.board.service.BoardService;
import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.security.AuthUser;
import com.tripbros.server.security.SecurityUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Tag(name = "게시글 컨트롤러", description = "게시글 관련 API")
public class BoardController {
	private final BoardService boardService;

	@PostMapping
	@Operation(summary = "게시글 작성")
	public ResponseEntity<BaseResponse<Object>> createBoard(@AuthUser SecurityUser user, @RequestBody @Valid CreateBoardRequestDTO createBoardRequestDTO, Errors errors){
		if (errors.hasErrors())
			throw new BoardrequestException(errors);

		boardService.createBoard(user.getUser(), createBoardRequestDTO);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.CREATE_BOARD_SUCCESS.getMessage(), null);

		return ResponseEntity.ok().body(response);
	}
}