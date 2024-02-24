package com.tripbros.server.chatting.repository.querydsl;

import static com.tripbros.server.chatting.domain.QChatroom.*;
import static com.tripbros.server.chatting.domain.QChatroomParticipant.*;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.domain.QBoard;
import com.tripbros.server.chatting.domain.Chatroom;
import com.tripbros.server.chatting.domain.QChatroomParticipant;
import com.tripbros.server.chatting.dto.ChatroomResponse;
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

	@Override
	public List<ChatroomResponse> getAllUserChatroomToDto(User user) {
		QChatroomParticipant subParticipant = new QChatroomParticipant("subParticipant");
		QUser subUser = new QUser("subUser");
		return queryFactory.select(
				Projections.constructor(
					ChatroomResponse.class,
					chatroom.id,
					chatroom.title,
					new CaseBuilder()
						.when(chatroom.isGroupChat.isTrue()).then(
							chatroom.board.user.profileImage
						)
						.otherwise(JPAExpressions.select(subUser.profileImage)
							.from(chatroomParticipant)
							.join(chatroomParticipant.user, subUser)
							.where(chatroomParticipant.chatroom.eq(chatroom)
								.and(chatroomParticipant.user.ne(user)))
							.limit(1)), // 프로필사진
					chatroom.lastMessage,
					chatroom.updatedAt,
					chatroom.isGroupChat
				)
			)
			.from(chatroomParticipant)
			.join(chatroomParticipant.chatroom, chatroom)
			.join(chatroomParticipant.user, QUser.user).on(chatroomParticipant.user.eq(user))
			.fetch();
	}

}
