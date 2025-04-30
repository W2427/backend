package com.ose.tasks.domain.model.service.builder.structure;
import com.ose.dto.OperatorDTO;
import com.ose.exception.ValidationError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.*;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.wbs.structure.StructureWeldEntityInterface;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.tasks.entity.wbs.entity.SpoolEntity;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldEntity;
import com.ose.tasks.entity.wbs.structureEntity.Wp02Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp03Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp04Entity;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.tasks.vo.wbs.WBSEntityType;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.QrcodePrefixType;
import com.ose.vo.RedisKey;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 结构 Weld 实体构建器。
 */
@Component
public class StructureWeldEntityBuilder extends StringRedisService implements WBSEntityBuilder<StructureWeldEntity> {

    //结构焊口操作服务
    private final StructureWeldEntityInterface structureWeldEntityService;

    // 项目层级机构管理服务接口
    private final HierarchyInterface hierarchyService;

    // 计划管理服务接口
    private final PlanInterface planService;

    private final BpmEntitySubTypeRepository bpmEntityCategoryRepository;

    private final StructureWeldEntityRepository structureWeldEntityRepository;

    private final Wp05EntityRepository wp05EntityRepository;

    private final Wp04EntityRepository wp04EntityRepository;

    private final Wp03EntityRepository wp03EntityRepository;

    private final Wp02EntityRepository wp02EntityRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private static final Pattern THICKNESS = Pattern.compile(
        "(S-[\\d]{1,3}s?)|(X{1,3}S)|(STD)|(NA)|(\\d+(\\.\\d+)?('|\"|ft|in|mm|cm|m).*)|(\\d+(\\.\\d+)?(mm|cm|m)\\((\\d+)/(\\d+)('|\"|ft|in)\\)\\s*THCK)|(\\d+(\\.\\d+)?)",
        Pattern.CASE_INSENSITIVE
    );
    private final EntitySubTypeInterface entitySubTypeService;

    static Set<String> structureEntityTypes = new HashSet();

    static {
        structureEntityTypes.add("WP01");
        structureEntityTypes.add("WP02");
        structureEntityTypes.add("WP03");
        structureEntityTypes.add("WP04");
    }

    @Autowired
    public StructureWeldEntityBuilder(StructureWeldEntityInterface structureWeldEntityService,
                                      HierarchyInterface hierarchyService,
                                      PlanInterface planService,
                                      BpmEntitySubTypeRepository bpmEntityCategoryRepository,
                                      StructureWeldEntityRepository structureWeldEntityRepository,
                                      Wp05EntityRepository wp05EntityRepository,
                                      Wp04EntityRepository wp04EntityRepository,
                                      Wp03EntityRepository wp03EntityRepository,
                                      StringRedisTemplate stringRedisTemplate,
                                      Wp02EntityRepository wp02EntityRepository,
                                      ProjectNodeRepository projectNodeRepository, EntitySubTypeInterface entitySubTypeService) {
        super(stringRedisTemplate);
        this.structureWeldEntityService = structureWeldEntityService;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.bpmEntityCategoryRepository = bpmEntityCategoryRepository;
        this.structureWeldEntityRepository = structureWeldEntityRepository;
        this.wp05EntityRepository = wp05EntityRepository;
        this.wp04EntityRepository = wp04EntityRepository;
        this.wp03EntityRepository = wp03EntityRepository;
        this.wp02EntityRepository = wp02EntityRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.entitySubTypeService = entitySubTypeService;
    }

    @Override
    public WBSImportLogDTO<StructureWeldEntity> buildWbs(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator){

        WBSImportLogDTO wbsImportLogDTO = buildDefault(config,project,row, columnConfigs,operator);
        WBSEntityBase entity = wbsImportLogDTO.getWbsEntityBase();
        BpmEntitySubType best = entitySubTypeService.getEntitySubTypeByWbs(project.getId(),entity.getEntityType(),entity.getEntitySubType());
        if(best != null) entity.setDiscipline(best.getDiscipline());
        wbsImportLogDTO.setWbsEntityBase(entity);
        return wbsImportLogDTO;
    }
    @Override
    public <T extends WBSEntityBase> String checkProperty(T entity, Set<String> entityTypes,
                                                          Set<EntitySubTypeRule> entitySubTypeRules) {
        String entityTypeSubTypeKey = String.format(RedisKey.ENTITY_TYPE_SUB_TYPE.getDisplayName(), entity.getProjectId().toString());

        String entitySubType = entity.getEntitySubType();

        //如果实体子类型没填写，报错（其实这个判断没必要）
        if (entitySubType == null) return "The sub entity type for entity" + entity.getNo() + "doesn't exist.";

        //根据实体大类和实体子类型去查找数据库，如果找不到，就报错
//        List<BpmEntitySubType> bpmEntityCategoryList =
//            bpmEntityCategoryRepository.findEntityCategoriesByEntityType(this.toWpNameString(), entitySubType, entity.getProjectId());
        if(toWpNameString().equalsIgnoreCase(hget(entityTypeSubTypeKey, entitySubType))){
//        if (CollectionUtils.isEmpty(bpmEntityCategoryList)) {
            return "There is no such sub entity type: " + entity.getEntitySubType() + " for " + this.toWpNameString() + " in the database.";
        }
        return null;
    }


    @Override
    public StructureWeldEntity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public StructureWeldEntity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, StructureWeldEntity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        structureWeldEntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );

        // 删除实体需要同时删除对应的在hierarchy_node和project_node里的数据(deleted设置为true)
        // 删除实体可能关联的层级信息
//        hierarchyService.delete(operator, project, entity.getOrgId(), entity.getId());
//        planService.updateStatusOfWBSOfDeletedEntity(project.getProjId(), "STRUCT_WELD_JOINT", entity.getId());
    }

    @Override
    public void deleteNoneHierarchyEntity(Long noneHierarchyEntityId) {
        structureWeldEntityRepository.deleteById(noneHierarchyEntityId);
    }


    /**
     * 设定焊口的材料分组
     */
    private void setWeldMaterialGroup(Set<EntitySubTypeRule> weldEntitySubTypeRules,
                                      Project project,
                                      WBSEntityBase entity) {
        Long projectId = project.getId();

//
//        //fftj 应该使用 getMaterial1 已修改
//        MaterialCodeAliasGroup materialCode =
//            materialCodeAliasGroupRepository.findGroupByMaterialCodeAliasAndOrgIdAndProjectIdAndDeletedIsFalse(
//                ((StructureWeldEntity) entity).getMaterial1(),
//                project.getOrgId(),
//                projectId).orElse(null);
//
//
//        // 导入焊口时，根据第一个部件的材质代码取得焊口对应的材料分组代码
//        // 即使两个部件的材质代码不一样，也按第一个部件的材质代码来取焊口对应的材料分组代码
//        if (materialCode != null) {
//            ((StructureWeldEntity) entity).setMaterialGroupCode(materialCode.getGroupCode());
//        }

        EntitySubTypeRule rule = getWeldEntityTypeRuleBySetting((StructureWeldEntity) entity, weldEntitySubTypeRules);
        if (rule != null) {
            // 实体类型
            ((StructureWeldEntity) entity).setWeldEntityType(rule.getEntityType());
//
//            // 壁厚等级
//            if (rule.getThicknessRequired()) {
//                if (StringUtils.isEmpty(((StructureWeldEntity) entity).getThickness1())) {
//                    throw new ValidationError("thickness is INVALID"); // TODO thickness 2
//                }
//                String thicknessStr1 = ((StructureWeldEntity) entity).getThickness1();
//                thicknessStr1 = thicknessStr1.replaceAll("(S)(CH|-)?(\\d+)s?", "$1-$3"); //ftjftj
//                thicknessStr1 = thicknessStr1.replaceAll("S-(STD|XS|XXS|XXXS)", "$1");
//                Matcher matcher = THICKNESS.matcher(thicknessStr1);
//
//                if (matcher.find()) {
//                    ((StructureWeldEntity) entity).setThickness1(matcher.group());
//                } else {
//                    throw new ValidationError("thickness is INVALID"); // TODO
//                }
//            }
//
            // 实体业务类型
            if (rule.getEntitySubType() != null
                && rule.getEntitySubType().getEntityBusinessType() != null) {
                ((StructureWeldEntity) entity).setEntityBusinessType(
                    rule.getEntitySubType().getEntityBusinessType().getNameEn());
            }

        } else {
            throw new ValidationError("weld has NOT MATCHED entity type rule."); // TODO
        }
    }

    /**
     * 设置焊口实体类型。
     *
     * @param weldEntity 结构焊口实体
     * @param rules      焊口实体设定规则
     * @return EntityCategoryRule 实体类型设置规则
     */
    private EntitySubTypeRule getWeldEntityTypeRuleBySetting(
        StructureWeldEntity weldEntity,
        Set<EntitySubTypeRule> rules) {

        // 焊口
        for (EntitySubTypeRule rule : rules) {
            if (!StringUtils.isEmpty(rule.getValue1()) && !StringUtils.isEmpty(rule.getValue2())) {
                if (weldEntity.getWeldType().trim().equalsIgnoreCase(rule.getValue2().trim())
                    && weldEntity.getStage().trim().equalsIgnoreCase(rule.getValue1().trim())) {
                    return rule;
                }
            }
        }
        return null;
    }

    /**
     * 设定 根据 焊口规则设定 焊口类型
     *
     * @return
     */
    private StructureWeldEntity setStructureWeldEntityByRules(StructureWeldEntity entity, Set<EntitySubTypeRule> entitySubTypeRules) {
        EntitySubTypeRule rule = getWeldEntityTypeRuleBySetting(entity, entitySubTypeRules);
        if (rule != null) {
            // 实体类型
            entity.setWeldEntityType(rule.getEntityType());

            // 实体业务类型
            if (rule.getEntitySubType() != null
                && rule.getEntitySubType().getEntityBusinessType() != null) {
                entity.setEntityBusinessType(
                    rule.getEntitySubType().getEntityBusinessType().getNameEn());
            }

        } else {
            throw new ValidationError("weld has NOT MATCHED entity type rule."); // TODO
        }
        return entity;
    }

    @Override
    public String toString() {
        return "STRUCT_WELD_JOINT";
    }


    private String toWpNameString() {
        return "STRUCT_WELD_JOINT";
    }


    /**
     * 设置 WP PN ID号，更新WBS-ENTRY
     */
    public void setWpIdsAndWbs(Long projectId, Long entityId) {
        structureWeldEntityRepository.updateWpAndProjectNodeIds(projectId, entityId);

    }

    public EntitySubTypeRule getEntityRule(
        WBSEntityImportSheetConfigBuilder.SheetConfig config,
        Row row,
        Set<EntitySubTypeRule> rules
    ) {
        return null;
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
        int colIndex = 4;

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
    public StructureWeldEntity checkRule(Long orgId, Long projectId, StructureWeldEntity entity, EntitySubTypeRule rule) {
        entity.setWeldEntityType(rule.getSubType());

//        if (rule.getThicknessRequired()) {
//            if (StringUtils.isEmpty(entity.getThickness())) {
//                throw new ValidationError("thickness is INVALID");
//            }
//            String thicknessStr = entity.getThickness();
//            thicknessStr = thicknessStr.replaceAll("(S)(CH|-)?(\\d+)s?", "$1-$3");
//            thicknessStr = thicknessStr.replaceAll("S-(STD|XS|XXS|XXXS)", "$1");
//            Matcher matcher = THICKNESS.matcher(thicknessStr);
//
//            if (matcher.find()) {
//                entity.setThickness(matcher.group());
//            } else {
//                throw new ValidationError("thickness is INVALID");
//            }
//        }

        if (rule.getEntitySubType() != null
            && rule.getEntitySubType().getEntityBusinessType() != null) {
            entity.setEntityBusinessType(
                rule.getEntitySubType().getEntityBusinessType().getNameEn());
        }

        if (rule.getParentType().equals(WBSEntityType.WP02.name())) {
            Wp02Entity wp02Entity = wp02EntityRepository.findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(orgId, projectId, entity.getParentNo()).orElse(null);
            if (wp02Entity == null) {
                throw new ValidationError("NO WP02 PARENT for this weld");
            }
            entity.setParentNo(wp02Entity.getNo());
        }

        if (rule.getParentType().equals(WBSEntityType.WP03.name())) {

            Optional<Wp03Entity> wp03Entity = wp03EntityRepository.findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(orgId, projectId, entity.getParentNo());
            if (!wp03Entity.isPresent()) {
                throw new ValidationError("NO WP03 PARENT for this weld");
            }
            entity.setParentNo(wp03Entity.get().getNo());
        }

        if (rule.getParentType().equals(WBSEntityType.WP04.name())) {

            Optional<Wp04Entity> wp04Entity = wp04EntityRepository.findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(orgId, projectId, entity.getParentNo());
            if (!wp04Entity.isPresent()) {
                throw new ValidationError("NO WP04 PARENT for this weld");
            }
            entity.setParentNo(wp04Entity.get().getNo());
        }
        return entity;
    }

    @Override
    public StructureWeldEntity generateQrCode(StructureWeldEntity entity) {
        if (entity.getQrCode() == null) {
            String qrCode = QrcodePrefixType.STRUCTURE_WELD.getCode() + StringUtils.generateShortUuid();
            entity.setQrCode(qrCode);
        }

        return entity;
    }
}
