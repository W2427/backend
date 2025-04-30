package com.ose.tasks.domain.model.service.builder.structure;

import com.ose.dto.OperatorDTO;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp03EntityRepository;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder.SheetConfig;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.structureWbs.Wp03EntryCriteriaDTO;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp03Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp03EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp05Entity;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.tasks.vo.bpm.CategoryRuleType;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.QrcodePrefixType;
import com.ose.vo.RedisKey;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 结构Panel实体构建器。
 */
@Component
public class Wp03EntityBuilder extends StringRedisService implements WBSEntityBuilder<Wp03Entity> {


    // WP03 实体操作服务
    private final BaseWBSEntityInterface<Wp03EntityBase, Wp03EntryCriteriaDTO> wp03EntityService;

    private final EntitySubTypeInterface entitySubTypeService;

    private final Wp03EntityRepository wp03EntityRepository;

    private final ProjectNodeRepository projectNodeRepository;

    @Autowired
    public Wp03EntityBuilder(BaseWBSEntityInterface<Wp03EntityBase, Wp03EntryCriteriaDTO> wp03EntityService,
                             StringRedisTemplate stringRedisTemplate,
                             EntitySubTypeInterface entitySubTypeService, Wp03EntityRepository wp03EntityRepository,
                             ProjectNodeRepository projectNodeRepository) {
        super(stringRedisTemplate);
        this.wp03EntityService = wp03EntityService;
        this.entitySubTypeService = entitySubTypeService;
        this.wp03EntityRepository = wp03EntityRepository;
        this.projectNodeRepository = projectNodeRepository;
    }

    @Override
    public WBSImportLogDTO<Wp03Entity> buildWbs(SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator){

        WBSImportLogDTO wbsImportLogDTO = buildDefault(config,project,row, columnConfigs,operator);
        WBSEntityBase entity = wbsImportLogDTO.getWbsEntityBase();
        BpmEntitySubType best = entitySubTypeService.getEntitySubTypeByWbs(project.getId(),entity.getEntityType(),entity.getEntitySubType());
        if(best != null) entity.setDiscipline(best.getDiscipline());
        wbsImportLogDTO.setWbsEntityBase(entity);
        return wbsImportLogDTO;
    }

    @Override
    public <T extends WBSEntityBase> String checkProperty(T entity, Set<String> entityTypes, Set<EntitySubTypeRule> entitySubTypeRules) {

        String entitySubType = entity.getEntitySubType();

        //如果实体子类型没填写，报错（其实这个判断没必要）
        if (entitySubType == null) return "The sub entity type for entity" + entity.getNo() + "doesn't exist.";
        String entityTypeSubTypeKey = String.format(RedisKey.ENTITY_TYPE_SUB_TYPE.getDisplayName(), entity.getProjectId().toString());

        //根据实体大类和实体子类型去查找数据库，如果找不到，就报错
        if (toWpNameString().equalsIgnoreCase(hget(entityTypeSubTypeKey, entitySubType))) {
//            List<BpmEntitySubType> bpmEntityCategoryList =
//            bpmEntityCategoryRepository.findEntityCategoriesByEntityType(this.toWpNameString(), entitySubType, entity.getProjectId());
//        if (CollectionUtils.isEmpty(bpmEntityCategoryList)) {
            return "There is no such sub entity type: " + entity.getEntitySubType() + " for " + this.toWpNameString() + " in the database.";
        }
        return null;
    }


    @Override
    public Wp03Entity build(SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public Wp03Entity build(SheetConfig config, Project project, Row row, Wp03Entity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        wp03EntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );

        // 删除实体需要同时删除对应的在hierarchy_node和project_node里的数据(deleted设置为true)
        // 删除实体可能关联的层级信息
//        planService.updateStatusOfWBSOfDeletedEntity(project.getProjId(), "WP03", entity.getId());
    }

    @Override
    public void deleteNoneHierarchyEntity(Long noneHierarchyEntityId) {
        Wp03Entity wp03Entity = wp03EntityRepository.findById(noneHierarchyEntityId).orElse(null);
        if (wp03Entity != null) {
            wp03EntityRepository.deleteById(noneHierarchyEntityId);
        }
    }

    @Override
    public String toString() {
        return "WP03";
    }

    private String toWpNameString() {
        return "WP03";
    }

    /**
     * 设置 WP PN ID号，更新WBS-ENTRY
     */
    public void setWpIdsAndWbs(Long projectId, Long entityId) {
        wp03EntityRepository.updateWpAndProjectNodeIds(projectId, entityId);

    }

    /**
     * 取得实体规则
     */
    @Override
    public EntitySubTypeRule getRule(
        Long orgId,
        Long projectId,
        SheetConfig config,
        Row row,
        Set<EntitySubTypeRule> entitySubTypeRules
    ) {
        EntitySubTypeRule entitySubTypeRule = null;
        int colIndex = config.getMaxParents() + 2;

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
    public Wp03Entity checkRule(Long orgId, Long projectId, Wp03Entity entity, EntitySubTypeRule rule) {
        return null;
    }

    /**
     * 取得父级 Map<String, String>
     *
     * @param subTypeRuleType
     * @return
     */
    public Map<String, String> getParentNo(
        CategoryRuleType subTypeRuleType,
        Row row,
        EntitySubTypeRule rule
    ) {
        Map<String, String> parentNoMap = new HashMap<>();
        // 读取上级节点编号
        String parentNodeNo = StringUtils.trim(WorkbookUtils.readAsString(row, 0));
        if (!StringUtils.isEmpty(parentNodeNo)) {
            parentNoMap.put("PIPING", parentNodeNo);
            parentNoMap.put("STRUCTURE", parentNodeNo);
            parentNoMap.put("ELECTRICAL", parentNodeNo);
            parentNoMap.put("MECHANICAL", parentNodeNo);
        }
        return parentNoMap;
    }

    @Override
    public Wp03Entity generateQrCode(Wp03Entity entity) {
        if (entity.getQrCode() == null) {
            String qrCode = QrcodePrefixType.WP03.getCode() + StringUtils.generateShortUuid();
            entity.setQrCode(qrCode);
        }

        return entity;
    }
}
