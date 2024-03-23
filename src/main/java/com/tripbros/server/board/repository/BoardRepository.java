package com.tripbros.server.board.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.dto.GetBoardResponseDTO;

public interface BoardRepository extends JpaRepository<Board, Long> {

	@Query("SELECT distinct new com.tripbros.server.board.dto.GetBoardResponseDTO("
		+ "b.id, b.user.nickname, b.user.profileImage, b.hit, "
		+ "CASE WHEN bookmark.id IS NULL OR :userId IS NULL THEN false ELSE true END, "
		+ "b.user.age, b.user.sex, b.title, b.content, b.purpose, b.schedule.locate.country, b.schedule.locate.city, "
		+ "b.bookmarkedCount, b.preferSex, b.schedule.startDate, b.schedule.endDate, b.requiredHeadCount, "
		+ "b.nowHeadCount, b.schedule.placeId, b.schedule.placeName, b.schedule.placeLatitude, b.schedule.placeLongitude, b.chatCount, b.createdAt, b.preferAgeRange) "
		+ "FROM Board b "
		+ "LEFT OUTER JOIN FETCH BookmarkedBoard bookmark "
		+ "ON (bookmark.board.id = b.id AND bookmark.user.id = :userId) ")
	List<GetBoardResponseDTO> findAllGetDTO(@Param("userId") Long userId);

	@Query("SELECT distinct new com.tripbros.server.board.dto.GetBoardResponseDTO("
		+ "b.id, b.user.nickname, b.user.profileImage, b.hit, "
		+ "CASE WHEN bookmark.id IS NULL THEN false ELSE true END, "
		+ "b.user.age, b.user.sex, b.title, b.content, b.purpose, b.schedule.locate.country, b.schedule.locate.city, "
		+ "b.bookmarkedCount, b.preferSex, b.schedule.startDate, b.schedule.endDate, b.requiredHeadCount, "
		+ "b.nowHeadCount, b.schedule.placeId, b.schedule.placeName, b.schedule.placeLatitude, b.schedule.placeLongitude, b.chatCount, b.createdAt, b.preferAgeRange) "
		+ "FROM Board b "
		+ "LEFT OUTER JOIN FETCH BookmarkedBoard bookmark "
		+ "ON (bookmark.board.id = b.id AND bookmark.user.id = :userId) "
		+ "where b.user.id = :userId")
	List<GetBoardResponseDTO> findMyAllGetDto(@Param("userId") Long userId);

	List<Board> findByScheduleStartDateBefore(LocalDate curDate);
}
