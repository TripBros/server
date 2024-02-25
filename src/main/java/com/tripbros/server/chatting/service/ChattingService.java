package com.tripbros.server.chatting.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.repository.BoardRepository;
import com.tripbros.server.chatting.domain.Chatroom;
import com.tripbros.server.chatting.domain.ChatroomParticipant;
import com.tripbros.server.chatting.domain.Message;
import com.tripbros.server.chatting.dto.ChatroomResponse;
import com.tripbros.server.chatting.dto.MessageRequest;
import com.tripbros.server.chatting.dto.MessageResponse;
import com.tripbros.server.chatting.exception.ChatException;
import com.tripbros.server.chatting.exception.ChatExceptionMessage;
import com.tripbros.server.chatting.repository.ChatParticipantRepository;
import com.tripbros.server.chatting.repository.ChatroomRepository;
import com.tripbros.server.chatting.repository.MessageRepository;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChattingService {
	private final ChatroomRepository chatroomRepository;
	private final ChatParticipantRepository participantRepository;
	private final BoardRepository boardRepository;
	private final MessageRepository messageRepository;
	private final UserRepository userRepository;
	private final SimpMessagingTemplate template;


	public UUID getOneToOneChatroom(User user, Long boardId) {
		Board board = boardRepository.findById(boardId).orElseThrow(() -> new NoSuchElementException()); //TODO : 변경
		if (board.getUser().getId().equals(user.getId())) {
			throw new ChatException(ChatExceptionMessage.CANNOT_CHAT_WITH_SELF.getMessage());

		}
		return participantRepository.findSingleRoomByBoardAndUser(board, user).orElseGet(() -> {
			Chatroom chatroom = Chatroom.builder()
				.board(board)
				.updatedAt(LocalDateTime.now())
				.status(true)
				.isGroupChat(false)
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
		}).getId();
	}

	public UUID getGroupChatroom(List<User> userList, Board board) {
		Chatroom groupChatroom = participantRepository.findGroupChatroomByBoard(board).orElseGet(() -> {
			Chatroom chatroom = Chatroom.builder()
				.board(board)
				.updatedAt(LocalDateTime.now())
				.status(true)
				.isGroupChat(true)
				.title(board.getTitle())
				.build();
			chatroomRepository.save(chatroom);
			return chatroom;
		});
		userList.forEach(u -> {
			if (!participantRepository.existsByUserAndChatroomId(u, groupChatroom.getId())) {
				ChatroomParticipant participant = ChatroomParticipant.builder()
					.user(u)
					.chatroom(groupChatroom)
					.build();
				participantRepository.save(participant);
				saveSystemMessage(groupChatroom.getId(), u.getNickname() + "님이 입장하였습니다.");
			}
		});

		return groupChatroom.getId();

	}
	public Message saveMessage(Long userId, UUID chatroomId, MessageRequest request) {
		User user = userRepository.getReferenceById(userId);
		Chatroom chatroom = chatroomRepository.getReferenceById(chatroomId);
		Message message = Message.builder()
			.chatroom(chatroom)
			.user(user)
			.sentAt(LocalDateTime.now())
			.content(request.message())
			.build();
		chatroom.updateLastMessageWithTime(request.message());
		return messageRepository.save(message);
	}

	private Message saveSystemMessage(UUID chatroomId, String content) {
		Chatroom chatroom = chatroomRepository.getReferenceById(chatroomId);
		Message message = Message.builder()
			.chatroom(chatroom)
			.content(content)
			.sentAt(LocalDateTime.now())
			.user(null)
			.build();
		chatroom.updateLastMessageWithTime(content);
		return messageRepository.save(message);
	}

	public List<MessageResponse> getAllChattingData(User user, UUID chatroomId) {
		if (!checkUserPermission(user, chatroomId)){
			throw new ChatException(ChatExceptionMessage.CANNOT_ACCESS_CHATROOM.getMessage());
		}
		return messageRepository.findAllByChatroomWithDto(
			chatroomRepository.getReferenceById(chatroomId));
	}

	private boolean checkUserPermission(User user, UUID chatroomId) {
		return participantRepository.existsByUserAndChatroomId(user, chatroomId);
	}

	public List<ChatroomResponse> getAllChatroom(User user) {
		return participantRepository.getAllUserChatroomToDto(user);
	}

	public void sendSystemMessage(UUID chatroomId, String message) {
		MessageResponse response = MessageResponse.builder()
			.isSystemMessage(true)
			.content(message)
			.sentAt(LocalDateTime.now())
			.build();
		saveSystemMessage(chatroomId, message);
		template.convertAndSend("/sub/chat/" + chatroomId, response);
	}

}
