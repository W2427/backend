package com.ose.tasks.domain.model.service;

import com.ose.util.FileUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.DocumentHistoryRepository;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.DocumentUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;
import com.ose.tasks.dto.drawing.DocumentUploadDTO;
import com.ose.tasks.dto.drawing.ProofreadSubDrawingPreviewDTO;
import com.ose.tasks.entity.DocumentHistory;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.vo.EntityStatus;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Component
public class DocumentHistoryService extends StringRedisService implements DocumentHistoryInterface {

    private final static Logger logger = LoggerFactory.getLogger(DocumentHistoryService.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.servlet.multipart.location}")
    private String multipartFormDataDir;

    private final DocumentHistoryRepository documentHistoryRepository;

    private final BatchTaskInterface batchTaskService;

    private final ProjectInterface projectService;

    private final UploadFeignAPI uploadFeignAPI;

    @Autowired
    public DocumentHistoryService(DocumentHistoryRepository documentHistoryRepository,
                                  StringRedisTemplate stringRedisTemplate,
                                  BatchTaskInterface batchTaskService,
                                  ProjectInterface projectService,
                                  UploadFeignAPI uploadFeignAPI) {
        super(stringRedisTemplate);
        this.documentHistoryRepository = documentHistoryRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 获取外检报告上传历史记录
     */
    @Override
    public Page<DocumentHistory> uploadHistories(DocumentUploadHistorySearchDTO pageDTO, Long operatorId) {
        return documentHistoryRepository.uploadHistories(pageDTO, operatorId);
    }

    /**
     * 文件上传
     */
    @Override
    public BpmExInspConfirmResponseDTO uploadDocument(
        DocumentUploadDTO uploadDTO,
        OperatorDTO operatorDTO,
        ContextDTO context
    ) throws FileNotFoundException {

        Project project = projectService.get(1624840920328845260L, 1624840920575068904L);
        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.DOCUMENT_UPLOAD,
            false,
            context,
            batchTask -> {

                try {
                    // 1、上传的原始文件保存、并创建历史记录
                    System.out.println("开始外检上传文件" + new Date());
                    String temporaryFileName = uploadDTO.getFileName();
                    File diskFileTemp = new File(temporaryDir, temporaryFileName);
                    if (!diskFileTemp.exists()) {
                        throw new NotFoundError();
                    }

                    FileMetadataDTO metadata = FileUtils.readMetadata(diskFileTemp, FileMetadataDTO.class);
//                    if (!metadata.getFilename().endsWith(".xlsx") && !metadata.getFilename().endsWith(".pdf")) {
//                        throw new NotFoundError();
//                    }
                    FilePostDTO filePostDTO = new FilePostDTO();
                    filePostDTO.setContext(context);


                    logger.info("外检上传 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.
                        save(String.valueOf(1624840920575068904L), String.valueOf(1624840920575068904L), temporaryFileName, filePostDTO);
                    logger.info("外检上传 保存docs服务->结束");

                    FileES fileES = fileESResBody.getData();

                    DocumentHistory documentHistory = new DocumentHistory();
                    documentHistory.setFilePath(fileES.getPath());
                    documentHistory.setOperator(operatorDTO.getId());
                    documentHistory.setOperatorName(operatorDTO.getName());
                    documentHistory.setFileId(LongUtils.parseLong(fileES.getId()));
                    documentHistory.setStatus(EntityStatus.ACTIVE);
                    documentHistory.setDeleted(false);
                    documentHistory.setCreatedBy(operatorDTO.getId());
                    documentHistory.setCreatedAt(new Date());
                    documentHistory.setLastModifiedAt(new Date());
                    documentHistory.setLastModifiedBy(operatorDTO.getId());
                    documentHistory.setMemo(uploadDTO.getComment());
                    documentHistory.setFileName(metadata.getFilename());

                    String labels = "";
                    if (uploadDTO.getLabel() != null && !uploadDTO.getLabel().equals("") && uploadDTO.getLabel().size() > 0) {
                        for(String label : uploadDTO.getLabel()) {
                            labels = labels + label + ",";
                        }
                        labels = labels.substring(0, labels.length() -1);
                    }
                    documentHistory.setLabel(labels);
                    documentHistoryRepository.save(documentHistory);

                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
                return new BatchResultDTO();
            });

        return new BpmExInspConfirmResponseDTO();
    }

    @Override
    public void delete(OperatorDTO operatorDTO, Long id) {
        Optional<DocumentHistory> documentHistoryOptional = documentHistoryRepository.findById(id);
        if (documentHistoryOptional.isPresent()) {
            DocumentHistory documentHistory = documentHistoryOptional.get();
            documentHistory.setDeletedAt(new Date());
            documentHistory.setDeletedBy(operatorDTO.getId());
            documentHistory.setStatus(EntityStatus.DELETED);
            documentHistory.setDeleted(true);
            documentHistory.setLastModifiedBy(operatorDTO.getId());
            documentHistory.setLastModifiedAt(new Date());
            documentHistoryRepository.save(documentHistory);
        }
    }

    @Override
    public DocumentHistory modify(OperatorDTO operatorDTO, ContextDTO context,  Long id, DocumentUploadDTO documentUploadDTO) {
        Optional<DocumentHistory> documentHistoryOptional = documentHistoryRepository.findById(id);
        if (documentHistoryOptional.isPresent()) {
            if (documentUploadDTO.getFileName() != null) {
                Project project = projectService.get(1624840920328845260L, 1624840920575068904L);
                batchTaskService.runConstructTaskExecutor(
                    null,
                    project,
                    BatchTaskCode.DOCUMENT_UPLOAD,
                    false,
                    context,
                    batchTask -> {

                        try {
                            // 1、上传的原始文件保存、并创建历史记录
                            System.out.println("开始外检上传文件" + new Date());
                            String temporaryFileName = documentUploadDTO.getFileName();
                            File diskFileTemp = new File(temporaryDir, temporaryFileName);
                            if (!diskFileTemp.exists()) {
                                throw new NotFoundError();
                            }

                            FileMetadataDTO metadata = FileUtils.readMetadata(diskFileTemp, FileMetadataDTO.class);
//                            if (!metadata.getFilename().endsWith(".xlsx") && !metadata.getFilename().endsWith(".pdf")) {
//                                throw new NotFoundError();
//                            }
                            FilePostDTO filePostDTO = new FilePostDTO();
                            filePostDTO.setContext(context);


                            logger.info("外检上传 保存docs服务->开始");
                            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.
                                save(String.valueOf(1624840920575068904L), String.valueOf(1624840920575068904L), temporaryFileName, filePostDTO);
                            logger.info("外检上传 保存docs服务->结束");

                            FileES fileES = fileESResBody.getData();

                            DocumentHistory documentHistory = documentHistoryOptional.get();
                            documentHistory.setFilePath(fileES.getPath());
                            documentHistory.setOperator(operatorDTO.getId());
                            documentHistory.setOperatorName(operatorDTO.getName());
                            documentHistory.setFileId(LongUtils.parseLong(fileES.getId()));
                            documentHistory.setLastModifiedAt(new Date());
                            documentHistory.setLastModifiedBy(operatorDTO.getId());
                            documentHistory.setMemo(documentUploadDTO.getComment());
                            documentHistory.setFileName(metadata.getFilename());

                            String labels = "";
                            if (documentUploadDTO.getLabel() != null && !documentUploadDTO.getLabel().equals("") && documentUploadDTO.getLabel().size() > 0) {
                                for(String label : documentUploadDTO.getLabel()) {
                                    labels = labels + label + ",";
                                }
                                labels = labels.substring(0, labels.length() -1);
                            }
                            documentHistory.setLabel(labels);
                            documentHistoryRepository.save(documentHistory);

                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                        }
                        return new BatchResultDTO();
                    });
            } else {
                DocumentHistory documentHistory = documentHistoryOptional.get();
                documentHistory.setOperator(operatorDTO.getId());
                documentHistory.setOperatorName(operatorDTO.getName());
                documentHistory.setLastModifiedAt(new Date());
                documentHistory.setLastModifiedBy(operatorDTO.getId());

                if (documentUploadDTO.getComment() != null && !documentUploadDTO.getComment().equals("")) {
                    documentHistory.setMemo(documentUploadDTO.getComment());
                }

                if (documentUploadDTO.getLabel() != null && !documentUploadDTO.getLabel().equals("") && documentUploadDTO.getLabel().size() > 0) {
                    String labels = "";
                    for(String label : documentUploadDTO.getLabel()) {
                        labels = labels + label + ",";
                    }
                    labels = labels.substring(0, labels.length() -1);
                    documentHistory.setLabel(labels);
                }
                documentHistoryRepository.save(documentHistory);
            }
        }
        return documentHistoryOptional.get();
    }

    @Override
    public DocumentHistory get(Long id) {
        Optional<DocumentHistory> op = documentHistoryRepository.findById(id);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    @Override
    public ProofreadSubDrawingPreviewDTO preview(
        Long id
    ) {
        Optional<DocumentHistory> documentHistoryOptional = documentHistoryRepository.findById(id);
        if (documentHistoryOptional.isPresent()) {
            DocumentHistory documentHistory = documentHistoryOptional.get();
            ProofreadSubDrawingPreviewDTO dto = new ProofreadSubDrawingPreviewDTO();
            String drawingBase = "";

            try {
                File subDrawingFile = new File(protectedDir + documentHistory.getFilePath().substring(1));
                if (subDrawingFile.length() > 1024000) {

                    String subDrawingImg = pdfToImg(
                        documentHistory.getFileName(),
                        protectedDir + documentHistory.getFilePath().substring(1)
                    );


                    String subDrawingPdf = convertImgToPDF(
                        subDrawingImg,
                        documentHistory.getFileName(),
                        temporaryDir
                    );
                    drawingBase = getBaseFromFile(
                        new File(subDrawingPdf)
                    );
                } else {
                    drawingBase = getBaseFromFile(
                        new File(protectedDir + documentHistory.getFilePath().substring(1))
                    );

                }
            } catch (Exception e) {

            }

            dto.setSubDrawingBase(
                drawingBase
            );
            return dto;
        }
        return null;
    }


    private String generateXlsxReport(String reportPath, String templateFile){

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(templateFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // ① 获取模板文件 复制到 临时路径下
        String temporaryFileName = FileUtils.copy(
            inputStream,
            multipartFormDataDir,
            "12345678"
        );

        // ② 把临时路径下的模板文件读到workbook中，并填充数据
        File excel;
        Workbook workbook = null;

        try {
            excel = new File(multipartFormDataDir, temporaryFileName);
            workbook = WorkbookFactory.create(excel);

            Sheet configSheet = workbook.getSheet("SETTINGS");
            Row configRow = WorkbookUtils.getRow(configSheet, 2);
            String sheetName = WorkbookUtils.readAsString(configRow, 1);
            configRow = WorkbookUtils.getRow(configSheet, 3);
            Integer sheetCount = WorkbookUtils.readAsInteger(configRow, 1);
            configRow = WorkbookUtils.getRow(configSheet, 4);
            String memberA = WorkbookUtils.readAsString(configRow, 1).trim();
            configRow = WorkbookUtils.getRow(configSheet, 5);
            String memberB = WorkbookUtils.readAsString(configRow, 1).trim();
            configRow = WorkbookUtils.getRow(configSheet, 6);
            Integer forceX = WorkbookUtils.readAsInteger(configRow, 1);
            configRow = WorkbookUtils.getRow(configSheet, 7);
            Integer memberARow = WorkbookUtils.readAsInteger(configRow, 1);
            configRow = WorkbookUtils.getRow(configSheet, 8);
            Integer memberBRow = WorkbookUtils.readAsInteger(configRow, 1);

            // 将导入的数据源保存到map数组中
//            Integer memberARow = 9;
//            Integer memberBRow = 74;
//            Boolean memberAStatus = false;
//            Boolean memberBStatus = false;
            Sheet dataSource = workbook.getSheet("DATASOURCE");
//            for (Row row : dataSource) {
//                for (Cell cell : row) {
//                    while(cell.getCellType() == Cell.CELL_TYPE_STRING){
//                        if(cell.getRichStringCellValue().getString().equals(memberA) && !memberAStatus) {
//                            memberARow = row.getRowNum();
//                            memberAStatus = true;
//                        }
//                        if(cell.getRichStringCellValue().getString().equals(memberB) && !memberBStatus) {
//                            memberBRow = row.getRowNum();
//                            memberBStatus = true;
//                        }
//                        if (memberAStatus && memberBStatus) {
//                            break;
//                        }
//                    }
//                }
//            }
            List<Map<String, Object>> parameters = new ArrayList();
            for (int i = 0 ; i < sheetCount; i++) {
                Row dataSourceRow = WorkbookUtils.getRow(dataSource, memberARow + i - 1);
                Integer valueA = Math.abs(WorkbookUtils.readAsInteger(dataSourceRow, forceX - 1));
                Map<String, Object> parameter = new HashMap<>();
                parameter.put("RR5", valueA);

                dataSourceRow = WorkbookUtils.getRow(dataSource, memberBRow + i - 1);
                Integer valueB = Math.abs(WorkbookUtils.readAsInteger(dataSourceRow, forceX - 1));
                parameter.put("RR6", valueB);

                parameters.add(parameter);
            }


            //˙要复制的 Sheet，主数据 Sheet
            int masterSheetIdx = workbook.getSheetIndex(sheetName);
            workbook.setSheetHidden(masterSheetIdx, true);


            // 填充 表主体
            int pageCnt = sheetCount;

            //Header Map
            Sheet headerSetting = workbook.getSheet("OUTPUT");
            Map<String, List<Integer>> headerMap = getHeaderInfo(headerSetting);

            // 如果是多重模板并且数据量大于第一页模板的数据则启用多重模板
            for (int p = 0; p < pageCnt; p++) {
                Map<String, Object> parameter = parameters.get(p);
                Sheet clonedSheet = workbook.cloneSheet(masterSheetIdx);
                int sheetIdx = workbook.getSheetIndex(clonedSheet.getSheetName());
                workbook.setSheetName(sheetIdx, sheetName + p);

                //填充表头
                headerMap.forEach((key, value) -> {
                    Row row = WorkbookUtils.getRow(clonedSheet, value.get(1));
                    WorkbookUtils.getCell(row, value.get(0)).setCellValue((Integer) parameter.get(key));
                });
            }

            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
            WorkbookUtils.save(workbook, reportPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return reportPath;
    }

    private Map<String, List<Integer>> getHeaderInfo(Sheet sheet){

        int rowNum = 2;
        Map<String, List<Integer>> headerMap = new HashMap<>();

        while (true){
            Row row = WorkbookUtils.getRow(sheet, rowNum++);
            List<Integer> cell = new ArrayList<>();
            if(StringUtils.isEmpty(WorkbookUtils.readAsString(row, 0))) break;
            cell.add(WorkbookUtils.readAsInteger(row, 1)-1);
            cell.add(WorkbookUtils.readAsInteger(row, 2)-1);

            headerMap.put(WorkbookUtils.readAsString(row, 0), cell);

        }
        return headerMap;
    }

    /**
     * 文件转base64字符串
     *
     * @param file 文件对象
     * @return
     */
    private String getBaseFromFile(File file) {
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        BufferedOutputStream bout = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while (len != -1) {
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }

            bout.flush();
            byte[] bytes = baos.toByteArray();
            return Base64.toBase64String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * pdf 转图片
     */
    public String pdfToImg(
        String drawingNo,
        String pdfPath
    ) {

        float DEFAULT_DPI = 105;

        String DEFAULT_FORMAT = "jpg";
        try {


            int width = 0;

            int[] singleImgRGB;
            int shiftHeight = 0;

            BufferedImage imageResult = null;

            PDDocument pdDocument = PDDocument.load(new File(pdfPath));
            PDFRenderer renderer = new PDFRenderer(pdDocument);

            BufferedImage image = renderer.renderImageWithDPI(0, DEFAULT_DPI, ImageType.RGB);
            int imageHeight = image.getHeight();
            int imageWidth = image.getWidth();


            width = imageWidth;

            imageResult = new BufferedImage(width, imageHeight, BufferedImage.TYPE_INT_RGB);

            singleImgRGB = image.getRGB(0, 0, width, imageHeight, null, 0, width);

            imageResult.setRGB(0, shiftHeight, width, imageHeight, singleImgRGB, 0, width);
            pdDocument.close();

            String imgPath = temporaryDir + drawingNo + ".jpg";
            ImageIO.write(imageResult, DEFAULT_FORMAT, new File(imgPath));
            return imgPath;
        } catch (Exception e) {
            e.printStackTrace();
            return pdfPath;
        }
    }

    /**
     * 图片转pdf
     */
    public String convertImgToPDF(
        String imagePath,
        String fileName,
        String destDir) throws IOException {
        PDDocument document = new PDDocument();
        InputStream in = new FileInputStream(imagePath);
        BufferedImage bimg = ImageIO.read(in);
        float width = bimg.getWidth();
        float height = bimg.getHeight();
        PDPage page = new PDPage(new PDRectangle(width, height));
        document.addPage(page);
        PDImageXObject img = PDImageXObject.createFromFile(imagePath, document);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(img, 0, 0);
        contentStream.close();
        in.close();
        document.save(destDir + "/" + fileName + ".pdf");
        document.close();
        return destDir + "/" + fileName + ".pdf";
    }

}
