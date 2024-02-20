package com.tripbros.server.board.domain;

import java.time.LocalDateTime;

import com.tripbros.server.board.dto.EditBoardRequestDTO;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.user.domain.User;
import com.tripbros.server.enumerate.Purpose;
import com.tripbros.server.enumerate.Sex;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
	private Integer nowHeadCount;

	private Boolean deadlineReachedFlag;
	private Long bookmarkedCount;

	@Enumerated(EnumType.STRING)
	private Sex preferSex;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prefer_age_range_id")
	private PreferAgeRange preferAgeRange;

	private LocalDateTime createdAt;
	private Long hit;

	private Integer chatCount;

	@Builder
	public Board(User user, Schedule schedule, String content, Purpose purpose, String title, Integer requiredHeadCount,
		Integer nowHeadCount, Boolean deadlineReachedFlag, Long bookmarkedCount, Sex preferSex,
		PreferAgeRange preferAgeRange, LocalDateTime createdAt, Long hit, Integer chatCount) {
		this.user = user;
		this.schedule = schedule;
		this.content = content;
		this.purpose = purpose;
		this.title = title;
		this.requiredHeadCount = requiredHeadCount;
		this.nowHeadCount = nowHeadCount;
		this.deadlineReachedFlag = deadlineReachedFlag;
		this.bookmarkedCount = bookmarkedCount;
		this.preferSex = preferSex;
		this.preferAgeRange = preferAgeRange;
		this.createdAt = createdAt;
		this.hit = hit;
		this.chatCount = chatCount;
	}


	public Board editBoard(EditBoardRequestDTO editBoardRequestDTO, Schedule schedule){
		this.title = editBoardRequestDTO.title();
		this.content = editBoardRequestDTO.content();
		this.schedule = schedule;
		this.purpose = editBoardRequestDTO.purpose();
		this.requiredHeadCount = editBoardRequestDTO.requiredHeadCount();
		this.preferSex = editBoardRequestDTO.preferSex();
		this.preferAgeRange.editPreferAgeRange(editBoardRequestDTO);

		return this;
	}

	public void updateBookmarkedCount(Long bookmarkedCount){
		this.bookmarkedCount += bookmarkedCount;
	}
}
