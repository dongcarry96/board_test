package com.example.board.board.domain;


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
@Table(name = "BOARD")
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @EmbeddedId
    private BoardId id;

    @Column(name = "BOARD_TITLE")
    private String boardTitle;

    @Column(name = "BOARD_COMMENT")
    private String boardComment;

    @Column(name = "BOARD_HIT")
    private Integer boardHit;

    @Column(name = "CREATOR")
    private String creator;

    @CreatedDate
    @Column(name = "CREATE_TIME", updatable = false)
    private LocalDateTime createTime;

    @Column(name = "MODIFIER")
    private String modifier;

    @LastModifiedDate
    @Column(name = "MODIFIED_TIME")
    private LocalDateTime modifiedTime;

    @Column(name = "FILE_ROOT")
    private String fileRoot;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @Column(name = "DELETED_TIME")
    private LocalDateTime deletedTime;

    public void increaseHit() {
        this.boardHit++;
    }

    public void softDelete() {
        this.isDeleted = "Y";
        this.deletedTime = LocalDateTime.now();
    }

    public void updateFile(String fileRoot) {
        this.fileRoot = fileRoot;
    }
}
