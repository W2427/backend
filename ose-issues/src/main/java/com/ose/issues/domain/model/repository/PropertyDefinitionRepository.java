package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.IssuePropertyDefinition;
import com.ose.issues.vo.CustomPropertyType;
import com.ose.issues.vo.IssuePropertyCategory;
import com.ose.issues.vo.IssueType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface PropertyDefinitionRepository extends PagingAndSortingWithCrudRepository<IssuePropertyDefinition, Long> {

    /**
     * 获取问题属性模板。
     *
     * @param projectId 项目 ID
     * @param pageable  分页参数
     * @return 问题属性模板
     */
    Page<IssuePropertyDefinition> findByProjectIdAndDeletedIsFalse(Long projectId, Pageable pageable);

    /**
     * 获取问题属性模板。
     *
     * @param projectId 项目 ID
     * @return 问题属性模板
     */
    List<IssuePropertyDefinition> findByProjectIdAndDeletedIsFalse(Long projectId);

    /**
     * 获取问题属性模板。
     *
     * @param projectId 项目 ID
     * @param issueType 自定义属性值的类型
     * @param pageable  分页参数
     * @return 问题属性模板
     */
    Page<IssuePropertyDefinition> findByProjectIdAndIssueTypeAndDeletedIsFalse(
        Long projectId,
        IssueType issueType,
        Pageable pageable
    );

    /**
     * 获取问题属性模板。
     *
     * @param projectId    项目 ID
     * @param propertyType 自定义属性值的类型
     * @return 问题属性模板
     */
    List<IssuePropertyDefinition> findByProjectIdAndPropertyTypeAndDeletedIsFalse(
        Long projectId,
        CustomPropertyType propertyType
    );

    /**
     * 获取问题属性模板。
     *
     * @param projectId    项目 ID
     * @param propertyType 自定义属性值的类型
     * @param pageable     分页参数
     * @return 问题属性模板
     */
    Page<IssuePropertyDefinition> findByProjectIdAndPropertyTypeAndDeletedIsFalse(
        Long projectId,
        CustomPropertyType propertyType,
        Pageable pageable
    );

    /**
     * 获取问题属性模板。
     *
     * @param projectId 项目 ID
     * @param issueType 自定义属性值的类型
     * @return 问题属性模板
     */
    List<IssuePropertyDefinition> findByProjectIdAndIssueTypeAndDeletedIsFalse(
        Long projectId,
        IssueType issueType
    );

    /**
     * 获取问题属性模板。
     *
     * @param projectId    项目 ID
     * @param issueType    自定义属性值的类型
     * @param propertyType 自定义属性值的类型
     * @param pageable     分页参数
     * @return 问题属性模板
     */
    Page<IssuePropertyDefinition> findByProjectIdAndIssueTypeAndPropertyTypeAndDeletedIsFalse(
        Long projectId,
        IssueType issueType,
        CustomPropertyType propertyType,
        Pageable pageable
    );

    /**
     * 获取问题属性模板。
     *
     * @param projectId    项目 ID
     * @param issueType    自定义属性值的类型
     * @param propertyType 自定义属性值的类型
     * @return 问题属性模板
     */
    List<IssuePropertyDefinition> findByProjectIdAndIssueTypeAndPropertyTypeAndDeletedIsFalse(
        Long projectId,
        IssueType issueType,
        CustomPropertyType propertyType
    );

    /**
     * 获取问题属性详情。
     *
     * @param keyId 属性ID
     * @return 属性详情
     */
    IssuePropertyDefinition findByIdAndDeletedIsFalse(Long keyId);

    /**
     * 根据ID列表获取全部属性列表。
     *
     * @param keys keyID列表
     * @return 属性列表
     */
    List<IssuePropertyDefinition> findByIdIn(List<Long> keys);

    /**
     * 根据名称取得自定义属性列表。
     *
     * @param names 自定义属性名称列表
     * @return 自定义属性列表
     */
    List<IssuePropertyDefinition> findByProjectIdAndNameInAndDeletedIsFalse(Long projectId, Collection<String> names);

    Page<IssuePropertyDefinition> findByProjectIdAndIssueTypeAndPropertyTypeAndPropertyCategoryAndDeletedIsFalse(
        Long projectId,
        IssueType issueType,
        CustomPropertyType propertyType,
        IssuePropertyCategory propertyCategory,
        Pageable pageable
    );

    Page<IssuePropertyDefinition> findByProjectIdAndIssueTypeAndPropertyCategoryAndDeletedIsFalse(
        Long projectId,
        IssueType issueType,
        IssuePropertyCategory propertyCategory,
        Pageable pageable
    );

    List<IssuePropertyDefinition> findByProjectIdAndIssueTypeAndPropertyTypeAndPropertyCategoryAndDeletedIsFalseAndIsIssueFieldIsFalse(
        Long projectId,
        IssueType issueType,
        CustomPropertyType propertyType,
        IssuePropertyCategory propertyCategory
    );

    List<IssuePropertyDefinition> findByProjectIdAndIssueTypeAndPropertyCategoryAndDeletedIsFalseAndIsIssueFieldIsFalse(
        Long projectId,
        IssueType issueType,
        IssuePropertyCategory propertyCategory
    );

    IssuePropertyDefinition findByProjectIdAndIssueTypeAndNameAndDeletedIsFalse(Long projectId, IssueType issue, String columnName);
}
