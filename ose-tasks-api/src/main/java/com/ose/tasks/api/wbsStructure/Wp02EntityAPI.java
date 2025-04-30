package com.ose.tasks.api.wbsStructure;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.structureWbs.Wp02EntryCriteriaDTO;
import com.ose.tasks.dto.structureWbs.Wp02EntryInsertDTO;
import com.ose.tasks.dto.structureWbs.Wp02EntryUpdateDTO;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp02EntityBase;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface Wp02EntityAPI {

    /**
     * 查询结构 分段 Section Wp02 实体。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页条件
     * @return 结构分段 Section Wp02 实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends Wp02EntityBase> searchWp02Entities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        Wp02EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );


    /**
     * 查询任务包 结构 分段 实体。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页条件
     * @return 任务包 结构 分段 实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-task-package-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends Wp02EntityBase> searchWp02TaskPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        Wp02EntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得 结构 分段 Section Wp02 实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @return 结构分段 Section Wp02 实体详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<? extends Wp02EntityBase> getWp02Entity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 删除结构 分段 Section Wp02实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteWp02Entity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version
    );

    /**
     * 插入结构 分段 Section Wp02实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addWp02Entity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody Wp02EntryInsertDTO isoEntryInsertDTO
    );

    /**
     * 更新结构 分段 Section Wp02实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateWp02Entity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody Wp02EntryUpdateDTO isoEntryUpdateDTO
    );

    /**
     * 更新父级信息。
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}/hierarchy-parents",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody moveWp02(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体 ID") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    );

    /**
     * 取得结构 分段 Section Wp02图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getDrawingInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 取得结构 分段 Section Wp02材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getMaterialInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 按条件下载结构 分段 Section Wp02实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wp02-entities/download"
    )
    void downloadWp02Entities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        Wp02EntryCriteriaDTO criteriaDTO
    ) throws IOException;

}
