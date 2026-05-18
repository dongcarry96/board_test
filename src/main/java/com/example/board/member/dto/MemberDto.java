package com.example.board.member.dto;

import com.example.board.member.domain.Member;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberDto implements Serializable {

    private String userId;
    private String userPw;
    private String userName;
    private String userPhone1;
    private String userPhone2;
    private String userPhone3;
    private String userAddr1;
    private String userAddr2;
    private String userCompany;

    public Member toEntity() {
        return Member.builder()
                .userId(userId)
                .userPw(userPw)
                .userName(userName)
                .userPhone1(userPhone1)
                .userPhone2(userPhone2)
                .userPhone3(userPhone3)
                .userAddr1(userAddr1)
                .userAddr2(userAddr2)
                .userCompany(userCompany)
                .build();
    }
}
