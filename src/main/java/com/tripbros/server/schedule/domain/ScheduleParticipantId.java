package com.tripbros.server.schedule.domain;

import java.io.Serializable;
import java.util.Objects;

import com.tripbros.server.user.domain.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ScheduleParticipantId implements Serializable {
	private User user;
	private Schedule schedule;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ScheduleParticipantId that = (ScheduleParticipantId)o;
		return Objects.equals(user, that.user) && Objects.equals(schedule, that.schedule);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, schedule);
	}
}
