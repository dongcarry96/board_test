package com.example.board.admin.controller;


import com.example.board.admin.service.AdminService;
import com.example.board.board.domain.ComCode;
import com.example.board.board.dto.BoardDto;
import com.example.board.board.service.BoardService;
import com.example.board.board.service.ComCodeService;
import com.example.board.comment.dto.CommentDto;
import com.example.board.member.dto.ActivityLogDto;
import com.example.board.member.dto.MemberDto;
import com.example.board.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final MemberService memberService;
    private final BoardService boardService;
    private final ComCodeService comCodeService;

    // 관리자 회원 조회
    @GetMapping("/user/list")
    public String userList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MemberDto> userList = adminService.getUserList(pageable);
        model.addAttribute("userList",userList);
        return "/admin/userList";
    }

    // 관리자 댓글 조회
    @GetMapping("/comment/list")
    public String commentList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDto> commentList = adminService.getCommentList(pageable);
        model.addAttribute("commentList", commentList);
        return "/admin/commentList";
    }

    // 관리자 게시글 조회
    @GetMapping("/board/list")
    public String boardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardDto> boardList = adminService.getBoardList(pageable);
        Map<String, String> codeMap = comCodeService.getBoardTypeCodes().stream()
                .collect(Collectors.toMap(
                        c -> c.getId().getCodeId(),
                        ComCode::getCodeName
                ));
        model.addAttribute("codeMap", codeMap);

        model.addAttribute("boardList",boardList);
        return "/admin/boardList";
    }

    // 관리자 로그 조회
    @GetMapping("/log/list")
    public String logList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ActivityLogDto> logList = adminService.getActivityLogList(pageable);
        model.addAttribute("logList", logList);
        return "/admin/logList";
    }

    // 관리자 회원 삭제
    @PostMapping("user/delete/{userId}")
    public String userDelete(@PathVariable String userId) {
        adminService.userDelete(userId);
        return "redirect:/admin/user/list";
    }

    // 관리자 게시글 삭제
    @PostMapping("/board/delete/{type}/{num}")
    public String boardDelete(@PathVariable String type, @PathVariable Integer num) {
        adminService.boardDelete(type, num);
        return "redirect:/admin/board/list";
    }



    // 관리자 댓글 삭제
    @PostMapping("/comment/delete/{commentId}")
    public String commentDelete(@PathVariable Long commentId,
                                RedirectAttributes redirectAttributes
                                ) {
        try {
            adminService.commentDelete(commentId);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/comment/list";
    }
}
