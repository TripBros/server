package com.tripbros.server.schedule.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.user.domain.User;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
	List<Schedule> findAllByUser(User user);
}
