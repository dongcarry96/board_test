package com.example.board.board.service;

import com.example.board.board.domain.Board;
import com.example.board.board.domain.BoardId;
import com.example.board.board.dto.BoardDto;
import com.example.board.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<BoardDto> getBoardList(String type, Pageable pageable) {
        Page<Board> result;
        if(type == null || type.isBlank()) {
            result = boardRepository.findAll(pageable);
        } else {
            result = boardRepository.findByIdBoardType(type, pageable);
        }

        return result.map(BoardDto::fromEntity);
    }

    // 게시글 작성
    @Transactional
    public void register(BoardDto boardDto) {
        Integer maxNum = boardRepository.findMaxBoardNum(boardDto.getBoardType());
        int newNum = maxNum + 1;
        Board board = Board.builder()
                            .id(new BoardId(
                            boardDto.getBoardType(),
                            newNum))
                            .boardTitle(boardDto.getBoardTitle())
                            .boardComment(boardDto.getBoardComment())
                            .boardHit(0)
                            .creator(boardDto.getCreator())
                            .createTime(String.valueOf(System.currentTimeMillis()))
                            .build();
        boardRepository.save(board);
    }

    @Transactional
    public BoardDto getBoard(String type, Integer num) {
        Board board = boardRepository.findById(
                new BoardId(type, num)
        ).orElseThrow();
        board.increaseHit();
        return BoardDto.fromEntity(board);
    }

    @Transactional
    public void update(BoardDto boardDto) {
        Board board = boardRepository.findById(
                new BoardId(boardDto.getBoardType(), boardDto.getBoardNum())
        ).orElseThrow();

        board = Board.builder()
                .id(board.getId())
                .boardTitle(boardDto.getBoardTitle())
                .boardComment(boardDto.getBoardComment())
                .boardHit(board.getBoardHit())
                .creator(board.getCreator())
                .createTime(board.getCreateTime())
                .modifier(boardDto.getModifier())
                .modifiedTime(board.getModifiedTime())
                .build();

        boardRepository.save(board);
    }

    @Transactional
    public void delete(String type, Integer num) {
        BoardId boardId = new BoardId(type, num);
        boardRepository.deleteById(boardId);
    }
}
