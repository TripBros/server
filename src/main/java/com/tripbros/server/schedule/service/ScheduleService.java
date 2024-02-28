package com.tripbros.server.schedule.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.chatting.service.ChattingService;
import com.tripbros.server.common.exception.UserPermissionException;
import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.recommend.repository.LocateRepository;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.schedule.dto.GetScheduleResponseDTO;
import com.tripbros.server.schedule.exception.SchedulePermissionException;
import com.tripbros.server.schedule.repository.ScheduleRepository;
import com.tripbros.server.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final LocateRepository locateRepository;
	private final ChattingService chattingService;
	private final EditRequestService editRequestService;

	public Schedule createSchedule(User user, CreateScheduleRequestDTO createScheduleRequestDTO){
		Schedule schedule = createScheduleRequestDTO.toEntity(user, locateRepository);
		scheduleRepository.save(schedule);

		log.info("success to create schedule");
		return schedule;
	}

	public Schedule editSchedule(User user, EditScheduleRequestDTO editScheduleRequestDTO){
		// TODO : 동행 일정 수정 시, 푸시 알림 전송 및 동기화 작업 필요
		Optional<Schedule> target = scheduleRepository.findById(editScheduleRequestDTO.id());

		Schedule schedule = target.orElseThrow(
			() -> new SchedulePermissionException("존재하지 않은 일정입니다."));
		if(user != null)
			checkUserPermission(user, schedule);
		if (!schedule.isHostFlag())
			throw new SchedulePermissionException("동행 일정은 관리자만 수정 할 수 있습니다.");

		Locate modifiedLocate = locateRepository.findByCountryAndCity(editScheduleRequestDTO.country(), editScheduleRequestDTO.city());
		Schedule result = target.get().editSchedule(editScheduleRequestDTO, modifiedLocate);
		if (scheduleRepository.existsByHost(schedule)) { //동행 일정인 경우
			editRequestService.sendEditRequest(schedule);
		}

		log.info("success to edit schedule");
		return result;
	}

	public List<GetScheduleResponseDTO> getSchedules(User user){
		List<Schedule> scheduleList = scheduleRepository.findAllByUser(user);
		List<GetScheduleResponseDTO> responseList = scheduleList.stream()
			.map(GetScheduleResponseDTO::toDTO)
			.toList();

		log.info("success to get schedules");
		return responseList;
	}

	public void deleteSchedule(User user, Long scheduleId) {
		Optional<Schedule> target = scheduleRepository.findById(scheduleId);

		Schedule schedule = target.orElseThrow(
			() -> new SchedulePermissionException("존재하지 않은 일정입니다."));
		if(user != null)
			checkUserPermission(user, schedule);
		if (scheduleRepository.existsByHost(schedule))
			throw new SchedulePermissionException("동행자가 존재하는 일정은 삭제 할 수 없습니다.");

		scheduleRepository.deleteById(scheduleId);

		log.info("success to delete schedule");
	}

	private static void checkUserPermission(User user, Schedule schedule) {
		if (!schedule.getUser().getId().equals(user.getId()))
			throw new UserPermissionException();
	}

	public void joinCompanionSchedule(User user, User opponent, Board board) {
		//board에는 schedule, user가 fetch join 되어 있음
		User hostUser = board.getUser();
		Schedule baseSchedule = board.getSchedule();
		baseSchedule.setOwner(hostUser); //update (Dirty Checking)

		User nonHostUser = findNonHostUser(user, opponent, hostUser);
		Schedule copiedSchedule = Schedule.copyValueWithoutMemo(baseSchedule, nonHostUser);
		scheduleRepository.save(copiedSchedule);

		List<User> userByHost = scheduleRepository.findUserByHost(baseSchedule);
		userByHost.add(hostUser);
		if (userByHost.size() >= 3) {
			chattingService.getGroupChatroom(userByHost, board);
		}
	}

	private static User findNonHostUser(User user, User opponent, User hostUser) {
		return Stream.of(user, opponent)
			.filter(u -> !u.getId().equals(hostUser.getId()))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("오류가 발생했습니다 : Host User Not Found"));
	}

}