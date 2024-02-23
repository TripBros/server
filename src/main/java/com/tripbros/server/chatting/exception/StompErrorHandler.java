package com.tripbros.server.chatting.exception;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import com.tripbros.server.security.UnauthorizedAccessException;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class StompErrorHandler extends StompSubProtocolErrorHandler {
	@Override
	public Message<byte[]> handleClientMessageProcessingError(Message<byte[]> clientMessage, Throwable ex) {
		final Throwable exception = convertException(ex);
		if (exception instanceof UnauthorizedAccessException) {
			return handleJwtException(clientMessage, exception);
		}
		return super.handleClientMessageProcessingError(clientMessage, exception);
	}

	private Message<byte[]> handleJwtException(Message<byte[]> clientMessage, Throwable ex) {
		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
		accessor.setLeaveMutable(true);

		return MessageBuilder.createMessage(ex.getMessage().getBytes(StandardCharsets.UTF_8), accessor.getMessageHeaders());
	}


	private Throwable convertException(Throwable ex) {
		if (ex instanceof MessageDeliveryException) {
			return ex.getCause();
		}
		return ex;
	}

}
