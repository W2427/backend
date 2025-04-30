package com.ose.report.controller;

import com.ose.controller.BaseController;
import com.ose.util.*;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.report.domain.service.ReportHistoryInterface;
import com.ose.report.dto.BaseListReportDTO;
import com.ose.report.dto.BaseReportDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.report.vo.ReportExportType;
import com.ose.response.JsonObjectResponseBody;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

/**
 * 报表控制器基类。
 */
public class BaseReportController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(BaseReportController.class);
    // 报表模板存放路径
    @Value("${application.templates.reports}")
    private String templateBaseDir;

    // 报表使用静态资源存放路径
    @Value("${application.templates.reports.assets}")
    private String assetsBaseDir;

    // 准备上传的文件的保存路径
    @Value("${spring.servlet.multipart.location}")
    private String multipartFormDataDir;

    // 报表历史记录服务
    private final ReportHistoryInterface reportHistoryService;

    // 文件上传接口
    private final UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法。
     *
     * @param reportHistoryService 报表历史记录服务
     * @param uploadFeignAPI       文件上传接口
     */
    public BaseReportController(
        ReportHistoryInterface reportHistoryService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
            UploadFeignAPI uploadFeignAPI
    ) {

        this.reportHistoryService = reportHistoryService;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 生成报表文件。
     *
     * @param operator     操作者信息
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param templateName 模板文件名
     * @param reportDTO    报表信息
     * @return 报表创建历史记录
     */
    ReportHistory generateReportFile(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        String templateName,
        BaseReportDTO reportDTO
    ) {
        logger.info("BaseReportController generateReportFile method 1 start ");
        return generateReportFile(operator, orgId, projectId, templateName, reportDTO, null, null);
    }

    /**
     * 生成报表文件。
     *
     * @param operator     操作者信息
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param templateName 模板文件名
     * @param reportDTO    报表信息
     * @param parameters   参数映射表
     * @return 报表创建历史记录
     */
    ReportHistory generateReportFile(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        String templateName,
        BaseReportDTO reportDTO,
        Map<String, Object> parameters
    ) {
        logger.info("BaseReportController generateReportFile  method 2 start");
        return generateReportFile(operator, orgId, projectId, templateName, reportDTO, parameters, null);
    }

    /**
     * 生成报表文件。
     *
     * @param operator     操作者信息
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param templateName 模板文件名
     * @param reportDTO    报表信息
     * @param records      详细记录列表
     * @return 报表创建历史记录
     */
    ReportHistory generateReportFile(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        String templateName,
        BaseReportDTO reportDTO,
        List<?> records
    ) {
        logger.info("BaseReportController generateReportFile  method 3 start");
        return generateReportFile(operator, orgId, projectId, templateName, reportDTO, null, records);
    }

    /**
     * 生成报表文件。
     *
     * @param operator     操作者信息
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param templateName 模板文件名
     * @param reportDTO    报表信息
     * @param parameters   参数映射表
     * @param records      详细记录列表
     * @return 报表创建历史记录
     */
    ReportHistory generateReportFile(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        String templateName,
        BaseReportDTO reportDTO,
        Map<String, Object> parameters,
        List<?> records
    ) {
        logger.info("BaseReportController generateReportFile  method 4 start");
        if (parameters == null) {
            parameters = new HashMap<>();
        }

        // 设置静态资源路径
        parameters.computeIfAbsent("ASSETS_DIR", k -> assetsBaseDir);


        if (!StringUtils.isEmpty(reportDTO.getClientLogoDir())) {
            parameters.put("CLIENT_LOGO_DIR", reportDTO.getClientLogoDir());
        } else {
            parameters.put("CLIENT_LOGO_DIR", "false");
        }

        if (!StringUtils.isEmpty(reportDTO.getContractorLogoDir())) {
            parameters.put("CONTRACTOR_LOGO_DIR", reportDTO.getContractorLogoDir());
        } else {
            parameters.put("CONTRACTOR_LOGO_DIR", "false");
        }
        // 未设置数据源时将报表 DTO 中的 items 作为数据源
        if (records == null) {
            if (reportDTO instanceof BaseListReportDTO) {
                records = ((BaseListReportDTO) reportDTO).getItems();
            } else {
                records = new ArrayList<>();
                records.add(null);
            }
        }

        final JRBeanCollectionDataSource dataSource = (records == null || records.size() == 0)
            ? null
            : new JRBeanCollectionDataSource(records);

        final ReportExportType exportType = reportDTO.getExportType() == null
            ? ReportExportType.PDF
            : reportDTO.getExportType();

        //如果导出 类型维 XLSX，检查是否有导出模板（通用模板，项目模板），如果有则使用 定制的XLS导出功能
        boolean isNativeXls = false;//是否使用原生方法生成xlsx报告
        String xlsTemplateName = null;
        if(exportType == ReportExportType.MS_EXCEL) {
            xlsTemplateName = FileUtils.getFileNameNoEx(templateName) + "." + "xlsx";
            File t = new File(templateBaseDir + reportDTO.getProjectNamePrefix() + "-" + xlsTemplateName);
            if (t.exists()) {
                xlsTemplateName = reportDTO.getProjectNamePrefix() + "-" + xlsTemplateName;
                isNativeXls = true;
            } else {
                t = new File(templateBaseDir + xlsTemplateName);
                if(t.exists()) {
//                    templateName = xlsTemplateName;
                    isNativeXls = true;
                }
            }

        }

        //查找项目模板
        File t = new File(templateBaseDir + reportDTO.getProjectNamePrefix() + "-" + templateName);
        if (!isNativeXls && t.exists()) {
            templateName = reportDTO.getProjectNamePrefix() + "-" + templateName;
        }




        // 设置报表参数
        reportDTO.setOrgId(reportDTO.getOrgId() == null ? orgId : reportDTO.getOrgId());
        reportDTO.setProjectId(reportDTO.getProjectId() == null ? projectId : reportDTO.getProjectId());
        reportDTO.setRev(reportDTO.getRev() == null ? "RV.0" : reportDTO.getRev());
        BeanUtils.copyProperties(reportDTO, parameters);
        parameters.put("REPORT_ID", reportDTO.getReportQrCode());

        parameters.put("seriesNo", reportDTO.getSerialNo());
        parameters.put("reportNo", reportDTO.getReportNo());
        parameters.put("drawingRev", reportDTO.getRev());

        parameters.put("supplier", reportDTO.getSupplier());
        parameters.put("location", reportDTO.getLocation());

        parameters.put("structureName", reportDTO.getStructureName());
        parameters.put("drawingNo", reportDTO.getDrawingNo());
        parameters.put("date", reportDTO.getDate());
        parameters.put("materialType", reportDTO.getMaterialType());

        parameters.put("material", reportDTO.getMaterial());

        parameters.put("moduleNo", reportDTO.getModuleNo());

        parameters.put("weldingProcess", reportDTO.getWeldingProcess());



        final String reportPath = multipartFormDataDir + CryptoUtils.uniqueId() + exportType.getExtName();

        // 创建历史记录
        ReportHistory reportHistory = reportHistoryService
            .save(operator, new ReportHistory(operator, templateName, reportDTO));

        InputStream template = null;
        OutputStream report = null;

        // 生成报表文件
        try {

            if(!isNativeXls) {
                template = new FileInputStream(new File(templateBaseDir + templateName));
                report = new FileOutputStream(new File(reportPath));
            }
            //生成原生的 xlsx 的报告
            if(isNativeXls){
                generateXlsxReport(reportPath, parameters, records, templateBaseDir + xlsTemplateName);

                // 生成 PDF 文档
            } else if (exportType == ReportExportType.PDF) {
                if (dataSource == null) {
                    JasperRunManager.runReportToPdfStream(template, report, parameters);
                } else {
                    JasperRunManager.runReportToPdfStream(template, report, parameters, dataSource);
                }
                // 生成 Microsoft Office 格式文件
            } else if (exportType == ReportExportType.MS_EXCEL || exportType == ReportExportType.MS_WORD) {

                final JRAbstractExporter exporter;

                if (exportType == ReportExportType.MS_EXCEL) {
                    exporter = new JRXlsxExporter();
                } else {
                    exporter = new JRDocxExporter();
                }

                final JasperPrint jasperPrint;

                if (dataSource == null) {
                    jasperPrint = JasperFillManager.fillReport(template, parameters);
                } else {
                    jasperPrint = JasperFillManager.fillReport(template, parameters, dataSource);
                }

                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(report));
                exporter.exportReport();
            }
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace(System.out);
            reportHistory.setGeneratedError(e.toString());
            throw new BusinessError(e.toString()); // TODO
        } finally {
            StreamUtils.close(template);
            StreamUtils.close(report);
        }

        DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem(
            "file", APPLICATION_PDF_VALUE, true, reportPath
        );

        try {
            IOUtils.copy(new FileInputStream(reportPath), fileItem.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace(System.out);
            reportHistory.setGeneratedError(e.toString());
            throw new BusinessError(e.toString()); // TODO
        }
        logger.error("基础1 上传docs服务->开始");

        // 将文件上传到文档服务器
        try {
            MockMultipartFile fileItem1 = new MockMultipartFile("file", new File(reportPath).getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody =
                uploadFeignAPI.uploadProjectDocumentFile(
                    orgId.toString(),
                    fileItem1
                );



            logger.error("基础1 上传docs服务->结束");
            // 保存上传的文件
            logger.error("基础1 保存docs服务->开始");
            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(
                orgId.toString(),
                projectId.toString(),
                tempFileResBody.getData().getName(),
                new FilePostDTO()
            );

            logger.error("基础1 保存docs服务->结束");

            // 更新历史记录信息
            FileES fileES = fileESResBody.getData();
            reportHistory.setFileId(LongUtils.parseLong(
                fileES.getId()
            ));
            reportHistory.setFilePath(fileES.getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        reportHistoryService.save(operator, reportHistory);

        FileUtils.remove(reportPath);
        logger.info("BaseReportController generateReportFile  method 4 end");
        return reportHistory;
    }

    /**
     * 上传 PDF 文件。
     * TODO: remove this method.
     *
     * @deprecated use generateReportFile(...) methods
     */
    FileES uploadPDFFile(Long orgId, Long projectId, OutputStream reportStream, UploadFeignAPI uploadFeignAPI) {

        File diskFile = new File(
            multipartFormDataDir,
            RandomUtils.getHexString(24)
        );

        JsonObjectResponseBody<FileES> fileESResBody;

        try {

            org.apache.commons.io.FileUtils.writeByteArrayToFile(
                diskFile,
                ((ByteArrayOutputStream) reportStream).toByteArray()
            );

            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory())
                .createItem(
                    "file",
                    MediaType.APPLICATION_PDF_VALUE,
                    true,
                    diskFile.getName() + ".pdf"
                );

//            diskFile = new File(protectedDir + fileES.getPath().substring(1));
//            System.out.println(diskFile.getName());
//            MockMultipartFile fileItem = new MockMultipartFile("file", diskFile.getName(), MediaType.APPLICATION_PDF_VALUE, new FileInputStream(diskFile));


            IOUtils.copy(
                new FileInputStream(diskFile),
                fileItem.getOutputStream()
            );

            // 将文件上传到文档服务器
            logger.error("基础2 上传docs服务->开始");
            MockMultipartFile fileItem1 = new MockMultipartFile("file", diskFile.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody
                = uploadFeignAPI.uploadProjectDocumentFile(
                orgId.toString(),
                fileItem1
            );

//            uploadFeignAPI.uploadProjectDocumentFile(orgId.toString(), fileItem);

            logger.error("基础2 上传docs服务->结束");
            logger.error("基础2 保存docs服务->开始");
            fileESResBody = uploadFeignAPI.save(
                orgId.toString(),
                projectId.toString(),
                tempFileResBody.getData().getName(),
                new FilePostDTO()
            );
            logger.error("基础2 保存docs服务->结束");
        } catch (IOException e) {
            throw new BusinessError(); // TODO
        }

        return fileESResBody.getData();
    }


    private String generateXlsxReport(String reportPath,
                                      Map<String, Object> parameters,
                                      List<?> records,
                                      String templateFile){

        InputStream inputStream = null;
//        templateFile = "/var/www/ose/resources/templates/reports/F217xPipingx07xPipingFitupInspectionReport.xlsx";
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
//        } catch (IOException | InvalidFormatException e) {
//            e.printStackTrace(System.out);
//            throw new BusinessError();
//        }

        Sheet configSheet = workbook.getSheet("SETTINGS");
        Row configRow = WorkbookUtils.getRow(configSheet, 2);
        String sheetName = WorkbookUtils.readAsString(configRow, 1);
        configRow = WorkbookUtils.getRow(configSheet, 3);
        Integer headerRow = WorkbookUtils.readAsInteger(configRow, 1);
        configRow = WorkbookUtils.getRow(configSheet, 4);
        Integer rowCnt = WorkbookUtils.readAsInteger(configRow, 1);
        configRow = WorkbookUtils.getRow(configSheet, 5);
        Integer col1 = WorkbookUtils.readAsInteger(configRow, 1);
        configRow = WorkbookUtils.getRow(configSheet, 6);
        Integer row1 = WorkbookUtils.readAsInteger(configRow, 1);
        configRow = WorkbookUtils.getRow(configSheet, 7);
        Integer col2 = WorkbookUtils.readAsInteger(configRow, 1);
        configRow = WorkbookUtils.getRow(configSheet, 8);
        Integer row2 = WorkbookUtils.readAsInteger(configRow, 1);
        configRow = WorkbookUtils.getRow(configSheet, 9);
        String qrKey = WorkbookUtils.readAsString(configRow, 1);

        configRow = WorkbookUtils.getRow(configSheet, 10);
        String isMultipleTemplates = WorkbookUtils.readAsString(configRow, 1);

        configRow = WorkbookUtils.getRow(configSheet, 14);
        String isMultipleTemplates2 = WorkbookUtils.readAsString(configRow, 1);

        Sheet columnSheet = workbook.getSheet("COLUMNS");
        Map<String, Integer> columnHeaderMap = getColumnHeaderInfo(columnSheet);
        if(MapUtils.isEmpty(columnHeaderMap)) {
            throw new BusinessError("There is no column setting sheet");
        }


        //˙要复制的 Sheet，主数据 Sheet
        Sheet sheet = workbook.getSheet(sheetName);
        int rowNum = headerRow;
        int masterSheetIdx = workbook.getSheetIndex(sheetName);
        workbook.setSheetHidden(masterSheetIdx, true);


//        填充 表主体
        int recordCnt = (records == null ? 0 : records.size());
        int pageCnt = new Double(Math.ceil((recordCnt * 1.0) / rowCnt)).intValue();

        if (records.size() == 0) {
            pageCnt = 1;
        }

        //Header Map
        Sheet headerSetting = workbook.getSheet("HEADERS");
        Map<String, List<Integer>> headerMap = getHeaderInfo(headerSetting);

        //BarCode
        String qrCodeValue = (String) parameters.get(qrKey);
        BufferedImage qrImage = QRCodeUtils.generateQRCodeNoBlank(qrCodeValue, 100, "png");

        int rowCount = rowCnt.intValue();

        // 如果是多重模板并且数据量大于第一页模板的数据则启用多重模板
        if (recordCnt > rowCount && isMultipleTemplates != null && isMultipleTemplates.equals("1")) {
            configRow = WorkbookUtils.getRow(configSheet, 11);
            String sheetName2 = WorkbookUtils.readAsString(configRow, 1);
            configRow = WorkbookUtils.getRow(configSheet, 12);
            Integer rowCnt2 = WorkbookUtils.readAsInteger(configRow, 1);
            configRow = WorkbookUtils.getRow(configSheet, 13);
            Integer headerRow2 = WorkbookUtils.readAsInteger(configRow, 1);
            int rowCount2 = rowCnt2.intValue();
            pageCnt = 1;
            int pageCnt2 = new Double(Math.ceil(((recordCnt - rowCnt) * 1.0) / rowCnt2)).intValue();

            Sheet sheet2 = workbook.getSheet(sheetName2);
            int masterSheetIdx2 = workbook.getSheetIndex(sheetName2);
            workbook.setSheetHidden(masterSheetIdx2, true);

            for (int p = 0; p < pageCnt; p++) {
                Sheet clonedSheet = workbook.cloneSheet(masterSheetIdx);
                int sheetIdx = workbook.getSheetIndex(clonedSheet.getSheetName());
                workbook.setSheetName(sheetIdx, sheetName + p);

                rowNum = headerRow;
                for (int i = 0; i < rowCount; i++) {
                    Map<String, Object> record = (Map<String, Object>) StringUtils.toMap(records.get(rowCount*p + i));
                    Row row = WorkbookUtils.getRow(clonedSheet, rowNum++);
                    columnHeaderMap.forEach((key, value) -> {
                        WorkbookUtils.getCell(row, value).setCellValue((String) record.get(key));
                    });
                }
                //填充表头
                headerMap.forEach((key, value) -> {
                    Row row = WorkbookUtils.getRow(clonedSheet, value.get(1));
                    if (parameters.get(key) instanceof Date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = (Date) parameters.get(key);
                        WorkbookUtils.getCell(row, value.get(0)).setCellValue(sdf.format(date));
                    } else {
                        WorkbookUtils.getCell(row, value.get(0)).setCellValue((String) parameters.get(key));
                    }
                });
                //添加 二维码图片
                CreationHelper helper = clonedSheet.getWorkbook().getCreationHelper();
                ClientAnchor anchor = helper.createClientAnchor();
                // 图片插入坐标
                anchor.setCol1((short) (col1.intValue() - 1));
                anchor.setRow1(row1 - 1);
                anchor.setCol2((short) (col2.intValue() - 1));
                anchor.setRow2(row2 - 1);
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                XSSFPicture pic = WorkbookUtils.addQrPic((XSSFSheet) clonedSheet, (XSSFWorkbook) workbook, qrImage, anchor);
            }

            Sheet headerSetting2 = workbook.getSheet("HEADERS2");
            Map<String, List<Integer>> headerMap2 = getHeaderInfo(headerSetting2);

            for (int p = 0; p < pageCnt2; p++) {
                Sheet clonedSheet = workbook.cloneSheet(masterSheetIdx2);
                int sheetIdx = workbook.getSheetIndex(clonedSheet.getSheetName());
                workbook.setSheetName(sheetIdx, sheetName2 + p);

                rowNum = headerRow2;
                for (int i = 0; i < rowCount2; i++) {
                    Map<String, Object> record = (Map<String, Object>) StringUtils.toMap(records.get(rowCount2*p + i + rowCount));
                    Row row = WorkbookUtils.getRow(clonedSheet, rowNum++);
                    columnHeaderMap.forEach((key, value) -> {
                        WorkbookUtils.getCell(row, value).setCellValue((String) record.get(key));
                    });
                }
                //填充表头
                headerMap2.forEach((key, value) -> {
                    Row row = WorkbookUtils.getRow(clonedSheet, value.get(1));
                    if (parameters.get(key) instanceof Date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = (Date) parameters.get(key);
                        WorkbookUtils.getCell(row, value.get(0)).setCellValue(sdf.format(date));
                    } else {
                        WorkbookUtils.getCell(row, value.get(0)).setCellValue((String) parameters.get(key));
                    }
                });
                //添加 二维码图片
                CreationHelper helper = clonedSheet.getWorkbook().getCreationHelper();
                ClientAnchor anchor = helper.createClientAnchor();
                // 图片插入坐标
                anchor.setCol1((short) (col1.intValue() - 1));
                anchor.setRow1(row1 - 1);
                anchor.setCol2((short) (col2.intValue() - 1));
                anchor.setRow2(row2 - 1);
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                XSSFPicture pic = WorkbookUtils.addQrPic((XSSFSheet) clonedSheet, (XSSFWorkbook) workbook, qrImage, anchor);
            }

            if (isMultipleTemplates2 != null && isMultipleTemplates2.equals("1")) {
                configRow = WorkbookUtils.getRow(configSheet, 15);
                String sheetName3 = WorkbookUtils.readAsString(configRow, 1);

                Sheet sheet3 = workbook.getSheet(sheetName3);
                int masterSheetIdx3 = workbook.getSheetIndex(sheetName3);
                workbook.setSheetHidden(masterSheetIdx3, true);

                for (int p = 0; p < pageCnt; p++) {
                    Sheet clonedSheet = workbook.cloneSheet(masterSheetIdx3);
                    int sheetIdx = workbook.getSheetIndex(clonedSheet.getSheetName());
                    workbook.setSheetName(sheetIdx, sheetName3 + p);
                    //填充表头
                    headerMap.forEach((key, value) -> {
                        Row row = WorkbookUtils.getRow(clonedSheet, value.get(1));
                        if (parameters.get(key) instanceof Date) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Date date = (Date) parameters.get(key);
                            WorkbookUtils.getCell(row, value.get(0)).setCellValue(sdf.format(date));
                        } else {
                            WorkbookUtils.getCell(row, value.get(0)).setCellValue((String) parameters.get(key));
                        }
                    });
                    //添加 二维码图片
                    CreationHelper helper = clonedSheet.getWorkbook().getCreationHelper();
                    ClientAnchor anchor = helper.createClientAnchor();
                    // 图片插入坐标
                    anchor.setCol1((short) (col1.intValue() - 1));
                    anchor.setRow1(row1 - 1);
                    anchor.setCol2((short) (col2.intValue() - 1));
                    anchor.setRow2(row2 - 1);
                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
                    XSSFPicture pic = WorkbookUtils.addQrPic((XSSFSheet) clonedSheet, (XSSFWorkbook) workbook, qrImage, anchor);
                }
            }

        } else {
            for(int p = 0; p < pageCnt; p++) {
                Sheet clonedSheet = workbook.cloneSheet(masterSheetIdx);
                int sheetIdx = workbook.getSheetIndex(clonedSheet.getSheetName());
                workbook.setSheetName(sheetIdx, sheetName + p);

                rowNum = headerRow;
                for (int i = 0; i < rowCount; i++) {
                    Map<String, Object> record = (Map<String, Object>) StringUtils.toMap(records.get(rowCount*p + i));

                    Row row = WorkbookUtils.getRow(clonedSheet, rowNum++);
                    columnHeaderMap.forEach((key, value) -> {
                        WorkbookUtils.getCell(row, value).setCellValue((String) record.get(key));

                    });

                }

                //填充表头
                headerMap.forEach((key, value) -> {
                    Row row = WorkbookUtils.getRow(clonedSheet, value.get(1));
                    if (parameters.get(key) instanceof Date) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = (Date) parameters.get(key);

//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    formatter.format(date);
//                    LocalDate date = LocalDate.parse("2017 06 17", formatter);wps-simple
//                    System.out.println(formatter.format(date));
//                    localDate.format(DateTimeFormatter.ISO_DATE);
                        WorkbookUtils.getCell(row, value.get(0)).setCellValue(sdf.format(date));
                    } else {
                        WorkbookUtils.getCell(row, value.get(0)).setCellValue((String) parameters.get(key));
                    }
                });

                //添加 二维码图片
                CreationHelper helper = clonedSheet.getWorkbook().getCreationHelper();
                ClientAnchor anchor = helper.createClientAnchor();

                // 图片插入坐标
                anchor.setCol1((short) (col1.intValue() - 1));
                anchor.setRow1(row1 - 1);
                anchor.setCol2((short) (col2.intValue() - 1));
                anchor.setRow2(row2 - 1);

                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
//        anchor.setPosition();
                XSSFPicture pic = WorkbookUtils.addQrPic((XSSFSheet) clonedSheet, (XSSFWorkbook) workbook, qrImage, anchor);
//            pic.resize(0.7, 1); //TODO 调整
            }
        }

//        try {
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


    private Map<String, Integer> getColumnHeaderInfo(Sheet sheet){

        int rowNum = 2;
        Map<String, Integer> headerColumnMap = new HashMap<>();

        while (true){
            Row row = WorkbookUtils.getRow(sheet, rowNum++);
            if(StringUtils.isEmpty(WorkbookUtils.readAsString(row, 0))) break;
            headerColumnMap.put(WorkbookUtils.readAsString(row, 0), WorkbookUtils.readAsInteger(row, 1)-1);

        }
        return headerColumnMap;
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
