package com.tripbros.server.board.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.domain.PreferAgeRange;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.enumerate.Country;
import com.tripbros.server.enumerate.Purpose;
import com.tripbros.server.enumerate.Sex;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.user.domain.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBoardRequestDTO(@NotBlank(message = "게시글 제목은 필수 작성입니다.") String title,
									@NotBlank(message = "게시글 본문은 필수 작성입니다.") String content,
									@NotNull(message = "동행 일정의 국가 선택은 필수입니다.") Country country,
									@NotNull(message = "동행 일정의 도시 선택은 필수입니다.") City city,
									@NotNull(message = "동행 시작 날짜는 필수 입력입니다.") LocalDate startDate,
									@NotNull(message = "동행 종료 날짜는 필수 입력입니다.") LocalDate endDate,
									@NotNull(message = "일정 목적 선택은 필수입니다.") Purpose purpose,
									@NotNull(message = "동행 모집 인원은 필수 선택입니다.") Integer requiredHeadCount,
									@NotNull(message = "동행 선호 나이대는 필수 선택입니다.") Boolean twentiesFlag,
									@NotNull(message = "동행 선호 나이대는 필수 선택입니다.") Boolean thirtiesFlag,
									@NotNull(message = "동행 선호 나이대는 필수 선택입니다.") Boolean fortiesFlag,
									@NotNull(message = "동행 선호 나이대는 필수 선택입니다.") Boolean fiftiesFlag,
									@NotNull(message = "동행 선호 나이대는 필수 선택입니다.") Boolean sixtiesAboveFlag,
									@NotNull(message = "동행 선호 나이대는 필수 선택입니다.") Boolean unrelatedFlag,
									@NotNull(message = "동행 선호 성별은 필수 선택입니다.") Sex preferSex,
									LocalDateTime createAt,
									Long placeId,
									String placeName,
									String placeUrl) {

	public Board toEntity(User user, Schedule schedule, PreferAgeRange preferAgeRange){

		return Board.builder()
			.user(user)
			.title(title)
			.content(content)
			.schedule(schedule)
			.purpose(purpose)
			.requiredHeadCount(requiredHeadCount)
			.preferSex(preferSex)
			.preferAgeRange(preferAgeRange)
			.createdAt(createAt)
			.deadlineReachedFlag(false)
			.hit(0L)
			.bookmarkedCount(0L)
			.nowHeadCount(1)
			.build();
	}
}
