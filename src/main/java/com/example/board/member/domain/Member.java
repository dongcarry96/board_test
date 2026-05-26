package com.example.board.member.domain;

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
@Table(name = "USER_INFO")
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_PW")
    private String userPw;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_EMAIL", unique = true   )
    private String userEmail;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "EMAIL_VERIFIED")
    private String emailVerified;

    @Column(name = "USER_PHONE1")
    private String userPhone1;

    @Column(name = "USER_PHONE2")
    private String userPhone2;

    @Column(name = "USER_PHONE3")
    private String userPhone3;

    @Column(name = "USER_ADDR1")
    private String userAddr1;

    @Column(name = "USER_ADDR2")
    private String userAddr2;

    @Column(name = "USER_COMPANY")
    private String userCompany;

    @Column(name = "CREATOR")
    private String creator;

    @CreatedDate
    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Column(name = "MODIFIER")
    private String modifier;

    @LastModifiedDate
    @Column(name = "MODIFIED_TIME")
    private LocalDateTime modifiedTime;

    public void verifyEmail() {
        this.emailVerified = "Y";
        this.userRole = UserRole.USER;
    }

}
