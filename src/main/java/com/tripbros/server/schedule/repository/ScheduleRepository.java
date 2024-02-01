package com.tripbros.server.schedule.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.schedule.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
}
