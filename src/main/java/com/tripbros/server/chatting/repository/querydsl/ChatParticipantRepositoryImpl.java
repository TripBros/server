package com.tripbros.server.chatting.repository.querydsl;

import static com.tripbros.server.chatting.domain.QChatroom.*;
import static com.tripbros.server.chatting.domain.QChatroomParticipant.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.domain.QBoard;
import com.tripbros.server.user.domain.QUser;
import com.tripbros.server.user.domain.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatParticipantRepositoryImpl implements ChatParticipantRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public Boolean existSingleRoomByBoardAndUser(Board board, User user) {
		Integer fetchOne = queryFactory.selectOne()
			.from(chatroomParticipant)
			.join(chatroomParticipant.chatroom, chatroom).fetchJoin()
			.join(chatroom.board, QBoard.board).fetchJoin()
			.join(chatroomParticipant.user, QUser.user).fetchJoin()
			.where(QUser.user.eq(user)
				.and(QBoard.board.eq(board))
				.and(chatroom.isGroupChat.eq(false)))
			.fetchFirst();
		return fetchOne != null;
	}
}
