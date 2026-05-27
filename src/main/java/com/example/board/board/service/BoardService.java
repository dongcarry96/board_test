package com.example.board.board.service;

import com.example.board.board.domain.Board;
import com.example.board.board.domain.BoardId;
import com.example.board.board.dto.BoardDto;
import com.example.board.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<BoardDto> getBoardList(List<String> type,
                                       Pageable pageable,
                                       String sort,
                                       String searchType,
                                       String keyword
                                       ) {
        Sort sorting = "hit".equals(sort)
                ? Sort.by(Sort.Direction.DESC, "boardHit")
                : Sort.by(Sort.Direction.DESC, "createTime");

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sorting
        );

        boolean hasKeyword = keyword != null && !keyword.isBlank();

        if (hasKeyword) {
            return boardRepository
                    .searchBoards(type, searchType, keyword, sortedPageable)
                    .map(BoardDto::fromEntity);
        }

        Page<Board> result;
        if (type == null || type.isEmpty()) {
            result = boardRepository.findAllByIsDeleted("N",sortedPageable);
        } else if (type.size() == 1) {
            result = boardRepository.findByIdBoardTypeAndIsDeleted(type.get(0),"N", sortedPageable);
        } else {
            result = boardRepository.findByIdBoardTypeInAndIsDeleted(type,"N", sortedPageable);
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
                            .isDeleted("N")
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

    public BoardDto PrevBoard(String type, Integer num) {
        return boardRepository.findPrevBoard(type, num, "N")
                .map(BoardDto::fromEntity)
                .orElse(null);
    }

    public BoardDto NextBoard(String type, Integer num) {
        return boardRepository.findNextBoard(type, num)
                .map(BoardDto::fromEntity)
                .orElse(null);
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
    public void boardDelete(String type, Integer num, String userId) {
        Board board = boardRepository.findById(new BoardId(type, num))
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        if (!board.getCreator().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        board.softDelete();
    }

    @Transactional
    public void checkDelete(List<String> boardIds, String userId) {
        for(String boardId : boardIds) {
            String[] parts = boardId.split(":");
            if(parts.length != 2) {
                throw new IllegalArgumentException("잘못된 게시글 아이디 형식입니다.");
            }
            String type = parts[0];
            Integer num = Integer.parseInt(parts[1]);

            Board board = boardRepository.findById(new BoardId(type, num))
                    .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
            if(!board.getCreator().equals(userId)) {
                throw new IllegalArgumentException("삭제 권한이 없습니다.");
            }
            board.softDelete();
        }
    }
}
