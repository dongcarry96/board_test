package com.example.board.board.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "BOARD")
public class Board {

    @EmbeddedId
    private BoardId id;

    @Column(name = "BOARD_TITLE")
    private String boardTitle;

    @Column(name = "BOARD_COMMENT")
    private String boardComment;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "CREATE_TIME")
    private String createTime;

    @Column(name = "MODIFIER")
    private String modifier;
    
    @Column(name = "MODIFIED_TIME")
    private String modifiedTime;

    @Column(name = "FILE_ROOT")
    private String fileRoot;

}
