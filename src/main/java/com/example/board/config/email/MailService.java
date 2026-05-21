package com.example.board.config.email;

import com.example.board.config.jwt.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final JwtProvider jwtProvider;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private MimeMessage createMail(String mail, String token) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(senderEmail); // 발신자 이메일 주소 설정
        message.setRecipients(MimeMessage.RecipientType.TO, mail); // 수신자 이메일 주소 설정
        message.setSubject("이메일 인증 링크"); // 이메일 제목 설정

        String body = "";
        body += "<h3>이메일 인증 링크입니다.</h3>";
        body += "<a href=\"http://localhost:4040/api/v1/mail/verify?token=" + token + "\">여기를 클릭하여 인증하세요</a>";
        body += "<p>감사합니다.</p>";

        message.setText(body, "UTF-8", "html");

        return message;
    }

    /**
     * 인증 메일을 발송하고 토큰을 반환하는 메서드
     */
    public String sendSimpleMessage(String sendEmail, String username) throws MessagingException {
        String token = jwtProvider.generateToken(username);

        MimeMessage message = createMail(sendEmail, token);

        try {
            javaMailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            return "메일 발송 중 오류가 발생하였습니다.";
        }
        return token;
    }

    /**
     * 링크 클릭 시 토큰을 검증하는 메서드
     */
    public String verifyEmail(String token) {
        try {
            String username = jwtProvider.getUsernameFromEmailJwt(token);
            System.out.println("이메일 인증이 완료되었습니다. 사용자명: " + username);
            return username;
        } catch (ExpiredJwtException e) {
            return "토큰이 만료되었습니다. 다시 진행해주세요.";
        }
    }
}