package com.tripbros.server.schedule.dto.editrequest;

public record EditRequestResponse(
	Long requestId,
	ScheduleDTO oldSchedule,
	ScheduleDTO newSchedule
) {
}
