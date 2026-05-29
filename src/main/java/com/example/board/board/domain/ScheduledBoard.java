package com.example.board.board.domain;

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
@Table(name = "SCHEDULED_BOARD")
@EntityListeners(AuditingEntityListener.class)
public class ScheduledBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCHEDULE_ID")
    private Long scheduleId;  // 임시 번호 (자동증가 PK)

    @Column(name = "BOARD_TYPE", nullable = false)
    private String boardType;

    @Column(name = "BOARD_TITLE", nullable = false)
    private String boardTitle;

    @Column(name = "BOARD_COMMENT")
    private String boardComment;

    @Column(name = "FILE_ROOT")
    private String fileRoot;

    @Column(name = "CREATOR", nullable = false)
    private String creator;

    @Column(name = "SCHEDULED_TIME", nullable = false)
    private LocalDateTime scheduledTime;

    @CreatedDate
    @Column(name = "CREATE_TIME", updatable = false)
    private LocalDateTime createTime;
}
