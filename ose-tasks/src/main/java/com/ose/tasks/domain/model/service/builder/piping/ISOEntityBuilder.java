package com.ose.tasks.domain.model.service.builder.piping;
import com.ose.dto.OperatorDTO;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmDeliveryEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ISOEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.SpoolEntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.wbs.piping.ISOEntityInterface;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
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
import java.util.regex.Pattern;

/**
 * ISO实体构建器。
 */
@Component
//@Lazy
public class ISOEntityBuilder implements WBSEntityBuilder<ISOEntity> {

    private final SpoolEntityRepository spoolEntityRepository;

    private final ISOEntityInterface isoEntityService;

    // 项目层级机构管理服务接口
    private final HierarchyInterface hierarchyService;

    private final EntitySubTypeInterface entitySubTypeService;

    //bpm_delivery_entity
    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;

    private final ISOEntityRepository isoEntityRepository;

    private final ProjectNodeRepository projectNodeRepository;

//    private final NPSRepository nPSRepository;

    // 计划管理服务接口
    private final PlanInterface planService;

    private static final Pattern TAIL_PATTERN = Pattern.compile("(.+)\\-(.+(\\(.*\\))?)$");

    private static final Pattern HEAD_PATTERN = Pattern.compile("(.+(\\(.*\\))?)\\-(.+)$");

    private static final String isoHierarchyTypes[] =
        new String[]{"PIPING", "TEST_PACKAGE", "CLEAN_PACKAGE", "SUB_SYSTEM"};

    @Autowired
    public ISOEntityBuilder(SpoolEntityRepository spoolEntityRepository,
                            ISOEntityInterface isoEntityService,
                            HierarchyInterface hierarchyService,
                            EntitySubTypeInterface entitySubTypeService, BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
                            ISOEntityRepository isoEntityRepository,
                            PlanInterface planService,
//                            NPSRepository nPSRepository,
                            ProjectNodeRepository projectNodeRepository) {
        this.spoolEntityRepository = spoolEntityRepository;
        this.isoEntityService = isoEntityService;
        this.hierarchyService = hierarchyService;
        this.entitySubTypeService = entitySubTypeService;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.isoEntityRepository = isoEntityRepository;
        this.planService = planService;
//        this.nPSRepository = nPSRepository;
        this.projectNodeRepository = projectNodeRepository;
    }

    //TODO HEAD_PATTERN, SAME_PATTERN


    @Override
    public WBSImportLogDTO<ISOEntity> buildWbs(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator){

        WBSImportLogDTO wbsImportLogDTO = buildDefault(config,project,row, columnConfigs,operator);

        WBSEntityBase entity = wbsImportLogDTO.getWbsEntityBase();
        BpmEntitySubType best = entitySubTypeService.getEntitySubTypeByWbs(project.getId(),entity.getEntityType(),entity.getEntitySubType());
        if(best != null) entity.setDiscipline(best.getDiscipline());
        wbsImportLogDTO.setWbsEntityBase(entity);
        return wbsImportLogDTO;
    }

    @Override
    public ISOEntity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public ISOEntity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, ISOEntity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        isoEntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );

        // 删除实体需要同时删除对应的在hierarchy_node和project_node里的数据(deleted设置为true)
        // 删除实体可能关联的层级信息
        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entity.getId());
//        hierarchyService.delete(operator, project, entity.getOrgId(), entity.getId());
//        planService.updateStatusOfWBSOfDeletedEntity(project.getProjId(), "ISO", entity.getId());
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
        ISOEntity iSOEntity = isoEntityRepository.findById(noneHierarchyEntityId).orElse(null);
        if (iSOEntity != null) {
            isoEntityRepository.deleteById(noneHierarchyEntityId);
        }
    }

    @Override //node type
    public String toString() {
        return "ISO";
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
        EntitySubTypeRule entitySubTypeRule = null;
        int colIndex = config.getMaxParents() + 1;

        // 实施阶段
        String stage = WorkbookUtils.readAsString(row, colIndex++);
        if (stage == null || StringUtils.isEmpty(stage)) {
            return null;
        }
        // 类型
        String type = WorkbookUtils.readAsString(row, colIndex++);
        if (type == null || StringUtils.isEmpty(type)) {
            return null;
        }
        for (EntitySubTypeRule rule : entitySubTypeRules) {
            String value1 = rule.getValue1();
            String value2 = rule.getValue2();
            if (!StringUtils.isEmpty(value1) || !StringUtils.isEmpty(value2)) {
                if (value1.equals(stage) && value2.equals(type)) {
                    entitySubTypeRule = rule;
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        return entitySubTypeRule;
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
    public ISOEntity checkRule(Long orgId, Long projectId, ISOEntity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public ISOEntity generateQrCode(ISOEntity entity) {
        if (entity.getQrCode() == null) {
            String qrCode = QrcodePrefixType.ISO.getCode() + StringUtils.generateShortUuid();
            entity.setQrCode(qrCode);
        }

        return entity;
    }

}
