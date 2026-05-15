package com.example.board.board.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ComCodeId {

    @Column(name = "CODE_TYPE")
    private String codeType;

    @Column(name = "CODE_ID")
    private String codeId;
}
