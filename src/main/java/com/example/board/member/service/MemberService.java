package com.example.board.member.service;

import com.example.board.board.domain.BoardId;
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
        return memberRepository.findByUserEmailAndIsDeleted(userId, "N").isEmpty();
    }

    public boolean isAvailableUserEmail(String userEmail) {
        return memberRepository.findByUserEmailAndIsDeleted(userEmail, "N").isEmpty();
    }

    @Transactional
    public void verifyEmail(String userId) {
        Member member = memberRepository.findByUserIdAndIsDeleted(userId, "N")
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        member.verifyEmail();
    }

    @Transactional
    public void userDelete(String userId) {
        Member member = memberRepository.findByUserIdAndIsDeleted(userId, "N")
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));
        member.softDelete();
    }

}
