package com.example.board.board.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @Column(name = "CREATE_TIME")
    private String createTime;

    @Column(name = "MODIFIER")
    private String modifier;

    @LastModifiedDate
    @Column(name = "MODIFIED_TIME")
    private String modifiedTime;

    @Column(name = "FILE_ROOT")
    private String fileRoot;

    public void increaseHit() {
        this.boardHit++;
    }
}
