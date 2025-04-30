package com.ose.notifications.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.notifications.api.NotificationTemplateAPI;
import com.ose.notifications.domain.model.service.NotificationTemplateInterface;
import com.ose.notifications.dto.NotificationTemplatePatchDTO;
import com.ose.notifications.dto.NotificationTemplatePostDTO;
import com.ose.notifications.entity.NotificationTemplate;
import com.ose.notifications.entity.NotificationTemplateBasic;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "通知模版管理接口")
@RestController
public class NotificationTemplateController extends BaseController implements NotificationTemplateAPI {

    // 模版服务
    private NotificationTemplateInterface templateService;

    /**
     * 构造方法。
     */
    @Autowired
    public NotificationTemplateController(
        NotificationTemplateInterface notificationTemplateService
    ) {
        templateService = notificationTemplateService;
    }

    @Operation(
        summary = "创建系统通知消息模版",
        description = "使用 <a href=\"http://velocity.apache.org/engine/1.7/developer-guide.html\" target=\"_blank\">Apache Velocity</a> 作为模版引擎。<br>"
            + "通知发送接口的请求的 Body 部分包含一个名为 <code>parameters</code> 字段，该字段若为列表，那么它在模版的上下文中将会被命名为 <code>items</code>，否则其各属性值将以各自的属性名设置到上下文中。<br>"
            + "另外，模版中可通过 <code>SENDER</code> 参数引用发送者用户的信息，通过 <code>RECEIVER</code> 参数（仅当通知不为公告时）引用接收者用户的信息。<br>"
    )
    @Override
    @RequestMapping(
        method = POST,
        value = "/notification-templates",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationTemplate> create(
        @RequestBody @Valid NotificationTemplatePostDTO templateDTO
    ) {
        return create(null, templateDTO);
    }

    @Operation(
        summary = "创建组织通知消息模版",
        description = "使用 Apache Velocity 作为模版引擎。"
    )
    @Override
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/notification-templates",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationTemplate> create(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @RequestBody @Valid NotificationTemplatePostDTO templateDTO
    ) {
        return new JsonObjectResponseBody<>(
            templateService
                .create(getContext().getOperator(), orgId, templateDTO)
        );
    }

    @Operation(description = "查询系统通知模版")
    @Override
    @RequestMapping(
        method = GET,
        value = "/notification-templates",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonListResponseBody<NotificationTemplateBasic> search(
        @RequestParam(required = false) @Parameter(description = "模板名称") String name,
        @RequestParam(required = false) @Parameter(description = "标签列表") Set<String> tags,
        PageDTO pageDTO
    ) {
        return search(null, name, tags, pageDTO);
    }

    @Operation(description = "查询组织通知模版")
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/notification-templates",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonListResponseBody<NotificationTemplateBasic> search(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @RequestParam(required = false) @Parameter(description = "模板名称") String name,
        @RequestParam(required = false) @Parameter(description = "标签列表") Set<String> tags,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            templateService.search(orgId, name, tags, pageDTO.toPageable())
        );
    }

    @Operation(description = "取得系统通知模版详细信息")
    @Override
    @RequestMapping(
        method = GET,
        value = "/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationTemplate> get(
        @PathVariable @Parameter(description = "模版 ID") Long templateId
    ) {
        return get(null, templateId);
    }

    @Operation(description = "取得组织通知模版详细信息")
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationTemplate> get(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "模版 ID") Long templateId
    ) {
        return new JsonObjectResponseBody<>(templateService.get(orgId, templateId));
    }

    @Operation(description = "取得系统通知模版历史版本")
    @Override
    @RequestMapping(
        method = GET,
        value = "/notification-templates/{templateId}/history",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonListResponseBody<NotificationTemplateBasic> history(
        @PathVariable @Parameter(description = "模版 ID") Long templateId,
        PageDTO pageDTO
    ) {
        return history(null, templateId, pageDTO);
    }

    @Operation(description = "取得组织通知模版历史版本")
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/notification-templates/{templateId}/history",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonListResponseBody<NotificationTemplateBasic> history(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "模版 ID") Long templateId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(templateService.history(orgId, templateId, pageDTO.toPageable()));
    }

    @Operation(
        summary = "更新系统通知模版信息",
        description = "更新模版内容可能会导致模版 ID 被更新。"
    )
    @Override
    @RequestMapping(
        method = PATCH,
        value = "/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationTemplate> update(
        @PathVariable @Parameter(description = "模版 ID") Long templateId,
        @RequestParam @Parameter(description = "模版更新版本号") long version,
        @RequestBody @Valid NotificationTemplatePatchDTO templateDTO
    ) {
        return update(null, templateId, version, templateDTO);
    }

    @Operation(
        summary = "更新组织通知模版信息",
        description = "更新模版内容可能会导致模版 ID 被更新。"
    )
    @Override
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonObjectResponseBody<NotificationTemplate> update(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "模版 ID") Long templateId,
        @RequestParam @Parameter(description = "模版更新版本号") long version,
        @RequestBody @Valid NotificationTemplatePatchDTO templateDTO
    ) {
        return new JsonObjectResponseBody<>(
            templateService.update(
                getContext().getOperator(),
                orgId,
                templateId,
                templateDTO,
                version
            )
        );
    }

    @Operation(description = "删除系统通知模版信息")
    @Override
    @RequestMapping(
        method = DELETE,
        value = "/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "模版 ID") Long templateId,
        @RequestParam @Parameter(description = "模版更新版本号") long version
    ) {
        return delete(null, templateId, version);
    }

    @Operation(description = "删除组织通知模版信息")
    @Override
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege // TODO
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "模版 ID") Long templateId,
        @RequestParam @Parameter(description = "模版更新版本号") long version
    ) {

        templateService.delete(
            getContext().getOperator(),
            orgId,
            templateId,
            version
        );

        return new JsonResponseBody();
    }

}
