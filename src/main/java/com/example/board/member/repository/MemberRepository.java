package com.example.board.member.repository;

import com.example.board.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByUserIdAndIsDeleted(String userId, String isDeleted);
    Optional<Member> findByUserEmailAndIsDeleted(String userEmail, String isDeleted);

    Page<Member> findAllByIsDeleted(String isDeleted, Pageable pageable);

}
