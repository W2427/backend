package com.ose.tasks.api.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.DrawingCoordinateCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateEntitySubTypeCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingSignatureCoordinateDTO;
import com.ose.tasks.entity.bpm.*;
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
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 坐标接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-coordinate")
public interface DrawingCoordinateAPI {


    /**
     * 获取条形码坐标列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingCoordinateResponseDTO> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        DrawingCoordinateCriteriaDTO criteriaDTO
    );

    /**
     * 添加条形码坐标
     */
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<DrawingCoordinate> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingCoordinateDTO coordinateDTO
    );

    /**
     * 修改电子签名坐标
     */
    @RequestMapping(
        method = PATCH,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingCoordinate> edit(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingCoordinateDTO coordinateDTO
    );

    /**
     * 删除电子签名坐标
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteCoordinate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取坐标对应的实体类型列表。
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
        DrawingCoordinateEntitySubTypeCriteriaDTO criteriaDTO
    );

    /**
     * 添加实体类型
     *
     * @param id              坐标id
     * @param entitySubTypeId 实体id
     * @return 工序信息
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/entity-sub-types/{entitySubTypeId}"
    )
    JsonResponseBody addEntity(
        @PathVariable("id") Long id,
        @PathVariable("entitySubTypeId") Long entitySubTypeId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 批量添加实体类型
     *
     * @param id              坐标id
     * @return 工序信息
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/entity-sub-types"
    )
    JsonListResponseBody<BatchAddResponseDTO> addEntityBatch(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @RequestBody BatchAddRelationDTO dto
    );


    /**
     * 删除实体类型
     *
     * @param id              坐标id
     * @param entitySubTypeId 实体id
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}/entity-sub-types/{entitySubTypeId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteEntity(
        @PathVariable("id") Long id,
        @PathVariable("entitySubTypeId") Long entitySubTypeId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 获取坐标详细信息
     *
     * @return 坐标
     */
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingCoordinate> get(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 返回文件坐标文件流信息
     *
     */
    @RequestMapping(
        method = GET,
        value = "/{id}/preview"
    )
    @ResponseStatus(OK)
    void downloadDrawingCoordinateFile(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "文件历史ID") Long id
    ) throws IOException;
}
