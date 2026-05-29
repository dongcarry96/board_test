package com.example.board.board.repository;

import com.example.board.board.domain.ScheduledBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduledBoardRepository extends JpaRepository<ScheduledBoard, Long> {

    List<ScheduledBoard> findByScheduledTimeLessThanEqual(LocalDateTime now);

    Page<ScheduledBoard> findByCreatorOrderByCreateTimeDesc(String creator, Pageable pageable);
}
