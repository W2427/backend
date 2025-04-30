package com.ose.issues.domain.model.service;

import com.ose.issues.dto.PropertyDefinitionCreateDTO;
import com.ose.issues.dto.PropertyDefinitionUpdateDTO;
import com.ose.issues.entity.IssuePropertyDefinition;
import com.ose.issues.vo.CustomPropertyType;
import com.ose.issues.vo.IssuePropertyCategory;
import com.ose.issues.vo.IssueType;
import com.ose.service.EntityInterface;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 自定义属性定义服务接口。
 */
public interface PropertyDefinitionInterface extends EntityInterface {

    /**
     * 创建自定义属性。
     *
     * @param operatorId            操作人 ID
     * @param orgId                 组织 ID
     * @param projectId             项目 ID
     * @param propertyDefinitionDTO 属性信息
     * @return 自定义属性
     */
    IssuePropertyDefinition create(
        Long operatorId,
        Long orgId,
        Long projectId,
        PropertyDefinitionCreateDTO propertyDefinitionDTO
    );

    /**
     * 删除自定义属性定义。
     *
     * @param operatorId           操作人 ID
     * @param propertyDefinitionId 自定义属性定义 ID
     * @return 自定义属性定义
     */
    IssuePropertyDefinition delete(
        Long operatorId,
        Long propertyDefinitionId
    );

    /**
     * 更新自定义属性。
     *
     * @param operatorId                  操作人 ID
     * @param propertyDefinitionId        属性定义 ID
     * @param propertyDefinitionUpdateDTO 更新信息
     * @return 自定义属性
     */
    IssuePropertyDefinition update(
        Long operatorId,
        Long propertyDefinitionId,
        PropertyDefinitionUpdateDTO propertyDefinitionUpdateDTO
    );

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
    Page<IssuePropertyDefinition> search(
        Long projectId,
        IssueType issueType,
        CustomPropertyType propertyType,
        IssuePropertyCategory propertyCategory,
        Pageable pageable
    );

    /**
     * 获取自定义属性定义列表。
     *
     * @param projectId        项目 ID
     * @param issueType        问题类型
     * @param propertyType     自定义属性值的类型
     * @param propertyCategory 遗留问题属性分类
     * @return 自定义属性定义列表
     */
    List<IssuePropertyDefinition> search(
        Long projectId,
        IssueType issueType,
        CustomPropertyType propertyType,
        IssuePropertyCategory propertyCategory
    );

    /**
     * 自定义属性定义详情。
     *
     * @param propertyDefinitionId 自定义属性定义 ID
     * @return 属性模板详情
     */
    IssuePropertyDefinition details(Long propertyDefinitionId);

}
