package com.tripbros.server.domain;

import java.util.Date;

import com.tripbros.server.domain.enumerate.Age;
import com.tripbros.server.domain.enumerate.Purpose;
import com.tripbros.server.domain.enumerate.Sex;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

	private String title;
	private Long requiredHeadCount;
	private Purpose purpose;
	private boolean deadlineReachedFlag;
	private Long bookmarked;
	private Sex preferSex;
	private Age preferAge; // Age enum 미정
	private Date createdAt;
	private Long hit;

	@Builder
	public Board(User user, Schedule schedule, String content, String title, Long requiredHeadCount, Purpose purpose,
		boolean deadlineReachedFlag, Long bookmarked, Sex preferSex, Age preferAge, Date createdAt, Long hit) {
		this.user = user;
		this.schedule = schedule;
		this.content = content;
		this.title = title;
		this.requiredHeadCount = requiredHeadCount;
		this.purpose = purpose;
		this.deadlineReachedFlag = deadlineReachedFlag;
		this.bookmarked = bookmarked;
		this.preferSex = preferSex;
		this.preferAge = preferAge;
		this.createdAt = createdAt;
		this.hit = hit;
	}
}
