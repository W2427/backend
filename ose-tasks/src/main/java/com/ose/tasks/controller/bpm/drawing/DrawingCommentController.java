package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.DrawingCommentAPI;
import com.ose.tasks.api.drawing.DrawingCoordinateAPI;
import com.ose.tasks.domain.model.service.drawing.DrawingCommentInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingCoordinateInterface;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.DrawingComment;
import com.ose.tasks.entity.drawing.DrawingCommentResponse;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.vo.RelationReturnEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "评论管理接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-comment")
public class DrawingCommentController extends BaseController implements DrawingCommentAPI {

    /**
     * 实体类型服务
     */
    private final DrawingCommentInterface drawingCommentService;

    /**
     * 构造方法
     *
     * @param drawingCommentService 实体类型服务
     */
    @Autowired
    public DrawingCommentController(DrawingCommentInterface drawingCommentService) {
        this.drawingCommentService = drawingCommentService;
    }


    @Override
    @Operation(
        summary = "获取评论列表",
        description = "获取评论列表"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<DrawingComment> getList(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        DrawingCommentCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            drawingCommentService.getList(
                orgId,
                projectId,
                criteriaDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "添加评论",
        description = "添加评论"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<DrawingComment> create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody DrawingCommentDTO commentDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        return new JsonObjectResponseBody<>(
            drawingCommentService.create(
                orgId,
                projectId,
                commentDTO,
                operator
            )
        );
    }


    @Override
    @Operation(
        summary = "修改评论",
        description = "修改评论"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<DrawingComment> edit(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody DrawingCommentDTO commentDTO
    ) {
        return new JsonObjectResponseBody<>(
            drawingCommentService.update(
                orgId,
                projectId,
                id,
                commentDTO
            )
        );
    }


    @Override
    @Operation(
        summary = "删除评论",
        description = "删除评论"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody deleteDrawingComment(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        drawingCommentService.deleteDrawingComment(
            orgId,
            projectId,
            id
        );
        return new JsonResponseBody();
    }


    @Override
    @Operation(
        summary = "关闭评论",
        description = "关闭评论"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/close",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody close(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        drawingCommentService.close(
            orgId,
            projectId,
            id
        );
        return new JsonResponseBody();
    }


    @Override
    @Operation(
        summary = "开启评论",
        description = "开启评论"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/open",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody open(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        drawingCommentService.open(
            orgId,
            projectId,
            id
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取评论详细信息",
        description = "根据ID查询评论详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<DrawingComment> get(
        @PathVariable @Parameter(description = "评论id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonObjectResponseBody<>(getContext(), drawingCommentService.get(id, projectId, orgId));
    }

    @Override
    @Operation(
        summary = "添加评论",
        description = "添加评论"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/response"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<DrawingCommentResponse> createResponse(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "commentID") Long id,
        @RequestBody DrawingCommentResponseDTO responseDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        return new JsonObjectResponseBody<>(
            drawingCommentService.createResponse(
                orgId,
                projectId,
                id,
                responseDTO,
                operator
            )
        );
    }

    @Override
    @Operation(
        summary = "添加评论",
        description = "添加评论"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}/response-to-response"
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<DrawingCommentResponse> createResponseToResponse(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "responseID") Long id,
        @RequestBody DrawingCommentResponseDTO responseDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();
        return new JsonObjectResponseBody<>(
            drawingCommentService.createResponseToResponse(
                orgId,
                projectId,
                id,
                responseDTO,
                operator
            )
        );
    }


    @Override
    @Operation(
        summary = "获取评论列表",
        description = "获取评论列表"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}/response"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<DrawingCommentResponse> getResponseList(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable("id") Long id
    ) {
        return new JsonListResponseBody<>(
            drawingCommentService.getResponseList(
                orgId,
                projectId,
                id
            )
        );
    }


    @Override
    @Operation(
        summary = "获取评论列表",
        description = "获取评论列表"
    )
    @RequestMapping(
        method = GET,
        value = "/response/{responseId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<DrawingCommentResponse> getResponseListByResponse(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable("responseId") Long responseId
    ) {
        return new JsonListResponseBody<>(
            drawingCommentService.getResponseListByResponse(
                orgId,
                projectId,
                responseId
            )
        );
    }


    @Override
    @Operation(
        summary = "修改评论",
        description = "修改评论"
    )
    @RequestMapping(
        method = POST,
        value = "/response/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<DrawingCommentResponse> editResponse(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody DrawingCommentResponseDTO dto
    ) {
        return new JsonObjectResponseBody<>(
            drawingCommentService.updateResponse(
                orgId,
                projectId,
                id,
                dto
            )
        );
    }


    @Override
    @Operation(
        summary = "删除回复",
        description = "删除回复"
    )
    @RequestMapping(
        method = DELETE,
        value = "/response/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<DrawingCommentResponse> deleteResponse(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id
    ) {
        return new JsonObjectResponseBody<>(
            drawingCommentService.deleteResponse(
                orgId,
                projectId,
                id
            )
        );
    }

//    @Override
//    @Operation(
//        value = "返回文件评论文件流信息"
//    )
//    @RequestMapping(
//        method = GET,
//        value = "/{id}/preview",
//        produces = APPLICATION_JSON_VALUE
//    )
//    @WithPrivilege
//    @ResponseStatus(OK)
//    public void downloadDrawingCoordinateFile(
//        @PathVariable @Parameter(description = "组织ID") Long orgId,
//        @PathVariable @Parameter(description = "项目ID") Long projectId,
//        @PathVariable @Parameter(description = "文件历史ID") Long id) throws IOException {
//        final OperatorDTO operator = getContext().getOperator();
//        File file = drawingCoordinateService.saveDownloadFile(orgId, projectId, id, operator.getId());
//
//        HttpServletResponse response = getContext().getResponse();
//        try {
//            response.setContentType("application/pdf");
//
//            response.setHeader("Content-Disposition", "inline");
//
//            IOUtils.copy(
//                new FileInputStream(file), response.getOutputStream()
//            );
//
//        } catch (FileNotFoundException e) {
//            throw new NotFoundError();
//        } catch (UnsupportedEncodingException e) {
//            throw new BusinessError("", "文件编码不支持");
//        } catch (IOException e) {
//            throw new BusinessError("", "下载文件出错");
//        }
//
//        response.flushBuffer();
//    }
}
