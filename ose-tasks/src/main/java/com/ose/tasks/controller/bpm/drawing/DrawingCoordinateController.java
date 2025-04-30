package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.DrawingCoordinateAPI;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingCoordinateInterface;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.DrawingCoordinateCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateDTO;
import com.ose.tasks.dto.drawing.DrawingCoordinateEntitySubTypeCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingSignatureCoordinateDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.entity.drawing.DrawingSignatureCoordinate;
import com.ose.tasks.vo.RelationReturnEnum;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "实体类型接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-coordinate")
public class DrawingCoordinateController extends BaseController implements DrawingCoordinateAPI {

    /**
     * 实体类型服务
     */
    private final DrawingCoordinateInterface drawingCoordinateService;

    /**
     * 构造方法
     *
     * @param drawingCoordinateService 实体类型服务
     */
    @Autowired
    public DrawingCoordinateController(DrawingCoordinateInterface drawingCoordinateService) {
        this.drawingCoordinateService = drawingCoordinateService;
    }


    @Override
    @Operation(
        summary = "获取实体类型对应的条形码坐标列表",
        description = "获取实体类型对应的条形码坐标列表"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<DrawingCoordinateResponseDTO> getList(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        DrawingCoordinateCriteriaDTO criteriaDTO
    ) {
        Page<DrawingCoordinate> drawingCoordinates = drawingCoordinateService.getList(orgId, projectId, criteriaDTO);
        Function<DrawingCoordinate, DrawingCoordinateResponseDTO> converter = DrawingCoordinateResponseDTO::new;
        Page<DrawingCoordinateResponseDTO> result = drawingCoordinates.map(converter);

        for (DrawingCoordinateResponseDTO dto : result.getContent()) {
            dto.setEntitySubTypeList(drawingCoordinateService.getEntitySubTypeByDrawingCoordinateId(dto.getId(), projectId, orgId));
        }

        return new JsonListResponseBody<>(
            getContext(),
            result
        );
    }

    @Override
    @Operation(
        summary = "添加坐标",
        description = "添加坐标"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<DrawingCoordinate> create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody DrawingCoordinateDTO coordinateDTO
    ) {
        return new JsonObjectResponseBody<>(
            drawingCoordinateService.create(
                orgId,
                projectId,
                coordinateDTO
            )
        );
    }


    @Override
    @Operation(
        summary = "修改坐标",
        description = "修改坐标"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<DrawingCoordinate> edit(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody DrawingCoordinateDTO coordinateDTO
    ) {
        return new JsonObjectResponseBody<>(
            drawingCoordinateService.update(
                orgId,
                projectId,
                id,
                coordinateDTO
            )
        );
    }


    @Override
    @Operation(
        summary = "删除坐标",
        description = "删除坐标"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteCoordinate(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        drawingCoordinateService.deleteCoordinate(
            orgId,
            projectId,
            id
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "获取工序对应的实体类型列表")
    @WithPrivilege
    public JsonListResponseBody<BpmEntitySubType> getEntitySubTypeList(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "工序 ID") Long id,
        DrawingCoordinateEntitySubTypeCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            drawingCoordinateService
                .getEntitySubTypeList(id, projectId, orgId, criteriaDTO)
        );
    }


    @Override
    @Operation(
        summary = "添加实体类型",
        description = "为指定的坐标添加实体类型。"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/entity-sub-types/{entitySubTypeId}"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonResponseBody addEntity(
        @PathVariable @Parameter(description = "坐标id") Long id,
        @PathVariable @Parameter(description = "实体id") Long entitySubTypeId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        RelationReturnEnum r = drawingCoordinateService.addEntitySubType(id, entitySubTypeId, projectId, orgId);
        if (r.equals(RelationReturnEnum.SAVE_SUCCESS)) {
            return new JsonResponseBody();
        } else {
            return new JsonResponseBody().setError(new ValidationError(r.getMessage()));
        }
    }


    @Override
    @Operation(
        summary = "添加实体类型",
        description = "为指定的坐标添加实体类型。"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/entity-sub-types"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BatchAddResponseDTO> addEntityBatch(
        @PathVariable @Parameter(description = "坐标id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody BatchAddRelationDTO dto) {
        List<BatchAddResponseDTO> result = new ArrayList<>();
        for (Long entityid : dto.getIds()) {

            BatchAddResponseDTO rdto = new BatchAddResponseDTO();
            rdto.setId(entityid);
            RelationReturnEnum r = drawingCoordinateService.addEntitySubType(id, entityid, projectId, orgId);
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
    @Operation(
        summary = "删除实体类型",
        description = "删除指定的坐标下的指定实体类型"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}/entity-sub-types/{entitySubTypeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteEntity(
        @PathVariable @Parameter(description = "坐标id") Long id,
        @PathVariable @Parameter(description = "实体id") Long entitySubTypeId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        RelationReturnEnum r = drawingCoordinateService.deleteEntitySubType(id, entitySubTypeId, projectId, orgId);
        if (r.equals(RelationReturnEnum.DELETE_SUCCESS)) {
            return new JsonResponseBody();
        } else {
            return new JsonResponseBody().setError(new ValidationError(r.getMessage()));
        }
    }

    @Override
    @Operation(
        summary = "获取坐标详细信息",
        description = "根据ID查询坐标详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<DrawingCoordinate> get(
        @PathVariable @Parameter(description = "坐标id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonObjectResponseBody<>(getContext(), drawingCoordinateService.get(id, projectId, orgId));
    }

    @Override
    @Operation(
        summary = "返回文件坐标文件流信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}/preview",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public void downloadDrawingCoordinateFile(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "文件历史ID") Long id) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File file = drawingCoordinateService.saveDownloadFile(orgId, projectId, id, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType("application/pdf");

            response.setHeader("Content-Disposition", "inline");

            IOUtils.copy(
                new FileInputStream(file), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("", "文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("", "下载文件出错");
        }

        response.flushBuffer();
    }
}
