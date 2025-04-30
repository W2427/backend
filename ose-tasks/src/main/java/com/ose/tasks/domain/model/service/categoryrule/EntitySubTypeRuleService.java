package com.ose.tasks.domain.model.service.categoryrule;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.categoryrule.EntitySubTypeRuleRepository;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleCriteriaDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleInsertDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleUpdateDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ose.vo.EntityStatus.ACTIVE;
import static com.ose.vo.EntityStatus.DELETED;

@Component
public class EntitySubTypeRuleService implements EntitySubTypeRuleInterface {


    private final EntitySubTypeRuleRepository entityCategoryRuleRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public EntitySubTypeRuleService(
        EntitySubTypeRuleRepository entityCategoryRuleRepository

    ) {
        this.entityCategoryRuleRepository = entityCategoryRuleRepository;

    }

    /**
     * 查询 实体类型设置规则。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageDTO     分页参数
     * @return WBS 实体分页数据
     */
    @Override
    public Page<EntitySubTypeRule> search(
        Long orgId,
        Long projectId,
        EntitySubTypeRuleCriteriaDTO criteriaDTO,
        PageDTO pageDTO) {
        return entityCategoryRuleRepository.search(orgId, projectId, criteriaDTO, pageDTO);
    }

    @Override
    public EntitySubTypeRule get(Long entityCategoryRuleId, Long orgId, Long projectId) {
        return entityCategoryRuleRepository.findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(
            entityCategoryRuleId,
            orgId,
            projectId)
            .orElse(null);
    }

    @Override
    public void delete(OperatorDTO operator, Long orgId, Long projectId, Long ruleId) {
        EntitySubTypeRule entity = get(ruleId, orgId, projectId);

        entity.setDeletedBy(operator.getId());
        entity.setDeletedAt();
        entity.setStatus(DELETED);

        entityCategoryRuleRepository.save(entity);
    }

    @Override
    public void insert(OperatorDTO operator, Project project, EntitySubTypeRuleInsertDTO dto) {
        if (!StringUtils.isEmpty(dto.getSubType())) {
            List<EntitySubTypeRule> findRules = entityCategoryRuleRepository.
                findByOrgIdAndProjectIdAndEntitySubTypeAndStatusAndDeletedIsFalse(
                    project.getOrgId(),
                    project.getId(),
                    dto.getSubType(),
                    EntityStatus.ACTIVE
                );

            for (EntitySubTypeRule findRule : findRules) {
                if (findRule.getParentType() != null) {
                    if (!findRule.getParentType().equals(dto.getParentType())) {
                        throw new BusinessError("新增规则的焊口父级与已经存在的规则中的父级类型矛盾");
                    }
                }
            }
        }


        EntitySubTypeRule entity = new EntitySubTypeRule(project);
        BeanUtils.copyProperties(dto, entity);
        entity.setRuleOrder(dto.getRuleOrder());
        entity.setCreatedBy(operator.getId());
        entity.setCreatedAt();
        entity.setLastModifiedBy(operator.getId());
        entity.setLastModifiedAt();
        entity.setProjectId(project.getId());
        entity.setStatus(ACTIVE);
        entity.setDeleted(false);
        entity.setVersion(entity.getLastModifiedAt().getTime());
        if (dto.getEntityType() == "PIPE_COMPONENT") {
            entity.setEntityType("COMPONENT");
        }
        entityCategoryRuleRepository.save(entity);
    }

    /**
     * @param operator 操作者信息
     * @param project  项目
     * @param ruleId   要更新的实体类型规则ID
     * @param dto      实体类型设置规则
     * @return EntitySubTypeRule
     */
    @Override
    public EntitySubTypeRule update(OperatorDTO operator,
                                     Project project,
                                     Long ruleId,
                                     EntitySubTypeRuleUpdateDTO dto) {

        EntitySubTypeRule ruleUpd = get(ruleId, project.getOrgId(), project.getId());
        if (ruleUpd == null) {
            throw new NotFoundError();
        }


        if (!StringUtils.isEmpty(dto.getSubType())) {
            List<EntitySubTypeRule> findRules = entityCategoryRuleRepository.
                findByOrgIdAndProjectIdAndEntitySubTypeAndStatusAndDeletedIsFalse(
                    project.getOrgId(),
                    project.getId(),
                    dto.getSubType(),
                    EntityStatus.ACTIVE
                );

            for (EntitySubTypeRule findRule : findRules) {
                if (findRule.getParentType() != null) {
                    if (!findRule.getParentType().equals(dto.getParentType())) {
                        throw new BusinessError("修改后规则的焊口父级与已经存在的规则中的父级类型矛盾");
                    }
                }
            }
        }

        BeanUtils.copyProperties(dto, ruleUpd);
        ruleUpd.setLastModifiedBy(operator.getId());
        ruleUpd.setLastModifiedAt();
        ruleUpd.setProjectId(project.getId());
        ruleUpd.setStatus(ACTIVE);
        ruleUpd.setDeleted(false);
        entityCategoryRuleRepository.save(ruleUpd);
        return ruleUpd;
    }


    @Override
    public boolean existsByCategoryAndCategoryType(
        String category,
        String entityType,
        Long projectId) {
        return entityCategoryRuleRepository.existsByEntitySubTypeAndEntityTypeAndProjectIdAndDeletedIsFalse(
            category,
            entityType,
            projectId);
    }

}
