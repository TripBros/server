package com.tripbros.server.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.tripbros.server.security.TokenProvider;
import com.tripbros.server.security.UnauthorizedAccessException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StompHandler implements ChannelInterceptor {
	private final TokenProvider tokenProvider;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		if (StompCommand.CONNECT == accessor.getCommand()) {
			String jwt =  tokenProvider.extractJwt(accessor);
			if(!tokenProvider.validateToken(jwt))
				throw new UnauthorizedAccessException("인증 실패");
		}
		return message;
	}
}
