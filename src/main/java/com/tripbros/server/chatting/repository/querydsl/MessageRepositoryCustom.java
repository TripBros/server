package com.tripbros.server.chatting.repository.querydsl;

import java.util.List;

import com.tripbros.server.chatting.domain.Chatroom;
import com.tripbros.server.chatting.dto.MessageResponse;

public interface MessageRepositoryCustom {

	List<MessageResponse> findAllByChatroomWithDto(Chatroom chatroom);
}
