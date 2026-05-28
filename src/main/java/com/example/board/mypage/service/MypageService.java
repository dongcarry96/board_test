package com.example.board.mypage.service;

import com.example.board.board.domain.BoardId;
import com.example.board.board.dto.BoardDto;
import com.example.board.board.repository.BoardRepository;
import com.example.board.comment.dto.CommentDto;
import com.example.board.comment.repository.CommentRepository;
import com.example.board.member.repository.MemberRepository;
import com.example.board.mypage.repository.LastViewedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MypageService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LastViewedRepository lastViewedRepository;

    public BoardDto getLastViewedBoard(String userId) {
        return lastViewedRepository.findById(userId)
                .flatMap(lv -> boardRepository.findById(
                        new BoardId(lv.getBoardType(), lv.getBoardNum())))
                .filter(board -> "N".equals(board.getIsDeleted()))
                .map(BoardDto::fromEntity)
                .orElse(null);
    }

    public Page<BoardDto> getMyBoards(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createTime"));
        return boardRepository
                .findByCreatorAndIsDeleted(userId, "N", pageable)
                .map(BoardDto::fromEntity);
    }

    public Page<CommentDto> getMyComments(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "createTime"));
        return commentRepository
                .findByCreatorAndIsDeleted(userId, "N", pageable)
                .map(CommentDto::fromEntity);
    }
}
