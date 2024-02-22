package com.tripbros.server.chatting.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripbros.server.chatting.dto.MessageRequest;
import com.tripbros.server.chatting.dto.MessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

	private final SimpMessagingTemplate template;

	@MessageMapping("/room/{roomId}")
	public void sendMessage(@DestinationVariable(value = "roomId") String roomId, MessageRequest request) {
		log.info("# roomId = {}", roomId);
		log.info("# message = {}", request);

		MessageResponse response = MessageResponse.builder()
			.sentAt(LocalDateTime.now())
			.content(request.message())
			.isSystemMessage(false)
			.profileImage("https://upload.wikimedia.org/wikipedia/commons/thumb/e/e1/Google_Chrome_icon_%28February_2022%29.svg/800px-Google_Chrome_icon_%28February_2022%29.svg.png")
			.userName("mockedUserName")
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
}
