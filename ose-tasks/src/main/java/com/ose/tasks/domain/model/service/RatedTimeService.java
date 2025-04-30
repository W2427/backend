package com.ose.tasks.domain.model.service;

import com.ose.util.*;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.*;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.dto.RatedTimeCreateDTO;
import com.ose.tasks.dto.RatedTimeCriteriaDTO;
import com.ose.tasks.dto.RatedTimeImportDTO;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.vo.RatedTimeUnit;
import com.ose.vo.EntityStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class RatedTimeService implements RatedTimeInterface {
    private final static Logger logger = LoggerFactory.getLogger(RatedTimeService.class);
    private static final String STYLES_SHEET_NAME = "styles";
    private static final String RATED_TIME_SHEET_NAME = "import_template";
    private static final int CUSTOM_PROPERTY_COLUMN_START_AT = 14;
    private static final int HEADER_ROW_NO = 2;
    private final UploadFeignAPI uploadFeignAPI;
    private static final SimpleDateFormat BATCH_NO_FORMAT = new SimpleDateFormat("YYYYMMddHHmmss");

    private final RatedTimeImportRecordRepository ratedTimeImportRecordRepository;
    private RatedTimeRepository ratedTimeRepository;
    private RatedTimeCriterionRepository ratedTimeCriterionRepository;
    private BpmProcessRepository bpmProcessRepository;
    private BpmEntitySubTypeRepository bpmEntitySubTypeRepository;


    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Autowired
    public RatedTimeService(
        RatedTimeRepository ratedTimeRepository,
        RatedTimeCriterionRepository ratedTimeCriterionRepository,
        BpmProcessRepository bpmProcessRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        BpmEntitySubTypeRepository bpmEntitySubTypeRepository,
        RatedTimeImportRecordRepository ratedTimeImportRecordRepository
    ) {
        this.ratedTimeRepository = ratedTimeRepository;
        this.ratedTimeCriterionRepository = ratedTimeCriterionRepository;
        this.bpmProcessRepository = bpmProcessRepository;
        this.bpmEntitySubTypeRepository = bpmEntitySubTypeRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.ratedTimeImportRecordRepository = ratedTimeImportRecordRepository;
    }

    /**
     * 创建定额工时。
     *
     * @param operatorId         操作人ID
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param processStageId     工序阶段ID
     * @param processId          工序ID
     * @param entitySubTypeId   实体类型ID
     * @param ratedTimeCreateDTO 定额工时信息
     */
    @Override
    public void create(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long processStageId,
        Long processId,
        Long entitySubTypeId,
        RatedTimeCreateDTO ratedTimeCreateDTO
    ) {

        RatedTime ratedTime = new RatedTime();

        BeanUtils.copyProperties(ratedTimeCreateDTO, ratedTime);

        ratedTime.setOrgId(orgId);
        ratedTime.setProjectId(projectId);
        ratedTime.setProcessStageId(processStageId);
        ratedTime.setProcessId(processId);
        ratedTime.setEntitySubTypeId(entitySubTypeId);

        RatedTimeCriterion ratedTimeCriterionItem =
            ratedTimeCriterionRepository.findByOrgIdAndProjectIdAndProcessIdAndProcessStageIdAndEntitySubTypeIdAndDeletedIsFalse(orgId, projectId, processId, processStageId, entitySubTypeId);

        if (ratedTimeCriterionItem != null) {
            ratedTime.setRatedTimeCriterionId(ratedTimeCriterionItem.getId());
        }

        Date now = new Date();
        ratedTime.setCreatedBy(operatorId);
        ratedTime.setCreatedAt(now);
        ratedTime.setLastModifiedBy(operatorId);
        ratedTime.setLastModifiedAt(now);
        ratedTime.setStatus(EntityStatus.ACTIVE);

        ratedTimeRepository.save(ratedTime);
    }

    /**
     * 更新定额工时。
     *
     * @param operatorId  操作人ID
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     */
    @Override
    public void update(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long ratedTimeId,
        RatedTimeCreateDTO ratedTimeCreateDTO) {

        RatedTime ratedTime = ratedTimeRepository.findByIdAndDeletedIsFalse(ratedTimeId);

        if (ratedTime == null) {
            throw new NotFoundError("ratedTime is not found");
        }

        BeanUtils.copyProperties(ratedTimeCreateDTO, ratedTime);

        Date now = new Date();
        ratedTime.setCreatedBy(operatorId);
        ratedTime.setCreatedAt(now);
        ratedTime.setLastModifiedBy(operatorId);
        ratedTime.setLastModifiedAt(now);
        ratedTime.setStatus(EntityStatus.ACTIVE);

        ratedTimeRepository.save(ratedTime);
    }

    /**
     * 获取定额工时列表。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriteriaDTO 筛选条件
     * @return 定额工时列表
     */
    @Override
    public Page<RatedTime> search(
        Long orgId,
        Long projectId,
        RatedTimeCriteriaDTO ratedTimeCriteriaDTO) {

        return ratedTimeRepository.search(orgId, projectId, ratedTimeCriteriaDTO);
    }

    /**
     * 删除定额工时信息。
     *
     * @param operatorId  操作人ID
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     */
    @Override
    public void delete(
        Long operatorId,
        Long orgId,
        Long projectId,
        Long ratedTimeId) {

        RatedTime ratedTime = ratedTimeRepository.findByIdAndDeletedIsFalse(ratedTimeId);

        if (ratedTime == null) {
            throw new NotFoundError("ratedTime is not found");
        }

        Date now = new Date();
        ratedTime.setStatus(EntityStatus.DELETED);
        ratedTime.setLastModifiedBy(operatorId);
        ratedTime.setLastModifiedAt(now);
        ratedTime.setDeletedBy(operatorId);
        ratedTime.setDeletedAt(now);
        ratedTime.setDeleted(true);

        ratedTimeRepository.save(ratedTime);
    }

    /**
     * 获取定额工时详情。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     * @return 定额工时详情
     */
    @Override
    public RatedTime get(Long orgId, Long projectId, Long ratedTimeId) {

        return ratedTimeRepository.findByIdAndDeletedIsFalse(ratedTimeId);
    }

    /**
     * 批量导入额定工时。
     *
     * @param operatorId         操作人ID
     * @param orgId              组织ID
     * @param project            项目
     * @param ratedTimeImportDTO 导入文件DTO
     */
    @Override
    public RatedTimeImportRecord importRatedTimeFile(Long operatorId,
                                                     Long orgId,
                                                     Project project,
                                                     RatedTimeImportDTO ratedTimeImportDTO) {
        File excel;
        Workbook workbook;
        Sheet sheet;
        Sheet styles;

        try {

            excel = new File(temporaryDir, ratedTimeImportDTO.getFileName());

            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new BusinessError(e.getMessage());
        }

        if (workbook == null
            || (sheet = workbook.getSheet(RATED_TIME_SHEET_NAME)) == null
            || (styles = workbook.getSheet(STYLES_SHEET_NAME)) == null) {
            return null;
        }

        final String batchNo = BATCH_NO_FORMAT.format(new Date());
        Row row;
        int failedCount = 0;
        List<RatedTime> ratedTimes = new ArrayList<>();
        RatedTime ratedTime;
        CellStyle errorStyle = styles.getRow(0).getCell(0).getCellStyle();
        int errorColNo = CUSTOM_PROPERTY_COLUMN_START_AT;


        for (int i = HEADER_ROW_NO; i < sheet.getPhysicalNumberOfRows(); i++) {

            row = sheet.getRow(i);
            List<String> errors = new ArrayList<>();
            ratedTime = new RatedTime();

            String no = WorkbookUtils.readAsString(row, 0);
            if (StringUtils.isEmpty(no)) {
                break;
            }
            String process = WorkbookUtils.readAsString(row, 2);
            String processStage = WorkbookUtils.readAsString(row, 1);


            if (StringUtils.isEmpty(process)) {
                errors.add("必须设置工序");
            } else if (StringUtils.isEmpty(processStage)) {
                errors.add("必须设置工序阶段");
            } else {

                BpmProcess bpmProcess = bpmProcessRepository.findByOrgIdAndProjectIdAndNameCnAndStatus(
                    project.getId(),
                    orgId,
                    process,
                    processStage,
                    EntityStatus.ACTIVE
                ).orElse(null);
                if (bpmProcess == null) {
                    errors.add("工序和工序阶段不存在");
                } else {

                    ratedTime.setProcessId(bpmProcess.getId());
                    ratedTime.setProcessStageId(bpmProcess.getProcessStage().getId());
                }
            }


            String entitySubType = WorkbookUtils.readAsString(row, 7);
            if (StringUtils.isEmpty(entitySubType)) {
                errors.add("必须设置实体类型");
            } else {

                BpmEntitySubType bpmEntitySubType = bpmEntitySubTypeRepository.findByProjectIdAndNameCnAndStatus(
                    project.getId(),
                    entitySubType,
                    EntityStatus.ACTIVE
                ).orElse(null);
                if (bpmEntitySubType == null) {
                    errors.add("实体类型不存在");
                } else {

                    ratedTime.setEntitySubTypeId(bpmEntitySubType.getId());
                }
            }


            RatedTimeCriterion ratedTimeCriterionItem =
                ratedTimeCriterionRepository.findByOrgIdAndProjectIdAndProcessIdAndProcessStageIdAndEntitySubTypeIdAndDeletedIsFalse(
                    orgId,
                    project.getId(),
                    ratedTime.getProcessId(),
                    ratedTime.getProcessStageId(),
                    ratedTime.getEntitySubTypeId()
                );
            if (ratedTimeCriterionItem != null) {
                ratedTime.setRatedTimeCriterionId(ratedTimeCriterionItem.getId());
                if (ratedTimeCriterionItem.getPipeDiameter() != null && ratedTimeCriterionItem.getPipeDiameter() == true) {
                    ratedTime.setPipeDiameter(WorkbookUtils.readAsDouble(row, 3));
                }
                if (ratedTimeCriterionItem.getPipeThickness() != null && ratedTimeCriterionItem.getPipeThickness() == true) {
                    ratedTime.setMinPipeThickness(WorkbookUtils.readAsDouble(row, 4));
                    ratedTime.setMaxPipeThickness(WorkbookUtils.readAsDouble(row, 5));
                }
                if (ratedTimeCriterionItem.getTestPressure() != null && ratedTimeCriterionItem.getTestPressure() == true) {
                    ratedTime.setMinTestPressure(WorkbookUtils.readAsDouble(row, 8));
                    ratedTime.setMaxTestPressure(WorkbookUtils.readAsDouble(row, 9));
                }
                if (ratedTimeCriterionItem.getCleaningMedium() != null && ratedTimeCriterionItem.getCleaningMedium() == true) {
                    ratedTime.setCleaningMedium(WorkbookUtils.readAsString(row, 10));
                }
                if (ratedTimeCriterionItem.getMedium() != null && ratedTimeCriterionItem.getMedium() == true) {
                    ratedTime.setMedium(WorkbookUtils.readAsString(row, 11));
                }
                if (ratedTimeCriterionItem.getUnitM() != null && ratedTimeCriterionItem.getUnitM() == true) {
                    if (!StringUtils.isEmpty(WorkbookUtils.readAsString(row, 12)) && (WorkbookUtils.readAsString(row, 12).equals("M"))) {
                        ratedTime.setUnit(WorkbookUtils.readAsString(row, 12));
                    }
                }
                if (ratedTimeCriterionItem.getUnitN() != null && ratedTimeCriterionItem.getUnitN() == true) {
                    if (!StringUtils.isEmpty(WorkbookUtils.readAsString(row, 12)) && WorkbookUtils.readAsString(row, 12).equals("N")) {
                        ratedTime.setUnit(WorkbookUtils.readAsString(row, 12));
                    }
                }
                if (ratedTimeCriterionItem.getMaterial() != null && ratedTimeCriterionItem.getMaterial() == true) {

                    String materialGroup = WorkbookUtils.readAsString(row, 6);

                }

            } else {
                errors.add("请先创建当前定额工时的查询字段");
            }

            ratedTime.setOrgId(orgId);
            ratedTime.setProjectId(project.getId());
            WorkbookUtils.readAsString(row, 1);

            ratedTime.setHourNorm(WorkbookUtils.readAsDouble(row, 13));
            ratedTime.setStatus(EntityStatus.ACTIVE);
            ratedTime.setCreatedBy(operatorId);
            ratedTime.setCreatedAt();
            ratedTime.setLastModifiedBy(operatorId);
            ratedTime.setLastModifiedAt();
            ratedTime.setDeleted(false);


            if (ratedTimes.size() > 0) {
                boolean status = false;
                for (int j = 0; j < ratedTimes.size(); j++) {
                    status = false;
                    if (
                        ratedTimes.get(j).getProcessStageId().equals(ratedTime.getProcessStageId())
                            && ratedTimes.get(j).getProcessId().equals(ratedTime.getProcessId())
                            && equalsDouble(ratedTimes.get(j).getPipeDiameter(), ratedTime.getPipeDiameter())
                            && equalsDouble(ratedTimes.get(j).getMinPipeThickness(), ratedTime.getMinPipeThickness())
                            && equalsDouble(ratedTimes.get(j).getMaxPipeThickness(), ratedTime.getMaxPipeThickness())
                            && equalsString(ratedTimes.get(j).getMaterial(), ratedTime.getMaterial())
                            && ratedTimes.get(j).getEntitySubTypeId().equals(ratedTime.getEntitySubTypeId())
                            && equalsDouble(ratedTimes.get(j).getMinTestPressure(), ratedTime.getMinTestPressure())
                            && equalsDouble(ratedTimes.get(j).getMaxTestPressure(), ratedTime.getMaxTestPressure())
                            && equalsString(ratedTimes.get(j).getCleaningMedium(), ratedTime.getCleaningMedium())
                            && equalsString(ratedTimes.get(j).getMedium(), ratedTime.getMedium())
                            && equalsUnit(ratedTimes.get(j).getUnit(), ratedTime.getUnit())
                            && equalsDouble(ratedTimes.get(j).getHourNorm(), ratedTime.getHourNorm())
                        ) {
                        errors.add("数据重复");
                        status = false;
                        break;
                    } else {
                        status = true;
                        continue;
                    }
                }
                if (ratedTimeCriterionItem != null && checkRatedTime(ratedTime)) {
                    errors.add("数据已存在");
                }
                if (status && errors.size() == 0) {
                    ratedTimes.add(ratedTime);
                }
            } else {
                if (ratedTimeCriterionItem != null && checkRatedTime(ratedTime)) {
                    errors.add("数据已存在");
                }
                if (errors.size() == 0) {
                    ratedTimes.add(ratedTime);
                }
            }

            if (errors.size() > 0) {
                WorkbookUtils
                    .setCellValue(sheet, i, errorColNo, String.join("；", errors))
                    .setCellStyle(errorStyle);
                failedCount++;
                continue;
            }

        }


        if (failedCount == 0 && ratedTimes.size() > 0) {
            ratedTimeRepository.saveAll(ratedTimes);
        }


        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
        FileMetadataDTO.FileUploadConfig uploadConfig = new FileMetadataDTO.FileUploadConfig();
        uploadConfig.setBizType("project/RatedTimeES");
        uploadConfig.setPublic(false);
        uploadConfig.setReserveOriginal(true);
        FileMetadataDTO metadata = new FileMetadataDTO();
        metadata.setFilename("import-rated-time-history.xlsx");
        metadata.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        metadata.setOrgId(orgId.toString());
        metadata.setUploadedAt(new Date());
        metadata.setUploadedBy(LongUtils.toString(operatorId));
        metadata.setConfig(uploadConfig);
        FileUtils.saveMetadata(excel, metadata);
        logger.error("定时任务 保存docs服务->开始");
        JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(
            orgId.toString(),
            project.getId().toString(),
            ratedTimeImportDTO.getFileName(),
            new FilePostDTO()
        );
        logger.error("定时任务 保存docs服务->结束");

        RatedTimeImportRecord ratedTimeImportRecord = new RatedTimeImportRecord();
        ratedTimeImportRecord.setBatchNo(batchNo);
        ratedTimeImportRecord.setFileId(LongUtils.parseLong(responseBody.getData().getId()));
        ratedTimeImportRecord.setFileName(responseBody.getData().getName());
        ratedTimeImportRecord.setOrgId(orgId);
        ratedTimeImportRecord.setProjectId(project.getId());
        ratedTimeImportRecord.setSuccessNum(ratedTimes.size());
        ratedTimeImportRecord.setFailedNum(failedCount);
        ratedTimeImportRecord.setCreatedBy(operatorId);
        ratedTimeImportRecord.setCreatedAt();
        ratedTimeImportRecord.setLastModifiedBy(operatorId);
        ratedTimeImportRecord.setLastModifiedAt();
        ratedTimeImportRecord.setStatus(EntityStatus.ACTIVE);
        ratedTimeImportRecordRepository.save(ratedTimeImportRecord);
        return ratedTimeImportRecordRepository.findByIdAndDeletedIsFalse(ratedTimeImportRecord.getId());
    }

    /**
     * 获取excel行。
     *
     * @param sheet  页
     * @param rowNum 行数
     */
    private Row getRow(Sheet sheet, int rowNum) {
        if (sheet == null) {
            return null;
        }
        return sheet.getRow(rowNum) != null ? sheet.getRow(rowNum)
            : sheet.createRow(rowNum);
    }

    /**
     * 获取excel单元格。
     *
     * @param row     行
     * @param cellNum 单元格数
     */
    private Cell getCell(Row row, int cellNum) {
        if (row == null) {
            return null;
        }
        return row.getCell(cellNum) != null ? row.getCell(cellNum)
            : row.createCell(cellNum);
    }

    /**
     * excel表格内数据字符串查重。
     *
     * @param ratedTime1
     * @param ratedTime2
     */
    private Boolean equalsString(String ratedTime1, String ratedTime2) {
        if (!StringUtils.isEmpty(ratedTime1) && !StringUtils.isEmpty(ratedTime2)) {
            if (ratedTime1.equals((ratedTime2))) {
                return true;
            } else {
                return false;
            }
        } else {
            if (StringUtils.isEmpty(ratedTime1) && StringUtils.isEmpty(ratedTime2)) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * excel表格内数据Double查重。
     *
     * @param ratedTime1
     * @param ratedTime2
     */
    private Boolean equalsDouble(Double ratedTime1, Double ratedTime2) {
        if (ratedTime1 != null && ratedTime2 != null) {
            if (ratedTime1.equals((ratedTime2))) {
                return true;
            } else {
                return false;
            }
        } else {
            if (ratedTime1 == null && ratedTime2 == null) {
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * excel表格内数据单元查重。
     *
     * @param ratedTime1
     * @param ratedTime2
     */
    private Boolean equalsUnit(RatedTimeUnit ratedTime1, RatedTimeUnit ratedTime2) {
        if (ratedTime1 != null && ratedTime2 != null) {
            if (ratedTime1.equals((ratedTime2))) {
                return true;
            } else {
                return false;
            }
        } else {
            if (ratedTime1 == null && ratedTime2 == null) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 数据库查重。
     *
     * @param ratedTime
     */
    private Boolean checkRatedTime(RatedTime ratedTime) {

        RatedTimeCriterion ratedTimeCriterion = ratedTimeCriterionRepository
            .findByOrgIdAndProjectIdAndProcessIdAndProcessStageIdAndEntitySubTypeIdAndDeletedIsFalse(ratedTime.getOrgId(), ratedTime.getProjectId(), ratedTime.getProcessId(), ratedTime.getProcessStageId(), ratedTime.getEntitySubTypeId());

        List<RatedTime> ratedTimeList = ratedTimeRepository
            .findByRatedTimeCriterionIdAndDeletedIsFalse(ratedTimeCriterion.getId());
        Boolean Status = false;
        if (ratedTimeList.size() > 0) {
            for (int j = 0; j < ratedTimeList.size(); j++) {
                if (
                    equalsDouble(ratedTimeList.get(j).getPipeDiameter(), ratedTime.getPipeDiameter())
                        && equalsDouble(ratedTimeList.get(j).getMinPipeThickness(), ratedTime.getMinPipeThickness())
                        && equalsDouble(ratedTimeList.get(j).getMaxPipeThickness(), ratedTime.getMaxPipeThickness())
                        && equalsString(ratedTimeList.get(j).getMaterial(), ratedTime.getMaterial())
                        && equalsDouble(ratedTimeList.get(j).getMaxTestPressure(), ratedTime.getMaxTestPressure())
                        && equalsDouble(ratedTimeList.get(j).getMinTestPressure(), ratedTime.getMinTestPressure())
                        && equalsString(ratedTimeList.get(j).getCleaningMedium(), ratedTime.getCleaningMedium())
                        && equalsString(ratedTimeList.get(j).getMedium(), ratedTime.getMedium())
                        && equalsUnit(ratedTimeList.get(j).getUnit(), ratedTime.getUnit())
                        && equalsUnit(ratedTimeList.get(j).getUnit(), ratedTime.getUnit())
                    ) {
                    Status = true;
                }
            }
        }
        return Status;
    }
}
