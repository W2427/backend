package com.ose.notifications.api;

import com.ose.dto.PageDTO;
import com.ose.notifications.dto.NotificationConfigurationPatchDTO;
import com.ose.notifications.dto.NotificationConfigurationPostDTO;
import com.ose.notifications.entity.NotificationConfiguration;
import com.ose.notifications.entity.NotificationConfigurationBasic;
import com.ose.notifications.vo.NotificationType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 通知配置操作接口。
 */
public interface NotificationConfigurationAPI {

    /**
     * 创建通知配置。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations/{notificationType}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationConfigurationBasic> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("notificationType") NotificationType notificationType,
        @RequestBody NotificationConfigurationPostDTO configurationDTO
    );

    /**
     * 查询通知配置。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<NotificationConfigurationBasic> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO
    );

    /**
     * 取得通知配置信息。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations/{notificationId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationConfiguration> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("notificationId") Long notificationId
    );

    /**
     * 更新通知配置。
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations/{notificationType}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationConfigurationBasic> update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("notificationType") NotificationType notificationType,
        @RequestParam("version") long version,
        @RequestBody NotificationConfigurationPatchDTO configurationDTO
    );

    /**
     * 删除通知配置。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations/{notificationType}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationConfigurationBasic> delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("notificationType") NotificationType notificationType,
        @RequestParam("version") long version
    );

}
