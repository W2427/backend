package com.ose.issues.domain.model.service;

import com.ose.dto.BaseDTO;
import com.ose.exception.NotFoundError;
import com.ose.issues.domain.model.repository.PropertyDefinitionRepository;
import com.ose.issues.dto.PropertyDefinitionCreateDTO;
import com.ose.issues.dto.PropertyDefinitionUpdateDTO;
import com.ose.issues.entity.IssueProperty;
import com.ose.issues.entity.IssuePropertyDefinition;
import com.ose.issues.vo.CustomPropertyType;
import com.ose.issues.vo.IssuePropertyCategory;
import com.ose.issues.vo.IssueType;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自定义属性定义服务。
 */
@Component
public class PropertyDefinitionService implements PropertyDefinitionInterface {

    private PropertyDefinitionRepository propertyDefinitionRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public PropertyDefinitionService(
        PropertyDefinitionRepository propertyDefinitionRepository
    ) {
        this.propertyDefinitionRepository = propertyDefinitionRepository;
    }

    /**
     * 创建自定义属性。
     *
     * @param operatorId            操作人 ID
     * @param orgId                 组织 ID
     * @param projectId             项目 ID
     * @param propertyDefinitionDTO 属性信息
     * @return 自定义属性
     */
    @Override
    @Transactional
    public IssuePropertyDefinition create(
        final Long operatorId,
        final Long orgId,
        final Long projectId,
        final PropertyDefinitionCreateDTO propertyDefinitionDTO
    ) {
        // TODO 校验projectID 的有效性
        IssuePropertyDefinition propertyDefinition = BeanUtils
            .copyProperties(propertyDefinitionDTO, new IssuePropertyDefinition());

        propertyDefinition.setOrgId(orgId);
        propertyDefinition.setProjectId(projectId);
        propertyDefinition.setOptions(propertyDefinitionDTO.getOptions());
        propertyDefinition.setCreatedAt();
        propertyDefinition.setCreatedBy(operatorId);
        propertyDefinition.setLastModifiedAt();
        propertyDefinition.setLastModifiedBy(operatorId);
        propertyDefinition.setStatus(EntityStatus.ACTIVE);

        return propertyDefinitionRepository.save(propertyDefinition);
    }

    /**
     * 更新自定义属性。
     *
     * @param operatorId                  操作人 ID
     * @param propertyDefinitionId        属性定义 ID
     * @param propertyDefinitionUpdateDTO 更新信息
     * @return 自定义属性
     */
    @Override
    @Transactional
    public IssuePropertyDefinition update(
        final Long operatorId,
        final Long propertyDefinitionId,
        final PropertyDefinitionUpdateDTO propertyDefinitionUpdateDTO
    ) {

        IssuePropertyDefinition propertyDefinition = propertyDefinitionRepository
            .findByIdAndDeletedIsFalse(propertyDefinitionId);

        if (propertyDefinition == null) {
            throw new NotFoundError();
        }

        if (propertyDefinitionUpdateDTO.getIssueType() != null) {
            propertyDefinition.setIssueType(propertyDefinitionUpdateDTO.getIssueType());
        }

        if (propertyDefinitionUpdateDTO.getPropertyType() != null) {
            propertyDefinition.setPropertyType(propertyDefinitionUpdateDTO.getPropertyType());
        }

        if (propertyDefinitionUpdateDTO.getName() != null) {
            propertyDefinition.setName(propertyDefinitionUpdateDTO.getName());
        }

        if (propertyDefinitionUpdateDTO.getOptions() != null) {
            propertyDefinition.setOptions(propertyDefinitionUpdateDTO.getOptions());
        }

        if (propertyDefinitionUpdateDTO.getPropertyCategory() != null) {
            propertyDefinition.setPropertyCategory(propertyDefinitionUpdateDTO.getPropertyCategory());
        }

        propertyDefinition.setLastModifiedAt();
        propertyDefinition.setLastModifiedBy(operatorId);

        return propertyDefinitionRepository.save(propertyDefinition);
    }

    /**
     * 删除自定义属性定义。
     *
     * @param operatorId           操作人 ID
     * @param propertyDefinitionId 自定义属性定义 ID
     * @return 自定义属性定义
     */
    @Override
    public IssuePropertyDefinition delete(Long operatorId, Long propertyDefinitionId) {

        IssuePropertyDefinition propertyDefinition = propertyDefinitionRepository
            .findByIdAndDeletedIsFalse(propertyDefinitionId);

        if (propertyDefinition == null) {
            throw new NotFoundError();
        }

        propertyDefinition.setDeletedBy(operatorId);
        propertyDefinition.setDeletedAt();
        return propertyDefinitionRepository.save(propertyDefinition);
    }

    /**
     * 获取自定义属性定义列表。
     *
     * @param projectId        项目 ID
     * @param issueType        问题类型
     * @param propertyType     自定义属性值的类型
     * @param propertyCategory 遗留问题属性分类
     * @param pageable         分页参数
     * @return 自定义属性定义列表
     */
    @Override
    public Page<IssuePropertyDefinition> search(
        final Long projectId,
        final IssueType issueType,
        final CustomPropertyType propertyType,
        final IssuePropertyCategory propertyCategory,
        final Pageable pageable
    ) {
        if (issueType != null && propertyType != null) {
            if (propertyCategory != null) {
                return propertyDefinitionRepository
                    .findByProjectIdAndIssueTypeAndPropertyTypeAndPropertyCategoryAndDeletedIsFalse(
                        projectId, issueType, propertyType, propertyCategory, pageable);
            } else {
                return propertyDefinitionRepository
                    .findByProjectIdAndIssueTypeAndPropertyTypeAndDeletedIsFalse(projectId, issueType, propertyType, pageable);
            }
        } else if (issueType != null) {
            if (propertyCategory != null) {
                return propertyDefinitionRepository
                    .findByProjectIdAndIssueTypeAndPropertyCategoryAndDeletedIsFalse(projectId, issueType, propertyCategory, pageable);
            } else {
                return propertyDefinitionRepository
                    .findByProjectIdAndIssueTypeAndDeletedIsFalse(projectId, issueType, pageable);
            }
        } else if (propertyType != null) {
            return propertyDefinitionRepository
                .findByProjectIdAndPropertyTypeAndDeletedIsFalse(projectId, propertyType, pageable);
        } else {
            return propertyDefinitionRepository
                .findByProjectIdAndDeletedIsFalse(projectId, pageable);
        }
    }

    /**
     * 获取自定义属性定义列表。
     *
     * @param projectId        项目 ID
     * @param issueType        问题类型
     * @param propertyType     自定义属性值的类型
     * @param propertyCategory 遗留问题属性分类
     * @return 自定义属性定义列表
     */
    @Override
    public List<IssuePropertyDefinition> search(
        final Long projectId,
        final IssueType issueType,
        final CustomPropertyType propertyType,
        final IssuePropertyCategory propertyCategory
    ) {
        if (issueType != null && propertyType != null) {
            if (propertyCategory != null) {
                return propertyDefinitionRepository
                    .findByProjectIdAndIssueTypeAndPropertyTypeAndPropertyCategoryAndDeletedIsFalseAndIsIssueFieldIsFalse(projectId, issueType, propertyType, propertyCategory);
            } else {
                return propertyDefinitionRepository
                    .findByProjectIdAndIssueTypeAndPropertyTypeAndDeletedIsFalse(projectId, issueType, propertyType);
            }
        } else if (issueType != null) {
            if (propertyCategory != null) {
                return propertyDefinitionRepository
                    .findByProjectIdAndIssueTypeAndPropertyCategoryAndDeletedIsFalseAndIsIssueFieldIsFalse(projectId, issueType, propertyCategory);
            } else {
                return propertyDefinitionRepository
                    .findByProjectIdAndIssueTypeAndDeletedIsFalse(projectId, issueType);
            }
        } else if (propertyType != null) {
            return propertyDefinitionRepository
                .findByProjectIdAndPropertyTypeAndDeletedIsFalse(projectId, propertyType);
        } else {
            return propertyDefinitionRepository
                .findByProjectIdAndDeletedIsFalse(projectId);
        }
    }

    /**
     * 自定义属性定义详情。
     *
     * @param propertyDefinitionId 自定义属性定义 ID
     * @return 属性模板详情
     */
    @Override
    public IssuePropertyDefinition details(Long propertyDefinitionId) {
        return propertyDefinitionRepository
            .findByIdAndDeletedIsFalse(propertyDefinitionId);
    }

    /**
     * 设置返回结果的引用数据。
     *
     * @param <T>      数据实体泛型
     * @param included 引用数据映射表
     * @param entities 返回结果
     * @return 返回结果的引用数据
     */
    @Override
    public <T extends BaseDTO> Map<Long, Object> setIncluded(
        final Map<Long, Object> included,
        final List<T> entities
    ) {

        if (entities == null || entities.size() == 0) {
            return included;
        }

        List<Long> propertyDefinitionIDs = new ArrayList<>();

        for (T entity : entities) {
            if (entity instanceof IssueProperty) {
                IssueProperty property = (IssueProperty) entity;
                propertyDefinitionIDs.add(property.getPropertyId());
            }
        }

        if (propertyDefinitionIDs.size() == 0) {
            return included;
        }

        List<IssuePropertyDefinition> keys = propertyDefinitionRepository
            .findByIdIn(propertyDefinitionIDs);

        for (IssuePropertyDefinition key : keys) {
            included.put(key.getId(), key);
        }

        return included;
    }
}
