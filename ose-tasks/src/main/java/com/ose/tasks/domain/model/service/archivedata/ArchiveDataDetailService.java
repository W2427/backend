package com.ose.tasks.domain.model.service.archivedata;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;

import static com.ose.tasks.domain.model.repository.archivedata.ArchiveDataDetailRepositoryImpl.InspectionType.EX_INSPECTION;
import static com.ose.tasks.domain.model.repository.archivedata.ArchiveDataDetailRepositoryImpl.InspectionType.IN_INSPECTION;
import static com.ose.tasks.domain.model.repository.archivedata.ArchiveDataDetailRepositoryImpl.QualifiedType.QUALIFIED;
import static com.ose.tasks.domain.model.repository.archivedata.ArchiveDataDetailRepositoryImpl.QualifiedType.UN_QUALIFIED;

import com.ose.tasks.domain.model.repository.archivedata.ArchiveDataDetailRepository;
import com.ose.tasks.dto.archivedata.ArchiveDataCriteriaDTO;
import com.ose.util.FileUtils;
import com.ose.util.WorkbookUtils;

/**
 * 文档包服务。
 */
@Component
public class ArchiveDataDetailService implements ArchiveDataDetailInterface {


    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    private final ArchiveDataDetailRepository archiveDataDetailRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public ArchiveDataDetailService(
        ArchiveDataDetailRepository archiveDataDetailRepository
    ) {
        this.archiveDataDetailRepository = archiveDataDetailRepository;
    }

    @Override
    public File downloadDataDetailsFile(
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        ArchiveDataType archiveType,
        ArchiveScheduleType scheduleType,
        ArchiveDataCriteriaDTO criteriaDTO
    ) {
        switch (archiveType) {
            case WBS_PASS_RATE_PROGRESS:
                return downloadWbsPassRateProgressDataDetailsFile(
                    operatorDTO,
                    projectId,
                    criteriaDTO,
                    scheduleType
                );
            case WBS_WELD_RATE_PROGRESS:
                return downloadWbsWeldRateProgressDataDetailsFile(
                    operatorDTO,
                    projectId,
                    criteriaDTO,
                    scheduleType
                );
            case WBS_NDT_RATE_PROGRESS:
                return downloadWbsNdtRateProgressDataDetailsFile(
                    operatorDTO,
                    projectId,
                    criteriaDTO,
                    scheduleType
                );
            default:
                break;
        }
        return null;
    }

    /**
     * 查询NDT合格率明细信息
     *
     * @param operatorDTO
     * @param projectId    项目ID
     * @param criteriaDTO
     * @param scheduleType
     * @return
     */
    @SuppressFBWarnings("BX_BOXING_IMMEDIATELY_UNBOXED")
    @SuppressWarnings("incomplete-switch")
    private File downloadWbsNdtRateProgressDataDetailsFile(
        OperatorDTO operatorDTO,
        Long projectId,
        ArchiveDataCriteriaDTO criteriaDTO,
        ArchiveScheduleType scheduleType
    ) {

        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/archive/export-ndt-rate-data-details.xlsx"),
            temporaryDir,
            operatorDTO.getId().toString()
        );


        File excel;
        Workbook workbook;

        try {
            excel = new File(temporaryDir, temporaryFileName);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }

        List<Map<String, Object>> dataQualified = archiveDataDetailRepository
            .getWbsNdtRateProgress(
                projectId,
                criteriaDTO,
                QUALIFIED
            );
        List<Map<String, Object>> dataUnQualified = archiveDataDetailRepository
            .getWbsNdtRateProgress(
                projectId,
                criteriaDTO,
                UN_QUALIFIED
            );

        double qualifiedCount = new Double(dataQualified.size());
        double unQualifiedCount = new Double(dataUnQualified.size());

        double qualifiedCountRate = qualifiedCount / (qualifiedCount + unQualifiedCount);
        String qualifiedCountRateStr = String.format("%.2f", qualifiedCountRate * 100) + "%";

        double qualifiedNps = 0;
        double unQualifiedNps = 0;

        Date exportTime = new Date();
        CellStyle cellStyleDate = workbook.createCellStyle();
        DataFormat formatDate = workbook.createDataFormat();
        cellStyleDate.setDataFormat(formatDate.getFormat("yyyy/m/d"));

        for (int i = 0; i < 2; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            CellStyle cellStyle0 = WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 0), 0).getCellStyle();
            WorkbookUtils.setCellValue(sheet, 0, 1, exportTime);
            cellStyle0.setDataFormat(formatDate.getFormat("yyyy/m/d"));
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 0), 1).setCellStyle(cellStyle0);

            CellStyle cellStyle1 = WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 0).getCellStyle();
            WorkbookUtils.setCellValue(sheet, 1, 0, scheduleType.getDisplayName() + "：");
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 0).setCellStyle(cellStyle1);

            switch (scheduleType) {
                case DAILY:
                    WorkbookUtils.setCellValue(sheet, 1, 1, "" + criteriaDTO.getGroupYear() + "-" + criteriaDTO.getGroupMonth() + "-" + criteriaDTO.getGroupDay());
                    break;
                case WEEKLY:
                    WorkbookUtils.setCellValue(sheet, 1, 1, "" + criteriaDTO.getGroupYear() + "年第" + criteriaDTO.getGroupWeek() + "周");
                    break;
                case MONTHLY:
                    WorkbookUtils.setCellValue(sheet, 1, 1, "" + criteriaDTO.getGroupYear() + "-" + criteriaDTO.getGroupMonth());
                    break;
            }
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 2, 1, criteriaDTO.getModule());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 2), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 3, 1, criteriaDTO.getStage());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 3), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 4, 1, criteriaDTO.getEntityMaterial());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 4), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 5, 1, criteriaDTO.getTeamName());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 5), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 6, 1, criteriaDTO.getNdtType());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 6), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 7, 1, criteriaDTO.getWelderNo());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 7), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 8, 1, qualifiedCountRateStr);
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 8), 1).setCellStyle(cellStyle1);

            int rowNum = 12;

            List<Map<String, Object>> data = null;
            switch (i) {
                case 0:
                    data = dataQualified;
                    break;
                case 1:
                    data = dataUnQualified;
                    break;
            }
            if (data != null) {
                for (Map<String, Object> map : data) {
                    Row row = WorkbookUtils.getRow(sheet, rowNum++);

                    GregorianCalendar date = new GregorianCalendar(
                        ((BigInteger) map.get("group_year")).intValue(),
                        ((BigInteger) map.get("group_month")).intValue() - 1,
                        ((BigInteger) map.get("group_day")).intValue()
                    );

                    WorkbookUtils.getCell(row, 0).setCellValue(date.getTime());
                    WorkbookUtils.getCell(row, 0).setCellStyle(cellStyleDate);


                    WorkbookUtils.getCell(row, 1).setCellValue((String) map.get("entity_no"));


                    WorkbookUtils.getCell(row, 2).setCellValue((Double) map.get("nps"));
                    if (i == 0) {
                        qualifiedNps += (Double) map.get("nps");
                    } else if (i == 1) {
                        unQualifiedNps += (Double) map.get("nps");
                    }

                    WorkbookUtils.getCell(row, 3).setCellValue((String) map.get("module_name"));


                    WorkbookUtils.getCell(row, 4).setCellValue((String) map.get("stage"));


                    WorkbookUtils.getCell(row, 5).setCellValue((String) map.get("entity_material"));


                    WorkbookUtils.getCell(row, 7).setCellValue((String) map.get("team_name"));


                    WorkbookUtils.getCell(row, 8).setCellValue((String) map.get("ndt_type"));


                    WorkbookUtils.getCell(row, 9).setCellValue((String) map.get("welder_no"));
                }
            }
        }

        double qualifiedNpsRate = qualifiedNps / (qualifiedNps + unQualifiedNps);
        String qualifiedNpsRateStr = String.format("%.2f", qualifiedNpsRate * 100) + "%";

        for (int i = 0; i < 2; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            CellStyle cellStyle1 = WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 0).getCellStyle();
            WorkbookUtils.setCellValue(sheet, 9, 1, qualifiedNpsRateStr);
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 9), 1).setCellStyle(cellStyle1);
        }

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
            return excel;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
    }

    /**
     * 查询焊接合格率明细信息
     *
     * @param operatorDTO
     * @param projectId    项目ID
     * @param criteriaDTO
     * @param scheduleType
     * @return
     */
    @SuppressWarnings("incomplete-switch")
    private File downloadWbsWeldRateProgressDataDetailsFile(
        OperatorDTO operatorDTO,
        Long projectId,
        ArchiveDataCriteriaDTO criteriaDTO,
        ArchiveScheduleType scheduleType
    ) {

        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/archive/export-weld-rate-data-details.xlsx"),
            temporaryDir,
            operatorDTO.getId().toString()
        );


        File excel;
        Workbook workbook;

        try {
            excel = new File(temporaryDir, temporaryFileName);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }

        List<Map<String, Object>> dataQualified = archiveDataDetailRepository
            .getWbsWeldRateProgress(
                projectId,
                criteriaDTO,
                QUALIFIED
            );
        List<Map<String, Object>> dataUnQualified = archiveDataDetailRepository
            .getWbsWeldRateProgress(
                projectId,
                criteriaDTO,
                UN_QUALIFIED
            );

        double qualifiedCount = new Double(dataQualified.size());
        double unQualifiedCount = new Double(dataUnQualified.size());

        double qualifiedCountRate = qualifiedCount / (qualifiedCount + unQualifiedCount);
        String qualifiedCountRateStr = String.format("%.2f", qualifiedCountRate * 100) + "%";

        double qualifiedNps = 0;
        double unQualifiedNps = 0;

        Date exportTime = new Date();
        CellStyle cellStyleDate = workbook.createCellStyle();
        DataFormat formatDate = workbook.createDataFormat();
        cellStyleDate.setDataFormat(formatDate.getFormat("yyyy/m/d"));

        for (int i = 0; i < 2; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            CellStyle cellStyle0 = WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 0), 0).getCellStyle();
            WorkbookUtils.setCellValue(sheet, 0, 1, exportTime);
            cellStyle0.setDataFormat(formatDate.getFormat("yyyy/m/d"));
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 0), 1).setCellStyle(cellStyle0);

            CellStyle cellStyle1 = WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 0).getCellStyle();
            WorkbookUtils.setCellValue(sheet, 1, 0, scheduleType.getDisplayName() + "：");
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 0).setCellStyle(cellStyle1);

            switch (scheduleType) {
                case DAILY:
                    WorkbookUtils.setCellValue(sheet, 1, 1, "" + criteriaDTO.getGroupYear() + "-" + criteriaDTO.getGroupMonth() + "-" + criteriaDTO.getGroupDay());
                    break;
                case WEEKLY:
                    WorkbookUtils.setCellValue(sheet, 1, 1, "" + criteriaDTO.getGroupYear() + "年第" + criteriaDTO.getGroupWeek() + "周");
                    break;
                case MONTHLY:
                    WorkbookUtils.setCellValue(sheet, 1, 1, "" + criteriaDTO.getGroupYear() + "-" + criteriaDTO.getGroupMonth());
                    break;
            }
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 2, 1, criteriaDTO.getModule());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 2), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 3, 1, criteriaDTO.getStage());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 3), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 4, 1, criteriaDTO.getEntityMaterial());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 4), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 5, 1, criteriaDTO.getTeamName());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 5), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 6, 1, criteriaDTO.getWeldType());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 6), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 7, 1, criteriaDTO.getWelderNo());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 7), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 8, 1, qualifiedCountRateStr);
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 8), 1).setCellStyle(cellStyle1);

            int rowNum = 12;

            List<Map<String, Object>> data = null;
            switch (i) {
                case 0:
                    data = dataQualified;
                    break;
                case 1:
                    data = dataUnQualified;
                    break;
            }
            if (data != null) {
                for (Map<String, Object> map : data) {
                    Row row = WorkbookUtils.getRow(sheet, rowNum++);

                    GregorianCalendar date = new GregorianCalendar(
                        ((BigInteger) map.get("group_year")).intValue(),
                        ((BigInteger) map.get("group_month")).intValue() - 1,
                        ((BigInteger) map.get("group_day")).intValue()
                    );

                    WorkbookUtils.getCell(row, 0).setCellValue(date.getTime());
                    WorkbookUtils.getCell(row, 0).setCellStyle(cellStyleDate);


                    WorkbookUtils.getCell(row, 1).setCellValue((String) map.get("entity_no"));


                    WorkbookUtils.getCell(row, 2).setCellValue((Double) map.get("nps"));
                    if (i == 0) {
                        qualifiedNps += (Double) map.get("nps");
                    } else if (i == 1) {
                        unQualifiedNps += (Double) map.get("nps");
                    }

                    WorkbookUtils.getCell(row, 3).setCellValue((String) map.get("module_name"));


                    WorkbookUtils.getCell(row, 4).setCellValue((String) map.get("stage"));


                    WorkbookUtils.getCell(row, 5).setCellValue((String) map.get("entity_material"));


                    WorkbookUtils.getCell(row, 7).setCellValue((String) map.get("team_name"));


                    WorkbookUtils.getCell(row, 8).setCellValue((String) map.get("weld_type"));


                    WorkbookUtils.getCell(row, 9).setCellValue((String) map.get("welder_no"));
                }
            }
        }

        double qualifiedNpsRate = qualifiedNps / (qualifiedNps + unQualifiedNps);
        String qualifiedNpsRateStr = String.format("%.2f", qualifiedNpsRate * 100) + "%";

        for (int i = 0; i < 2; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            CellStyle cellStyle1 = WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 0).getCellStyle();
            WorkbookUtils.setCellValue(sheet, 9, 1, qualifiedNpsRateStr);
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 9), 1).setCellStyle(cellStyle1);
        }

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
            return excel;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
    }

    /**
     * 查询报验合格率明细信息
     *
     * @param operatorDTO
     * @param projectId    项目ID
     * @param groupDTO
     * @param scheduleType
     * @return
     */
    @SuppressWarnings("incomplete-switch")
    private File downloadWbsPassRateProgressDataDetailsFile(
        OperatorDTO operatorDTO,
        Long projectId,
        ArchiveDataCriteriaDTO criteriaDTO,
        ArchiveScheduleType scheduleType
    ) {

        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/archive/export-pass-rate-data-details.xlsx"),
            temporaryDir,
            operatorDTO.getId().toString()
        );


        File excel;
        Workbook workbook;

        try {
            excel = new File(temporaryDir, temporaryFileName);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }

        List<Map<String, Object>> dataInFpyQualified = archiveDataDetailRepository
            .getWbsPassRateProgressFpyQualified(
                projectId,
                criteriaDTO,
                IN_INSPECTION,
                QUALIFIED
            );
        List<Map<String, Object>> dataInFpyUnQualified = archiveDataDetailRepository
            .getWbsPassRateProgressFpyQualified(
                projectId,
                criteriaDTO,
                IN_INSPECTION,
                UN_QUALIFIED
            );
        List<Map<String, Object>> dataExFpyQualified = archiveDataDetailRepository
            .getWbsPassRateProgressFpyQualified(
                projectId,
                criteriaDTO,
                EX_INSPECTION,
                QUALIFIED
            );
        List<Map<String, Object>> dataExFpyUnQualified = archiveDataDetailRepository
            .getWbsPassRateProgressFpyQualified(
                projectId,
                criteriaDTO,
                EX_INSPECTION,
                UN_QUALIFIED
            );

        double dataInFpyQualifiedCount = (double) dataInFpyQualified.size();
        double dataInFpyUnQualifiedCount = (double) dataInFpyUnQualified.size();

        double dataInFpyQualifiedCountRate = dataInFpyQualifiedCount / (dataInFpyQualifiedCount + dataInFpyUnQualifiedCount);
        String dataInFpyQualifiedCountRateStr = String.format("%.2f", dataInFpyQualifiedCountRate * 100) + "%";

        double dataExFpyQualifiedCount = (double) dataExFpyQualified.size();
        double dataExFpyUnQualifiedCount = (double) dataExFpyUnQualified.size();

        double dataExFpyQualifiedCountRate = dataExFpyQualifiedCount / (dataExFpyQualifiedCount + dataExFpyUnQualifiedCount);
        String dataExFpyQualifiedCountRateStr = String.format("%.2f", dataExFpyQualifiedCountRate * 100) + "%";

        Date exportTime = new Date();
        CellStyle cellStyleDate = workbook.createCellStyle();
        DataFormat formatDate = workbook.createDataFormat();
        cellStyleDate.setDataFormat(formatDate.getFormat("yyyy/m/d"));

        for (int i = 0; i < 4; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            CellStyle cellStyle0 = WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 0), 0).getCellStyle();
            WorkbookUtils.setCellValue(sheet, 0, 1, exportTime);
            cellStyle0.setDataFormat(formatDate.getFormat("yyyy/m/d"));
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 0), 1).setCellStyle(cellStyle0);

            CellStyle cellStyle1 = WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 0).getCellStyle();
            WorkbookUtils.setCellValue(sheet, 1, 0, scheduleType.getDisplayName() + "：");
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 0).setCellStyle(cellStyle1);

            switch (scheduleType) {
                case DAILY:
                    WorkbookUtils.setCellValue(sheet, 1, 1, "" + criteriaDTO.getGroupYear() + "-" + criteriaDTO.getGroupMonth() + "-" + criteriaDTO.getGroupDay());
                    break;
                case WEEKLY:
                    WorkbookUtils.setCellValue(sheet, 1, 1, "" + criteriaDTO.getGroupYear() + "年第" + criteriaDTO.getGroupWeek() + "周");
                    break;
                case MONTHLY:
                    WorkbookUtils.setCellValue(sheet, 1, 1, "" + criteriaDTO.getGroupYear() + "-" + criteriaDTO.getGroupMonth());
                    break;
            }
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 1), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 2, 1, criteriaDTO.getModule());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 2), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 3, 1, criteriaDTO.getStage());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 3), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 4, 1, criteriaDTO.getProcess());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 4), 1).setCellStyle(cellStyle1);

            WorkbookUtils.setCellValue(sheet, 5, 1, criteriaDTO.getTeamName());
            WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 5), 1).setCellStyle(cellStyle1);

            if (i == 0 || i == 1) {
                WorkbookUtils.setCellValue(sheet, 6, 1, dataInFpyQualifiedCountRateStr);
                WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 6), 1).setCellStyle(cellStyle1);
            } else if (i == 2 || i == 3) {
                WorkbookUtils.setCellValue(sheet, 6, 1, dataExFpyQualifiedCountRateStr);
                WorkbookUtils.getCell(WorkbookUtils.getRow(sheet, 6), 1).setCellStyle(cellStyle1);
            }

            int rowNum = 9;

            List<Map<String, Object>> data = null;
            switch (i) {
                case 0:
                    data = dataInFpyQualified;
                    break;
                case 1:
                    data = dataInFpyUnQualified;
                    break;
                case 2:
                    data = dataExFpyQualified;
                    break;
                case 3:
                    data = dataExFpyUnQualified;
                    break;
            }
            if (data != null) {
                for (Map<String, Object> map : data) {
                    Row row = WorkbookUtils.getRow(sheet, rowNum++);

                    GregorianCalendar date = new GregorianCalendar(
                        ((BigInteger) map.get("group_year")).intValue(),
                        ((BigInteger) map.get("group_month")).intValue() - 1,
                        ((BigInteger) map.get("group_day")).intValue()
                    );

                    WorkbookUtils.getCell(row, 0).setCellValue(date.getTime());
                    WorkbookUtils.getCell(row, 0).setCellStyle(cellStyleDate);


                    WorkbookUtils.getCell(row, 1).setCellValue((String) map.get("entity_no"));


                    WorkbookUtils.getCell(row, 2).setCellValue((String) map.get("module_name"));


                    WorkbookUtils.getCell(row, 3).setCellValue((String) map.get("stage"));


                    WorkbookUtils.getCell(row, 4).setCellValue((String) map.get("process"));


                    WorkbookUtils.getCell(row, 6).setCellValue(map.get("team_name") == null ? "" : (String) map.get("team_name"));
                }
            }
        }

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
            return excel;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
    }

}
