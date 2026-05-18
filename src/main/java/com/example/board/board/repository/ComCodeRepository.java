package com.example.board.board.repository;

import com.example.board.board.domain.ComCode;
import com.example.board.board.domain.ComCodeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComCodeRepository extends JpaRepository<ComCode, ComCodeId> {
    List<ComCode> findByIdCodeType(String codeType);
}
