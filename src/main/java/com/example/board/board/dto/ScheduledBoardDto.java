package com.example.board.board.dto;

import com.example.board.board.domain.ScheduledBoard;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledBoardDto {

    private Long scheduleId;
    private String boardType;
    private String boardTitle;
    private String boardComment;
    private String fileRoot;
    private String creator;
    private LocalDateTime scheduledTime;
    private LocalDateTime createTime;

    public String getScheduledTimeFormatted() {
        if (scheduledTime == null) return "";
        return scheduledTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static ScheduledBoardDto fromEntity(ScheduledBoard entity) {
        return ScheduledBoardDto.builder()
                .scheduleId(entity.getScheduleId())
                .boardType(entity.getBoardType())
                .boardTitle(entity.getBoardTitle())
                .boardComment(entity.getBoardComment())
                .fileRoot(entity.getFileRoot())
                .creator(entity.getCreator())
                .scheduledTime(entity.getScheduledTime())
                .createTime(entity.getCreateTime())
                .build();
    }

    public ScheduledBoard toEntity() {
        return ScheduledBoard.builder()
                .boardType(boardType)
                .boardTitle(boardTitle)
                .boardComment(boardComment)
                .fileRoot(fileRoot)
                .creator(creator)
                .scheduledTime(scheduledTime)
                .build();
    }
}
