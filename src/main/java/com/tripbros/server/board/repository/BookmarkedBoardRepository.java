package com.tripbros.server.board.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.domain.BookmarkedBoard;
import com.tripbros.server.user.domain.User;

public interface BookmarkedBoardRepository extends JpaRepository<BookmarkedBoard, Long> {
	Optional<BookmarkedBoard> findByUserAndBoard(User user, Board board);
}
