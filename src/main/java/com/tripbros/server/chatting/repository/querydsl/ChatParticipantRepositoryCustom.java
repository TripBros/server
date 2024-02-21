package com.tripbros.server.chatting.repository.querydsl;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.user.domain.User;

public interface ChatParticipantRepositoryCustom {
	Boolean existSingleRoomByBoardAndUser(Board board, User user);
}
