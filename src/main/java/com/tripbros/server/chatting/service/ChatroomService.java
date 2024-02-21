package com.tripbros.server.chatting.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.repository.BoardRepository;
import com.tripbros.server.chatting.domain.Chatroom;
import com.tripbros.server.chatting.domain.ChatroomParticipant;
import com.tripbros.server.chatting.domain.Message;
import com.tripbros.server.chatting.dto.MessageRequest;
import com.tripbros.server.chatting.repository.ChatParticipantRepository;
import com.tripbros.server.chatting.repository.ChatroomRepository;
import com.tripbros.server.chatting.repository.MessageRepository;
import com.tripbros.server.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChatroomService {
	private final ChatroomRepository chatroomRepository;
	private final ChatParticipantRepository participantRepository;
	private final BoardRepository boardRepository;
	private final MessageRepository messageRepository;

	public Chatroom createSingleChatroom(User user, Long boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException()); //TODO : 변경
		Chatroom chatroom = Chatroom.builder()
			.board(board)
			.createAt(LocalDateTime.now())
			.status(true)
			.title(board.getTitle())
			.build();

		chatroomRepository.save(chatroom);
		ChatroomParticipant participant = ChatroomParticipant.builder().chatroom(chatroom).user(user).build();
		ChatroomParticipant owner = ChatroomParticipant.builder()
			.chatroom(chatroom)
			.user(board.getUser())
			.build();
		participantRepository.save(participant);
		participantRepository.save(owner);
		return chatroom;
	}

	public Message saveMessage(User user, Long chatroomId, MessageRequest request) {
		Message message = Message.builder()
			.chatroom(chatroomRepository.getReferenceById(chatroomId))
			.user(user)
			.sendedAt(LocalDateTime.now())
			.content(request.message())
			.build();
		return messageRepository.save(message);
	}
}
