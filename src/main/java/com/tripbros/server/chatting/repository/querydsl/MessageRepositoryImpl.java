package com.tripbros.server.chatting.repository.querydsl;

import static com.tripbros.server.chatting.domain.QMessage.*;
import static com.tripbros.server.user.domain.QUser.*;

import java.util.List;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tripbros.server.chatting.domain.Chatroom;
import com.tripbros.server.chatting.dto.MessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class MessageRepositoryImpl implements MessageRepositoryCustom{

	private final JPAQueryFactory queryFactory;
	@Override
	public List<MessageResponse> findAllByChatroomWithDto(Chatroom chatroom) {
		return queryFactory.select(
				Projections.constructor(
					MessageResponse.class,
					user.nickname,
					user.profileImage,
					message.content,
					message.sentAt,
					new CaseBuilder().when(user.isNull()).then(true).otherwise(false) //isSystemMessage
				)
			)
			.from(message)
			.leftJoin(message.user, user)
			.where(message.chatroom.eq(chatroom))
			.orderBy(message.sentAt.asc())
			.fetch();
	}
}
