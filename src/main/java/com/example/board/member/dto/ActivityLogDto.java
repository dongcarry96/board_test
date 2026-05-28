package com.example.board.member.dto;

import com.example.board.member.domain.ActivityLog;
import com.example.board.member.domain.ActivityType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ActivityLogDto {
    private Long logId;
    private String userId;
    private ActivityType activityType;
    private String clientIp;
    private String activityDt;

    public static ActivityLogDto fromEntity(ActivityLog activityLog) {
        return ActivityLogDto.builder()
                .logId(activityLog.getLogId())
                .userId(activityLog.getUserId())
                .activityType(activityLog.getActivityType())
                .clientIp(activityLog.getClientIp())
                .activityDt(activityLog.getActivityDt())
                .build();
    }

    public ActivityLog toEntity() {
        return ActivityLog.builder()
                .logId(logId)
                .userId(userId)
                .activityType(activityType)
                .clientIp(clientIp)
                .activityDt(activityDt)
                .build();
    }
}
