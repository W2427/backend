package com.ose.tasks.domain.model.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.feign.RequestWrapper;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.ExportExcelRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.entity.ExportExcel;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.util.FileUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Component
public class XlsExportService implements XlsExportInterface {
    private final static Logger logger = LoggerFactory.getLogger(XlsExportService.class);


    private ExportExcelRepository exportExcelRepository;


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;


    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;


    private UploadFeignAPI uploadFeignAPI;

    private final DrawingRepository drawingRepository;

    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final WBSEntryRepository wbsEntryRepository;


    @Value("${spring.servlet.multipart.location}")
    private String multipartFormDataDir;

    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    @Autowired
    public XlsExportService(
        ExportExcelRepository exportExcelRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService,
        DrawingRepository drawingRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        WBSEntryRepository wbsEntryRepository) {
        this.exportExcelRepository = exportExcelRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.drawingRepository = drawingRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.wbsEntryRepository = wbsEntryRepository;
    }

    /**
     * 取得指定组织的所有导出的XLS 视图。
     *
     * @param orgId     组织 ID
     * @param projectId 项目ID
     * @return 项目分页数据
     */
    @Override
    public Page<ExportExcel> getList(Long orgId, Long projectId, PageDTO pageDTO) {


        return exportExcelRepository
            .findByOrgIdAndProjectIdAndDeletedIsFalse(orgId, projectId, pageDTO.toPageable());



    }

    /**
     * 保存实体下载临时文件(WHS)。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param exportView 项目ID
     */
    @Override
    public File saveWhsDownloadFile(Long orgId, Long projectId, String exportView) throws FileNotFoundException {

        String moduleNo = exportView.substring(exportView.lastIndexOf("-") + 1);
        List<String> weldTypeList = new ArrayList<>();
        weldTypeList.add("TBUTT");
        weldTypeList.add("TTJ");
        weldTypeList.add("TTKY");
        weldTypeList.add("TFW");
        weldTypeList.add("TT CONNECTION");
        weldTypeList.add("TBW");

        String temporaryFileName = exportView + System.currentTimeMillis() + ".xlsx";
        String whsFilePath = "/var/ose/private/upload/" + temporaryFileName;
        File excel = new File(whsFilePath);

        String template = "/var/ose/resources/templates/reports/WHS.xlsx";

        OutputStream outputStream = null;
        outputStream = new FileOutputStream(new File(whsFilePath));
        ExcelWriter excelWriter = EasyExcel.write(outputStream).withTemplate(template).build();
//        WriteSheet test1 = EasyExcel.writerSheet(0, "PRIMARY,SECOND").head(Whs.class).build();
//        WriteSheet test2 = EasyExcel.writerSheet(1, "TERTIARY").head(Whs.class).build();
//        excelWriter.write(whsList, test1).write(whsAssemblyList,test2);
//        excelWriter.finish();

        excelWriter.finish();

//        ExcelWriterBuilder writerBuilder = EasyExcel.write(whsFilePath, Whs.class).withTemplate(template);
//        writerBuilder.sheet(0, "PRIMARY,SECOND").doFill(whsList);
//        writerBuilder.sheet(1, "TERTIARY").doFill(whsAssemblyList);



        return excel;
    }

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param exportView 项目ID
     * @return 实体下载临时文件
     */
    @Override
    public File saveDownloadFile(Long orgId, Long projectId, String exportView) {

        String exportAssemblyView = "3000-D-EC-000-CN-ITP-0818-00_05U-08-ASSEMBLY-WHS GBS3-BLM1";
        String sql = "select * from `" + exportView + "` where project_id = " + projectId + " and org_id = " + orgId + "";
        String sqlCount = "select count(1) from `" + exportView + "` where project_id = " + projectId + " and org_id = " + orgId + "";
        String sqlTitle = "DESC `" + exportView + "`";
        String sql1 = "select * from `" + exportAssemblyView + "` where project_id = " + projectId + " and org_id = " + orgId + "";
        String sqlCount1 = "select count(1) from `" + exportAssemblyView + "` where project_id = " + projectId + " and org_id = " + orgId + "";
        String sqlTitle1 = "DESC `" + exportAssemblyView + "`";

        if(exportView.equals("pannel_followup_gbs")){
            InputStream inputStream = null;
            String WHSTemplateFile = "/var/ose/resources/templates/reports/pannel_followup_GBS.xlsx";
            try {
                inputStream = new FileInputStream(WHSTemplateFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            String WHSTemporaryFileName = FileUtils.copy(
                inputStream,
                multipartFormDataDir,
                "12345678"
            );

            File excel;
            Workbook workbook = null;
            try {
                Long recordCount = exportExcelRepository.findRowCount(sqlCount);
                System.out.println(sqlCount);

                excel = new File(multipartFormDataDir, WHSTemporaryFileName);
                workbook = WorkbookFactory.create(excel);
                Sheet configSheet = workbook.getSheet("SETTINGS");
                Row configRow = WorkbookUtils.getRow(configSheet, 2);
                String sheetName = WorkbookUtils.readAsString(configRow, 1);

                Sheet columnSheet = workbook.getSheet("COLUMNS");
                Map<String, Integer> columnHeaderMap = getColumnHeaderInfo(columnSheet);
                if (MapUtils.isEmpty(columnHeaderMap)) {
                    throw new BusinessError("There is no column setting sheet");
                }

                int pageSize = 6000;
                int pageCount = (int) (recordCount / pageSize + 1);

                List<String> titleList = exportExcelRepository.findColumnName(sqlTitle);

                Sheet sheet = workbook.getSheet(sheetName);

                for (int p = 0; p < pageCount; p++) {
                    String sqlExec = sql + " limit " + p * pageSize + ", " + pageSize;
                    System.out.println(sqlExec);
                    List<List<Object>> data = exportExcelRepository.findViewData(sqlExec);

                    List<Map<String, Object>> records = new ArrayList<>();
                    for (List<Object> singleData : data) {
                        Map<String, Object> record = new HashMap<>();
                        for (int i = 0; i < singleData.size(); i++) {

                            record.put(titleList.get(i), singleData.get(i));
                        }
                        records.add(record);
                    }

                    int idx = 2 + p * pageSize;
                    for (int j = 0; j < data.size(); j++) {
                        Row row = WorkbookUtils.getRow(sheet, idx++);
                        Map<String, Object> record = records.get(j);
                        columnHeaderMap.forEach((key, value) -> {
                            if (record.get(key) instanceof Double) {
                                record.put(key, String.valueOf(record.get(key)));
                            }
                            if (record.get(key) instanceof Integer) {
                                record.put(key, record.get(key));
                            }
                            WorkbookUtils.getCell(row, value).setCellValue(record.get(key).toString());
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String temporaryFileName = exportView + System.currentTimeMillis() + ".xlsx";

            try {
                workbook.write(new FileOutputStream(temporaryDir + "/" + temporaryFileName));

                excel = new File(temporaryDir, temporaryFileName);
                workbook.close();
                return excel;
            } catch (IOException e) {
                e.printStackTrace(System.out);
                throw new BusinessError();
            }
        }
        else if (exportView.contains("3000-D-EC-000-CN-ITP-0818-00_05U-08-WHS")) {
            InputStream inputStream = null;
            String WHSTemplateFile = "/var/ose/resources/templates/reports/WHS.xlsx";
            try {
                inputStream = new FileInputStream(WHSTemplateFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            String WHSTemporaryFileName = FileUtils.copy(
                inputStream,
                multipartFormDataDir,
                "12345678"
            );


            File excel;
            Workbook workbook = null;

            try {
                Long recordCount = exportExcelRepository.findRowCount(sqlCount);
                Long assemblyRecordCount = exportExcelRepository.findRowCount(sqlCount1);
                System.out.println(sqlCount);

                excel = new File(multipartFormDataDir, WHSTemporaryFileName);
                workbook = WorkbookFactory.create(excel);
                Sheet configSheet = workbook.getSheet("SETTINGS");
                Row configRow = WorkbookUtils.getRow(configSheet, 2);
                String sheetName = WorkbookUtils.readAsString(configRow, 1);
                Row assemblyConfigRow = WorkbookUtils.getRow(configSheet, 5);
                String assemblySheetName = WorkbookUtils.readAsString(assemblyConfigRow, 1);

                Sheet columnSheet = workbook.getSheet("COLUMNS");
                Map<String, Integer> columnHeaderMap = getColumnHeaderInfo(columnSheet);
                if (MapUtils.isEmpty(columnHeaderMap)) {
                    throw new BusinessError("There is no column setting sheet");
                }

                int pageSize = 6000;
                int pageCount = (int) (recordCount / pageSize + 1);
                int assemblyPageCount = (int) (assemblyRecordCount / pageSize + 1);
                int pageNo = 1;

                List<String> titleList = exportExcelRepository.findColumnName(sqlTitle);
                List<String> assemblyTitleList = exportExcelRepository.findColumnName(sqlTitle1);

                int column = titleList.size();

                Sheet sheet = workbook.getSheet(sheetName);
                Sheet assemblySheet = workbook.getSheet(assemblySheetName);

                for (int p = 0; p < pageCount; p++) {
                    String sqlExec = sql + " limit " + p * pageSize + ", " + pageSize;
                    System.out.println(sqlExec);
                    List<List<Object>> data = exportExcelRepository.findViewData(sqlExec);

                    List<Map<String, Object>> records = new ArrayList<>();
                    for (List<Object> singleData : data) {
                        Map<String, Object> record = new HashMap<>();
                        for (int i = 0; i < singleData.size(); i++) {

                            record.put(titleList.get(i), singleData.get(i));
                        }
                        records.add(record);
                    }

                    int idx = 1 + p * pageSize;
                    for (int j = 0; j < data.size(); j++) {
                        Row row = WorkbookUtils.getRow(sheet, idx++);
                        Map<String, Object> record = records.get(j);
                        columnHeaderMap.forEach((key, value) -> {
                            if (record.get(key) instanceof Double) {
                                record.put(key, String.valueOf(record.get(key)));
                            }
//                            logger.info("当前插入：" + record.get("新编号") + "的" + key);
                            WorkbookUtils.getCell(row, value).setCellValue(( String) record.get(key));
                        });
                    }
                }
                for (int p = 0; p < assemblyPageCount; p++) {
                    String sqlExec1 = sql1 + " limit " + p * pageSize + ", " + pageSize;
                    List<List<Object>> assemblyData = exportExcelRepository.findViewData(sqlExec1);

                    if (assemblyData.size() > 0) {
                        List<Map<String, Object>> assemblyRecords = new ArrayList<>();
                        for (List<Object> singleData : assemblyData) {
                            Map<String, Object> record = new HashMap<>();
                            for (int i = 0; i < singleData.size(); i++) {

                                record.put(assemblyTitleList.get(i), singleData.get(i));
                            }
                            assemblyRecords.add(record);
                        }

                        int idx = 1 + p * pageSize;
                        for (int j = 0; j < assemblyData.size(); j++) {
                            Row row = WorkbookUtils.getRow(assemblySheet, idx++);
                            Map<String, Object> record = assemblyRecords.get(j);
                            columnHeaderMap.forEach((key, value) -> {
                                if (record.get(key) instanceof Double) {
                                    record.put(key, String.valueOf(record.get(key)));
                                }
                                WorkbookUtils.getCell(row, value).setCellValue((String) record.get(key));
                            });
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            String temporaryFileName = exportView + System.currentTimeMillis() + ".xlsx";

            try {
                workbook.write(new FileOutputStream(temporaryDir + "/" + temporaryFileName));

                excel = new File(temporaryDir, temporaryFileName);
                workbook.close();
                return excel;
            } catch (IOException e) {
                e.printStackTrace(System.out);
                throw new BusinessError();
            }
        } else if (exportView.contains("NDT_BACKLOG_DECK_WISE")) {

            InputStream inputStream = null;
            String WHSTemplateFile = "/var/ose/resources/templates/reports/NDT-BACKLOG-DECK WISE.xlsx";

            try {
                inputStream = new FileInputStream(WHSTemplateFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String WHSTemporaryFileName = FileUtils.copy(
                inputStream,
                multipartFormDataDir,
                "12345678"
            );

            File excel;
            Workbook workbook = null;

            try {
                Long recordCount = exportExcelRepository.findRowCount(sqlCount);

                excel = new File(multipartFormDataDir, WHSTemporaryFileName);
                workbook = WorkbookFactory.create(excel);
                Sheet configSheet = workbook.getSheet("SETTINGS");
                Row configRow = WorkbookUtils.getRow(configSheet, 2);
                String sheetName = WorkbookUtils.readAsString(configRow, 1);

                Sheet columnSheet = workbook.getSheet("COLUMNS");
                Map<String, Integer> columnHeaderMap = getColumnHeaderInfo(columnSheet);
                if (MapUtils.isEmpty(columnHeaderMap)) {
                    throw new BusinessError("There is no column setting sheet");
                }

                int pageSize = 6000;
                int pageCount = (int) (recordCount / pageSize + 1);
                int pageNo = 1;

                List<String> titleList = exportExcelRepository.findColumnName(sqlTitle);
                int column = titleList.size();

                final double[] utWeldLengthTotal = {0.0};
                final double[] requiredUtLengthTotal = {0.0};
                final double[] utTestedLengthTotal = {0.0};
                final double[] utOutstandingLengthTotal = {0.0};
                final double[] mtWeldLengthTotal = {0.0};
                final double[] requiredMtLengthTotal = {0.0};
                final double[] mtTestedLengthTotal = {0.0};
                final double[] mtOutstandingLengthTotal = {0.0};
                Sheet sheet = workbook.getSheet(sheetName);
                for (int p = 0; p < pageCount; p++) {
                    String sqlExec = sql + " order by 模块名,甲板名" + " limit " + p * pageSize + ", " + pageSize;
                    System.out.println(sqlExec);
                    List<List<Object>> data = exportExcelRepository.findViewData(sqlExec);

                    List<Map<String, Object>> records = new ArrayList<>();
                    for (List<Object> singleData : data) {
                        Map<String, Object> record = new HashMap<>();
                        for (int i = 0; i < singleData.size(); i++) {

                            record.put(titleList.get(i), singleData.get(i));
                        }
                        records.add(record);
                    }

                    int idx = 2 + p * pageSize;
                    for (int j = 0; j < data.size(); j++) {
                        Row row = WorkbookUtils.getRow(sheet, idx++);
                        Map<String, Object> record = records.get(j);
                        columnHeaderMap.forEach((key, value) -> {
                            if (record.get(key) instanceof Double) {
                                record.put(key, String.valueOf(record.get(key)));
                            }
                            WorkbookUtils.getCell(row, value).setCellValue((String) record.get(key));
                            if ("UT总长度".equals(key)) {
                                utWeldLengthTotal[0] += Double.parseDouble(record.get("UT总长度").toString());
                            }
                            if ("UT所需长度".equals(key)) {
                                requiredUtLengthTotal[0] += Double.parseDouble(record.get("UT所需长度").toString());
                            }
                            if ("UT已检测长度".equals(key)) {
                                utTestedLengthTotal[0] += Double.parseDouble(record.get("UT已检测长度").toString());
                            }
                            if ("UT未检测长度".equals(key)) {
                                utOutstandingLengthTotal[0] += Double.parseDouble(record.get("UT未检测长度").toString());
                            }
                            if ("MT总长度".equals(key)) {
                                mtWeldLengthTotal[0] += Double.parseDouble(record.get("MT总长度").toString());
                            }
                            if ("MT所需长度".equals(key)) {
                                requiredMtLengthTotal[0] += Double.parseDouble(record.get("MT所需长度").toString());
                            }
                            if ("MT已检测长度".equals(key)) {
                                mtTestedLengthTotal[0] += Double.parseDouble(record.get("MT已检测长度").toString());
                            }
                            if ("MT未检测长度".equals(key)) {
                                mtOutstandingLengthTotal[0] += Double.parseDouble(record.get("MT未检测长度").toString());
                            }
                        });
                    }
                    Row totalRow = WorkbookUtils.getRow(sheet, data.size()+2);
                    WorkbookUtils.getCell(totalRow, 0).setCellValue("Summary");
                    WorkbookUtils.getCell(totalRow, 1).setCellValue("");
                    WorkbookUtils.getCell(totalRow, 2).setCellValue("");
                    WorkbookUtils.getCell(totalRow, 3).setCellValue("");
                    WorkbookUtils.getCell(totalRow, 4).setCellValue(utWeldLengthTotal[0]);
                    WorkbookUtils.getCell(totalRow, 5).setCellValue(requiredUtLengthTotal[0]);
                    WorkbookUtils.getCell(totalRow, 6).setCellValue(utTestedLengthTotal[0]);
                    WorkbookUtils.getCell(totalRow, 7).setCellValue(utOutstandingLengthTotal[0]);
                    WorkbookUtils.getCell(totalRow, 8).setCellValue("");
                    WorkbookUtils.getCell(totalRow, 9).setCellValue("");
                    WorkbookUtils.getCell(totalRow, 10).setCellValue(mtWeldLengthTotal[0]);
                    WorkbookUtils.getCell(totalRow, 11).setCellValue(requiredMtLengthTotal[0]);
                    WorkbookUtils.getCell(totalRow, 12).setCellValue(mtTestedLengthTotal[0]);
                    WorkbookUtils.getCell(totalRow, 13).setCellValue(mtOutstandingLengthTotal[0]);
                    WorkbookUtils.getCell(totalRow, 14).setCellValue("");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String temporaryFileName = exportView + System.currentTimeMillis() + ".xlsx";
            try {
                workbook.write(new FileOutputStream(temporaryDir + "/" + temporaryFileName));

                excel = new File(temporaryDir, temporaryFileName);
                workbook.close();
                return excel;
            } catch (IOException e) {
                e.printStackTrace(System.out);
                throw new BusinessError();
            }
        } else if (exportView.contains("shop_drawing_completion_status")) {

            InputStream inputStream = null;
            String WHSTemplateFile = "/var/ose/resources/templates/reports/shop_drawing_completion_status.xlsx";

            try {
                inputStream = new FileInputStream(WHSTemplateFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String WHSTemporaryFileName = FileUtils.copy(
                inputStream,
                multipartFormDataDir,
                "12345678"
            );

            File excel;
            Workbook workbook = null;

            try {
                excel = new File(multipartFormDataDir, WHSTemporaryFileName);
                workbook = WorkbookFactory.create(excel);
                Sheet configSheet = workbook.getSheet("SETTINGS");
                Row configRow = WorkbookUtils.getRow(configSheet, 2);
                String sheetName = WorkbookUtils.readAsString(configRow, 1);

                // 查询图纸数据
                List<Drawing> drawings = drawingRepository.findByOrgIdAndProjectIdAndStatus(orgId, projectId, EntityStatus.ACTIVE);
                List<BpmActivityInstanceBase> drawingFinishedTotal = bpmActivityInstanceRepository.findByProjectIdAndProcessNameCn(projectId);
                List<BpmActivityInstanceBase> drawingFinishedOnTimeTotal = bpmActivityInstanceRepository.findByProjectIdAndPlanEndDate(projectId);

                Sheet sheet = workbook.getSheet(sheetName);
                Row totalRow = WorkbookUtils.getRow(sheet, 1);
                WorkbookUtils.getCell(totalRow, 1).setCellValue(drawings.size());
                WorkbookUtils.getCell(totalRow, 2).setCellValue(drawingFinishedTotal.size());
                WorkbookUtils.getCell(totalRow, 3).setCellValue(drawingFinishedOnTimeTotal.size());
                if (drawings.size() > 0) {
                    WorkbookUtils.getCell(totalRow, 4).setCellValue(Double.valueOf(drawingFinishedTotal.size()) / drawings.size());
                    WorkbookUtils.getCell(totalRow, 5).setCellValue(Double.valueOf(drawingFinishedOnTimeTotal.size()) / drawings.size());
                } else {
                    WorkbookUtils.getCell(totalRow, 4).setCellValue(0);
                    WorkbookUtils.getCell(totalRow, 5).setCellValue(0);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            String temporaryFileName = exportView + System.currentTimeMillis() + ".xlsx";
            try {
                workbook.write(new FileOutputStream(temporaryDir + "/" + temporaryFileName));

                excel = new File(temporaryDir, temporaryFileName);
                workbook.close();
                return excel;
            } catch (IOException e) {
                e.printStackTrace(System.out);
                throw new BusinessError();
            }
        } else if (exportView.contains("NDT_first_pass_ratio")) {

            InputStream inputStream = null;
            String WHSTemplateFile = "/var/ose/resources/templates/reports/NDT_first_pass_ratio.xlsx";

            try {
                inputStream = new FileInputStream(WHSTemplateFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String WHSTemporaryFileName = FileUtils.copy(
                inputStream,
                multipartFormDataDir,
                "12345678"
            );

            File excel;
            Workbook workbook = null;

            try {
                excel = new File(multipartFormDataDir, WHSTemporaryFileName);
                workbook = WorkbookFactory.create(excel);
                Sheet configSheet = workbook.getSheet("SETTINGS");
                Row configRow = WorkbookUtils.getRow(configSheet, 2);
                String sheetName = WorkbookUtils.readAsString(configRow, 1);

                // 查询ndt第一次完成相关数据
                List<BpmActivityInstanceBase> ndtTotal = bpmActivityInstanceRepository.findByProjectIdAndFinishStateIsNull(projectId);
                List<BpmActivityInstanceBase> ndtFinishedTotal = bpmActivityInstanceRepository.findByProjectIdAndFinishStateIsFinished(projectId);

                Sheet sheet = workbook.getSheet(sheetName);
                Row totalRow = WorkbookUtils.getRow(sheet, 1);
                WorkbookUtils.getCell(totalRow, 1).setCellValue(ndtTotal.size());
                WorkbookUtils.getCell(totalRow, 2).setCellValue(ndtFinishedTotal.size());
                if (ndtTotal.size() > 0) {
                    WorkbookUtils.getCell(totalRow, 3).setCellValue(Double.valueOf(ndtFinishedTotal.size()) / ndtTotal.size());
                } else {
                    WorkbookUtils.getCell(totalRow, 3).setCellValue(0);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            String temporaryFileName = exportView + System.currentTimeMillis() + ".xlsx";
            try {
                workbook.write(new FileOutputStream(temporaryDir + "/" + temporaryFileName));

                excel = new File(temporaryDir, temporaryFileName);
                workbook.close();
                return excel;
            } catch (IOException e) {
                e.printStackTrace(System.out);
                throw new BusinessError();
            }
        }  else if (exportView.contains("construction_plan_completion status")) {

            InputStream inputStream = null;
            String WHSTemplateFile = "/var/ose/resources/templates/reports/construction_plan_completion_status.xlsx";

            try {
                inputStream = new FileInputStream(WHSTemplateFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            String WHSTemporaryFileName = FileUtils.copy(
                inputStream,
                multipartFormDataDir,
                "12345678"
            );

            File excel;
            Workbook workbook = null;

            try {
                excel = new File(multipartFormDataDir, WHSTemporaryFileName);
                workbook = WorkbookFactory.create(excel);
                Sheet configSheet = workbook.getSheet("SETTINGS");
                Row configRow = WorkbookUtils.getRow(configSheet, 2);
                String sheetName = WorkbookUtils.readAsString(configRow, 1);

                // 查询ndt第一次完成相关数据
                List<WBSEntry> planFinishedTotal = wbsEntryRepository.findByProjectIdAndFinished(projectId);
                List<WBSEntry> planFinishedOnTimeTotal = wbsEntryRepository.findByProjectIdAndFinishedOnTime(projectId);

                Sheet sheet = workbook.getSheet(sheetName);
                Row totalRow = WorkbookUtils.getRow(sheet, 1);
                WorkbookUtils.getCell(totalRow, 1).setCellValue(planFinishedTotal.size());
                WorkbookUtils.getCell(totalRow, 2).setCellValue(planFinishedOnTimeTotal.size() * 3);
                if (planFinishedTotal.size() > 0) {
                    WorkbookUtils.getCell(totalRow, 3).setCellValue(Double.valueOf(planFinishedOnTimeTotal.size()) / planFinishedTotal.size() * 3);
                } else {
                    WorkbookUtils.getCell(totalRow, 3).setCellValue(0);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            String temporaryFileName = exportView + System.currentTimeMillis() + ".xlsx";
            try {
                workbook.write(new FileOutputStream(temporaryDir + "/" + temporaryFileName));

                excel = new File(temporaryDir, temporaryFileName);
                workbook.close();
                return excel;
            } catch (IOException e) {
                e.printStackTrace(System.out);
                throw new BusinessError();
            }
        } else {
            File excel;
            SXSSFWorkbook workbook = new SXSSFWorkbook();

            try {
                Long recordCount = exportExcelRepository.findRowCount(sqlCount);
                System.out.println(sqlCount);

                int pageSize = 6000;
                int pageCount = (int) (recordCount / pageSize + 1);
                int pageNo = 1;

                List<String> titleList = exportExcelRepository.findColumnName(sqlTitle);
                int column = titleList.size();


                SXSSFSheet sheet = workbook.createSheet(exportView);

                sheet.setRandomAccessWindowSize(-1);


                SXSSFRow row1 = sheet.createRow(0);
                for (int i = 0; i < titleList.size(); i++) {
                    String name = titleList.get(i);

                    SXSSFCell cell = row1.createCell(i);

                    cell.setCellValue(name);
                }

                for (int p = 0; p < pageCount; p++) {
                    String sqlExec = sql + " limit " + p * pageSize + ", " + pageSize;
                    System.out.println(sqlExec);

                    List<List<Object>> data = exportExcelRepository.findViewData(sqlExec);

                    int idx = 1 + p * pageSize;
                    for (int j = 0; j < data.size(); j++) {

                        SXSSFRow row = sheet.createRow(idx++);
                        for (int i = 0; i < column; i++) {
                            String str = String.valueOf(data.get(j).get(i));
                            if (str.equals("null")) str = "";

                            SXSSFCell cell = row.createCell(i);

                            cell.setCellValue(str);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String temporaryFileName = exportView + System.currentTimeMillis() + ".xls";

            try {
                workbook.write(new FileOutputStream(temporaryDir + "/" + temporaryFileName));

                excel = new File(temporaryDir, temporaryFileName);
                workbook.close();
                return excel;
            } catch (IOException e) {
                e.printStackTrace(System.out);
                throw new BusinessError();
            }
        }
    }

    private Map<String, Integer> getColumnHeaderInfo(Sheet sheet) {

        int rowNum = 2;
        Map<String, Integer> headerColumnMap = new HashMap<>();

        while (true) {
            Row row = WorkbookUtils.getRow(sheet, rowNum++);
            if (StringUtils.isEmpty(WorkbookUtils.readAsString(row, 0))) break;
            headerColumnMap.put(WorkbookUtils.readAsString(row, 0), WorkbookUtils.readAsInteger(row, 1) - 1);

        }
        return headerColumnMap;
    }

    @Override
    public List<ExportExcel> getViewName(Long orgId, Long projectId, String excelView) {
        return exportExcelRepository.findByViewName(orgId, projectId, excelView);
    }

    @Override
    public Future<String> saveAsyncDownloadFile(ContextDTO context, Long orgId, Long projectId, ExportExcel exportExcel) {

        /*  1. 检查文件生成的时间
            2. 如果文件已经不在2小时内，则生成线程生成xlsx文件
            3. 长传保存文件
            4. 更新export_excel中的文件生成时间
            5. 如果export_excel表中生成文件的时间在2小时内，则直接下载文件
         */

        exportExcel.setGenerating(true);
        exportExcel.setExcelGenDate(System.currentTimeMillis());
        exportExcelRepository.save(exportExcel);

        String authorization = context.getAuthorization();

        if(!context.isContextSet()) {
            String userAgent = context.getUserAgent();
            RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                context.getResponse()
            );




            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }













































        context.setRequestMethod(context.getRequest().getMethod());
        Project project = projectService.get(orgId, projectId);
        batchTaskService.run(
            context,
            project,
            BatchTaskCode.XLS_EXPORT,
            batchTask -> {
                File xls_file = saveDownloadFile(orgId, projectId, exportExcel.getExcelViewName());
                FileInputStream input = null;
                MultipartFile multipartFile = null;
                try {
                    input = new FileInputStream(xls_file);
                    multipartFile = new MockMultipartFile("file",
                        xls_file.getName(),
                        "application/vnd.ms-excel",
                        IOUtils.toByteArray(input)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }


                logger.error("excel 上传遗留问题附件docs服务->开始");
                JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI.
                    uploadIssueAttachment(orgId.toString(), multipartFile);
                logger.error("excel 上传遗留问题附件docs服务->结束");
                System.out.println("ftj abc");


                logger.error("excel1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(
                    orgId.toString(),
                    projectId.toString(),
                    tempFileResBody.getData().getName(),
                    new FilePostDTO()
                );
                logger.error("excel1 保存docs服务->结束");

                System.out.println("uploaded file" + xls_file.getAbsolutePath());


                FileES fileES = fileESResBody.getData();
                exportExcel.setFileId(LongUtils.parseLong(fileES.getId()));
                exportExcel.setFilePath(fileES.getPath());
                exportExcel.setGenerating(false);
                exportExcel.setExcelGenDate(System.currentTimeMillis());
                FileUtils.remove(xls_file.getAbsolutePath());
                exportExcelRepository.save(exportExcel);
                return new BatchResultDTO();
            });
        return new AsyncResult<>("true");
    }

}
