package com.ose.issues.domain.model.repository;

import com.ose.issues.entity.Issue;
import com.ose.issues.vo.IssueSource;
import com.ose.issues.vo.IssueType;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface IssueRepository extends PagingAndSortingWithCrudRepository<Issue, Long>, IssueCustomRepository {

    /**
     * 获取问题详情。
     *
     * @param issueId 问题ID
     * @return 问题详情
     */
    Issue findByIdAndDeletedIsFalse(Long issueId);

    /**
     * 获取问题详情。
     *
     * @param issues 问题ID
     * @return 问题详情
     */
    List<Issue> findByIdInAndDeletedIsFalse(List<Long> issues);

    /**
     * 检查是否存在编号相同的项目。
     *
     * @param projectId 项目 ID
     * @param type      问题类型
     * @param no        问题编号
     * @return 检查结果
     */
    Boolean existsByProjectIdAndTypeAndNoAndIdIsNotAndDeletedIsFalse(Long projectId, IssueType type, String no, Long issueId);

    /**
     * 查询最大流水号
     *
     * @param projectId 项目ID
     * @param source
     * @return
     */
    @Query("select max(e.seriesNo) from Issue e where e.projectId=:projectId and e.punchSource =:source")
    Integer findMaxSeriesByProjectIdAndPunchSource(@Param("projectId") Long projectId, @Param("source") IssueSource source);



    Issue findByProjectIdAndNoAndDeletedIsFalse(Long projectId, String no);

    List<Issue> findByProjectIdAndTypeAndDescriptionCnIsNullAndDeletedIsFalse(Long projectId, IssueType issue);
}
