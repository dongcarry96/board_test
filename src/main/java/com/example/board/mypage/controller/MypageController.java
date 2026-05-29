package com.example.board.mypage.controller;

import com.example.board.board.domain.ComCode;
import com.example.board.board.dto.BoardDto;
import com.example.board.board.service.ComCodeService;
import com.example.board.board.service.ScheduledBoardService;
import com.example.board.comment.dto.CommentDto;
import com.example.board.mypage.service.MypageService;
import com.example.board.notification.service.NotificationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {
    private final MypageService mypageService;
    private final ComCodeService comCodeService;
    private final ScheduledBoardService scheduledBoardService;
    private final NotificationService notificationService;

    @GetMapping("/me")
    public String myPage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int boardPage,
            @RequestParam(defaultValue = "0") int commentPage,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int scheduledPage,
            @RequestParam(defaultValue = "0") int notificationPage,
            HttpSession session,
            Model model
    ) {
        String userId = userDetails.getUsername();


        // 마지막 열람한 게시글
        BoardDto lastViewedBoard = mypageService.getLastViewedBoard(userId);
        model.addAttribute("lastViewedBoard", lastViewedBoard);
        Map<String, String> boardTypeMap = new HashMap<>();
        for (ComCode code : comCodeService.getBoardTypeCodes()) {
            boardTypeMap.put(
                    code.getId().getCodeId(),
                    code.getCodeName()

            );
        }
        model.addAttribute("boardTypeMap", boardTypeMap);
        // 게시글 목록
        Page<BoardDto> myBoards = mypageService.getMyBoards(userId, boardPage, size);
        model.addAttribute("myBoards", myBoards);
        model.addAttribute("boardPage", boardPage);

        // 댓글 목록
        Page<CommentDto> myComments = mypageService.getMyComments(userId, commentPage, size);
        model.addAttribute("myComments", myComments);
        model.addAttribute("commentPage", commentPage);

        model.addAttribute("userId", userId);
        model.addAttribute("size", size);

        Pageable scheduledPageable = PageRequest.of(scheduledPage, 5);
        model.addAttribute("scheduledBoards", scheduledBoardService.getMyScheduledBoards(userId, scheduledPageable));

        Pageable notificationPageable = PageRequest.of(notificationPage, 5);
        model.addAttribute("notifications",
                notificationService.getMyNotifications(userId, notificationPageable));
        model.addAttribute("notificationPage", notificationPage);

        return "/member/mypage";
    }

    @PostMapping("/notifications/read-all")
    public String readAll(@AuthenticationPrincipal UserDetails userDetails) {
        notificationService.markAllAsRead(userDetails.getUsername());
        return "redirect:/mypage/me";
    }
}
