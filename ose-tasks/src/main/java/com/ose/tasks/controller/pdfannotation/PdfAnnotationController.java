package com.ose.tasks.controller.pdfannotation;

import com.ose.aspect.RequestCountAspect;
import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.controller.ServerController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.*;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.pdfannotation.PdfAnnotationAPI;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingFileHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingFileRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingAnnotationInterface;
import com.ose.tasks.domain.model.service.pdfannotation.PdfAnnotationInterface;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingFile;
import com.ose.tasks.entity.drawing.DrawingFileHistory;
import com.ose.tasks.util.*;
import com.ose.util.PdfUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "PDF ANNOTATION API")
@RestController
public class PdfAnnotationController extends BaseController implements PdfAnnotationAPI {

    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${application.files.temporary}")
    private String temporaryDir;
    private final DrawingFileRepository drawingFileRepository;
    private final DrawingDetailRepository drawingDetailRepository;
    private final PdfAnnotationInterface pdfAnnotationService;
    private final UploadFeignAPI uploadFeignAPI;
    private final UserFeignAPI userFeignAPI;
    private final DrawingFileHistoryRepository drawingFileHistoryRepository;

    private final DrawingAnnotationInterface drawingAnnotationService;

    @Autowired
    public PdfAnnotationController(
        DrawingFileRepository drawingFileRepository,
        PdfAnnotationInterface pdfAnnotationService,
        UploadFeignAPI uploadFeignAPI,
        DrawingFileHistoryRepository drawingFileHistoryRepository,
        DrawingAnnotationInterface drawingAnnotationService,
        DrawingDetailRepository drawingDetailRepository,
        UserFeignAPI userFeignAPI
    ) {
        this.drawingFileRepository = drawingFileRepository;
        this.pdfAnnotationService = pdfAnnotationService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.drawingFileHistoryRepository = drawingFileHistoryRepository;
        this.drawingAnnotationService = drawingAnnotationService;
        this.drawingDetailRepository = drawingDetailRepository;
        this.userFeignAPI = userFeignAPI;
    }


    @Override
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateAnnotation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @RequestBody List<AnnotationResponseDTO> annotionDTOs
    ) {
        String fileName = "";
        DrawingFile drawingFile = drawingFileRepository.findById(drawingFileId).orElse(null);
        if (null == drawingFile) {
            throw new BusinessError("drawing file is invalid");
        }
        String outputFile = temporaryDir + drawingFile.getFileName();
        if (new Long(111111L).equals(drawingFileId)) {
            fileName = "/var/www/bpm-operation/src/assets/pdf_view/pdf-test.pdf";
        } else {
            if (drawingFile == null) {
                throw new BusinessError("there is no such drawing file");
            }
            fileName = protectedDir + drawingFile.getFilePath();// "/var/www/bpm-operation/src/assets/test.pdf";
        }
        if (fileName == null) {
            throw new BusinessError("there is no such drawing detail file");
        }
        pdfAnnotationService.updateAnnotation(orgId, projectId, fileName, annotionDTOs, null, 0, 0, 0L, null);
        return new JsonResponseBody();
    }

    @Override
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}/v1",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonObjectResponseBody<AnnotationUpdateDTOV1> updateAnnotationV1(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @RequestBody AnnotationUpdateDTOV1 annotationUpdateDTOV1
    ) {

        String fileName = "";
        Long operatorId = 0L;
        if (null != getContext().getOperator() && null != getContext().getOperator().getId()) {
            operatorId = getContext().getOperator().getId();
        }
        DrawingFile drawingFile = drawingFileRepository.findById(drawingFileId).orElse(null);
        if (null == drawingFile) {
            throw new BusinessError("drawing file is invalid");
        }

        // 判断图纸版本是否为最新版，最新版可以修改，否则请重新刷新画面。
        DrawingDetail drawingDetail = drawingDetailRepository.findById(drawingFile.getDrawingDetailId()).orElse(null);

        if (null == drawingDetail) {
            throw new BusinessError("This drawing detail ID is wrong");
        }

        if (null == drawingDetail.getPdfUpdateVersion()) {
            drawingDetail.setLastModifiedAt();
            drawingDetail.setPdfUpdateVersion(DwgRevUtils.generateDrawingDetailVersion(
                projectId,
                getContext().getOperator().getId()
            ));
            drawingDetailRepository.save(drawingDetail);
        } else {
            if (annotationUpdateDTOV1.getPdfUpdateVersion() != null && !annotationUpdateDTOV1.getPdfUpdateVersion().equals(drawingDetail.getPdfUpdateVersion())) {
                throw new BusinessError("This PDF has been update, please refresh this page.");
            } else {
                drawingDetail.setLastModifiedAt();
                drawingDetail.setPdfUpdateVersion(DwgRevUtils.generateDrawingDetailVersion(
                    projectId,
                    getContext().getOperator().getId()
                ));
                drawingDetailRepository.save(drawingDetail);
            }
        }

//        List<DrawingFileHistory> drawingFileHistories = drawingFileHistoryRepository.findByOrgIdAndProjectIdAndDrawingFileIdAndStatusOrderByCreatedAtDesc(orgId, projectId, drawingFileId, EntityStatus.ACTIVE);

        if (new Long(111111L).equals(drawingFileId)) {
            fileName = "/var/www/bpm-operation/src/assets/pdf_view/pdf-test.pdf";
        }
//        else if (!drawingFileHistories.isEmpty()) {
//            fileName = protectedDir + drawingFileHistories.get(0).getFilePath();
//        } else
        if (null != drawingFile && !StringUtils.isEmpty(drawingFile.getFilePath())) {
            fileName = protectedDir + drawingFile.getFilePath();
        } else {
            throw new BusinessError("there is no such drawing file");
        }
        if (annotationUpdateDTOV1.getTaskId() == null) {
            throw new BusinessError("Task Id should not be null");
        }
        String outputFile = temporaryDir + drawingFile.getFileName();
//        fileName = "/var/www/saint-whale/backend/private/files/7a/5b/af/da/30/b2/70/a8/0d/c6/41/bf/9a/db/1a/2e/95/9a/8f/bc.original.pdf";//ftj

        List<AnnotationResponseDTO> annotionDTOs = pdfAnnotationService.convertAnnotationUploadDto(annotationUpdateDTOV1);

        for (AnnotationResponseDTO annotionDTO : annotionDTOs) {
            if (null != annotionDTO.getUser()) {
                List<UserProfile> userProfiles = userFeignAPI.getUserByUsername(orgId, annotionDTO.getUser()).getData();
                if (!userProfiles.isEmpty()) {
                    annotionDTO.setUserId(userProfiles.get(0).getId());
                }
            }
        }
        if (annotionDTOs == null) annotionDTOs = new ArrayList<>();
        Integer start = annotationUpdateDTOV1.getStart();
        Integer limit = annotationUpdateDTOV1.getLimit();
        Map<Integer, Set<String>> pageAnnoIdMap = new HashMap<>();
        Map<String, Long> pageUserNameUserIdMap = new HashMap<>();
        annotionDTOs.forEach(annot -> {
            pageAnnoIdMap.computeIfAbsent(Integer.parseInt(annot.getPageNo()), k -> new HashSet<>()).add(annot.getId());
            pageUserNameUserIdMap.computeIfAbsent(annot.getUser(), k -> annot.getUserId());
        });
        for (int i = start; i < start + limit; i++) {
            pageAnnoIdMap.computeIfAbsent(i, k -> new HashSet<>());
        }
        //更新 annotation到pdf文件
//        Map<Integer, PageInfo> pageInfoMap = new HashMap<>();
        Map<Integer, PageAnnoInfo> pageAnnotations = pdfAnnotationService.updateAnnotation(orgId, projectId, fileName, annotionDTOs, pageAnnoIdMap, start, limit, operatorId, pageUserNameUserIdMap);
        //把annotation 存储到 数据库表
        pdfAnnotationService.saveAnnotation2Db(drawingFile, pageAnnotations, annotationUpdateDTOV1.getTaskId(), annotationUpdateDTOV1.getProcInstId());
        AnnotationUpdateDTOV1 annotationUpdateResponse = new AnnotationUpdateDTOV1();
        annotationUpdateResponse.setPdfUpdateVersion(drawingDetail.getPdfUpdateVersion());

        return new JsonObjectResponseBody<>(
            getContext(),
            annotationUpdateResponse
        );
    }

    /**
     * 取得对应页面的PDF ANNOTATION 的历史信息。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param drawingFileHistoryId file信息
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileHistoryId}/history-annotation",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonListResponseBody<AnnotationHistoryResponseDTO> getPageAnnotationHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileHistoryId") Long drawingFileHistoryId,
        AnnotationRawLineDTO annoDTO
    ) {
        Integer pageNo = annoDTO.getPageNo();
        if (pageNo == null) throw new BusinessError("Please input page no");

//        DrawingFileHistory dfh = drawingFileHistoryRepository.findById(drawingFileHistoryId).orElse(null);
//        if(dfh == null) {throw new BusinessError("There is no such file");}
        List<AnnotationHistoryResponseDTO> annotationHistoryResponseDTOS =
            drawingAnnotationService.getAnnotationHistory(projectId, drawingFileHistoryId, pageNo);
        return new JsonListResponseBody<>(annotationHistoryResponseDTOS);
    }

    /**
     * 取得对应页面的PDF ANNOTATION信息。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param drawingFileId file信息
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}/{pageNo}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonListResponseBody<AnnotationResponseDTO> getPageAnnotation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @PathVariable("pageNo") Integer pageNo,
        AnnotationRawLineDTO annoDTO
    ) {
        String fileName = "";
        DrawingFile drawingFile = drawingFileRepository.findById(drawingFileId).orElse(null);
        if (new Long(111111L).equals(drawingFileId)) {
            fileName = "/var/www/bpm-operation/src/assets/pdf_view/pdf-test.pdf";
        } else {
            if (drawingFile == null) {
                throw new BusinessError("there is no such drawing file");
            }
            fileName = protectedDir + drawingFile.getFilePath();// "/var/www/bpm-operation/src/assets/test.pdf";
            if (fileName == null) {
                throw new BusinessError("there is no such drawing detail file");
            }
        }
        List<AnnotationResponseDTO> list = new ArrayList<>();
        list = PdfAnnotationUtil.getPageAnnotation(fileName, pageNo);

        list = pdfAnnotationService.combineAnnotation(orgId, projectId, list);

        return new JsonListResponseBody<>(list);
    }


    /**
     * 取得对应页面的PDF ANNOTATION信息。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param drawingFileId file信息
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonListResponseBody<AnnotationResponseDTOV1> getPageAnnotation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        PageDTO pageDTO
    ) {
        String fileName = "";
        DrawingFile drawingFile = drawingFileRepository.findById(drawingFileId).orElse(null);
        if (new Long(111111L).equals(drawingFileId)) {
//            fileName = "/var/www/bpm-operation/src/assets/pdf_view/pdf-test.pdf";
            fileName = "/var/www/saint-whale/backend/private/files/06/2d/b0/d3/54/a8/45/14/80/67/06/43/69/06/09/c6/e8/1d/23/be.original.pdf";
        } else {
            if (drawingFile == null) {
                DrawingFileHistory drawingFileHistory = drawingFileHistoryRepository.findById(drawingFileId).orElse(null);
                if (drawingFileHistory == null) {
                    throw new BusinessError("there is no such drawing file");
                } else {
                    fileName = protectedDir + drawingFileHistory.getFilePath();
                }
            } else {
                fileName = protectedDir + drawingFile.getFilePath();// "/var/www/bpm-operation/src/assets/test.pdf";
            }
            if (fileName == null) {
                throw new BusinessError("there is no such drawing detail file");
            }
        }
        System.out.println("pdf file name is: " + fileName);
//        fileName = "/var/www/saint-whale/backend/private/files/7a/5b/af/da/30/b2/70/a8/0d/c6/41/bf/9a/db/1a/2e/95/9a/8f/bc.original.pdf";//ftj
        List<AnnotationResponseDTOV1> list = new ArrayList<>();
        int pageNo = pageDTO.getPage().getNo();
        int pageSize = pageDTO.getPage().getSize();
        List<AnnotationResponseDTOV1> listAll = new ArrayList<>();

        list = PdfAnnotationV1Util.getPageAnnotationV1(fileName, (pageNo - 1) * pageSize + 1, pageNo * pageSize);

//            list = pdfAnnotationService.combineAnnotation(orgId, projectId, list);
        listAll.addAll(list);
        for (AnnotationResponseDTOV1 annotionDTO : listAll) {
            if (null != annotionDTO.getUser()) {
                List<UserProfile> userProfiles = userFeignAPI.getUserByUsername(orgId, annotionDTO.getUser()).getData();
                if (!userProfiles.isEmpty()) {
                    annotionDTO.setUserId(userProfiles.get(0).getId());
                } else {
                    userProfiles = userFeignAPI.getUserByUsername(orgId, "admin").getData();
                    annotionDTO.setUserId(userProfiles.isEmpty() ? null : userProfiles.get(0).getId());
                }
            }
        }

        return new JsonListResponseBody<>(listAll);
    }

    /**
     * 取得对应页面的PDF ANNOTATION信息。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param drawingFileId file信息
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}/base64/{pageNo}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<AnnotationDTO> getPageBase64(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @PathVariable("pageNo") Integer pageNo,
        AnnotationRawLineDTO annoDTO
    ) {
        String fileName = "";
        DrawingFile drawingFile = drawingFileRepository.findById(drawingFileId).orElse(null);
        if (new Long(111111L).equals(drawingFileId)) {
            fileName = "/var/www/bpm-operation/src/assets/pdf_view/pdf-test.pdf";
        } else {
            if (drawingFile == null) {
                throw new BusinessError("there is no such drawing file");
            }
            fileName = protectedDir + drawingFile.getFilePath();// "/var/www/bpm-operation/src/assets/test.pdf";
        }
        if (fileName == null) {
            throw new BusinessError("there is no such drawing detail file");
        }
        AnnotationDTO annotationDTO = new AnnotationDTO();
        if (pageNo == null) pageNo = 1;

        annotationDTO.setBase64(PdfUtils.pdfEncoder(fileName, pageNo - 1, temporaryDir));
        annotationDTO.setPageTotal(PdfUtils.getPdfPageCount(fileName));
        annotationDTO.setCurrentPage(pageNo);
        return new JsonObjectResponseBody<>(annotationDTO);
    }

    /**
     * 导出 Pdf中的标记内容到xlsx报告。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}/xlsxs",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public void exportXlsxs(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId
    ) {
        ContextDTO contextDTO = getContext();
        File excel = pdfAnnotationService.generateCommentXlsxs(orgId, projectId, drawingFileId);
        if (excel == null) throw new BusinessError("FILE is EMPTY");

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=" + excel.getName() + ".xlsx"
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("", "文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("", "下载文件出错");
        }

        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出 Pdf中的pageNo页标记内容到xlsx报告。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 实体列表
     */
    @RequestMapping(
        method = GET,
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}/xlsx/{pageNo}",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public void exportXlsx(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @PathVariable("pageNo") Integer pageNo
    ) {
        ContextDTO contextDTO = getContext();
        File excel = pdfAnnotationService.generateCommentXlsx(orgId, projectId, drawingFileId, pageNo);
        if (excel == null) throw new BusinessError("FILE is EMPTY");

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=" + excel.getName() + ".xlsx"
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("", "文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("", "下载文件出错");
        }

        try {
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取得图纸文件base64格式图纸信息。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param drawingFileId file信息
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}/base64",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    public JsonListResponseBody<AnnotationDTO> getPageBase64List(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        PageDTO PageDTO
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            pdfAnnotationService.getPageBase64List(orgId, projectId, drawingFileId, PageDTO)
        );
    }
}
