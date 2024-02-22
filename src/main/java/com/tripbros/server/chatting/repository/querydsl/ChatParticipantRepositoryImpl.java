package com.tripbros.server.chatting.repository.querydsl;

import static com.tripbros.server.chatting.domain.QChatroom.*;
import static com.tripbros.server.chatting.domain.QChatroomParticipant.*;

import java.util.Optional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.domain.QBoard;
import com.tripbros.server.chatting.domain.Chatroom;
import com.tripbros.server.user.domain.QUser;
import com.tripbros.server.user.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatParticipantRepositoryImpl implements ChatParticipantRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<Chatroom> findSingleRoomByBoardAndUser(Board board, User user) {
		return Optional.ofNullable(queryFactory.select(chatroom)
			.from(chatroomParticipant)
			.join(chatroomParticipant.chatroom, chatroom)
			.join(chatroom.board, QBoard.board)
			.join(chatroomParticipant.user, QUser.user)
			.where(QUser.user.eq(user)
				.and(QBoard.board.eq(board))
				.and(chatroom.isGroupChat.eq(false)))
			.fetchFirst());
	}
}
