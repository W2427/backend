package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.dto.jpql.VersionQLDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.bpm.TaskHierarchyDTO;
import com.ose.tasks.dto.wbs.*;
import com.ose.tasks.entity.process.ProcessEntity;
import com.ose.tasks.entity.wbs.entry.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 计划管理接口。
 */
public interface PlanAPI {

    /**
     * 导入计划结构。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-plan",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importPlan(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody HierarchyNodeImportDTO nodeImportDTO
    );

    /**
     * 查询 WBS。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WBSHierarchyDTO> getWBS(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WBSEntryQueryDTO queryDTO
    );

    /**
     * 查询 WBS。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/wbs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WBSHierarchyDTO> getWBS(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        WBSEntryQueryDTO queryDTO
    );


    /**
     * 根据搜索条件，取得扁平的WBS计划列表，包括3级和4级计划
     */
    @Operation(description = "取得扁平的WBS计划列表，包括3级和4级计划")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-plain",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntryPlain> getPlainWBS(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    );


    /**
     * 根据搜索条件，取得扁平的WBS汇总的计划列表，包括3级和4级计划的汇总情况
     */
    @Operation(description = "取得扁平的WBS计划汇总列表，包括3级和4级计划汇总情况")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-plain-group",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntryGroupPlainDTO> getPlainGroupWBS(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    );

    /**
     * 取得 WBS 三级计划跟四级计划施工组织及用户信息。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/team-user-delegate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WBSEntryDelegateDTO> getWBSEntryDelegateAndTeam(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long wbsEntryId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/entity-processes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntryWithRelations> listEntityProcesses(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WBSEntryQueryDTO queryDTO,
        PageDTO pageDTO
    );

    /**
     * 查询四级计划。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/entity-processes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntryWithRelations> listEntityProcesses(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        WBSEntryQueryDTO queryDTO,
        PageDTO pageDTO
    );

    /**
     * 添加实体资源。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/works/{workId}/entities",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WBSEntry> addEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("workId") Long workId,
        @RequestBody WBSEntityPostDTO wbsEntityPostDTO
    );

    /**
     * 更新实体工序计划条目。
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-entity-processes/{entityEntryId}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WBSEntry> updateEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityEntryId") Long entityEntryId,
        @RequestParam("version") long version,
        @RequestBody WBSEntityPatchDTO wbsEntityPatchDTO
    );

    /**
     * 更新工作计划条目。
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-works/{workEntryId}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WBSEntry> updateEntry(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("workEntryId") Long workEntryId,
        @RequestParam("version") long version,
        @RequestBody WBSEntryPatchDTO wbsEntryPatchDTO
    );

    /**
     * 删除实体资源。
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<VersionQLDTO> deleteEntry(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        @RequestParam("version") long version
    );

    /**
     * 自动绑定实体资源。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody bindEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long workId,
        @RequestParam(name = "moduleUpdate", required = false, defaultValue = "true")
        @Parameter(description = "更新所在模块计划的前置任务还是整个项目计划的前置任务，true=更新所在模块")
            boolean moduleUpdate
    );

    /**
     * 取得计划条目详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WBSEntry> getWBSEntry(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId
    );

    /**
     * 取得计划条目前置任务。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/predecessors",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntryPredecessorDetail> predecessors(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        PageDTO pageDTO
    );

    /**
     * 取得计划条目后置任务。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/successors",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntrySuccessorDetail> successors(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        PageDTO pageDTO
    );

    /**
     * 取得计划条目的所有上级。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/parents",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntryPlain> getParents(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        @RequestParam(name = "appendSelf", required = false, defaultValue = "false")
        @Parameter(description = "是否返回条目自身信息")
            boolean appendSelf
    );

    /**
     * 设置 WBS 条目的权重。
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/score",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setEntryScore(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        @RequestParam("version") long version,
        @RequestBody WBSScorePutDTO scorePutDTO
    );

    /**
     * 将 WBS 条目标记为挂起。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/suspend",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setAsSuspended(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId
    );

    /**
     * 恢复挂起的 WBS 条目。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/resume",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody resume(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId
    );

    /**
     * 将 WBS 条目标记为已完成。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/finish",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setAsFinished(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        @RequestBody WBSEntryFinishDTO finishDTO
    );

    /**
     * 指派工作组。
     */
    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/team",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody dispatchTeam(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        @RequestBody WBSTeamPutDTO teamPutDTO
    );

    /**
     * 任务分配人。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param wbsEntryId    WBS条目ID
     * @param wbsUserPutDTO 分配信息
     */
    @PostMapping(
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{wbsEntryId}/user",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody dispatchUser(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("wbsEntryId") Long wbsEntryId,
        @RequestBody WBSUserPutDTO wbsUserPutDTO
    );

    /**
     * 启动计划任务。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/modules/{moduleId}/start-wbs",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody startWBS(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("moduleId") Long moduleId
    );

    /**
     * 启动计划任务。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{entryId}/start",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody startWBSEntry(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entryId") Long entryId
    );

    /**
     * 批量启动任务包计划任务。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/start-taskPackage",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody startAllWBSEntry(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody WBSEntriesDTO wBSEntryDTO
    );

    /**
     * 查询已分配的计划任务。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{entryId}/tasks",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WBSEntryActivityInstance> searchTasks(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entryId") Long entryId,
        PageDTO pageDTO
    );

    /**
     * 导出三级计划。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/export-wbs-works",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void exportWBSWorks(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("rootId") Long rootId
    ) throws IOException;

    /**
     * 查询实体。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/stages/{stageName}/processes/{processName}/entity-types/{entityType}/entities",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ProcessEntity> entities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("stageName") String stageName,
        @PathVariable("processName") String processName,
        @PathVariable("entityType") String entityType,
        PageDTO pageDTO
    );

    /**
     * 导出四级计划。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/wbs/{workId}/export-wbs-entity-processes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void exportWBSEntityProcesses(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("workId") Long workId
    ) throws IOException;

    /**
     * 重新生成四级计划前后置关系。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/regenerate-wbs-relations",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody regenerateWBSRelations(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 设置计划手动进度
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param wbsEntryId
     * @param manualProgress
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/manual-progress/{wbsEntryId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setWBSEntryManualProgress(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestParam @Parameter(description = "手动进度百分比") String manualProgress
    );

    /**
     * 取得扁平WBS计划中的材料汇总信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-plain-material",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<MaterialMatchSummaryDTO> getWbsPlainMaterial(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @Parameter(description = "扁平计划查询条件") WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    );


    /**
     * 取得扁平WBS计划中的ISO材料信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-plain-iso-material",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<MaterialISOMatchSummaryDTO> getWbsPlainIsoMaterial(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @Parameter(description = "扁平计划查询条件") WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    );

    /**
     * 取得扁平WBS计划中的图纸信息
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param wbsEntryPlainQueryDTO
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-plain-dwg",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WBSEntryDwgSummaryDTO> getWbsPlainDwg(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @Parameter(description = "扁平计划查询条件") WBSEntryPlainQueryDTO wbsEntryPlainQueryDTO
    );


    /**
     * 取得扁平wbs_entry_execution_history中的未生成四级计划的实体，并生成或删除四级计划
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/wbs-generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody generateWbsFromEntity(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId
    );


    /**
     * 返回计划管理页面工序阶段和工序
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/stage-processes"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<TaskHierarchyDTO> getStageProcesses(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam(value = "stageId", required = false) Long stageId
    );

    /**
     * 检查实体是否可以进行实体变更
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/plain/check/{entityId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<PlainCheckDTO> checkChange(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entityId") Long entityId
    );

    /**
     * 启动工序下的四级计划及任务。
     *
     * @return 工序
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/process/plain"
    )
    @ResponseStatus(OK)
    JsonResponseBody startPlan(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @RequestBody WBSEntryPlainStartDTO wBSEntryPlainStartDTO
    );

    /**
     * 启动实体下的四级计划及任务。
     *
     * @return 工序
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/weld/plain"
    )
    @ResponseStatus(OK)
    JsonResponseBody startWeldPlan(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @RequestBody WBSEntryWeldPlainStartDTO wBSEntryWeldPlainStartDTO
    );


    /**
     * 自动对增加的实体 生成四级计划 及关系。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/added-wbs/{wbsEntryId}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody bindAddedEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestBody WBSEntryDTO wbsEntryDTO
    );

    /**
     * 自动对增加的实体 生成四级计划 及关系。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/added-taskPackage-wbs/{taskPackageId}",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody bindAddedTaskPackageEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "任务包 ID") Long taskPackageId,
        @RequestBody WBSEntryDTO wbsEntryDTO
    );


    /**
     * 删除 增加的实体 生成的四级计划 及关系。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/deleted-added-wbs/{originalBatchTaskId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteBindedEntities(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "原来生成计划的批处理 ID") Long originalBatchTaskId
    );

    /**
     * 更新当前 计划再REDIS 中的状态值 COLD HOT
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/reset-plan-queue"
    )
    @ResponseStatus(OK)
    JsonResponseBody resetPlanQueue(@PathVariable @Parameter(description = "组织 ID") Long orgId,
                                           @PathVariable @Parameter(description = "项目 ID") Long projectId);

    @Operation(description = "增加生成四级计划的队列")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/plan-queue/{wbsEntryId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody addPlanQueue(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestBody WBSEntryDTO wbsEntryDTO
    );

    @Operation(description = "增加生成增量四级计划的队列")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/added-plan-queue/{wbsEntryId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody addAddedPlanQueue(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "WBS 条目 ID") Long wbsEntryId,
        @RequestBody WBSEntryDTO wbsEntryDTO
    );

    @Operation(description = "增加生成四级计划的队列")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/plan-queue",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<PlanQueueDTO> getPlanQueue(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PageDTO pageDTO
    );
}
