package com.tripbros.server.schedule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.GetScheduleResponseDTO;
import com.tripbros.server.user.domain.User;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
	@Query("select s.user from Schedule s join s.user where s.host = :hostSchedule")
	List<User> findUserByHost(Schedule hostSchedule);

	boolean existsByHost(Schedule hostSchedule);

	Optional<Schedule> findByUserAndHost(User user, Schedule hostSchedule);

	@Query("SELECT new com.tripbros.server.schedule.dto.GetScheduleResponseDTO("
		+ "s.id, s.title, s.locate.country, s.locate.city, s.placeId, s.placeName, "
		+ "CASE WHEN board.title IS NOT NULL THEN board.title END, "
		+ "s.hostFlag, s.startDate, s.endDate, s.memo)"
		+ "FROM Schedule s "
		+ "LEFT OUTER JOIN FETCH Board board "
		+ "ON board.schedule = s "
		+ "WHERE s.user = :user ")
	List<GetScheduleResponseDTO> findMyAllSchedules(User user);
}
