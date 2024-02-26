package com.tripbros.server.schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.schedule.domain.WaitingCompanion;
import com.tripbros.server.user.domain.User;

public interface WaitingRepository extends JpaRepository<WaitingCompanion, Long> {


	@Query("select c from WaitingCompanion c join fetch c.board "
		+ "where c.opponent = :opponent and c.user = :user and c.board = :board")
	Optional<WaitingCompanion> findByUserAndOpponentAndBoard(User user, User opponent, Board board);

	boolean existsByUserAndOpponentAndBoard(User user, User opponent, Board board);


}
