package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.Notification;
import com.ose.notifications.vo.NotificationType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 通知数据仓库。
 */
public interface NotificationRepository extends PagingAndSortingWithCrudRepository<Notification, Long>, NotificationRepositoryCustom {

    Page<Notification> findByOrderByCreatedAtDesc(Pageable pageable);

    List<Notification> findByStatus(EntityStatus entityStatus);

    List<Notification> findByStatusAndType(EntityStatus entityStatus, NotificationType type);


}
