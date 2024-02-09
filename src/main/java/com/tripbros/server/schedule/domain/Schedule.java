package com.tripbros.server.schedule.domain;

import java.time.LocalDate;

import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "locate_id")
	private Locate locate;

	private String title;
	private LocalDate startDate;
	private LocalDate endDate;

	private boolean hostFlag;

	@ManyToOne
	@JoinColumn(name = "host_id")
	private Schedule host;

	@Column(columnDefinition = "TEXT")
	private String memo;

	@Builder
	public Schedule(User user, Locate locate, String title, LocalDate startDate, LocalDate endDate, boolean hostFlag,
		String memo) {
		this.user = user;
		this.locate = locate;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hostFlag = hostFlag;
		this.memo = memo;
	}

	public Schedule editSchedule(EditScheduleRequestDTO requestDTO, Locate locate){
		this.title = requestDTO.title();
		this.locate = locate;
		this.startDate = requestDTO.startDate();
		this.endDate = requestDTO.endDate();
		this.memo = requestDTO.memo();
		return this;
	}

	public Schedule setHost(Schedule host){
		this.host = host;
		return this;
	}
}
