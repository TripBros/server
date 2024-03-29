package com.tripbros.server.chatting.repository.querydsl;

import java.util.List;
import java.util.Optional;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.chatting.domain.Chatroom;
import com.tripbros.server.chatting.dto.ChatroomResponse;
import com.tripbros.server.user.domain.User;

public interface ChatParticipantRepositoryCustom {
	Optional<Chatroom> findSingleRoomByBoardAndUser(Board board, User user);

	List<ChatroomResponse> getAllUserChatroomToDto(User user);
}
