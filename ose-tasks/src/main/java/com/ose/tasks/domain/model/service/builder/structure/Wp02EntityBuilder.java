package com.ose.tasks.domain.model.service.builder.structure;

import com.ose.dto.OperatorDTO;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp02EntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.structureWbs.Wp02EntryCriteriaDTO;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp02Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp02EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp05Entity;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.QrcodePrefixType;
import com.ose.vo.RedisKey;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 结构Section实体构建器。
 */
@Component
public class Wp02EntityBuilder extends StringRedisService implements WBSEntityBuilder<Wp02Entity> {


    // Module 实体操作服务
    private final BaseWBSEntityInterface<Wp02EntityBase, Wp02EntryCriteriaDTO> wp02EntityService;
    private final EntitySubTypeInterface entitySubTypeService;

    private final Wp02EntityRepository wp02EntityRepository;

    private final ProjectNodeRepository projectNodeRepository;

    @Autowired
    public Wp02EntityBuilder(BaseWBSEntityInterface<Wp02EntityBase,
        Wp02EntryCriteriaDTO> wp02EntityService,
                             HierarchyInterface hierarchyService,
                             PlanInterface planService,
                             EntitySubTypeInterface entitySubTypeService, Wp02EntityRepository wp02EntityRepository,
                             StringRedisTemplate stringRedisTemplate,
                             BpmEntitySubTypeRepository bpmEntityCategoryRepository,
                             ProjectNodeRepository projectNodeRepository) {
        super(stringRedisTemplate);
        this.wp02EntityService = wp02EntityService;
        this.entitySubTypeService = entitySubTypeService;
        this.wp02EntityRepository = wp02EntityRepository;
        this.projectNodeRepository = projectNodeRepository;
    }

    @Override
    public WBSImportLogDTO<Wp02Entity> buildWbs(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator){

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
        String entityTypeSubTypeKey = String.format(RedisKey.ENTITY_TYPE_SUB_TYPE.getDisplayName(), entity.getProjectId().toString());

        //如果实体子类型没填写，报错（其实这个判断没必要）
        if (entitySubType == null) return "The sub entity type for entity" + entity.getNo() + "doesn't exist.";

        //根据实体大类和实体子类型去查找数据库，如果找不到，就报错
        if(toWpNameString().equalsIgnoreCase(hget(entityTypeSubTypeKey, entitySubType))){

//            List<BpmEntitySubType> bpmEntityCategoryList =
//            bpmEntityCategoryRepository.findEntityCategoriesByEntityType(this.toWpNameString(), entitySubType, entity.getProjectId());
//        if (CollectionUtils.isEmpty(bpmEntityCategoryList)) {
            return "There is no such sub entity type: " + entity.getEntitySubType() + " for " + this.toWpNameString() + " in the database.";
        }
        return null;
    }


    @Override
    public Wp02Entity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public Wp02Entity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, Wp02Entity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        wp02EntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );

        // 删除实体需要同时删除对应的在hierarchy_node和project_node里的数据(deleted设置为true)
        // 删除实体可能关联的层级信息
//        planService.updateStatusOfWBSOfDeletedEntity(project.getProjId(), "WP02", entity.getId());
    }

    @Override
    public void deleteNoneHierarchyEntity(Long noneHierarchyEntityId) {
        Wp02Entity wp02Entity = wp02EntityRepository.findById(noneHierarchyEntityId).orElse(null);
        if(wp02Entity!=null) {
            wp02EntityRepository.deleteById(noneHierarchyEntityId);
        }
    }

    @Override //node type
    public String toString() {
        return "WP02";
    }

    private String toWpNameString() {
        return "WP02";
    }


    /**
     * 设置 WP PN ID号，更新WBS-ENTRY
     */
    public void setWpIdsAndWbs(Long projectId, Long entityId){
        wp02EntityRepository.updateWpAndProjectNodeIds(projectId, entityId);

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
    public Wp02Entity checkRule(Long orgId, Long projectId, Wp02Entity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public Wp02Entity generateQrCode(Wp02Entity entity) {
        if (entity.getQrCode() == null) {
            String qrCode = QrcodePrefixType.WP02.getCode() + StringUtils.generateShortUuid();
            entity.setQrCode(qrCode);
        }

        return entity;
    }

}
