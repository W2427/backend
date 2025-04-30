package com.ose.tasks.domain.model.service.builder.piping;
import com.ose.dto.OperatorDTO;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmDeliveryEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.PressureTestPackageEntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.PressureTestPackageEntityBase;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.tasks.vo.wbs.HierarchyType;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 试压包实体构建器。
 */
@Component
public class PressureTestPackageEntityBuilder implements WBSEntityBuilder<PressureTestPackageEntityBase> {

    private final BaseWBSEntityInterface<PressureTestPackageEntityBase, WBSEntryCriteriaBaseDTO> pressureTestPackageEntityService;


    private final HierarchyInterface hierarchyService;


    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;


    private final PlanInterface planService;

    private final PressureTestPackageEntityRepository pressureTestPackageEntityRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final EntitySubTypeInterface entitySubTypeService;

    @Autowired
    public PressureTestPackageEntityBuilder(BaseWBSEntityInterface<PressureTestPackageEntityBase, WBSEntryCriteriaBaseDTO> pressureTestPackageEntityService,
                                            HierarchyInterface hierarchyService,
                                            BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
                                            PlanInterface planService,
                                            PressureTestPackageEntityRepository pressureTestPackageEntityRepository,
                                            ProjectNodeRepository projectNodeRepository,
                                            EntitySubTypeInterface entitySubTypeService) {
        this.pressureTestPackageEntityService = pressureTestPackageEntityService;
        this.hierarchyService = hierarchyService;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.planService = planService;
        this.pressureTestPackageEntityRepository = pressureTestPackageEntityRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.entitySubTypeService = entitySubTypeService;
    }


    @Override
    public PressureTestPackageEntityBase build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public PressureTestPackageEntityBase build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, PressureTestPackageEntityBase entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public WBSImportLogDTO<PressureTestPackageEntityBase> buildWbs(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator) {
        WBSImportLogDTO wbsImportLogDTO = buildDefault(config, project, row, columnConfigs, operator);
        WBSEntityBase entity = wbsImportLogDTO.getWbsEntityBase();
        BpmEntitySubType best = entitySubTypeService.getEntitySubTypeByWbs(project.getId(), entity.getEntityType(), entity.getEntitySubType());
        if (best != null) entity.setDiscipline(best.getDiscipline());
        wbsImportLogDTO.setWbsEntityBase(entity);
        return wbsImportLogDTO;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        pressureTestPackageEntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );



        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entity.getId());

    }

    @Override
    public <T extends WBSEntityBase> String checkProperty(T entity, Set<String> entityTypes,
                                                          Set<EntitySubTypeRule> entityCategoryRules) {

        String subEntityType = entity.getEntitySubType();
        if (subEntityType == null) {

            subEntityType = WBSEntityType.PRESSURE_TEST_PACKAGE.name();
        }
        if (!entityTypes.contains(subEntityType)) {
            return "there is no sub entity type" + entity.getNo();
        }
        return null;
    }

    @Override
    public void deleteNoneHierarchyEntity(Long noneHierarchyEntityId) {
        PressureTestPackageEntityBase pressureTestPackageEntityBase = pressureTestPackageEntityRepository.findById(noneHierarchyEntityId).orElse(null);
        if (pressureTestPackageEntityBase != null) {
            pressureTestPackageEntityRepository.deleteById(noneHierarchyEntityId);
        }
    }

    @Override
    public EntitySubTypeRule getRule(Long orgId, Long projectId, WBSEntityImportSheetConfigBuilder.SheetConfig config, Row row, Set<EntitySubTypeRule> entityCategoryRules) {
        return null;
    }

    @Override
    public String checkParent(Project project, Row row, EntitySubTypeRule rule) {
        String parentNo = WorkbookUtils.readAsString(row, 0);
        ProjectNode projectNode = projectNodeRepository.findByProjectIdAndNoAndEntityTypeAndDeletedIsFalse(
            project.getId(),
            parentNo,
            rule.getParentType()
        ).orElse(null);
        if (projectNode == null) {
            return "实体父级不存在或当前实体父级类型不正确";
        } else {
            return "";
        }
    }

    @Override
    public PressureTestPackageEntityBase checkRule(Long orgId, Long projectId, PressureTestPackageEntityBase entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public PressureTestPackageEntityBase generateQrCode(PressureTestPackageEntityBase entity) {
        return null;
    }


    @Override
    public String toString() {
        return "PRESSURE_TEST_PACKAGE";
    }
}
