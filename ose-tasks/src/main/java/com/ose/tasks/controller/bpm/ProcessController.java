package com.ose.tasks.controller.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.ose.exception.NotFoundError;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.ProcessAPI;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.vo.RelationReturnEnum;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "工序接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/processes")
public class ProcessController extends BaseController implements ProcessAPI {

    /**
     * 工序服务
     */
    private final ProcessInterface processService;

    /**
     * 构造方法
     *
     * @param processService 工序服务
     */
    @Autowired
    public ProcessController(ProcessInterface processService) {
        this.processService = processService;
    }

    /**
     * 获取工序列表
     *
     * @return 工序列表
     */
    @Override
    @Operation(
        summary = "查询工序",
        description = "获取工序列表。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ProcessResponseDTO> getList(
        ProcessCriteriaDTO criteriaDTO,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {

        Page<BpmProcess> process = processService.getList(criteriaDTO, projectId, orgId);
        Function<BpmProcess, ProcessResponseDTO> converter = ProcessResponseDTO::new;
        Page<ProcessResponseDTO> result = process.map(converter);

        for (ProcessResponseDTO dto : result.getContent()) {
            dto.setEntitySubType(processService.getEntitySubTypeByProcessId(dto.getId(), projectId, orgId));
            dto.setActivityModel(processService.findActivityModel(orgId, projectId, dto.getId()));
        }

        return new JsonListResponseBody<>(
            getContext(),
            result
        );
    }


    @Override
    @Operation(
        summary = "创建工序",
        description = "根据工序信息，创建工序。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<ProcessResponseDTO> create(
        @RequestBody @Parameter(description = "工序信息") ProcessDTO processDTO,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {


        ProcessResponseDTO respDTO = new ProcessResponseDTO(processService.create(processDTO, projectId, orgId));
        List<BpmEntitySubType> entityCategories = processService.getEntitySubTypeByProcessId(respDTO.getId(), projectId, orgId);
        respDTO.setEntitySubType(entityCategories);
        return new JsonObjectResponseBody<>(
            getContext(),
            respDTO);
    }


    @Override
    @Operation(
        summary = "编辑工序",
        description = "编辑指定的工序"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ProcessResponseDTO> edit(
        @RequestBody @Parameter(description = "工序信息") ProcessDTO processDTO,
        @PathVariable @Parameter(description = "工序id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {

        ProcessResponseDTO respDTO = new ProcessResponseDTO(processService.modify(id, processDTO, projectId, orgId));
        List<BpmEntitySubType> entityCategories = processService.getEntitySubTypeByProcessId(respDTO.getId(), projectId, orgId);
        respDTO.setEntitySubType(entityCategories);
        return new JsonObjectResponseBody<>(
            getContext(),
            respDTO);
    }


    @Override
    @Operation(
        summary = "删除工序",
        description = "删除指定的工序"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteStep(
        @PathVariable @Parameter(description = "工序id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        List<BpmEntityTypeProcessRelation> relations = processService.getRelationByEntitySubTypeId(id);
        if (relations != null && relations.size() > 0) {
            return new JsonResponseBody().setError(new ValidationError("relation"));
        }
        processService.delete(id, projectId, orgId);
        return new JsonResponseBody();
    }


    @Override
    @Operation(
        summary = "添加实体类型",
        description = "为指定的工序添加实体类型。"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/entity-sub-types/{entityCategotyId}"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody addEntity(
        @PathVariable @Parameter(description = "工序id") Long id,
        @PathVariable @Parameter(description = "实体id") Long entityCategotyId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        RelationReturnEnum r = processService.addEntitySubType(id, entityCategotyId, projectId, orgId);
        if (r.equals(RelationReturnEnum.SAVE_SUCCESS)) {
            return new JsonResponseBody();
        } else {
            return new JsonResponseBody().setError(new ValidationError(r.getMessage()));
        }
    }


    @Override
    @Operation(
        summary = "删除实体类型",
        description = "删除指定的工序下的指定实体类型"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}/entity-sub-types/{entityCategotyId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteEntity(
        @PathVariable @Parameter(description = "工序id") Long id,
        @PathVariable @Parameter(description = "实体id") Long entityCategotyId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        RelationReturnEnum r = processService.deleteEntitySubType(id, entityCategotyId, projectId, orgId);
        ;
        if (r.equals(RelationReturnEnum.DELETE_SUCCESS)) {
            return new JsonResponseBody();
        } else {
            return new JsonResponseBody().setError(new ValidationError(r.getMessage()));
        }
    }

    @Override
    @Operation(
        summary = "获取工序详细信息",
        description = "根据ID查询工序详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<BpmProcess> get(
        @PathVariable @Parameter(description = "工序id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonObjectResponseBody<>(getContext(), processService.get(id, projectId, orgId));
    }


    @Override
    @Operation(
        summary = "获取全部实体类型",
        description = "获取全部实体类型"
    )
    @RequestMapping(
        method = GET,
        value = "/entity-sub-types",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BpmEntitySubType> getStep(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonListResponseBody<>(
            getContext(),
            processService.getEntitySubTypeList(projectId, orgId)
        );
    }


    @Override
    @Operation(
        summary = "批量排序",
        description = "批量排序"
    )
    @RequestMapping(
        method = POST,
        value = "/sort",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody batchSort(
        @RequestBody @Parameter(description = "排序信息") List<SortDTO> sortDTOs,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        processService.sort(sortDTOs, projectId, orgId);
        return new JsonResponseBody();
    }

    /**
     * 批量添加实体类型
     *
     * @param id    工序id
     * @param orgId 实体id
     * @return 工序信息
     */
    @Operation(
        summary = "批量添加实体类型",
        description = "为工序批量添加实体类型"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/entity-sub-types"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BatchAddResponseDTO> addEntityBatch(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "工序id") Long id,
        @RequestBody BatchAddRelationDTO dto) {
        List<BatchAddResponseDTO> result = new ArrayList<>();
        for (Long entityid : dto.getIds()) {
            BatchAddResponseDTO rdto = new BatchAddResponseDTO();
            rdto.setId(entityid);
            RelationReturnEnum r = processService.addEntitySubType(id, entityid, projectId, orgId);
            if (r.equals(RelationReturnEnum.SAVE_SUCCESS)) {
                rdto.setResult(true);
            } else {
                rdto.setResult(false);
            }
            rdto.setMessage(r.getMessage());
            result.add(rdto);
        }
        return new JsonListResponseBody<>(result);
    }

    @Override
    @Operation(description = "获取工序对应的实体类型列表")
    @WithPrivilege
    public JsonListResponseBody<BpmEntitySubType> getEntitySubTypeList(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "工序 ID") Long id,
        ProcessEntitySubTypeCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            processService
                .getEntitySubTypeList(id, projectId, orgId, criteriaDTO)
        );
    }

    @Override
    @Operation(description = "获取工序对应的实体类型列表")
    @WithPrivilege
    public JsonListResponseBody<BpmEntitySubType> getEntitySubTypeList(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "工序阶段名称") String stageName,
        @PathVariable @Parameter(description = "工序名称") String processName,
        ProcessEntitySubTypeCriteriaDTO criteriaDTO
    ) {

        BpmProcess process = processService
            .get(orgId, projectId, stageName, processName);

        if (process == null) {
            throw new NotFoundError();
        }

        return new JsonListResponseBody<>(
            processService
                .getEntitySubTypeList(process.getId(), projectId, orgId, criteriaDTO)
        );
    }

    /**
     * 获取工序对应的工序阶段列表
     */
    @Operation(
        summary = "获取工序对应的工序阶段列表",
        description = "获取工序对应的工序阶段列表"
    )
    @RequestMapping(
        method = GET,
        value = "/process-stages"
    )
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonListResponseBody<BpmProcessStage> getProcessStageList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId) {
        return new JsonListResponseBody<>(processService.getProcessStageList(projectId, orgId));
    }

    /**
     * 获取工序对应的实体类型分类列表
     */
    @Operation(
        summary = "获取工序对应的实体类型分类列表",
        description = "获取工序对应的实体类型分类列表"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}/entity-types"
    )
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonListResponseBody<BpmEntityType> getEntityTypeList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "工序id") Long id) {
        return new JsonListResponseBody<>(processService.getEntityTypeList(id, projectId, orgId));
    }

    /**
     * 获取工序对应的工序阶段列表
     */
    @Operation(
        summary = "获取工序对应的工序分类列表",
        description = "获取工序对应的工序分类列表"
    )
    @RequestMapping(
        method = GET,
        value = "/process-categories"
    )
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonListResponseBody<BpmProcessCategory> getProcessCategoryList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId) {
        return new JsonListResponseBody<>(processService.getProcessCategoryList(projectId, orgId));
    }

    @Operation(
        summary = "获取工序权限列表",
        description = "获取工序权限列表"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}/privileges"
    )
    @SetUserInfo
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonListResponseBody<TaskPrivilegeDTO> getProcessPrivileges(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "工序id") Long id) {
        return new JsonListResponseBody<>(processService.getProcessPrivileges(orgId, projectId, id));
    }

    @Override
    @Operation(
        summary = "设置工序权限执行人",
        description = "设置工序权限执行人"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/privileges"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody setProcessPrivileges(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "工序id") Long id,
        @RequestBody TaskPrivilegeDTO DTO) {
        processService.setProcessPrivileges(orgId, projectId, id, DTO);
        return new JsonResponseBody();
    }

    /**
     * 获取全部 工序阶段-工序 列表
     *
     * @return 工序阶段-工序 列表
     */
    @RequestMapping(
        method = GET,
        value = "/processKeys"
    )
    @Operation(
        summary = "获取工序权限列表",
        description = "获取工序权限列表"
    )
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonListResponseBody<ProcessKeyDTO> getProcessKeys(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    ) {
        return new JsonListResponseBody<>(processService.getProcessKeys(orgId, projectId));

    }

    @Override
    @Operation(
        summary = "设置版本规则",
        description = "设置版本规则"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/version-rule"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody setProcessVersionRule(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "工序id") Long id,
        @RequestBody BpmProcessVersionRuleDTO DTO) {
        processService.setProcessVersionRule(orgId, projectId, id, DTO);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取工序版本规则详细信息",
        description = "根据ID查询工序版本规则详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}/version-rule",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<BpmProcessVersionRule> getProcessVersionRule(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "工序id") Long id) {
        return new JsonObjectResponseBody<>(getContext(), processService.getVersionRule(orgId, projectId, id));
    }

    @RequestMapping(
        method = GET,
        value = "/hierarchy"
    )
    @Operation(
        summary = "获取工序权限列表",
        description = "获取工序权限列表"
    )
    @ResponseStatus(OK)
    @WithPrivilege
    @Override
    public JsonListResponseBody<ProcessHierarchyDTO> getProcessHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    ) {
        return new JsonListResponseBody<>(processService.getHierarchy(orgId, projectId));

    }

}
