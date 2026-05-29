package com.example.board.board.controller;

import com.example.board.board.dto.ScheduledBoardDto;
import com.example.board.board.service.FileService;
import com.example.board.board.service.ScheduledBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/schedule")
public class ScheduledBoardController {

    private final ScheduledBoardService scheduledBoardService;
    private final FileService fileService;

    //예약 게시글 등록
    @PostMapping("/register")
    public String register(@RequestParam String boardType,
                           @RequestParam String boardTitle,
                            @RequestParam String boardComment,
                            @RequestParam(value = "file", required = false) MultipartFile file,
                            @RequestParam("scheduledTime")
                            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime scheduledTime,
                            @AuthenticationPrincipal UserDetails userDetails,
                            RedirectAttributes redirectAttributes
    ) {
        try {
            String fileRoot = fileService.saveFile(file);

            ScheduledBoardDto dto = ScheduledBoardDto.builder()
                    .boardType(boardType)
                    .boardTitle(boardTitle)
                    .boardComment(boardComment)
                    .fileRoot(fileRoot)
                    .creator(userDetails.getUsername())
                    .scheduledTime(scheduledTime)
                    .build();

            scheduledBoardService.register(dto);
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 예약되었습니다.");

        } catch (IOException e) {
            log.error("파일 저장 실패", e);
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/board/register";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/register";
        }

        return "redirect:/board/list";
    }

    //예약 게시글 취소
    @PostMapping("/cancel/{scheduleId}")
    public String cancel(
            @PathVariable Long scheduleId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        try {
            scheduledBoardService.cancel(scheduleId, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("successMessage", "예약이 취소되었습니다.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/mypage/me";
    }
}
