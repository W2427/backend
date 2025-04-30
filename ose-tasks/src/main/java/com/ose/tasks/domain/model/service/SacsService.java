package com.ose.tasks.domain.model.service;

import com.ose.util.*;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.SacsUploadHistoryRepository;
import com.ose.tasks.dto.SacsUploadHistorySearchDTO;
import com.ose.tasks.dto.bpm.BpmExInspConfirmResponseDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.SacsUploadHistory;
import com.ose.vo.EntityStatus;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Component
public class SacsService extends StringRedisService implements SacsInterface {

    private final static Logger logger = LoggerFactory.getLogger(SacsService.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.servlet.multipart.location}")
    private String multipartFormDataDir;

    private final SacsUploadHistoryRepository sacsUploadHistoryRepository;

    private final BatchTaskInterface batchTaskService;

    private final ProjectInterface projectService;

    private final UploadFeignAPI uploadFeignAPI;

    @Autowired
    public SacsService(SacsUploadHistoryRepository sacsUploadHistoryRepository,
                       StringRedisTemplate stringRedisTemplate,
                       BatchTaskInterface batchTaskService,
                       ProjectInterface projectService,
                       UploadFeignAPI uploadFeignAPI) {
        super(stringRedisTemplate);
        this.sacsUploadHistoryRepository = sacsUploadHistoryRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 获取外检报告上传历史记录
     */
    @Override
    public Page<SacsUploadHistory> sacsUploadHistories(Long orgId, Long projectId,
                                                       SacsUploadHistorySearchDTO pageDTO, Long operatorId) {
        return sacsUploadHistoryRepository.sacsUploadHistories(orgId, projectId,
            pageDTO, operatorId);
    }

    /**
     * 文件上传
     */
    @Override
    public BpmExInspConfirmResponseDTO uploadSacs(
        Long orgId,
        Long projectId,
        DrawingUploadDTO uploadDTO,
        OperatorDTO operatorDTO,
        ContextDTO context
    ) throws FileNotFoundException {

        Project project = projectService.get(orgId, projectId);
//        batchTaskService.runConstructTaskExecutor(
//            null,
//            project,
//            BatchTaskCode.SACS_UPLOAD,
//            false,
//            context,
//            batchTask -> {
//
//                try {
//
//
//                } catch (Exception e) {
//                    e.printStackTrace(System.out);
//                }
//                return new BatchResultDTO();
//            });
        // 1、上传的原始文件保存、并创建历史记录
        System.out.println("开始外检上传文件" + new Date());
        String temporaryFileName = uploadDTO.getFileName();
        File diskFileTemp = new File(temporaryDir, temporaryFileName);
        if (!diskFileTemp.exists()) {
            throw new NotFoundError();
        }

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFileTemp, FileMetadataDTO.class);
        if (!metadata.getFilename().endsWith(".xlsx")) {
            throw new NotFoundError();
        }
        FilePostDTO filePostDTO = new FilePostDTO();
        filePostDTO.setContext(context);


        logger.info("外检上传 保存docs服务->开始");
        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.
            save(orgId.toString(), projectId.toString(), temporaryFileName, filePostDTO);
        logger.info("外检上传 保存docs服务->结束");

        FileES fileES = fileESResBody.getData();

        SacsUploadHistory sacsUploadHistory = new SacsUploadHistory();
        sacsUploadHistory.setOrgId(orgId);
        sacsUploadHistory.setProjectId(projectId);
        sacsUploadHistory.setFilePath(fileES.getPath());
        sacsUploadHistory.setOperator(operatorDTO.getId());
        sacsUploadHistory.setOperatorName(operatorDTO.getName());
        sacsUploadHistory.setFileId(LongUtils.parseLong(fileES.getId()));
        sacsUploadHistory.setStatus(EntityStatus.ACTIVE);

        sacsUploadHistory.setCreatedAt(new Date());
        sacsUploadHistory.setLastModifiedAt(new Date());
        sacsUploadHistory.setMemo(uploadDTO.getComment());
        sacsUploadHistory.setFileName(metadata.getFilename());

        // 2、将上传的模板文件分析，提取公式计算单元格应填写的信息，并输出文档、提供下载

        String reportPath = multipartFormDataDir + CryptoUtils.uniqueId() + ".xlsx";
        generateXlsxReport(reportPath, protectedDir + fileES.getPath().substring(1));

        DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem(
            "file", APPLICATION_PDF_VALUE, true, reportPath
        );
        MockMultipartFile fileItem1 = null;
        try {
            IOUtils.copy(new FileInputStream(reportPath), fileItem.getOutputStream());
            fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError(e.toString()); // TODO
        }
        JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody =
            uploadFeignAPI.uploadProjectDocumentFile(
                orgId.toString(),
                fileItem1
            );
        JsonObjectResponseBody<FileES> fileESResBodyB = uploadFeignAPI.save(
            orgId.toString(),
            projectId.toString(),
            tempFileResBody.getData().getName(),
            new FilePostDTO()
        );
        FileES fileESB = fileESResBodyB.getData();
        sacsUploadHistory.setCalculatedFileId(LongUtils.parseLong(fileESB.getId()));
        sacsUploadHistory.setCalculatedFileName(sacsUploadHistory.getFileName());
        sacsUploadHistory.setCalculatedFilePath(fileESB.getPath());
        sacsUploadHistoryRepository.save(sacsUploadHistory);





        return new BpmExInspConfirmResponseDTO();
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

}
