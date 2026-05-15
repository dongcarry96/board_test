package com.example.board.member.domain;

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
@Table(name = "USER_INFO")
public class Member {

    @Id
    @Column(name = "USER_ID", length = 15)
    private String userId;

    @Column(name = "USER_PW", length = 16)
    private String userPw;

    @Column(name = "USER_NAME", length = 15)
    private String userName;

    @Column(name = "USER_PHONE1", length = 3)
    private String userPhone1;

    @Column(name = "USER_PHONE2", length = 4)
    private String userPhone2;

    @Column(name = "USER_PHONE3", length = 4)
    private String userPhone3;

    @Column(name = "USER_ADDR1", length = 8)
    private String userAddr1;

    @Column(name = "USER_ADDR2", length = 150)
    private String userAddr2;

    @Column(name = "USER_COMPANY", length = 60)
    private String userCompany;

    @Column(name = "CREATOR", length = 15)
    private String creator;

    @Column(name = "CREATE_TIME", length = 14)
    private String createTime;

    @Column(name = "MODIFIER", length = 15)
    private String modifier;

    @Column(name = "MODIFIED_TIME", length = 14)
    private String modifiedTime;

}
