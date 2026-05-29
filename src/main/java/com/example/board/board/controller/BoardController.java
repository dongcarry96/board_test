package com.example.board.board.controller;

import com.example.board.board.domain.ComCode;
import com.example.board.board.dto.BoardDto;
import com.example.board.board.service.BoardService;
import com.example.board.board.service.ComCodeService;
import com.example.board.board.service.FileService;
import com.example.board.comment.dto.CommentDto;
import com.example.board.comment.service.CommentService;
import com.example.board.member.service.MemberService;
import com.example.board.mypage.service.LastViewedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;
    private final ComCodeService comCodeService;
    private final LastViewedService lastViewedService;
    private final FileService fileService;

    @GetMapping("/list")
    public String boardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> type,
            @RequestParam(defaultValue = "date") String sort,
            @RequestParam(defaultValue = "title") String searchType,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardDto> boardList = boardService.getBoardList(type, pageable, sort, searchType, keyword);
        model.addAttribute("boardList", boardList);
        model.addAttribute("selectedTypes", type != null ? type : List.of());
        model.addAttribute("selectedSort", sort);
        model.addAttribute("selectedSearchType", searchType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("menuCode", comCodeService.getBoardTypeCodes());
        Map<String, String> boardTypeMap = new HashMap<>();
        for (ComCode code : comCodeService.getBoardTypeCodes()) {
            boardTypeMap.put(
                    code.getId().getCodeId(),
                    code.getCodeName()

            );
        }
        model.addAttribute("boardTypeMap", boardTypeMap);
        return "/board/list";
    }

    @GetMapping("/read/{type}/{num}")
    public String boardRead(
            @PathVariable String type,
            @PathVariable Integer num,
            @RequestParam(required = false) Long editCommentId,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        BoardDto board = boardService.getBoard(type, num);
        lastViewedService.update(userDetails.getUsername(), type, num);
        BoardDto prevBoard = boardService.PrevBoard(type, num);
        BoardDto nextBoard = boardService.NextBoard(type, num);


        List<CommentDto> comments = commentService.getComments(type, num);
        model.addAttribute("comments", comments);
        model.addAttribute("editCommentId", editCommentId);


        if ("a01".equals(type)) {
            type = "일반";
        } else if ("a02".equals(type)) {
            type = "Q&A";
        } else if ("a03".equals(type)) {
            type = "익명";
        } else {
            type = "자유";
        }

        if (board.getFileRoot() != null && !board.getFileRoot().isBlank()) {
            model.addAttribute("originalFileName", fileService.extractOriginalFileName(board.getFileRoot()));
        }

        model.addAttribute("board", board);
        model.addAttribute("type",type);
        model.addAttribute("num",num);
        model.addAttribute("prevBoard", prevBoard);
        model.addAttribute("nextBoard", nextBoard);
        return "/board/read";
    }

    @GetMapping("/register")
    public String boardRegisterVeiw(Model model,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        BoardDto boardDto = new BoardDto();
        if (userDetails != null) {
            boardDto.setCreator(userDetails.getUsername());
        }
        model.addAttribute("boardDto",boardDto);
        model.addAttribute("comCodes", comCodeService.getBoardTypeCodes());
        return "/board/register";
    }

    @PostMapping("/register")
    public String boardRegister(BoardDto boardDto,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                @AuthenticationPrincipal UserDetails userDetails,
                                RedirectAttributes redirectAttributes
                                ) {
        if (userDetails != null) {
            boardDto.setCreator(userDetails.getUsername());
        }

        try {
            String fileRoot = fileService.saveFile(file);
            boardDto.setFileRoot(fileRoot);
        } catch (IOException e) {
            log.error("파일 저장 실패", e);
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/board/register";
        }

        boardService.register(boardDto);
        return "redirect:/board/list";
    }

    @GetMapping("/update/{type}/{num}")
    public String boardUpdateView(@PathVariable String type,
                                  @PathVariable Integer num,
                                  Model model) {
        BoardDto board = boardService.getBoard(type, num);
        model.addAttribute("boardDto", board);
        model.addAttribute("comCodes", comCodeService.getBoardTypeCodes());
        return "/board/update";
    }

    @PostMapping("/update/{type}/{num}")
    public String boardUpdate(@PathVariable String type,
                              @PathVariable Integer num,
                              BoardDto boardDto,
                              @RequestParam(value = "file", required = false) MultipartFile file,
                              @RequestParam(value = "deleteFile", required = false) String deleteFile,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes
    ) {

        boardDto.setBoardType(type);
        boardDto.setBoardNum(num);
        if (userDetails != null) {
            boardDto.setModifier(userDetails.getUsername());
        }

        BoardDto existing = boardService.getBoard(type, num);
        String currentFileRoot = existing.getFileRoot();

        try {
            if (file != null && !file.isEmpty()) {
                fileService.deleteFile(currentFileRoot);
                boardDto.setFileRoot(fileService.saveFile(file));
            } else if ("true".equals(deleteFile)) {
                fileService.deleteFile(currentFileRoot);
                boardDto.setFileRoot(null);
            } else {
                boardDto.setFileRoot(currentFileRoot);
            }
        } catch (IOException e) {
            log.error("파일 저장 실패", e);
            redirectAttributes.addFlashAttribute("errorMessage", "파일 업로드 중 오류가 발생했습니다.");
            return "redirect:/board/update/" + type + "/" + num;
        }

        boardService.update(boardDto);

        return "redirect:/board/read/" + type + "/" + num;
    }

    @PostMapping("/delete/{type}/{num}")
    public String boardDelete(@PathVariable String type,
                              @PathVariable Integer num,
                              @AuthenticationPrincipal UserDetails userDetails,
                              RedirectAttributes redirectAttributes) {
        try {
            boardService.boardDelete(type, num, userDetails.getUsername());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/list";
        }
        return "redirect:/board/list";
    }

    @PostMapping("/delete/check")
    public String boardCheckDelete(@RequestParam(required = false) List<String> boardIds,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    RedirectAttributes redirectAttributes) {
        if (boardIds == null || boardIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제할 게시글을 선택해주세요.");
            return "redirect:/board/list";
        }
        try {
            boardService.checkDelete(boardIds, userDetails.getUsername());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/board/list";
    }
}
