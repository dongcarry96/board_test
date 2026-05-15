package com.example.board.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/list")
    public String boardList() {
        return "/board/list";
    }

    @GetMapping("/read")
    public String boardRead() {
        return "/board/read";
    }
    @GetMapping("/register")
    public String boardRegister() {
        return "/board/register";
    }
    @GetMapping("/update")
    public String boardUpdate() {
        return "/board/update";
    }
}
