package com.example.board.comment.service;

import com.example.board.comment.domain.Comment;
import com.example.board.comment.dto.CommentDto;
import com.example.board.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    // 댓글 목록 조회
    public List<CommentDto> getComments(String boardType, Integer boardNum) {
        return commentRepository
                .findByBoardTypeAndBoardNumAndIsDeletedOrderByCreateTimeAsc(boardType, boardNum, "N")
                .stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    // 댓글 작성
    @Transactional
    public void save(CommentDto commentDto, String userId) {
        Comment comment = Comment.builder()
                .boardType(commentDto.getBoardType())
                .boardNum(commentDto.getBoardNum())
                .commentContent(commentDto.getCommentContent())
                .creator(userId)
                .isDeleted("N")
                .build();
        commentRepository.save(comment);
    }

    // 댓글 수정
    @Transactional
    public void update(Long commentId, String content, String userId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeleted(commentId, "N")
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.getCreator().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        comment.update(content);
    }

    // 댓글 삭제
    @Transactional
    public void delete(Long commentId, String userId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeleted(commentId, "N")
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        if (!comment.getCreator().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        comment.softDelete();
    }
}
