package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.DrawingAnnotationAPI;
import com.ose.tasks.domain.model.service.drawing.DrawingAnnotationInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingAnnotationReplyInterface;
import com.ose.tasks.dto.drawing.DrawingAnnotationCreateDTO;
import com.ose.tasks.entity.drawing.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "图纸评审意见接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class DrawingAnnotationController extends BaseController implements DrawingAnnotationAPI {

    private DrawingAnnotationInterface drawingAnnotationService;
    private DrawingAnnotationReplyInterface drawingAnnotationReplyService;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingAnnotationController(
        DrawingAnnotationInterface drawingAnnotationService,
        DrawingAnnotationReplyInterface drawingAnnotationReplyService
    ) {
        this.drawingAnnotationService = drawingAnnotationService;
        this.drawingAnnotationReplyService = drawingAnnotationReplyService;
    }

    @Operation(summary = "图纸评审意见表", description = "图纸评审意见表")
    @RequestMapping(method = GET, value = "drawing-detail/{drawingDetailId}/drawing-annotation",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<DrawingAnnotation> search(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        PageDTO page
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            drawingAnnotationService.search(orgId, projectId, drawingDetailId, page));
    }

    @Operation(summary = "图纸评审意见表详细信息", description = "图纸评审意见表详细信息")
    @RequestMapping(method = GET, value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingAnnotation> detail(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @PathVariable @Parameter(description = "评审意见ID") Long drawingAnnotationId) {
        DrawingAnnotation drawingAnnotation = drawingAnnotationService.detail(orgId, projectId, drawingAnnotationId);
        return new JsonObjectResponseBody<>(getContext(), drawingAnnotation);
    }

    @Operation(summary = "图纸评审意见表创建", description = "图纸评审意见表创建")
    @RequestMapping(method = POST, value = "drawing-detail/{drawingDetailId}/drawing-annotation",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingAnnotation> create(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @RequestBody DrawingAnnotationCreateDTO drawingAnnotationCreateDTO) {
        DrawingAnnotation drawingAnnotation = drawingAnnotationService.create(orgId, projectId, drawingDetailId, drawingAnnotationCreateDTO, getContext());
        return new JsonObjectResponseBody<>(getContext(), drawingAnnotation);
    }

    @Operation(summary = "删除图纸评审意见", description = "删除图纸评审意见")
    @RequestMapping(method = DELETE, value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @SetUserInfo
    public JsonResponseBody delete(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                   @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                   @PathVariable @Parameter(description = "图纸评审意见ID") Long drawingAnnotationId) {
        drawingAnnotationService.delete(orgId, projectId, drawingAnnotationId, getContext());

        return new JsonResponseBody();
    }

    @Operation(summary = "图纸评审意见回复创建", description = "图纸评审意见回复创建")
    @RequestMapping(
        method = POST,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}/reply",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingAnnotationReply> createReply(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @PathVariable @Parameter(description = "图纸评审 ID") Long drawingAnnotationId,
        @RequestBody DrawingAnnotationCreateDTO drawingAnnotationCreateDTO) {
        DrawingAnnotationReply drawingAnnotationReply = drawingAnnotationReplyService.create(orgId, projectId, drawingDetailId, drawingAnnotationId, drawingAnnotationCreateDTO, getContext());
        return new JsonObjectResponseBody<>(getContext(), drawingAnnotationReply);
    }

    @Operation(summary = "图纸评审意见表", description = "图纸评审意见表")
    @RequestMapping(
        method = GET,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}/reply",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<DrawingAnnotationReply> searchReply(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @PathVariable @Parameter(description = "图纸评审 ID") Long drawingAnnotationId,
        PageDTO page
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            drawingAnnotationReplyService.search(orgId, projectId, drawingAnnotationId, page));
    }

    @Operation(summary = "图纸评审意见回复", description = "图纸评审意见回复")
    @RequestMapping(
        method = GET,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}/reply/{drawingAnnotationReplyId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<DrawingAnnotationReply> detailReply(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @PathVariable @Parameter(description = "评审意见ID") Long drawingAnnotationId,
        @PathVariable @Parameter(description = "评审意见回复ID") Long drawingAnnotationReplyId) {
        DrawingAnnotationReply drawingAnnotationReply = drawingAnnotationReplyService.detail(orgId, projectId, drawingAnnotationReplyId);
        return new JsonObjectResponseBody<>(getContext(), drawingAnnotationReply);
    }
}
