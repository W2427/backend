package com.ose.tasks.api.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/processes")
public interface ProcessAPI {

    /**
     * 获取工序列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<ProcessResponseDTO> getList(
        ProcessCriteriaDTO criteriaDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 创建工序
     *
     * @param processDTO 工序信息
     * @return 工序信息
     */
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ProcessResponseDTO> create(
        @RequestBody ProcessDTO processDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 编辑工序
     *
     * @param processDTO 工序信息
     * @param id         工序id
     * @return 工序信息
     */
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    JsonObjectResponseBody<ProcessResponseDTO> edit(
        @RequestBody ProcessDTO processDTO,
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 删除工序
     *
     * @param id 工序id
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteStep(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取工序对应的实体类型列表。
     */
    @RequestMapping(
        method = GET,
        value = "/{id}/entity-sub-types"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntitySubType> getEntitySubTypeList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        ProcessEntitySubTypeCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "/{stageName}/{processName}/entity-sub-types"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntitySubType> getEntitySubTypeList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("stageName") String stageName,
        @PathVariable("processName") String processName,
        ProcessEntitySubTypeCriteriaDTO criteriaDTO
    );

    /**
     * 添加实体类型
     *
     * @param id       工序id
     * @param entityId 实体id
     * @return 工序信息
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/entity-sub-types/{entityId}"
    )
    JsonResponseBody addEntity(
        @PathVariable("id") Long id,
        @PathVariable("entityId") Long entityId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );


    /**
     * 删除实体类型
     *
     * @param id       工序id
     * @param entityId 实体id
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}/entity-sub-types/{entityId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteEntity(
        @PathVariable("id") Long id,
        @PathVariable("entityId") Long entityId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取工序详细信息
     *
     * @return 工序
     */
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmProcess> get(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取全部实体类型
     *
     * @return 工序
     */
    @RequestMapping(
        method = GET,
        value = "/entity-sub-types"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntitySubType> getStep(
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 工序、分类批量排序
     *
     * @return 工序
     */
    @RequestMapping(
        method = POST,
        value = "/processes/sort"
    )
    @ResponseStatus(OK)
    JsonResponseBody batchSort(
        @RequestBody List<SortDTO> sortDTOs,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 批量添加实体类型
     *
     * @param id 工序id
     * @return 工序信息
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/entity-sub-types"
    )
    JsonListResponseBody<BatchAddResponseDTO> addEntityBatch(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody BatchAddRelationDTO dto
    );

    /**
     * 获取全部工序对应的工序阶段
     *
     * @return 工序阶段列表
     */
    @RequestMapping(
        method = GET,
        value = "/process-stages"
    )
    JsonListResponseBody<BpmProcessStage> getProcessStageList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 获取工序对应的实体类型列表
     *
     * @return 工序对应的实体类型列表
     */
    @RequestMapping(
        method = GET,
        value = "/{id}/entity-types"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntityType> getEntityTypeList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取全部工序对应的工序阶段
     *
     * @return 工序阶段列表
     */
    @RequestMapping(
        method = GET,
        value = "/process-categories"
    )
    JsonListResponseBody<BpmProcessCategory> getProcessCategoryList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 获取全部工序权限
     *
     * @return 工序阶段列表
     */
    @RequestMapping(
        method = GET,
        value = "/{id}/privileges"
    )
    JsonListResponseBody<TaskPrivilegeDTO> getProcessPrivileges(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    @RequestMapping(
        method = POST,
        value = "/{id}/privileges"
    )
    @ResponseStatus(OK)
    JsonResponseBody setProcessPrivileges(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody TaskPrivilegeDTO DTO
    );

    /**
     * 获取全部 工序阶段-工序 列表
     *
     * @return 工序阶段-工序 列表
     */
    @RequestMapping(
        method = GET,
        value = "/processKeys"
    )
    JsonListResponseBody<ProcessKeyDTO> getProcessKeys(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    @RequestMapping(
        method = POST,
        value = "/{id}/version-rule"
    )
    @ResponseStatus(OK)
    JsonResponseBody setProcessVersionRule(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody BpmProcessVersionRuleDTO DTO
    );

    @RequestMapping(
        method = GET,
        value = "/{id}/version-rule"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmProcessVersionRule> getProcessVersionRule(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    @RequestMapping(
        method = GET,
        value = "/hierarchy"
    )
    JsonListResponseBody<ProcessHierarchyDTO> getProcessHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );
}
