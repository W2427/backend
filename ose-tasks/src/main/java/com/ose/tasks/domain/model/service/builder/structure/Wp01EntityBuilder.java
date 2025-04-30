package com.ose.tasks.domain.model.service.builder.structure;
import com.ose.dto.OperatorDTO;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntitySubTypeRepository;
import com.ose.tasks.domain.model.repository.wbs.structure.Wp01EntityRepository;
import com.ose.tasks.domain.model.service.HierarchyInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.builder.WBSEntityBuilder;
import com.ose.tasks.domain.model.service.plan.PlanInterface;
import com.ose.tasks.domain.model.service.sheet.WBSEntityImportSheetConfigBuilder.SheetConfig;
import com.ose.tasks.domain.model.service.wbs.BaseWBSEntityInterface;
import com.ose.tasks.dto.structureWbs.Wp01EntryCriteriaDTO;
import com.ose.tasks.dto.wbs.WBSImportLogDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp01Entity;
import com.ose.tasks.entity.wbs.structureEntity.Wp01EntityBase;
import com.ose.tasks.entity.wbs.structureEntity.Wp05Entity;
import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.util.WorkbookUtils;
import com.ose.vo.QrcodePrefixType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Module 实体构建器。
 */
@Component
public class Wp01EntityBuilder extends StringRedisService implements WBSEntityBuilder<Wp01Entity> {


    // Module 实体操作服务
    private final BaseWBSEntityInterface<Wp01EntityBase, Wp01EntryCriteriaDTO> wp01EntityService;

    // 项目层级机构管理服务接口
    private final HierarchyInterface hierarchyService;

    // 计划管理服务接口
    private final PlanInterface planService;
    private final EntitySubTypeInterface entitySubTypeService;

    private final ProjectNodeRepository projectNodeRepository;

    private final BpmEntitySubTypeRepository bpmEntityCategoryRepository;

    private final Wp01EntityRepository wp01EntityRepository;

    /**
     * 构造方法
     *
     * @param wp01EntityService           wp01 service
     * @param hierarchyService            hierarchy Service
     * @param planService                 plan service
     * @param entitySubTypeService
     * @param projectNodeRepository
     * @param bpmEntityCategoryRepository entityCategoryRepositotry
     * @param wp01EntityRepository
     */
    @Autowired
    public Wp01EntityBuilder(BaseWBSEntityInterface<Wp01EntityBase, Wp01EntryCriteriaDTO> wp01EntityService,
                             HierarchyInterface hierarchyService,
                             PlanInterface planService, StringRedisTemplate stringRedisTemplate,
                             EntitySubTypeInterface entitySubTypeService, ProjectNodeRepository projectNodeRepository, BpmEntitySubTypeRepository bpmEntityCategoryRepository,
                             Wp01EntityRepository wp01EntityRepository) {
        super(stringRedisTemplate);
        this.wp01EntityService = wp01EntityService;
        this.hierarchyService = hierarchyService;
        this.planService = planService;
        this.entitySubTypeService = entitySubTypeService;
        this.projectNodeRepository = projectNodeRepository;
        this.bpmEntityCategoryRepository = bpmEntityCategoryRepository;
        this.wp01EntityRepository = wp01EntityRepository;
    }

    @Override
    public Wp01Entity build(SheetConfig config, Project project, Row row) {
        return null;
    }

    @Override
    public Wp01Entity build(SheetConfig config, Project project, Row row, Wp01Entity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public WBSImportLogDTO<Wp01Entity> buildWbs(SheetConfig config, Project project, Row row, List<ColumnImportConfig> columnConfigs, OperatorDTO operator){

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

        //根据实体大类和实体子类型去查找数据库，如果找不到，就报错
        List<BpmEntitySubType> bpmEntityCategoryList =
            bpmEntityCategoryRepository.findEntityCategoriesByEntityType(this.toWpNameString(), entitySubType, entity.getProjectId());
        if (CollectionUtils.isEmpty(bpmEntityCategoryList)) {
            return "There is no such sub entity type: " + entity.getEntitySubType() + " for " + this.toWpNameString() + " in the database.";
        }
        return null;
    }

    @Override
    public <T extends WBSEntityBase> void delete(T entity, OperatorDTO operator, Project project) {
        wp01EntityService.delete(
            operator,
            entity.getOrgId(),
            project,
            entity.getId()
        );

        // 删除实体需要同时删除对应的在hierarchy_node和project_node里的数据(deleted设置为true)
        // 删除实体可能关联的层级信息
//        planService.updateStatusOfWBSOfDeletedEntity(project.getProjId(), "WP01", entity.getId());
    }

    @Override
    public void deleteNoneHierarchyEntity(Long noneHierarchyEntityId) {
        Wp01Entity wp01Entity = wp01EntityRepository.findById(noneHierarchyEntityId).orElse(null);
        if (wp01Entity !=null){
        wp01EntityRepository.deleteById(noneHierarchyEntityId);
        }
    }

    @Override //node type
    public String toString() {
        return "WP01";
    }


    private String toWpNameString() {
        return "WP01";
    }

    /**
     * 设置 WP PN ID号，更新WBS-ENTRY
     */
    public void setWpIdsAndWbs(Long projectId, Long entityId){
        wp01EntityRepository.updateWpAndProjectNodeIds(projectId, entityId);

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
    public Wp01Entity checkRule(Long orgId, Long projectId, Wp01Entity entity, EntitySubTypeRule rule) {
        return null;
    }

    @Override
    public Wp01Entity generateQrCode(Wp01Entity entity) {
        if (entity.getQrCode() == null) {
            String qrCode = QrcodePrefixType.WP01.getCode() + StringUtils.generateShortUuid();
            entity.setQrCode(qrCode);
        }

        return entity;
    }
}
