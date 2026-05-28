package com.example.board.mypage.service;

import com.example.board.mypage.domain.LastViewed;
import com.example.board.mypage.repository.LastViewedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LastViewedService {
    private final LastViewedRepository lastViewedRepository;

    @Transactional
    public void update(String userId, String boardType, Integer boardNum) {
        LastViewed lastViewed = lastViewedRepository.findById(userId)
                .orElse(LastViewed.builder()
                        .userId(userId)
                        .build());

        lastViewed.update(boardType, boardNum);
        lastViewedRepository.save(lastViewed);
    }
}
