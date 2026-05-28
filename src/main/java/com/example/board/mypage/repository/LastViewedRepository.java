package com.example.board.mypage.repository;


import com.example.board.mypage.domain.LastViewed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LastViewedRepository extends JpaRepository<LastViewed, String> {
}
