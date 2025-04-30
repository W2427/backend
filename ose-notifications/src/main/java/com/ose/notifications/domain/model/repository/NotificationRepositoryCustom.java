package com.ose.notifications.domain.model.repository;

import com.ose.notifications.dto.NotificationSearchDTO;
import com.ose.notifications.entity.Notification;
import org.springframework.data.domain.Page;

/**
 * 通知数据仓库。
 */
public interface NotificationRepositoryCustom {

    Page<Notification> search(NotificationSearchDTO notificationSearchDTO);
}
