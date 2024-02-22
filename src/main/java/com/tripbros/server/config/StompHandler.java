package com.tripbros.server.config;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.tripbros.server.security.TokenProvider;
import com.tripbros.server.security.UnauthorizedAccessException;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class StompHandler implements ChannelInterceptor {
	private final TokenProvider tokenProvider;
	private final UserRepository userRepository;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		Map<String, Object> sessionAttributes = accessor.getSessionAttributes();

		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String jwt =  tokenProvider.extractJwtFromStomp(accessor);
			if (jwt == null || !tokenProvider.validateToken(jwt)) {
				throw new UnauthorizedAccessException("인증 실패");
			}
			String email = tokenProvider.getUserEmailFromToken(jwt);
			log.info("presend:email = {}", email);
			User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException());
			sessionAttributes.put("userId", user.getId());
			sessionAttributes.put("userNickname", user.getNickname());
			sessionAttributes.put("userProfileImage", user.getProfileImage());
			accessor.setSessionAttributes(sessionAttributes);

		}
		if (StompCommand.SEND.equals(accessor.getCommand())) {
			accessor.setNativeHeader("userId", sessionAttributes.get("userId").toString());
			accessor.setNativeHeader("userNickname", sessionAttributes.get("userNickname").toString());
			accessor.setNativeHeader("userProfileImage", sessionAttributes.get("userProfileImage").toString());
			message = MessageBuilder.createMessage(message.getPayload(), accessor.toMessageHeaders());
		}
		return message;
	}


}
