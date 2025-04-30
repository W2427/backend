package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationLogBasic;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 通知记录数据仓库。
 */
public interface NotificationLogBasicRepository extends PagingAndSortingRepository<NotificationLogBasic, Long> {
}
