package com.ose.notifications.api;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.notifications.dto.NotificationCreateDTO;
import com.ose.notifications.dto.NotificationPostDTO;
import com.ose.notifications.entity.Notification;
import com.ose.notifications.entity.NotificationBatch;
import com.ose.notifications.vo.NotificationType;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 通知操作接口。
 */
@FeignClient("ose-notifications")
public interface NotificationFeignAPI {

    /**
     * 发送通知。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/notifications",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationBatch> send(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("type") NotificationType notificationType,
        @RequestBody NotificationPostDTO notificationDTO
    );

    /**
     * 取得批次详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/notification-batches/{batchId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    // TODO
    JsonObjectResponseBody<NotificationBatch> batch(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("batchId") Long batchId
    );

    /**
     * 重新发送发送失败的通知。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/notification-batches/{batchId}/resend",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    // TODO
    JsonResponseBody resend(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("batchId") Long batchId
    );

    /**
     * 取得用户的通知详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/users/{userId}/notifications/{notificationId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Notification> get(
        @PathVariable("userId") Long userId,
        @PathVariable("notificationId") Long notificationId
    );

    /**
     * 将通知标记为已读。
     */
    @RequestMapping(
        method = PUT,
        value = "/users/{userId}/notifications/{notificationId}/read",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setAsRead(
        @PathVariable("userId") Long userId,
        @PathVariable("notificationId") Long notificationId
    );

    /**
     * 将通知标记为未读。
     */
    @RequestMapping(
        method = DELETE,
        value = "/users/{userId}/notifications/{notificationId}/read",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setAsUnread(
        @PathVariable("userId") Long userId,
        @PathVariable("notificationId") Long notificationId
    );

    /**
     * 创建消息通知。
     *
     * @param notificationCreateDTO 通知创建传输数据
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/messages",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @RequestBody NotificationCreateDTO notificationCreateDTO
    );

    @RequestMapping(
        method = DELETE,
        value = "/delete-all",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteAll();

}
