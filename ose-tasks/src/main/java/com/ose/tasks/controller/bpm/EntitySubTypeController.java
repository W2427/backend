package com.ose.tasks.controller.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.DrawingCoordinateDTO;
import com.ose.tasks.dto.drawing.DrawingSignatureCoordinateDTO;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.entity.drawing.DrawingSignatureCoordinate;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.EntitySubTypeAPI;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityTypeProcessRelation;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmProcessStage;
import com.ose.tasks.vo.RelationReturnEnum;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "实体类型接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/entity-sub-types")
public class EntitySubTypeController extends BaseController implements EntitySubTypeAPI {

    /**
     * 实体类型服务
     */
    private final EntitySubTypeInterface entityCategoryService;

    /**
     * 构造方法
     *
     * @param entityCategoryService 实体类型服务
     */
    @Autowired
    public EntitySubTypeController(EntitySubTypeInterface entityCategoryService) {
        this.entityCategoryService = entityCategoryService;
    }

    /**
     * @param entityDTO 实体DTO
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return
     */
    @Override
    @Operation(
        summary = "创建实体类型",
        description = "根据实体类型信息，创建实体类型。"
    )
    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<BpmEntitySubType> create(@RequestBody @Parameter(description = "实体信息") EntitySubTypeDTO entityDTO,
                                                           @PathVariable @Parameter(description = "项目id") Long projectId,
                                                           @PathVariable @Parameter(description = "orgId") Long orgId) {

        BpmEntitySubType entitySubType = entityCategoryService.findByOrgIdAndProjectIdAndNameEn(orgId, projectId, entityDTO.getNameEn());
        if (entitySubType != null) {
            throw new ValidationError("The english name has been exist");
        }

        return new JsonObjectResponseBody<>(
            getContext(),
            entityCategoryService.create(entityDTO, projectId, orgId)
        );
    }


    @Override
    @Operation(
        summary = "查询实体类型",
        description = "获取实体类型列表。"
    )
    @RequestMapping(method = GET)
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BpmEntitySubType> getList(HttpServletRequest request, HttpServletResponse response,
                                                          EntitySubTypeCriteriaDTO page,
                                                          @PathVariable @Parameter(description = "项目id") Long projectId,
                                                          @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonListResponseBody<>(
            getContext(),
            entityCategoryService.getList(page, projectId, orgId)
        );
    }



    @Override
    @Operation(
        summary = "删除实体类型",
        description = "删除指定的实体类型"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(HttpServletRequest request, HttpServletResponse response,
                                   @PathVariable @Parameter(description = "实体id") Long id,
                                   @PathVariable @Parameter(description = "项目id") Long projectId,
                                   @PathVariable @Parameter(description = "orgId") Long orgId) {
        List<BpmEntityTypeProcessRelation> relations = entityCategoryService.getRelationByEntitySubTypeId(id);
        if (relations != null) {
            return new JsonResponseBody().setError(new ValidationError("relation"));
        }
        entityCategoryService.delete(id, projectId, orgId);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "编辑实体类型",
        description = "编辑指定的实体类型"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<BpmEntitySubType> modify(HttpServletRequest request, HttpServletResponse response,
                                                           @PathVariable @Parameter(description = "实体id") Long id,
                                                           @RequestBody @Parameter(description = "实体信息") EntitySubTypeDTO entityDTO,
                                                           @PathVariable @Parameter(description = "项目id") Long projectId,
                                                           @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonObjectResponseBody<>(
            getContext(),
            entityCategoryService.modify(id, entityDTO, projectId, orgId)
        );
    }


    @Override
    @Operation(
        summary = "获取工序列表",
        description = "获取指定实体类型对应的工序列表"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}/processes",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BpmEntityTypeProcessRelation> getProcessList(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           @PathVariable @Parameter(description = "实体id") Long id,
                                                           @PathVariable @Parameter(description = "项目id") Long projectId,
                                                           @PathVariable @Parameter(description = "orgId") Long orgId,
                                                           EntitySubTypeProcessCriteriaDTO criteriaDTO) {
        return new JsonListResponseBody<>(entityCategoryService.getProcessList(id, projectId, orgId, criteriaDTO));
    }


    @Override
    @Operation(
        summary = "添加工序",
        description = "为指定实体类型添加指定工序"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/processes/{processId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody addProcess(HttpServletRequest request, HttpServletResponse response,
                                       @PathVariable @Parameter(description = "实体id") Long id,
                                       @PathVariable @Parameter(description = "工序id") Long processId,
                                       @PathVariable @Parameter(description = "项目id") Long projectId,
                                       @PathVariable @Parameter(description = "orgId") Long orgId) {
        RelationReturnEnum r = entityCategoryService.addProcess(id, processId, projectId, orgId);
        if (r.equals(RelationReturnEnum.SAVE_SUCCESS)) {
            return new JsonResponseBody();
        } else {
            return new JsonResponseBody().setError(new ValidationError(r.getMessage()));
        }
    }


    @Override
    @Operation(
        summary = "删除工序",
        description = "删除指定实体类型对应的指定工序"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}/processes/{processId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteProcess(HttpServletRequest request, HttpServletResponse response,
                                          @PathVariable @Parameter(description = "实体id") Long id,
                                          @PathVariable @Parameter(description = "工序id") Long processId,
                                          @PathVariable @Parameter(description = "项目id") Long projectId,
                                          @PathVariable @Parameter(description = "orgId") Long orgId) {
        RelationReturnEnum r = entityCategoryService.deleteProcess(id, processId, projectId, orgId);
        if (r.equals(RelationReturnEnum.DELETE_SUCCESS)) {
            return new JsonResponseBody();
        } else {
            return new JsonResponseBody().setError(new ValidationError(r.getMessage()));
        }
    }

    @Override
    @Operation(
        summary = "获取实体类型详细信息",
        description = "查询实体类型详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<EntitySubTypeResponseDTO> get(HttpServletRequest request, HttpServletResponse response,
                                                                 @PathVariable @Parameter(description = "实体id") Long id,
                                                                 @PathVariable @Parameter(description = "项目id") Long projectId,
                                                                 @PathVariable @Parameter(description = "orgId") Long orgId) {

        EntitySubTypeResponseDTO dto = new EntitySubTypeResponseDTO(entityCategoryService.getEntity(id, projectId, orgId));

        /*Page<BpmProcess> processes = entityCategoryService.getProcessList(dto.getId(), projectId, orgId);
        dto.setProcess(processes.getContent());*/
        return new JsonObjectResponseBody<>(
            getContext(),
            dto
        );
    }


    @Override
    @Operation(
        summary = "获取全部工序",
        description = "获取全部工序"
    )
    @RequestMapping(
        method = GET,
        value = "/processes",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BpmProcess> getProcessList(HttpServletRequest request,
                                                           HttpServletResponse response,
                                                           @PathVariable @Parameter(description = "项目id") Long projectId,
                                                           @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonListResponseBody<>(entityCategoryService.getProcessList(projectId, orgId));
    }


    /**
     * 批量添加工序
     *
     * @param id 实体类型id
     * @return 对应的工序列表
     */
    @Operation(
        summary = "批量添加工序",
        description = "为实体批量添加工序"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/processes"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BatchAddResponseDTO> addProcessBatch(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "实体类型id") Long id,
        @RequestBody BatchAddRelationDTO dto) {
        List<BatchAddResponseDTO> result = new ArrayList<>();
        for (Long processid : dto.getIds()) {
            BatchAddResponseDTO rdto = new BatchAddResponseDTO();
            rdto.setId(processid);
            RelationReturnEnum r = entityCategoryService.addProcess(id, processid, projectId, orgId);
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

    /**
     * 获取实体对应的工序阶段列表
     */
    @Operation(
        summary = "获取实体对应的工序阶段列表",
        description = "获取实体对应的工序阶段列表"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}/process-stages"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmProcessStage> getProcessStageList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "实体类型id") Long id) {
        return new JsonListResponseBody<>(entityCategoryService.getProcessStageList(orgId, projectId, id));
    }

    /**
     * 批量排序
     */
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
        entityCategoryService.sort(sortDTOs, projectId, orgId);
        return new JsonResponseBody();
    }

    @Override
    public JsonListResponseBody<BpmEntityType> getEntityCategoryTypeList(HttpServletRequest request, HttpServletResponse response, Long projectId, Long orgId, CategoryTypeTag type) {
        return null;
    }


    @Override
    @Operation(
        summary = "获取实体类型分类列表",
        description = "获取全部实体类型对应的分类列表"
    )
    @RequestMapping(
        method = GET,
        value = "{type}/types",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BpmEntityType> getEntityTypeList(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type) {
        return new JsonListResponseBody<>(entityCategoryService.getEntityTypeList(projectId,
            orgId,
            type.name()));
    }

    @Override
    @Operation(
        summary = "获取实体类型对应的电子签名坐标列表",
        description = "获取实体类型对应的电子签名坐标列表"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<DrawingSignatureCoordinate> getSignatureCoordinate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        return new JsonListResponseBody<>(
            entityCategoryService.getSignatureCoordinate(
                orgId,
                projectId,
                id
            )
        );
    }

    @Override
    @Operation(
        summary = "获取实体类型对应的条形码坐标列表",
        description = "获取实体类型对应的条形码坐标列表"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<DrawingCoordinate> getBarCodeCoordinate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        return new JsonListResponseBody<>(
            entityCategoryService.getBarCodeCoordinate(
                orgId,
                projectId,
                id
            )
        );
    }

    @Override
    @Operation(
        summary = "获取实体类型对应的条形码坐标列表",
        description = "获取实体类型对应的条形码坐标列表"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<DrawingCoordinate> getBarCodeCoordinatesByEntitySubType(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "entitySubType") String entitySubType
    ) {
        return new JsonListResponseBody<>(
            entityCategoryService.getBarCodeCoordinateListByEntitySubType(
                orgId,
                projectId,
                entitySubType
            )
        );
    }

    @Override
    @Operation(
        summary = "添加电子签名坐标",
        description = "添加电子签名坐标"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<DrawingSignatureCoordinate> addSignatureCoordinate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody DrawingSignatureCoordinateDTO coordinateDTO
    ) {
        return new JsonObjectResponseBody<>(
            entityCategoryService.addSignatureCoordinate(
                orgId,
                projectId,
                id,
                coordinateDTO
            )
        );
    }


    @Override
    @Operation(
        summary = "修改电子签名坐标",
        description = "修改电子签名坐标"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<DrawingSignatureCoordinate> updateSignatureCoordinate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "entitySubTypeId") Long entitySubTypeId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody DrawingSignatureCoordinateDTO coordinateDTO
    ) {
        return new JsonObjectResponseBody<>(
            entityCategoryService.updateSignatureCoordinate(
                orgId,
                projectId,
                entitySubTypeId,
                id,
                coordinateDTO
            )
        );
    }


    @Override
    @Operation(
        summary = "修改条形码坐标",
        description = "修改条形码坐标"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<DrawingCoordinate> updateBarCodeCoordinate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "entitySubTypeId") Long entitySubTypeId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody DrawingCoordinateDTO coordinateDTO
    ) {
        return new JsonObjectResponseBody<>(
            entityCategoryService.updateBarCodeCoordinate(
                orgId,
                projectId,
                entitySubTypeId,
                id,
                coordinateDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "删除电子签名坐标",
        description = "删除电子签名坐标"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteSignatureCoordinate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "entitySubTypeId") Long entitySubTypeId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        entityCategoryService.deleteSignatureCoordinate(
            orgId,
            projectId,
            entitySubTypeId,
            id
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除坐标",
        description = "删除坐标"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteCoordinate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "entitySubTypeId") Long entitySubTypeId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        entityCategoryService.deleteCoordinate(
            orgId,
            projectId,
            entitySubTypeId,
            id
        );
        return new JsonResponseBody();
    }



}
