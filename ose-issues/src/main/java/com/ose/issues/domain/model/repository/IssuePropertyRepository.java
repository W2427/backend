package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.IssueProperty;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface IssuePropertyRepository extends PagingAndSortingWithCrudRepository<IssueProperty, Long> {

    /**
     * 取得问题的所有属性设置。
     *
     * @param issueIDs 问题 ID 列表
     * @return 属性列表
     */
    List<IssueProperty> findByProjectIdAndIssueIdIn(Long projectId, Collection<Long> issueIDs);

    /**
     * 取得问题的自定义属性。
     *
     * @param issueId     问题 ID
     * @param propertyIDs 属性 ID
     * @return 自定义属性列表
     */
    List<IssueProperty> findByProjectIdAndIssueIdAndPropertyIdIn(Long projectId, Long issueId, Collection<Long> propertyIDs);

    /**
     * 删除问题的自定义属性（排除指定的自定义属性）。
     *
     * @param issueId           问题 ID
     * @param exceptPropertyIDs 例外属性 ID
     */
    void deleteByProjectIdAndIssueIdAndPropertyIdNotIn(Long projectId, Long issueId, Collection<Long> exceptPropertyIDs);

    IssueProperty findByProjectIdAndIssueIdAndPropertyIdAndStatus(Long projectId, Long issueId, Long propertyId, EntityStatus active);
}
