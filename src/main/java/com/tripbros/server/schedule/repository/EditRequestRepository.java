package com.tripbros.server.schedule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tripbros.server.schedule.domain.ScheduleEditRequest;
import com.tripbros.server.user.domain.User;

public interface EditRequestRepository extends JpaRepository<ScheduleEditRequest, Long> {

	@Query("select r from ScheduleEditRequest r left join fetch r.newSchedule where r.user = :user")
	List<ScheduleEditRequest> findByUser(User user);

	@Query("select r from  ScheduleEditRequest r left join fetch r.newSchedule left join fetch r.newSchedule.locate join fetch r.user where r.id = :id and r.user = :user")
	Optional<ScheduleEditRequest> findByIdAndUserWithNewSchedule(Long id, User user);

	@Query("select r from ScheduleEditRequest r left join fetch r.newSchedule where r.user = :user")
	List<ScheduleEditRequest> findAllByUserAndStatusIsTrue(User user);

}
