package com.example.board.member.repository;

import com.example.board.member.domain.ActivityLog;
import com.example.board.member.domain.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
    List<ActivityLog> findByUserIdOrderByActivityDtDesc(String userId);
}