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
    Page<Board> findAllByIsDeleted(String IsDeleted, Pageable pageable);
    Page<Board> findByIdBoardTypeAndIsDeleted(String boardType, String isDeleted, Pageable pageable );
    Page<Board> findByIdBoardTypeInAndIsDeleted(List<String> boardTypes, String isDeleted, Pageable pageable);
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
        and IS_DELETED = :isDeleted
    order by board_num desc
    fetch first 1 rows only
    """, nativeQuery = true)
    Optional<Board> findPrevBoard(@Param("type") String type,
                                  @Param("num") Integer num,
                                  @Param("isDeleted") String isDeleted);

    @Query(value = """
    select *
    from board
    where board_type = :type
      and board_num > :num
        and IS_DELETED = 'N'
    order by board_num asc
    fetch first 1 rows only
    """, nativeQuery = true)
    Optional<Board> findNextBoard(@Param("type") String type,
                                  @Param("num") Integer num);

    @Query("""
    SELECT b FROM Board  b
    WHERE b.isDeleted = 'N'
    AND (:keyword IS NULL OR (
    (:searchType = 'title' AND b.boardTitle LIKE %:keyword%)
    or (:searchType = 'creator' AND b.creator LIKE %:keyword%)
    or (:searchType = 'keyword' AND (b.boardTitle LIKE %:keyword% OR b.boardComment LIKE %:keyword%))
    ))
    AND (:#{#type == null || #type.isEmpty()} = true OR b.id.boardType IN :type)
    """)
    Page<Board> searchBoards(
            @Param("type") List<String> type,
            @Param("searchType") String searchType,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    Page<Board> findByCreatorAndIsDeleted(String creator, String isDeleted, Pageable pageable);
}
