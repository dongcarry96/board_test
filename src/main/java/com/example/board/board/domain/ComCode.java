package com.example.board.board.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "COM_CODE")
public class ComCode {

    @EmbeddedId
    private ComCodeId id;

    @Column(name = "CODE_NAME")
    private String codeName;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Column(name = "MODIFIER")
    private String modifier;

    @Column(name = "MODIFIED_TIME")
    private LocalDateTime modifiedTime;
}
