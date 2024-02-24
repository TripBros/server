package com.tripbros.server.chatting.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.chatting.domain.Chatroom;

public interface ChatroomRepository extends JpaRepository<Chatroom, UUID> {


	@Query("select b from Chatroom c join c.board b join fetch b.user join fetch b.schedule where c.id = :chatroomId")
	Board findBoardById(UUID chatroomId);

}
