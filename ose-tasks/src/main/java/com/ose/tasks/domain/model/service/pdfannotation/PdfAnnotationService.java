package com.ose.tasks.domain.model.service.pdfannotation;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ose.dto.*;
import com.ose.tasks.domain.model.repository.drawing.DrawingAnnotationRepository;
import com.ose.tasks.entity.drawing.DrawingAnnotation;
import com.ose.tasks.util.PageAnnoInfo;
import com.ose.tasks.util.PageInfo;
import com.ose.util.CollectionUtils;
import com.ose.util.CryptoUtils;
import com.ose.util.PdfUtils;
import com.ose.exception.BusinessError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.drawing.DrawingFileHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingFileRepository;
import com.ose.tasks.domain.model.repository.pdfannotation.PdfAnnotationReplyRepository;
import com.ose.tasks.domain.model.repository.pdfannotation.PdfAnnotationRepository;
import com.ose.tasks.entity.drawing.DrawingFile;
import com.ose.tasks.entity.drawing.DrawingFileHistory;
import com.ose.tasks.entity.pdfAnnotation.PdfAnnotation;
import com.ose.tasks.entity.pdfAnnotation.PdfAnnotationReply;
import com.ose.tasks.util.PdfAnnotationUtil;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLine;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.AnnotatedType;
import java.util.*;

/**
 * Func Part 专业服务。
 */
@Component
public class PdfAnnotationService extends StringRedisService implements PdfAnnotationInterface {

    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    private final PdfAnnotationRepository pdfAnnotationRepository;

    private final PdfAnnotationReplyRepository pdfAnnotationReplyRepository;

    private final DrawingFileRepository drawingFileRepository;
    private final DrawingFileHistoryRepository drawingFileHistoryRepository;

    private final DrawingAnnotationRepository drawingAnnotationRepository;

    /**
     * 构造方法。
     *
     * @param stringRedisTemplate          Redis 模板
     * @param pdfAnnotationRepository
     * @param pdfAnnotationReplyRepository
     * @param drawingAnnotationRepository
     */
    @Autowired
    public PdfAnnotationService(StringRedisTemplate stringRedisTemplate,
                                PdfAnnotationRepository pdfAnnotationRepository,
                                PdfAnnotationReplyRepository pdfAnnotationReplyRepository,
                                DrawingFileRepository drawingFileRepository,
                                DrawingFileHistoryRepository drawingFileHistoryRepository,
                                DrawingAnnotationRepository drawingAnnotationRepository) {
        super(stringRedisTemplate);
        this.pdfAnnotationRepository = pdfAnnotationRepository;
        this.pdfAnnotationReplyRepository = pdfAnnotationReplyRepository;
        this.drawingFileRepository = drawingFileRepository;
        this.drawingFileHistoryRepository = drawingFileHistoryRepository;
        this.drawingAnnotationRepository = drawingAnnotationRepository;
    }


    @Override

    public Map<Integer, PageAnnoInfo> updateAnnotation(Long orgId,
                                                       Long projectId,
                                                       String fileName,
                                                       List<AnnotationResponseDTO> annotionDTOs,
                                                       Map<Integer, Set<String>> pageAnnoIdMap,
                                                       Integer start,
                                                       Integer limit,
                                                       Long operatorId,
                                                       Map<String, Long> pageUserNameUserIdMap) {

        Map<Integer, List<AnnotationResponseDTO>> annotationMap = new HashMap<>();
        annotionDTOs.forEach(aDTO -> {
            Integer pageNo = Integer.valueOf(aDTO.getPageNo());
            if (annotationMap.containsKey(pageNo)) {
                annotationMap.get(pageNo).add(aDTO);
            } else {
                List<AnnotationResponseDTO> annotationList = new ArrayList<>();
                annotationList.add(aDTO);
                annotationMap.put(pageNo, annotationList);
            }
        });
        try {
//            Map<Integer, PageInfo> pageInfoMap = new HashMap<>();
            return PdfAnnotationUtil.saveAnnotation(fileName, annotationMap, pageAnnoIdMap, start, limit, operatorId,pageUserNameUserIdMap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File generateCommentXlsxs(Long orgId, Long projectId, Long drawingFileId) {
        List<PdfAnnotation> pas = pdfAnnotationRepository.findByProjectIdAndDocIdAndDeletedIsFalse(
            projectId,
            drawingFileId
        );
        if (CollectionUtils.isEmpty(pas)) {
            return null;
        }

        List<PdfAnnotationReply> pars = pdfAnnotationReplyRepository.findByProjectIdAndDocIdAndDeletedIsFalse(
            projectId,
            drawingFileId
        );


        //创建工作簿
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = wb.createSheet();
        String filePath = temporaryDir + CryptoUtils.shortUniqueId();
        //创建行
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("NO");
        row.createCell(1).setCellValue("STATUS");
        row.createCell(2).setCellValue("USER");
        row.createCell(3).setCellValue("DATE");
        row.createCell(4).setCellValue("CONTENT");
        row.createCell(5).setCellValue("PAGE");
        row.createCell(6).setCellValue("DOC NO");
        int rowCnt = 1;
        int paCnt = 1;
        for (PdfAnnotation pa : pas) {
            //创建行
            row = sheet.createRow(rowCnt++);
            row.createCell(0).setCellValue(paCnt++);//NO
            row.createCell(1).setCellValue(pa.getClosed() ? "CLOSED" : "OPEN");//STATUS

            row.createCell(2).setCellValue(pa.getUserName());//USER
            row.createCell(3).setCellValue(pa.getCreatedAt());//DATE
            row.createCell(4).setCellValue(pa.getComment());//CONTENT
            row.createCell(5).setCellValue(pa.getPageNo());
            row.createCell(6).setCellValue(pa.getDocNo());

            for (PdfAnnotationReply par : pars) {
                if (!par.getAnnotationUuid().equalsIgnoreCase(pa.getAnnotationUuid())) {
                    continue;
                }
                row = sheet.createRow(rowCnt++);
//                row.createCell(0).setCellValue(paCnt++);//NO
//                row.createCell(1).setCellValue(pa.getClosed()?"CLOSED":"OPEN");//STATUS

                row.createCell(2).setCellValue(par.getUserName());//USER
                row.createCell(3).setCellValue(par.getCreatedAt());//DATE
                row.createCell(4).setCellValue(par.getReply());//CONTENT
                row.createCell(5).setCellValue(pa.getPageNo());
            }
        }
        //写入文件
        try {

            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath += ".xlsx";
            FileOutputStream stream = new FileOutputStream(filePath);
            wb.write(stream);
            wb.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new File(filePath);
    }

    /**
     * 存储 annotation到db的表
     * @param drawingFile
     * @param pageAnnotations
     * @param taskId
     * @param procInstId
     */
    @Override
    public void saveAnnotation2Db(DrawingFile drawingFile, Map<Integer, PageAnnoInfo> pageAnnotations, Long taskId, Long procInstId){
        Long orgId = drawingFile.getOrgId();
        Long projectId = drawingFile.getProjectId();
//        Long procInstId =
        Long version = new Date().getTime();
        pageAnnotations.forEach((page, annotations) -> {
            PageInfo pi = annotations.getPageInfo();

            annotations.getAnnotations().forEach(anno->{

                DrawingAnnotation dwgAnno = drawingAnnotationRepository.findByProjectIdAndAnnotationId(projectId, anno.getAnnotationName());

                if(dwgAnno == null) dwgAnno = new DrawingAnnotation();
                dwgAnno.setUser(anno.getCOSObject().getNameAsString(COSName.T));
                dwgAnno.setComment(anno.getContents());
                dwgAnno.setDrawingDetailId(drawingFile.getDrawingDetailId());
                dwgAnno.setDrawingFileHistoryId(drawingFile.getId());
                dwgAnno.setOrgId(orgId);
                dwgAnno.setProjectId(projectId);
                dwgAnno.setProcInstId(procInstId);
                dwgAnno.setTaskId(taskId);
                dwgAnno.setVersion(version);
                dwgAnno.setLastModifiedAt();
                dwgAnno.setPageNo(page);
                dwgAnno.setJsonAnnotation(anno);
                dwgAnno.setAnnotationId(anno.getAnnotationName());
                dwgAnno.setPw(pi.getPw());
                dwgAnno.setPh(pi.getPh());
                dwgAnno.setPageRotation(pi.getPageRotation());
                dwgAnno.setStatus(EntityStatus.ACTIVE);

                drawingAnnotationRepository.save(dwgAnno);
            });
        });
//        dwgAnno.set
//        drawingAnnotationRepository.
    }

    @Override
    public File generateCommentXlsx(Long orgId, Long projectId, Long drawingFileId, Integer pageNo) {
        List<PdfAnnotation> pas = pdfAnnotationRepository.findByProjectIdAndDocIdAndPageNoAndDeletedIsFalse(
            projectId,
            drawingFileId,
            pageNo
        );
        if (CollectionUtils.isEmpty(pas)) {
            return null;
        }

        List<PdfAnnotationReply> pars = pdfAnnotationReplyRepository.findByProjectIdAndDocIdAndPageNoAndDeletedIsFalse(
            projectId,
            drawingFileId,
            pageNo
        );


        //创建工作簿
        XSSFWorkbook wb = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = wb.createSheet();
        String filePath = temporaryDir + CryptoUtils.shortUniqueId();
        //创建行
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("NO");
        row.createCell(1).setCellValue("STATUS");
        row.createCell(2).setCellValue("USER");
        row.createCell(3).setCellValue("DATE");
        row.createCell(4).setCellValue("CONTENT");
        row.createCell(5).setCellValue("PAGE");
        row.createCell(6).setCellValue("DOC NO");
        int rowCnt = 1;
        int paCnt = 1;
        for (PdfAnnotation pa : pas) {
            //创建行
            row = sheet.createRow(rowCnt++);
            row.createCell(0).setCellValue(paCnt++);//NO
            row.createCell(1).setCellValue(pa.getClosed() ? "CLOSED" : "OPEN");//STATUS

            row.createCell(2).setCellValue(pa.getUserName());//USER
            row.createCell(3).setCellValue(pa.getCreatedAt());//DATE
            row.createCell(4).setCellValue(pa.getComment());//CONTENT
            row.createCell(5).setCellValue(pa.getPageNo());
            row.createCell(6).setCellValue(pa.getDocNo());
            for (PdfAnnotationReply par : pars) {
                if (!par.getAnnotationUuid().equalsIgnoreCase(pa.getAnnotationUuid())) {
                    continue;
                }
                row = sheet.createRow(rowCnt++);
//                row.createCell(0).setCellValue(paCnt++);//NO
//                row.createCell(1).setCellValue(pa.getClosed()?"CLOSED":"OPEN");//STATUS

                row.createCell(2).setCellValue(par.getUserName());//USER
                row.createCell(3).setCellValue(par.getCreatedAt());//DATE
                row.createCell(4).setCellValue(par.getReply());//CONTENT
                row.createCell(5).setCellValue(pa.getPageNo());
            }
        }
        //写入文件
        try {

            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            filePath += ".xlsx";
            FileOutputStream stream = new FileOutputStream(filePath);
            wb.write(stream);
            wb.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new File(filePath);
    }

    @Override
    public List<AnnotationResponseDTO> combineAnnotation(Long orgId, Long projectId, List<AnnotationResponseDTO> list) {
        List<AnnotationResponseDTO> nList = new ArrayList<>();
        list.forEach(ann -> {
            PdfAnnotation pdfAnn = pdfAnnotationRepository.findByProjectIdAndAnnotationUuid(projectId, ann.getId());
            if (null != pdfAnn && !pdfAnn.getDeleted()) {
                ann.setContent(pdfAnn.getComment());
                List<PdfAnnotationReply> pdfAnnReplies = pdfAnnotationReplyRepository
                    .findByProjectIdAndAnnotationUuidAndDeletedIsFalse(projectId, ann.getId());
                Map<String, AnnotationReplyDTO> replyMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(pdfAnnReplies)) {
                    pdfAnnReplies.forEach(pdfAnnReply -> {
                        if (!pdfAnnReply.getDeleted()) {
                            AnnotationReplyDTO ard = new AnnotationReplyDTO();
                            ard.setAnnotationUuid(pdfAnnReply.getAnnotationUuid());
                            ard.setSeq(pdfAnnReply.getSeq());
                            ard.setDeleted(pdfAnnReply.getDeleted());
                            ard.setReply(pdfAnnReply.getReply());
                            ard.setDocId(pdfAnnReply.getDocId());
                            ard.setReplyUuid(pdfAnnReply.getReplyUuid());
                            ard.setUserId(pdfAnnReply.getUserId());
                            ard.setUserName(pdfAnnReply.getUserName());
                            replyMap.put(pdfAnnReply.getReplyUuid(), ard);
                        }
                    });
                }
                ann.setReplies(replyMap);
                nList.add(ann);
            }
        });

        return nList;
    }

    @Override
    public Page<AnnotationDTO> getPageBase64List(Long orgId, Long projectId, Long drawingFileId, PageDTO pageDTO) {
        String fileName = "";
        List<AnnotationDTO> pdfs = new ArrayList<>();
//        List<DrawingFileHistory> drawingFileHistories = drawingFileHistoryRepository.findByOrgIdAndProjectIdAndDrawingFileIdAndStatusOrderByCreatedAtDesc(orgId, projectId, drawingFileId, EntityStatus.ACTIVE);
        DrawingFile drawingFile = drawingFileRepository.findById(drawingFileId).orElse(null);
//        if (!drawingFileHistories.isEmpty()) {
//            fileName = protectedDir + drawingFileHistories.get(0).getFilePath();
//        } else
        if (null != drawingFile && !StringUtils.isEmpty(drawingFile.getFilePath())) {
            fileName = protectedDir + drawingFile.getFilePath();
        } else {
            DrawingFileHistory drawingFileHistory = drawingFileHistoryRepository.findById(drawingFileId).orElse(null);
            if(drawingFileHistory == null || StringUtils.isEmpty(drawingFileHistory.getFilePath())) {

                throw new BusinessError("there is no such drawing file");
            } else {
                fileName = protectedDir + drawingFileHistory.getFilePath();

            }
        }

        if (new Long(111111L).equals(drawingFileId)) {
            fileName = "/var/www/bpm-operation/src/assets/pdf_view/pdf-test.pdf";
        } else {
            // "/var/www/bpm-operation/src/assets/test.pdf";
        }
        if (fileName == null) {
            throw new BusinessError("there is no such drawing detail file");
        }
//        fileName = "/var/www/saint-whale/backend/private/files//7a/5b/af/da/30/b2/70/a8/0d/c6/41/bf/9a/db/1a/2e/95/9a/8f/bc.original.pdf";
        int pdfSize = PdfUtils.getPdfPageCount(fileName);
        int startIndex = (pageDTO.getPage().getNo() - 1) * pageDTO.getPage().getSize();
        int endIndex = (pageDTO.getPage().getNo()) * pageDTO.getPage().getSize() - 1;

        if (startIndex > endIndex) {
            throw new BusinessError("the startIndex must be smaller then endIndex");
        }
        // 判断是否超过所有PDF的数量
        if (startIndex > (pdfSize - 1)) {
            throw new BusinessError("the page what you want to see is more then the original size");
        }
        if (endIndex > (pdfSize - 1)) {
            endIndex = (pdfSize - 1);
        }

        for (int i = startIndex; i <= endIndex; i++) {
            AnnotationDTO annotationDTO = new AnnotationDTO();
            annotationDTO.setBase64(PdfUtils.pdfEncoder(fileName, i, temporaryDir));
            annotationDTO.setCurrentPage(i + 1);
            pdfs.add(annotationDTO);
        }
        return new PageImpl<>(pdfs, pageDTO.toPageable(), pdfSize);
    }

    /**
     * 转换 ANNOTATION更新信息。
     *
     * @param annotationUpdateDTOV1 ANNOTATION信息
     */
    public List<AnnotationResponseDTO> convertAnnotationUploadDto(
        AnnotationUpdateDTOV1 annotationUpdateDTOV1
    ) {
        List<AnnotationResponseDTO> annotationResponseDTOs = new ArrayList<>();
        // 判断数组是否为空
        if (!annotationUpdateDTOV1.getAnnotations().isEmpty()) {
            for (AnnotationUpdateItemDTOV1 annotationUpdateItemDTOV1 : annotationUpdateDTOV1.getAnnotations()) {
                // konvaString 转换成对象
                if (null == annotationUpdateItemDTOV1.getKonvaString()) {
                    continue;
                }
                AnnotationUpdateItemDTOV1.KonvaObject konvaObject = JSON.parseObject(
                    annotationUpdateItemDTOV1.getKonvaString(),
                    new TypeReference<AnnotationUpdateItemDTOV1.KonvaObject>() {
                    });

                // 判断children是否存在
                if (konvaObject.getChildren().isEmpty() || null == konvaObject.getChildren().get(0).getClassName()) {
                    continue;
                }
                // 需要返回的对象载体
                AnnotationResponseDTO annotationResponseDTO = new AnnotationResponseDTO();
                // 定位数组
                List<AnnotationPointDTO> jsonpoints = new ArrayList<>();
                // 图形类型
                if (null == annotationUpdateItemDTOV1.getSubtype()) {
                    continue;
                }
                String shapeType = "";
                shapeType = annotationUpdateItemDTOV1.getSubtype().toUpperCase();
                annotationResponseDTO.setName(konvaObject.getAttrs().getName());
//                    if("SQUARE".equalsIgnoreCase(shapeType)) shapeType = "RECT";
                switch (shapeType) {
                    case "CIRCLE":
                        AnnotationPointDTO annotationPointDTOForCircle = new AnnotationPointDTO();
                        Float circleOpacity = 1.0f;
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getOpacity()) {
                            circleOpacity = konvaObject.getChildren().get(0).getAttrs().getOpacity();
                        }
                        annotationResponseDTO.setOpacity(circleOpacity);
                        annotationResponseDTO.setId(annotationUpdateItemDTOV1.getId());
                        if (null != annotationUpdateItemDTOV1.getKonvaClientRect()) {
                            float x = 0.0f;
                            float y = 0.0f;
                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getX())) {
                                x = annotationUpdateItemDTOV1.getKonvaClientRect().getX();
                            }
                            annotationPointDTOForCircle.setX(x);
                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getY())) {
                                y = annotationUpdateItemDTOV1.getKonvaClientRect().getY();
                            }
                            annotationPointDTOForCircle.setY(y);
                            jsonpoints.add(annotationPointDTOForCircle);
                            AnnotationPointDTO annotationPointDTOForCircle2 = new AnnotationPointDTO();
                            float x1 = 0.0f;
                            float y1 = 0.0f;
                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getWidth())) {
                                x1 = x + annotationUpdateItemDTOV1.getKonvaClientRect().getWidth();
                            }
                            annotationPointDTOForCircle2.setX(x1);

                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getHeight())) {
                                y1 = y + annotationUpdateItemDTOV1.getKonvaClientRect().getHeight();
                            }
                            annotationPointDTOForCircle2.setY(y1);
                            jsonpoints.add(annotationPointDTOForCircle2);
                            annotationResponseDTO.setJsonPoints(jsonpoints);
                        }
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getStroke()) {
                            annotationResponseDTO.setPdColor(PdfAnnotationUtil.convertPDColor(konvaObject.getChildren().get(0).getAttrs().getStroke()));
                        }
                        annotationResponseDTO.setBorderStyle("S");
                        if (null != annotationUpdateItemDTOV1.getTitle()) {
                            annotationResponseDTO.setUser(annotationUpdateItemDTOV1.getTitle());
                        }
                        annotationResponseDTO.setSubject(shapeType);
                        annotationResponseDTO.setMetadata(null);
//                        annotationResponseDTO.setDeleted(false);
                        annotationResponseDTO.setPageNo(annotationUpdateItemDTOV1.getPageNumber() + "");
                        annotationResponseDTO.setType(shapeType);
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth()) {
                            annotationResponseDTO.setBordWidth(konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth().intValue());
                        } else {
                            annotationResponseDTO.setBordWidth(2);
                        }
                        annotationResponseDTOs.add(annotationResponseDTO);
                        break;
                    case "SQUARE":
                        AnnotationPointDTO annotationPointDTOForSquare = new AnnotationPointDTO();
                        Float opacity = 1.0f;
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getOpacity()) {
                            opacity = konvaObject.getChildren().get(0).getAttrs().getOpacity();
                        }
                        annotationResponseDTO.setOpacity(opacity);
                        annotationResponseDTO.setId(annotationUpdateItemDTOV1.getId());
                        if (null != annotationUpdateItemDTOV1.getKonvaClientRect()) {
                            float x = 0.0f;
                            float y = 0.0f;
                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getX())) {
                                x = annotationUpdateItemDTOV1.getKonvaClientRect().getX();
                            }
                            annotationPointDTOForSquare.setX(x);
                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getY())) {
                                y = annotationUpdateItemDTOV1.getKonvaClientRect().getY();
                            }
                            annotationPointDTOForSquare.setY(y);
                            jsonpoints.add(annotationPointDTOForSquare);
                            AnnotationPointDTO annotationPointDTOForSquare1 = new AnnotationPointDTO();
                            float x1 = 0.0f;
                            float y1 = 0.0f;
                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getWidth())) {
                                x1 = x + annotationUpdateItemDTOV1.getKonvaClientRect().getWidth();
                            }
                            annotationPointDTOForSquare1.setX(x1);

                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getHeight())) {
                                y1 = y + annotationUpdateItemDTOV1.getKonvaClientRect().getHeight();
                            }
                            annotationPointDTOForSquare1.setY(y1);
                            jsonpoints.add(annotationPointDTOForSquare1);
                            annotationResponseDTO.setJsonPoints(jsonpoints);
                        }
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getStroke()) {
                            annotationResponseDTO.setPdColor(PdfAnnotationUtil.convertPDColor(konvaObject.getChildren().get(0).getAttrs().getStroke()));
                        }
                        annotationResponseDTO.setBorderStyle("S");
                        if (null != annotationUpdateItemDTOV1.getTitle()) {
                            annotationResponseDTO.setUser(annotationUpdateItemDTOV1.getTitle());
                        }
                        annotationResponseDTO.setSubject(shapeType);
                        annotationResponseDTO.setMetadata(null);
//                        annotationResponseDTO.setDeleted(false);
                        annotationResponseDTO.setPageNo(annotationUpdateItemDTOV1.getPageNumber() + "");
                        annotationResponseDTO.setType(shapeType);
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth()) {
                            annotationResponseDTO.setBordWidth(konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth().intValue());
                        } else {
                            annotationResponseDTO.setBordWidth(2);
                        }
                        annotationResponseDTOs.add(annotationResponseDTO);
                        break;
                    case "LINE":
                        annotationResponseDTO.setId(annotationUpdateItemDTOV1.getId());

                        if (null != konvaObject.getChildren().get(0).getAttrs() && !konvaObject.getChildren().get(0).getAttrs().getPoints().isEmpty()) {
                            for (int i = 0; i < konvaObject.getChildren().get(0).getAttrs().getPoints().size(); i += 2) {
                                AnnotationPointDTO annotationPointDTOForLine = new AnnotationPointDTO();
                                Float x1 = konvaObject.getChildren().get(0).getAttrs().getPoints().get(i);
                                Float y1 = konvaObject.getChildren().get(0).getAttrs().getPoints().get(i + 1);
                                if (null != x1) {
                                    annotationPointDTOForLine.setX(x1);
                                }
                                if (null != y1) {
                                    annotationPointDTOForLine.setY(y1);
                                }
                                if (null != x1 && null != y1) {
                                    jsonpoints.add(annotationPointDTOForLine);
                                }
                            }
                            annotationResponseDTO.setJsonPoints(jsonpoints);
                        }
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getStroke()) {
                            annotationResponseDTO.setPdColor(PdfAnnotationUtil.convertPDColor(konvaObject.getChildren().get(0).getAttrs().getStroke()));
                        }
                        if (konvaObject.getChildren().get(0).getClassName().equalsIgnoreCase("ARROW")) {
                            if (annotationResponseDTO.getMetadata() == null) {
                                annotationResponseDTO.setMetadata(new HashMap<>());
                            }
                            annotationResponseDTO.getMetadata().put("LE", PDAnnotationLine.LE_OPEN_ARROW);
                        }
                        annotationResponseDTO.setBorderStyle("S");
                        annotationResponseDTO.setLineCap(konvaObject.getChildren().get(0).getAttrs().getLineCap());
                        annotationResponseDTO.setLineJoin(konvaObject.getChildren().get(0).getAttrs().getLineJoin());
                        annotationResponseDTO.setHitStrokeWidth(konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth());
                        annotationResponseDTO.setUser(annotationUpdateItemDTOV1.getTitle());
                        annotationResponseDTO.setSubject(shapeType);

//                        annotationResponseDTO.setMetadata(null);
                        annotationResponseDTO.setPageNo(annotationUpdateItemDTOV1.getPageNumber() + "");
                        annotationResponseDTO.setType(konvaObject.getChildren().get(0).getClassName());
                        annotationResponseDTOs.add(annotationResponseDTO);
                        break;
                    case "FREETEXT":
                        annotationResponseDTO.setId(annotationUpdateItemDTOV1.getId());
                        if (null != konvaObject.getChildren().get(0).getAttrs()) {
                            AnnotationPointDTO annotationPointDTOForFreeText = new AnnotationPointDTO();
                            float x = 0.0f;
                            float y = 0.0f;
                            ///
                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getX())) {
                                x = annotationUpdateItemDTOV1.getKonvaClientRect().getX();
                            }
                            annotationPointDTOForFreeText.setX(x);
                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getY())) {
                                y = annotationUpdateItemDTOV1.getKonvaClientRect().getY();
                            }
                            annotationPointDTOForFreeText.setY(y);
                            jsonpoints.add(annotationPointDTOForFreeText);
                            annotationPointDTOForFreeText = new AnnotationPointDTO();
                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getWidth())) {
                                x = x + annotationUpdateItemDTOV1.getKonvaClientRect().getWidth();
                            }
                            annotationPointDTOForFreeText.setX(x);

                            if (!Float.isNaN(annotationUpdateItemDTOV1.getKonvaClientRect().getHeight())) {
                                y = y + annotationUpdateItemDTOV1.getKonvaClientRect().getHeight();
                            }
                            annotationPointDTOForFreeText.setY(y);
                            jsonpoints.add(annotationPointDTOForFreeText);
                            ///
                            annotationResponseDTO.setJsonPoints(jsonpoints);
                        }
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getFill()) {
                            annotationResponseDTO.setPdColor(PdfAnnotationUtil.convertPDColor(konvaObject.getChildren().get(0).getAttrs().getFill()));
                        }
                        annotationResponseDTO.setBorderStyle("S");
                        annotationResponseDTO.setUser(annotationUpdateItemDTOV1.getTitle());
                        annotationResponseDTO.setSubject(shapeType);
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getText()) {
                            annotationResponseDTO.setContent(konvaObject.getChildren().get(0).getAttrs().getText());
                        }
                        annotationResponseDTO.setMetadata(null);
//                        annotationResponseDTO.setDeleted(false);
                        annotationResponseDTO.setPageNo(annotationUpdateItemDTOV1.getPageNumber() + "");
                        annotationResponseDTO.setType(konvaObject.getChildren().get(0).getClassName());
                        if (null != konvaObject.getChildren().get(0).getAttrs()) {
                            annotationResponseDTO.setFontSize(konvaObject.getChildren().get(0).getAttrs().getFontSize());
                            if (null != konvaObject.getChildren().get(0).getAttrs().getWidth()) {
                                annotationResponseDTO.setWidth(konvaObject.getChildren().get(0).getAttrs().getWidth());
                            }
                        }
//                            annotationResponseDTO.setType("TEXT");
                        annotationResponseDTO.setLineCap(konvaObject.getChildren().get(0).getAttrs().getLineCap());
                        annotationResponseDTO.setLineJoin(konvaObject.getChildren().get(0).getAttrs().getLineJoin());
                        annotationResponseDTO.setHitStrokeWidth(konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth());
                        annotationResponseDTO.setWrap(konvaObject.getChildren().get(0).getAttrs().getWrap());
                        annotationResponseDTOs.add(annotationResponseDTO);
                        break;
                    case "INK":
                        annotationResponseDTO.setId(annotationUpdateItemDTOV1.getId());
                        annotationResponseDTO.setType(shapeType);
                        annotationResponseDTO.setUser(annotationUpdateItemDTOV1.getTitle());
                        annotationResponseDTO.setSubject(shapeType);
                        annotationResponseDTO.setMetadata(null);
                        annotationResponseDTO.setPageNo(annotationUpdateItemDTOV1.getPageNumber() + "");
                        List<List<Float>> inkPointsList = new ArrayList<>();

                        for(int k=0;k<konvaObject.getChildren().size();k++) {
                            List<Float> inkPoints = konvaObject.getChildren().get(k).getAttrs().getPoints();
//                                Float x1 = konvaObject.getChildren().get(k).getAttrs().getPoints().get(i);
//                                    Float y1 = konvaObject.getChildren().get(k).getAttrs().getPoints().get(i + 1);
//                                    if (null != x1) {
//                                        annotationPointDTOForInk.setX(x1);
//                                    }
//                                    if (null != y1) {
//                                        annotationPointDTOForInk.setY(y1);
//                                    }
//                                    if (null != x1 && null != y1) {
//                                        jsonpoints.add(annotationPointDTOForInk);
//                                    }
//                                }
//                                inkElementDTO.setJsonPoints(jsonpoints);
                            inkPointsList.add(inkPoints);
                        }
                        annotationResponseDTO.setInkPointsList(inkPointsList);

                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getStroke()) {
                            annotationResponseDTO.setPdColor(PdfAnnotationUtil.convertPDColor(konvaObject.getChildren().get(0).getAttrs().getStroke()));
                        }
                        annotationResponseDTO.setBorderStyle("S");

//                        annotationResponseDTO.setDeleted(false);
//                            annotationResponseDTO.setType(konvaObject.getChildren().get(0).getClassName());
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth() && !"INK".equalsIgnoreCase(shapeType)) {
                            annotationResponseDTO.setBordWidth(konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth().intValue());
                        } else {
                            annotationResponseDTO.setBordWidth(2);
                        }
                        annotationResponseDTO.setLineCap(konvaObject.getChildren().get(0).getAttrs().getLineCap());
                        annotationResponseDTO.setLineJoin(konvaObject.getChildren().get(0).getAttrs().getLineJoin());
                        annotationResponseDTO.setHitStrokeWidth(konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth());

                        annotationResponseDTOs.add(annotationResponseDTO);
                        break;

                    case "POLYLINE":
                    case "POLYGON":
                        annotationResponseDTO.setId(annotationUpdateItemDTOV1.getId());
                        if (null != konvaObject.getChildren().get(0).getAttrs() && !konvaObject.getChildren().get(0).getAttrs().getPoints().isEmpty()) {
                            for (int i = 0; i < konvaObject.getChildren().get(0).getAttrs().getPoints().size(); i += 2) {
                                AnnotationPointDTO annotationPointDTOForPloyLine = new AnnotationPointDTO();
                                Float x1 = konvaObject.getChildren().get(0).getAttrs().getPoints().get(i);
                                Float y1 = konvaObject.getChildren().get(0).getAttrs().getPoints().get(i + 1);
                                if (null != x1) {
                                    annotationPointDTOForPloyLine.setX(x1);
                                }
                                if (null != y1) {
                                    annotationPointDTOForPloyLine.setY(y1);
                                }
                                if (null != x1 && null != y1) {
                                    jsonpoints.add(annotationPointDTOForPloyLine);
                                }
                            }
                            annotationResponseDTO.setJsonPoints(jsonpoints);
                        }
                        if (konvaObject.getChildren().get(0).getClassName().equalsIgnoreCase("ARROW")) {
                            if (annotationResponseDTO.getMetadata() == null) {
                                annotationResponseDTO.setMetadata(new HashMap<>());
                            }
                            annotationResponseDTO.getMetadata().put("LE", PDAnnotationLine.LE_OPEN_ARROW);
                        }
                        Float polylineOpacity = 1.0f;
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getOpacity()) {
                            polylineOpacity = konvaObject.getChildren().get(0).getAttrs().getOpacity();
                        }
                        if (null != konvaObject.getChildren().get(0).getAttrs().getStroke()) {
                            annotationResponseDTO.setPdColor(PdfAnnotationUtil.convertPDColor(konvaObject.getChildren().get(0).getAttrs().getStroke()));
                        }
                        annotationResponseDTO.setBorderStyle("S");
                        annotationResponseDTO.setOpacity(polylineOpacity);
                        annotationResponseDTO.setUser(annotationUpdateItemDTOV1.getTitle());
                        annotationResponseDTO.setSubject(shapeType);
                        annotationResponseDTO.setBezier(konvaObject.getChildren().get(0).getAttrs().getBezier());
//                            annotationResponseDTO.setMetadata(null);
//                        annotationResponseDTO.setDeleted(false);
                        annotationResponseDTO.setPageNo(annotationUpdateItemDTOV1.getPageNumber() + "");
//                            annotationResponseDTO.setType(konvaObject.getChildren().get(0).getClassName());
                        annotationResponseDTO.setType(shapeType);
                        if (null != konvaObject.getChildren().get(0).getAttrs() && null != konvaObject.getChildren().get(0).getAttrs().getStrokeWidth()) {
                            annotationResponseDTO.setBordWidth((int) Float.parseFloat(konvaObject.getChildren().get(0).getAttrs().getStrokeWidth()));
                        } else {
                            annotationResponseDTO.setBordWidth(2);
                        }
                        annotationResponseDTO.setLineCap(konvaObject.getChildren().get(0).getAttrs().getLineCap());
                        annotationResponseDTO.setLineJoin(konvaObject.getChildren().get(0).getAttrs().getLineJoin());
                        annotationResponseDTO.setHitStrokeWidth(konvaObject.getChildren().get(0).getAttrs().getHitStrokeWidth());

//                            Float opacity = annotationUpdateItemDTOV1.getKonvaObject().getChildren().get(0).getAttrs().getOpacity();
                        annotationResponseDTOs.add(annotationResponseDTO);
                        break;
                }
            }
        }
        return annotationResponseDTOs;
    }
}
