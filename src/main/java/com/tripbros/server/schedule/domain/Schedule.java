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
	private String locateImage;

	private String placeName;
	private String placeId;
	private Double placeLatitude;
	private Double placeLongitude;

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
	public Schedule(User user, Locate locate, String locateImage, String placeName, String placeId, Double placeLatitude, Double placeLongitude, String title,
		LocalDate startDate, LocalDate endDate, boolean hostFlag, Schedule host, String memo) {
		this.user = user;
		this.locate = locate;
		this.locateImage = locateImage;
		this.placeName = placeName;
		this.placeId = placeId;
		this.placeLatitude = placeLatitude;
		this.placeLongitude = placeLongitude;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.hostFlag = hostFlag;
		this.host = host;
		this.memo = memo;
	}

	public Schedule editSchedule(EditScheduleRequestDTO requestDTO, Locate locate, String locateImage){
		this.title = requestDTO.title();
		this.locate = locate;
		this.locateImage = locateImage;
		this.startDate = requestDTO.startDate();
		this.endDate = requestDTO.endDate();
		this.memo = requestDTO.memo();
		this.placeId = requestDTO.placeId();
		this.placeName = requestDTO.placeName();
		this.placeLatitude = requestDTO.placeLatitude();
		this.placeLongitude = requestDTO.placeLongitude();
		return this;
	}

	public void editScheduleWithoutMemo(Schedule schedule){
		this.title = schedule.getTitle();
		this.locate = schedule.getLocate();
		this.startDate = schedule.getStartDate();
		this.endDate = schedule.getEndDate();
		this.placeId = schedule.getPlaceId();
		this.placeName = schedule.getPlaceName();
		this.placeLatitude = schedule.getPlaceLatitude();
		this.placeLongitude = schedule.getPlaceLongitude();
	}

	public Schedule setHost(Schedule host){
		this.host = host;
		return this;
	}

	public void setOwner(User user) {
		this.user = user;
	}

	public static Schedule copyValueWithoutMemo(Schedule hostSchedule, User user) {
		return Schedule.builder()
			.user(user)
			.host(hostSchedule)
			.locate(hostSchedule.getLocate())
			.placeName(hostSchedule.getPlaceName())
			.placeId(hostSchedule.getPlaceId())
			.placeLatitude(hostSchedule.getPlaceLatitude())
			.placeLongitude(hostSchedule.getPlaceLongitude())
			.title(hostSchedule.getTitle())
			.startDate(hostSchedule.getStartDate())
			.endDate(hostSchedule.getEndDate())
			.hostFlag(false)
			.build();
	}
}
