package com.tripbros.server.chatting.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.chatting.domain.Chatroom;

public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {

}
