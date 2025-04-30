package com.ose.tasks.domain.model.service.builder.piping;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmDeliveryEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.CleanPackageEntityRepository;
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
import com.ose.tasks.entity.wbs.entity.CleanPackageEntityBase;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp05Entity;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 清洁包实体构建器。
 */
@Component
public class CleanPackageEntityBuilder implements WBSEntityBuilder<CleanPackageEntityBase> {


    private final BaseWBSEntityInterface<CleanPackageEntityBase, WBSEntryCriteriaBaseDTO> cleanPackageEntityService;

    // 项目层级机构管理服务接口
    private final HierarchyInterface hierarchyService;

    //bpm_delivery_entity
    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;

    // 计划管理服务接口
    private final PlanInterface planService;
    private final EntitySubTypeInterface entitySubTypeService;


    private final CleanPackageEntityRepository cleanPackageEntityRepository;

    private final ProjectNodeRepository projectNodeRepository;

    @Autowired
    public CleanPackageEntityBuilder(BaseWBSEntityInterface<CleanPackageEntityBase, WBSEntryCriteriaBaseDTO> cleanPackageEntityService,
                                     HierarchyInterface hierarchyService,
                                     BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
                                     PlanInterface planService,
                                     EntitySubTypeInterface entitySubTypeService, CleanPackageEntityRepository cleanPackageEntityRepository,
                                     ProjectNodeRepository projectNodeRepository) {
        this.cleanPackageEntityService = cleanPackageEntityService;
        this.hierarchyService = hierarchyService;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.planService = planService;
        this.entitySubTypeService = entitySubTypeService;
        this.cleanPackageEntityRepository = cleanPackageEntityRepository;
        this.projectNodeRepository = projectNodeRepository;
    }

    @Override
    public CleanPackageEntityBase build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public CleanPackageEntityBase build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, CleanPackageEntityBase entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public WBSImportLogDTO<CleanPackageEntityBase> buildWbs(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator) {

        WBSImportLogDTO wbsImportLogDTO = buildDefault(config, project, row, columnConfigs, operator);
        WBSEntityBase entity = wbsImportLogDTO.getWbsEntityBase();
        BpmEntitySubType best = entitySubTypeService.getEntitySubTypeByWbs(project.getId(), entity.getEntityType(), entity.getEntitySubType());
        if (best != null) entity.setDiscipline(best.getDiscipline());
        wbsImportLogDTO.setWbsEntityBase(entity);
        return wbsImportLogDTO;
    }


    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        cleanPackageEntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );

        // 删除实体需要同时删除对应的在hierarchy_node和project_node里的数据(deleted设置为true)
        // 删除实体可能关联的层级信息
        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entity.getId());
//        hierarchyService.delete(operator, project, entity.getOrgId(), entity.getId());
//        planService.updateStatusOfWBSOfDeletedEntity(project.getProjId(), "CLEAN_PACKAGE", entity.getId());
    }

    @Override
    public <T extends WBSEntityBase> String checkProperty(T entity, Set<String> entityTypes,
                                                          Set<EntitySubTypeRule> entitySubTypeRules) {

        String entitySubType = entity.getEntitySubType();
        if (entitySubType == null) return "There is No sub entity type for entity " + entity.getNo();
        if (!entityTypes.contains(entitySubType)) {
            return "there is no sub entity type for entity " + entity.getNo();
        }
        return null;
    }

    @Override
    public void deleteNoneHierarchyEntity(Long noneHierarchyEntityId) {
        CleanPackageEntityBase cleanPackageEntityBase = cleanPackageEntityRepository.findById(noneHierarchyEntityId).orElse(null);
        if (cleanPackageEntityBase != null) {
            cleanPackageEntityRepository.deleteById(noneHierarchyEntityId);
        }
    }


    @Override //node type
    public String toString() {
        return "CLEAN_PACKAGE";
    }

    /**
     * 取得实体规则
     */
    @Override
    public EntitySubTypeRule getRule(
        Long orgId,
        Long projectId,
        WBSEntityImportSheetConfigBuilder.SheetConfig config,
        Row row,
        Set<EntitySubTypeRule> entitySubTypeRules
    ) {
        return null;
    }

    @Override
    public String checkParent(Project project, Row row, EntitySubTypeRule rule) {
        String parentNo = WorkbookUtils.readAsString(row, 0);
        ProjectNode projectNode = projectNodeRepository.findByOrgIdAndProjectIdAndEntityTypeAndNoAndDeletedIsFalse(
            project.getOrgId(),
            project.getId(),
            rule.getParentType(),
            parentNo
        ).orElse(null);
        if (projectNode == null) {
            return "实体父级不存在或当前实体父级类型不正确";
        } else {
            return "";
        }
    }

    @Override
    public CleanPackageEntityBase checkRule(Long orgId, Long projectId, CleanPackageEntityBase entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public CleanPackageEntityBase generateQrCode(CleanPackageEntityBase entity) {
        return null;
    }

}
