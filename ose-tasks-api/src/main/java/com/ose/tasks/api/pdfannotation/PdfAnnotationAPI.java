package com.ose.tasks.api.pdfannotation;

import com.ose.dto.*;
import com.ose.dto.*;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequestMapping(value = "/orgs")
public interface PdfAnnotationAPI {

    /**
     * 创建PDF ANNOTATION信息。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param drawingFileId file信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateAnnotation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @RequestBody List<AnnotationResponseDTO> annotionDTOs
    );

    /**
     * 创建PDF ANNOTATION信息。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param drawingFileId file信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileId}/v1",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<AnnotationUpdateDTOV1> updateAnnotationV1(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @RequestBody AnnotationUpdateDTOV1 annotationUpdateDTOV1
    );

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
    void exportXlsxs(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId
    );


    /**
     * 导出 Pdf中的标记内容到xlsx报告。
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
    void exportXlsx(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @PathVariable("pageNo") Integer pageNo
    );

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
    JsonListResponseBody<AnnotationResponseDTO> getPageAnnotation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @PathVariable("pageNo") Integer pageNo,
        AnnotationRawLineDTO annoDTO
    );

    /**
     * 取得对应页面的PDF ANNOTATION 的历史信息。
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param drawingFileHistoryId file信息
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/pdf-annotation/{drawingFileHistoryId}/history-annotation",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<AnnotationHistoryResponseDTO> getPageAnnotationHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileHistoryId") Long drawingFileHistoryId,
        AnnotationRawLineDTO annoDTO
    );


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
    JsonListResponseBody<AnnotationResponseDTOV1> getPageAnnotation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        PageDTO pageDTO
    );

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
    JsonObjectResponseBody<AnnotationDTO> getPageBase64(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        @PathVariable("pageNo") Integer pageNo,
        AnnotationRawLineDTO annoDTO
    );

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
    JsonListResponseBody<AnnotationDTO> getPageBase64List(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("drawingFileId") Long drawingFileId,
        PageDTO PageDTO
    );


}
