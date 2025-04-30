package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationBatchSummary;
import com.ose.notifications.vo.NotificationType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 通知数据仓库。
 */
public interface NotificationBatchSummaryRepository extends PagingAndSortingWithCrudRepository<NotificationBatchSummary, Long> {

    Page<NotificationBatchSummary> findByOrgIdAndProjectId(Long orgId, Long projectId, Pageable pageable);

    Page<NotificationBatchSummary> findByOrgIdAndProjectIdAndType(Long orgId, Long projectId, NotificationType type, Pageable pageable);

}
