package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.api.drawing.DrawingFileHistoryAPI;
import com.ose.tasks.domain.model.service.drawing.*;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingFileHistorySearchDTO;
import com.ose.tasks.entity.drawing.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "图纸文件历史接口")
@RestController
public class DrawingFileHistoryController extends BaseController implements DrawingFileHistoryAPI {
    private final static Logger logger = LoggerFactory.getLogger(DrawingFileHistoryController.class);
    private final DrawingFileHistoryInterface drawingFileHistoryService;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingFileHistoryController(
        DrawingFileHistoryInterface drawingFileHistoryService
    ) {
        this.drawingFileHistoryService = drawingFileHistoryService;
    }

    /**
     * 获取设计图纸文件历史
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-detail/{drawingDetailId}/drawing-file-history"
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<DrawingFileHistory> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingDetailId") Long drawingDetailId,
        DrawingFileHistorySearchDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>(getContext(),
            drawingFileHistoryService.search(orgId, projectId, drawingDetailId, criteriaDTO));
    }


    /**
     * 获取设计图纸文件历史详情
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-file-history/{drawingFileHistoryId}"
    )
    @WithPrivilege
    @Override
    public JsonObjectResponseBody<DrawingFileHistory> detail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileHistoryId") Long drawingFileHistoryId
    ) {
        return new JsonObjectResponseBody<>(getContext(),
            drawingFileHistoryService.detail(orgId, projectId, drawingFileHistoryId));
    }

    /**
     * 获取设计图纸文件历史详情 by drawingFileId
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/drawing-file-history/{drawingFileId}/proc-inst-id/{procInstId}"
    )
//    @WithPrivilege
    @Override
    public JsonListResponseBody<DrawingFileHistory> getDrawingFileHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @PathVariable("procInstId") Long procInstId
    ) {
        List<DrawingFileHistory> his = drawingFileHistoryService.getDrawingFileHistory(orgId, projectId, drawingFileId, procInstId);
        his = his.stream()
            .filter(history -> {
                String filePath = history.getFilePath();
                return filePath.endsWith("pdf") || filePath.endsWith("PDF");
            })
            .collect(Collectors.toList());
        return new JsonListResponseBody<>(his);
    }

    /**
     * 导出生产设计图纸目录信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @Operation(
        summary = "返回文件历史文件流信息"
    )
    @GetMapping(
        value = "/orgs/{orgId}/projects/{projectId}/drawing-file-history/{drawingFileHistoryId}/preview",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public void downloadFileHistory(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "文件历史ID") Long drawingFileHistoryId
    ) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File file = drawingFileHistoryService.saveDownloadFile(orgId, projectId, drawingFileHistoryId, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType("application/pdf");

            response.setHeader("Content-Disposition", "inline");

            IOUtils.copy(
                new FileInputStream(file), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("", "文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("", "下载文件出错");
        }

        response.flushBuffer();
    }

}
