package com.ose.tasks.api.wbsStructure;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.structureWbs.Wp01EntryCriteriaDTO;
import com.ose.tasks.dto.structureWbs.Wp01EntryInsertDTO;
import com.ose.tasks.dto.structureWbs.Wp01EntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.wbs.structureEntity.Wp01EntityBase;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface Wp01EntityAPI {

    /**
     * 查询结构 模块 实体。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页条件
     * @return 结构模块 实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends Wp01EntityBase> searchWp01Entities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        Wp01EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 查询任务包 结构 模块 实体。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页条件
     * @return 任务包 结构模块 实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-task-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends Wp01EntityBase> searchWp01TaskPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp01EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得 结构 模块 实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @return 结构模块 实体详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<? extends Wp01EntityBase> getWp01Entity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 删除结构 模块实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteWp01Entity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version
    );

    /**
     * 插入结构 模块实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addWp01Entity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody Wp01EntryInsertDTO isoEntryInsertDTO
    );

    /**
     * 更新结构 模块实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateWp01Entity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody Wp01EntryUpdateDTO isoEntryUpdateDTO
    );

    /**
     * 更新父级信息。
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody moveWp01(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    );

    /**
     * 取得结构 模块图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<SubDrawing> getDrawingInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 取得结构 模块材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getMaterialInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 按条件下载结构 模块实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp01-entities/download"
    )
    void downloadWp01Entities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        Wp01EntryCriteriaDTO criteriaDTO
    ) throws IOException;

}
