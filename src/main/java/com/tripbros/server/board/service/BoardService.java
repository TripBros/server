package com.tripbros.server.board.service;

import org.springframework.stereotype.Service;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.dto.CreateBoardRequestDTO;
import com.tripbros.server.board.repository.BoardRepository;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
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

	private Schedule createCompanionSchedule (CreateBoardRequestDTO createBoardRequestDTO) {
		String scheduleTitle = "[".concat(createBoardRequestDTO.city().toString()).concat("] ")
			.concat(createBoardRequestDTO.title());
		CreateScheduleRequestDTO createScheduleRequest = new CreateScheduleRequestDTO(scheduleTitle,
			createBoardRequestDTO.country(),
			createBoardRequestDTO.city(),
			createBoardRequestDTO.startDate(),
			createBoardRequestDTO.endDate(),
			null);

		return scheduleService.createSchedule(null, createScheduleRequest);
	}

}
