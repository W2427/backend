package com.ose.tasks.api.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.TaskPrivilegeDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-list")
public interface DrawingListAPI {

    /**
     * 创建生产设计图纸清单条目-piping
     */
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<Drawing> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingDTO pipingDTO
    );

    /**
     * 修改生产设计图纸清单条目-piping
     */
    @RequestMapping(
        method = POST,
        value = "/{id}"
    )
    JsonObjectResponseBody<Drawing> edit(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingDTO pipingDTO
    );

    /**
     * 修改生产设计图纸清单条目-piping
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/version"
    )
    JsonObjectResponseBody<Drawing> editVersion(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingDTO pipingDTO
    );

    /**
     * 获取图纸清单列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingWorkHourDTO> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page,
        DrawingCriteriaDTO criteriaDTO
    );

    /**
     * 获取图纸参数列表
     *
     * @return 参数列表
     */
    @RequestMapping(
        method = GET,
        value = "/condition"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingCriteriaDTO> getConditionList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 获取图纸清单列表(包含deleted)
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<Drawing> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 获取图纸清单列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "/categories"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntitySubType> getDrawingCategoryList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 获取图纸清单列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "/users/{assigneeId}/drawings"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingWorkHourDTO> listByAssignee(@PathVariable Long assigneeId, PageDTO page, DrawingCriteriaDTO criteriaDTO);

    /**
     * 获取图纸清单列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "/drawing-disciplines"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingFilterDTO> getDrawingDisciplines(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 获取图纸清单列表
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "/drawing-doc-types"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingFilterDTO> getDrawingDocTypes(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 获取生产设计图纸清单条目-piping
     */
    @RequestMapping(
        method = GET,
        value = "/{id}"
    )
    JsonObjectResponseBody<Drawing> detail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取生产设计图纸清单条目-piping
     */
    @RequestMapping(
        method = GET,
        value = "/{id}/uncheck"
    )
    JsonObjectResponseBody<Drawing> detailUncheck(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );


    /**
     * 删除图纸清单
     *
     * @param id 工序分类信息
     * @return 工序分类信息
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/delete",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingDeleteDTO drawingDeleteDTO
    );

    /**
     * 删除上传的图纸文件
     *
     * @param drawingFileId 工序分类信息
     * @return 工序分类信息
     */
    @RequestMapping(
        method = POST,
        value = "/drawing-file/{id}/delete",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteDrawingFile(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long drawingFileId,
        @RequestBody DrawingDeleteDTO drawingDeleteDTO
    );

    /**
     * 删除图纸清单
     *
     * @param id 工序分类信息
     * @return 工序分类信息
     */
    @RequestMapping(
        method = DELETE,
        value = "/{id}/physical-delete",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody physicalDelete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取图纸历史记录
     */
    @RequestMapping(
        method = GET,
        value = "/{id}/history"
    )
    JsonListResponseBody<DrawingHistory> history(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 上传图纸文件
     */
    @RequestMapping(
        method = POST,
        value = "/{id}/upload-pdf"
    )
    JsonResponseBody upload(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingUploadDTO uploadDTO
    );

    @RequestMapping(
        method = POST,
        value = "/generate-rerport"
    )
    JsonObjectResponseBody<Drawing> generateRerport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id);

    /**
     * 操作locked
     */
    @RequestMapping(
        method = POST,
        value = {
            "/{id}/lock",
            "/{id}/unlock"
        }
    )
    JsonResponseBody modifyLocked(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取设计图纸清单条目，带有各种文件信息
     */
    @RequestMapping(
        method = GET,
        value = "/{drawingId}/details"
    )
    JsonListResponseBody<DrawingDetail> detailList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
         DrawingDetailQueryDTO drawingDetailQueryDTO
    );

    /**
     * 获取所有图纸清单条目，带有各种文件信息
     */
    @RequestMapping(
        method = GET,
        value = "/{drawingId}/details-files/all"
    )
    JsonListResponseBody<DrawingDetail> allDetailFileList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        DrawingDetailQueryDTO drawingDetailQueryDTO
    );

    /**
     * 获取设计图纸清单条目，带有各种文件信息
     */
    @RequestMapping(
        method = GET,
        value = "/{drawingId}/detail-files"
    )
    JsonListResponseBody<DrawingDetail> detailFileList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        DrawingDetailQueryDTO drawingDetailQueryDTO
    );

    @RequestMapping(
        method = GET,
        value = "/drawing-categories"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmEntitySubType> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );


    /**
     * 图纸审查
     */
    @RequestMapping(
        method = POST,
        value = {
            "/{id}/check"
        }
    )
    JsonResponseBody check(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingProofreadDTO proofreadDTO
    );

    /**
     * RedMark图纸审查
     */
    @RequestMapping(
        method = POST,
        value = {
            "/{id}/red-mark/check"
        }
    )
    JsonResponseBody redMarkCheck(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingProofreadDTO proofreadDTO
    );

    @RequestMapping(
        method = GET,
        value = "/{drawingId}/upload-zip-file-history"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingUploadZipFileHistory> zipFileHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        PageDTO page
    );

    @RequestMapping(
        method = GET,
        value = "/{drawingId}/upload-zip-file-history/{id}/detail"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        @PathVariable("id") Long id,
        PageDTO page
    );

    @RequestMapping(
        method = GET,
        value = "/{qrCode}/check-issue"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody checkIssue(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("qrCode") String qrCode
    );

    /**
     * 创建图纸工作流实例
     */
    @RequestMapping(
        method = GET,
        value = {
            "/{id}/create-task-info"
        }
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<DrawingCreateTaskInfoDTO> getCreateTaskInfo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 上传子图纸文件
     */
    @RequestMapping(
        method = POST,
        value = "/upload/file"
    )
    JsonObjectResponseBody<UploadDrawingFileResultDTO> uploadSubFile(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO
    );


    @RequestMapping(
        method = GET,
        value = "/{id}/privileges"
    )
    JsonListResponseBody<TaskPrivilegeDTO> getProcessPrivileges(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    @RequestMapping(
        method = POST,
        value = "/{id}/privileges"
    )
    @ResponseStatus(OK)
    JsonResponseBody setProcessPrivileges(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody TaskPrivilegeDTO DTO
    );

    @RequestMapping(
        method = GET,
        value = "/{id}/check-up-version/{version}"
    )
    @ResponseStatus(OK)
    JsonResponseBody checkUpVersion(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @PathVariable("version") String version
    );

    @RequestMapping(
        method = POST,
        value = "/{id}/check-lock"
    )
    @ResponseStatus(OK)
    JsonResponseBody checkLock(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );


    /**
     * 上传子图纸文件
     */
    @RequestMapping(
        method = POST,
        value = "/redmark-upload"
    )
    JsonResponseBody uploadRedmarkFile(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO
    );

    /**
     * 上传子图纸文件
     */
    @RequestMapping(
        method = POST,
        value = "/redmark-upload/qrcode"
    )
    JsonResponseBody uploadRedmarkFileQrcode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO
    );

    /**
     * 导出生产设计图纸目录信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Operation(
        summary = "导出生产设计图纸目录信息"
    )
    @GetMapping(
        value = "/export-xls",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void exportPipingDrawingList(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        DrawingCriteriaDTO criteriaDTO
    ) throws IOException;

    /**
     * 上传图纸文件
     */
    @RequestMapping(
        method = POST,
        value = "/batch-upload-pdf"
    )
    JsonObjectResponseBody<UploadDrawingFileResultDTO> batchUpload(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO
    ) throws IOException;

    @Operation(summary = "创建图纸详情", description = "创建设计图详情")
    @RequestMapping(method = POST, value = "/drawing-detail", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    JsonObjectResponseBody<DrawingDetail> createDetail(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                        @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                        @RequestBody DrawingDetailCriteriaDTO detailDTO);

    /**
     * 获取生产设计图纸清单条目
     */
    @RequestMapping(
        method = GET,
        value = "/drawing-detail/{drawingDetailId}"
    )
    JsonObjectResponseBody<DrawingDetail> drawingDetail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingDetailId") Long drawingDetailId
    );

    /**
     * 获取生产设计图纸设校审人员
     */
    @RequestMapping(
        method = GET,
        value = "/drawing-delegate/{drawingId}"
    )
    JsonObjectResponseBody<DrawingDelegateDTO> drawingDelegate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId
    );

    /**
     * 获取当前项目图纸有关的专业分层树
     */
    @RequestMapping(
        method = GET,
        value = "/treeData"
    )
    List<Map<String, Object>> getDisciplineTreeData(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     *
     */
    @RequestMapping(
        method = GET,
        value = "/treeData/x-axis"
    )
    List<Map<String, Object>> getTreeXData(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId

    );

    /**
     *
     */
    @RequestMapping(
        method = GET,
        value = "/treeData/y-axis/{discipline}/{level}"
    )
    List<Map<String, Object>> getTreeYData(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("discipline") String discipline,
        @PathVariable("level") String level

    );

    /**
     * 上传图纸文件zip文件包,补录图纸
     */
    @RequestMapping(
        method = POST,
        value = "/repair-drawing"
    )
    JsonObjectResponseBody<UploadDrawingFileResultDTO> repairDrawing(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO
    ) throws IOException;
}
