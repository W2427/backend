package com.ose.tasks.api.bpm;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.DrawingCoordinateDTO;
import com.ose.tasks.dto.drawing.DrawingSignatureCoordinateDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.entity.drawing.DrawingSignatureCoordinate;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 实体类型接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/entity-sub-types")
public interface EntitySubTypeAPI {

    /**
     * 创建实体类型
     *
     * @param entityDTO 实体类型信息
     * @return 创建的实体类型
     */
    @RequestMapping(method = POST)
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmEntitySubType> create(
        @RequestBody EntitySubTypeDTO entityDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取实体类型列表
     *
     * @param page 分页信息
     * @return 实体类型列表
     */
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntitySubType> getList(
        HttpServletRequest request,
        HttpServletResponse response,
        EntitySubTypeCriteriaDTO page,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取实体类型详细信息
     *
     * @param id 实体id
     * @return 实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<EntitySubTypeResponseDTO> get(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 删除实体类型
     *
     * @param id 实体类型id
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 编辑实体类型
     *
     * @param id        实体类型id
     * @param entityDTO 实体信息
     * @return 实体信息
     */
    @RequestMapping(
        method = POST,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody modify(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("id") Long id,
        @RequestBody EntitySubTypeDTO entityDTO,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取工序列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "/{id}/processes"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntityTypeProcessRelation> getProcessList(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        EntitySubTypeProcessCriteriaDTO criteriaDTO
    );

    /**
     * 获取工序阶段列表
     *
     * @return 工序阶段列表
     */
    @RequestMapping(
        method = GET,
        value = "/{id}/process-stages"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmProcessStage> getProcessStageList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 添加工序
     *
     * @param id 实体类型id
     * @return 对应的工序列表
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/processes"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BatchAddResponseDTO> addProcessBatch(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody BatchAddRelationDTO dto
    );

    /**
     * 添加工序
     *
     * @param id 实体类型id
     * @return 对应的工序列表
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/processes/{processId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody addProcess(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("id") Long id,
        @PathVariable("processId") Long processId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 删除工序类型
     *
     * @param id 实体id
     * @return 对应的工序列表
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}/processes/{processId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteProcess(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("id") Long id,
        @PathVariable("processId") Long processId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取全部工序
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "/processes"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmProcess> getProcessList(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 批量排序
     *
     * @return 工序
     */
    @RequestMapping(
        method = POST,
        value = "/sort"
    )
    @ResponseStatus(OK)
    JsonResponseBody batchSort(
        @RequestBody List<SortDTO> sortDTOs,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取工序列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "{type}/types"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntityType> getEntityCategoryTypeList(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type
    );

    /**
     * 获取电子签名坐标列表
     */
    @RequestMapping(
        method = GET,
        value = "{id}/signature-coordinate"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingSignatureCoordinate> getSignatureCoordinate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取条形码坐标列表
     */
    @RequestMapping(
        method = GET,
        value = "{id}/barcode-coordinate"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingCoordinate> getBarCodeCoordinate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取条形码坐标列表
     */
    @RequestMapping(
        method = GET,
        value = "{entitySubType}/barcode-coordinates"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingCoordinate> getBarCodeCoordinatesByEntitySubType(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entitySubType") String entitySubType
    );

    /**
     * 添加电子签名坐标
     */
    @RequestMapping(
        method = POST,
        value = "{id}/signature-coordinate"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<DrawingSignatureCoordinate> addSignatureCoordinate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingSignatureCoordinateDTO coordinateDTO
    );

    /**
     * 修改电子签名坐标
     */
    @RequestMapping(
        method = PATCH,
        value = "{entitySubTypeId}/signature-coordinate/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingSignatureCoordinate> updateSignatureCoordinate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entitySubTypeId") Long entitySubTypeId,
        @PathVariable("id") Long id,
        @RequestBody DrawingSignatureCoordinateDTO coordinateDTO
    );

    /**
     * 修改电子签名坐标
     */
    @RequestMapping(
        method = PATCH,
        value = "{entitySubTypeId}/barcode-coordinate/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingCoordinate> updateBarCodeCoordinate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entitySubTypeId") Long entitySubTypeId,
        @PathVariable("id") Long id,
        @RequestBody DrawingCoordinateDTO coordinateDTO
    );

    /**
     * 删除电子签名坐标
     */
    @RequestMapping(
        method = DELETE,
        value = "{entitySubTypeId}/signature-coordinate/{id}"
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteSignatureCoordinate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entitySubTypeId") Long entitySubTypeId,
        @PathVariable("id") Long id
    );

    /**
     * 删除电子签名坐标
     */
    @RequestMapping(
        method = DELETE,
        value = "{entitySubTypeId}/barcode-coordinate/{id}"
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteCoordinate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("entitySubTypeId") Long entitySubTypeId,
        @PathVariable("id") Long id
    );

    @Operation(
        summary = "获取实体类型分类列表",
        description = "获取全部实体类型对应的分类列表"
    )
    @RequestMapping(
        method = GET,
        value = "{type}/types",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntityType> getEntityTypeList(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type);

}
