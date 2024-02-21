package com.tripbros.server.chatting.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.tripbros.server.chatting.dto.MessageRequest;
import com.tripbros.server.chatting.dto.MessageResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

	private final SimpMessagingTemplate template;

	@MessageMapping("/room/{roomId}")
	public void sendMessage(@DestinationVariable(value = "roomId") String roomId, MessageRequest request) {
		log.info("# roomId = {}", roomId);
		log.info("# message = {}", request);

		MessageResponse response = MessageResponse.builder().content(request.message()).time(LocalDateTime.now()).build();

		template.convertAndSend("/sub/room/" + roomId, response);
	}
}
