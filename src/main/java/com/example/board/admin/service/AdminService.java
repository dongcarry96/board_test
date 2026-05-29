package com.example.board.admin.service;

import com.example.board.board.domain.Board;
import com.example.board.board.domain.BoardId;
import com.example.board.board.dto.BoardDto;
import com.example.board.board.repository.BoardRepository;
import com.example.board.comment.domain.Comment;
import com.example.board.comment.dto.CommentDto;
import com.example.board.comment.repository.CommentRepository;
import com.example.board.member.domain.ActivityLog;
import com.example.board.member.domain.Member;
import com.example.board.member.dto.ActivityLogDto;
import com.example.board.member.dto.MemberDto;
import com.example.board.member.repository.ActivityLogRepository;
import com.example.board.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final ActivityLogRepository activityLogRepository;

    // 관리자 회원 조회
    public Page<MemberDto> getUserList(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Member> result = memberRepository.findAllByIsDeleted("N", sortedPageable);
        return result.map(MemberDto::fromEntity);
    }

    // 관리자 댓글 조회
    public Page<CommentDto> getCommentList(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        Page<Comment> result =  commentRepository.findAllByIsDeleted("N", sortedPageable);
        return result.map(CommentDto::fromEntity);
    }

    // 관리자 게시글 조회
    public Page<BoardDto> getBoardList(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Board> result = boardRepository.findAllByIsDeleted("N", sortedPageable);
        return result.map(BoardDto::fromEntity);
    }
    // 로그 기록 조회
    public Page<ActivityLogDto> getActivityLogList(Pageable pageable) {
        Pageable sortePageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
        Page<ActivityLog> result = activityLogRepository.findAll(sortePageable);
        return result.map(ActivityLogDto::fromEntity);
    }

    @Transactional
    public void userDelete(String userId) {
        Member member = memberRepository.findByUserIdAndIsDeleted(userId, "N")
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        member.softDelete();
    }

    // 게시글 삭제
    @Transactional
    public void boardDelete(String type, Integer num) {
        Board board = boardRepository.findById(new BoardId(type, num))
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.softDelete();
    }

    // 댓글 삭제
    @Transactional
    public void commentDelete(Long commentId) {
        Comment comment = commentRepository.findByCommentIdAndIsDeleted(commentId, "N")
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        comment.softDelete();
    }

}
