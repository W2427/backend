package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.SubDrawingAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingZipInterface;
import com.ose.tasks.domain.model.service.drawing.SubDrawingInterface;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.util.CryptoUtils;
import com.ose.util.FileUtils;
import com.ose.util.PdfUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ose.tasks.vo.bpm.BpmCode.FILE_TYPE_PDF;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "管道生产设计子图纸清单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class SubDrawingController extends BaseController implements SubDrawingAPI {


    private SubDrawingInterface subDrawingService;

    private ProjectInterface projectService;

    private final DrawingBaseInterface drawingBaseService;

    private DrawingZipInterface drawingZipHistoryService;

    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    /**
     * 构造方法
     */
    @Autowired
    public SubDrawingController(SubDrawingInterface subDrawingService, DrawingZipInterface drawingZipHistoryService,
                                ProjectInterface projectService, DrawingBaseInterface drawingBaseService) {
        this.subDrawingService = subDrawingService;
        this.projectService = projectService;

        this.drawingBaseService = drawingBaseService;

        this.drawingZipHistoryService = drawingZipHistoryService;
    }

    @Override
    @Operation(summary = "创建ISO图纸条目", description = "创建ISO图纸条目")
    @RequestMapping(method = POST, value = "drawing-list/piping/{drawingId}/sub-drawing-list",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<SubDrawing> create(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                     @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                     @PathVariable @Parameter(description = "drawingId") Long drawingId,
                                                     @RequestBody SubDrawingDTO dto) {
        if (dto.getActInstId() == null || dto.getActInstId().equals("")) {
            throw new NotFoundError("actInstId is null");
        }
        Long userid = getContext().getOperator().getId();


        subDrawingService.checkSubDrawingNo(
            orgId,
            projectId,
            dto.getSubDrawingNo(),
            dto.getPageNo(),
            dto.getDrawingDetailId(),
            drawingId);

        SubDrawing subDrawing = subDrawingService.create(orgId, projectId, drawingId, userid, dto);

        if (dto.getVariables() != null && !dto.getVariables().isEmpty()) {
            List<Map<String, String>> variables = dto.getVariables();
            for (Map<String, String> v : variables) {
                v.put("id", CryptoUtils.uniqueId().toUpperCase());
            }
            subDrawing.setConfigData(StringUtils.toJSON(variables));
        }
        subDrawingService.save(subDrawing);

        return new JsonObjectResponseBody<>(getContext(), subDrawing);
    }

    @Override
    @Operation(summary = "修改ISO图纸清单条目", description = "修改ISO图纸清单条目")
    @RequestMapping(method = POST, value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<SubDrawing> edit(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                   @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                   @PathVariable @Parameter(description = "图纸 ID") Long drawingId,
                                                   @PathVariable @Parameter(description = "子图纸ID") Long id,
                                                   @RequestBody SubDrawingDTO dto) {

        if (dto.getActInstId() == null || dto.getActInstId().equals("")) {
            throw new NotFoundError("actInstId is null");
        }

        Long userid = getContext().getOperator().getId();

        SubDrawing subDrawing = subDrawingService.modify(orgId, projectId, drawingId, id, userid, dto);

        if (dto.getVariables() != null && !dto.getVariables().isEmpty()) {
            List<Map<String, String>> variables = dto.getVariables();
            for (Map<String, String> v : variables) {
                if (!v.containsKey("id")) {
                    v.put("id", CryptoUtils.uniqueId().toUpperCase());
                }
            }
            subDrawing.setConfigData(StringUtils.toJSON(variables));
            subDrawingService.save(subDrawing);
        }

        drawingBaseService.clearParentQrCode(id);

        return new JsonObjectResponseBody<>(getContext(), subDrawing);
    }

    @Override
    @Operation(summary = "查询ISO图纸清单列表", description = "查询ISO图纸清单列表")
    @RequestMapping(method = GET, value = "drawing-list/piping/{drawingId}/sub-drawing-list",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<SubDrawing> list(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                 @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                 @PathVariable @Parameter(description = "图纸 ID") Long drawingId,
                                                 PageDTO page, SubDrawingCriteriaDTO criteriaDTO
    ) {
        if (criteriaDTO.isFetchAll()) {
            page.setFetchAll(true);
        }
        if (criteriaDTO.getProcess() != null && criteriaDTO.getProcess().replace("_", "").contains("REDMARK")) {
            if (StringUtils.isEmpty(criteriaDTO.getProcess())) {
                return new JsonListResponseBody<>();
            }
        }
        return new JsonListResponseBody<>(getContext(),
            subDrawingService.getList(orgId, projectId, drawingId, page, criteriaDTO));
    }

    @Override
    @Operation(summary = "查询ISO图纸所有子图纸清单列表", description = "查询ISO图纸所有子图纸清单列表")
    @RequestMapping(method = GET, value = "sub-drawing-list",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<SubDrawing> subList(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                    @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                    PageDTO page, SubDrawingCriteriaDTO criteriaDTO) {
        if (criteriaDTO.getProcess() != null && criteriaDTO.getProcess().replace("_", "").contains("REDMARK")) {
            if (StringUtils.isEmpty(criteriaDTO.getProcess())) {
                return new JsonListResponseBody<>();
            }
        }
        return new JsonListResponseBody<>(getContext(),
            subDrawingService.getSubList(orgId, projectId, page, criteriaDTO)
        );
    }

    @Override
    @Operation(summary = "合并下载查询ISO图纸", description = "合并下载查询ISO图纸")
    @RequestMapping(method = GET, value = "drawing-list/piping/{drawingId}/sub-drawing-list/download",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody subListDownload(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸 ID") Long drawingId,
        PageDTO page, SubDrawingCriteriaDTO criteriaDTO
    ) {
        if (criteriaDTO.getProcess() != null && criteriaDTO.getProcess().replace("_", "").contains("REDMARK")) {
            if (StringUtils.isEmpty(criteriaDTO.getProcess())) {
                return new JsonObjectResponseBody<>();
            }
        }
        page.setFetchAll(true);
        subDrawingService.downSubList(orgId, projectId, drawingId, page, criteriaDTO,getContext().getOperator(), getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "获取ISO图纸清单条目详细信息", description = "获取ISO图纸清单条目详细信息")
    @RequestMapping(method = GET, value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<SubDrawing> detail(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                     @PathVariable @Parameter(description = "项目 ID") Long projectId, @PathVariable @Parameter(description = "条目ID") Long id) {
        SubDrawing subDrawing = subDrawingService.get(orgId, projectId, id);
        if (subDrawing.getConfigData() != null) {
            try {
                List<Map<String, Long>> le = StringUtils.fromJSON(subDrawing.getConfigData(), List.class);
                subDrawing.setVariables(le);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
        return new JsonObjectResponseBody<>(getContext(), subDrawing);
    }

    @Override
    @Operation(summary = "删除ISO图纸清单条目", description = "删除ISO图纸清单条目")
    @RequestMapping(method = DELETE, value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody delete(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                   @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                   @PathVariable @Parameter(description = "条目ID") Long id) {
        subDrawingService.delete(orgId, projectId, id);

        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "获取子图纸变量", description = "获取子图纸变量")
    @RequestMapping(method = GET, value = "drawing-list/piping/{drawingId}/sub-drawing-list/variables",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<SubDrawingConfig> variables(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                            @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                            @PathVariable @Parameter(description = "图纸 ID") Long drawingId) {
        return new JsonListResponseBody<>(getContext(),
            subDrawingService.getVariables(orgId, projectId, drawingId));
    }

    @Override
    @Operation(summary = "获取子图纸历史记录", description = "获取子图纸历史记录")
    @RequestMapping(method = GET, value = "drawing-list/piping/{drawingId}/sub-drawing-list/{id}/history",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<SubDrawingHistory> history(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                           @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                           @PathVariable @Parameter(description = "子图纸 ID") Long id) {
        return new JsonListResponseBody<>(getContext(),
            subDrawingService.getHistory(orgId, projectId, id));
    }

    @Override
    @Operation(summary = "上传图纸文件", description = "上传图纸文件")
    @RequestMapping(method = POST, value = "drawing-list/piping/{drawingId}/sub-drawing-list/{subDrawingId}/upload-pdf",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody uploadPDF(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子条目ID") Long subDrawingId,
        @PathVariable @Parameter(description = "条目ID") Long drawingId,
        @RequestBody DrawingUploadDTO uploadDTO) {

        if (uploadDTO.getFileName() == null) {
            throw new ValidationError("Please upload the drawing file.");
        }
        /*boolean actInst = subDrawingService.checkActivity(orgId,projectId,drawingId);
        if(!actInst) {
            throw new ValidationError("Please run the activity.");
        }*/
        String temporaryFileName = uploadDTO.getFileName();

        File diskFile = new File(temporaryDir, temporaryFileName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);

        String uploadFileName = metadata.getFilename();

        Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + uploadDTO.getFileName());
        boolean result = false;
        Drawing draw = drawingBaseService.getDetailedDrawing(orgId, projectId, drawingId);
//        if (draw.getCurrentProcessNameEn().equals(BpmCode.ENGINEERING)) {
//
//            result = subDrawingService.uploadDrawingSubPipingPdf(orgId, projectId, uploadFileName, drawingId,
//                subDrawingId, getContext(), uploadDTO, false);
//        } else if (draw.getCurrentProcessNameEn().equals(BpmCode.DRAWING_PARTIAL_UPDATE) || draw.getCurrentProcessNameEn().equals(BpmCode.DRAWING_INTEGRAL_UPDATE)) {
//
//            result = subDrawingService.uploadUpdateDrawingSubPipingPdf(orgId, projectId, uploadFileName, drawingId,
//                subDrawingId, getContext(), uploadDTO, false);
//        } else {
//            throw new ValidationError("设计出图或升班，字段nameEn不匹配.");
//        }

        if (!result) {
            if (pageCounts != 1) {
                throw new ValidationError("Upload file page count error.");
            } else {
                throw new ValidationError("File name does not match the record.");
            }
        }

        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "编辑子图纸变量", description = "编辑子图纸变量")
    @RequestMapping(method = POST, value = "drawing-list/piping/{drawingId}/sub-drawing-list/{subId}/variables/{id}",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody variables(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                      @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                      @PathVariable @Parameter(description = "图纸 ID") Long drawingId,
                                      @PathVariable @Parameter(description = "子图纸 ID") Long subId,
                                      @PathVariable @Parameter(description = "变量 ID") Long id,
                                      @RequestBody SubDrawingVariableDTO dto) {
        SubDrawing subDrawing = subDrawingService.findDrawingSubPipingBySubId(subId);
        if (subDrawing != null) {
            try {
                if (subDrawing.getConfigData() != null && !subDrawing.getConfigData().isEmpty()) {
                    List<Map<String, Long>> le;
                    le = StringUtils.fromJSON(subDrawing.getConfigData(), List.class);
                    for (Map<String, Long> v : le) {
                        if (v.containsKey("id")) {
                            if (v.get("id").equals(id)) {
                                Map<String, Long> ne = dto.getVariable();
                                int index = le.indexOf(v);
                                le.remove(index);
                                ne.put("id", id);
                                le.add(index, ne);
                                break;
                            }
                        }
                    }
                    subDrawing.setConfigData(StringUtils.toJSON(le));
                    subDrawingService.save(subDrawing);

                    drawingBaseService.clearParentQrCode(subId);
                }
            } catch (IOException e) {

                e.printStackTrace(System.out);
            }
        }
        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "删除ISO图纸清单条目", description = "删除ISO图纸清单条目")
    @RequestMapping(method = DELETE, value = "drawing-list/piping/{drawingId}/sub-drawing-list/{subId}/histories/{id}",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody deleteHistory(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                          @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                          @PathVariable @Parameter(description = "子图纸ID") Long subId,
                                          @PathVariable @Parameter(description = "ID") Long id) {
        SubDrawing subDrawing = subDrawingService.findDrawingSubPipingBySubId(subId);
        SubDrawingHistory targetHis = subDrawingService.getHistoryById(id);
        if (targetHis != null) {
            targetHis.setStatus(EntityStatus.DELETED);
            targetHis.setLastModifiedAt();
            subDrawingService.save(targetHis);
            if (targetHis.isUsed()) {
                subDrawing.setFileId(null);
                subDrawing.setFileName(null);
                subDrawing.setFilePath(null);
                subDrawing.setPageCount(0);
                subDrawingService.save(subDrawing);
                drawingBaseService.clearParentQrCode(subId);
            }
        }
        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "上传图纸文件zip文件包", description = "上传图纸文件zip文件包")
    @RequestMapping(method = POST, value = "drawing-list/piping/{drawingId}/sub-drawing-list/upload",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<UploadDrawingFileResultDTO> upload(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                                                     @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                                                     @PathVariable @Parameter(description = "drawingId") Long drawingId,
                                                                     @RequestBody DrawingUploadDTO uploadDTO) {

        String temporaryFileName = uploadDTO.getFileName();


        File diskFile = new File(temporaryDir, temporaryFileName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);

        String uploadFileName = metadata.getFilename();
        String fileType = FileUtils.extname(uploadFileName);

        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();
        if (fileType.toLowerCase().equals("." + FILE_TYPE_PDF)) {
            boolean result = subDrawingService.uploadDrawingSubPipingPdf(orgId, projectId, uploadFileName,
                drawingId, null, getContext(), uploadDTO, false);
            if (!result) {
                dto.setErrorCount(1);
                List<String> noMatchFile = new ArrayList<>();
                noMatchFile.add(uploadFileName);
                dto.setErrorList(noMatchFile);
                dto.setSuccessCount(0);
            }
        } else if (fileType.toLowerCase().equals("." + BpmCode.FILE_TYPE_ZIP)) {
            dto = subDrawingService.uploadDrawingSubPipingZip(orgId, projectId, uploadFileName,
                drawingId, null, getContext(), uploadDTO);
        }

        return new JsonObjectResponseBody<>(dto);
    }

    @Override
    @Operation(summary = "上传图纸文件", description = "上传图纸文件")
    @RequestMapping(method = POST, value = "drawing-list/piping/{drawingId}/sub-drawing-list/upload-catalog",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<UploadDrawingFileResultDTO> uploadCatalog(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId, @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "drawingId") Long drawingId, @RequestBody DrawingUploadDTO uploadDTO) {

        Long userid = getContext().getOperator().getId();

        UploadDrawingFileResultDTO dto = subDrawingService.uploadDrawingSubPipingCatalog(orgId, projectId, drawingId, userid, uploadDTO);

        return new JsonObjectResponseBody<>(dto);

    }

    @Override
    @Operation(summary = "获取子图纸历史记录", description = "获取子图纸历史记录")
    @RequestMapping(method = GET, value = "drawing-list/piping/{drawingId}/sub-drawing-list/{subId}/histories/{id}/use",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody useHistory(@PathVariable @Parameter(description = "所属组织 ID") Long orgId,
                                       @PathVariable @Parameter(description = "项目 ID") Long projectId,
                                       @PathVariable @Parameter(description = "子图纸ID") Long subId,
                                       @PathVariable @Parameter(description = "ID") Long id) {

        SubDrawingHistory targetHis = subDrawingService.getHistoryById(id);
        targetHis.setUsed(true);
        targetHis.setLastModifiedAt();
        subDrawingService.save(targetHis);

        subDrawingService.updateHistoryUsedFalseExcept(id, subId);

        SubDrawing subDrawing = subDrawingService.findDrawingSubPipingBySubId(subId);
        subDrawing.setFileId(targetHis.getFileId());
        subDrawing.setFileName(targetHis.getFileName());
        subDrawing.setFilePath(targetHis.getFilePath());
        subDrawing.setPageCount(targetHis.getFilePageCount());
        subDrawingService.save(subDrawing);
        drawingBaseService.clearParentQrCode(subId);

        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "获取子图纸文件id", description = "获取子图纸文件id")
    @RequestMapping(method = GET, value = "sub-drawing-list/{id}/download",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DrawingFileDTO> downloadSubDrawing(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "ID") Long id) {
        return new JsonObjectResponseBody<>(
            subDrawingService.downloadSubDrawing(id)
        );
    }

    @Override
    @Operation(summary = "导出图纸清单", description = "导出图纸清单")
    @RequestMapping(method = GET, value = "drawing-list/piping/{drawingId}/export-sub-drawing-list")
    @WithPrivilege
    public void exportSubDrawing(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "drawingId") Long drawingId,
        SubDrawingDownLoadDTO subDrawingDownLoadDTO) throws IOException {
        final Project project = projectService.get(orgId, projectId);

        File excel = subDrawingService.exportSubDrawing(
            orgId,
            projectId,
            drawingId,
            subDrawingDownLoadDTO,
            getContext().getOperator(),
            project);

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-drawing-catalog.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }

    @Override
    @Operation(summary = "上传图纸文件", description = "上传图纸文件")
    @RequestMapping(method = POST, value = "drawing-list/piping/{drawingId}/sub-drawing-list/{subDrawingId}/active",
        consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody uploadActivePDF(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子条目ID") Long subDrawingId,
        @PathVariable @Parameter(description = "条目ID") Long drawingId,
        @RequestBody DrawingUploadDTO uploadDTO) {

        if (uploadDTO.getFileName() == null) {
            throw new ValidationError("Please upload the drawing file.");
        }
        String temporaryFileName = uploadDTO.getFileName();

        File diskFile = new File(temporaryDir, temporaryFileName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);

        String uploadFileName = metadata.getFilename();

        Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + uploadDTO.getFileName());
        boolean result;
        Drawing draw = drawingBaseService.getDetailedDrawing(orgId, projectId, drawingId);
        result = subDrawingService.uploadSecondActivePDF(orgId, projectId, uploadFileName, drawingId,
            subDrawingId, getContext(), uploadDTO, false);
        if (!result) {
            if (pageCounts != 1) {
                throw new ValidationError("Upload file page count error.");
            } else {
                throw new ValidationError("File name does not match the record.");
            }
        }

        return new JsonResponseBody();
    }

    /**
     * 按条件打包子图纸zip包。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = POST,
        value = "drawing-list/piping/{drawingId}/sub-drawing-list/download-zip"
    )
    @Operation(description = "按条件下载子图纸zip包")
    @WithPrivilege
    @Override
    public JsonResponseBody startZip(@PathVariable("orgId") Long orgId,
                                     @PathVariable("projectId") Long projectId,
                                     @PathVariable("drawingId") Long drawingId,
                                     @RequestBody SubDrawingCriteriaDTO criteriaDTO) {
        drawingBaseService.startZip(orgId, projectId, drawingId, getContext().getOperator(), criteriaDTO, getContext());
        return new JsonResponseBody();
    }

    @RequestMapping(
        method = GET,
        value = "zip-history"
    )
    @Operation(description = "获取打包历史表所有记录")
    @WithPrivilege
    public JsonListResponseBody<DrawingZipDetail> search(@PathVariable("orgId") Long orgId,
                                                         @PathVariable("projectId") Long projectId,
                                                         PageDTO page) {
        return new JsonListResponseBody<>(
            drawingBaseService.search(page, orgId, projectId)
        );
    }

    @RequestMapping(
        method = POST,
        value = "patch-sub-drawing"
    )
    @Operation(description = "检查子图纸二维码")
    @WithPrivilege
    @Override
    public JsonResponseBody checkSubDrawing(@PathVariable("orgId") Long orgId,
                                            @PathVariable("projectId") Long projectId) {
        drawingBaseService.checkSubDrawing(orgId, projectId);


        return new JsonResponseBody();
    }

    @RequestMapping(
        method = POST,
        value = "patch-sub-drawing-qr-code/{once}"
    )
    @Operation(description = "更新子图纸二维码")
    @WithPrivilege
    @Override
    public JsonResponseBody patchSubDrawing(@PathVariable("orgId") Long orgId,
                                            @PathVariable("projectId") Long projectId,
                                            @PathVariable(value = "once", required = false) String once) {
        drawingBaseService.patchSubDrawingQrCode(projectId, once);


        return new JsonResponseBody();
    }
}
