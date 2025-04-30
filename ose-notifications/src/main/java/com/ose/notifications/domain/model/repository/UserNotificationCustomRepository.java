package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.UserNotification;
import com.ose.notifications.vo.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 通知数据仓库。
 */
public interface UserNotificationCustomRepository {

    Page<UserNotification> search(
        Long userId,
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        Boolean readByUser,
        Pageable pageable
    );

    Page<UserNotification> findAllOrderByCreatedAtDesc(
        Pageable pageable
    );

}
