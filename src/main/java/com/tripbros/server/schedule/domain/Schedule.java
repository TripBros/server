package com.tripbros.server.schedule.domain;

import java.time.LocalDate;

import com.tripbros.server.recommend.domain.Locate;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locate_id")
	private Locate locate;

	private String placeName;
	private Long placeId;
	private String placeUrl;

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
	public Schedule(User user, Locate locate, String placeName, Long placeId, String placeUrl, String title,
		LocalDate startDate, LocalDate endDate, boolean hostFlag, Schedule host, String memo) {
		this.user = user;
		this.locate = locate;
		this.placeName = placeName;
		this.placeId = placeId;
		this.placeUrl = placeUrl;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hostFlag = hostFlag;
		this.host = host;
		this.memo = memo;
	}

	public Schedule editSchedule(EditScheduleRequestDTO requestDTO, Locate locate){
		this.title = requestDTO.title();
		this.locate = locate;
		this.startDate = requestDTO.startDate();
		this.endDate = requestDTO.endDate();
		this.memo = requestDTO.memo();
		this.placeId = requestDTO.placeId();
		this.placeName = requestDTO.placeName();
		this.placeUrl = requestDTO.placeUrl();
		return this;
	}

	public Schedule setHost(Schedule host){
		this.host = host;
		return this;
	}
}
