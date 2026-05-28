package com.example.board.mypage.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "LAST_VIEWED")
@EntityListeners(AuditingEntityListener.class)
public class LastViewed {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name ="BOARD_TYPE")
    private String boardType;

    @Column(name = "BOARD_NUM")
    private Integer boardNum;

    @LastModifiedDate
    @Column(name = "VIEWED_TIME")
    private String viewedTime;

    public void update(String boardType, Integer boardNum) {
        this.boardType = boardType;
        this.boardNum  = boardNum;
    }
}
