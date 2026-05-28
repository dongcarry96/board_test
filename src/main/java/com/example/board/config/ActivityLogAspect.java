package com.example.board.config;

import com.example.board.member.domain.ActivityType;
import com.example.board.member.service.ActivityLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {
    private final ActivityLogService activityLogService;

    @AfterReturning("execution(* com.example.board.board.service.BoardService.register(..))")
    public void logBoardCreate() {
        saveLog(ActivityType.BOARD_CREATE);
    }

    @AfterReturning("execution(* com.example.board.board.service.BoardService.update(..))")
    public void logBoardUpdate() {
        saveLog(ActivityType.BOARD_UPDATE);
    }

    @AfterReturning("execution(* com.example.board.board.service.BoardService.boardDelete(..))")
    public void logBoardDelete() {
        saveLog(ActivityType.BOARD_DELETE);
    }

    @AfterReturning("execution(* com.example.board.board.service.BoardService.checkDelete(..))")
    public void logBoardBulkDelete() {
        saveLog(ActivityType.BOARD_DELETE);
    }

    @AfterReturning("execution(* com.example.board.comment.service.CommentService.save(..))")
    public void logCommentCreate() {
        saveLog(ActivityType.COMMENT_CREATE);
    }

    @AfterReturning("execution(* com.example.board.comment.service.CommentService.update(..))")
    public void logCommentUpdate() {
        saveLog(ActivityType.COMMENT_UPDATE);
    }

    @AfterReturning("execution(* com.example.board.comment.service.CommentService.delete(..))")
    public void logCommentDelete() {
        saveLog(ActivityType.COMMENT_DELETE);
    }

    private void saveLog(ActivityType activityType) {
            String userId = getCurrentUserId();
            String clientIp = getClientIp();
            activityLogService.save(userId, activityType, clientIp);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String name = authentication.getName();
        return "anonymousUser".equals(name) ? null : name;
    }

    private String getClientIp() {
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs == null) return "unknown";

            HttpServletRequest request = attrs.getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            if (ip != null && !ip.isBlank()) {
                return ip.split(",")[0].trim();
            }
            return request.getRemoteAddr();
        } catch (Exception e) {
            return "unknown";
        }
    }
}