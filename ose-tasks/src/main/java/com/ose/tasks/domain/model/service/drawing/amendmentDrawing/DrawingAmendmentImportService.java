package com.ose.tasks.domain.model.service.drawing.amendmentDrawing;

import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.TeamPrivilegeDTO;
import com.ose.auth.dto.TeamPrivilegeListDTO;
import com.ose.auth.entity.UserBasic;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.BatchTaskBasicRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingAmendmentRepository;
import com.ose.tasks.domain.model.repository.drawing.externalDrawing.ExternalDrawingRepository;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingImportColumnDTO;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.BatchTaskBasic;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.externalDrawing.DrawingAmendment;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Component
public class DrawingAmendmentImportService extends StringRedisService implements DrawingAmendmentImportInterface {

    private class Coordinates {

        private int row;
        private int column;

        public Coordinates(int row, int column) {
            super();
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }
    }

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    private DrawingAmendmentRepository drawingAmendmentRepository;

    private ExternalDrawingRepository externalDrawingRepository;

    // 批处理任务数据仓库
    private final BatchTaskBasicRepository batchTaskBasicRepository;

    private final UserFeignAPI userFeignAPI;

    private final BpmEntitySubTypeRepository bpmEntitySubTypeRepository;

    private Set<String> supportedDisciplines = new HashSet<>(
        Arrays.asList(
            "STRUCTURE",
            "PIPING"
        )
    );

    /**
     * 构造方法
     */
    @Autowired
    public DrawingAmendmentImportService(
        DrawingAmendmentRepository drawingAmendmentRepository,
        ExternalDrawingRepository externalDrawingRepository,
        StringRedisTemplate stringRedisTemplate,
        BatchTaskBasicRepository batchTaskBasicRepository,
        UserFeignAPI userFeignAPI,
        BpmEntitySubTypeRepository bpmEntitySubTypeRepository
    ) {
        super(stringRedisTemplate);
        this.drawingAmendmentRepository = drawingAmendmentRepository;
        this.batchTaskBasicRepository = batchTaskBasicRepository;
        this.userFeignAPI = userFeignAPI;
        this.bpmEntitySubTypeRepository = bpmEntitySubTypeRepository;
        this.externalDrawingRepository = externalDrawingRepository;
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

    /**
     * 导入{管道}生产设计图纸清单
     */
    @Override
    public BatchResultDTO importDrawingList(Long orgId,
                                            Long projectId,
                                            OperatorDTO operator,
                                            BatchTask batchTask,
                                            DrawingImportDTO importDTO,
                                            String discipline
    ) {

        if (discipline == null) discipline = "PIPING";

        if (!supportedDisciplines.contains(discipline.toUpperCase())) {
            throw new BusinessError("not supported discipline");
        }

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
        if (sheetNum < 1) {
            throw new BusinessError("there is no importSheet");
        }
//        workbook.getSheet()
        DrawingImportColumnDTO columnConfig = new DrawingImportColumnDTO();

        switch (discipline.toUpperCase()) {
            case "PIPING":
                columnConfig.setDwgNoColumn(3);
                columnConfig.setDwgTitleColumn(4);
                columnConfig.setPlanEndColumn(23);
                columnConfig.setPlanStartColumn(22);
                columnConfig.setRatedHoursColumn(5);
                columnConfig.setSeqNoColumn(0);
                break;

            case "STRUCTURE":
                columnConfig.setSeqNoColumn(0);
                columnConfig.setAreaName(1);
                columnConfig.setModuleName(2);
                columnConfig.setAmendmentNo(3);
                columnConfig.setDwgNoColumn(4);
                columnConfig.setDwgTitleColumn(5);
                columnConfig.setLatestRevColumn(6);
                columnConfig.setRatedHoursColumn(7);
                columnConfig.setPlanStartColumn(8);
                columnConfig.setPlanEndColumn(9);
                columnConfig.setIsSubDrawing(10);
                columnConfig.setDrawingCategory(11);
                columnConfig.setWorkNet(12);
                columnConfig.setSection(13);
                columnConfig.setBlock(14);
                columnConfig.setSmallArea(15);
                columnConfig.setDesignDiscipline(16);
                columnConfig.setInstallationDrawingNo(17);
                break;

        }

        sheetImportResult = importDrawing(
            orgId,
            projectId,
            operator,
            workbook.getSheetAt(0),
            batchResult,
            columnConfig
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


    /**
     * 导入pi sheet
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param operator
     * @param sheet
     * @param batchResult
     * @return
     */
    private BatchResultDTO importDrawing(Long orgId,
                                         Long projectId,
                                         OperatorDTO operator,
                                         Sheet sheet,
                                         BatchResultDTO batchResult,
                                         DrawingImportColumnDTO columnConfig
    ) {

        final String progressKey = "BATCH_TASK:" + batchResult.getTaskId() + ":PROGRESS";
        Iterator<Row> rows = sheet.rowIterator();
        Row row;
        int totalCount = 0;
        int skippedCount = 0;
        int processedCount = 0;
        int errorCount = 0;
        Date date = new Date();

        List<TeamPrivilegeDTO> teamList = new ArrayList<>();
        TeamPrivilegeDTO teamPrivilegeDTO = new TeamPrivilegeDTO();
        teamPrivilegeDTO.setTeamId(orgId);
        Set<String> memberPrivileges = new HashSet<>();
        memberPrivileges.add(UserPrivilege.DESIGN_ENGINEER_EXECUTE.toString());
        teamPrivilegeDTO.setMemberPrivileges(memberPrivileges);
        teamList.add(teamPrivilegeDTO);
        List<UserBasic> drawUsers = userFeignAPI
            .getByPrivileges(orgId, new TeamPrivilegeListDTO(teamList))
            .getData();

        teamList.clear();
        teamPrivilegeDTO = new TeamPrivilegeDTO();
        teamPrivilegeDTO.setTeamId(orgId);
        memberPrivileges = new HashSet<>();
        memberPrivileges.add(UserPrivilege.DRAWING_CHECK_EXECUTE.toString());
        teamPrivilegeDTO.setMemberPrivileges(memberPrivileges);
        teamList.add(teamPrivilegeDTO);
        List<UserBasic> checkUsers = userFeignAPI
            .getByPrivileges(orgId, new TeamPrivilegeListDTO(teamList))
            .getData();

        teamList.clear();
        teamPrivilegeDTO = new TeamPrivilegeDTO();
        teamPrivilegeDTO.setTeamId(orgId);
        memberPrivileges = new HashSet<>();
        memberPrivileges.add(UserPrivilege.DRAWING_REVIEW_EXECUTE.toString());
        teamPrivilegeDTO.setMemberPrivileges(memberPrivileges);
        teamList.add(teamPrivilegeDTO);
        List<UserBasic> approveUsers = userFeignAPI
            .getByPrivileges(orgId, new TeamPrivilegeListDTO(teamList))
            .getData();

        Integer areaName = columnConfig.getAreaName(); //1
        Integer moduleName = columnConfig.getModuleName(); //2
        Integer dwgNoColumn = columnConfig.getDwgNoColumn(); //3
        Integer dwgTitleColumn = columnConfig.getDwgTitleColumn(); //4
        Integer latestRevColumn = columnConfig.getLatestRevColumn(); //5
        Integer ratedHoursColumn = columnConfig.getRatedHoursColumn(); //6
        Integer planStartColumn = columnConfig.getPlanStartColumn(); //23
        Integer planEndColumn = columnConfig.getPlanEndColumn(); //24
        Integer isSubDrawing = columnConfig.getIsSubDrawing(); //10

        Integer drawingCategory = columnConfig.getDrawingCategory();
        Integer workNet = columnConfig.getWorkNet();
        Integer section = columnConfig.getSection();
        Integer block = columnConfig.getBlock();
        Integer smallArea = columnConfig.getSmallArea();
        Integer designDiscipline = columnConfig.getDesignDiscipline();
        Integer installationDrawingNo = columnConfig.getInstallationDrawingNo();
        Integer amendmentNoColumn = columnConfig.getAmendmentNo();


        rows = sheet.rowIterator();
        /*-------
        2.0 导入图纸内容
         */
        while (rows.hasNext()) {

            row = rows.next();
            int colIndex = 0;
            if (row.getRowNum() < BpmCode.SD_HEADER) {
                continue;
            }
            try {

                batchResult.addTotalCount(1);
                totalCount++;
                //导入主表记录
                DrawingAmendment drawing = new DrawingAmendment();
                drawing.setBatchTaskId(batchResult.getTaskId());

                String amendmentNo = WorkbookUtils.readAsString(row, amendmentNoColumn);
                if (amendmentNo == null
                    || "".equals(amendmentNo)) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "drawing latestRev. error.");
                    continue;
                }
                drawing.setNo(amendmentNo);

                String latestRev = WorkbookUtils.readAsString(row, latestRevColumn);
                if (latestRev == null
                    || "".equals(latestRev)) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "修改单编号丢失");
                    continue;
                }
                drawing.setLatestRev(latestRev);

                String area = WorkbookUtils.readAsString(row, areaName);
                if (area == null || "".equals(area)) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "区域号丢失");
                    continue;
                }
                drawing.setAreaName(area);

                String module = WorkbookUtils.readAsString(row, moduleName);
                if (module == null || "".equals(module)) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "模块号丢失");
                    continue;
                }
                drawing.setModuleName(module);


                String dwgNo = WorkbookUtils.readAsString(row, dwgNoColumn);
                if (dwgNo == null
                    || "".equals(dwgNo)) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "drawing no. error.");
                    continue;
                }
                boolean flag = externalDrawingRepository.existsByDwgNoAndStatus(dwgNo, EntityStatus.ACTIVE);
                if ( !flag ) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "图纸编号不存在");
                    continue;
                }

                drawing.setDwgNo(dwgNo);

                String dwgTitle = WorkbookUtils.readAsString(row, dwgTitleColumn);
                drawing.setDrawingTitle(dwgTitle);
                if (dwgTitle == null
                    || "".equals(dwgTitle)) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "drawing title error.");
                    continue;
                }

                Double normalHours = WorkbookUtils.readAsDouble(row, ratedHoursColumn);
//                if (normalHours == 0) {
//                    errorCount++;
//                    batchResult.addErrorCount(1);
//                    setImportDataErrorMessage(row, "normal hours error.");
//                    continue;
//                }
//                drawing.setEstimatedManHours(normalHours);

                Date start = WorkbookUtils.readAsDate(row, planStartColumn);
                if (start == null) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "start date error.");
                    continue;
                }
                drawing.setEngineeringStartDate(start);

                Date end = WorkbookUtils.readAsDate(row, planEndColumn);
                if (end == null) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "delivery date error.");
                    continue;
                }
                drawing.setEngineeringFinishDate(end);

                Boolean isSub = WorkbookUtils.readAsBoolean(row, isSubDrawing);
//                if (isSub == null) {
//                    errorCount++;
//                    batchResult.addErrorCount(1);
//                    setImportDataErrorMessage(row, "没有填写是否包含子图纸");
//                    continue;
//                }

                String drawingCate = WorkbookUtils.readAsString(row, drawingCategory);
                BpmEntitySubType bpmEntitySubType = bpmEntitySubTypeRepository.findByProjectIdAndNameEn(projectId, drawingCate);
                if (bpmEntitySubType == null) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "图纸类型不存在");
                    continue;
                }
                drawing.setDrawingCategory(bpmEntitySubType);

                String network = WorkbookUtils.readAsString(row, workNet);
                drawing.setWorkNet(network);

                String sectionCol = WorkbookUtils.readAsString(row, section);
                drawing.setSection(sectionCol);

                String blockCol = WorkbookUtils.readAsString(row, block);
                drawing.setBlock(blockCol);

                String smallAreaCol = WorkbookUtils.readAsString(row, smallArea);
                drawing.setSmallArea(smallAreaCol);

                String designCol = WorkbookUtils.readAsString(row, designDiscipline);
                drawing.setDesignDiscipline(designCol);

                String installationCol = WorkbookUtils.readAsString(row, installationDrawingNo);
                drawing.setInstallationDrawingNo(installationCol);







                /*----
                2.1 检查 图纸是否已经存在
                 */
                Optional<DrawingAmendment> op = drawingAmendmentRepository.findByOrgIdAndProjectIdAndNoAndStatus(orgId, projectId, drawing.getNo(), EntityStatus.ACTIVE);
                if (op.isPresent()) {
                    errorCount++;
                    batchResult.addErrorCount(1);
                    setImportDataErrorMessage(row, "修改单已存在");
                } else {
                    //如果不存在，设置属性，并保存
                    drawing.setOperator(operator.getId());
                    drawing.setOrgId(orgId);
                    drawing.setProjectId(projectId);
                    drawing.setLastModifiedAt(date);
                    drawing.setCreatedAt(date);
                    drawing.setStatus(EntityStatus.ACTIVE);
//                    drawing.setLatestRev("0");
                    drawingAmendmentRepository.save(drawing);


                    processedCount++;
                    batchResult.addProcessedCount(1);

                }

                // TODO 通过 MQTT 更新导入进度
                setRedisKey(progressKey, "" + batchResult.getProgress(), 3000);

            } catch (Exception e) {
                e.printStackTrace(System.out);
                errorCount++;
                batchResult.addErrorCount(1);
                setImportDataErrorMessage(row, "" + colIndex + "th import error.");
            }

        }

        return new BatchResultDTO(
            totalCount,
            processedCount,
            skippedCount,
            errorCount
        );
    }

    /**
     * 查询导入记录
     */
    @Override
    public Page<BatchTaskBasic> search(Long orgId, Long projectId, BatchTaskCriteriaDTO batchTaskCriteriaDTO, PageDTO page) {
        return batchTaskBasicRepository.searchDrawing(
            null,
            orgId,
            projectId,
            batchTaskCriteriaDTO,
            page.toPageable()
        );
    }

    /**
     * 更新importFileId
     */
//    @Transactional
//    @Override
//    public boolean updateFileId(Long batchTaskId, Long fileId) {
//        List<ExternalDrawing> list = drawingAmendmentRepository.findByBatchTaskId(batchTaskId);
//        if (!list.isEmpty()) {
//            drawingAmendmentRepository.updateFileIdIn(fileId, list);
//        }
//        return true;
//    }
}
