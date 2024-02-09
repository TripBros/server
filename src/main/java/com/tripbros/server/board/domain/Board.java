package com.tripbros.server.board.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.enumerate.Age;
import com.tripbros.server.enumerate.Purpose;
import com.tripbros.server.enumerate.Sex;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;

	@Column(columnDefinition = "TEXT")
	private String content;

	@Enumerated(EnumType.STRING)
	private Purpose purpose;

	private String title;
	private Integer requiredHeadCount;

	private Boolean deadlineReachedFlag;
	private Long bookmarked;

	@Enumerated(EnumType.STRING)
	private Sex preferSex;

	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<Age> preferAgeRange = new ArrayList<>();

	private LocalDate createdAt;
	private Long hit;

	@Builder
	public Board(User user, Schedule schedule, String content, Purpose purpose, String title, Integer requiredHeadCount,
		Boolean deadlineReachedFlag, Long bookmarked, Sex preferSex, List<Age> preferAgeRange, LocalDate createdAt,
		Long hit) {
		this.user = user;
		this.schedule = schedule;
		this.content = content;
		this.purpose = purpose;
		this.title = title;
		this.requiredHeadCount = requiredHeadCount;
		this.deadlineReachedFlag = deadlineReachedFlag;
		this.bookmarked = bookmarked;
		this.preferSex = preferSex;
		this.preferAgeRange = preferAgeRange;
		this.createdAt = createdAt;
		this.hit = hit;
	}
}
