package com.example.board.board.service;

import com.example.board.board.domain.ScheduledBoard;
import com.example.board.board.dto.ScheduledBoardDto;
import com.example.board.board.repository.ScheduledBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledBoardService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final ScheduledBoardRepository scheduledBoardRepository;

    // 예약 게시글 등록
    @Transactional
    public void register(ScheduledBoardDto dto) {
        LocalDateTime now = LocalDateTime.now(KST);
        if (dto.getScheduledTime() == null) {
            throw new IllegalArgumentException("예약 시간을 입력해주세요.");
        }
        if (!dto.getScheduledTime().isAfter(now)) {
            throw new IllegalArgumentException("예약 시간은 현재 시각 이후여야 합니다.");
        }

        scheduledBoardRepository.save(dto.toEntity());
        log.info("예약 게시글 등록 - creator: {}, scheduledTime: {}", dto.getCreator(), dto.getScheduledTime());
    }

    // 예약 게시글 취소
    @Transactional
    public void cancel(Long scheduleId, String userId) {
        ScheduledBoard scheduled = scheduledBoardRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("예약 게시글을 찾을 수 없습니다."));

        if (!scheduled.getCreator().equals(userId)) {
            throw new IllegalArgumentException("취소 권한이 없습니다.");
        }

        scheduledBoardRepository.delete(scheduled);
        log.info("예약 게시글 취소 - scheduleId: {}, userId: {}", scheduleId, userId);
    }

    // 예약 게시글 조회
    @Transactional(readOnly = true)
    public Page<ScheduledBoardDto> getMyScheduledBoards(String creator, Pageable pageable) {
        return scheduledBoardRepository
                .findByCreatorOrderByCreateTimeDesc(creator, pageable)
                .map(ScheduledBoardDto::fromEntity);
    }
}
