package com.tripbros.server.chatting.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.chatting.domain.ChatroomParticipant;
import com.tripbros.server.chatting.repository.querydsl.ChatParticipantRepositoryCustom;
import com.tripbros.server.user.domain.User;

public interface ChatParticipantRepository extends JpaRepository<ChatroomParticipant, Long>,
	ChatParticipantRepositoryCustom {
	boolean existsByUserAndChatroomId(User user, UUID chatroomId);


}
