package com.ose.tasks.api;

import com.ose.notifications.dto.NotificationPostDTO;
import com.ose.notifications.entity.NotificationBatch;
import com.ose.notifications.vo.NotificationType;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 项目管理接口。
 */
public interface NotificationAPI {

    /**
     * 发送通知。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/notifications",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationBatch> send(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("type") NotificationType type,
        @RequestBody NotificationPostDTO postDTO
    );

}
