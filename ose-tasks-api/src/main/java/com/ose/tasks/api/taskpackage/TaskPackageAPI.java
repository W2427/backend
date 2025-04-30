package com.ose.tasks.api.taskpackage;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.HierarchyDTO;
import com.ose.tasks.dto.HierarchyNodeDTO;
import com.ose.tasks.dto.TaskPackageEntityImportDTO;
import com.ose.tasks.dto.process.EntityProcessDTO;
import com.ose.tasks.dto.taskpackage.*;
import com.ose.tasks.dto.wbs.WBSEntryCriteriaDTO;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.taskpackage.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 任务包管理接口。
 */
public interface TaskPackageAPI {

    /* 任务包相关接口 */

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackageBasic> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody TaskPackageCreateDTO taskPackageDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackagePercent> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TaskPackageCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/modify-name",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageAUthCriteriaDTO> searchModifyName(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackage> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId
    );

    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        @RequestParam("version") Long version,
        @RequestBody TaskPackageUpdateDTO taskPackageDTO
    );

    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/teams",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setTeams(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        @RequestBody TaskPackageTeamDTO teamDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/teams",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageProcessTeamDTO> getTeams(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId
    );

    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/delegates",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delegate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        @RequestBody TaskPackageDelegateDTO delegateDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/delegates",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageProcessDelegateDTO> delegates(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId
    );

    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        @RequestParam("version") Long version
    );

    /* 任务包-实体关系相关接口 */


    /**
     * 取得添加任务包对应的层级结构。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-hierarchy",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<HierarchyDTO<HierarchyNodeDTO>> getHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("depth") int depth,
        @RequestParam(name = "viewType", required = false) String viewType,
        @RequestParam(name = "needEntity", required = false, defaultValue = "true") Boolean needEntity,
        @RequestParam("entityType") String entityType
    );

    @Operation(description = "导入任务包关联实体接口")
    @PostMapping(
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/entities-import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importTaskPackageEntities(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "任务包ID") Long taskPackageId,
        @RequestBody TaskPackageEntityImportDTO taskPackageEntityImportDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageEntityRelation> addEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        @RequestBody TaskPackageEntityRelationCreateDTO relationDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageEntityRelation> entities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        TaskPackageEntityRelationSearchDTO taskPackageEntityRelationSearchDTO
    );

    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/entities/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteEntity(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        @PathVariable("entityId") String entityId
    );

    /* 任务包-图纸关系相关接口 */

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/drawings",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageDrawingRelation> addDrawings(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        @RequestBody TaskPackageDrawingRelationCreateDTO relationDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/drawings",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageDrawingRelation> drawings(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        TaskPackageDrawingRelationCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/sub-drawings",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageDrawingDTO> subDrawings(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/all-drawings",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackageDrawingIdDTO> allDrawings(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId
    );

    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/drawings/{relationId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteDrawing(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        @PathVariable("relationId") Long relationId
    );

    /* 计划条目相关接口 */

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/wbs-entries",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackageDTO> wbsEntries(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        WBSEntryCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/wbs-entries/sectors",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackageDTO> wbsEntriesSectors(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId
    );

    /* 任务包-工序关系相关接口 */

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/{taskPackageId}/processes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<EntityProcessDTO> processes(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskPackageId") Long taskPackageId,
        WBSEntryCriteriaDTO criteriaDTO
    );


    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-packages/project-node-entities",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ProjectNode> projectNodeEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TaskPackageProjectNodeEntitySearchDTO taskPackageProjectNodeEntitySearchDTO
    );

}
