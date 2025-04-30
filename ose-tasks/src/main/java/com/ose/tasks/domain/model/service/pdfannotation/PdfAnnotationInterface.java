package com.ose.tasks.domain.model.service.pdfannotation;

import com.ose.dto.AnnotationDTO;
import com.ose.dto.AnnotationResponseDTO;
import com.ose.dto.AnnotationUpdateDTOV1;
import com.ose.dto.PageDTO;
import com.ose.dto.*;
import com.ose.tasks.entity.drawing.DrawingFile;
import com.ose.tasks.util.PageAnnoInfo;
import com.ose.tasks.util.PageInfo;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.springframework.data.domain.Page;

import java.io.File;
import java.lang.reflect.AnnotatedType;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface PdfAnnotationInterface {

    /**
     * 更新PDF ANNOTATION信息。
     *
     * @param fileName     文件名
     * @param annotionDTOs ANNOTATION信息
     */
    Map<Integer, PageAnnoInfo> updateAnnotation(
        Long orgId,
        Long projectId,
        String fileName,
        List<AnnotationResponseDTO> annotionDTOs,
        Map<Integer, Set<String>> pageAnnoIdMap,
        Integer start,
        Integer limit,
        Long operatorId,
        Map<String, Long> pageUserNameUserIdMap);

    /**
     * 生成 PDF文件的Annottion的xlsx
     *
     * @param orgId
     * @param projectId
     * @param drawingFileId
     * @return
     */
    File generateCommentXlsxs(Long orgId, Long projectId, Long drawingFileId);

    File generateCommentXlsx(Long orgId, Long projectId, Long drawingFileId, Integer pageNo);

    List<AnnotationResponseDTO> combineAnnotation(Long orgId, Long projectId, List<AnnotationResponseDTO> list);

    Page<AnnotationDTO> getPageBase64List(Long orgId, Long projectId, Long drawingFileId, PageDTO pageDTO);

    /**
     * 转换 ANNOTATION更新信息。
     *
     * @param annotationUpdateDTOV1 ANNOTATION信息
     */
    List<AnnotationResponseDTO> convertAnnotationUploadDto(
        AnnotationUpdateDTOV1 annotationUpdateDTOV1
    );

    /**
     * 存储 annotation到db的表
     * @param drawingFile
     * @param pageAnnotations
     * @param taskId
     * @param procInstId
     */
    void saveAnnotation2Db(DrawingFile drawingFile, Map<Integer, PageAnnoInfo> pageAnnotations, Long taskId, Long procInstId);
}
