package com.example.board.member.controller;


import com.example.board.board.service.ComCodeService;
import com.example.board.config.email.MailService;
import com.example.board.config.jwt.JwtTokenProvider;
import com.example.board.member.domain.ActivityType;
import com.example.board.member.dto.MemberDto;
import com.example.board.member.service.ActivityLogService;
import com.example.board.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final ComCodeService comCodeService;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ActivityLogService activityLogService;

    @GetMapping("/login")
    public String memberLoginVeiw() {
        return "/member/login";
    }

    @GetMapping("/join")
    public String memberJoinVeiw(Model model) {
        model.addAttribute("userJoinDto", new MemberDto());

        model.addAttribute("phoneNumbers", comCodeService.getPhoneCodes());
        return "/member/join";
    }

    @PostMapping("/join")
    public String memberJoin(@ModelAttribute MemberDto memberDto) throws MessagingException {
        memberService.save(memberDto);
        mailService.sendVerificationEmail(memberDto.getUserId(), memberDto.getUserEmail());
        return "redirect:/member/login?joined=true";
    }

    // 아이디 중복체크
    @GetMapping("/check-id")
    @ResponseBody
    public Map<String, Boolean> checkId(@RequestParam String userId) {

        boolean result = memberService.isAvailableUserId(userId);

        return Map.of("available", result);
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        String result = mailService.verifyEmail(token);

        return switch (result) {
            case "expired" -> "redirect:/member/login?verify=expired";
            case "invalid" -> "redirect:/member/login?verify=invalid";
            default -> {
                memberService.verifyEmail(result);
                yield "redirect:/member/login?verify=success";
            }
        };
    }

    @GetMapping("/check-email")
    @ResponseBody
    public Map<String, Boolean> checkEmail(@RequestParam String userEmail) {
        boolean result = memberService.isAvailableUserEmail(userEmail);
        return Map.of("available", result);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> apiLogin(@RequestBody Map<String, String> loginRequest,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.get("userId"),
                            loginRequest.get("userPw")
                    )
            );

            String token = jwtTokenProvider.generateToken(authentication.getName());

            Cookie cookie = new Cookie("access_token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            response.addCookie(cookie);

            String clientIp = getClientIp(request);
            activityLogService.save(authentication.getName(), ActivityType.LOGIN, clientIp);


            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "userId", authentication.getName()
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401)
                    .body(Map.of("message", "아이디 또는 비밀번호가 틀렸습니다."));
        } catch (DisabledException e) {
            return ResponseEntity.status(403)
                    .body(Map.of("message", "이메일 인증 후 로그인할 수 있습니다."));
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isBlank()) {
            return ip.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
