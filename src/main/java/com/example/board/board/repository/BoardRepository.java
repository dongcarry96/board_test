package com.example.board.board.repository;

import com.example.board.board.domain.Board;
import com.example.board.board.domain.BoardId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, BoardId> {
    Page<Board> findByIdBoardType(String boardType, Pageable pageable);
    Page<Board> findByIdBoardTypeIn(List<String> boardTypes, Pageable pageable);
    @Query("""
        select coalesce(max(b.id.boardNum), 0)
        from Board b
        where b.id.boardType = :boardType
    """)
    Integer findMaxBoardNum(String boardType);

    @Query(value = """
    select *
    from board
    where board_type = :type
      and board_num < :num
    order by board_num desc
    fetch first 1 rows only
    """, nativeQuery = true)
    Optional<Board> findPrevBoard(String type, Integer num);

    @Query(value = """
    select *
    from board
    where board_type = :type
      and board_num > :num
    order by board_num asc
    fetch first 1 rows only
    """, nativeQuery = true)
    Optional<Board> findNextBoard(String type, Integer num);

}
