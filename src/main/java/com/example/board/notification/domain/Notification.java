package com.example.board.notification.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "NOTIFICATION")
@EntityListeners(AuditingEntityListener.class)
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTIFICATION_ID")
    private Long notificationId;

    @Column(name = "RECEIVER_ID", nullable = false)
    private String receiverId;

    @Column(name = "BOARD_TYPE", nullable = false)
    private String boardType;

    @Column(name = "BOARD_NUM", nullable = false)
    private Integer boardNum;

    @Column(name = "BOARD_TITLE")
    private String boardTitle;

    @Column(name = "COMMENTER_ID", nullable = false)
    private String commenterId;

    @Column(name = "COMMENT_CONTENT")
    private String commentContent;

    @Column(name = "IS_READ")
    @Builder.Default
    private String isRead = "N";

    @CreatedDate
    @Column(name = "CREATE_TIME", updatable = false)
    private LocalDateTime createTime;

    public void markAsRead() {
        this.isRead = "Y";
    }
}
