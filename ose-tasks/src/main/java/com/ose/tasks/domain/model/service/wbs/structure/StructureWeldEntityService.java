package com.ose.tasks.domain.model.service.wbs.structure;

import com.alibaba.excel.EasyExcel;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.HierarchyNodeRelationRepository;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.SubDrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.plan.WBSEntryPlainRelationRepository;
import com.ose.tasks.domain.model.repository.qc.StructureConstructionLogRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.StructureWeldEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.StructureWeldHierarchyInfoEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp05EntityRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryBlobRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRelationRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.BizCodeInterface;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.WpsImportDTO;
import com.ose.tasks.dto.WpsImportResultDTO;
import com.ose.tasks.dto.structureWbs.StructureWeldEntryCriteriaDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
import com.ose.tasks.entity.qc.StructureTestLog;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldEntity;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldEntityBase;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldHierarchyInfoEntity;
import com.ose.tasks.entity.wbs.structureEntity.Wp05Entity;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import com.ose.util.*;
import com.ose.vo.DisciplineCode;
import com.ose.vo.EntityStatus;
import com.ose.vo.unit.LengthUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

import static com.ose.tasks.vo.wbs.WBSEntityType.STRUCT_WELD_JOINT;
import static com.ose.util.StringUtils.trim;
import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;

@Component
public class StructureWeldEntityService implements StructureWeldEntityInterface {


    private final StructureWeldEntityRepository structureWeldEntityRepository;

    private final HierarchyNodeRelationRepository hierarchyNodeRelationRepository;


    private final StructureWeldHierarchyInfoEntityRepository structureWeldHierarchyInfoEntityRepository;


    private ProjectNodeRepository projectNodeRepository;


//    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;


    private final WBSEntryRepository wbsEntryRepository;


//    private final StructureEntityQrCodeRepository structureEntityQrCodeRepository;


    private final WBSEntryRelationRepository wbsEntryRelationRepository;


    private final WBSEntryBlobRepository wbsEntryBlobRepository;


    private final WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository;


    private final BpmRuTaskRepository bpmRuTaskRepository;


    private final BpmActivityInstanceRepository bpmActivityInstanceRepository;


    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    private final WBSEntryStateRepository wbsEntryStateRepository;


    private final SubDrawingHistoryRepository subDrawingHistoryRepository;


    private HierarchyInterface hierarchyService;


    private final PlanInterface planService;

    private final BpmEntityTypeRepository bpmEntityTypeRepository;

    private final ProjectRepository projectRepository;

//    private final WpsSimpleRepository wpsSimpleRepository;

//    private final WeldWelderRelationRepository weldWelderRelationRepository;

    private final Wp05EntityRepository wp05EntityRepository;

    private final QCReportRepository qcReportRepository;

    @Value("${application.files.temporary}")
    private String temporaryDir;


    private static final int DATA_START_ROW = 2;


    private static final int TEMPLATE_ROW_COUNT = 20;

    private static final String YES = "Y";

    private static final String NO = "N";

    private final static Logger logger = LoggerFactory.getLogger(StructureWeldEntityService.class);

    private final BizCodeInterface bizCodeService;

    private final StructureConstructionLogRepository structureConstructionLogRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public StructureWeldEntityService(
        StructureWeldEntityRepository structureWeldEntityRepository,
        StructureWeldHierarchyInfoEntityRepository structureWeldHierarchyInfoEntityRepository,
        ProjectNodeRepository projectNodeRepository,
        SubDrawingHistoryRepository subDrawingHistoryRepository,
        HierarchyInterface hierarchyService,
        PlanInterface planService,
        WBSEntryBlobRepository wbsEntryBlobRepository,
        WBSEntryStateRepository wbsEntryStateRepository,
        WBSEntryPlainRelationRepository wbsEntryPlainRelationRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        BpmActivityInstanceRepository bpmActivityInstanceRepository,
        WBSEntryRelationRepository wbsEntryRelationRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        BpmEntityTypeRepository bpmEntityTypeRepository,
        ProjectRepository projectRepository,
        BizCodeInterface bizCodeService,
//        StructureEntityQrCodeRepository structureEntityQrCodeRepository,
//        BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
        WBSEntryRepository wbsEntryRepository,
        HierarchyNodeRelationRepository hierarchyNodeRelationRepository,
//        WpsSimpleRepository wpsSimpleRepository,
//        WeldWelderRelationRepository weldWelderRelationRepository,
        Wp05EntityRepository wp05EntityRepository,
        QCReportRepository qcReportRepository,
        StructureConstructionLogRepository structureConstructionLogRepository) {
        this.structureWeldEntityRepository = structureWeldEntityRepository;
        this.structureWeldHierarchyInfoEntityRepository = structureWeldHierarchyInfoEntityRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.subDrawingHistoryRepository = subDrawingHistoryRepository;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
        this.wbsEntryBlobRepository = wbsEntryBlobRepository;
        this.wbsEntryRelationRepository = wbsEntryRelationRepository;
        this.wbsEntryPlainRelationRepository = wbsEntryPlainRelationRepository;
        this.bpmActivityInstanceRepository = bpmActivityInstanceRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.bpmEntityTypeRepository = bpmEntityTypeRepository;
        this.projectRepository = projectRepository;
//        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.wbsEntryRepository = wbsEntryRepository;
//        this.structureEntityQrCodeRepository = structureEntityQrCodeRepository;
        this.bizCodeService = bizCodeService;
        this.hierarchyNodeRelationRepository = hierarchyNodeRelationRepository;
//        this.wpsSimpleRepository = wpsSimpleRepository;
//        this.weldWelderRelationRepository = weldWelderRelationRepository;
        this.wp05EntityRepository = wp05EntityRepository;
        this.qcReportRepository = qcReportRepository;
        this.structureConstructionLogRepository = structureConstructionLogRepository;
    }

    /**
     * 查询 WBS 实体。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param pageDTO   分页参数
     * @return WBS 实体分页数据
     */
    @Override
    public Page<? extends StructureWeldEntityBase> search(
        Long orgId,
        Long projectId,
        StructureWeldEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        if (criteriaDTO.getDiscipline() != null && criteriaDTO.getDiscipline().equals(DisciplineCode.STRUCTURE)) {
            if (criteriaDTO.getAncestorHierarchyIds() != null && criteriaDTO.getAncestorHierarchyIds().size() > 0) {
                List<Long> isoIds = hierarchyNodeRelationRepository.findStructWeldIdAndHierarchyAncestorIds(
                    orgId,
                    projectId,
                    "STRUCT_WELD_JOINT",
                    criteriaDTO.getAncestorHierarchyIds()
                );
                if (isoIds.size() > 0) {
                    criteriaDTO.setEntityIds(isoIds);
                    return structureWeldHierarchyInfoEntityRepository.search(orgId, projectId, criteriaDTO, pageDTO, StructureWeldHierarchyInfoEntity.class);
                } else {
                    return null;
                }
            } else {
                return structureWeldHierarchyInfoEntityRepository.search(orgId, projectId, criteriaDTO, pageDTO, StructureWeldHierarchyInfoEntity.class);
            }
        } else {
            return structureWeldHierarchyInfoEntityRepository.search(orgId, projectId, criteriaDTO, pageDTO, StructureWeldHierarchyInfoEntity.class);
        }


    }


    /**
     * 取得 WBS 实体详细信息。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entityId  WBS 实体 ID
     * @return WBS 实体详细信息
     */
    @Override
    public StructureWeldEntityBase get(Long orgId, Long projectId, Long entityId) {

        StructureWeldEntityBase entity = structureWeldEntityRepository
            .findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(
                entityId,
                orgId,
                projectId
            )
            .orElse(null);

        if (entity == null) {
            throw new NotFoundError();
        }

        return entity;
    }

    /**
     * 删除 WBS 实体。
     *
     * @param operator 操作者信息
     * @param orgId    组织 ID
     * @param project  项目
     * @param entityId WBS 实体 ID
     */
    @Override

    public void delete(
        OperatorDTO operator,
        Long orgId,
        Project project,
        Long entityId
    ) {

        StructureWeldEntityBase entity = get(orgId, project.getId(), entityId);

        StructureWeldEntity structureWeldEntity = BeanUtils.copyProperties(entity, new StructureWeldEntity());

        List<BpmActivityInstanceBase> baisList = bpmActivityInstanceRepository.findByProjectIdAndEntityId(project.getId(), entity.getId());
        if (baisList.size() > 0) {
            for (BpmActivityInstanceBase bai : baisList) {
                BpmActivityInstanceState bais = bpmActivityInstanceStateRepository.findByBaiId(bai.getId());
                if (bais != null) {
                    bpmActivityInstanceStateRepository.delete(bais);
                }
                List<BpmRuTask> brt = bpmRuTaskRepository.findByActInstId(bai.getId());
                if (brt.size() > 0) {
                    bpmRuTaskRepository.deleteAll(brt);
                }
                bpmActivityInstanceRepository.delete(bai);

            }
        }

        List<WBSEntry> wbsEntryList = wbsEntryRepository.findByProjectIdAndEntityIdAndDeletedIsFalse(project.getId(), entity.getId());
        if (wbsEntryList.size() > 0) {
            for (WBSEntry wbsEntry : wbsEntryList) {
                WBSEntryState wbsEntryState = wbsEntryStateRepository.findByWbsEntryId(wbsEntry.getId());
                if (wbsEntryState != null) {
                    wbsEntryStateRepository.delete(wbsEntryState);
                }
                WBSEntryBlob wbsEntryBlob = wbsEntryBlobRepository.findByWbsEntryId(wbsEntry.getId());
                if (wbsEntryBlob != null) {
                    wbsEntryBlobRepository.delete(wbsEntryBlob);
                }
                List<WBSEntryPlainRelation> wbsEntryPlainRelations = wbsEntryPlainRelationRepository.findByWbsEntryId(wbsEntry.getId());
                if (wbsEntryPlainRelations.size() > 0) {
                    wbsEntryPlainRelationRepository.deleteAll(wbsEntryPlainRelations);
                }
                List<WBSEntryRelation> wbsEntryRelation1 = wbsEntryRelationRepository.findByProjectIdAndPredecessorId(project.getId(), wbsEntry.getGuid());
                List<WBSEntryRelation> wbsEntryRelation2 = wbsEntryRelationRepository.findByProjectIdAndSuccessorId(project.getId(), wbsEntry.getGuid());
                if (wbsEntryRelation1.size() > 0) {
                    wbsEntryRelationRepository.deleteAll(wbsEntryRelation1);
                }
                if (wbsEntryRelation2.size() > 0) {
                    wbsEntryRelationRepository.deleteAll(wbsEntryRelation2);
                }
                wbsEntryRepository.delete(wbsEntry);
            }
        }

        // 删除报告中的entityNo
        List<QCReport> qcReportList = qcReportRepository.findByProjectIdAndEntityNosLike(project.getId(), "%" + structureWeldEntity.getNo() + "%");
        for (QCReport qcReport : qcReportList) {
            String[] split = qcReport.getEntityNos().split("\"" + structureWeldEntity.getNo() + "\"");
            String entityNos = "";
            for (int i = 0; i < split.length; i++) {
                entityNos += split[i];
            }
            entityNos = entityNos.replace(",,",",");
            qcReport.setEntityNos(entityNos);
        }
        qcReportRepository.saveAll(qcReportList);

        // 删除日志
        List<StructureTestLog> structureTestLogList = structureConstructionLogRepository.findByEntityIdAndDeletedIsFalse(entity.getId());
        if (structureTestLogList.size() > 0) {
            for (StructureTestLog structureTestLog : structureTestLogList) {
                structureTestLog.setStatus(DELETED);
                structureTestLog.setDeleted(true);
                structureTestLog.setDeletedAt(new Date());
                structureTestLog.setDeletedBy(operator.getId());
                structureTestLog.setLastModifiedAt(new Date());
                structureTestLog.setLastModifiedBy(operator.getId());
                structureConstructionLogRepository.save(structureTestLog);
            }
        }

//        Boolean isDeletable = projectNodeRepository.existsByProjectIdAndEntityIdAndIsDeletableIsTrue(project.getId(), entity.getId());
//        if(isDeletable == null || isDeletable)  {
        structureWeldEntity.setDeletedBy(operator.getId());
        structureWeldEntity.setDeletedAt();
        structureWeldEntity.setStatus(DELETED);
        structureWeldEntity.setDeleted(true);

        hierarchyService.delete(operator, project, orgId, entityId);
//        } else {
//
//            structureWeldEntity.setCancelled(true);
//            structureWeldEntity.setStatus(EntityStatus.CANCEL);
//            structureWeldEntity.setLastModifiedBy(operator.getId());
//            structureWeldEntity.setLastModifiedAt();
//
//            hierarchyService.cancel(operator, project, orgId, entityId);
//        }
        structureWeldEntityRepository.save(structureWeldEntity);

//        if(isDeletable == null) return;
//        planService.updateStatusOfWBSOfDeletedEntity(project.getId(), entity.getWbsEntityType(), entityId, operator.getId(), isDeletable);

    }

    /**
     * 插入 WBS 实体同时插入ProjectNodes表里的记录。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    Part 实体
     */
    @Override
    public void insert(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        StructureWeldEntityBase entity
    ) {
        if (entity.getNo() == null) {
            throw new BusinessError("", "焊口自身编号不能为空");
        }
        if (existsByEntityNo(entity.getNo(), projectId)) {
            throw new BusinessError("", "business-error: wpMisc no ALREADY EXISTS.");
        }
        entity.setDisplayName(entity.getNo());

        if (entity.getRevision() == null) {
            throw new BusinessError("", "business-error: Revision is null.");
        }

        if (entity.getHierachyParent() == null) {
            throw new BusinessError("", "business-error: hierachyParent is null.");
        }

        if (entity.getLength() == null) {
            entity.setLength(0.0);
        }


        if (entity.getLengthText() == null) {
            entity.setLengthText(entity.getLength() + entity.getLengthUnit().toString());
        }

        entity.setWeldEntityType(entity.getWeldType());


        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        StructureWeldEntity structureWeldEntity = BeanUtils.copyProperties(entity, new StructureWeldEntity());
        if (structureWeldEntity.getId() == null) {
            structureWeldEntity.setId(CryptoUtils.uniqueDecId());
        }
        structureWeldEntityRepository.save(structureWeldEntity);


        ProjectNode projectNode = new ProjectNode();
        projectNode.setNo(entity.getNo());
        projectNode.setCompanyId(entity.getCompanyId());
        projectNode.setEntityId(entity.getId());
        projectNode.setEntityType(entity.getEntityType());
        projectNode.setEntitySubType(entity.getEntitySubType());
        projectNode.setEntityBusinessType(entity.getEntityBusinessType());
        projectNode.setOrgId(orgId);
        projectNode.setProjectId(entity.getProjectId());
        projectNode.setRemarks(entity.getRemarks());
        projectNode.setCreatedAt();
        projectNode.setCreatedBy(operator.getId());
        projectNode.setLastModifiedAt();
        projectNode.setLastModifiedBy(operator.getId());
        projectNode.setStatus(ACTIVE);
        projectNode.setDeleted(false);
        projectNode.setVersion(entity.getLastModifiedAt().getTime());
        projectNode.setDiscipline(DisciplineCode.STRUCTURE.name());
        projectNodeRepository.save(projectNode);
        projectNodeRepository.setParentEntityIdOnStructureEntity(projectId, "structure_weld", structureWeldEntity.getId());


        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) return;
        BpmEntityType bpmEntityType =
            bpmEntityTypeRepository.
                findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(
                    STRUCT_WELD_JOINT.name(),
                    project.getId(),
                    project.getOrgId(),
                    CategoryTypeTag.READONLY.name(),
                    EntityStatus.ACTIVE).orElse(null);
        if (bpmEntityType == null) {

            logger.error("There is no " + STRUCT_WELD_JOINT + "setting in DB");
            return;
        }
        Map<String, Map<String, String>> structWeldCode = bizCodeService.getStructureWeldCode(project.getOrgId(), project.getId());

        structureWeldEntityRepository.save(structureWeldEntity);

    }


    /**
     * 更新 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    Part 实体
     */
    @Override
    public StructureWeldEntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        StructureWeldEntityBase entity
    ) {
        if (entity.getNo() == null) {
            throw new BusinessError("", "焊口自身编号不能为空");
        }

        entity.setDisplayName(entity.getNo());

        if (entity.getRevision() == null) {
            throw new BusinessError("", "business-error: Revision is null.");
        }

        if (entity.getStage() == null) {
            throw new BusinessError("", "实施阶段不能为空");
        }


        if (entity.getLength() == null) {
            entity.setLength(0.0);
        }


        if (entity.getLengthText() == null) {
            entity.setLengthText(entity.getLength() + entity.getLengthUnit().toString());
        }

        for (LengthUnit unit : LengthUnit.values()) {
            if (entity.getLengthUnit().equals(unit)) {
                break;
            }

        }
        entity.setWeldEntityType(entity.getWeldType());

        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());

        StructureWeldEntity structureWeldEntity = BeanUtils.copyProperties(entity, new StructureWeldEntity());
        structureWeldEntityRepository.save(structureWeldEntity);
        return entity;
    }

    /**
     * 判断 WBS 实体是否已经存在。
     *
     * @param projectId 项目 ID
     * @param entityNO  WBS 实体号
     * @return WBS 实体是否存在  存在 ：true；不存在：false；
     */
    @Override
    public boolean existsByEntityNo(String entityNO, Long projectId) {

        if (structureWeldEntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
            return true;
        }

        return false;
    }

    /**
     * 取得零件图纸信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return
     */
    @Override
    public SubDrawingHistory findSubDrawing(
        Long id,
        Long orgId,
        Long projectId) {


        StructureWeldEntityBase structureWeldEntity = structureWeldEntityRepository.findById(id).orElse(null);
        if (structureWeldEntity == null) {
            return null;
        }

        SubDrawingHistory subDrawingHistory =
            subDrawingHistoryRepository.findBySubDrawingNoAndIssueFlagIsTrue(
                structureWeldEntity.getNo())
                .orElse(null);
        return subDrawingHistory;

    }

    /**
     * 取得零件实体对应的材料信息。
     *
     * @param id        实体ID
     * @param orgId     组织信息
     * @param projectId 项目ID
     * @return 实体信息
     */
    @Override
    public List<MaterialInfoDTO> getMaterialInfo(
        Long id,
        Long orgId,
        Long projectId) {


        return null;
    }

    /**
     * 保存实体下载临时文件。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param operatorId  项目ID
     * @return 实体下载临时文件
     */
    @Override
    public File saveDownloadFile(Long orgId, Long projectId, StructureWeldEntryCriteriaDTO criteriaDTO, Long operatorId) {

        String templateFilePath = "/var/imos/resources/templates/reports/export-project-entities-weld-structure.xlsx";

        String temporaryFileName = System.currentTimeMillis() + ".xlsx";
        String filePath = "/var/imos/private/upload/" + temporaryFileName;
        File excel = new File(filePath);

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends StructureWeldEntityBase> partEntities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();
        for (StructureWeldEntityBase entityBase : partEntities) {
            Optional<Wp05Entity> wp05Entity1 = wp05EntityRepository.findByNoAndProjectIdAndDeletedIsFalse(entityBase.getWp05No1(), projectId);
            if (wp05Entity1.isPresent()) {
                entityBase.setWp05No1(wp05Entity1.get().getDisplayName());
            }
            Optional<Wp05Entity> wp05Entity2 = wp05EntityRepository.findByNoAndProjectIdAndDeletedIsFalse(entityBase.getWp05No2(), projectId);
            if (wp05Entity2.isPresent()) {
                entityBase.setWp05No2(wp05Entity2.get().getDisplayName());
            }
        }
        EasyExcel.write(filePath, StructureWeldEntityBase.class).withTemplate(templateFilePath).sheet("weld_structure").doFill(partEntities);
        return excel;
    }


    @Override
    public String toString() {
        return "STRUCT_WELD_JOINT";
    }


    @Override
    public WpsImportResultDTO updateWeldEntityWps(Long orgId, Long projectId, Long userId, WpsImportDTO uploadDTO) {
        // todo 还未导入焊工、wps等信息，暂时注释
//        Workbook workbook = null;
//        File excel;
//
//
//        try {
//            excel = new File(temporaryDir, uploadDTO.getFileName());
//            workbook = WorkbookFactory.create(excel);
//        } catch (RecordFormatException e) {
//            throw new ValidationError("WRONG XLS FORMAT");
//        } catch (IOException e) {
//            throw new NotFoundError();
//        }
//
//        int errorCount = 0;
//        int successCount = 0;
//        int skipCount = 0;
//        List<String> errLine = new ArrayList<>();
//
//        Sheet sheet = workbook.getSheetAt(0);
//
//        WpsSimpleSearchDTO wpsSimpleSearchDTO = new WpsSimpleSearchDTO();
//        wpsSimpleSearchDTO.setFetchAll(true);
//
//        Page<WpsSimplified> wpsPageList = wpsSimpleRepository.findByOrgIdAndProjectIdAndStatusOrderByNo(
//            orgId,
//            projectId,
//            EntityStatus.ACTIVE,
//            wpsSimpleSearchDTO.toPageable()
//        );
//        List<WpsSimplified> wpsList = wpsPageList.getContent();
//        if (wpsList.size() == 0) {
//            throw new BusinessError("该项目wps列表为空");
//        }
//
//        Row row;
//        boolean skipRow = false;
//        Iterator<Row> rows = sheet.rowIterator();
//        while (rows.hasNext()) {
//            row = rows.next();
//            if (row.getRowNum() < 1) {
//                continue;
//            }
//
//            try {
//                int colIndex = 0;
//                String isoNo = trim(WorkbookUtils.readAsString(row, colIndex++));
//                String weldNo = trim(WorkbookUtils.readAsString(row, colIndex++));
//                String wpsStr = trim(WorkbookUtils.readAsString(row, colIndex++));
//                if (StringUtils.isEmpty(isoNo) || StringUtils.isEmpty(weldNo) || StringUtils.isEmpty(wpsStr)) {
//                    errorCount++;
//                    errLine.add("" + (row.getRowNum() + 1) + " information is not enough");
//                    continue;
//                }
//
//                String weldFullNo = isoNo + "-" + StringUtils.padLeft(weldNo, 4, '0');
//                List<String> wpsNos = Arrays.asList(wpsStr.split(","));
//                List<String> wpsFoundNoList = new ArrayList<>();
//                List<Long> wpsIdsList = new ArrayList<>();
//                String wpsFoundStr = "";
//                String wpsFoundIdsStr = "";
//
//                StructureWeldEntity weldEntityFind = structureWeldEntityRepository.findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(
//                    orgId,
//                    projectId,
//                    weldFullNo
//                ).orElse(null);
//                if (weldEntityFind == null) {
//                    errorCount++;
//                    errLine.add("焊口编号" + weldFullNo + "不存在");
//                    continue;
//                }
//
//                for (String wpsNo : wpsNos) {
//                    for (WpsSimplified wps : wpsList) {
//                        if (wps.getNo().equalsIgnoreCase(wpsNo)) {
//                            wpsFoundNoList.add(wpsNo);
//                            wpsIdsList.add(wps.getId());
//                            break;
//                        }
//                    }
//                }
//                if (wpsFoundNoList.size() > 0) {
//                    wpsFoundStr = StringUtil.join(wpsFoundNoList.toArray(), ",");
//                    wpsFoundIdsStr = StringUtil.join(wpsIdsList.toArray(), ",");
//
//                    structureWeldEntityRepository.updateWeldWpsNo(orgId, projectId, userId, weldFullNo, wpsFoundStr, wpsFoundIdsStr);
//                    successCount++;
//                } else {
//                    errorCount++;
//                    errLine.add("" + (row.getRowNum() + 1) + " no such wps in wps lib");
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace(System.out);
//                errorCount++;
//                errLine.add("" + (row.getRowNum() + 1));
//            }
//        }
//
//        try {
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace(System.out);
//        }
//
//        WpsImportResultDTO dto = new WpsImportResultDTO();
//        dto.setErrorCount(errorCount);
//        dto.setSkipCount(skipCount);
//        dto.setErrorList(errLine);
//        dto.setSuccessCount(successCount);
//        return dto;
        return null;
    }

}
