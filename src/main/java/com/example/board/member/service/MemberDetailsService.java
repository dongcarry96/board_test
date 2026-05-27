package com.example.board.member.service;

import com.example.board.member.domain.Member;
import com.example.board.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserIdAndIsDeleted(userId, "N")
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return User.builder()
                .username(member.getUserId())
                .password(member.getUserPw())
                .roles(member.getUserRole().name())
                .disabled(!"Y".equals(member.getEmailVerified()))
                .build();
    }


}
