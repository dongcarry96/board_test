package com.example.board.member.dto;

import com.example.board.member.domain.Member;
import com.example.board.member.domain.UserRole;
import jakarta.persistence.Column;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberDto implements Serializable {

    private String userId;
    private String userPw;
    private String userName;
    private String userEmail;
    private String emailVerified;
    private UserRole userRole;
    private String userPhone1;
    private String userPhone2;
    private String userPhone3;
    private String userAddr1;
    private String userAddr2;
    private String userCompany;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .userEmail(member.getUserEmail())
                .emailVerified(member.getEmailVerified())
                .userRole(member.getUserRole())
                .userPhone1(member.getUserPhone1())
                .userPhone2(member.getUserPhone2())
                .userPhone3(member.getUserPhone3())
                .userAddr1(member.getUserAddr1())
                .userAddr2(member.getUserAddr2())
                .userCompany(member.getUserCompany())
                .build();
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .userId(userId)
                .userPw(passwordEncoder.encode(userPw))
                .userName(userName)
                .userEmail(userEmail)
                .emailVerified(emailVerified)
                .userRole(UserRole.valueOf("USER"))
                .isDeleted("N")
                .creator(userId)
                .userPhone1(userPhone1)
                .userPhone2(userPhone2)
                .userPhone3(userPhone3)
                .userAddr1(userAddr1)
                .userAddr2(userAddr2)
                .userCompany(userCompany)
                .build();
    }
}
