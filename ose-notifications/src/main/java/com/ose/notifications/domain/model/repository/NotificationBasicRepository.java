package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationBasic;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 通知数据仓库。
 */
public interface NotificationBasicRepository extends PagingAndSortingRepository<NotificationBasic, Long> {
}
