package com.example.board.board.repository;

import com.example.board.board.domain.Board;
import com.example.board.board.domain.BoardId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, BoardId> {
    Page<Board> findByIdBoardType(String boardType, Pageable pageable);
    @Query("""
        select coalesce(max(b.id.boardNum), 0)
        from Board b
        where b.id.boardType = :boardType
    """)
    Integer findMaxBoardNum(String boardType);
}
