package com.example.board.member.controller;


import com.example.board.board.service.ComCodeService;
import com.example.board.member.dto.MemberDto;
import com.example.board.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final ComCodeService comCodeService;

    @GetMapping("/login")
    public String memberLoginVeiw() {
        return "/member/login";
    }

    @GetMapping("/join")
    public String memberJoinVeiw(Model model) {
        model.addAttribute("userJoinDto", new MemberDto());

        model.addAttribute("phoneNumbers", comCodeService.getPhoneCodes());
        return "/member/join";
    }

    @PostMapping("/join")
    public String memberJoin(@ModelAttribute MemberDto memberDto) {
        memberService.save(memberDto);
        return "redirect:/member/login";
    }

    // 아이디 중복체크
    @GetMapping("/check-id")
    @ResponseBody
    public Map<String, Boolean> checkId(@RequestParam String userId) {

        boolean result = memberService.isAvailableUserId(userId);

        return Map.of("available", result);
    }

}
