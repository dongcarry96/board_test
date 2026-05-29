package com.example.board.board.dto;

import com.example.board.board.domain.Board;
import com.example.board.board.domain.BoardId;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private String boardType;
    private Integer boardNum;
    private String boardTitle;
    private String boardComment;
    private Integer boardHit;
    private String creator;
    private LocalDateTime createTime;
    private String modifier;
    private LocalDateTime modifiedTime;
    private String fileRoot;

    public static BoardDto fromEntity(Board board) {
        return BoardDto.builder()
                .boardType(board.getId().getBoardType())
                .boardNum(board.getId().getBoardNum())
                .boardTitle(board.getBoardTitle())
                .boardComment(board.getBoardComment())
                .boardHit(board.getBoardHit())
                .creator(board.getCreator())
                .createTime(board.getCreateTime())
                .modifier(board.getModifier())
                .modifiedTime(board.getModifiedTime())
                .fileRoot(board.getFileRoot())
                .build();
    }

    public Board toEntity() {
        return Board.builder()
                .id(new BoardId(boardType, boardNum))
                .boardTitle(boardTitle)
                .boardComment(boardComment)
                .boardHit(boardHit)
                .creator(creator)
                .createTime(createTime)
                .build();
    }

}
