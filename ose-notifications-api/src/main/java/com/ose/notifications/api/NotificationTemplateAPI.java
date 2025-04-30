package com.ose.notifications.api;

import com.ose.dto.PageDTO;
import com.ose.notifications.dto.NotificationTemplatePatchDTO;
import com.ose.notifications.dto.NotificationTemplatePostDTO;
import com.ose.notifications.entity.NotificationTemplate;
import com.ose.notifications.entity.NotificationTemplateBasic;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 通知模版操作接口。
 */
public interface NotificationTemplateAPI {

    /**
     * 创建系统通知消息模版。
     */
    @RequestMapping(
        method = POST,
        value = "/notification-templates",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationTemplate> create(
        @RequestBody NotificationTemplatePostDTO templateDTO
    );

    /**
     * 创建组织通知消息模版。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/notification-templates",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationTemplate> create(
        @PathVariable("orgId") Long orgId,
        @RequestBody NotificationTemplatePostDTO templateDTO
    );

    /**
     * 查询系统通知模版。
     */
    @RequestMapping(
        method = GET,
        value = "/notification-templates",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<NotificationTemplateBasic> search(
        @RequestParam("name") String name,
        @RequestParam("tags") Set<String> tags,
        PageDTO pageDTO
    );

    /**
     * 查询组织通知模版。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/notification-templates",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<NotificationTemplateBasic> search(
        @PathVariable("orgId") Long orgId,
        @RequestParam("name") String name,
        @RequestParam("tags") Set<String> tags,
        PageDTO pageDTO
    );

    /**
     * 取得系统通知模版详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationTemplate> get(
        @PathVariable("templateId") Long templateId
    );

    /**
     * 取得组织通知模版详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationTemplate> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("templateId") Long templateId
    );

    /**
     * 取得系统通知模版历史版本。
     */
    @RequestMapping(
        method = GET,
        value = "/notification-templates/{templateId}/history",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<NotificationTemplateBasic> history(
        @PathVariable("templateId") Long templateId,
        PageDTO pageDTO
    );

    /**
     * 取得组织通知模版历史版本。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/notification-templates/{templateId}/history",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<NotificationTemplateBasic> history(
        @PathVariable("orgId") Long orgId,
        @PathVariable("templateId") Long templateId,
        PageDTO pageDTO
    );

    /**
     * 更新系统通知模版信息。
     */
    @RequestMapping(
        method = PATCH,
        value = "/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationTemplate> update(
        @PathVariable("templateId") Long templateId,
        @RequestParam("version") long version,
        @RequestBody NotificationTemplatePatchDTO templateDTO
    );

    /**
     * 更新组织通知模版信息。
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<NotificationTemplate> update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("templateId") Long templateId,
        @RequestParam("version") long version,
        @RequestBody NotificationTemplatePatchDTO templateDTO
    );

    /**
     * 删除系统通知模版信息。
     */
    @RequestMapping(
        method = DELETE,
        value = "/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("templateId") Long templateId,
        @RequestParam("version") long version
    );

    /**
     * 删除组织通知模版信息。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/notification-templates/{templateId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("templateId") Long templateId,
        @RequestParam("version") long version
    );

}
