package com.tripbros.server.board.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.dto.CreateBoardRequestDTO;
import com.tripbros.server.board.dto.EditBoardRequestDTO;
import com.tripbros.server.board.exception.BoardPermissionException;
import com.tripbros.server.board.repository.BoardRepository;
import com.tripbros.server.common.exception.UserPermissionException;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.schedule.service.ScheduleService;
import com.tripbros.server.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	private final ScheduleService scheduleService;

	public Board createBoard(User user, CreateBoardRequestDTO createBoardRequestDTO){
		Schedule schedule = createCompanionSchedule(createBoardRequestDTO);

		Board board = createBoardRequestDTO.toEntity(user, schedule);
		boardRepository.save(board);

		log.info("success to create board");
		return board;
	}

	public Board editBoard(User user, EditBoardRequestDTO editBoardRequestDTO){
		Optional<Board> target = boardRepository.findById(editBoardRequestDTO.id());

		Board board = target.orElseThrow(() -> new BoardPermissionException("존재하지 않은 게시글 입니다."));
		checkUserPermission(user, board);

		// 동행 일정 수정
		String newTitle = getCompanionTitle(editBoardRequestDTO.city(),editBoardRequestDTO.title());
		EditScheduleRequestDTO editScheduleRequest = new EditScheduleRequestDTO(board.getSchedule().getId(),
			newTitle,
			editBoardRequestDTO.country(),
			editBoardRequestDTO.city(),
			editBoardRequestDTO.startDate(),
			editBoardRequestDTO.endDate(),
			null);

		Schedule editedSchedule = scheduleService.editSchedule(null, editScheduleRequest);

		// 게시글 수정
		Board result = board.editBoard(editBoardRequestDTO, editedSchedule);

		log.info("success to edit board");
		return result;
	}

	private Schedule createCompanionSchedule (CreateBoardRequestDTO createBoardRequestDTO) {
		String scheduleTitle = getCompanionTitle(createBoardRequestDTO.city(), createBoardRequestDTO.title());
		CreateScheduleRequestDTO createScheduleRequest = new CreateScheduleRequestDTO(scheduleTitle,
			createBoardRequestDTO.country(),
			createBoardRequestDTO.city(),
			createBoardRequestDTO.startDate(),
			createBoardRequestDTO.endDate(),
			null);

		return scheduleService.createSchedule(null, createScheduleRequest);
	}

	private static String getCompanionTitle(City city, String boardTitle) {
		return "[".concat(city.toString()).concat("] ").concat(boardTitle);
	}

	private static void checkUserPermission(User user, Board board) {
		if (!board.getUser().getId().equals(user.getId()))
			throw new UserPermissionException();
	}
}
