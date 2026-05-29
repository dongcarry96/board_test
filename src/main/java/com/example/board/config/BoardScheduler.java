package com.example.board.config;

import com.example.board.board.domain.Board;
import com.example.board.board.domain.BoardId;
import com.example.board.board.domain.ScheduledBoard;
import com.example.board.board.repository.BoardRepository;
import com.example.board.board.repository.ScheduledBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardScheduler {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private final ScheduledBoardRepository scheduledBoardRepository;
    private final BoardRepository boardRepository;

    @Scheduled(cron = "0 * * * * *")   // 매분 0초에 실행
    @Transactional
    public void publishScheduledBoards() {
        LocalDateTime now = LocalDateTime.now(KST);
        log.info("[예약게시글] 스케줄러 실행됨 - 현재 시각: {}", now);
        List<ScheduledBoard> targets = scheduledBoardRepository.findByScheduledTimeLessThanEqual(now);

        if (targets.isEmpty()) {
            return;
        }

        log.info("[예약게시글] 발행 대상 {}건 처리 시작 (KST: {})", targets.size(), now);

        for (ScheduledBoard scheduled : targets) {
            try {
                publishOne(scheduled);
            } catch (Exception e) {
                log.error("[예약게시글] 발행 실패 - scheduleId: {}, error: {}",
                        scheduled.getScheduleId(), e.getMessage(), e);
            }
        }

        log.info("[예약게시글] 발행 처리 완료");
    }

    private void publishOne(ScheduledBoard scheduled) {
        Integer maxNum = boardRepository.findMaxBoardNum(scheduled.getBoardType());
        int newNum = (maxNum == null ? 0 : maxNum) + 1;

        Board board = Board.builder()
                .id(new BoardId(scheduled.getBoardType(), newNum))
                .boardTitle(scheduled.getBoardTitle())
                .boardComment(scheduled.getBoardComment())
                .boardHit(0)
                .creator(scheduled.getCreator())
//                .createTime(scheduled.getCreateTime())
                .fileRoot(scheduled.getFileRoot())
                .isDeleted("N")
                .build();

        boardRepository.save(board);

        scheduledBoardRepository.delete(scheduled);

        log.info("[예약게시글] 발행 완료 - scheduleId: {} → boardType: {}, boardNum: {}",
                scheduled.getScheduleId(), scheduled.getBoardType(), newNum);
    }
}
