package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.UserNotification;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 通知数据仓库。
 */
public interface UserNotificationRepository extends PagingAndSortingWithCrudRepository<UserNotification, Long>, UserNotificationCustomRepository {

    UserNotification findAllById(Long messageId);
}
