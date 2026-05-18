package com.example.board.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BoardId implements Serializable {

    @Column(name = "BOARD_TYPE")
    private String boardType;

    @Column(name = "BOARD_NUM")
    private Integer boardNum;
}
