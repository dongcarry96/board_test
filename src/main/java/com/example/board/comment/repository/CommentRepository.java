package com.example.board.comment.repository;

import com.example.board.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByIsDeleted(String isDeleted, Pageable pageable);
    List<Comment> findByBoardTypeAndBoardNumAndIsDeletedOrderByCreateTimeAsc(
            String boardType, Integer boardNum, String isDeleted);
    Optional<Comment> findByCommentIdAndIsDeleted(Long commentId, String isDeleted);
}
