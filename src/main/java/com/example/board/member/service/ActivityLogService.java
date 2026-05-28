package com.example.board.member.service;

import com.example.board.member.domain.ActivityLog;
import com.example.board.member.domain.ActivityType;
import com.example.board.member.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Async
    public void save(String userId, ActivityType activityType, String clientIp) {
            ActivityLog activityLog = ActivityLog.builder()
                    .userId(userId)
                    .activityType(activityType)
                    .clientIp(clientIp)
                    .build();
            activityLogRepository.save(activityLog);
    }

    public List<ActivityLog> getUserActivityLogs(String userId) {
        return activityLogRepository.findByUserIdOrderByActivityDtDesc(userId);
    }
}