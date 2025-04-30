package com.ose.tasks.domain.model.service.wbs;

import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.*;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityTypeRepository;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.domain.model.repository.wbs.*;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.HierarchyNodeRelationInterface;
import com.ose.tasks.domain.model.service.builder.*;
import com.ose.tasks.domain.model.service.builder.piping.ComponentEntityBuilder;
import com.ose.tasks.domain.model.service.builder.piping.WeldEntityBuilder;
import com.ose.tasks.domain.model.service.builder.structure.StructureWeldEntityBuilder;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.xlsximport.XlsxImportInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.dto.wbs.WBSEntryExecutionHistoryDTO;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.tasks.entity.wbs.entry.WBSEntryExecutionHistory;
import com.ose.tasks.entity.wbs.structureEntity.*;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.tasks.vo.bpm.CategoryRuleType;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.tasks.vo.wbs.WBSEntryExecutionState;
import com.ose.tasks.vo.wbs.WBSImportLogStatus;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.apache.commons.collections.MapUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder.SheetConfig;
import static com.ose.vo.EntityStatus.ACTIVE;

/**
 * 实体管理服务。
 */
@Component
public class WBSEntityService extends StringRedisService implements WBSEntityInterface {

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    private static Map<String, Integer> columnMap = new HashMap<>();

    // 导入文件配置工作表名
    private static final String SETTINGS_SHEET_NAME = "SETTING";

    // 项目数据仓库
    private final ProjectRepository projectRepository;

    // 项目层级结构数据仓库
    private final HierarchyRepository hierarchyRepository;

    // 项目节点数据仓库
    private final ProjectNodeRepository projectNodeRepository;

    // 实体类型设置规则数据仓库
    private final EntitySubTypeRuleRepository entityCategoryRuleRepository;

    // 项目实体子类型数据仓库
    private final BpmEntitySubTypeRepository bpmEntitySubTypeRepository;

    // 项目实体大类型数据仓库
    private final HierarchyNodeRelationInterface hierarchyNodeRelationService;

    // WBS 条目数据仓库
    private final WBSEntryRepository wbsEntryRepository;

    //weldEntityInterface
    // 工作表配置信息构建逻辑
    private final WBSEntityImportSheetConfigBuilder sheetConfigBuilder;

    private final WBSEntryExecutionHistoryRepository wbsEntryExecutionHistoryRepository;

    private Set<EntitySubTypeRule> entityCategoryRules;


    private final HierarchyInterface hierarchyService;

    private final BatchTaskRepository batchTaskRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;

    private final BpmEntityTypeRepository bpmEntityTypeRepository;

    private final XlsxImportInterface xlsxImportService;

    private static final Pattern THICKNESS = Pattern.compile(
        "(S-[\\d]{1,3}s?)|(X{1,3}S)|(STD)|(NA)|(\\d+(\\.\\d+)?('|\"|ft|in|mm|cm|m).*)|(\\d+(\\.\\d+)?(mm|cm|m)\\((\\d+)/(\\d+)('|\"|ft|in)\\)\\s*THCK)|(\\d+(\\.\\d+)?)",
        Pattern.CASE_INSENSITIVE
    );

    private Set<String> entityTypes = new HashSet<>();

    private static final String HYPHEN = "-";
    private static final String PIPE = "PIPE";
    private static final String OTHER = "OTHER";
    private static final String SEPARATOR = ";";
    private static final String OTHERS_ON_SPOOL = "OTHERS_ON_SPOOL";
    private static final String OTHERS_ON_ISO = "OTHERS_ON_ISO";
    private static final String FLANGE = "FLANGE";
    private static final String isoHierarchyTypes[] =
        new String[]{"PIPING", "TEST_PACKAGE", "CLEAN_PACKAGE", "SUB_SYSTEM"};

    private static final String DELETE = "DELETE";

    private static final String MODULE_NOS_AT_REDIS_KEY = "MODULE_NOS-%s";

    private static Set<String> columnKeys = new HashSet<String>() {{
//        add(SECTOR);
//        add(FUNCTION);
//        add(TYPE);
//        add(ENTITY_SUB_TYPE);
//        add(ENTITY_TYPE);
//        add(CHAIN);
//        add(FUNC_PART);
    }};


    /**
     * 构造方法。
     */
    @Autowired
    public WBSEntityService(
        StringRedisTemplate stringRedisTemplate,
        ProjectRepository projectRepository,
        HierarchyRepository hierarchyRepository,
        ProjectNodeRepository projectNodeRepository,
        BpmEntitySubTypeRepository bpmEntitySubTypeRepository,
        EntitySubTypeRuleRepository entityCategoryRuleRepository,
        HierarchyNodeRelationInterface hierarchyNodeRelationService, WBSEntryRepository wbsEntryRepository,
        WBSEntityImportSheetConfigBuilder sheetConfigBuilder,
        WBSEntryExecutionHistoryRepository wbsEntryExecutionHistoryRepository,
        BatchTaskRepository batchTaskRepository,
        HierarchyInterface hierarchyService,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
        BpmEntityTypeRepository bpmEntityTypeRepository,
        XlsxImportInterface xlsxImportService) {
        super(stringRedisTemplate);
        this.projectRepository = projectRepository;
        this.hierarchyRepository = hierarchyRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.bpmEntitySubTypeRepository = bpmEntitySubTypeRepository;
        this.hierarchyNodeRelationService = hierarchyNodeRelationService;
        this.entityCategoryRuleRepository = entityCategoryRuleRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.sheetConfigBuilder = sheetConfigBuilder;
        this.wbsEntryExecutionHistoryRepository = wbsEntryExecutionHistoryRepository;
        this.hierarchyService = hierarchyService;
        this.batchTaskRepository = batchTaskRepository;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
        this.bpmEntityTypeRepository = bpmEntityTypeRepository;
        this.xlsxImportService = xlsxImportService;
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


    /**
     * 根据节点编号查询实体信息。
     *
     * @param <T>        WBS 实体范型
     * @param projectId  项目 ID
     * @param nodeNo     节点编号
     * @param repository WBS 实体数据仓库
     * @return WBS 实体
     */
    private <T extends WBSEntityBase> T getWBSEntityByNodeNo(
        Long projectId,
        String nodeNo,
        WBSEntityBaseRepository<T> repository
    ) {
        return repository
            .findByProjectIdAndNoAndDeletedIsFalse(projectId, nodeNo)
            .orElse(null);
    }


    /**
     * 设置导入数据错误信息。
     *
     * @param row     错误所在行
     * @param message 错误消息
     */
    private void setImportDataErrorMessage(Row row, String message, int errColumn) {
        row.createCell(errColumn).setCellValue(message);
    }

    /**
     * 从工作表中导入 WBS 实体信息。
     *
     * @param <T>        WBS 实体范型
     * @param operator   操作者 ID
     * @param project    项目信息
     * @param config     工作表读取配置
     * @return 导入结果统计
     */
    private <T extends WBSEntityBase> BatchResultDTO importEntities(
        Project project,
        OperatorDTO operator,
        Sheet sheet,
        BatchResultDTO batchResult,
        List<ColumnImportConfig> columnConfigs,
        SheetConfig config,
        BatchTask batchTask
    ) {

        final Long projectId = project.getId();
        final Long orgId = project.getOrgId();

        final String progressKey = "BATCH_TASK:" + batchResult.getTaskId() + ":PROGRESS";
        Iterator<Row> rows = config.getSheet().rowIterator();
        Row row;
        T entity = null;
        int totalCount = 0;
        int skippedCount = 0;
        int processedCount = 0;
        int errorCount = 0;
        WBSEntityBuilder<T> builder = config.getBuilder();
        WBSEntityBaseRepository<T> repository = config.getRepository();
        Map<String, Integer> repeatMap = new HashMap<>();
        Set<String> repeatNo = new HashSet<>();
        EntitySubTypeRule rule = null; // 实体设定规则
        List<String> nodeHierarchyTypes = new ArrayList<>();//记录层级类型
        Set<Long> noneHierarchyEntityIds = new HashSet<>();//记录没有层级的实体Id
        Set<Long> addedEntityIds = new HashSet<>();//添加的 实体ID
        String subTypeRuleType = "INCLUDE_NON"; // 实体规则配置类型
        //0.2 取得一个项目全部的实体类型
        if (CollectionUtils.isEmpty(entityTypes)) {
            List<BpmEntitySubType> bpmEntityCategories = bpmEntitySubTypeRepository.
                findByProjectIdAndStatus(projectId, EntityStatus.ACTIVE);
            for (BpmEntitySubType bpmEc : bpmEntityCategories) {
                entityTypes.add(bpmEc.getNameEn());
            }
        }

        if (builder instanceof WeldEntityBuilder) {
            entityCategoryRules = getEntitySubTypeRules(
                project.getOrgId(),
                projectId,
                WBSEntityType.WELD_JOINT.name());

        } else if (builder instanceof ComponentEntityBuilder) {
            entityCategoryRules = getEntitySubTypeRules(
                project.getOrgId(),
                projectId,
                WBSEntityType.COMPONENT.name());
        } else if (builder instanceof StructureWeldEntityBuilder) {
            entityCategoryRules = getEntitySubTypeRules(
                project.getOrgId(),
                projectId,
                WBSEntityType.STRUCT_WELD_JOINT.name());
        }

        //循环取得xls中的每一行
        while (rows.hasNext()) {

            row = rows.next();
            //跳过表头
            if (row.getRowNum() < config.getContentHeaderRows()) {
                continue;
            }

            try {
                WBSImportLogDTO wbsImportLogDTO = builder.buildWbs(config, project, row, columnConfigs, operator);

                if(WBSImportLogStatus.SKIP.equals(wbsImportLogDTO.getStatus())) {
                    throw new ValidationError(wbsImportLogDTO.getErrorStr());
                } else if(WBSImportLogStatus.ERROR.equals(wbsImportLogDTO.getStatus())){
                    throw new ValidationError(wbsImportLogDTO.getErrorStr());
                }

                entity = (T) wbsImportLogDTO.getWbsEntityBase();

                entity = builder.generateQrCode(entity);

                // 如果是焊口或者组件需要check父级信息
                if (builder instanceof WeldEntityBuilder || builder instanceof ComponentEntityBuilder || builder instanceof StructureWeldEntityBuilder) {
                    rule = builder.getRule(orgId, projectId, config, row, entityCategoryRules);
                    if (rule != null) {
                        entity = builder.checkRule(orgId, projectId, entity, rule);
                    }
                }

                // 设置更新信息
                entity.setLastModifiedAt();
                entity.setLastModifiedBy(operator.getId());
                entity.setStatus(ACTIVE);


                repository.save(entity);

                totalCount++;

                // 更新节点信息 ftjftj
                updateHierarchyNodesAndProjectNodes(entity,
                    wbsImportLogDTO.getParentNodeNoMap(),
                    projectId,
                    project.getOrgId(),
                    operator.getId(),
                    batchResult);

                batchResult.addProcessedCount(1);

                // TODO 通过 MQTT 更新导入进度
                setRedisKey(progressKey, "" + batchResult.getProgress(), 3000);

            } catch (ValidationError e) {
                if (entity != null) {
                    noneHierarchyEntityIds.add(entity.getId());
                }
                errorCount++;
                batchResult.addErrorCount(1);
                setImportDataErrorMessage(row, e.getMessage(), config.getColumns());
            }

            if (batchResult.getProcessedCount() % 10 == 0
                && batchTask.checkRunningStatus()
            ) {
                batchTask.setResult(batchResult);
                batchTask.setLastModifiedAt();
                batchTaskRepository.save(batchTask);
            }

        }

        /* new codes

         */

        //更新 component的数量
//        repeatNo.forEach(componentNo -> {
//            componentEntityRepository.updateQtyByNodeNo(project.getOrgId(), projectId, repeatMap.get(componentNo), componentNo);
//        });

        //删除所有 没有挂在层级上的实体
        noneHierarchyEntityIds.forEach(noneHierarchyEntityId -> {
            ProjectNode pn = projectNodeRepository.findByProjectIdAndEntityIdAndDeleted(projectId, noneHierarchyEntityId, false).orElse(null);
            if (pn == null) {
                builder.deleteNoneHierarchyEntity(noneHierarchyEntityId);
            }
        });

        // 清除ISO多余的层级信息
        hierarchyService.deleteRedundantNodes(projectId);

//        hierarchyRepository.updateISOHierarchyInfo(projectId);

        return new BatchResultDTO(
            totalCount,
            processedCount,
            skippedCount,
            errorCount
        );

    }

    /**
     * 焊口实体：校验no的唯一性
     *
     * @param nodeNo
     * @param projectId 项目ID
     */
    private void checkNo(String nodeNo, Long projectId) {
//        if (weldEntityService.existsByEntityNo(nodeNo, projectId)) {
//        错误行，nodeNo已存在
//            throw new ValidationError("weld no ALREADY EXISTS.");
//        }
//    }
    }

    /**
     * 导入实体。
     *
     * @param batchTask     批处理任务信息
     * @param operator      操作者信息
     * @param project       项目信息
     * @param nodeImportDTO 节点导入操作数据传输对象
     * @return 批处理执行结果
     */
    @Override
    public BatchResultDTO importEntities(
        BatchTask batchTask,
        OperatorDTO operator,
        Project project,
        HierarchyNodeImportDTO nodeImportDTO
    ) {

        final Date timestamp = new Date();
        Long operatorId = operator.getId();
        Workbook workbook;
        File excel;

        batchTask.setDiscipline(nodeImportDTO.getDiscipline());
        batchTask.setRemarks(nodeImportDTO.getRemarks());

        batchTaskRepository.save(batchTask);

        Long projectId = project.getId();
        Long orgId = project.getOrgId();

        // 读取已上传的导入文件
        try {
            excel = new File(temporaryDir, nodeImportDTO.getFilename());
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            throw new NotFoundError();
        }

        // 读取实体导入工作表配置
        List<SheetConfig> configs = null;
        try {
            configs = sheetConfigBuilder
                .readConfigs(workbook.getSheet(SETTINGS_SHEET_NAME));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        BatchResultDTO batchResult = new BatchResultDTO(batchTask);

        // 导入数据统计
        for (SheetConfig config : configs) {
            batchResult.addTotalCount(
                config.getSheet().getPhysicalNumberOfRows()
                    - config.getContentHeaderRows()
            );
        }

        if (batchResult.getTotalCount() == 0) {
            return batchResult;
        }

        BatchResultDTO sheetImportResult;
//        Map<String, String> spoolIsoMap = new HashMap<>();

        // 导入工作表中的数据
        boolean checkedKeyColumn = false;
        for (SheetConfig config : configs) {

            //读取对应实体表的 列配置信息
            Sheet columnSetting = workbook.getSheet(config.getColumnSettingSheetName());

            // 取得所有自定义属性信息
            List<ColumnImportConfig> columnImportConfigs
                = xlsxImportService.getColumnDefinitions(columnSetting, config.getDb(),
                config.getTable(), orgId, projectId, config.getEntityTypeClass().getName(), config.getHeaderRows());

            int rowCount = columnSetting.getPhysicalNumberOfRows();

            if (!checkedKeyColumn) {
                for (int rowIndex = config.getHeaderRows(); rowIndex < rowCount; rowIndex++) {

                    Row row = columnSetting.getRow(rowIndex);
                    if (row.getCell(5) == null) continue;
                    String idxKey = StringUtils.trim(row.getCell(5).getStringCellValue());
                    if (StringUtils.isEmpty(idxKey)) continue;
                    Integer idxVal = row.getCell(1).getCellTypeEnum() == CellType.STRING ? Integer.parseInt(row.getCell(1).getStringCellValue()) : (int) row.getCell(1).getNumericCellValue();
                    if (!StringUtils.isEmpty(idxKey)) {
                        columnMap.put(idxKey.toUpperCase(), idxVal);
                    }
                }
                if (!columnMap.keySet().containsAll(columnKeys) && !CollectionUtils.isEmpty(columnKeys)) {
                    throw new BusinessError("NO Proper setting");
                }
                checkedKeyColumn = true;
            }

            // 管件实体处理比较特殊
            // 管件以外的实体（管线，单管，管段，焊口，试压包，清洁包，子系统）
//            sheetImportResult = importEntities(
//                operatorId,
//                project,
//                batchResult,
//                config,
//                batchTask
//            );

            sheetImportResult = importEntities(
                project,
                operator,
                config.getSheet(),
                batchResult,
                columnImportConfigs,
                config,
                batchTask
            );

            batchResult.addLog(
                sheetImportResult.getProcessedCount()
                    + " "
                    + ("" + config.getEntityType()).toLowerCase()
                    + " entities imported."
            );

        }

        // 保存工作簿
        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace(System.out); // TODO
        }

        // 更新项目最后更新信息
        project.setLastModifiedAt(timestamp);
        project.setLastModifiedBy(operatorId);
        projectRepository.save(project);

        return batchResult;
    }

    /**
     * 取得未挂载到层级结构的实体。
     *
     * @param projectId  项目 ID
     * @param entityType 实体类型
     * @param pageable   分页参数
     * @return 实体节点分页数据
     */
    @Override
    public Page<ProjectNode> unmountedEntities(
        Long projectId,
        String entityType,
        Pageable pageable
    ) {

        if (entityType != null) {
            return projectNodeRepository.findUnmountedEntitiesWithEntityType(
                projectId,
                entityType,
                pageable
            );
        }

        return projectNodeRepository.findUnmountedEntities(
            projectId,
            pageable
        );
    }


    private <T extends WBSEntityBase> void updateHierarchyNodesAndProjectNodes(
        T entity,
        Map<String, String> parentNodeNoMap,
        Long projectId,
        Long orgId,
        Long operatorId,
        BatchResultDTO batchResultDTO) {

            /*
                为了后边的计划生成，LAYER_PACKAGE维度的数据是必须要生成的
                0. 如果 实体类型是 ISO，考虑下边 生成情况 ok
                1. 检查 所有的 parentNodeNoList 中是否有 LAYER_PACKAGE类型的父级 ok
                2. 对于 LAYER_PACKAGE 类型父级，更新此父级 ok
                3. 如果父级集合中没有 LAYER_PACKAGE 类型，先查找 对应此project_node 的LAYER_PACKAGE 父级
                4. 如果有，则不处理
                5. 如果没有，则增加一个和ISO类型 数据 一样（类型为LAYER_PACKAGE）的父级。(提前保存好 AREA的path)

             */
        try {
            HierarchyNode lastSiblingNode;
            int sortWeight;
            HierarchyNode node;
            int i = -1;
            // 更新节点信息
            for (Map.Entry<String, String> parentNodeNo : parentNodeNoMap.entrySet()) {
                i++;
                String hierarchyType = parentNodeNo.getKey();
                String parentNoStr = parentNodeNo.getValue();
//                    parentNoStr = parentNoStr + HYPHEN + "PIPING";
//                }
                // 取得上级节点信息,
                HierarchyNode parentNode = null;
                System.out.println(hierarchyType +  parentNoStr);
                parentNode = hierarchyRepository
                    .findByNoAndProjectIdAndHierarchyTypeAndDeletedIsFalse(
                        parentNoStr,
                        projectId,
                        hierarchyType
                    );

                // 上级节点必须已创建
                if (parentNode == null) {
                    throw new ValidationError("parent '" + parentNodeNo + "' doesn't exit"); // TODO
                }

                HierarchyNode oldHn = hierarchyRepository.findByProjectIdAndEntityIdAndHierarchyTypeAndDeletedIsFalse(projectId, entity.getId(), hierarchyType);
                if(oldHn != null && oldHn.getParentId() != null && !oldHn.getParentId().equals(parentNode.getId())) {

                    hierarchyNodeRelationRepository.deleteByHierarchyId(projectId, oldHn.getId());
                    hierarchyRepository.deleteHierarchyById(oldHn.getId());
                }

                // 上级节点若为实体则检查上级节点是否接受该实体类型的子节点
                BpmEntityType pent = bpmEntityTypeRepository.findByProjectIdAndNameEnAndStatus(projectId, parentNode.getEntityType(), ACTIVE);
                if (!pent
                    .isParentOf(entity.getEntityType())
                )  {
                    // TODO
                    throw new ValidationError(
                        "parent '"
                            + parentNode.getNo()
                            + "' cannot be parent of '"
                            + entity.getNo()
                            + "'"
                    );
                }


                // 取得同上级节点下最后一个节点
                lastSiblingNode = hierarchyRepository
                    .findFirstByProjectIdAndParentIdAndDeletedIsFalseOrderBySortDesc(
                        projectId,
                        parentNode.getId()
                    )
                    .orElse(null);

                // 取得排序位置
                sortWeight = lastSiblingNode != null
                    ? lastSiblingNode.getSort()
                    : parentNode.getSort();

                // 重新排序，增长过快
//                hierarchyRepository.reorder(projectId, sortWeight, 1);

                // 尝试取得实体节点信息
                node = getEntityNode(
                    projectId,
                    parentNode.getId(),
                    entity.getNo(),
                    parentNodeNo.getKey()
                );

                // 尚未创建节点时生成新的节点信息
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

                // 更新节点信息
                node.setParentNode(parentNode);
                node.setNo(entity.getNo(), parentNode.getNo());

                node.setEntityType(entity.getEntityType());
                // 将当前实体大类是否为层级赋给project_node表
                BpmEntityType currentEntityType = bpmEntityTypeRepository.findByProjectIdAndNameEnAndStatus(projectId, entity.getEntityType(), ACTIVE);
                node.getNode().setFixedLevel(currentEntityType.getFixedLevel());
                // 焊口和管件有子类型，需要特殊处理
                if (entity instanceof WeldEntity) {
                    // 焊口名称是ISO号
                    node.getNode().setDisplayName(entity.getDisplayName());
                    node.setEntitySubType(((WeldEntity) entity).getEntitySubType());
                    // 实体业务类型
                    node.getNode().setEntityBusinessType(entity.getEntityBusinessType());
                    node.getNode().setWorkLoad(((WeldEntity) entity).getNps());
                    node.getNode().setDwgShtNo(((WeldEntity) entity).getSheetNo());
                    node.getNode().setDiscipline("PIPING");
                } else if (entity instanceof ComponentEntity) {
                    node.getNode().setDisplayName("");
                    node.setEntitySubType(((ComponentEntity) entity).getEntitySubType());
                    node.getNode().setWorkLoad(((ComponentEntity) entity).getNps());
                    node.getNode().setDwgShtNo(((ComponentEntity) entity).getSheetNo());
                    // 实体业务类型
                    node.getNode().setEntityBusinessType(entity.getEntityBusinessType());
                    node.getNode().setDiscipline("PIPING");
                } else if (entity instanceof PipePieceEntity) {
                    node.getNode().setDisplayName(entity.getDisplayName());
                    node.setEntitySubType(entity.getEntityType());
                    // 实体业务类型
                    node.getNode().setEntityBusinessType(entity.getEntityType());
                    // 查看SPOOL/ISO表里是否存在
//                    SpoolEntity se = spoolEntityRepository.findByNoAndProjectIdAndDeletedIsFalse(parentNodeNo.getValue(), projectId).orElse(null);
//                    if (se != null) node.getNode().setDwgShtNo(se.getSheetNo());
                    node.getNode().setWorkLoad(((PipePieceEntity) entity).getNps());
                    node.getNode().setDiscipline("PIPING");
                } else if (entity instanceof CleanPackageEntityBase) {
                    // 试压包，清洁包，子系统不设置实体子类型和实体业务类型
                    // 实体子类型
                    node.setEntitySubType(((CleanPackageEntityBase)entity).getEntitySubType());
                    // 实体业务类型
                    node.getNode().setEntityBusinessType(null);
                } else if (entity instanceof SubSystemEntityBase) {
                    // 试压包，清洁包，子系统不设置实体子类型和实体业务类型
                    // 实体子类型
                    node.setEntitySubType(((SubSystemEntityBase)entity).getEntitySubType());
                    node.getNode().setDisplayName(((SubSystemEntityBase)entity).getDescription());
                    // 实体业务类型
                    node.getNode().setEntityBusinessType(null);
                } else if (entity instanceof SpoolEntity) {
                    node.setEntitySubType(entity.getEntityType());
                    // 实体业务类型
                    node.getNode().setEntityBusinessType(entity.getEntityType());
                    node.getNode().setWorkLoad(((SpoolEntity) entity).getNps());
                    node.getNode().setDwgShtNo(((SpoolEntity) entity).getSheetNo());
                    node.getNode().setDiscipline("PIPING");
                } else if (entity instanceof ISOEntityBase) {
                    node.setEntitySubType(entity.getEntitySubType());
                    // 实体业务类型
                    node.getNode().setEntityBusinessType(entity.getEntityType());
                    node.getNode().setWorkLoad(((ISOEntity) entity).getNps());
                    node.getNode().setDwgShtNo(0);
                    node.getNode().setDiscipline("PIPING");
                } else if (entity instanceof Wp01EntityBase) {
                    node.setEntitySubType(entity.getEntityType());
                    node.getNode().setEntityBusinessType(entity.getEntityType());
                    node.getNode().setDiscipline("STRUCTURE");
                } else if (entity instanceof Wp02EntityBase) {
                    node.setEntitySubType(entity.getEntityType());
                    node.getNode().setEntityBusinessType(entity.getEntityType());
                    node.getNode().setDiscipline("STRUCTURE");
                } else if (entity instanceof Wp03EntityBase) {
                    node.setEntitySubType(entity.getEntityType());
                    node.getNode().setEntityBusinessType(entity.getEntityType());
                    node.getNode().setDiscipline("STRUCTURE");
                } else if (entity instanceof Wp04EntityBase) {
                    node.setEntitySubType(entity.getEntityType());
                    node.getNode().setEntityBusinessType(entity.getEntityType());
                    node.getNode().setDiscipline("STRUCTURE");
                } else if (entity instanceof Wp05EntityBase) {
                    node.setEntitySubType(entity.getEntityType());
                    node.getNode().setEntityBusinessType(entity.getEntityType());
                    node.getNode().setDiscipline("STRUCTURE");
                } else if (entity instanceof StructureWeldEntityBase) {
                    node.setEntitySubType(entity.getEntityType());
                    node.getNode().setEntityBusinessType(entity.getEntityType());
                    node.getNode().setDiscipline("STRUCTURE");
                } else {
                        node.setEntitySubType(entity.getEntityType());
                        // 实体业务类型
                        node.getNode().setEntityBusinessType(entity.getEntityType());
                        node.getNode().setWorkLoad(((ISOEntity) entity).getNps());
                        node.getNode().setDiscipline("GENERAL");
                }

                node.setEntityId(entity.getId());
                node.setSort(sortWeight + 1);
                node.setLastModifiedAt();
                node.setLastModifiedBy(operatorId);
                node.setStatus(ACTIVE);
                node.setEntityId(entity.getId());

                node.setHierarchyType(hierarchyType);
//                if(node.getHierarchyType().equals("SUB_SYSTEM")) {
//                    node.getNode().setDiscipline("GENERAL");
//                } else {
//                    node.getNode().setDiscipline("PIPING");
//                }
                projectNodeRepository.save(node.getNode());
                hset(RedisKey.PROJECT_USER_KEY.getDisplayName(), node.getNo(), node.getNode().getId().toString());

                hierarchyRepository.save(node);

                hierarchyNodeRelationService.saveHierarchyPath(orgId, projectId, node);
//                saveNode(node);
//                projectNodeRepository.save(node.getNode());
//                hierarchyRepository.save(node);
                if ("WP01".equals(node.getNode().getEntityType())
                    || "SYSTEM".equals(node.getNode().getEntityType())) {

                    String redisKey = String.format(MODULE_NOS_AT_REDIS_KEY, projectId.toString());
                    Set<String> moduleNames = projectNodeRepository.findModuleNames(orgId, projectId);//To Redis
                    setRedisKey(redisKey, StringUtils.toJSON(moduleNames));
                }

                //如果是 ISO节点，并且不是 PIPING维度，则增加这个ISO节点下的 SPOOL/PP/WELD/COMPONENT等的 HIERARCHY记录，根据 PIPING维度树 复制 修改
                if (node.getNode().getEntityType() == "ISO" &&
                    node.getHierarchyType() != "PIPING" &&
                    node.getHierarchyType() != "SUB_SYSTEM") {
                    addDimensionHierarchyRecord(orgId, projectId, node, operatorId);
                }
//                else if (node.getNode().getEntityType() == "SUB_SYSTEM" && node.getHierarchyType() == "SUB_SYSTEM") {
//                    //如果是 子系统节点，需要在此子系统下添加 专业列表
//                    addSubSystemDiscipline(orgId, projectId, node, operatorId);
//                }

            }


        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw e;
        }
    }


    //如果是 子系统节点，需要在此子系统下添加 专业列表
//    @Transactional
    public void addSubSystemDiscipline(Long orgId, Long projectId, HierarchyNode node, Long operatorId) {
        System.out.println("add system");
        String hierarchyType = node.getHierarchyType();
//        Long hierarchyId = node.getId();
        Long companyId = node.getCompanyId();
        Arrays.asList().forEach(disciplineCode -> {
//                String.getSystemDisciplines().forEach(disciplineCode -> {
            ProjectNode projectNode = new ProjectNode();
            projectNode.setModuleType("SYSTEM");
            String subSystemDiscipline = node.getNode().getNo() + HYPHEN + disciplineCode;
            HierarchyNode disciplineNode = hierarchyRepository.findByNoAndProjectIdAndHierarchyTypeAndDeletedIsFalse(
                subSystemDiscipline, projectId, hierarchyType
            );
            if (disciplineNode != null) {
                return;
            }
            projectNode.setNo(subSystemDiscipline);//(disciplineCode.name(),node.getNode().getNo());
            projectNode.setProjectId(projectId);
            projectNode.setOrgId(orgId);
            projectNode.setCompanyId(companyId);
            projectNode.setDiscipline(disciplineCode.toString());
            projectNode.setDisplayName(disciplineCode.toString());
            projectNode.setStatus(EntityStatus.ACTIVE);
            projectNode.setDeleted(false);
            projectNode.setCreatedAt();
            projectNode.setCreatedBy(operatorId);
            disciplineNode = new HierarchyNode(node, projectNode);
            disciplineNode.setCreatedBy(operatorId);
            disciplineNode.setLastModifiedBy(operatorId);
            projectNodeRepository.save(projectNode);
            hset(RedisKey.PROJECT_USER_KEY.getDisplayName(), projectNode.getNo(), projectNode.getId().toString());

            hierarchyRepository.save(disciplineNode);
            hierarchyNodeRelationService.saveHierarchyPath(orgId, projectId, disciplineNode);
        });


    }

    //如果是 ISO节点，并且不是 PIPING维度，则增加这个ISO节点下的 SPOOL/PP/WELD/COMPONENT等的 HIERARCHY记录，根据 PIPING维度树 复制 修改
//    @Transactional
    public void addDimensionHierarchyRecord(Long orgId, Long projectId, HierarchyNode currentDimensionNode, Long operatorId) {
        System.out.println("add dimension");
        String hierarchyType = currentDimensionNode.getHierarchyType();
        Long isoProjectNodeId = currentDimensionNode.getNode().getId();
        HierarchyNode isoHierarchyNode = hierarchyRepository.
            findByProjectIdAndNodeIdAndHierarchyTypeAndDeletedIsFalse(projectId, isoProjectNodeId, "PIPING");
        if (isoHierarchyNode == null) {
            return;
        }
        List<HierarchyNode> isoHierarchyNodes = hierarchyRepository.
            findChildrenByRootIdAndHierarchyType(projectId, isoHierarchyNode.getPath() + isoHierarchyNode.getId() + "/%", "PIPING");

        for (HierarchyNode childNode : isoHierarchyNodes) {
            HierarchyNode node = hierarchyRepository.
                findByProjectIdAndNodeIdAndHierarchyTypeAndDeletedIsFalse(projectId, childNode.getNode().getId(), hierarchyType);
            if (node != null) {
                continue;
            }
            childNode.getNode().setStatus(EntityStatus.ACTIVE);
            childNode.setStatus(EntityStatus.ACTIVE);
            node = new HierarchyNode(currentDimensionNode, childNode.getNode());
            node.setCreatedBy(operatorId);
            node.setLastModifiedBy(operatorId);
            hierarchyRepository.save(node);
            hierarchyNodeRelationService.saveHierarchyPath(orgId, projectId, node);
        }


    }

    /**
     * 取得实体类型设置规则。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param entityType 实体大类型
     * @return 实体类型设置规则列表
     */
    private Set<EntitySubTypeRule> getEntitySubTypeRules(Long orgId,
                                                           Long projectId,
                                                           String entityType) {
        return entityCategoryRuleRepository.findByEntityTypeAndOrgIdAndProjectIdAndDeletedIsFalseOrderByRuleOrder(
            entityType,
            orgId,
            projectId);
    }

    /**
     * 设置焊口实体类型。
     *
     * @param weldEntity 焊口实体
     * @param rules      焊口实体设定规则
     * @return EntitySubTypeRule 实体类型设置规则
     */
    private EntitySubTypeRule getWeldEntityTypeRuleBySetting(
        WeldEntity weldEntity,
        Set<EntitySubTypeRule> rules) {

        // 焊口
        for (EntitySubTypeRule rule : rules) {
            if (!StringUtils.isEmpty(rule.getValue1()) && !StringUtils.isEmpty(rule.getValue2())) {
                if (weldEntity.getWeldType().trim().equalsIgnoreCase(rule.getValue2().trim())
                    && weldEntity.getShopField().trim().equalsIgnoreCase(rule.getValue1().trim())) {
                    return rule;
                }
            }

        }
        return null;
    }


    /**
     * 设置管件实体类型。
     *
     * @param shortCode 管件shortCode
     * @param rules     管件实体类型设置规则
     * @return EntitySubTypeRule 实体类型设置规则
     */
    @Override
    public EntitySubTypeRule getComponentEntityTypeRuleBySetting(
        String shortCode,
        Set<EntitySubTypeRule> rules) {

        // 管件
        for (EntitySubTypeRule rule : rules) {
            String value1 = rule.getValue1();
            String value2 = rule.getValue2();
            if (!StringUtils.isEmpty(value1) || !StringUtils.isEmpty(value2)) {
                boolean containValue1 = true;
                boolean notContainValue2 = true;
                if (!StringUtils.isEmpty(value1)) {
                    String[] keywords = value1.split(SEPARATOR);
                    for (String keyword : keywords) {
                        if (shortCode.toUpperCase().contains(keyword.toUpperCase())) {
                            containValue1 &= true;
                        } else {
                            containValue1 &= false;
                        }
                    }
                    if (!StringUtils.isEmpty(value2)) {
                        if (shortCode.toUpperCase().contains(value2.toUpperCase())) {
                            notContainValue2 = false;
                        }
                    }
                    if (containValue1 && notContainValue2) {
                        return rule;
                    }

                } else if (!StringUtils.isEmpty(rule.getValue2())) {
                    if (!shortCode.toUpperCase().contains(value2.toUpperCase())) {
                        return rule;
                    }
                }
            }

        }
        return null;
    }

    /**
     * 根据实体ID判断是否已经启动工作流
     *
     * @param entityId  实体ID
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return 实体存在已启动的工作流，返回true，不存在返回false
     */
    @Override
    public boolean isInWorkFlow(Long entityId,
                                Long projectId,
                                Long orgId) {

        return (0 != wbsEntryRepository.existsByProjectIdAndEntityIdAndOrgIdAndDeletedIsFalse(entityId, projectId));
    }

    /**
     * 将增加的实体插入到 待更新计划表中 wbs_entry_execution_history
     *
     * @param operatorId
     * @param projectId  项目ID
     * @param entityId
     * @return
     */
    @Override
    public WBSEntryExecutionHistory insertExecutionHistory(
        Long operatorId,
        Long projectId,
        Long entityId
    ) {
        List<WBSEntryExecutionHistoryDTO> wbsEehs = projectNodeRepository.getWbsEntryExecutionHistory(operatorId, projectId, entityId);
        if (wbsEehs == null || wbsEehs.size() == 0 || wbsEehs.size() > 1) {
            return null;
        }

        WBSEntryExecutionHistoryDTO wbsEntryExecutionHistoryDTO = wbsEehs.get(0);

        WBSEntryExecutionHistory weeh = new WBSEntryExecutionHistory();

        BeanUtils.copyProperties(wbsEntryExecutionHistoryDTO, weeh);

        weeh.setStatus(EntityStatus.ACTIVE);

        WBSEntryExecutionHistory weehT = wbsEntryExecutionHistoryRepository
            .findByProjectIdAndEntityIdAndExecutionState(projectId, entityId, WBSEntryExecutionState.UNDO);

        if (weehT == null) {
            wbsEntryExecutionHistoryRepository.save(weeh);
            return weeh;

        } else {
            return null;
        }

    }

    private <T extends WBSEntityBase> boolean haveChildren(
        Long projectId,
        T entity
    ) {
        List<HierarchyNode> hierarchyNodes = hierarchyRepository.searchChild(
            projectId,
            entity.getId()
        );
        if (hierarchyNodes.size() > 0) {
            return true;
        }
        return false;
    }
}
