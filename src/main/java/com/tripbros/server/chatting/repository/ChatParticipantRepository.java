package com.tripbros.server.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.chatting.domain.ChatroomParticipant;
import com.tripbros.server.chatting.domain.ChatroomParticipantId;

public interface ChatParticipantRepository extends JpaRepository<ChatroomParticipant, ChatroomParticipantId> {

}
