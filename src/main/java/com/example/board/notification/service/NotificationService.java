package com.example.board.notification.service;

import com.example.board.notification.domain.Notification;
import com.example.board.notification.dto.NotificationDto;
import com.example.board.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 알림 저장 (CommentService에서 호출)
    @Transactional
    public void send(String receiverId, String boardType, Integer boardNum,
                     String boardTitle, String commenterId, String commentContent) {
        Notification notification = Notification.builder()
                .receiverId(receiverId)
                .boardType(boardType)
                .boardNum(boardNum)
                .boardTitle(boardTitle)
                .commenterId(commenterId)
                .commentContent(commentContent)
                .build();

        notificationRepository.save(notification);
        log.info("[Notification] 알림 저장 - receiver: {}, commenter: {}, board: {}/{}",
                receiverId, commenterId, boardType, boardNum);
    }

    // 마이페이지 알림 목록 조회
    @Transactional(readOnly = true)
    public Page<NotificationDto> getMyNotifications(String receiverId, Pageable pageable) {
        return notificationRepository
                .findByReceiverIdOrderByCreateTimeDesc(receiverId, pageable)
                .map(NotificationDto::fromEntity);
    }

    // 알림 읽음 처리
    @Transactional
    public void markAsRead(Long notificationId, String receiverId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));
        if (!notification.getReceiverId().equals(receiverId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        notification.markAsRead();
    }

    // 전체 읽음 처리
    @Transactional
    public void markAllAsRead(String receiverId) {
        notificationRepository
                .findByReceiverIdOrderByCreateTimeDesc(receiverId, Pageable.unpaged())
                .forEach(n -> {
                    if ("N".equals(n.getIsRead())) n.markAsRead();
                });
    }
}
