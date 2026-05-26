package com.example.board.admin.service;

import com.example.board.board.dto.BoardDto;
import com.example.board.member.domain.Member;
import com.example.board.member.dto.MemberDto;
import com.example.board.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;

    public Page<MemberDto> getUserList(Pageable pageable) {

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Member> result = memberRepository.findAll(sortedPageable);
        return result.map(MemberDto::fromEntity);
    }
}
