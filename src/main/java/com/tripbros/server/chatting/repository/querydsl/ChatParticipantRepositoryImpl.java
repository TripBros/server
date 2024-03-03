package com.tripbros.server.chatting.repository.querydsl;

import static com.tripbros.server.board.domain.QBoard.*;
import static com.tripbros.server.chatting.domain.QChatroom.*;
import static com.tripbros.server.chatting.domain.QChatroomParticipant.*;
import static com.tripbros.server.recommend.domain.QLocate.*;
import static com.tripbros.server.schedule.domain.QSchedule.*;
import static com.tripbros.server.schedule.domain.QWaitingCompanion.*;
import static com.tripbros.server.user.domain.QUser.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
	public Optional<Chatroom> findGroupChatroomByBoard(Board board) {
		return Optional.ofNullable(queryFactory.select(chatroom)
			.from(chatroomParticipant)
			.join(chatroomParticipant.chatroom, chatroom)
			.join(chatroom.board, QBoard.board)
			.where(QBoard.board.eq(board)
				.and(chatroom.isGroupChat.isTrue()))
			.fetchOne()
		);
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
							board.user.profileImage
						)
						.otherwise(JPAExpressions.select(subUser.profileImage)
							.from(chatroomParticipant)
							.join(chatroomParticipant.user, subUser)
							.where(chatroomParticipant.chatroom.eq(chatroom)
								.and(chatroomParticipant.user.ne(user)))
							.limit(1)), // 프로필사진
					chatroom.lastMessage,
					chatroom.updatedAt,
					chatroom.isGroupChat,
					new CaseBuilder()
						.when(board.user.eq(user))
						.then(true)
						.otherwise(false), // isHost
					//isConfirmed
					new CaseBuilder()
						.when(JPAExpressions.selectOne()
							.from(waitingCompanion)
							.where(waitingCompanion.board.eq(board)
								.and((waitingCompanion.user.eq(user).or(waitingCompanion.opponent.eq(user))))
								.and(waitingCompanion.isDeleted.isTrue()))
							.exists())
						.then(true)
						.otherwise(false),
					locate.city,
					schedule.startDate,
					schedule.endDate,
					board.requiredHeadCount
				)
			)
			.from(chatroomParticipant)
			.innerJoin(chatroomParticipant.chatroom, chatroom)
			.innerJoin(chatroom.board, board)
			.innerJoin(board.schedule, schedule)
			.innerJoin(schedule.locate, locate)
			.join(chatroomParticipant.user, QUser.user).on(chatroomParticipant.user.eq(user))
			.fetch();
	}

	@Override
	public User getOpponentUserByChatroomId(UUID chatroomId, User me) {
		return queryFactory.select(user)
			.from(chatroomParticipant)
			.where(chatroomParticipant.chatroom.id.eq(chatroomId)
				.and(chatroomParticipant.user.ne(me)))
			.fetchOne();
	}
}
