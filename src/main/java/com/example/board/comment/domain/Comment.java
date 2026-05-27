package com.example.board.comment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "BOARD_COMMENT")
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    @Column(name = "BOARD_TYPE")
    private String boardType;

    @Column(name = "BOARD_NUM")
    private Integer boardNum;

    @Column(name = "COMMENT_CONTENT")
    private String commentContent;

    @Column(name = "CREATOR")
    private String creator;

    @CreatedDate
    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(name = "MODIFIED_TIME")
    private LocalDateTime modifiedTime;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @Column(name = "DELETED_TIME")
    private LocalDateTime deletedTime;

    public void softDelete() {
        this.isDeleted = "Y";
        this.deletedTime = LocalDateTime.now();
    }

    public void update(String content) {
        this.commentContent = content;
    }
}
