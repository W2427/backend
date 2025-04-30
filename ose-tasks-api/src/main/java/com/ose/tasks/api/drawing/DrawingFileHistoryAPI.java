package com.ose.tasks.api.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingFileHistorySearchDTO;
import com.ose.tasks.entity.drawing.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface DrawingFileHistoryAPI {
    /**
     * 获取图纸文件历史
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-detail/{drawingDetailId}/drawing-file-history"
    )
    JsonListResponseBody<DrawingFileHistory> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingDetailId") Long drawingDetailId,
        DrawingFileHistorySearchDTO criteriaDTO
    );

    /**
     * 获取图纸文件历史详情
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-file-history/{drawingFileHistoryId}"
    )
    JsonObjectResponseBody<DrawingFileHistory> detail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileHistoryId") Long drawingFileHistoryId
    );

    /**
     * 获取设计图纸文件历史详情 by drawingFileId
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-file-history/{drawingFileId}/proc-inst-id/{procInstId}"
    )
    JsonListResponseBody<DrawingFileHistory> getDrawingFileHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @PathVariable("procInstId") Long procInstId
    );

    /**
     * 返回文件历史文件流信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingFileHistoryId 文件历史ID
     */
    @Operation(
        summary = "返回文件历史文件流信息"
    )
    @GetMapping(
        value = "/orgs/{orgId}/projects/{projectId}/drawing-file-history/{drawingFileHistoryId}/preview",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void downloadFileHistory(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "文件历史ID") Long drawingFileHistoryId
    ) throws IOException;

}
