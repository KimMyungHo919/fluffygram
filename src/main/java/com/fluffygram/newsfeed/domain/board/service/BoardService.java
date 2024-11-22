package com.fluffygram.newsfeed.domain.board.service;

import com.fluffygram.newsfeed.domain.board.dto.BoardResponseDto;
import com.fluffygram.newsfeed.domain.board.entity.Board;
import com.fluffygram.newsfeed.domain.board.repository.BoardRepository;
import com.fluffygram.newsfeed.domain.user.entity.User;
import com.fluffygram.newsfeed.domain.user.repository.UserRepository;
import com.fluffygram.newsfeed.global.exception.BusinessException;
import com.fluffygram.newsfeed.global.exception.ExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    //게시물 생성(저장)
    public BoardResponseDto save(Long userId, String title, String contents, Long loginUserId) {
        // 로그인한 사용자와 아이디(id) 일치 여부 확인
        if(!loginUserId.equals(userId)) {
            throw new BusinessException(ExceptionType.USER_NOT_MATCH);
        }

        //사용자 id로 사용자 조회
        User findUserById = userRepository.findByIdOrElseThrow(userId);

        Board board = new Board(title, contents, findUserById);

        //게시물 저장
        Board saveBoard = boardRepository.save(board);

        return BoardResponseDto.toDto(saveBoard);
    }

    //게시물 전체 List 조회
    public List<BoardResponseDto> findAllBoard(Pageable pageable) {
        return boardRepository.findAllByOrderByCreatedAtDesc(pageable)
                .stream()
                .map(BoardResponseDto::toDtoForAll)
                .toList();
    }

    //게시물 id로 특정 게시물 단건 조회
    public BoardResponseDto findBoardById(Long id) {
        //게시물 조회
        Board findBoard = boardRepository.findBoardByIdOrElseThrow(id);

        return BoardResponseDto.toDto(findBoard);
    }

    //게시물 id로 특정 게시물 수정
    public BoardResponseDto updateBoard(Long id, String title, String contents, Long loginUserId) {
        //게시물 조회
        Board findBoard = boardRepository.findBoardByIdOrElseThrow(id);

        // 로그인한 사용자와 아이디(id) 일치 여부 확인
        if(!loginUserId.equals(findBoard.getUser().getId())) {
            throw new BusinessException(ExceptionType.USER_NOT_MATCH);
        }

        // 제목 및 내용 수정하기
        findBoard = findBoard.updateBoard(title, contents);

        //수정된 게시물 저장하기
        Board saveBoard = boardRepository.save(findBoard);

        return BoardResponseDto.toDto(saveBoard);
    }
    
    //게시물 id로 특정 게시물 삭제
    public void deleteBoard(Long id, Long loginUserId) {
        //게시물 조회
        Board findBoard = boardRepository.findBoardByIdOrElseThrow(id);

        // 로그인한 사용자와 아이디(id) 일치 여부 확인
        if(!loginUserId.equals(findBoard.getUser().getId())) {
            throw new BusinessException(ExceptionType.USER_NOT_MATCH);
        }

        boardRepository.delete(findBoard);
    }
    
}
