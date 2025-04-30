package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entry.WBSEntryPredecessorDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * WBS 条目关系 CRUD 操作接口。
 */
public interface WBSEntryPredecessorRepository extends PagingAndSortingRepository<WBSEntryPredecessorDetail, Long> {

    /**
     * 查询前置任务。
     *
     * @param projectId     项目 ID
     * @param successorGUID 后置任务 GUID
     * @param pageable      分页参数
     * @return 前置任务分页数据
     */
    @Query("SELECT p FROM WBSEntryPredecessorDetail p WHERE p.projectId = :projectId AND p.successorId = :successorGUID AND (p.optional IS NULL OR p.optional = FALSE)")
    Page<WBSEntryPredecessorDetail> findByProjectIdAndSuccessorIdAAndOptionalIsNotTrue(
        @Param("projectId") Long projectId,
        @Param("successorGUID") String successorGUID,
        Pageable pageable
    );




}
