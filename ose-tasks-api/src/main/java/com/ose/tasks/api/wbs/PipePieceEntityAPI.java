package com.ose.tasks.api.wbs;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.wbs.ParentsPutDTO;
import com.ose.tasks.dto.wbs.PipePieceEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.PipePieceEntryInsertDTO;
import com.ose.tasks.dto.wbs.PipePieceEntryUpdateDTO;
import com.ose.tasks.entity.wbs.entity.PipePieceEntityBase;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface PipePieceEntityAPI {

    /**
     * 查询管段实体。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 条件DTO
     * @param pageDTO     分页用DTO
     * @return 管段实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<? extends PipePieceEntityBase> searchPipeEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PipePieceEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得管段实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @return 管段实体详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<? extends PipePieceEntityBase> getPipeEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 删除管段实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deletePipeEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    );

    /**
     * 插入管段实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addPipePieceEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody PipePieceEntryInsertDTO pipePieceEntryInsertDTO
    );

    /**
     * 更新管段实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updatePipePieceEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody PipePieceEntryUpdateDTO pipePieceEntryUpdateDTO
    );

    /**
     * 更新所属单管。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}/spool",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody moveToSpool(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId,
        @RequestParam("version") long version,
        @RequestBody ParentsPutDTO parentsPutDTO
    );

    /**
     * 取得管段图纸信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}/drawing-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getDrawingInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 取得管段材料信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  实体ID
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/{entityId}/material-info",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody getMaterialInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 按条件下载管段实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-piece-entities/download"
    )
    void downloadPipePieceEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PipePieceEntryCriteriaDTO criteriaDTO
    ) throws IOException;

}
