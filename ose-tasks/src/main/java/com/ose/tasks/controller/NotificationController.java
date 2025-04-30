package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.notifications.api.NotificationFeignAPI;
import com.ose.notifications.dto.NotificationPostDTO;
import com.ose.notifications.entity.NotificationBatch;
import com.ose.notifications.vo.NotificationType;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.api.NotificationAPI;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "通知管理接口")
@RestController
public class NotificationController extends BaseController implements NotificationAPI {


    private final NotificationFeignAPI notificationFeignAPI;

    /**
     * 构造方法。
     */
    @Autowired
    public NotificationController(
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            NotificationFeignAPI notificationFeignAPI
    ) {
        this.notificationFeignAPI = notificationFeignAPI;
    }

    @Operation(description = "发送通知")
    @Override
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/notifications",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<NotificationBatch> send(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "通知类型") NotificationType type,
        @RequestBody NotificationPostDTO postDTO
    ) {
        return notificationFeignAPI.send(orgId, projectId, type, postDTO);
    }

}
