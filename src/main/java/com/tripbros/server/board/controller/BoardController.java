package com.tripbros.server.board.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.board.dto.CreateBoardRequestDTO;
import com.tripbros.server.board.dto.EditBoardRequestDTO;
import com.tripbros.server.board.dto.GetBoardResponseDTO;
import com.tripbros.server.board.dto.GetBookmarkedBoardResponseDTO;
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

	@GetMapping
	@Operation(summary = "게시글 전체 조회")
	public ResponseEntity<BaseResponse<List<GetBoardResponseDTO>>> getBoards(@AuthUser SecurityUser user){
		List<GetBoardResponseDTO> result = boardService.getBoards(user.getUser());

		BaseResponse<List<GetBoardResponseDTO>> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.GET_BOARD_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}
	@GetMapping("/own")
	@Operation(summary = "내가 쓴 게시글 전체 조회")
	public ResponseEntity<BaseResponse<List<GetBoardResponseDTO>>> getMyBoards(@AuthUser SecurityUser user){
		List<GetBoardResponseDTO> result = boardService.getMyBoards(user.getUser());

		BaseResponse<List<GetBoardResponseDTO>> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.GET_BOARD_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/hit")
	@Operation(summary = "게시글 상세 조회")
	public ResponseEntity<BaseResponse<Object>> updateBoardHit(@AuthUser SecurityUser user, @RequestParam Long boardId){
		boardService.updateBoardHit(boardId);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.GET_BOARD_DETAIL_SUCCESS.getMessage(), null);

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/deadline-reached")
	@Operation(summary = "게시글 수동 마감 처리")
	public ResponseEntity<BaseResponse<Object>> updateDeadlineReached(@AuthUser SecurityUser user, @RequestParam Long boardId){
		boardService.updateDeadlineReached(user.getUser(), boardId);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.UPDATE_DEADLINE_REACH.getMessage(), "게시글 마감 완료");

		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping("/deadline-reached")
	@Operation(summary = "일정 날짜가 지나지 않은 게시글에 한해 마감 취소")
	public ResponseEntity<BaseResponse<Object>> cancelDeadlineReached(@AuthUser SecurityUser user, @RequestParam Long boardId) {
		BaseResponse<Object> response;

		if (boardService.cancelDeadLineReached(user.getUser(), boardId)){
			response = new BaseResponse<>(true, HttpStatus.OK,
				BoardResultMessage.UPDATE_DEADLINE_REACH.getMessage(), "게시글 마감 취소 완료");
			return ResponseEntity.ok().body(response);
		}

		else {
			response = new BaseResponse<>(false, HttpStatus.BAD_REQUEST,
				"일정의 날짜가 지난 게시글은 마감을 취소할 수 없습니다.", "게시글 마감 취소 실패");
			return ResponseEntity.badRequest().body(response);
		}
	}

	@DeleteMapping
	@Operation(summary = "게시글 삭제")
	public ResponseEntity<BaseResponse<Object>> deleteBoard(@AuthUser SecurityUser user, @RequestParam Long boardId){

		boardService.deleteBoard(user.getUser(), boardId);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.DELETE_BOARD_SUCCESS.getMessage(), null);

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/bookmark")
	@Operation(summary = "게시글 북마크 업데이트 요청")
	public ResponseEntity<BaseResponse<Object>> updateBookmarkedBoard(@AuthUser SecurityUser user, @RequestParam Long boardId){
		String responseMessage = boardService.updateBookmarkedBoard(user.getUser(), boardId);

		BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.UPDATE_BOOKMARK_BOARD_SUCCESS.getMessage(), responseMessage);

		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/bookmark")
	@Operation(summary = "북마크 한 게시글 조회")
	public ResponseEntity<BaseResponse<List<GetBookmarkedBoardResponseDTO>>> getBookmarkedBoard(@AuthUser SecurityUser user){
		List<GetBookmarkedBoardResponseDTO> result = boardService.getBookmarkedBoards(user.getUser());

		BaseResponse<List<GetBookmarkedBoardResponseDTO>> response = new BaseResponse<>(true, HttpStatus.OK,
			BoardResultMessage.GET_BOOKMARKED_BOARD_SUCCESS.getMessage(), result);

		return ResponseEntity.ok().body(response);
	}
}
