package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.ProofreadAPI;
import com.ose.tasks.domain.model.service.drawing.ProofreadInterface;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.SubDrawingReviewOpinion;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "图纸校审接口")
@RestController
public class ProofreadController extends BaseController implements ProofreadAPI {

    private ProofreadInterface proofreadService;

    /**
     * 构造方法
     */
    @Autowired
    public ProofreadController(
        ProofreadInterface proofreadService
    ) {
        this.proofreadService = proofreadService;
    }


    @Override
    @Operation(
        summary = "查询登录用户相关校审中的图集",
        description = "查询登录用户相关校审中的图集"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<DrawingDetail> drawingList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PageDTO page,
        ProofreadDrawingListCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(
            proofreadService.getProofreadDrawingList(
                orgId,
                projectId,
                getContext().getOperator().getId(),
                page,
                criteriaDTO
            )
        );

    }

    @Override
    @Operation(
        summary = "查询图集校审任务信息",
        description = "查询图集校审任务信息"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ProofreadTaskDTO> drawingTaskInfo(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图集 ID") Long drawingId,
        DrawingProofreadSearchDTO drawingProofreadSearchDTO
    ) {
        return new JsonObjectResponseBody<>(
            proofreadService.getDrawingTaskInfo(
                orgId,
                projectId,
                drawingId,
                drawingProofreadSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "子图纸预览PDF",
        description = "子图纸预览PDF"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ProofreadSubDrawingPreviewDTO> subDrawingPreview(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子图纸 ID") Long subDrawingId
    ) {
        return new JsonObjectResponseBody<>(
            proofreadService.getSubDrawingPreview(
                orgId,
                projectId,
                subDrawingId
            )
        );
    }

    @Override
    @Operation(
        summary = "子图纸校审意见列表",
        description = "子图纸校审意见列表"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<SubDrawingReviewOpinion> subDrawingOpinionList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子图纸 ID") Long subDrawingId,
        @PathVariable @Parameter(description = "任务节点 ID") Long taskId
    ) {
        return new JsonListResponseBody<>(
            proofreadService.getSubDrawingOpinionList(
                orgId,
                projectId,
                subDrawingId,
                taskId
            )
        );
    }

    @Override
    @Operation(
        summary = "新增子图纸校审意见",
        description = "新增子图纸校审意见"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<SubDrawingReviewOpinion> createOpinion(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子图纸 ID") Long subDrawingId,
        @PathVariable @Parameter(description = "任务节点 ID") Long taskId,
        @RequestBody SubDrawingReviewOpinionDTO dto
    ) {
        return new JsonObjectResponseBody<>(
            proofreadService.createSubDrawingOpinion(
                orgId,
                projectId,
                subDrawingId,
                taskId,
                dto,
                getContext().getOperator().getId()
            )
        );
    }

    @Override
    @Operation(
        summary = "修改子图纸校审状态",
        description = "修改子图纸校审状态"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody reviewStatus(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子图纸 ID") Long subDrawingId,
        @RequestBody SubDrawingReviewStatusDTO dto
    ) {
        proofreadService.modifyReviewStatus(
            orgId,
            projectId,
            subDrawingId,
            dto
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "批量修改子图纸校审状态",
        description = "批量修改子图纸校审状态"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody batchReviewStatus(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody SubDrawingReviewStatusDTO dto
    ) {
        proofreadService.batchModifyReviewStatus(
            orgId,
            projectId,
            dto
        );
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "上传修改的图纸",
        description = "上传修改的图纸"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ProofreadUploadResponseDTO> uploadSubDrawing(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸 ID") Long drawingId,
        @RequestBody ProofreadPipingDrawingUploadDTO uploadDTO
    ) {
        return new JsonObjectResponseBody<>(
            proofreadService.uploadSubDrawing(
                orgId,
                projectId,
                drawingId,
                uploadDTO,
                getContext().getOperator()
            )
        );
    }

    @Override
    @Operation(
        summary = "回复校审意见",
        description = "回复校审意见"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<SubDrawingReviewOpinion> createOpinionReply(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子图纸 ID") Long subDrawingId,
        @PathVariable @Parameter(description = "任务节点 ID") Long taskId,
        @PathVariable @Parameter(description = "校审意见 ID") Long opinionId,
        @RequestBody SubDrawingReviewOpinionDTO dto
    ) {
        return new JsonObjectResponseBody<>(
            proofreadService.createOpinionReply(
                orgId,
                projectId,
                subDrawingId,
                taskId,
                opinionId,
                dto,
                getContext().getOperator().getId()
            )
        );
    }
}
