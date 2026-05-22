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

        Member verified = Member.builder()
                .userId(member.getUserId())
                .userPw(member.getUserPw())
                .userName(member.getUserName())
                .userEmail(member.getUserEmail())
                .emailVerified("Y")
                .userPhone1(member.getUserPhone1())
                .userPhone2(member.getUserPhone2())
                .userPhone3(member.getUserPhone3())
                .userAddr1(member.getUserAddr1())
                .userAddr2(member.getUserAddr2())
                .userCompany(member.getUserCompany())
                .creator(member.getCreator())
                .modifier(member.getModifier())
                .build();

        memberRepository.save(verified);
    }

}
