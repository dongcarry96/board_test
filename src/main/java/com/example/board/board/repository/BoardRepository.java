package com.example.board.board.repository;

import com.example.board.board.domain.Board;
import com.example.board.board.domain.BoardId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, BoardId> {
    Page<Board> findAll(Pageable pageable);
}
