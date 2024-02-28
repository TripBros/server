package com.tripbros.server.schedule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.user.domain.User;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
	List<Schedule> findAllByUser(User user);

	@Query("select s.user from Schedule s join s.user where s.host = :hostSchedule")
	List<User> findUserByHost(Schedule hostSchedule);

	boolean existsByHost(Schedule hostSchedule);

	Optional<Schedule> findByUserAndHost(User user, Schedule hostSchedule);
}
