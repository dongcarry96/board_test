package com.example.board.comment.dto;

import com.example.board.comment.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long commentId;
    private String boardType;
    private Integer boardNum;
    private String commentContent;
    private String creator;
    private LocalDateTime createTime;
    private LocalDateTime modifiedTime;

    public static CommentDto fromEntity(Comment comment) {
        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .boardType(comment.getBoardType())
                .boardNum(comment.getBoardNum())
                .commentContent(comment.getCommentContent())
                .creator(comment.getCreator())
                .createTime(comment.getCreateTime())
                .modifiedTime(comment.getModifiedTime())
                .build();
    }
}
