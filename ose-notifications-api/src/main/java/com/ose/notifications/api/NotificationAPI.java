package com.ose.notifications.api;

import com.ose.dto.PageDTO;
import com.ose.notifications.dto.NotificationCreateDTO;
import com.ose.notifications.dto.NotificationSearchDTO;
import com.ose.notifications.entity.Notification;
import com.ose.notifications.entity.NotificationBatchSummary;
import com.ose.notifications.entity.UserNotification;
import com.ose.notifications.vo.NotificationType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 通知操作接口。
 */
public interface NotificationAPI extends NotificationFeignAPI {

    /**
     * 查询通知批次列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/notification-batches",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<NotificationBatchSummary> batches(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam(name = "type", required = false) NotificationType notificationType,
        PageDTO pageDTO
    );

    /**
     * 查询用户的通知。
     *
     * @param userId           用户 ID
     * @param orgId            组织 ID
     * @param projectId        项目 ID
     * @param notificationType 通知类型
     * @param readByUser       是否已读
     * @param pageDTO          分页参数
     * @return 通知列表
     */
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/notifications",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<UserNotification> list(
        @PathVariable("userId") Long userId,
        @RequestParam("orgId") Long orgId,
        @RequestParam("projectId") Long projectId,
        @RequestParam(name = "type", required = false) NotificationType notificationType,
        @RequestParam(name = "read", required = false) Boolean readByUser,
        PageDTO pageDTO
    );

    /**
     * 查询消息通知。
     * @param notificationSearchDTO
     * @return 消息通知列表。
     */
    @RequestMapping(
        method = GET ,
        value = "/messages",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Notification> list(
        NotificationSearchDTO notificationSearchDTO
    );

    /**
     * 创建消息通知。
     *
     * @param notificationCreateDTO 通知创建传输数据
     * @return
     */
//    @RequestMapping(
//        method = POST,
//        value = "/messages",
//        produces = APPLICATION_JSON_VALUE
//    )
//    JsonResponseBody create(
//        @RequestBody NotificationCreateDTO notificationCreateDTO
//    );

    /**
     * 修改消息通知
     *
     *
     * @param messageId
     * @param notificationCreateDTO 通知修改传输对象
     * @return
     */
    @RequestMapping(
        method = PATCH,
        value = "/messages/{messageId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("消息通知Id") Long messageId,
        NotificationCreateDTO notificationCreateDTO
    );

    /**
     * 修改消息状态
     *
     *
     * @param messageId
     * @param notificationCreateDTO 通知修改传输对象
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/messages/{messageId}/upadte-status",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateStatus(
        @PathVariable("消息通知Id") Long messageId,
        @RequestBody NotificationCreateDTO notificationCreateDTO
    );

}
