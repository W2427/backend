package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.auth.api.RoleFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.TeamPrivilegeDTO;
import com.ose.auth.dto.TeamPrivilegeListDTO;
import com.ose.auth.dto.UserNameCriteriaDTO;
import com.ose.auth.entity.Role;
import com.ose.auth.entity.UserBasic;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.controller.bpm.ActivityTaskController;
import com.ose.tasks.domain.model.repository.*;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.domain.model.service.xlsximport.XlsxImportInterface;
import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.DrawingPlanHour;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingEntryDelegateRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.service.HierarchyNodeRelationInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingImportInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder.SheetConfig;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.drawing.DrawingImportColumnDTO;
import com.ose.tasks.dto.drawing.DrawingImportDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.BatchTaskBasic;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingEntryDelegate;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import static com.ose.tasks.domain.model.service.ProjectInterface.ENGINEERING_NODE_TYPES;
import static com.ose.tasks.domain.model.service.ProjectInterface.ENGINEERING_OPTIONAL_NODE_TYPES;
import static com.ose.vo.EntityStatus.ACTIVE;

@Component
public class DrawingImportService extends StringRedisService implements DrawingImportInterface {

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


    @Value("${application.files.temporary}")
    private String temporaryDir;

    private final String SETTING = "SETTING";

    private DrawingRepository drawingRepository;

    private final WBSEntityImportSheetConfigBuilder sheetConfigBuilder;

    private final BatchTaskBasicRepository batchTaskBasicRepository;

    private final UserFeignAPI userFeignAPI;

    private final RoleFeignAPI roleFeignAPI;

    private DrawingEntryDelegateRepository drawingEntryDelegateRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final HierarchyRepository hierarchyRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final ProjectInterface projectService;

    private final HierarchyNodeRelationInterface hierarchyNodeRelationService;

    private final ProjectRepository projectRepository;

    private final XlsxImportInterface xlsxImportService;

    private final BatchTaskRepository batchTaskRepository;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final ActivityTaskInterface activityTaskService;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final BpmRuTaskRepository bpmRuTaskRepository;

    private final ActivityTaskController activityTaskController;

    private final BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private final BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository;

    private final DrawingPlanHourRepository drawingPlanHourRepository;

    private final TaskRuleCheckService taskRuleCheckService;

    private final DrawingService drawingService;

    private static String NO_PARENT = "NO_PARENT";

    private static String SECTOR = "SECTOR";
    private static String FUNCTION = "FUNCTION";
    private static String TYPE = "TYPE";
    private static String ENTITY_SUB_TYPE = "ENTITY_SUB_TYPE";

    private static String ENTITY_TYPE = "ENTITY_TYPE";
    private static String CHAIN = "CHAIN";
    private static String FUNC_PART = "FUNC_PART";
    private static Set<String> columnKeys = new HashSet<String>() {{
//        add(SECTOR);
        add(FUNCTION);
        add(TYPE);
        add(ENTITY_SUB_TYPE);
        add(ENTITY_TYPE);
        add(CHAIN);
//        add(FUNC_PART);
    }};
    private static Map<String, Integer> columnMap = new HashMap<>();


    /**
     * 构造方法
     */
    @Autowired
    public DrawingImportService(
        DrawingRepository drawingRepository,
        StringRedisTemplate stringRedisTemplate,
        WBSEntityImportSheetConfigBuilder sheetConfigBuilder,
        BatchTaskBasicRepository batchTaskBasicRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        UserFeignAPI userFeignAPI,
        DrawingEntryDelegateRepository drawingEntryDelegateRepository,
        DrawingDetailRepository drawingDetailRepository,
        HierarchyRepository hierarchyRepository,
        ProjectNodeRepository projectNodeRepository,
        ProjectInterface projectService,
        HierarchyNodeRelationInterface hierarchyNodeRelationService,
        ProjectRepository projectRepository,
        XlsxImportInterface xlsxImportService,
        ActivityTaskInterface activityTaskService,
        BatchTaskRepository batchTaskRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        TodoTaskBaseInterface todoTaskBaseService,
        BpmRuTaskRepository bpmRuTaskRepository,
        ActivityTaskController activityTaskController,
        BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository,
        RoleFeignAPI roleFeignAPI,
        BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository,
        DrawingPlanHourRepository drawingPlanHourRepository,
        TaskRuleCheckService taskRuleCheckService,
        DrawingService drawingService) {
        super(stringRedisTemplate);
        this.drawingRepository = drawingRepository;
        this.sheetConfigBuilder = sheetConfigBuilder;
        this.batchTaskBasicRepository = batchTaskBasicRepository;
        this.userFeignAPI = userFeignAPI;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.projectService = projectService;
        this.hierarchyNodeRelationService = hierarchyNodeRelationService;
        this.drawingEntryDelegateRepository = drawingEntryDelegateRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.projectRepository = projectRepository;
        this.xlsxImportService = xlsxImportService;
        this.batchTaskRepository = batchTaskRepository;
        this.bpmActInstRepository = bpmActInstRepository;
        this.activityTaskService = activityTaskService;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.activityTaskController = activityTaskController;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
        this.roleFeignAPI = roleFeignAPI;
        this.bpmDrawingSignTaskRepository = bpmDrawingSignTaskRepository;
        this.taskRuleCheckService = taskRuleCheckService;
        this.drawingService = drawingService;
        this.drawingPlanHourRepository = drawingPlanHourRepository;
    }

    /**
     * 获取合并单元格的值所在的坐标
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    private Coordinates getMergedRegionValue(Sheet sheet, Row row, int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();

            if (row.getRowNum() >= firstRow && row.getRowNum() <= lastRow) {

                if (column >= firstColumn && column <= lastColumn) {
/*                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);    */
                    return new Coordinates(firstRow, firstColumn);
                }
            }
        }
        return null;
    }

    /**
     * 判断是否是单元格
     *
     * @param sheet
     * @param row
     * @param colIndex
     * @return
     */
    private boolean isMergedRegion(Sheet sheet, Row row, int colIndex) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row.getRowNum() >= firstRow && row.getRowNum() <= lastRow) {
                if (colIndex >= firstColumn && colIndex <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
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

        Project project = projectService.get(projectId);
        Set<String> optNodeTypes = new HashSet<>();
        Set<String> allNodeTypes = new HashSet<>();
        List<String> currentNodeTypes = new ArrayList<>();
        ENGINEERING_NODE_TYPES.forEach(ent -> {
            currentNodeTypes.add(ent);
        });
        if (!StringUtils.isEmpty(project.getEngineeringOptionalNodeTypes())) {
            optNodeTypes = new HashSet<>(Arrays.asList(project.getEngineeringOptionalNodeTypes().split(",")));
        }
        for (String eont : ENGINEERING_OPTIONAL_NODE_TYPES) {
            allNodeTypes.add(eont);
            if (!optNodeTypes.contains(eont)) {
                currentNodeTypes.remove(eont);
            }
        }

        Workbook workbook;
        File excel;


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

        DrawingImportColumnDTO columnConfig = new DrawingImportColumnDTO();

        Sheet setting = workbook.getSheet(SETTING);
        // 读取实体导入工作表配置
        List<SheetConfig> configs;
        try {
            configs = sheetConfigBuilder
                .readConfigs(workbook.getSheet(SETTING));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        boolean checkedKeyColumn = false;
        for (SheetConfig config : configs) {
            //读取对应实体表的 列配置信息
            Sheet columnSetting = workbook.getSheet(config.getColumnSettingSheetName());

            // 取得所有自定义属性信息
            List<ColumnImportConfig> columnImportConfigs
                = xlsxImportService.getColumnDefinitions(columnSetting, config.getDb(),
                config.getTable(), orgId, projectId, config.getEntityTypeClass().getName(), config.getHeaderRows());

//            int maxColumnNo = Collections.max(columnImportConfigs.stream().map(ColumnImportConfigBase::getColumnNo).collect(java.util.stream.Collectors.toList()));
            int rowCount = columnSetting.getPhysicalNumberOfRows();

            if (!checkedKeyColumn) {
                for (int rowIndex = config.getHeaderRows(); rowIndex < rowCount; rowIndex++) {

                    Row row = columnSetting.getRow(rowIndex);
                    if (row.getCell(5) == null) continue;
                    String idxKey = StringUtils.trim(row.getCell(5).getStringCellValue());
                    if (StringUtils.isEmpty(idxKey)) continue;
                    Integer idxVal = row.getCell(1).getCellTypeEnum() == CellType.STRING ? Integer.parseInt(row.getCell(1).getStringCellValue()) : (int) row.getCell(1).getNumericCellValue();
                    if (!StringUtils.isEmpty(idxKey)) {
                        if (allNodeTypes.contains(idxKey.toUpperCase()) && !currentNodeTypes.contains(idxKey.toUpperCase())) {
//                    columnMap.put(idxKey.toUpperCase(), idxVal);
                        } else {
                            columnMap.put(idxKey.toUpperCase(), idxVal);
                        }
                    }
                }
                if (!columnMap.keySet().containsAll(columnKeys)) {
                    throw new BusinessError("NO Proper setting");
                }
                checkedKeyColumn = true;
            }


            sheetImportResult = importDrawing(
                orgId,
                projectId,
                operator,
                config.getSheet(),
                batchResult,
                columnImportConfigs,
                currentNodeTypes,
                config,
                batchTask,
                importDTO.getDrawingType()
            );

            batchResult.addLog(
                sheetImportResult.getProcessedCount()
                    + " "
                    + workbook.getSheetAt(0).getSheetName()
                    + " imported."
            );
        }


        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {

            e.printStackTrace(System.out);

        }

        return batchResult;
    }


    private Map<String, UserProfile> getUserInfos(Long orgId) {
        Map<String, UserProfile> userMap = new HashMap<>();

        List<UserProfile> userProfiles = userFeignAPI.batchGetByOrgId(orgId).getData();

        userProfiles.forEach(up -> {
            userMap.put(up.getEmail(), up);
        });

        return userMap;
    }

    private void handleDrawingEntryDelegate(Drawing drawing, UserPrivilege privilege, Long userId, Long operatorId) {
        DrawingEntryDelegate ded = drawingEntryDelegateRepository.findByDrawingIdAndPrivilegeAndStatus(drawing.getId(), privilege, ACTIVE);
        if (ded != null) {
            ded.setUserId(userId);
        } else {
            ded = new DrawingEntryDelegate();
            ded.setUserId(userId);
            ded.setDrawingId(drawing.getId());
            ded.setPrivilege(privilege);
            ded.setCreatedAt();
            ded.setCreatedBy(operatorId);
            ded.setLastModifiedAt();
            ded.setLastModifiedBy(operatorId);
            ded.setStatus(ACTIVE);
        }
        drawingEntryDelegateRepository.save(ded);
    }

    private void handleDrawingTask(Long orgId, Long projectId, Drawing drawing, UserProfile userProfile, Long operatorId, String category) {

        List<BpmActivityInstanceBase> actInsts = bpmActInstRepository.findByProjectIdAndEntityNoAndFinishStateIsRunning(projectId, drawing.getNo());

        if (actInsts.size() > 0) {
            for (BpmActivityInstanceBase actInst : actInsts) {
                List<BpmActTaskAssignee> bpmActTaskAssigneeList = bpmActTaskAssigneeRepository.findByActInstIdAndTaskCategory(actInst.getId(), category);
                for (BpmActTaskAssignee bpmActTaskAssignee : bpmActTaskAssigneeList) {
//                    activityTaskController.activityTaskAssignee(orgId, projectId, actInst.getId(), bpmActTaskAssignee.getId(), userId);

                    String name = "";
                    try {
                        JsonObjectResponseBody<UserProfile> userResponse = userFeignAPI.get(userProfile.getId());
                        name += ", " + userResponse.getData().getName();
                    } catch (Exception e) {
                        JsonObjectResponseBody<Role> roleResponse = roleFeignAPI.get(orgId, userProfile.getId());
                        if (roleResponse.getData() != null) {
                            name += ", " + roleResponse.getData().getName();
                        }
                    }

                    if (name.length() > 0) {
                        name = name.substring(2);
                    }

//                    BpmActTaskAssignee actAssignee = activityTaskService.findActTaskAssigneesById(bpmActTaskAssignee.getId());
                    if (bpmActTaskAssignee != null) {

                        BpmActivityInstanceState bpmActivityInstanceState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
                        if (bpmActivityInstanceState == null) {
                            bpmActivityInstanceState = new BpmActivityInstanceState();
                            bpmActivityInstanceState.setBaiId(actInst.getId());
                            bpmActivityInstanceState.setOrgId(orgId);
                            bpmActivityInstanceState.setProjectId(projectId);
                        }

                        activityTaskService.modifyTaskAssignee(bpmActTaskAssignee.getId(), userProfile.getId(), name);//, operatorDTO);
                        activityTaskService.assignee(actInst.getId(), bpmActTaskAssignee.getTaskDefKey(),
                            bpmActTaskAssignee.getTaskName(), userProfile.getId(), operatorId.toString());
                        List<BpmRuTask> ruTaskList = activityTaskService.findBpmRuTaskByActInstId(actInst.getId());
                        String currentExecutor = "";

                        currentExecutor = todoTaskBaseService.setTaskAssignee(ruTaskList);

                        bpmActivityInstanceState.setCurrentExecutor(currentExecutor);
                        bpmActivityInstanceStateRepository.save(bpmActivityInstanceState);

                        if ((taskRuleCheckService.isDrawingDesignTaskNode(bpmActTaskAssignee.getTaskDefKey()) || taskRuleCheckService.isRedMarkDesignTaskNode(bpmActTaskAssignee.getTaskDefKey()))
                            && taskRuleCheckService.DESIGN_PROCESSES.contains(actInst.getProcess())) {
                            Drawing dwg = drawingService.findByDwgNo(orgId, projectId, actInst.getEntityNo());
                            drawingService.save(dwg);

                            DrawingEntryDelegate drawingEntryDelegate = drawingEntryDelegateRepository
                                .findByDrawingIdAndPrivilegeAndStatus(dwg.getId(), UserPrivilege.getByName(bpmActTaskAssignee.getTaskCategory()), EntityStatus.ACTIVE);
                            if (drawingEntryDelegate == null) {
                                drawingEntryDelegate = new DrawingEntryDelegate();
                                drawingEntryDelegate.setCreatedAt();
                                drawingEntryDelegate.setCreatedBy(operatorId);
                                drawingEntryDelegate.setDrawingId(drawing.getId());
                                drawingEntryDelegate.setPrivilege(UserPrivilege.getByName(bpmActTaskAssignee.getTaskCategory()));
                                drawingEntryDelegate.setStatus(EntityStatus.ACTIVE);
                            }
                            drawingEntryDelegate.setUserId(userProfile.getId());
                            drawingEntryDelegate.setLastModifiedAt();
                            drawingEntryDelegate.setLastModifiedBy(operatorId);
                            drawingEntryDelegateRepository.save(drawingEntryDelegate);
                        }

                        if ((taskRuleCheckService.isDrawingCheckTaskNode(bpmActTaskAssignee.getTaskDefKey()) || taskRuleCheckService.isRedMarkCheckTaskNode(bpmActTaskAssignee.getTaskDefKey()))
                            && taskRuleCheckService.DESIGN_PROCESSES.contains(actInst.getProcess())) {
                            Drawing dwg = drawingService.findByDwgNo(orgId, projectId, actInst.getEntityNo());
                            drawingService.save(dwg);

                            DrawingEntryDelegate drawingEntryDelegate = drawingEntryDelegateRepository
                                .findByDrawingIdAndPrivilegeAndStatus(dwg.getId(), UserPrivilege.getByName(bpmActTaskAssignee.getTaskCategory()), EntityStatus.ACTIVE);
                            if (drawingEntryDelegate == null) {
                                drawingEntryDelegate = new DrawingEntryDelegate();
                                drawingEntryDelegate.setCreatedAt();
                                drawingEntryDelegate.setCreatedBy(operatorId);
                                drawingEntryDelegate.setDrawingId(userProfile.getId());
                                drawingEntryDelegate.setPrivilege(UserPrivilege.getByName(bpmActTaskAssignee.getTaskCategory()));
                                drawingEntryDelegate.setStatus(EntityStatus.ACTIVE);
                            }
                            drawingEntryDelegate.setUserId(userProfile.getId());
                            drawingEntryDelegate.setLastModifiedAt();
                            drawingEntryDelegate.setLastModifiedBy(operatorId);
                            drawingEntryDelegateRepository.save(drawingEntryDelegate);
                        }

                        if ((taskRuleCheckService.isDrawingApprovedTaskNode(bpmActTaskAssignee.getTaskDefKey()))
                            && taskRuleCheckService.DESIGN_PROCESSES.contains(actInst.getProcess())) {
                            Drawing dwg = drawingService.findByDwgNo(orgId, projectId, actInst.getEntityNo());
                            drawingService.save(dwg);

                            DrawingEntryDelegate drawingEntryDelegate = drawingEntryDelegateRepository
                                .findByDrawingIdAndPrivilegeAndStatus(dwg.getId(), UserPrivilege.getByName(bpmActTaskAssignee.getTaskCategory()), EntityStatus.ACTIVE);
                            if (drawingEntryDelegate == null) {
                                drawingEntryDelegate = new DrawingEntryDelegate();
                                drawingEntryDelegate.setCreatedAt();
                                drawingEntryDelegate.setCreatedBy(operatorId);
                                drawingEntryDelegate.setDrawingId(drawing.getId());
                                drawingEntryDelegate.setPrivilege(UserPrivilege.getByName(bpmActTaskAssignee.getTaskCategory()));
                                drawingEntryDelegate.setStatus(EntityStatus.ACTIVE);
                            }
                            drawingEntryDelegate.setUserId(userProfile.getId());
                            drawingEntryDelegate.setLastModifiedAt();
                            drawingEntryDelegate.setLastModifiedBy(operatorId);
                            drawingEntryDelegateRepository.save(drawingEntryDelegate);
                        }
                    }
                }
//                List<BpmRuTask> ruTaskList = bpmRuTaskRepository.findByActInstIdAndCategory(actInst.getId(), category);
//                for (BpmRuTask todo : ruTaskList) {
//                    todo.setAssignee(userProfile.getId().toString());
//                    bpmRuTaskRepository.save(todo);
//
//                    BpmActivityInstanceState bpmActivityInstanceState = bpmActivityInstanceStateRepository.findByBaiId(todo.getActInstId());
//                    if (bpmActivityInstanceState != null) {
//                        bpmActivityInstanceState.setCurrentExecutor(userProfile.getName());
//                        bpmActivityInstanceStateRepository.save(bpmActivityInstanceState);
//                    }
//
//                }
            }
        }

    }

    private void handleDrawingPlanHour(Long projectId, Drawing drawing, Long userId, String category) {
        Date engineeringStartDate = drawing.getEngineeringStartDate();
        Date engineeringFinishDate = drawing.getEngineeringFinishDate();

        List<Integer> months = new ArrayList<>();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(engineeringStartDate);
        startCal.set(Calendar.DAY_OF_MONTH, 1); // 设置为当月1号
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(engineeringFinishDate);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");

        while (startCal.before(endCal) || startCal.equals(endCal)) {
            months.add(Integer.valueOf(sdf.format(startCal.getTime())));
            startCal.add(Calendar.MONTH, 1);
        }

        Long drawingId = drawing.getId();

        //设校审631比例
        Double proportion = 0.0;
        if ("DESIGN_ENGINEER_EXECUTE".equals(category)) {
            proportion = 0.6;
        } else if ("DRAWING_REVIEW_EXECUTE".equals(category)) {
            proportion = 0.3;
        }else if ("DRAWING_APPROVE_EXECUTE".equals(category)){
            proportion = 0.1;
        }

        Double planHours = BigDecimal.valueOf((drawing.getPlanHours() / (double) months.size()) * proportion)
                            .setScale(2, RoundingMode.HALF_UP)
                            .doubleValue();


        for (Integer month : months) {
            DrawingPlanHour drawingPlanHour = drawingPlanHourRepository.findByProjectIdAndDrawingIdAndPrivilegeAndMonthly(projectId, drawingId, category, month);
            if (drawingPlanHour == null){
                drawingPlanHour = new DrawingPlanHour();
            }
            drawingPlanHour.setDrawingId(drawingId);
            drawingPlanHour.setProjectId(projectId);
            drawingPlanHour.setPrivilege(category);
            drawingPlanHour.setUserId(userId);
            drawingPlanHour.setMonthly(month);
            drawingPlanHour.setHours(planHours);

            drawingPlanHourRepository.save(drawingPlanHour);
        }

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
                                         List<ColumnImportConfig> columnConfigs,
                                         List<String> currentNodeTypes,
                                         SheetConfig sheetConfig,
                                         BatchTask batchTask,
                                         String drawingType
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

        teamList.clear();
        teamPrivilegeDTO = new TeamPrivilegeDTO();
        teamPrivilegeDTO.setTeamId(orgId);
        memberPrivileges = new HashSet<>();
        memberPrivileges.add(UserPrivilege.DRAWING_CHECK_EXECUTE.toString());
        teamPrivilegeDTO.setMemberPrivileges(memberPrivileges);
        teamList.add(teamPrivilegeDTO);
        List<UserBasic> coSignUsers = userFeignAPI
            .getByPrivileges(orgId, new TeamPrivilegeListDTO(teamList))
            .getData();

        teamList.clear();
        teamPrivilegeDTO = new TeamPrivilegeDTO();
        teamPrivilegeDTO.setTeamId(orgId);
        memberPrivileges = new HashSet<>();
        memberPrivileges.add(UserPrivilege.DRAWING_QC_EXECUTE.toString());
        teamPrivilegeDTO.setMemberPrivileges(memberPrivileges);
        teamList.add(teamPrivilegeDTO);
        List<UserBasic> qcUsers = userFeignAPI
            .getByPrivileges(orgId, new TeamPrivilegeListDTO(teamList))
            .getData();

        teamList.clear();
        teamPrivilegeDTO = new TeamPrivilegeDTO();
        teamPrivilegeDTO.setTeamId(orgId);
        memberPrivileges = new HashSet<>();
        memberPrivileges.add(UserPrivilege.DOCUMENT_CONTROL_EXECUTE.toString());
        teamPrivilegeDTO.setMemberPrivileges(memberPrivileges);
        teamList.add(teamPrivilegeDTO);
        List<UserBasic> docUsers = userFeignAPI
            .getByPrivileges(orgId, new TeamPrivilegeListDTO(teamList))
            .getData();

        Map<String, String> parentNodeNoMap = new HashMap<>();

        String dUsername;
        String cUsername;
        String aUsername;
//        Integer seqNoColumn = columnConfigMap.getSeqNoColumn();
        Project project = projectService.get(projectId);
        Long companyId = project.getCompanyId();
        Map<String, UserProfile> userMap = getUserInfos(orgId);

        /*----
            1.0 遍历 导入图纸清单的 设计人员、校验人员和审批人员的清单，通过遍历清单建立 人员名 与 人员详情 的遍历MAP
        */
        Map<String, UserProfile> users = new HashMap<>();
        System.out.println("last max row number" + sheet.getLastRowNum());
        rows = sheet.rowIterator();
        /*-------
        2.0 导入图纸内容
         */

        while (rows.hasNext()) {

            row = rows.next();
            int colIndex = 0;
            if (row.getRowNum() < BpmCode.DD_HEADER) {
                continue;
            }
            if (row.getRowNum() > sheet.getLastRowNum()) {
                break;
            }
            try {

                Drawing drawing = (Drawing) sheetConfig.getBuilder().build(sheetConfig, project, row, columnConfigs, operator);
                if (drawing == null) {
                    throw new ValidationError("key no identifier is null");
                }
                if (drawing.getDrawingType() != null && !drawing.getDrawingType().equals(drawingType)) {
                    throw new ValidationError("Current template does not match!当前模板不匹配");
                }
                //数字化图纸导入
                /*----
                2.1 检查 图纸是否已经存在
                 */
                String parentNode = drawing.getParentNo();
                if (NO_PARENT.equals(parentNode)) {
                    setRedisKey(progressKey, "" + batchResult.getProgress(), 3000);

                    if (drawing.getEngineeringStartDate() != null && drawing.getEngineeringFinishDate() != null && drawing.getPlanHours() != null){
                        //design
                        if (drawing.getPreparePersonId() != null ) {
                            handleDrawingPlanHour(projectId, drawing, drawing.getPreparePersonId(), "DESIGN_ENGINEER_EXECUTE");
                        }
                        //review
                        if (drawing.getReviewPersonId() != null ) {
                            handleDrawingPlanHour(projectId, drawing, drawing.getReviewPersonId(), "DRAWING_REVIEW_EXECUTE");
                        }
                        //approve
                        if (drawing.getApprovePersonId() != null ) {
                            handleDrawingPlanHour(projectId, drawing, drawing.getApprovePersonId(), "DRAWING_APPROVE_EXECUTE");
                        }
                    }

                    continue;
                }

                batchResult.addTotalCount(1);
                totalCount++;

                parentNodeNoMap.put("ENGINEERING_COMMON", parentNode);
                if (MapUtils.isEmpty(parentNodeNoMap)) {
                    skippedCount++;
                    batchResult.addSkippedCount(1);
                    throw new ValidationError("parent node is empty");

                }

                //update drawing_entry_delegate， prepare person
                if (!StringUtils.isEmpty(drawing.getPreparePerson()) && userMap.get(drawing.getPreparePerson()) != null) {
                    handleDrawingEntryDelegate(drawing, UserPrivilege.DESIGN_ENGINEER_EXECUTE,
                        userMap.get(drawing.getPreparePerson()).getId(), operator.getId());
                    handleDrawingTask(orgId, projectId, drawing, userMap.get(drawing.getPreparePerson()), operator.getId(), "DESIGN_ENGINEER_EXECUTE");
                    drawing.setPreparePersonId(userMap.get(drawing.getPreparePerson()).getId());
                }
                //review
                if (!StringUtils.isEmpty(drawing.getReviewPerson()) && userMap.get(drawing.getReviewPerson()) != null) {
                    handleDrawingEntryDelegate(drawing, UserPrivilege.DRAWING_REVIEW_EXECUTE,
                        userMap.get(drawing.getReviewPerson()).getId(), operator.getId());
                    handleDrawingTask(orgId, projectId, drawing, userMap.get(drawing.getReviewPerson()), operator.getId(), "DRAWING_REVIEW_EXECUTE");
                    drawing.setReviewPersonId(userMap.get(drawing.getReviewPerson()).getId());
                }
                //approve
                if (!StringUtils.isEmpty(drawing.getApprovePerson()) && userMap.get(drawing.getApprovePerson()) != null) {
                    handleDrawingEntryDelegate(drawing, UserPrivilege.DRAWING_APPROVE_EXECUTE,
                        userMap.get(drawing.getApprovePerson()).getId(), operator.getId());
                    handleDrawingTask(orgId, projectId, drawing, userMap.get(drawing.getApprovePerson()), operator.getId(), "DRAWING_APPROVE_EXECUTE");
                    drawing.setApprovePersonId(userMap.get(drawing.getApprovePerson()).getId());
                }
                //设计QC校对
                if (!StringUtils.isEmpty(drawing.getQcPerson()) && userMap.get(drawing.getQcPerson()) != null) {
                    handleDrawingEntryDelegate(drawing, UserPrivilege.DRAWING_QC_EXECUTE,
                        userMap.get(drawing.getQcPerson()).getId(), operator.getId());
                    handleDrawingTask(orgId, projectId, drawing, userMap.get(drawing.getQcPerson()), operator.getId(), "DRAWING_QC_EXECUTE");
                }
                //文档控制打印下发
                if (!StringUtils.isEmpty(drawing.getDocPerson()) && userMap.get(drawing.getDocPerson()) != null) {
                    handleDrawingEntryDelegate(drawing, UserPrivilege.DOCUMENT_CONTROL_EXECUTE,
                        userMap.get(drawing.getDocPerson()).getId(), operator.getId());
                    handleDrawingTask(orgId, projectId, drawing, userMap.get(drawing.getDocPerson()), operator.getId(), "DOCUMENT_CONTROL_EXECUTE");
                }

                drawingRepository.save(drawing);

                //同步人员


                updateHierarchyNodesAndProjectNodes(drawing,
                    parentNodeNoMap,
                    projectId,
                    orgId,
                    operator.getId(),
                    batchResult);

                //关联修改bpm_activity_instance_base中entity_no和drawing_title
                List<BpmActivityInstanceBase> tasks = bpmActInstRepository.findByProjectIdAndEntityId(projectId, drawing.getId());
                for (BpmActivityInstanceBase item : tasks) {
                    item.setEntityNo(drawing.getNo());
                    item.setDrawingTitle(drawing.getDocumentTitle());
                    bpmActInstRepository.save(item);
                }

                processedCount++;
                batchResult.addProcessedCount(1);


                setRedisKey(progressKey, "" + batchResult.getProgress(), 3000);

            } catch (Exception e) {
                e.printStackTrace(System.out);
                errorCount++;
                batchResult.addErrorCount(1);
                setImportDataErrorMessage(row, e.getMessage());
            }
            if (batchResult.getProcessedCount() % 10 == 0
                && batchTask.checkRunningStatus()
            ) {
                batchTask.setResult(batchResult);
                batchTask.setLastModifiedAt();
                batchTaskRepository.save(batchTask);
            }

        }

        return new BatchResultDTO(
            totalCount,
            processedCount,
            skippedCount,
            errorCount
        );
    }

    private void updateHierarchyNodesAndProjectNodes(
        Drawing entity,
        Map<String, String> parentNodeNoMap,
        Long projectId,
        Long orgId,
        Long operatorId,
        BatchResultDTO batchResultDTO) {

        try {
            HierarchyNode lastSiblingNode;
            int sortWeight;
            HierarchyNode node;
            int i = -1;

            for (Map.Entry<String, String> parentNodeNo : parentNodeNoMap.entrySet()) {
                i++;
                String hierarchyType = parentNodeNo.getKey();
                String parentNoStr = parentNodeNo.getValue();


                HierarchyNode parentNode = null;
                parentNode = hierarchyRepository
                    .findByNoAndProjectIdAndHierarchyTypeAndDeletedIsFalse(
                        parentNoStr,
                        projectId,
                        hierarchyType
                    );


                if (parentNode == null) {

                    throw new ValidationError("parent '" + parentNodeNo + "' doesn't exit");
                }


                lastSiblingNode = hierarchyRepository
                    .findFirstByProjectIdAndParentIdAndDeletedIsFalseOrderBySortDesc(
                        projectId,
                        parentNode.getId()
                    )
                    .orElse(null);


                sortWeight = lastSiblingNode != null
                    ? lastSiblingNode.getSort()
                    : parentNode.getSort();


                node = getEntityNode(
                    projectId,
                    parentNode.getId(),
                    entity.getNo(),
                    parentNodeNo.getKey()
                );


                if (node == null) {
                    node = new HierarchyNode(parentNode);
                    node.setEntityType(entity.getEntityType());
                    node.setNode(
                        projectNodeRepository
                            .findByProjectIdAndNoAndDeletedIsFalse(
                                projectId,
                                entity.getNo()
                            )
                            .orElse(null)
                    );
                    node.setCreatedAt();
                    node.setCreatedBy(operatorId);
                    node.setNew(true);
                }


                node.setParentNode(parentNode);
                node.setNo(entity.getNo(), parentNode.getNo());

                node.setDrawingType(entity.getDrawingType());
                node.getNode().setDrawingType(entity.getDrawingType());

                node.setEntityType(entity.getEntityType());


                node.getNode().setDisplayName(entity.getNo());
                node.setEntitySubType(entity.getEntitySubType());

                node.getNode().setEntityBusinessType(entity.getEntityBusinessType());
                node.getNode().setNo1(entity.getClientDocNo());
                node.getNode().setNo2(entity.getOwnerDocNo());
//                node.getNode().setWorkLoad(entity.getNps());
//                node.getNode().setDwgShtNo(entity.getSheetNo());


                node.setEntityId(entity.getId());
                node.setSort(sortWeight + 1);
                node.setLastModifiedAt();
                node.setLastModifiedBy(operatorId);
                node.setStatus(ACTIVE);
                node.setHierarchyType(hierarchyType);
//                node.getNode().setDisplayName(node.getNo());

                node.getNode().setDiscipline(entity.getDiscipline());
                projectNodeRepository.save(node.getNode());
                hierarchyRepository.save(node);

                hierarchyNodeRelationService.saveHierarchyPath(orgId, projectId, node);

            }


        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw e;
        }
    }


    /**
     * 取得实体节点信息。
     *
     * @param projectId     项目 ID
     * @param parentId      上级节点 ID
     * @param nodeNo        节点编号
     * @param hierarchyType
     * @return 实体节点信息
     */
    private HierarchyNode getEntityNode(
        Long projectId,
        Long parentId,
        String nodeNo,
        String hierarchyType
    ) {

        HierarchyNode node = hierarchyRepository
            .findByNoAndProjectIdAndParentIdAndDeletedIsFalse(
                nodeNo,
                projectId,
                parentId,
                hierarchyType
            )
            .orElse(null);

        if (node == null) {
            return null;
        }

        return node;
    }

    private boolean checkUserContain(Long userid, List<UserBasic> users) {
        for (UserBasic user : users) {
            if (user.getId().equals(userid)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 设置图纸人员指派
     */
    private void setDrawingDelegate(Drawing drawing,
                                    UserPrivilege userPrivilege,
                                    OperatorDTO operator,
                                    Map<String, UserProfile> users,
                                    String userName) {
        DrawingEntryDelegate drawingDelegate = drawingEntryDelegateRepository
            .findByDrawingIdAndPrivilegeAndStatus(
                drawing.getId(),
                userPrivilege,
                EntityStatus.ACTIVE
            );

        if (drawingDelegate == null) {
            drawingDelegate = new DrawingEntryDelegate();
            drawingDelegate.setCreatedAt();
            drawingDelegate.setCreatedBy(operator.getId());
            drawingDelegate.setDrawingId(drawing.getId());
            drawingDelegate.setPrivilege(userPrivilege);
            drawingDelegate.setStatus(EntityStatus.ACTIVE);
        }
        drawingDelegate.setUserId(users.get(userName).getId());
        drawingDelegate.setLastModifiedAt();
        drawingDelegate.setLastModifiedBy(operator.getId());
        drawingEntryDelegateRepository.save(drawingDelegate);
    }

    /**
     * 设置 设计人员 和 人名-人员详情的 MAP
     *
     * @param username
     * @param users
     */
    private void setDesignerMap(String username, Map<String, UserProfile> users) {
        if (username != null && !"".equals(username)) {
            if (!users.containsKey(username)) {
                UserNameCriteriaDTO userNameCriteriaDTO = new UserNameCriteriaDTO(username);
                List<UserProfile> userProfiles = userFeignAPI.getUserByUsername(userNameCriteriaDTO).getData();
                if (userProfiles != null
                    && !userProfiles.isEmpty()) {
                    UserProfile user = userProfiles.get(0);
                    users.put(username, user);
                } else {
                    users.put(username, null);
                }
            }
        }
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
    @Transactional
    @Override
    public boolean updateFileId(Long batchTaskId, Long fileId) {
        List<Drawing> list = drawingRepository.findByBatchTaskId(batchTaskId);
        if (!list.isEmpty()) {
            drawingRepository.updateFileIdIn(fileId, list);
        }
        return true;
    }


    /**
     * 001->1
     *
     * @param str
     * @return
     */
    private String replaceStartZero(String str) {
        if (str == null) {
            str = "0";
        }
        str = str.replaceAll("^(0+)", "");
        if ("".equals(str)) {
            str = "0";
        }
        return str;
    }

}
