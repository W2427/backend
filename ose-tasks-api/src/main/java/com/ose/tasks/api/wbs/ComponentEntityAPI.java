package com.ose.tasks.api.wbs;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.ComponentEntryCriteriaDTO;
import com.ose.tasks.dto.ComponentEntryInsertDTO;
import com.ose.tasks.dto.ComponentEntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.wbs.entity.ComponentEntityBase;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface ComponentEntityAPI {

    /**
     * 查询管件实体。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 条件DTO
     * @param pageDTO     分页用DTO
     * @return 管件实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends ComponentEntityBase> searchComponentEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        ComponentEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得管件实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @return 管件实体详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<? extends ComponentEntityBase> getComponentEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 删除管件实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteComponentEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    );

    /**
     * 插入管件实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addComponentEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ComponentEntryInsertDTO componentEntryInsertDTO
    );

    /**
     * 更新管件实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ComponentEntityBase> updateComponentEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ComponentEntryUpdateDTO componentEntryUpdateDTO
    );

    /**
     * 更新父级。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody moveComponent(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    );

    /**
     * 取得管件图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getDrawingInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 取得管件材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getMaterialInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 按条件下载管件实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/component-entities/download"
    )
    void downloadComponentEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        ComponentEntryCriteriaDTO criteriaDTO
    ) throws IOException;

}
