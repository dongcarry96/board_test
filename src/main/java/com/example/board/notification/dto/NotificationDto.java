package com.example.board.notification.dto;

import com.example.board.notification.domain.Notification;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {

    private Long notificationId;
    private String receiverId;
    private String boardType;
    private Integer boardNum;
    private String boardTitle;
    private String commenterId;
    private String commentContent;
    private String isRead;
    private LocalDateTime createTime;

    public static NotificationDto fromEntity(Notification notification) {
        return NotificationDto.builder()
                .notificationId(notification.getNotificationId())
                .receiverId(notification.getReceiverId())
                .boardType(notification.getBoardType())
                .boardNum(notification.getBoardNum())
                .boardTitle(notification.getBoardTitle())
                .commenterId(notification.getCommenterId())
                .commentContent(notification.getCommentContent())
                .isRead(notification.getIsRead())
                .createTime(notification.getCreateTime())
                .build();
    }
}
