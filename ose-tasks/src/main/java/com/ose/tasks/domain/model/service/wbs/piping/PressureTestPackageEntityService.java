package com.ose.tasks.domain.model.service.wbs.piping;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.PressureTestPackageEntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.wbs.entity.PressureTestPackageEntityBase;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.BeanUtils;
import com.ose.util.FileUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.DisciplineCode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;


@Component
public class PressureTestPackageEntityService implements BaseWBSEntityInterface<PressureTestPackageEntityBase, WBSEntryCriteriaBaseDTO> {


    private final PressureTestPackageEntityRepository ptpEntityRepository;



    private final HierarchyInterface hierarchyService;

    private final ProjectNodeRepository projectNodeRepository;

    private final PressureTestPackageEntityRepository pressureTestPackageEntityRepository;


    private final PlanInterface planService;

    @Value("${application.files.temporary}")
    private String temporaryDir;


    private static final int DATA_START_ROW = 3;

    private static final int TEMPLATE_ROW_COUNT = 20;

    /**
     * 构造方法。
     */
    @Autowired
    public PressureTestPackageEntityService(
        PressureTestPackageEntityRepository ptpEntityRepository,
        HierarchyInterface hierarchyService,
        ProjectNodeRepository projectNodeRepository, PlanInterface planService,
        PressureTestPackageEntityRepository pressureTestPackageEntityRepository) {
        this.ptpEntityRepository = ptpEntityRepository;
        this.hierarchyService = hierarchyService;
        this.projectNodeRepository = projectNodeRepository;
        this.planService = planService;
        this.pressureTestPackageEntityRepository = pressureTestPackageEntityRepository;
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
    public Page<? extends PressureTestPackageEntityBase> search(
        Long orgId,
        Long projectId,
        WBSEntryCriteriaBaseDTO criteriaDTO,
        PageDTO pageDTO
    ) {
        return ptpEntityRepository.search(orgId, projectId, criteriaDTO, pageDTO, PressureTestPackageEntityBase.class);
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
    public PressureTestPackageEntityBase get(Long orgId, Long projectId, Long entityId) {

        PressureTestPackageEntityBase entity = ptpEntityRepository
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
    @Transactional
    public void delete(OperatorDTO operator, Long orgId, Project project, Long entityId) {
        PressureTestPackageEntityBase entity = get(orgId, project.getId(), entityId);
        if (entity == null) {
            throw new BusinessError("", "PTP entityID doesn't EXIST");
        }

//        Boolean isDeletable = projectNodeRepository.existsByProjectIdAndEntityIdAndIsDeletableIsTrue(project.getId(), entity.getId());
//        if(isDeletable == null || isDeletable)  {
            entity.setDeletedBy(operator.getId());
            entity.setDeletedAt();
            entity.setStatus(DELETED);
            entity.setDeleted(true);

            hierarchyService.delete(operator, project, orgId, entityId);
//        } else {
//
//            entity.setCancelled(true);
//            entity.setStatus(EntityStatus.CANCEL);
//            entity.setLastModifiedBy(operator.getId());
//            entity.setLastModifiedAt();
//
//            hierarchyService.cancel(operator, project, orgId, entityId);
//        }
        ptpEntityRepository.save(entity);



//        if(isDeletable == null) return;
//        planService.updateStatusOfWBSOfDeletedEntity(project.getId(), entity.getWbsEntityType(), entityId, operator.getId(), isDeletable);
    }

    /**
     * 插入 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    WBS 实体
     */
    @Override
    public void insert(OperatorDTO operator, Long orgId, Long projectId, PressureTestPackageEntityBase entity) {

//        throw new BusinessError("", "NOT YET IMPLEMENTED");
        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        PressureTestPackageEntityBase pressureTestPackageEntityBase = BeanUtils.copyProperties(entity, new PressureTestPackageEntityBase());
        pressureTestPackageEntityRepository.save(pressureTestPackageEntityBase);


        ProjectNode projectNode = new ProjectNode();
        projectNode.setNo(entity.getNo());
        projectNode.setCompanyId(entity.getCompanyId());
        projectNode.setEntityId(entity.getId());
        projectNode.setEntityType(WBSEntityType.PRESSURE_TEST_PACKAGE.name());
        projectNode.setEntitySubType(WBSEntityType.PRESSURE_TEST_PACKAGE.name());
        projectNode.setEntityBusinessType(entity.getEntityBusinessType());
//        projectNode.setNodeType(HierarchyNodeType.ENTITY);
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
        projectNode.setDiscipline(DisciplineCode.PIPING.name());

        projectNodeRepository.save(projectNode);
    }

    /**
     * 更新 WBS 实体。
     *
     * @param operator  操作者信息
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param entity    ISO 实体
     */
    @Override
    public PressureTestPackageEntityBase update(
        OperatorDTO operator,
        Long orgId,
        Long projectId,
        PressureTestPackageEntityBase entity
    ) {

        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(projectId);
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());

        PressureTestPackageEntityBase ptpEntity = BeanUtils.copyProperties(entity, new PressureTestPackageEntityBase());
        ptpEntityRepository.save(ptpEntity);
        return entity;
    }

    /**
     * 判断 WBS 实体是否存在。
     *
     * @param entityNO  WBS 实体 ID
     * @param projectId 项目 ID
     * @return 存在:true; 不存在:false
     */
    @Override
    public boolean existsByEntityNo(String entityNO, Long projectId) {
        if (pressureTestPackageEntityRepository.existsByNoAndProjectIdAndDeletedIsFalse(entityNO, projectId)) {
            return true;
        }

        return false;
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
    public File saveDownloadFile(Long orgId,
                                 Long projectId,
                                 WBSEntryCriteriaBaseDTO criteriaDTO,
                                 Long operatorId) {

        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/export-project-entities-ptp.xlsx"),
            temporaryDir,
            operatorId.toString()
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

        Sheet sheet = workbook.getSheet("Pressure Test Package");

        int rowNum = DATA_START_ROW;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<? extends PressureTestPackageEntityBase> ptpEntities = search(
            orgId,
            projectId,
            criteriaDTO,
            pageDTO).getContent();
        for (PressureTestPackageEntityBase entity : ptpEntities) {
            Row row = WorkbookUtils.getRow(sheet, rowNum++);

            if (rowNum >= TEMPLATE_ROW_COUNT + DATA_START_ROW) {
                WorkbookUtils.copyRow(sheet.getRow(DATA_START_ROW), row);
            }

            WorkbookUtils.getCell(row, 0).setCellValue(entity.getNo());

            WorkbookUtils.getCell(row, 1).setCellValue(entity.getRevision());
            WorkbookUtils.getCell(row, 2).setCellValue(entity.getPressureTestClass());
            WorkbookUtils.getCell(row, 3).setCellValue(entity.getTestPressureText());
            WorkbookUtils.getCell(row, 4).setCellValue(entity.getTestMedium());
            WorkbookUtils.getCell(row, 5).setCellValue(entity.getPtpDrawingNo());
            WorkbookUtils.getCell(row, 6).setCellValue(entity.getMaxOperatingPressureText());
            WorkbookUtils.getCell(row, 7).setCellValue(entity.getMaxDesignPressureText());
            WorkbookUtils.getCell(row, 8).setCellValue(entity.getRemarks());
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
