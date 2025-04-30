package com.ose.tasks.domain.model.service.builder.piping;

import com.ose.dto.OperatorDTO;
import com.ose.exception.ValidationError;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmDeliveryEntityRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.ISOEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.SpoolEntityRepository;
import com.ose.tasks.domain.model.repository.wbs.piping.WeldEntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.wbs.piping.WeldEntityInterface;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.*;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 焊口实体构建器。
 */
@Component
//@Lazy
public class WeldEntityBuilder implements WBSEntityBuilder<WeldEntity> {


    //WeldEntityInterface
    private final WeldEntityInterface weldEntityService;
//    private final NPSRepository nPSRepository;

    // 项目层级机构管理服务接口
    private final HierarchyInterface hierarchyService;

    // 计划管理服务接口
    private final PlanInterface planService;

    //bpm_delivery_entity
    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;

    // 材料代码别称和分组的对应表
//    private final MaterialCodeAliasGroupRepository materialCodeAliasGroupRepository;

    private final SpoolEntityRepository spoolEntityRepository;

    private final ISOEntityRepository isoEntityRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final WeldEntityRepository weldEntityRepository;

    private final BpmEntitySubTypeRepository bpmEntityCategoryRepository;

    private final EntitySubTypeRuleRepository entitySubTypeRuleRepository;

    private final EntitySubTypeInterface entitySubTypeService;

    private static final Pattern THICKNESS = Pattern.compile(
        "(S-[\\d]{1,3}s?)|(X{1,3}S)|(STD)|(N/A)|(\\d+(\\.\\d+)?('|\"|ft|in|mm|cm|m).*)|(\\d+(\\.\\d+)?(mm|cm|m)\\((\\d+)/(\\d+)('|\"|ft|in)\\)\\s*THCK)|(\\d+(\\.\\d+)?)",
        Pattern.CASE_INSENSITIVE
    );

    @Autowired
    public WeldEntityBuilder(WeldEntityInterface weldEntityService,
                             HierarchyInterface hierarchyService,
                             PlanInterface planService,
                             BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
//                             MaterialCodeAliasGroupRepository materialCodeAliasGroupRepository,
                             SpoolEntityRepository spoolEntityRepository,
                             ISOEntityRepository isoEntityRepository,
                             ProjectNodeRepository projectNodeRepository,
                             WeldEntityRepository weldEntityRepository,
//                             NPSRepository nPSRepository,
                             BpmEntitySubTypeRepository bpmEntityCategoryRepository,
                             EntitySubTypeRuleRepository entitySubTypeRuleRepository,
                             EntitySubTypeInterface entitySubTypeService) {
        this.weldEntityService = weldEntityService;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
//        this.materialCodeAliasGroupRepository = materialCodeAliasGroupRepository;
        this.spoolEntityRepository = spoolEntityRepository;
        this.isoEntityRepository = isoEntityRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.weldEntityRepository = weldEntityRepository;
//        this.nPSRepository = nPSRepository;
        this.bpmEntityCategoryRepository = bpmEntityCategoryRepository;
        this.entitySubTypeRuleRepository = entitySubTypeRuleRepository;
        this.entitySubTypeService = entitySubTypeService;
    }

    @Override
    public WBSImportLogDTO<WeldEntity> buildWbs(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator){

        WBSImportLogDTO wbsImportLogDTO = buildDefault(config,project,row, columnConfigs,operator);
        WBSEntityBase entity = wbsImportLogDTO.getWbsEntityBase();
        BpmEntitySubType best = entitySubTypeService.getEntitySubTypeByWbs(project.getId(),entity.getEntityType(),entity.getEntitySubType());
        if(best != null) entity.setDiscipline(best.getDiscipline());
        wbsImportLogDTO.setWbsEntityBase(entity);
        return wbsImportLogDTO;
    }

    @Override
    public WeldEntity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public WeldEntity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, WeldEntity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        weldEntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );

        // 删除实体需要同时删除对应的在hierarchy_node和project_node里的数据(deleted设置为true)
        // 删除实体可能关联的层级信息
        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getId(), entity.getId());
//        hierarchyService.delete(operator, project, entity.getOrgId(), entity.getId());
//        planService.updateStatusOfWBSOfDeletedEntity(project.getProjId(), "WELD_JOINT", entity.getId());
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
        WeldEntity weld = weldEntityRepository.findById(noneHierarchyEntityId).orElse(null);
        if (weld != null) {
            weldEntityRepository.deleteById(noneHierarchyEntityId);
        }
    }


    @Override //node type
    public String toString() {
        return "WELD_JOINT";
    }


    @Override
    public WeldEntity checkRule(Long orgId, Long projectId, WeldEntity entity, EntitySubTypeRule rule) {
        entity.setWeldEntityType(rule.getSubType());

        if (rule.getThicknessRequired()) {
            if (StringUtils.isEmpty(entity.getThickness())) {
                throw new ValidationError("thickness is INVALID");
            }
            String thicknessStr = entity.getThickness();
            thicknessStr = thicknessStr.replaceAll("(S)(CH|-)?(\\d+)s?", "$1-$3");
            thicknessStr = thicknessStr.replaceAll("S-(STD|XS|XXS|XXXS)", "$1");
            Matcher matcher = THICKNESS.matcher(thicknessStr);

            if (matcher.find()) {
                entity.setThickness(matcher.group());
            } else {
                throw new ValidationError("thickness is INVALID");
            }
        }

        if (rule.getEntitySubType() != null
            && rule.getEntitySubType().getEntityBusinessType() != null) {
            entity.setEntityBusinessType(
                rule.getEntitySubType().getEntityBusinessType().getNameEn());
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
            entity.setPaintingCode(isoEntity.getPaintingCode());
        }

        if (rule.getParentType().equals(WBSEntityType.ISO.name())) {

            Optional<ISOEntity> isoEntity = isoEntityRepository.findByProjectIdAndNoAndDeletedIsFalse(projectId, entity.getParentNo());
            if (!isoEntity.isPresent()) {
                throw new ValidationError("ISO is not existed");
            }

            entity.setIsoEntityId(isoEntity.get().getId());
            entity.setIsoNo(isoEntity.get().getNo());
            entity.setPaintingCode(isoEntity.get().getPaintingCode());
        }
        String nodeNo = entity.getParentNo() + "-W" + StringUtils.padLeft(entity.getDisplayName(), 4, '0');
        entity.setNo(nodeNo);
        return entity;
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
//        int colIndex = config.getMaxParents() + 1;
        int colIndex = 2;

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
    public WeldEntity generateQrCode(WeldEntity entity) {
        if (entity.getQrCode() == null) {
            String qrCode = QrcodePrefixType.WELD.getCode() + StringUtils.generateShortUuid();
            entity.setQrCode(qrCode);
        }

        return entity;
    }

}
