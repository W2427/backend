package com.ose.notifications.domain.model.service;

import com.ose.dto.OperatorDTO;
import com.ose.notifications.dto.NotificationCreateDTO;
import com.ose.notifications.dto.NotificationSearchDTO;
import com.ose.notifications.dto.receiver.UserReceiverDTO;
import com.ose.notifications.entity.Notification;
import com.ose.notifications.entity.NotificationBatch;
import com.ose.notifications.entity.NotificationBatchSummary;
import com.ose.notifications.entity.UserNotification;
import com.ose.notifications.vo.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 通知管理服务接口。
 */
public interface NotificationInterface {

    /**
     * 发送指定组织下所有待发送的通知邮件/短信。
     *
     * @param orgId 组织 ID
     */
    void sendAll(Long orgId);

    /**
     * 保存通知发送日志。
     *
     * @param operator         操作者信息
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param parameter        消息参数对象
     * @param users            发送目标用户信息
     * @return 发送数量
     */
    NotificationBatch saveBatchLogs(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        Object parameter,
        List<UserReceiverDTO> users
    );

    /**
     * 取得通知批次列表。
     *
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param pageable         分页参数
     * @return 批次列表
     */
    Page<NotificationBatchSummary> batches(
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        Pageable pageable
    );

    Page<Notification> search(
        NotificationSearchDTO notificationSearchDTO);

    /**
     * 取得批次信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param batchId   批次 ID
     * @return 批次信息
     */
    NotificationBatch getBatch(
        Long orgId,
        Long projectId,
        Long batchId
    );

    /**
     * 重新发送发送失败的通知。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param batchId   批次 ID
     */
    void resend(
        Long orgId,
        Long projectId,
        Long batchId
    );

    /**
     * 取得通知详细。
     *
     * @param userId         用户 ID
     * @param notificationId 通知 ID
     * @return 通知
     */
    Notification get(
        Long userId,
        Long notificationId
    );

    /**
     * 标记为已读。
     *
     * @param userId         用户 ID
     * @param notificationId 通知 ID
     */
    void setAsRead(
        Long userId,
        Long notificationId
    );

    /**
     * 标记为未读。
     *
     * @param userId         用户 ID
     * @param notificationId 通知 ID
     */
    void setAsUnread(
        Long userId,
        Long notificationId
    );

    /**
     * 查询通知。
     *
     * @param userId           用户 ID
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param readByUser       是否已读
     * @param pageable         分页参数
     * @return 通知列表
     */
    Page<UserNotification> search(
        Long userId,
        Long orgId,
        Long projectId,
        NotificationType notificationType,
        Boolean readByUser,
        Pageable pageable
    );

    /**
     * 创建消息通知。
     * @param notificationCreateDTO 通知创建传输数据 */
    void create(
//        Long operatorId,
//        String operatorName,
        NotificationCreateDTO notificationCreateDTO
    );

    /**
     * 修改消息通知。
     *  @param messageId
     * @param operatorName
     * @param operatorId
     * @param notificationUpdateDTO
     */
    void update(Long messageId, String operatorName, Long operatorId, NotificationCreateDTO notificationUpdateDTO);

    /**
     * 修改消息通知。
     *  @param messageId
     * @param notificationUpdateDTO
     */
    void updateStatus(Long messageId, NotificationCreateDTO notificationUpdateDTO);

    void deleteAll();
}
