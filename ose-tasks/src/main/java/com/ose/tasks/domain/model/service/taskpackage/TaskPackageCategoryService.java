package com.ose.tasks.domain.model.service.taskpackage;

import com.ose.dto.BaseDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ConflictError;
import com.ose.exception.DuplicatedError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityTypeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmProcessRepository;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.domain.model.repository.setting.DisciplineRepository;
import com.ose.tasks.domain.model.repository.setting.FuncPartRepository;
import com.ose.tasks.domain.model.repository.setting.HierarchyTypeRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageBasicRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryProcessRelationBasicRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryProcessRelationRepository;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageCategoryRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.dto.taskpackage.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.setting.Discipline;
import com.ose.tasks.entity.setting.FuncPart;
import com.ose.tasks.entity.setting.HierarchyType;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.tasks.entity.taskpackage.*;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.util.ValueUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;

/**
 * 任务包类型管理服务。
 */
@Component
public class TaskPackageCategoryService implements TaskPackageCategoryInterface {

    private final HierarchyTypeRepository hierarchyTypeRepository;
    private final TaskPackageCategoryRepository taskPackageCategoryRepository;

    private final FuncPartRepository funcPartRepository;

    private final DisciplineRepository disciplineRepository;

    private final ProjectInterface projectService;

    private final TaskPackageBasicRepository taskPackageBasicRepository;


    private final BpmProcessRepository processRepository;

    private final BpmEntityTypeRepository bpmEntityTypeRepository;


    private final TaskPackageCategoryProcessRelationBasicRepository taskPackageCategoryProcessRelationBasicRepository;
    private final TaskPackageCategoryProcessRelationRepository taskPackageCategoryProcessRelationRepository;
    private final EntitySubTypeRuleRepository entityCategoryRuleRepository;


    private final WBSEntryRepository wbsEntryRepository;

    private final WBSEntryStateRepository wbsEntryStateRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public TaskPackageCategoryService(
        HierarchyTypeRepository hierarchyTypeRepository, TaskPackageCategoryRepository taskPackageCategoryRepository,
        FuncPartRepository funcPartRepository,
        DisciplineRepository disciplineRepository,
        ProjectInterface projectService, TaskPackageBasicRepository taskPackageBasicRepository,
        BpmProcessRepository processRepository,
        BpmEntityTypeRepository bpmEntityTypeRepository,
        TaskPackageCategoryProcessRelationBasicRepository taskPackageCategoryProcessRelationBasicRepository,
        TaskPackageCategoryProcessRelationRepository taskPackageCategoryProcessRelationRepository,
        EntitySubTypeRuleRepository entityCategoryRuleRepository, WBSEntryRepository wbsEntryRepository,
        WBSEntryStateRepository wbsEntryStateRepository) {
        this.hierarchyTypeRepository = hierarchyTypeRepository;
        this.taskPackageCategoryRepository = taskPackageCategoryRepository;
        this.funcPartRepository = funcPartRepository;
        this.disciplineRepository = disciplineRepository;
        this.projectService = projectService;
        this.taskPackageBasicRepository = taskPackageBasicRepository;
        this.processRepository = processRepository;
        this.bpmEntityTypeRepository = bpmEntityTypeRepository;
        this.taskPackageCategoryProcessRelationBasicRepository = taskPackageCategoryProcessRelationBasicRepository;
        this.taskPackageCategoryProcessRelationRepository = taskPackageCategoryProcessRelationRepository;
        this.entityCategoryRuleRepository = entityCategoryRuleRepository;
        this.wbsEntryRepository = wbsEntryRepository;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
    }

    /**
     * 创建任务包类型。
     *
     * @param operator               操作者信息
     * @param orgId                  组织 ID
     * @param projectId              项目 ID
     * @param taskPackageCategoryDTO 任务包类型信息
     * @return 任务包类型数据实体
     */
    @Override
    @Transactional
    public TaskPackageCategory create(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final TaskPackageCategoryCreateDTO taskPackageCategoryDTO
    ) {

        TaskPackageCategory taskPackageCategory = BeanUtils.copyProperties(
            taskPackageCategoryDTO,
            new TaskPackageCategory()
        );





        taskPackageCategory.setName(StringUtils.trim(taskPackageCategory.getName()));

        if (taskPackageCategoryRepository
            .findByProjectIdAndNameAndDeletedIsFalse(projectId, taskPackageCategory.getName())
            .isPresent()
        ) {
            throw new DuplicatedError("error.task-package-category.duplicate-name");
        }

        taskPackageCategory.setOrgId(orgId);
        taskPackageCategory.setProjectId(projectId);
        taskPackageCategory.setCreatedAt();
        taskPackageCategory.setCreatedBy(operator.getId());
        taskPackageCategory.setLastModifiedAt();
        taskPackageCategory.setLastModifiedBy(operator.getId());
        taskPackageCategory.setStatus(EntityStatus.ACTIVE);

        return taskPackageCategoryRepository.save(taskPackageCategory);
    }

    /**
     * 查询任务包类型。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包类型分页数据
     */
    @Override
    public Page<TaskPackageCategory> search(
        final Long orgId,
        final Long projectId,
        final TaskPackageCategoryCriteriaDTO criteriaDTO,
        final Pageable pageable
    ) {

/*        String keyword = null;

        if (!StringUtils.isEmpty(criteriaDTO.getKeyword())) {
            keyword = String.format(
                "%s%%",
                SQLUtils.escapeLike(criteriaDTO.getKeyword())
            );
        }

        String entityType = criteriaDTO.getEntityType();

        String discipline = criteriaDTO.getDiscipline();*/

















        return taskPackageCategoryRepository.search(orgId, projectId, criteriaDTO, pageable);

    }

    /**
     * 取得任务包类型详细信息。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @return 任务包类型
     */
    @Override
    public TaskPackageCategory get(
        final Long orgId,
        final Long projectId,
        final Long categoryId
    ) {
        return taskPackageCategoryRepository
            .findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(orgId, projectId, categoryId)
            .orElse(null);
    }

    /**
     * 取得任务包类型详细信息。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @param version    更新版本号
     * @return 任务包类型
     */
    private TaskPackageCategory get(
        final Long orgId,
        final Long projectId,
        final Long categoryId,
        final Long version
    ) {

        TaskPackageCategory taskPackageCategory = get(orgId, projectId, categoryId);

        if (taskPackageCategory == null) {
            throw new NotFoundError();
        }

        if (!taskPackageCategory.getVersion().equals(version)) {
            throw new ConflictError();
        }

        return taskPackageCategory;
    }

    /**
     * 更新任务包类型。
     *
     * @param operator               操作者信息
     * @param orgId                  组织 ID
     * @param projectId              项目 ID
     * @param categoryId             任务包分类 ID
     * @param version                任务包更新版本号
     * @param taskPackageCategoryDTO 任务包类型信息
     * @return 任务包类型数据实体
     */
    @Override
    @Transactional
    public TaskPackageCategory update(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long categoryId,
        final Long version,
        final TaskPackageCategoryUpdateDTO taskPackageCategoryDTO
    ) {

        TaskPackageCategory taskPackageCategory = get(orgId, projectId, categoryId, version);

        ValueUtils.notNull(taskPackageCategoryDTO.getName(), taskPackageCategory::setName);
        ValueUtils.notNull(taskPackageCategoryDTO.getDescription(), taskPackageCategory::setDescription);
        ValueUtils.notNull(taskPackageCategoryDTO.getEntityType(), taskPackageCategory::setEntityType);
        ValueUtils.notNull(taskPackageCategoryDTO.getDiscipline(), taskPackageCategory::setDiscipline);
        ValueUtils.notNull(taskPackageCategoryDTO.getMemo(), taskPackageCategory::setMemo);

        taskPackageCategory.setLastModifiedBy(operator.getId());
        taskPackageCategory.setLastModifiedAt();

        return taskPackageCategoryRepository.save(taskPackageCategory);
    }

    /**
     * 删除任务包类型。
     *
     * @param operator   操作者信息
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包分类 ID
     * @param version    任务包分类更新版本号
     */
    @Override
    @Transactional
    public void delete(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long categoryId,
        final Long version
    ) {

        TaskPackageCategory taskPackageCategory = get(orgId, projectId, categoryId, version);


        if (taskPackageBasicRepository
            .existsByProjectIdAndCategoryIdAndDeletedIsFalse(projectId, categoryId)) {
            throw new BusinessError("error.task-package-category.task-package-exists");
        }

        taskPackageCategory.setStatus(EntityStatus.DELETED);
        taskPackageCategory.setDeletedBy(operator.getId());
        taskPackageCategory.setDeletedAt();


        taskPackageCategoryRepository.save(taskPackageCategory);


        taskPackageCategoryProcessRelationBasicRepository
            .deleteByProjectIdAndCategoryId(projectId, categoryId);
    }

    /**
     * 从实体子类型数据实体列表中过滤出指定实体类型的子实体类型。
     *
     * @param rootEntityType        根实体类型
     * @param entitySubTypeEntities 实体子类型数据实体列表
     * @return 实体子类型数据实体列表
     */
    private List<BpmEntitySubType> getEntitySubTypes(
        final String rootEntityType,
        final List<BpmEntitySubType> entitySubTypeEntities,
        final Long orgId,
        final Long projectId
    ) {

        List<BpmEntitySubType> result = new ArrayList<>();

        if (entitySubTypeEntities == null || entitySubTypeEntities.size() == 0) {
            return result;
        }

        BpmEntityType entityTypeEntity;
        String entityType;

        for (BpmEntitySubType entitySubTypeEntity : entitySubTypeEntities) {

            entityTypeEntity = entitySubTypeEntity.getEntityType();

            if (entityTypeEntity == null) {
                continue;
            }


            if (entityTypeEntity.getNameEn().equals("STRUCT_WELD_JOINT") || entityTypeEntity.getNameEn().equals("WELD_JOINT")) {
                Set<EntitySubTypeRule> entityCategoryRules = entityCategoryRuleRepository.findByEntityTypeAndOrgIdAndProjectIdAndDeletedIsFalseOrderByRuleOrder(entityTypeEntity.getNameEn(),orgId,projectId);
                if(entityCategoryRules.size() == 0){
                    continue;
                }
                EntitySubTypeRule entityCategoryRule = entityCategoryRules
                    .iterator().next();
                entityType = entityCategoryRule.getParentType();
            } else {
                entityType = entityTypeEntity.getNameEn();
            }

            BpmEntityType bet = bpmEntityTypeRepository.findByProjectIdAndNameEnAndStatus(projectId, rootEntityType, EntityStatus.ACTIVE);

            if (!rootEntityType.equalsIgnoreCase(entityType) && !bet.isParentOf(entityType)) {
                continue;
            }

            result.add(entitySubTypeEntity);
        }

        return result;
    }

    /**
     * 创建任务包类型-工序关系。
     *
     * @param operator    操作者信息
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param categoryId  任务包类型 ID
     * @param relationDTO 关系数据
     * @return 关系数据
     */
    @Override
    @Transactional
    public TaskPackageCategoryProcessRelationBasic addProcess(
        final OperatorDTO operator,
        final Long orgId,
        final Long projectId,
        final Long categoryId,
        final TaskPackageCategoryProcessRelationCreateDTO relationDTO
    ) {

        TaskPackageCategory taskPackageCategory = get(orgId, projectId, categoryId);

        if (taskPackageCategory == null) {
            throw new NotFoundError();
        }

        final Long processId = relationDTO.getProcessId();

        TaskPackageCategoryProcessRelationBasic processRelation = taskPackageCategoryProcessRelationBasicRepository
            .findFirstByProjectIdAndProcessId(projectId, processId)
            .orElse(null);


        if (processRelation != null) {

            TaskPackageCategory category = taskPackageCategoryRepository
                .findById(processRelation.getCategoryId())
                .orElse(null);

            throw new DuplicatedError(
                "error.task-package-category.process.already-bundled",
                category == null ? processRelation.getCategoryId().toString() : category.getName()
            );
        }

        BpmProcess process = processRepository
            .findById(processId).orElse(null);

        if (process == null
            || process.getStatus() == EntityStatus.DELETED
            || !orgId.equals(process.getOrgId())
            || !projectId.equals(process.getProjectId())
        ) {
            throw new BusinessError("error.entity-process.not-found");
        }


        if (
            getEntitySubTypes(
                taskPackageCategory.getEntityType(),
                process.getEntitySubTypes(),
                orgId,
                projectId
            ).size() == 0
        ) {
            throw new BusinessError("error.task-package-category.process.entity-type-not-matched");
        }

        TaskPackageCategoryProcessRelationBasic relation = new TaskPackageCategoryProcessRelationBasic();
        relation.setOrgId(orgId);
        relation.setProjectId(projectId);
        relation.setEntitySubTypeId(categoryId);
        relation.setProcessId(processId);
        relation.setCreatedAt();
        relation.setLastModifiedAt();
        relation.setStatus(EntityStatus.ACTIVE);

        relation = taskPackageCategoryProcessRelationBasicRepository.save(relation);

        taskPackageBasicRepository
            .findByProjectIdAndCategoryIdAndDeletedIsFalse(projectId, categoryId)
            .forEach(taskPackageBasic -> {

                Set<BigInteger> wbsEntryIds = new HashSet<>();
                if (taskPackageBasic.getDiscipline() == "STRUCTURE") {
                    wbsEntryIds = wbsEntryRepository.findStructureWbsEntryByTaskPackageId(taskPackageBasic.getProjectId(), taskPackageBasic.getId());
                } else {
                    wbsEntryIds = wbsEntryRepository.findWbsEntryByTaskPackageId(taskPackageBasic.getProjectId(), taskPackageBasic.getId());
                }
                wbsEntryIds.forEach(wbsEntryId ->{
                    wbsEntryRepository.updateTaskPackageId(wbsEntryId.longValue(), taskPackageBasic.getId());
                });
            });

        return relation;
    }

    /**
     * 取得工序列表。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @param pageable   分页参数
     * @return 关系分页数据
     */
    @Override
    public Page<TaskPackageCategoryProcessRelation> getProcesses(
        final Long orgId,
        final Long projectId,
        final Long categoryId,
        final Pageable pageable
    ) {
        return taskPackageCategoryProcessRelationRepository
            .findByOrgIdAndProjectIdAndCategoryId(
                orgId,
                projectId,
                categoryId,
                pageable
            );
    }

    /**
     * 删除任务包类型-工序关系。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     */
    @Override
    @Transactional
    public void deleteProcess(
        final Long orgId,
        final Long projectId,
        final Long categoryId,
        final Long processId
    ) {

        Optional<TaskPackageCategoryProcessRelationBasic> relation =
            taskPackageCategoryProcessRelationBasicRepository
                .findFirstByProjectIdAndProcessId(projectId, processId);

        if (!relation.isPresent()) {
            throw new NotFoundError();
        }

        taskPackageCategoryProcessRelationBasicRepository.delete(relation.get());

        taskPackageBasicRepository
            .findByProjectIdAndCategoryIdAndDeletedIsFalse(projectId, categoryId)
            .forEach(taskPackage -> {
                wbsEntryStateRepository.unsetTaskPackageInfo(taskPackage);
                Set<BigInteger> wbsEntryIds = new HashSet<>();
                if (taskPackage.getDiscipline() == "STRUCTURE") {
                    wbsEntryIds = wbsEntryRepository.findStructureWbsEntryByTaskPackageId(taskPackage.getProjectId(), taskPackage.getId());
                } else {
                    wbsEntryIds = wbsEntryRepository.findWbsEntryByTaskPackageId(taskPackage.getProjectId(), taskPackage.getId());
                }
                wbsEntryIds.forEach(wbsEntryId ->{
                    wbsEntryRepository.updateTaskPackageId(wbsEntryId.longValue(), taskPackage.getId());
                });
            });
    }

    /**
     * 取得所有已添加到指定类型任务包的实体的 ID。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @return 实体 ID 集合
     */
    @Override
    public Set<Long> entityIDs(
        final Long orgId,
        final Long projectId,
        final Long categoryId
    ) {
        return taskPackageCategoryRepository.findEntityIdByProjectIdAndId(projectId, categoryId);
    }

    /**
     * 取得任务包类型对应的实体类型。
     *
     * @param orgId      组织 ID
     * @param projectId  项目 ID
     * @param categoryId 任务包类型 ID
     * @return 实体类型列表
     */
    @Override
    public List<BpmEntitySubType> entityTypes(
        final Long orgId,
        final Long projectId,
        final Long categoryId
    ) {

        TaskPackageCategory taskPackageCategory = get(orgId, projectId, categoryId);

        if (taskPackageCategory == null) {
            throw new NotFoundError();
        }

        List<TaskPackageCategoryProcessRelation> relations = taskPackageCategoryProcessRelationRepository
            .findByOrgIdAndProjectIdAndCategoryId(orgId, projectId, categoryId);

        Map<Long, BpmEntitySubType> entityCategories = new HashMap<>();

        for (TaskPackageCategoryProcessRelation relation : relations) {
            getEntitySubTypes(
                taskPackageCategory.getEntityType(),
                relation.getProcess().getEntitySubTypes(),
                orgId,
                projectId
            ).forEach(entitySubType -> entityCategories.put(entitySubType.getId(), entitySubType));
        }

        return new ArrayList<>(entityCategories.values());
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param <T>      数据实体范型
     * @param included 引用数据映射表
     * @param entities 查询结果列表
     * @return 引用数据映射表
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        final Map<Long, Object> included,
        final List<T> entities
    ) {

        if (entities == null || entities.size() == 0) {
            return included;
        }

        Set<Long> categoryIDs = new HashSet<>();

        for (T entity : entities) {
            if (entity instanceof TaskPackageBasic) {
                categoryIDs.add(((TaskPackageBasic) entity).getCategoryId());
            }
            if (entity instanceof TaskPackagePercent) {
                categoryIDs.add(((TaskPackagePercent) entity).getCategoryId());
            }
        }

        if (categoryIDs.size() == 0) {
            return included;
        }

        taskPackageCategoryRepository
            .findByIdIn(categoryIDs)
            .forEach(category -> included.put(category.getId(), category));

        return included;
    }

    @Override
    public TaskPackageTypeEnumDTO getTypeEnums(Long orgId, Long projectId) {
//        List<HierarchyType> hierarchyTyps = hierarchyTypeRepository.findByProjectId(projectId);
//        List<FuncPart> funcPs = funcPartRepository.findByProjectId(projectId);
//        FuncPart funcP = funcPs.get(0);
//        funcP.setJsonHierarchyTypes(hierarchyTyps);
//        funcPartRepository.save(funcP);

        Project project = projectService.get(projectId);
        List<FuncPart> funcParts = funcPartRepository.findByProjectId(projectId);
        funcParts.forEach(funcPart -> {
            List<HierarchyType> hierarchyTypes = funcPart.getJsonHierarchyTypes();
            if(CollectionUtils.isEmpty(hierarchyTypes)) return;
            List<HierarchyType> hierarchyTypesNew = new ArrayList<>();
            hierarchyTypes.forEach(ht ->{
                switch(ht.getNameEn()) {
                    case "ENGINEERING_COMMON":
                        List<String> entityTypes = ht.getJsonEntityTypes();
                        String engOptsStr = project.getEngineeringOptionalNodeTypes();
                        entityTypes.removeAll(ProjectInterface.ENGINEERING_OPTIONAL_NODE_TYPES);
                        if(StringUtils.isEmpty(engOptsStr)) {
                            ht.setJsonEntityTypes(entityTypes);
                            hierarchyTypesNew.add(ht);
                            break;
                        }
                        entityTypes.addAll(Arrays.asList(engOptsStr.split(",")));
                        ht.setJsonEntityTypes(entityTypes);
                        hierarchyTypesNew.add(ht);
                        break;
                }
            });
            funcPart.setJsonHierarchyTypes(hierarchyTypesNew);
        });
        List<Discipline> disciplines = disciplineRepository.findByProjectId(projectId);
        return new TaskPackageTypeEnumDTO(
            disciplines,
            funcParts
            );
    }
}
