package com.tripbros.server.schedule.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.chatting.repository.ChatParticipantRepository;
import com.tripbros.server.chatting.repository.ChatroomRepository;
import com.tripbros.server.schedule.domain.WaitingCompanion;
import com.tripbros.server.schedule.enumerate.ScheduleResultMessage;
import com.tripbros.server.schedule.exception.SchedulePermissionException;
import com.tripbros.server.schedule.repository.WaitingRepository;
import com.tripbros.server.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class WaitingService {
	private final WaitingRepository waitingRepository;
	private final ChatParticipantRepository participantRepository;
	private final ChatroomRepository chatroomRepository;
	private final ScheduleService scheduleService;

	public ScheduleResultMessage confirmSchedule(User user, UUID chatroomId) {
		Board board = chatroomRepository.findBoardById(chatroomId);
		//로직을 갈아엎자!! (SOFT-DELETE 적용, USER 및 OPPONENT 칼럼 구분

		User opponent = participantRepository.getOpponentUserByChatroomId(chatroomId, user); // 진짜 상대방
		//상대방이, 나를 기다리는지 검사
		Optional<WaitingCompanion> companionOptional = waitingRepository.findByUserAndOpponentAndBoard(opponent, user, board);

		if (companionOptional.isPresent()) { // 상대방이 이미 대기중
			WaitingCompanion companion = companionOptional.get();
			if (companion.isDeleted()) {
				throw new SchedulePermissionException("이미 확정된 일정입니다.");
			}
			scheduleService.joinCompanionSchedule(user, opponent, companion.getBoard());
			companion.setDeleted(true);
			// 일정 확정 및 복사
			return ScheduleResultMessage.CONFIRM_WAITING_SUCCESS;
		}
		if (waitingRepository.existsByUserAndOpponentAndBoard(user, opponent, board)) {
			throw new SchedulePermissionException("이미 등록되었습니다.");
		}
		WaitingCompanion wait = WaitingCompanion.builder()
			.board(board)
			.opponent(opponent)
			.user(user)
			.build();
		waitingRepository.save(wait);

		//채팅방에 알림 보내야함
		return ScheduleResultMessage.ADD_WAITING_SUCCESS;
	}
}
