package com.tripbros.server.chatting.repository;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.repository.BoardRepository;
import com.tripbros.server.chatting.domain.Chatroom;
import com.tripbros.server.chatting.domain.ChatroomParticipant;
import com.tripbros.server.config.QuerydslConfig;
import com.tripbros.server.enumerate.Role;
import com.tripbros.server.enumerate.Sex;
import com.tripbros.server.user.domain.TravelStyle;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.user.repository.TravelStyleRepository;
import com.tripbros.server.user.repository.UserRepository;

import jakarta.persistence.EntityManager;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatParticipantRepositoryTest {

	private final Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private ChatroomRepository chatroomRepository;

	@Autowired
	private ChatParticipantRepository participantRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BoardRepository boardRepository;

	@Autowired
	private TravelStyleRepository styleRepository;

	@Autowired
	EntityManager entityManager;



	@DisplayName("기존 채팅방 없음 감지")
	@Test
	void 채팅방_존재하지_않음(){
		User user1 = user("test111@test.com");
		User user2 = user("test222@test.com");
		userRepository.save(user1);
		userRepository.save(user2);

		Board board = board(user1);
		boardRepository.save(board);

		Chatroom chatroom = chatroom(board);
		chatroomRepository.save(chatroom);


		Assertions.assertThat(participantRepository.findSingleRoomByBoardAndUser(board, user2).isEmpty()).isTrue();
	}

	@DisplayName("기존 채팅방 존재")
	@Test
	void 채팅방_존재() {
		User user1 = user("test111@test.com");
		User user2 = user("test222@test.com");
		userRepository.save(user1);
		userRepository.save(user2);

		Board board = board(user1);
		boardRepository.save(board);

		Chatroom chatroom = chatroom(board);
		chatroomRepository.save(chatroom);


		ChatroomParticipant part1 = ChatroomParticipant.builder()
			.user(user1)
			.chatroom(chatroom)
			.build();
		ChatroomParticipant part2 = ChatroomParticipant.builder()
			.user(user2)
			.chatroom(chatroom)
			.build();

		entityManager.flush();

		participantRepository.save(part1);
		participantRepository.save(part2);

		log.info("-------------------------------------");

		Assertions.assertThat(participantRepository.findSingleRoomByBoardAndUser(board, user2).isEmpty()).isFalse();
	}

	private User user(String email) {
		TravelStyle travelStyle = travelStyle();
		styleRepository.save(travelStyle);
		return User.builder()
			.role(Role.ROLE_USER)
			.email(email)
			.sex(Sex.MALE)
			.travelStyle(travelStyle)
			.nickname("nickname")
			.password("pwpwpwpw")
			.age(2002)
			.profileImage(null)
			.build();
	}

	private Board board(User user) {
		return Board.builder()
			.content("내용")
			.hit(0L)
			.chatCount(0)
			.title("제목")
			.preferAgeRange(null)
			.bookmarkedCount(0L)
			.createdAt(LocalDateTime.now())
			.deadlineReachedFlag(false)
			.user(user)
			.build();
	}

	private Chatroom chatroom(Board board) {
		return Chatroom.builder()
			.title("채팅제목")
			.status(true)
			.updatedAt(LocalDateTime.now())
			.board(board)
			.isGroupChat(false)
			.build();
	}

	private TravelStyle travelStyle() {
		return new TravelStyle(false, false, false, false, false);
	}

}