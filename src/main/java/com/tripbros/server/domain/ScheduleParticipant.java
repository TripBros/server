package com.tripbros.server.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@IdClass(ScheduleParticipantId.class)
@Getter
@NoArgsConstructor
public class ScheduleParticipant {
	@Id
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Id
	@ManyToOne
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;

	@Builder
	public ScheduleParticipant(User user, Schedule schedule) {
		this.user = user;
		this.schedule = schedule;
	}

}
