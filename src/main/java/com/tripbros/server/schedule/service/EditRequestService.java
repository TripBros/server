package com.tripbros.server.schedule.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.domain.ScheduleEditRequest;
import com.tripbros.server.schedule.dto.editrequest.EditRequestResponse;
import com.tripbros.server.schedule.dto.editrequest.ScheduleDTO;
import com.tripbros.server.schedule.repository.EditRequestRepository;
import com.tripbros.server.schedule.repository.ScheduleRepository;
import com.tripbros.server.user.domain.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class EditRequestService {
	private final EditRequestRepository requestRepository;
	private final ScheduleRepository scheduleRepository;

	public void sendEditRequest(Schedule hostSchedule) {
		List<User> userByHost = scheduleRepository.findUserByHost(hostSchedule);

		userByHost.forEach(u -> {
			ScheduleEditRequest request = ScheduleEditRequest.builder()
				.newSchedule(hostSchedule)
				.user(u)
				.build();
			requestRepository.save(request); // TODO: 추후 JDBC 직접 적용으로 BULK INSERT로 리팩토링
		});
		//todo: 시스템 채팅 메세지 전송
	}

	public void confirmEditRequest(User user, Long requestId) {
		ScheduleEditRequest request = requestRepository.findByIdAndUserWithNewSchedule(requestId, user)
			.orElseThrow(() -> new IllegalStateException("존재하지 않는 일정입니다."));
		Schedule oldSchedule = scheduleRepository.findByUserAndHost(user, request.getNewSchedule())
			.orElseThrow(() -> new IllegalStateException("존재하지 않는 일정입니다."));
		oldSchedule.editScheduleWithoutMemo(request.getNewSchedule());
		request.editStatus(false); //request 만료
		//TODO : 채팅메세지 보내야하는지 회의
	}

	public List<EditRequestResponse> getAllRequest(User user) {
		List<ScheduleEditRequest> requests = requestRepository.findAllByUserAndStatusIsTrue(user);
		return requests.stream()
			.map(r -> {
				Schedule oldSchedule = scheduleRepository.findByUserAndHost(user, r.getNewSchedule())
					.orElseThrow(() -> new IllegalStateException("존재하지 않는 일정입니다."));
				return new EditRequestResponse(r.getId(), ScheduleDTO.from(oldSchedule),
					ScheduleDTO.from(r.getNewSchedule()));
			})
			.toList();
	}
}
