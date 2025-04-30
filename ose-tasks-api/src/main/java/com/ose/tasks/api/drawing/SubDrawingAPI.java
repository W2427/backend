package com.ose.tasks.api.drawing;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.drawing.DrawingZipDetail;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.entity.drawing.SubDrawingConfig;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface SubDrawingAPI {

    /**
     * 创建ISO图纸条目
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<SubDrawing> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        @RequestBody SubDrawingDTO dto
    );

    /**
     * 修改ISO图纸条目
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}"
    )
    JsonObjectResponseBody<SubDrawing> edit(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        @PathVariable("id") Long id,
        @RequestBody SubDrawingDTO dto
    );

    /**
     * 获取ISO图纸清单
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<SubDrawing> list(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        PageDTO page,
        SubDrawingCriteriaDTO criteriaDTO
    );

    /**
     * 获取ISO子图纸清单
     *
     * @return 工序列表
     */
    @RequestMapping(
        method = GET,
        value = "sub-drawing-list"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<SubDrawing> subList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page,
        SubDrawingCriteriaDTO criteriaDTO
    );

    /**
     * 合并下载查询ISO图纸。
     *
     * @param orgId
     * @param projectId
     * @param page
     * @param criteriaDTO
     * @return
     */
    @RequestMapping(method = GET, value = "drawing-list/piping/{drawingId}/sub-drawing-list/download",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    JsonResponseBody subListDownload(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸 ID") Long drawingId,
        PageDTO page, SubDrawingCriteriaDTO criteriaDTO
    );
    /**
     * 获取ISO图纸信息
     */
    @RequestMapping(
        method = GET,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}"
    )
    JsonObjectResponseBody<SubDrawing> detail(
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
        method = DELETE,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 获取子图纸变量
     */
    @RequestMapping(
        method = GET,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/variables"
    )
    JsonListResponseBody<SubDrawingConfig> variables(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId
    );

    /**
     * 获取图纸历史记录
     */
    @RequestMapping(
        method = GET,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}/history"
    )
    JsonListResponseBody<SubDrawingHistory> history(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 删除图纸历史记录
     *
     * @param id 工序分类信息
     * @return 工序分类信息
     */
    @RequestMapping(
        method = DELETE,
        value = "drawing-list/piping/{drawingId}/sub-drawing-lis/{subId}/histories/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subId") Long subId,
        @PathVariable("id") Long id
    );

    /**
     * 上传图纸文件
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{subDrawingId}/upload-pdf"
    )
    JsonResponseBody uploadPDF(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subDrawingId") Long subDrawingId,
        @PathVariable("drawingId") Long drawingId,
        @RequestBody DrawingUploadDTO uploadDTO
    );


    /**
     * 获取子图纸变量
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{subId}/variables/{id}"
    )
    JsonResponseBody variables(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        @PathVariable("subId") Long subId,
        @PathVariable("id") Long id,
        @RequestBody SubDrawingVariableDTO dto
    );

    /**
     * 上传图纸文件
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}/upload"
    )
    JsonObjectResponseBody<UploadDrawingFileResultDTO> upload(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingUploadDTO uploadDTO
    );

    /**
     * 上传图纸文件目录
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}/upload-catalog"
    )
    JsonObjectResponseBody<UploadDrawingFileResultDTO> uploadCatalog(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody DrawingUploadDTO uploadDTO
    );

    /**
     * 指定子图纸使用文件
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{subId}/histories/{id}/use"
    )
    JsonResponseBody useHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subId") Long subId,
        @PathVariable("id") Long id
    );

    /**
     * 上传图纸文件目录
     */
    @RequestMapping(
        method = GET,
        value = "sub-drawing-list/{id}/download"
    )
    JsonObjectResponseBody<DrawingFileDTO> downloadSubDrawing(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    @RequestMapping(
        method = GET,
        value = "drawing-list/piping/{drawingId}/export-sub-drawing-list"
    )
    void exportSubDrawing(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "drawingId") Long drawingId,
        SubDrawingDownLoadDTO subDrawingDownLoadDTO) throws IOException;

    /**
     * 按条件下载子图纸zip包。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/download-zip"
    )
    JsonResponseBody startZip(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingId") Long drawingId,
        @RequestBody SubDrawingCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = GET,
        value = "zip-history"
    )
    @Operation(description = "获取打包历史表所有记录")
    JsonListResponseBody<DrawingZipDetail> search(@PathVariable("orgId") Long orgId,
                                                  @PathVariable("projectId") Long projectId,
                                                  PageDTO page);

    /**
     * 替换最新有效子图纸
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/{subDrawingId}/active"
    )
    JsonResponseBody uploadActivePDF(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("subDrawingId") Long subDrawingId,
        @PathVariable("drawingId") Long drawingId,
        @RequestBody DrawingUploadDTO uploadDTO
    );

    @RequestMapping(
        method = POST,
        value = "patch-sub-drawing"
    )
    @Operation(description = "检查子图纸二维码")
    JsonResponseBody checkSubDrawing(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId);


    @RequestMapping(
        method = POST,
        value = "patch-sub-drawing-qr-code/{once}"
    )
    @Operation(description = "更新子图纸二维码")
    JsonResponseBody patchSubDrawing(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     @PathVariable(value = "once", required = false) String once);
}
