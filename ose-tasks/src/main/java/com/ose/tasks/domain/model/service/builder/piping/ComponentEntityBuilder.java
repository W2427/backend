package com.ose.tasks.domain.model.service.builder.piping;

import com.ose.dto.OperatorDTO;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmDeliveryEntityRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ComponentEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ISOEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.SpoolEntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.wbs.piping.ComponentEntityInterface;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.ComponentEntity;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.tasks.entity.wbs.entity.SpoolEntity;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 组件实体构建器。
 */
@Component
public class ComponentEntityBuilder implements WBSEntityBuilder<ComponentEntity> {

    private final ComponentEntityInterface componentEntityService;

    // 项目层级机构管理服务接口
    private final HierarchyInterface hierarchyService;

    //bpm_delivery_entity
    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;

    private final ComponentEntityRepository componentEntityRepository;
//    private final NPSRepository nPSRepository;

//    private final PanelRepository panelRepository;

    private final EntitySubTypeInterface entitySubTypeService;


    private final BpmEntitySubTypeRepository bpmEntityCategoryRepository;

    private final EntitySubTypeRuleRepository entitySubTypeRuleRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final SpoolEntityRepository spoolEntityRepository;

    private final ISOEntityRepository isoEntityRepository;

    // 计划管理服务接口
    private final PlanInterface planService;
    private static final String PIPE = "PIPE";
    private static final String OTHER = "OTHER";
    private static final String SEPARATOR = ";";
    private static final String OTHERS_ON_SPOOL = "OTHERS_ON_SPOOL";
    private static final String OTHERS_ON_ISO = "OTHERS_ON_ISO";
    private static final String FLANGE = "FLANGE";

    private static final Pattern THICKNESS = Pattern.compile(
        "(S-[\\d]{1,3}s?)|(X{1,3}S)|(STD)|(N/A)|(\\d+(\\.\\d+)?('|\"|ft|in|mm|cm|m).*)|(\\d+(\\.\\d+)?(mm|cm|m)\\((\\d+)/(\\d+)('|\"|ft|in)\\)\\s*THCK)|(\\d+(\\.\\d+)?)",
        Pattern.CASE_INSENSITIVE
    );

    @Autowired
    public ComponentEntityBuilder(ComponentEntityInterface componentEntityService,
                                  HierarchyInterface hierarchyService,
                                  BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
                                  ComponentEntityRepository componentEntityRepository,
                                  EntitySubTypeInterface entitySubTypeService, PlanInterface planService,
//                                  NPSRepository nPSRepository,
//                                  PanelRepository panelRepository,
                                  BpmEntitySubTypeRepository bpmEntityCategoryRepository,
                                  EntitySubTypeRuleRepository entitySubTypeRuleRepository,
                                  ProjectNodeRepository projectNodeRepository,
                                  SpoolEntityRepository spoolEntityRepository,
                                  ISOEntityRepository isoEntityRepository) {
        this.componentEntityService = componentEntityService;
        this.hierarchyService = hierarchyService;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
        this.componentEntityRepository = componentEntityRepository;
        this.entitySubTypeService = entitySubTypeService;
        this.planService = planService;
//        this.nPSRepository = nPSRepository;
//        this.panelRepository = panelRepository;
        this.bpmEntityCategoryRepository = bpmEntityCategoryRepository;
        this.entitySubTypeRuleRepository = entitySubTypeRuleRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.spoolEntityRepository = spoolEntityRepository;
        this.isoEntityRepository = isoEntityRepository;
    }
    @Override
    public WBSImportLogDTO<ComponentEntity> buildWbs(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator){

        WBSImportLogDTO wbsImportLogDTO = buildDefault(config,project,row, columnConfigs,operator);
        WBSEntityBase entity = wbsImportLogDTO.getWbsEntityBase();
        BpmEntitySubType best = entitySubTypeService.getEntitySubTypeByWbs(project.getId(),entity.getEntityType(),entity.getEntitySubType());
        if(best != null) entity.setDiscipline(best.getDiscipline());
        wbsImportLogDTO.setWbsEntityBase(entity);
        return wbsImportLogDTO;
    }

    @Override
    public ComponentEntity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public ComponentEntity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, ComponentEntity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {

        componentEntityRepository.findById(entity.getId()).ifPresent(ce ->{
            componentEntityService.delete(
                operator,
                entity.getOrgId(),
                project,
                entity.getId()
            );

            // 删除实体需要同时删除对应的在hierarchy_node和project_node里的数据(deleted设置为true)
            // 删除实体可能关联的层级信息
            bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entity.getId());
        });

//        hierarchyService.delete(operator, project, entity.getOrgId(), entity.getId());
//        planService.updateStatusOfWBSOfDeletedEntity(project.getProjId(), "COMPONENT", entity.getId());
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
        ComponentEntity componentEntity = componentEntityRepository.findById(noneHierarchyEntityId).orElse(null);
        if (componentEntity != null) {
            componentEntityRepository.deleteById(noneHierarchyEntityId);
        }
    }


    @Override //node type
    public String toString() {
        return "COMPONENT";
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
        String shortCode = WorkbookUtils.readAsString(row, 4);
        if (shortCode == null || shortCode.equals("")) {
            return null;
        }

        for (EntitySubTypeRule rule : entitySubTypeRules) {
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
    public ComponentEntity checkRule(Long orgId, Long projectId, ComponentEntity entity, EntitySubTypeRule rule) {
        entity.setComponentEntityType(rule.getSubType());

        if (rule.getThicknessRequired()) {
            if (StringUtils.isEmpty(entity.getThickness())) {
                throw new ValidationError("thickness is required");
            }
            Matcher matcher = THICKNESS.matcher(entity.getThickness());

            if (matcher.find()) {
                entity.setThickness(matcher.group());
            } else {
                throw new ValidationError("thickness is INVALID");
            }
        }

        if (rule.getParentType().equals(WBSEntityType.SPOOL.name())) {
            SpoolEntity spoolEntity = spoolEntityRepository.findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(orgId, projectId, entity.getParentNo()).orElse(null);
            if (spoolEntity == null) {
                throw new ValidationError("NO SPOOL PARENT for this weld");
            }

            Long iso_entity_id = projectNodeRepository.findIsoNoBySpoolId(orgId, projectId, spoolEntity.getId());

            if (iso_entity_id == null) {
                throw new ValidationError("NO ISO PARENT for this weld");
            }

            ISOEntity isoEntity = isoEntityRepository.findById(iso_entity_id).orElse(new ISOEntity());

            entity.setIsoNo(isoEntity.getNo());
            entity.setIsoEntityId(iso_entity_id);
            entity.setSpoolEntityId(spoolEntity.getId());
            entity.setSpoolNo(spoolEntity.getNo());
        }

        if (rule.getParentType().equals(WBSEntityType.ISO.name())) {

            Optional<ISOEntity> isoEntity = isoEntityRepository.findByProjectIdAndNoAndDeletedIsFalse(projectId, entity.getParentNo());
            if (!isoEntity.isPresent()) {
                throw new ValidationError("ISO is not existed");
            }

            entity.setIsoEntityId(isoEntity.get().getId());
            entity.setIsoNo(isoEntity.get().getNo());
        }

        String nodeNo = entity.getParentNo() + HYPHEN + entity.getShortCode() + HYPHEN + HYPHEN + entity.getMaterialCode();
        entity.setNo(nodeNo);

        return entity;

    }

    @Override
    public ComponentEntity generateQrCode(ComponentEntity entity) {
        if (entity.getQrCode() == null) {
            String qrCode = QrcodePrefixType.COMPONENT.getCode() + StringUtils.generateShortUuid();
            entity.setQrCode(qrCode);
        }

        return entity;
    }

}
