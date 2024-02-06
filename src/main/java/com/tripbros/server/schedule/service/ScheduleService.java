package com.tripbros.server.schedule.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripbros.server.recommend.repository.LocateRepository;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.GetScheduleResponseDTO;
import com.tripbros.server.schedule.exception.SchedulePermissionException;
import com.tripbros.server.schedule.repository.ScheduleRepository;
import com.tripbros.server.user.domain.User;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {

	private final ScheduleRepository scheduleRepository;
	private final LocateRepository locateRepository;

	public Schedule createSchedule(User user, CreateScheduleRequestDTO createScheduleRequestDTO){
		Schedule schedule = createScheduleRequestDTO.toEntity(user, locateRepository);
		scheduleRepository.save(schedule);
		log.info("success to create schedule");
		return schedule;
	}

	// public ResponseEntity<BaseResponse<Object>> editSchedule(User user, EditScheduleRequestDTO editScheduleRequestDTO){
	// 	// TODO : 일정 수정 관련 회의 필요
	// 	scheduleRepository.deleteById(editScheduleRequestDTO.id());
	//
	// 	Schedule schedule = editScheduleRequestDTO.toEntity(user,locateRepository);
	// 	scheduleRepository.save(schedule);
	//
	// 	BaseResponse<Object> response = new BaseResponse<>(true, HttpStatus.OK,
	// 		ScheduleResultMessage.UPDATE_SCHEDULE_SUCCESS.getMessage(), null);
	//
	// 	log.info("success to edit schedule");
	// 	return ResponseEntity.ok().body(response);
	// }

	public List<GetScheduleResponseDTO> getSchedules(User user){
		List<Schedule> scheduleList = scheduleRepository.findAllByUser(user);
		List<GetScheduleResponseDTO> responseList = scheduleList.stream()
			.map(GetScheduleResponseDTO::toDTO)
			.toList();

		log.info("success to get schedules");
		return responseList;
	}

	public void deleteSchedule(@Valid User user, @Valid Long scheduleId) {
		Optional<Schedule> target = scheduleRepository.findById(scheduleId);
		target.ifPresentOrElse(
			s -> {
				if (!s.getUser().getId().equals(user.getId())) {
					throw new SchedulePermissionException("사용자 권한이 유효하지 않습니다.");
				}
				scheduleRepository.deleteById(scheduleId);
				log.info("success to delete schedule");
			},
			() -> {
				throw new SchedulePermissionException("존재하지 않은 일정입니다.");
			}
		);
	}

}
