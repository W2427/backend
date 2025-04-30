package com.ose.tasks.domain.model.service.performance.impl;


import com.alibaba.excel.EasyExcel;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.performance.PerformanceAppraisalListDetailRepository;
import com.ose.tasks.domain.model.repository.performance.PerformanceAppraisalListRepository;
import com.ose.tasks.domain.model.service.PerformanceEvaluationService;
import com.ose.tasks.domain.model.service.performance.PerformanceAppraisalListInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.xlsximport.XlsxImportInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.performance.*;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.util.easyExcel.LocalDateConverter;
import com.ose.util.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Component
public class PerformanceAppraisalListService implements PerformanceAppraisalListInterface {
    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.templateFilePath}")
    private String templateFilePath;

    @Value("${application.files.filePath}")
    private String filePath;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private static final DecimalFormat df = new DecimalFormat("#0.0");

    private final String SETTING = "SETTING";

    private static Map<String, Integer> columnMap = new HashMap<>();

    private final WBSEntityImportSheetConfigBuilder sheetConfigBuilder;

    private final PerformanceAppraisalListRepository performanceAppraisalListRepository;

    private final PerformanceAppraisalListDetailRepository performanceAppraisalListDetailRepository;

    private final PerformanceEvaluationService performanceEvaluationService;

    private final XlsxImportInterface xlsxImportService;

    private final UserFeignAPI userFeignAPI;

    public PerformanceAppraisalListService(
        WBSEntityImportSheetConfigBuilder sheetConfigBuilder,
        PerformanceAppraisalListRepository performanceAppraisalListRepository,
        PerformanceAppraisalListDetailRepository performanceAppraisalListDetailRepository,
        PerformanceEvaluationService performanceEvaluationService,
        UserFeignAPI userFeignAPI,
        XlsxImportInterface xlsxImportService) {
        this.sheetConfigBuilder = sheetConfigBuilder;
        this.performanceAppraisalListRepository = performanceAppraisalListRepository;
        this.performanceAppraisalListDetailRepository = performanceAppraisalListDetailRepository;
        this.performanceEvaluationService = performanceEvaluationService;
        this.xlsxImportService = xlsxImportService;
        this.userFeignAPI = userFeignAPI;
    }


    @Override
    public BatchResultDTO importDetailList(OperatorDTO operator,
                                           BatchTask batchTask,
                                           PerformanceAppraisalListImportDTO importDTO
    ) {
        Workbook workbook;
        File excel;

        // 读取已上传的导入文件
        try {
            excel = new File(temporaryDir, importDTO.getFileName());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new NotFoundError();
        }

        BatchResultDTO batchResult = new BatchResultDTO(batchTask);
        BatchResultDTO sheetImportResult;

        int sheetNum = workbook.getNumberOfSheets();
        if (sheetNum < 1) throw new BusinessError("there is no importSheet");

        sheetImportResult = importDetail(
            operator,
            workbook.getSheetAt(0),
            batchResult,
            importDTO.getQuarter()
        );


        batchResult.addLog(
            sheetImportResult.getProcessedCount()
                + " "
                + workbook.getSheetAt(0).getSheetName()
                + " imported."
        );

        // 保存工作簿
        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {

            e.printStackTrace(System.out); // TODO

        }

        return batchResult;
    }

    private BatchResultDTO importDetail(OperatorDTO operator, Sheet sheet, BatchResultDTO batchResult, Integer quarter) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 逐行读取数据
        Iterator<Row> rows = sheet.iterator();
        int totalCount = 0;
        int skippedCount = 0;
        int processedCount = 0;
        int errorCount = 0;

        // 创建一个列表来存储所有的 DTO 结果
        List<PerformanceAppraisalListExcelImportlDTO> allDTOs = new ArrayList<>();
        while (rows.hasNext()) {
            Row row = rows.next();
            int colIndex = 0;
            if (row.getRowNum() == 0) { // 跳过表头行
                continue;
            }
            try {
                totalCount++;
                // 创建 DTO 对象
                PerformanceAppraisalListExcelImportlDTO dto = new PerformanceAppraisalListExcelImportlDTO();

                Integer serialNumber = WorkbookUtils.readAsInteger(row, colIndex++);
                dto.setSerialNumber(serialNumber);

                String employeeId = WorkbookUtils.readAsString(row, colIndex++);
                dto.setEmployeeId(employeeId.replace("-", "_"));

                String pefId = WorkbookUtils.readAsString(row, colIndex++);
                dto.setPefId(pefId);

                String name = WorkbookUtils.readAsString(row, colIndex++);
                dto.setName(name);

                Double siteDays1 = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setSiteDays1(df.format(siteDays1));

                Double siteDays2 = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setSiteDays2(df.format(siteDays2));

                Double siteDays3 = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setSiteDays3(df.format(siteDays3));

                // 计算总和时统一处理空值
                double sum =(siteDays1 != null ? siteDays1 : 0.0) +
                            (siteDays2 != null ? siteDays2 : 0.0) +
                            (siteDays3 != null ? siteDays3 : 0.0);
                dto.setSiteDaysSum(df.format(sum));

                Double month1TotalLeave = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth1TotalLeave(df.format(month1TotalLeave));

                Double month1StandardManhour = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth1StandardManhour(df.format(month1StandardManhour));

                Double month1Overtime = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth1Overtime(df.format(month1Overtime));

                Double month1Attendance = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth1Attendance(df.format(month1Attendance * 100) + "%");

                Double month2TotalLeave = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth2TotalLeave(df.format(month2TotalLeave));

                Double month2StandardManhour = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth2StandardManhour(df.format(month2StandardManhour));

                Double month2Overtime = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth2Overtime(df.format(month2Overtime));

                Double month2Attendance = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth2Attendance(df.format(month2Attendance * 100) + "%");

                Double month3TotalLeave = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth3TotalLeave(df.format(month3TotalLeave));

                Double month3StandardManhour = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth3StandardManhour(df.format(month3StandardManhour));

                Double month3Overtime = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth3Overtime(df.format(month3Overtime));

                Double month3Attendance = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setMonth3Attendance(df.format(month3Attendance * 100) + "%");

                Double totalLeave = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setTotalLeave(df.format(totalLeave));

                Double standardManhour = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setStandardManhour(df.format(standardManhour));

                Double overtime = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setOvertime(df.format(overtime));

                Double attendance = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setAttendance(df.format(attendance * 100) + "%");

                String company = WorkbookUtils.readAsString(row, colIndex++);
                dto.setCompany(company);

                String contractCompany = WorkbookUtils.readAsString(row, colIndex++);
                dto.setContractCompany(contractCompany);

                String division = WorkbookUtils.readAsString(row, colIndex++);
                dto.setDivision(division);

                String jobTitle = WorkbookUtils.readAsString(row, colIndex++);
                dto.setJobTitle(jobTitle);

                Date joiningDate = WorkbookUtils.readAsDate(row, colIndex++);
                if (joiningDate != null) {
                    dto.setJoiningDate(sdf.format(joiningDate));
                }

                Date dateTransferToRegularEmployee = WorkbookUtils.readAsDate(row, colIndex++);
                if (dateTransferToRegularEmployee != null) {
                    dto.setDateTransferToRegularEmployee(sdf.format(dateTransferToRegularEmployee));
                }

                Double yearsOfEmployment = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setYearsOfEmployment(df.format(yearsOfEmployment));

                Double yearsOfExperience = WorkbookUtils.readAsDouble(row, colIndex++);
                dto.setYearsOfExperience(df.format(yearsOfExperience));

                String firstDegree = WorkbookUtils.readAsString(row, colIndex++);
                dto.setFirstDegree(firstDegree);

                String evaluationCode = WorkbookUtils.readAsString(row, colIndex++);
                dto.setEvaluationCode(evaluationCode);

                String employeeIdOfAppraiser1 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setEmployeeIdOfAppraiser1(employeeIdOfAppraiser1.replace("-", "_"));

                String appraiser1 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setAppraiser1(appraiser1);

                String employeeIdOfReviewer1 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setEmployeeIdOfReviewer1(employeeIdOfReviewer1.replace("-", "_"));

                String reviewer1 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setReviewer1(reviewer1);

                String employeeIdOfReviewer2 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setEmployeeIdOfReviewer2(employeeIdOfReviewer2.replace("-", "_"));

                String reviewer2 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setReviewer2(reviewer2);

                String employeeIdOfAppraiser2 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setEmployeeIdOfAppraiser2(employeeIdOfAppraiser2.replace("-", "_"));

                String appraiser2 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setAppraiser2(appraiser2);

                String employeeIdOfAppraiserReviewer3 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setEmployeeIdOfAppraiserReviewer3(employeeIdOfAppraiserReviewer3.replace("-", "_"));

                String appraiserReviewer3 = WorkbookUtils.readAsString(row, colIndex++);
                dto.setAppraiserReviewer3(appraiserReviewer3);

                String employeeCategory = WorkbookUtils.readAsString(row, colIndex++);
                dto.setEmployeeCategory(employeeCategory);

                String engineerLevel = WorkbookUtils.readAsString(row, colIndex++);
                dto.setEngineerLevel(engineerLevel);

                // 将 DTO 添加到列表中
                allDTOs.add(dto);
                processedCount++;
                batchResult.addProcessedCount(1);
            } catch (Exception e) {
                e.printStackTrace(System.out);
                errorCount++;
                batchResult.addErrorCount(1);
                setImportDataErrorMessage(row, "Error processing row: " + e.getMessage());
            }
        }

        //根据员工类型选择模板并填充dto
        handleImportData(allDTOs, quarter);

        //所有execl生成后找到对应目录下的appraisal文件夹并遍历其下的所有子文件夹分别打成压缩包发送给对应的人
        Path appraisalDir = Paths.get(filePath, "appraisal");

        try (Stream<Path> dirStream = Files.list(appraisalDir)) {
            dirStream.filter(Files::isDirectory)
                .forEach(subDir -> {
                    String folderName = subDir.getFileName().toString();
                    handleSubDirectory(subDir, folderName, allDTOs);
                });
        } catch (IOException e) {
            throw new BusinessError("目录遍历失败：" + e.getMessage());
        }

        //发送完成后删除相应文件夹
        try {
            Files.walk(appraisalDir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new BusinessError("删除失败: " + path);
                    }
                });
        } catch (IOException e) {
            throw new BusinessError("目录遍历失败：" + e.getMessage());
        }

        // 更新 BatchResultDTO 的统计信息
        return new BatchResultDTO(
            totalCount,
            processedCount,
            skippedCount,
            errorCount
        );
    }

    private void handleImportData(List<PerformanceAppraisalListExcelImportlDTO> list, Integer quarter) {
        for (PerformanceAppraisalListExcelImportlDTO dto : list) {
            //生成每个员工的评估execl
            //1.根据engineerLevel来选择对应的模板,如果有多个审核人则生成多个execl并将其放到对应的目录下
            String templateFilePathDown = switch (dto.getEngineerLevel()) {
                case "Engineer" -> templateFilePath + "OS-HR-PF-FORME03CN Evaluation Form - Intermediate Level.xlsx";
                case "Junior Engineer" -> templateFilePath + "OS-HR-PF-FORME04CN Evaluation Form - Junior Level.xlsx";
                case "Senior Engineer" -> templateFilePath + "OS-HR-PF-FORME02CN Evaluation Form - Senior Level.xlsx";
                case "Leader" -> templateFilePath + "OS-HR-PF-FORME01CN Evaluation Form - Leader Level.xlsx";
                default -> throw new BusinessError("Unexpected value: " + dto.getEngineerLevel());
            };

            String sheetName = switch (dto.getEngineerLevel()) {
                case "Engineer" -> "Intermediate Level";
                case "Junior Engineer" -> "Junior Level";
                case "Senior Engineer" -> "Senior Level";
                case "Leader" -> "Leader Level";
                default -> throw new BusinessError("Unexpected value: " + dto.getEngineerLevel());
            };
            // 2. 获取所有需要处理的评估人列表（过滤空值）
//            List<String> appraisers = Stream.of(
//                dto.getAppraiser1(),
//                dto.getAppraiser2(),
//                dto.getAppraiserReviewer3()
//            ).filter(str -> str != null && !str.trim().isEmpty() && !"NA".equalsIgnoreCase(str)).toList();
            Map<String, String> appraiserMap = Stream.of(
                    new AbstractMap.SimpleEntry<>("Division", dto.getAppraiser1() + "_" + dto.getEmployeeIdOfAppraiser1()),
                    new AbstractMap.SimpleEntry<>("PM", dto.getAppraiser2() + "_" + dto.getEmployeeIdOfAppraiser2()),
                    new AbstractMap.SimpleEntry<>("Company", dto.getAppraiserReviewer3() + "_" + dto.getEmployeeIdOfAppraiserReviewer3())
                )
                .filter(entry -> {
                    String value = entry.getValue();
                    boolean basicCondition = value != null
                        && !value.trim().isEmpty()
                        && !value.contains("NA");

                    // 仅对Company条目增加Leader判断
                    if ("Company".equals(entry.getKey())) {
                        return basicCondition
                            && "Leader".equals(dto.getEmployeeCategory()) && !value.contains("NA");
                    }
                    return basicCondition;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            if (appraiserMap.isEmpty()) continue;

            //3.查询员工季度评估数据
            PerformanceAppraisalDTO performanceAppraisalDTO = searchEmployeeData(dto, quarter);
            // 4. 为每个评估人生成文件
            appraiserMap.forEach((key, value) -> {
                try {
                    performanceAppraisalDTO.setAppraiser(value);
                    performanceAppraisalDTO.setEvaluationParty(key);

                    Path appraiserDir = Paths.get(filePath, "appraisal", value, key);

                    // 创建目录（存在时不创建）
                    if (!Files.exists(appraiserDir)) {
                        Files.createDirectories(appraiserDir);
                    }

                    // 生成唯一文件名
                    String fileName = String.format("%s_%s.xlsx",
                        dto.getName(),
                        dto.getEmployeeId());
                    Path outputPath = appraiserDir.resolve(fileName);

                    // 写入Excel
                    EasyExcel.write(outputPath.toString())
                        .registerConverter(new LocalDateConverter())
                        .withTemplate(templateFilePathDown)
                        .sheet(sheetName)
                        .doFill(performanceAppraisalDTO);

                } catch (IOException e) {
                    throw new BusinessError("文件操作失败：" + e.getMessage());
                }
            });
        }
    }

    private PerformanceAppraisalDTO searchEmployeeData(PerformanceAppraisalListExcelImportlDTO dto, Integer quarter) {
        PerformanceAppraisalDTO result = new PerformanceAppraisalDTO();
        BeanUtils.copyProperties(dto, result);
        switch (quarter) {
            case 1:
                result.setMonth1("2025-Jan");
                result.setMonth2("2025-Feb");
                result.setMonth3("2025-Mar");
                break;
            case 2:
                result.setMonth1("2025-Apr");
                result.setMonth2("2025-May");
                result.setMonth3("2025-Jun");
                break;
            case 3:
                result.setMonth1("2025-Jul");
                result.setMonth2("2025-Aug");
                result.setMonth3("2025-Sep");
                break;
            case 4:
                result.setMonth1("2025-Oct");
                result.setMonth2("2025-Nov");
                result.setMonth3("2025-Dec");
                break;
        }

        result.setSummaryStandardManhour(String.valueOf(Double.parseDouble(dto.getTotalLeave()) +
            Double.parseDouble(dto.getStandardManhour())));
        return result;
    }

    /**
     * 处理单个子目录
     *
     * @param subDir 子目录路径
     */
    private void handleSubDirectory(Path subDir, String folderName, List<PerformanceAppraisalListExcelImportlDTO> list) {
        System.out.println("正在处理目录: " + subDir);
        // 这里添加具体的业务逻辑，例如：
        // 1. 压缩目录（接之前的压缩逻辑）
        String destPath = Paths.get(temporaryDir, "appraisal").toString(); // 正确构建目标目录
        try {
            // 创建目标目录（如果不存在）
            Files.createDirectories(Paths.get(destPath));

            // 使用NIO方式压缩
            Path zipPath = Paths.get(destPath, folderName + ".zip");
            try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
                Files.walk(Paths.get(subDir.toString()))
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(file -> {
                        try {
                            String entryName = Paths.get(subDir.toString()).relativize(file).toString();
                            zos.putNextEntry(new ZipEntry(entryName));
                            Files.copy(file, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new BusinessError("文件压缩失败: " + file);
                        }
                    });
            }
            System.out.println("压缩包生成路径：" + zipPath.toString());
        } catch (IOException e) {
            throw new BusinessError("压缩操作失败：" + e.getMessage());
        }
        // 2. 发送邮件
        // 2.1 根据文件名查询邮箱
        String[] parts = folderName.split("_", 2);
        String username = parts.length > 1 ? parts[1] : "";
        JsonObjectResponseBody<UserProfile> user = userFeignAPI.getUserByUsername(username);
        String email = user.getData().getEmail();
        List<String> attachments = new ArrayList<>();
        String attachment = temporaryDir + "/appraisal/" + folderName + ".zip";
        attachments.add(attachment);

        List<String> subDirNames = getSubdirectoryNames(subDir);

        String title = "";

        if (!subDirNames.isEmpty()){
            if (subDirNames.contains("Company")){
                String company = list.stream()
                    .filter(dto ->
                        username.equals(dto.getEmployeeIdOfAppraiserReviewer3()) &&
                            dto.getCompany() != null
                    )
                    .map(PerformanceAppraisalListExcelImportlDTO::getCompany)
                    .distinct()
                    .collect(Collectors.joining("/"));

                title += company + " ";
            }

            if (subDirNames.contains("Division")){
                String division = list.stream()
                    .filter(dto ->
                        username.equals(dto.getEmployeeIdOfAppraiser1()) &&
                            dto.getDivision() != null
                    )
                    .map(PerformanceAppraisalListExcelImportlDTO::getDivision)
                    .distinct()
                    .collect(Collectors.joining("/"));

                title += division + " ";
            }

            if (subDirNames.contains("PM")){
                title += "PM ";
            }

        }


        String subject = "[INTERNAL] "+ title +". Employee Performance Evaluation Q1 2025";

        String content = "<html>"
            + "<body style=\"font-family: Arial, sans-serif; font-size: 14px;\">" +
            "  <table border=\"1\" cellpadding=\"8\" cellspacing=\"0\" style=\"border-collapse: collapse; margin-bottom: 20px;\">" +
            "    <tr>" +
            "      <td>Sensitivity</td>" +
            "      <td>Confidential</td>" +
            "    </tr>" +
            "    <tr>" +
            "      <td>Main Content Summary</tdh>" +
            "      <td>Employee Performance Evaluation</td>" +
            "    </tr>" +
            "    <tr>" +
            "      <td>Due Date</td>" +
            "      <td>21<sup>st</sup> April</td>" +
            "    </tr>" +
            "  </table>" +
            "  <p>Dear Appraisers,</p>" +
            "  <p>Please find attached the performance evaluation forms for Q1 2025. Due to some anomalies in the data sent last Friday, we are resending the form now. We apologize for any inconvenience caused.</p>" +

            "  <p>If you have already submitted the completed form to Abby Zhang (abby.zhang@oceanstar.com), she will contact you separately to replace the file.</p>" +

            "  <p>For those who have not yet submitted the form, please use the version sent today.</p>" +

            "  <p>Once completed, kindly send the filled-out form to Abby Zhang via email by next Monday, 21<sup>st</sup> April.</p>" +

            "  <p>If you have any questions, please contact Abby Zhang. Thank you for your cooperation.</p>" +

            "  <p  style=\"font-family: Arial, sans-serif; color: #003388; font-size: 11pt; line-height: 1.5;\"><strong>Regards,</strong><br>" +
            "  Human Resources Department via Intelligent & Digital Engineering</p>" +

            "  <p style=\"font-size: 12px; color: gray;\">Classified as Confidential</p>"
            + "</html>";
        try {
            MailUtils.send(
                new InternetAddress(mailFromAddress), // 发件人地址
                new InternetAddress[]{new InternetAddress(email)}, // 收件人地址
                subject, // 邮件主题
                content, // 邮件内容
                attachments
            );
        } catch (AddressException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getSubdirectoryNames(Path parentDir) {
        try (Stream<Path> stream = Files.list(parentDir)) {
            return stream
                .filter(Files::isDirectory)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new BusinessError("获取子目录失败: " + e.getMessage());
        }
    }

    /**
     * 设置导入数据错误信息。
     *
     * @param row     错误所在行
     * @param message 错误消息
     */
    private void setImportDataErrorMessage(Row row, String message) {
        row.createCell(row.getLastCellNum()).setCellValue(message);
    }
}
