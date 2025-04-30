package com.ose.tasks.api.drawing;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.SubDrawingReviewOpinion;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface ProofreadAPI {

    /*
     * 查询当前用户相关的校审中的图集
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-proofread/drawings",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingDetail> drawingList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page,
        ProofreadDrawingListCriteriaDTO criteriaDTO
    );

    /*
     * 查询图纸校审任务信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-proofread/drawings/{drawingId}/task",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ProofreadTaskDTO> drawingTaskInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        DrawingProofreadSearchDTO drawingProofreadSearchDTO
    );

    /*
     * 子图纸预览
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-proofread/sub-drawings/{subDrawingId}/preview",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ProofreadSubDrawingPreviewDTO> subDrawingPreview(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subDrawingId") Long subDrawingId
    );

    /*
     * 子图纸意见列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-proofread/sub-drawings/{subDrawingId}/tasks/{taskId}/opinions",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<SubDrawingReviewOpinion> subDrawingOpinionList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subDrawingId") Long subDrawingId,
        @PathVariable("taskId") Long taskId
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-proofread/sub-drawings/{subDrawingId}/tasks/{taskId}/opinions",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<SubDrawingReviewOpinion> createOpinion(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subDrawingId") Long subDrawingId,
        @PathVariable("taskId") Long taskId,
        @RequestBody SubDrawingReviewOpinionDTO dto
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-proofread/sub-drawings/{subDrawingId}/review-status",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody reviewStatus(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subDrawingId") Long subDrawingId,
        @RequestBody SubDrawingReviewStatusDTO dto
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-proofread/sub-drawings/batch-review-status",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody batchReviewStatus(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody SubDrawingReviewStatusDTO dto
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-proofread/drawings/{drawingId}/sub-drawing",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ProofreadUploadResponseDTO> uploadSubDrawing(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        @RequestBody ProofreadPipingDrawingUploadDTO uploadDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-proofread/" +
            "sub-drawings/{subDrawingId}/tasks/{taskId}/opinions/{opinionId}/replies",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<SubDrawingReviewOpinion> createOpinionReply(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subDrawingId") Long subDrawingId,
        @PathVariable("taskId") Long taskId,
        @PathVariable("opinionId") Long opinionId,
        @RequestBody SubDrawingReviewOpinionDTO dto
    );
}
