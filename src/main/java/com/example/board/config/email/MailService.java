package com.example.board.config.email;

import com.example.board.config.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${email.base-url}")
    private String baseUrl;

    public void sendVerificationEmail(String userId, String email) throws MessagingException {
        String token = jwtTokenProvider.generateEmailToken(userId);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(senderEmail);
        helper.setTo(email);
        helper.setSubject("안녕하세요 김동현 게시판 회원인증 메일입니다.");

        String verifyUrl = baseUrl + "/member/verify?token=" + token;
        String body = """
                <h2>이메일 인증</h2>
                <p>아래 버튼을 클릭하여 이메일 인증을 완료해주세요.</p>
                <a href="%s">
                   이메일 인증하기
                </a>
                <p>%s</p>
                """.formatted(verifyUrl, verifyUrl);

        helper.setText(body, true);
        javaMailSender.send(message);
    }

    public String verifyEmail(String token) {
        try {
            return jwtTokenProvider.getUserIdFromEmailToken(token);
        } catch (ExpiredJwtException e) {
            return "expired";
        } catch (JwtException e) {
            return "invalid";
        }
    }
}