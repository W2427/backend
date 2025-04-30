package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationBatchBasic;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 通知批次数据仓库。
 */
public interface NotificationBatchBasicRepository extends PagingAndSortingRepository<NotificationBatchBasic, Long> {
}
