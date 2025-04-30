package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.entity.ProjectNode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 实体管理接口。
 */
public interface WBSEntityAPI {

    /**
     * 导入实体。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-entities/{type}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("type") String type,
        @RequestParam("version") long version,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    );

    /**
     * 取得未挂载到层级结构的实体。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/unmounted-entities",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ProjectNode> unmountedEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam(name = "entityType", required = false) String entityType,
        PageDTO pageDTO
    );

    /**
     * 将增加的实体插入到 待更新计划表中 wbs_entry_execution_history
     *
     * @param operatorId
     * @param projectId  项目ID
     * @param entityId
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/entity-execution-history",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Operation(description = "插入新增实体到计划待更新表")
    @ResponseStatus(OK)
    JsonResponseBody addExecutionHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("operatorId") Long operatorId,
        @RequestParam("entityId") Long entityId
    );

}
