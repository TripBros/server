package com.tripbros.server.board.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.domain.BookmarkedBoard;
import com.tripbros.server.board.dto.GetBookmarkedBoardResponseDTO;
import com.tripbros.server.user.domain.User;

public interface BookmarkedBoardRepository extends JpaRepository<BookmarkedBoard, Long> {
	Optional<BookmarkedBoard> findByUserAndBoard(User user, Board board);

	@Query("SELECT distinct new com.tripbros.server.board.dto.GetBookmarkedBoardResponseDTO("
		+ "b.id, b.user.nickname, b.user.profileImage, b.hit, "
		+ "b.user.age, b.user.sex, b.title, b.content, b.purpose, b.schedule.locate.country, b.schedule.locate.city, "
		+ "b.bookmarkedCount, b.preferSex, b.schedule.startDate, b.schedule.endDate, b.requiredHeadCount, "
		+ "b.nowHeadCount, b.schedule.placeId, b.schedule.placeName, b.schedule.placeUrl, b.chatCount, b.createdAt, b.preferAgeRange) "
		+ "FROM Board b "
		+ "LEFT OUTER JOIN FETCH BookmarkedBoard bookmark "
		+ "ON (bookmark.board.id = b.id AND bookmark.user.id = :userId)"
		+ "WHERE bookmark.user.id = :userId ")
	List<GetBookmarkedBoardResponseDTO> findByUser(@Param("userId") Long userId);
}
