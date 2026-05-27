package com.example.board.comment.controller;

import com.example.board.comment.dto.CommentDto;
import com.example.board.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/save")
    public String save(CommentDto commentDto,
                       @AuthenticationPrincipal UserDetails userDetails,
                       RedirectAttributes redirectAttributes) {
        try {
            commentService.save(commentDto, userDetails.getUsername());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/board/read/" + commentDto.getBoardType() + "/" + commentDto.getBoardNum();
    }

    // 댓글 수정
    @PostMapping("/update/{commentId}")
    public String update(@PathVariable Long commentId,
                         @RequestParam String commentContent,
                         @RequestParam String boardType,
                         @RequestParam Integer boardNum,
                         @AuthenticationPrincipal UserDetails userDetails,
                         RedirectAttributes redirectAttributes) {
        try {
            commentService.update(commentId, commentContent, userDetails.getUsername());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/board/read/" + boardType + "/" + boardNum;
    }

    // 댓글 삭제
    @PostMapping("/delete/{commentId}")
    public String delete(@PathVariable Long commentId,
                         @RequestParam String boardType,
                         @RequestParam Integer boardNum,
                         @AuthenticationPrincipal UserDetails userDetails,
                         RedirectAttributes redirectAttributes) {
        try {
            commentService.delete(commentId, userDetails.getUsername());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/board/read/" + boardType + "/" + boardNum;
    }
}
