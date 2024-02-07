package com.tripbros.server.schedule.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

	public Schedule editSchedule(User user, EditScheduleRequestDTO editScheduleRequestDTO){
		// TODO : 일정 수정 관련 회의 필요
		Optional<Schedule> target = scheduleRepository.findById(editScheduleRequestDTO.id());
		if(target.isPresent()) {
			if (user.getId().equals(target.get().getUser().getId())) {
				Locate modifiedLocate = locateRepository.findByCountryAndCity(editScheduleRequestDTO.country(),
					editScheduleRequestDTO.city());
				Schedule result = target.get().editSchedule(editScheduleRequestDTO, modifiedLocate);

				log.info("success to edit schedule");
				return result;
			}
			else
				throw new SchedulePermissionException("사용자 권한이 유효하지 않습니다.");
		}
		else
			throw new SchedulePermissionException("존재하지 않은 일정입니다.");
	}

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
