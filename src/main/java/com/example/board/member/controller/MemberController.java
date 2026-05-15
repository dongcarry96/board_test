package com.example.board.member.controller;


import com.example.board.member.dto.MemberDto;
import com.example.board.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String memberLoginVeiw() {
        return "/member/login";
    }

    @GetMapping("/join")
    public String memberJoinVeiw() {
        return "/member/join";
    }

    @PostMapping("/join")
    public String memberJoin(@ModelAttribute MemberDto userJoinDto) {
        memberService.save(userJoinDto);
        return "/member/login";
    }

}
