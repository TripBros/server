package com.tripbros.server.chatting.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.tripbros.server.user.repository.UserRepository;

@DataJpaTest
class ChatParticipantRepositoryTest {

	@Autowired
	private UserRepository userRepository;

}