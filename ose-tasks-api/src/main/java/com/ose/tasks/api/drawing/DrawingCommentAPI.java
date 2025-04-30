package com.ose.tasks.api.drawing;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.DrawingComment;
import com.ose.tasks.entity.drawing.DrawingCommentResponse;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 评论管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-comment")
public interface DrawingCommentAPI {


    /**
     * 获取评论列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingComment> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        DrawingCommentCriteriaDTO criteriaDTO
    );

    /**
     * 添加评论
     */
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<DrawingComment> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingCommentDTO commentDTO
    );

    /**
     * 修改
     */
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingComment> edit(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingCommentDTO commentDTO
    );

    /**
     * 删除
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody deleteDrawingComment(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 关闭
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/close",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody close(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 关闭
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/open",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody open(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取评论详细信息
     *
     * @return 坐标
     */
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingComment> get(
        @PathVariable("id") Long id,
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId
    );

    /**
     * 新增评论回复
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/response"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<DrawingCommentResponse> createResponse(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingCommentResponseDTO responseDTO
    );

    /**
     * 新增评论回复
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/response-to-response"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<DrawingCommentResponse> createResponseToResponse(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingCommentResponseDTO responseDTO
    );

    /**
     * 获取评论列表
     */
    @RequestMapping(
        method = GET,
        value = "/{id}/response"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingCommentResponse> getResponseList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取评论列表
     */
    @RequestMapping(
        method = GET,
        value = "/response/{responseId}"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingCommentResponse> getResponseListByResponse(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("responseId") Long responseId
    );

    /**
     * 修改
     */
    @RequestMapping(
        method = POST,
        value = "/response/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingCommentResponse> editResponse(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingCommentResponseDTO dto
    );

    /**
     * 删除
     */
    @RequestMapping(
        method = DELETE,
        value = "/response/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingCommentResponse> deleteResponse(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

//    /**
//     * 返回文件坐标文件流信息
//     *
//     */
//    @RequestMapping(
//        method = GET,
//        value = "/{id}/preview"
//    )
//    @ResponseStatus(OK)
//    void downloadDrawingCoordinateFile(
//        @PathVariable @Parameter(description = "组织ID") Long orgId,
//        @PathVariable @Parameter(description = "项目ID") Long projectId,
//        @PathVariable @Parameter(description = "文件历史ID") Long id
//    ) throws IOException;
}
