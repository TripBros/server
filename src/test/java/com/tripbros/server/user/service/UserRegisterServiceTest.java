package com.tripbros.server.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tripbros.server.enumerate.Role;
import com.tripbros.server.enumerate.Sex;
import com.tripbros.server.user.domain.TravelStyle;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.user.dto.EditUserInfoRequest;
import com.tripbros.server.user.dto.RegisterRequest;
import com.tripbros.server.user.exception.RegisterException;
import com.tripbros.server.user.exception.UserExceptionMessage;
import com.tripbros.server.user.repository.TravelStyleRepository;
import com.tripbros.server.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserRegisterServiceTest {

	@InjectMocks
	private UserRegisterService service;

	@Mock
	private UserRepository repository;

	@Mock
	private TravelStyleRepository travelStyleRepository;

	@Mock
	private ImageService imageService;

	@Spy
	private BCryptPasswordEncoder encoder;


	private RegisterRequest request;

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
			, true);
	}

	@Test
	@DisplayName("회원 가입 실패 (이메일 중복)")
	void register_failed_email() {
		//give
		doReturn(true).when(repository).existsByEmail(any(String.class));
		//when && then
		assertThatThrownBy(() -> service.register(request, null)).isInstanceOf(RegisterException.class)
			.hasMessage(UserExceptionMessage.EMAIL_ALREADY_EXIST.getMessage());

		//verify
		verify(repository, times(1)).existsByEmail(any(String.class));

	}

	private TravelStyle getTravelStyle() {
		return TravelStyle.builder()
			.leisurePreferFlag(request.leisurePreferFlag())
			.adventurePreferFlag(request.adventurePreferFlag())
			.photoPreferFlag(request.photoPreferFlag())
			.planPreferFlag(request.planPreferFlag())
			.vehiclePreferFlag(request.vehiclePreferFlag())
			.build();
	}

	@Test
	@DisplayName("회원 가입 실패 (닉네임 중복)")
	void 닉네임중복() {
		//give
		doReturn(true).when(repository).existsByNickname(any(String.class));
		//when && then
		assertThatThrownBy(() -> service.register(request, null)).isInstanceOf(RegisterException.class)
			.hasMessage(UserExceptionMessage.NICKNAME_ALREADY_EXIST.getMessage());

		//verify
		verify(repository, times(1)).existsByNickname(any(String.class));
	}
	//
	@Test
	@DisplayName("회원 가입 성공")
	void 회원가입성공(){
		//give
		TravelStyle style = getTravelStyle();
		User user = request.toEntity(encoder.encode(request.password()), null, style, Role.ROLE_USER);
		doReturn(user).when(repository).save(any(User.class));
		doReturn(style).when(travelStyleRepository).save(any(TravelStyle.class));
		doReturn(null).when(imageService).uploadImageAndGetName(any());

		//when
		User registered = service.register(request, null);

		//then
		assertThat(registered.getEmail()).isEqualTo(request.email());
		assertThat(encoder.matches(request.password(), registered.getPassword())).isTrue();
	}




	@Test
	@DisplayName("회원 정보 수정")
	void 정보수정(){

		//give
		TravelStyle style = getTravelStyle();
		User user = request.toEntity(encoder.encode(request.password()), "url", style, Role.ROLE_USER);
		doReturn(user).when(repository).findByIdWithTravelStyle(any());
		doReturn("changed_image_url").when(imageService).uploadImageAndGetName(any());
		EditUserInfoRequest infoRequest = new EditUserInfoRequest("password", "newnickname", false, false,
			false, false, false);

		//when
		User edited = service.editInfo(infoRequest, new MockMultipartFile("changed_image_url", (byte[])null),
			user);
		//then
		assertThat(edited.getProfileImage()).isEqualTo("changed_image_url");
		assertThat(edited.getNickname()).isEqualTo("newnickname");

		//verify
		verify(imageService, times(1)).deleteImage(any(String.class));
		verify(repository, times(1)).existsByNickname(any(String.class));

	}
}