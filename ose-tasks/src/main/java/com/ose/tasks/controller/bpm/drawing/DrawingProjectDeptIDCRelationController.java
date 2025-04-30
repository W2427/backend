package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.DrawingProjectDeptIDCRelationAPI;
import com.ose.tasks.domain.model.service.drawing.*;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.drawing.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "管道生产设计图纸清单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-project-dept-idc-relation")
public class DrawingProjectDeptIDCRelationController extends BaseController implements DrawingProjectDeptIDCRelationAPI {

    private final DrawingProjectDeptIDCRelationInterface drawingProjectDeptIDCRelationService;


    /**
     * 构造方法
     */
    @Autowired
    public DrawingProjectDeptIDCRelationController(
        DrawingProjectDeptIDCRelationInterface drawingProjectDeptIDCRelationService
    ) {
        this.drawingProjectDeptIDCRelationService = drawingProjectDeptIDCRelationService;
    }

    @Override
    @Operation(summary = "创建", description = "创建")
    @RequestMapping(method = POST, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingProjectDeptIDCRelation> create(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                  @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                  @RequestBody DrawingProjectDeptIDCRelationDTO dto) {

        Long userId = getContext().getOperator().getId();
        return new JsonObjectResponseBody<>(
            getContext(),
            drawingProjectDeptIDCRelationService.create(orgId, projectId, userId, dto)
        );

    }

    @Override
    @Operation(summary = "修改", description = "修改")
    @RequestMapping(method = POST, value = "/{id}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingProjectDeptIDCRelation> edit(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                @PathVariable @Parameter(description = "条目ID") Long id,
                                                @RequestBody DrawingProjectDeptIDCRelationDTO dto) {
        Long userId = getContext().getOperator().getId();
        return new JsonObjectResponseBody<>(
            getContext(),
            drawingProjectDeptIDCRelationService.modify(orgId, projectId, id, userId, dto)
        );
    }

    @Override
    @Operation(summary = "查询", description = "查询")
    @RequestMapping(method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE, value = "")
    @WithPrivilege
    public JsonListResponseBody<DrawingProjectDeptIDCRelation> getList(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                 @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                    DrawingProjectDeptIDCRelationSearchDTO dto) {
        return new JsonListResponseBody<>(getContext(),
            drawingProjectDeptIDCRelationService.getList(orgId, projectId, dto));
    }

    @Override
    @Operation(summary = "查询", description = "查询")
    @RequestMapping(method = GET, consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE, value = "/{drawingId}")
    @WithPrivilege
    public JsonListResponseBody<DrawingProjectDeptIDCRelationDTO> getListByDrawingId(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                                       @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                                                     @PathVariable @Parameter(description = "图纸 ID") Long drawingId) {
        return new JsonListResponseBody<>(getContext(),
            drawingProjectDeptIDCRelationService.getListByDrawingId(orgId, projectId, drawingId));
    }


    @Override
    @Operation(summary = "删除", description = "删除")
    @RequestMapping(method = POST, value = "/{id}/delete", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody delete(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                   @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                   @PathVariable @Parameter(description = "条目ID") Long id) {
        OperatorDTO operatorDTO = getContext().getOperator();
        drawingProjectDeptIDCRelationService.delete(orgId, projectId, id, operatorDTO);
        return new JsonResponseBody();
    }


}
