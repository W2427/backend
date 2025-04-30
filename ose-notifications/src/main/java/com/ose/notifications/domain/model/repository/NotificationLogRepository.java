package com.ose.notifications.domain.model.repository;

import com.ose.notifications.entity.NotificationLog;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 通知记录数据仓库。
 */
public interface NotificationLogRepository extends PagingAndSortingWithCrudRepository<NotificationLog, Long> {

    /**
     * 取得通知日志。
     *
     * @param userId         用户 ID
     * @param notificationId 通知 ID
     * @return 通知日志
     */
    Optional<NotificationLog> findByUserIdAndNotificationId(Long userId, Long notificationId);

    /**
     * 取得待发送的通知日志。
     *
     * @param orgId    组织 ID
     * @param pageable 分页参数
     * @return 通知日志列表
     */
    @Query("SELECT n FROM NotificationLog n WHERE n.orgId = :orgId AND (n.emailSendStatus = com.ose.notifications.vo.NotificationSendStatus.PENDING OR n.smsSendStatus = com.ose.notifications.vo.NotificationSendStatus.PENDING)")
    List<NotificationLog> findBySendStatusIsPending(@Param("orgId") Long orgId, Pageable pageable);

    /**
     * 更新电子邮件发送状态为待发送。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param batchId   批次 ID
     */
    @Modifying
    @Query("UPDATE NotificationLog n SET n.emailSendStatus = com.ose.notifications.vo.NotificationSendStatus.PENDING WHERE n.orgId = :orgId AND n.projectId = :projectId AND n.batchId = :batchId AND n.emailSendStatus = com.ose.notifications.vo.NotificationSendStatus.FAILED")
    @Transactional
    void setEmailSendStatusAsPending(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("batchId") Long batchId
    );

    /**
     * 更新短信发送状态为待发送。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param batchId   批次 ID
     */
    @Modifying
    @Query("UPDATE NotificationLog n SET n.smsSendStatus = com.ose.notifications.vo.NotificationSendStatus.PENDING WHERE n.orgId = :orgId AND n.projectId = :projectId AND n.batchId = :batchId AND n.smsSendStatus = com.ose.notifications.vo.NotificationSendStatus.FAILED")
    @Transactional
    void setSmsSendStatusAsPending(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("batchId") Long batchId
    );

}
