package com.tripbros.server.chatting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.chatting.domain.Message;
import com.tripbros.server.chatting.repository.querydsl.MessageRepositoryCustom;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {

}
