package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.IssuePropertyDefinition;
import com.ose.issues.vo.IssueType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.Tuple;
import java.util.List;

public interface IssuePropertyDefinitionRepository extends PagingAndSortingRepository<IssuePropertyDefinition, Long> {

    /**
     * 取得问题的所有属性定义
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<IssuePropertyDefinition> findByOrgIdAndProjectIdAndDeletedIsFalse(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    /**
     * 获得经验教训的自定义属性
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param issueType
     * @return
     */
    List<IssuePropertyDefinition> findByOrgIdAndProjectIdAndIssueTypeAndDeletedIsFalse(Long orgId, Long projectId, IssueType issueType);


    @Query("SELECT i.name AS nm, i.shortDesc AS shortDesc FROM IssuePropertyDefinition i WHERE i.projectId = :projectId AND i.issueType = :issueType AND i.deleted = false")
    List<Tuple> getColumnHeaderMap(@Param("projectId") Long projectId, @Param("issueType") IssueType issueType);
}
