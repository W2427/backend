package com.ose.tasks.api.taskpackage;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.taskpackage.*;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.taskpackage.TaskPackageCategory;
import com.ose.tasks.entity.taskpackage.TaskPackageCategoryProcessRelation;
import com.ose.tasks.entity.taskpackage.TaskPackageCategoryProcessRelationBasic;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 任务包类型接口。
 */
public interface TaskPackageCategoryAPI {

    /* 任务包类型相关接口 */

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackageCategory> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody TaskPackageCategoryCreateDTO taskPackageCategoryDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageCategory> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TaskPackageCategoryCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/{categoryId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackageCategory> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("categoryId") Long categoryId
    );

    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/{categoryId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("categoryId") Long categoryId,
        @RequestParam("version") Long version,
        @RequestBody TaskPackageCategoryUpdateDTO taskPackageCategoryDTO
    );

    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/{categoryId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("categoryId") Long categoryId,
        @RequestParam("version") Long version
    );

    /* 任务包类型-工序关系相关接口 */

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/{categoryId}/processes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackageCategoryProcessRelationBasic> addProcess(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("categoryId") Long categoryId,
        @RequestBody TaskPackageCategoryProcessRelationCreateDTO relationDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/{categoryId}/processes",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<TaskPackageCategoryProcessRelation> processes(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("categoryId") Long categoryId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/{categoryId}/processes/{processId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteProcesses(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("categoryId") Long categoryId,
        @PathVariable("processId") Long processId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/{categoryId}/entity-ids",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackageCategoryEntityDTO> entityIDs(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("categoryId") Long categoryId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/{categoryId}/entity-types",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BpmEntitySubType> entityTypes(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("categoryId") Long categoryId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/task-package-categories/type-enum",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<TaskPackageTypeEnumDTO> getTypeEnums(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );
}
