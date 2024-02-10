package com.tripbros.server.user.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import com.tripbros.server.common.dto.BaseResponse;
import com.tripbros.server.enumerate.Sex;
import com.tripbros.server.security.SecurityUser;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.user.dto.EditUserInfoRequest;
import com.tripbros.server.user.dto.RegisterRequest;
import com.tripbros.server.user.enumerate.UserResultMessage;
import com.tripbros.server.user.exception.RegisterException;
import com.tripbros.server.user.exception.UserExceptionMessage;
import com.tripbros.server.user.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class UserRegisterServiceTest {

	@Autowired
	private UserRegisterService service;

	@Autowired
	private UserRepository repository;

	@PersistenceContext
	private EntityManager entityManager;

	private RegisterRequest request;
	private ResponseEntity<BaseResponse<Object>> response;

	@BeforeEach
	void 전처리(){
		 request = new RegisterRequest("emailtest@email.com"
			, "password"
			, "testnickname"
			, 30L
			, Sex.MALE
			, true
			, true
			, true
			, true
			, true
			, "testimage");

		BaseResponse<Object> r = new BaseResponse<>(true, HttpStatus.OK,
			UserResultMessage.REGISTER_SUCCESS.getMessage(), service.register(request));
		response = ResponseEntity.ok(r);
	}

	@Test
	@DisplayName("회원 가입 실패 (이메일 중복)")
	void register_failed_email() {
		//give
		//전처리로 수행

		//when && then
		Assertions.assertThatThrownBy(() -> service.register(request)).isInstanceOf(RegisterException.class)
			.hasMessage(UserExceptionMessage.EMAIL_ALREADY_EXIST.getMessage());
	}

	@Test
	@DisplayName("회원 가입 실패 (닉네임 중복)")
	void 닉네임중복() {
		//give


		RegisterRequest request1 = new RegisterRequest("22email@email.com"
			, "password"
			, "testnickname"
			, 30L
			, Sex.MALE
			, true
			, true
			, true
			, true
			, true
			, "testimage");

		//when && then
		Assertions.assertThatThrownBy(() -> service.register(request1)).isInstanceOf(RegisterException.class)
			.hasMessage(UserExceptionMessage.NICKNAME_ALREADY_EXIST.getMessage());
	}

	@Test
	@DisplayName("회원 가입 성공")
	void 회원가입성공(){
		//give && when
		//전처리로 수행

		//then
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getBody().data()).isEqualTo(null);
	}


	@Test
	@DisplayName("이메일 중복 검사 실패")
	void 이메일_중복_검사(){
		//give
		//전처리로 수행

		//when && then
		Assertions.assertThatThrownBy(() -> service.checkEmailDuplication("emailtest@email.com"))
			.isInstanceOf(RegisterException.class)
			.hasMessage(UserExceptionMessage.EMAIL_ALREADY_EXIST.getMessage());

	}

	@Test
	@DisplayName("닉네임 중복 검사 실패")
	void 닉네임_중복_검사(){
		//give
		//전처리로 수행

		//when && then
		Assertions.assertThatThrownBy(() -> service.checkNicknameDuplication("testnickname"))
			.isInstanceOf(RegisterException.class)
			.hasMessage(UserExceptionMessage.NICKNAME_ALREADY_EXIST.getMessage());

	}

	@Test
	@DisplayName("회원 정보 수정")
	void 정보수정(){
		SecurityUser securityUser = new SecurityUser(repository.findByEmail(request.email()).orElseThrow(() -> new RegisterException("err")));
		EditUserInfoRequest newRequest = new EditUserInfoRequest(null, "editestNickname!!!!", false, false, false,
			false, false, null);
		service.editInfo(newRequest, securityUser);
		entityManager.flush();
		entityManager.clear();

		User user = repository.findByEmail(request.email()).get();
		Assertions.assertThat(user.getNickname()).isEqualTo("editestNickname!!!!");

	}
}