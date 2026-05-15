package com.example.board.member.dto;

import com.example.board.member.domain.Member;
import jakarta.persistence.Column;
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
    private String creator;
    private String createTime;
    private String modifier;
    private String modifiedTime;

    public Member memberJoin() {
        Member member = Member.builder()
                .userId(userId)
                .userPw(userPw)
                .userName(userName)
                .userPhone1(userPhone1)
                .userPhone2(userPhone2)
                .userPhone3(userPhone3)
                .userAddr1(userAddr1)
                .userAddr2(userAddr2)
                .userCompany(userCompany)
                .creator(creator)
                .createTime(createTime)
                .modifier(modifier)
                .modifiedTime(modifiedTime)
                .build();
        return member;
    }
}
