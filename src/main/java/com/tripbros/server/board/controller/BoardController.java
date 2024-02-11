package com.tripbros.server.board.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.board.dto.CreateBoardRequestDTO;
import com.tripbros.server.board.dto.EditBoardRequestDTO;
import com.tripbros.server.board.enumerate.BoardResultMessage;
import com.tripbros.server.board.exception.BoardRequestException;
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
			throw new BoardRequestException(errors);

		boardService.createBoard(user.getUser(), createBoardRequestDTO);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.CREATE_BOARD_SUCCESS.getMessage(), null);

		return ResponseEntity.ok().body(response);
	}

	@PatchMapping
	@Operation(summary = "게시글 수정")
	public ResponseEntity<BaseResponse<Object>> editBoard(@AuthUser SecurityUser user, @RequestBody @Valid EditBoardRequestDTO editBoardRequestDTO, Errors errors){
		if(errors.hasErrors())
			throw new BoardRequestException(errors);

		boardService.editBoard(user.getUser(), editBoardRequestDTO);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.EDIT_BOARD_SUCCESS.getMessage(), null);

		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping
	@Operation(summary = "게시글 삭제")
	public ResponseEntity<BaseResponse<Object>> deleteBoard(@AuthUser SecurityUser user, @RequestBody @Valid Map<String, Long> request, Errors errors){
		if(errors.hasErrors())
			throw new BoardRequestException(errors);

		Long boardId = request.get("id");
		boardService.deleteBoard(user.getUser(), boardId);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.DELETE_BOARD_SUCCESS.getMessage(), null);

		return ResponseEntity.ok().body(response);
	}

}
