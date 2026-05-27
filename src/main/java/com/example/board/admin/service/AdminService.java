package com.example.board.admin.service;

import com.example.board.board.domain.Board;
import com.example.board.board.domain.BoardId;
import com.example.board.board.dto.BoardDto;
import com.example.board.board.repository.BoardRepository;
import com.example.board.member.domain.Member;
import com.example.board.member.dto.MemberDto;
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

    public Page<MemberDto> getUserList(Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Member> result = memberRepository.findAllByIsDeleted("N", sortedPageable);
        return result.map(MemberDto::fromEntity);
    }

    public Page<BoardDto> getBoardList(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Board> result = boardRepository.findAllByIsDeleted("N", sortedPageable);
        return result.map(BoardDto::fromEntity);
    }

    @Transactional
    public void boardDelete(String type, Integer num) {
        Board board = boardRepository.findById(new BoardId(type, num))
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        board.softDelete();
    }
}
