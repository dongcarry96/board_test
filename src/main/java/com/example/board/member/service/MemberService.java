package com.example.board.member.service;

import com.example.board.member.domain.Member;
import com.example.board.member.dto.MemberDto;
import com.example.board.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(MemberDto memberDto) {
        Member member = memberDto.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    public boolean isAvailableUserId(String userId) {
        return memberRepository.findByUserId(userId).isEmpty();
    }



}
