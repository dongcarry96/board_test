package com.example.board.member.service;

import com.example.board.member.domain.Member;
import com.example.board.member.dto.MemberDto;
import com.example.board.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(MemberDto memberDto) {
        memberDto.setEmailVerified("N");
        Member member = memberDto.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    public boolean isAvailableUserId(String userId) {
        return memberRepository.findByUserId(userId).isEmpty();
    }

    public boolean isAvailableUserEmail(String userEmail) {
        return memberRepository.findByUserEmail(userEmail).isEmpty();
    }

    @Transactional
    public void verifyEmail(String userId) {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        member.verifyEmail();
    }

}
