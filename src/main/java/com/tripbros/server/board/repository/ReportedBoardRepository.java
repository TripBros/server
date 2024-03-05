package com.tripbros.server.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tripbros.server.board.domain.ReportedBoard;

public interface ReportedBoardRepository extends JpaRepository<ReportedBoard, Long> {
}
