package com.example.board.board.controller;

import com.example.board.board.domain.ComCode;
import com.example.board.board.dto.BoardDto;
import com.example.board.board.service.BoardService;
import com.example.board.board.service.ComCodeService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final ComCodeService comCodeService;

    @GetMapping("/list")
    public String boardList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> type,
            @RequestParam(defaultValue = "date") String sort,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<BoardDto> boardList = boardService.getBoardList(type, pageable, sort);
        model.addAttribute("boardList", boardList);
        model.addAttribute("selectedTypes", type != null ? type : List.of());
        model.addAttribute("selectedSort", sort);
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
            Model model
    ) {
        BoardDto board =
                boardService.getBoard(type, num);
        BoardDto prevBoard = boardService.PrevBoard(type, num);
        BoardDto nextBoard = boardService.NextBoard(type, num);
        if ("a01".equals(type)) {
            type = "일반";
        } else if ("a02".equals(type)) {
            type = "Q&A";
        } else if ("a03".equals(type)) {
            type = "익명";
        } else {
            type = "자유";
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
                                @AuthenticationPrincipal UserDetails userDetails
                                ) {
        if (userDetails != null) {
            boardDto.setCreator(userDetails.getUsername());
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
                              @AuthenticationPrincipal UserDetails userDetails
    ) {

        boardDto.setBoardType(type);
        boardDto.setBoardNum(num);
        if (userDetails != null) {
            boardDto.setModifier(userDetails.getUsername());
        }
        boardService.update(boardDto);

        return "redirect:/board/read/" + type + "/" + num;
    }

    @PostMapping("/delete/{type}/{num}")
    public String boardDelete(@PathVariable String type,
                              @PathVariable Integer num) {
        boardService.delete(type, num);
        return "redirect:/board/list";
    }
}
