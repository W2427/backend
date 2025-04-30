package com.ose.notifications.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.notifications.api.NotificationConfigurationAPI;
import com.ose.notifications.domain.model.service.NotificationConfigurationInterface;
import com.ose.notifications.domain.model.service.NotificationTemplateInterface;
import com.ose.notifications.dto.NotificationConfigurationPatchDTO;
import com.ose.notifications.dto.NotificationConfigurationPostDTO;
import com.ose.notifications.entity.NotificationConfiguration;
import com.ose.notifications.entity.NotificationConfigurationBasic;
import com.ose.notifications.vo.NotificationType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "通知配置管理接口")
@RestController
public class NotificationConfigurationController extends BaseController implements NotificationConfigurationAPI {

    // 通知配置管理服务
    private final NotificationConfigurationInterface configurationService;

    // 通知消息模版管理服务
    private final NotificationTemplateInterface templateService;

    /**
     * 构造方法。
     */
    @Autowired
    public NotificationConfigurationController(
        NotificationConfigurationInterface notificationConfigurationInterface,
        NotificationTemplateInterface notificationTemplateInterface
    ) {
        configurationService = notificationConfigurationInterface;
        templateService = notificationTemplateInterface;
    }

    @Operation(description = "创建通知配置")
    @Override
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations/{notificationType}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationConfigurationBasic> create(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "通知类型") NotificationType notificationType,
        @RequestBody @Valid NotificationConfigurationPostDTO configurationDTO
    ) {
        return new JsonObjectResponseBody<>(
            configurationService.create(
                getContext().getOperator(),
                orgId,
                projectId,
                notificationType,
                configurationDTO
            )
        );
    }

    @Operation(description = "查询通知配置")
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    @SetUserInfo
    public JsonListResponseBody<NotificationConfigurationBasic> search(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PageDTO pageDTO
    ) {
        return (new JsonListResponseBody<>(
            configurationService.search(orgId, projectId, pageDTO.toPageable())
        )).setIncluded(templateService);
    }

    @Operation(description = "取得通知配置信息")
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations/{notificationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationConfiguration> get(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "通知 ID") Long notificationId
    ) {
        return new JsonObjectResponseBody<>(configurationService.get(orgId, projectId, notificationId));
    }

    @Operation(description = "更新通知配置")
    @Override
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations/{notificationType}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationConfigurationBasic> update(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "通知类型") NotificationType notificationType,
        @RequestParam @Parameter(description = "通知配置更新版本号") long version,
        @RequestBody @Valid NotificationConfigurationPatchDTO configurationDTO
    ) {
        return new JsonObjectResponseBody<>(
            configurationService.update(
                getContext().getOperator(),
                orgId,
                projectId,
                notificationType,
                version,
                configurationDTO
            )
        );
    }

    @Operation(description = "删除通知配置")
    @Override
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/notification-configurations/{notificationType}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationConfigurationBasic> delete(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "通知类型") NotificationType notificationType,
        @RequestParam @Parameter(description = "配置更新版本号") long version
    ) {
        return null;
    }

}
