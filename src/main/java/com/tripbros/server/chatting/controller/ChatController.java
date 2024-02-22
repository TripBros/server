package com.tripbros.server.chatting.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.chatting.dto.MessageRequest;
import com.tripbros.server.chatting.dto.MessageResponse;
import com.tripbros.server.chatting.enumerate.ChatResultMessage;
import com.tripbros.server.chatting.service.ChattingService;
import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.security.AuthUser;
import com.tripbros.server.security.SecurityUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/chat")
public class ChatController {

	private final SimpMessagingTemplate template;
	private final ChattingService chattingService;

	@MessageMapping("/room/{roomId}")
	public void sendMessage(@DestinationVariable(value = "roomId") String roomId, MessageRequest request,
		SimpMessageHeaderAccessor accessor) {
		log.info("# roomId = {}", roomId);
		log.info("# message = {}", request);
		log.info("# token = {}", accessor.getFirstNativeHeader("userId"));
		Long userId = Long.valueOf(accessor.getFirstNativeHeader("userId"));

		chattingService.saveMessage(userId, UUID.fromString(roomId), request);

		MessageResponse response = MessageResponse.builder()
			.sentAt(LocalDateTime.now())
			.content(request.message())
			.isSystemMessage(false)
			.profileImage(
				accessor.getFirstNativeHeader("userProfileImage"))
			.userName(accessor.getFirstNativeHeader("userNickname"))
			.build();
		template.convertAndSend("/sub/room/" + roomId, response);
	}

	@PostMapping("/room/{roomId}/enter")
	public HttpStatus enterRoom(@PathVariable Long roomId) {

		MessageResponse response = MessageResponse.builder().isSystemMessage(true)
			.content("시스템 메세지")
			.sentAt(LocalDateTime.now())
			.build();
		template.convertAndSend("/sub/room/" + roomId, response);
		return HttpStatus.OK;
	}

	@PostMapping
	public ResponseEntity<BaseResponse<String>> doSingleChat(@AuthUser SecurityUser user, @RequestParam Long boardId) {
		UUID chatroomId = chattingService.getOneToOneChatroom(user.getUser(), boardId);
		return ResponseEntity.ok(
			new BaseResponse<>(true, HttpStatus.OK, ChatResultMessage.CHATROOM_CREATE_SUCCESS.getMessage(),
				chatroomId.toString()));
	}

	@GetMapping("/{roomId}")
	public ResponseEntity<BaseResponse<List<MessageResponse>>> getChatData(@AuthUser SecurityUser user,
		@PathVariable UUID roomId) {
		List<MessageResponse> messages = chattingService.getAllChattingData(user.getUser(), roomId);
		return ResponseEntity.ok(
			new BaseResponse<>(true, HttpStatus.OK, ChatResultMessage.MESSAGES_LOAD_SUCCESS.getMessage(), messages));
	}

}
