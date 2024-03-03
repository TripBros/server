package com.tripbros.server.board.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.tripbros.server.board.domain.Board;
import com.tripbros.server.board.domain.BookmarkedBoard;
import com.tripbros.server.board.domain.PreferAgeRange;
import com.tripbros.server.board.dto.CreateBoardRequestDTO;
import com.tripbros.server.board.dto.EditBoardRequestDTO;
import com.tripbros.server.board.dto.GetBoardResponseDTO;
import com.tripbros.server.board.dto.GetBookmarkedBoardResponseDTO;
import com.tripbros.server.board.exception.BoardPermissionException;
import com.tripbros.server.board.repository.BoardRepository;
import com.tripbros.server.board.repository.BookmarkedBoardRepository;
import com.tripbros.server.board.repository.PreferAgeRangeRepository;
import com.tripbros.server.common.exception.UserPermissionException;
import com.tripbros.server.enumerate.City;
import com.tripbros.server.schedule.domain.Schedule;
import com.tripbros.server.schedule.dto.CreateScheduleRequestDTO;
import com.tripbros.server.schedule.dto.EditScheduleRequestDTO;
import com.tripbros.server.schedule.service.ScheduleService;
import com.tripbros.server.security.SecurityUser;
import com.tripbros.server.user.domain.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;
	private final BookmarkedBoardRepository bookmarkedBoardRepository;
	private final PreferAgeRangeRepository preferAgeRangeRepository;

	private final ScheduleService scheduleService;

	public Board createBoard(User user, CreateBoardRequestDTO createBoardRequestDTO){
		Schedule schedule = createCompanionSchedule(createBoardRequestDTO);

		PreferAgeRange preferAgeRange = getPreferAgeRange(createBoardRequestDTO);

		Board board = createBoardRequestDTO.toEntity(user, schedule, preferAgeRange);
		boardRepository.save(board);

		log.info("success to create board");
		return board;
	}

	public Board editBoard(User user, EditBoardRequestDTO editBoardRequestDTO){
		Optional<Board> target = boardRepository.findById(editBoardRequestDTO.id());

		Board board = target.orElseThrow(() -> new BoardPermissionException("존재하지 않은 게시글 입니다."));
		checkUserPermission(user, board.getUser().getId());

		// 동행 일정 수정
		String newTitle = getCompanionTitle(editBoardRequestDTO.city(),editBoardRequestDTO.title());
		EditScheduleRequestDTO editScheduleRequest = new EditScheduleRequestDTO(board.getSchedule().getId(),
			newTitle,
			editBoardRequestDTO.country(),
			editBoardRequestDTO.city(),
			editBoardRequestDTO.placeId(),
			editBoardRequestDTO.placeName(),
			editBoardRequestDTO.placeUrl(),
			editBoardRequestDTO.startDate(),
			editBoardRequestDTO.endDate(),
			null);

		Schedule editedSchedule = scheduleService.editSchedule(null, editScheduleRequest);

		// 게시글 수정
		Board result = board.editBoard(editBoardRequestDTO, editedSchedule);

		log.info("success to edit board");
		return result;
	}

	public List<GetBoardResponseDTO> getBoards(SecurityUser user){
		List<GetBoardResponseDTO> response;

		if(user == null)
			response = boardRepository.findAllGetDTO(null);
		else
		 	response = boardRepository.findAllGetDTO(user.getUser().getId());

		log.info("success to get boards");
		return response;
	}

	public List<GetBoardResponseDTO> getMyBoards(User user) {
		return boardRepository.findMyAllGetDto(user.getId());
	}

	public void updateBoardHit(Long boardId){
		Optional<Board> target = boardRepository.findById(boardId);
		Board board = target.orElseThrow(
			() -> new BoardPermissionException("존재하지 않은 게시글 입니다.")
		);
		board.updateBoardHit();
		log.info("success to update board hit");
	}

	public void updateDeadlineReached(User user, Long boardId){
		Optional<Board> target = boardRepository.findById(boardId);

		Board board = target.orElseThrow(
			() -> new BoardPermissionException("존재하지 않은 게시글 입니다.")
		);
		checkUserPermission(user, board.getUser().getId());

		board.updateDeadlineReached(true);
		log.info("success to update deadline reach");
	}

	public void deleteBoard(User user, Long boardId){
		Optional<Board> target = boardRepository.findById(boardId);

		Board board = target.orElseThrow(
			() -> new BoardPermissionException("존재하지 않은 게시글 입니다."));
		checkUserPermission(user, board.getUser().getId());

		// 게시글 삭제
		boardRepository.delete(board);

		// 일정 삭제
		Schedule targetSchedule = board.getSchedule();
		if(targetSchedule.getUser() == null)
			scheduleService.deleteSchedule(null, targetSchedule.getId());

		log.info("success to delete board");
	}

	public Boolean cancelDeadLineReached(User user, Long boardId){
		Optional<Board> target = boardRepository.findById(boardId);

		Board board = target.orElseThrow(() -> new BoardPermissionException("존재하지 않은 게시글 입니다."));
		checkUserPermission(user, board.getUser().getId());

		if(board.getSchedule().getStartDate().isBefore(LocalDate.now()))
			return false;

		board.updateDeadlineReached(false);
		return true;
	}

	public String updateBookmarkedBoard(User user, Long boardId){
		Board targetBoard = boardRepository.findById(boardId).orElseThrow(
			() -> new BoardPermissionException("유효하지 않은 게시글 입니다.")
		);

		Optional<BookmarkedBoard> bookmarkedBoard = bookmarkedBoardRepository.findByUserAndBoard(user, targetBoard);
		// 북마크가 안되어 있던 경우 -> 북마크
		if(bookmarkedBoard.isEmpty()) {
			BookmarkedBoard newBookmarkBoard = new BookmarkedBoard(user, targetBoard, LocalDateTime.now());
			bookmarkedBoardRepository.save(newBookmarkBoard);
			targetBoard.updateBookmarkedCount(1L);

			return "북마크 완료";
		}
		// 북마크 되어 있던 경우 -> 취소
		else {
			BookmarkedBoard bookmarked = bookmarkedBoard.get();
			checkUserPermission(user, bookmarked.getUser().getId());
			bookmarkedBoardRepository.delete(bookmarked);
			targetBoard.updateBookmarkedCount(-1L);

			return "북마크 취소 완료";
		}
	}

	public List<GetBookmarkedBoardResponseDTO> getBookmarkedBoards(User user){
		List<GetBookmarkedBoardResponseDTO> result = bookmarkedBoardRepository.findByUser(user.getId());
		log.info("success to get bookmarked boards");
		return result;
	}

	@Scheduled(cron = "0 0 0 * * ?")
	public void checkDeadlineReached(){
		LocalDate curDate = LocalDate.now();
		List<Board> boards = boardRepository.findByScheduleStartDateBefore(curDate);

		if(!boards.isEmpty()){
			for(Board board : boards){
				board.updateDeadlineReached(true);
			}
		}
	}

	private Schedule createCompanionSchedule (CreateBoardRequestDTO createBoardRequestDTO) {
		String scheduleTitle = getCompanionTitle(createBoardRequestDTO.city(), createBoardRequestDTO.title());
		CreateScheduleRequestDTO createScheduleRequest = new CreateScheduleRequestDTO(scheduleTitle,
			createBoardRequestDTO.country(),
			createBoardRequestDTO.city(),
			createBoardRequestDTO.placeId(),
			createBoardRequestDTO.placeName(),
			createBoardRequestDTO.placeUrl(),
			createBoardRequestDTO.startDate(),
			createBoardRequestDTO.endDate(),
			null);

		return scheduleService.createSchedule(null, createScheduleRequest);
	}

	private static String getCompanionTitle(City city, String boardTitle) {
		return "[".concat(city.toString()).concat("] ").concat(boardTitle);
	}

	private PreferAgeRange getPreferAgeRange(CreateBoardRequestDTO createBoardRequestDTO) {
		PreferAgeRange preferAgeRange = new PreferAgeRange(
			createBoardRequestDTO.twentiesFlag(),
			createBoardRequestDTO.thirtiesFlag(),
			createBoardRequestDTO.fortiesFlag(),
			createBoardRequestDTO.fiftiesFlag(),
			createBoardRequestDTO.sixtiesAboveFlag(),
			createBoardRequestDTO.unrelatedFlag());
		preferAgeRangeRepository.save(preferAgeRange);
		return preferAgeRange;
	}

	private static void checkUserPermission(User user, Long userId) {
		if (!userId.equals(user.getId()))
			throw new UserPermissionException();
	}
}
