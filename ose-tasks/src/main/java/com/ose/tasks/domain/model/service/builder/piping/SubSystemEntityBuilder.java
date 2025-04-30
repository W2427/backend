package com.ose.tasks.domain.model.service.builder.piping;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.SubSystemEntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.SubSystemEntityBase;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder.SheetConfig;

/**
 * 子系统实体构建器。
 */
@Component
public class SubSystemEntityBuilder implements WBSEntityBuilder<SubSystemEntityBase> {


    private final BaseWBSEntityInterface<SubSystemEntityBase, WBSEntryCriteriaBaseDTO> subSystemEntityService;


    private final HierarchyInterface hierarchyService;


    private final PlanInterface planService;

    private final SubSystemEntityRepository subSystemEntityRepository;

    private final EntitySubTypeInterface entitySubTypeService;

    private final ProjectNodeRepository projectNodeRepository;

    @Autowired
    public SubSystemEntityBuilder(BaseWBSEntityInterface<SubSystemEntityBase, WBSEntryCriteriaBaseDTO> subSystemEntityService,
                                  HierarchyInterface hierarchyService,
                                  PlanInterface planService,
                                  SubSystemEntityRepository subSystemEntityRepository,
                                  EntitySubTypeInterface entitySubTypeService,
                                  ProjectNodeRepository projectNodeRepository) {
        this.subSystemEntityService = subSystemEntityService;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.subSystemEntityRepository = subSystemEntityRepository;
        this.entitySubTypeService = entitySubTypeService;
        this.projectNodeRepository = projectNodeRepository;
    }

    @Override
    public SubSystemEntityBase build(SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public SubSystemEntityBase build(SheetConfig config, Project project, Row row, SubSystemEntityBase entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public WBSImportLogDTO<SubSystemEntityBase> buildWbs(SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator) {
        WBSImportLogDTO wbsImportLogDTO = buildDefault(config,project,row, columnConfigs,operator);
        WBSEntityBase entity = wbsImportLogDTO.getWbsEntityBase();
        BpmEntitySubType best = entitySubTypeService.getEntitySubTypeByWbs(project.getId(),entity.getEntityType(),entity.getEntitySubType());
        if(best != null) entity.setDiscipline(best.getDiscipline());
        wbsImportLogDTO.setWbsEntityBase(entity);
        return wbsImportLogDTO;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        subSystemEntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );
    }

    @Override
    public <T extends WBSEntityBase> String checkProperty(T entity, Set<String> entityTypes,
                                                          Set<EntitySubTypeRule> entityCategoryRules) {

        String entitySubType = entity.getEntitySubType();
        if (entitySubType == null) {
            entitySubType = "SUB_SYSTEM";

        }
        if (!entityTypes.contains(entitySubType)) {
            return "there is no sub entity type" + entity.getNo();
        }
        return null;
    }

    @Override
    public void deleteNoneHierarchyEntity(Long noneHierarchyEntityId) {
        SubSystemEntityBase subSystemEntityBase = subSystemEntityRepository.findById(noneHierarchyEntityId).orElse(null);
        if (subSystemEntityBase != null) {
            subSystemEntityRepository.deleteById(noneHierarchyEntityId);
        }
    }

    @Override
    public EntitySubTypeRule getRule(Long orgId, Long projectId, SheetConfig config, Row row, Set<EntitySubTypeRule> entityCategoryRules) {
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
    public SubSystemEntityBase checkRule(Long orgId, Long projectId, SubSystemEntityBase entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public SubSystemEntityBase generateQrCode(SubSystemEntityBase entity) {
        return null;
    }


    @Override
    public String toString() {
        return "SUB_SYSTEM";
    }
}
