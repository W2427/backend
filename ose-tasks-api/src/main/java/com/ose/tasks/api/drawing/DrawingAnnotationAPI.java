package com.ose.tasks.api.drawing;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.drawing.DrawingAnnotationCreateDTO;
import com.ose.tasks.entity.drawing.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface DrawingAnnotationAPI {

    /**
     * 获取图纸评审意见列表
     *
     * @return 图纸评审意见列表
     */
    @RequestMapping(
        method = GET,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingAnnotation> search(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        PageDTO page
    );

    /**
     * 图纸评审意见表创建
     */
    @RequestMapping(
        method = POST,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation"
    )
    JsonObjectResponseBody<DrawingAnnotation> create(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @RequestBody DrawingAnnotationCreateDTO drawingAnnotationCreateDTO);

    /**
     * 获取图纸评审意见详情
     */
    @RequestMapping(
        method = GET,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}"
    )
    JsonObjectResponseBody<DrawingAnnotation> detail(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @PathVariable @Parameter(description = "评审意见ID") Long drawingAnnotationId);

    /**
     * 删除图纸评审意见
     *
     * @param drawingAnnotationId 评审意见ID
     * @return 图纸评审意见
     */
    @RequestMapping(
        method = POST,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                            @PathVariable @Parameter(description = "图纸评审意见ID") Long drawingAnnotationId);

    /**
     * 图纸评审意见回复创建
     */
    @RequestMapping(
        method = POST,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}/reply"
    )
    JsonObjectResponseBody<DrawingAnnotationReply> createReply(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @PathVariable @Parameter(description = "图纸评审 ID") Long drawingAnnotationId,
        @RequestBody DrawingAnnotationCreateDTO drawingAnnotationCreateDTO);

    /**
     * 获取图纸评审意见回复列表
     *
     * @return 图纸评审意见回复列表
     */
    @RequestMapping(
        method = GET,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}/reply"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingAnnotationReply> searchReply(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @PathVariable @Parameter(description = "图纸评审 ID") Long drawingAnnotationId,
        PageDTO page
    );

    /**
     * 获取图纸评审意见回复详情
     */
    @RequestMapping(
        method = GET,
        value = "drawing-detail/{drawingDetailId}/drawing-annotation/{drawingAnnotationId}/reply/{drawingAnnotationReplyId}"
    )
    JsonObjectResponseBody<DrawingAnnotationReply> detailReply(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸详情 ID") Long drawingDetailId,
        @PathVariable @Parameter(description = "评审意见ID") Long drawingAnnotationId,
        @PathVariable @Parameter(description = "评审意见回复ID") Long drawingAnnotationReplyId);
}
