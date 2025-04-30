package com.ose.tasks.domain.model.service.builder.structure;
import com.ose.dto.OperatorDTO;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp04EntityRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp05EntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder;
import com.ose.tasks.domain.model.service.wbs.structure.Wp05EntityInterface;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
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
 * 结构Part实体构建器。
 */
@Component
public class Wp05EntityBuilder extends StringRedisService implements WBSEntityBuilder<Wp05Entity> {

    //Wp05EntityInterface
    private final Wp05EntityInterface wp05EntityService;

    // 项目层级机构管理服务接口
    private final HierarchyInterface hierarchyService;

    // 计划管理服务接口
    private final PlanInterface planService;

    private final Wp04EntityRepository wp04EntityRepository;
    private final EntitySubTypeInterface entitySubTypeService;


    //bpm_delivery_entity
//    private final BpmDeliveryEntityRepository bpmDeliveryEntityRepository;

    //bpm_cutting_entity
//    private final BpmCuttingEntityRepository bpmCuttingEntityRepository;

    private final Wp05EntityRepository wp05EntityRepository;

    private final ProjectNodeRepository projectNodeRepository;


    @Autowired
    public Wp05EntityBuilder(Wp05EntityInterface wp05EntityService,
                             HierarchyInterface hierarchyService,
                             PlanInterface planService,
                             Wp04EntityRepository wp04EntityRepository, EntitySubTypeInterface entitySubTypeService,
//                             BpmDeliveryEntityRepository bpmDeliveryEntityRepository,
//                             BpmCuttingEntityRepository bpmCuttingEntityRepository,
                             StringRedisTemplate stringRedisTemplate,
                             Wp05EntityRepository wp05EntityRepository,
                             ProjectNodeRepository projectNodeRepository) {
        super(stringRedisTemplate);
        this.wp05EntityService = wp05EntityService;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.wp04EntityRepository = wp04EntityRepository;
        this.entitySubTypeService = entitySubTypeService;
//        this.bpmDeliveryEntityRepository = bpmDeliveryEntityRepository;
//        this.bpmCuttingEntityRepository = bpmCuttingEntityRepository;
        this.wp05EntityRepository = wp05EntityRepository;
        this.projectNodeRepository = projectNodeRepository;
    }

    @Override
    public WBSImportLogDTO<Wp05Entity> buildWbs(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator){

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

        String entitySubType = entity.getEntitySubType();

        //如果实体子类型没填写，报错（其实这个判断没必要）
        if (entitySubType == null) return "The sub entity type for entity" + entity.getNo() + "doesn't exist.";
        String entityTypeSubTypeKey = String.format(RedisKey.ENTITY_TYPE_SUB_TYPE.getDisplayName(), entity.getProjectId().toString());

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
    public Wp05Entity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public Wp05Entity build(WBSEntityImportSheetConfigBuilder.SheetConfig config, Project project, Row row, Wp05Entity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        wp05EntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );

        // 删除实体需要同时删除对应的在hierarchy_node和project_node里的数据(deleted设置为true)
        // 删除实体可能关联的层级信息
//        bpmDeliveryEntityRepository.updateDeliveryEntityStatus(EntityStatus.DELETED, project.getProjId(), entity.getId());
//        bpmCuttingEntityRepository.updateCuttingEntityStatus(EntityStatus.DELETED, project.getProjId(), entity.getId());
//        planService.updateStatusOfWBSOfDeletedEntity(project.getProjId(), "WP05", entity.getId());
    }

    @Override
    public void deleteNoneHierarchyEntity(Long noneHierarchyEntityId) {
        wp05EntityRepository.deleteById(noneHierarchyEntityId);
    }

    @Override
    public String toString() {
        return "WP05";
    }

    private String toWpNameString() {
        return "WP05";
    }


    /**
     * 设置 WP PN ID号，更新WBS-ENTRY
     */
    public void setWpIdsAndWbs(Long projectId, Long entityId) {
        wp05EntityRepository.updateWpAndProjectNodeIds(projectId, entityId);

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
    public Wp05Entity checkRule(Long orgId, Long projectId, Wp05Entity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public Wp05Entity generateQrCode(Wp05Entity entity) {
        if (entity.getQrCode() == null) {
            String qrCode = QrcodePrefixType.WP05.getCode() + StringUtils.generateShortUuid();
            entity.setQrCode(qrCode);
        }

        return entity;
    }
}
