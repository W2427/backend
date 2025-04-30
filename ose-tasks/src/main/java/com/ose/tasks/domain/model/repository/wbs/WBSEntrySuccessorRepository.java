package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entry.WBSEntrySuccessorDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * WBS 条目关系 CRUD 操作接口。
 */
public interface WBSEntrySuccessorRepository extends PagingAndSortingRepository<WBSEntrySuccessorDetail, Long> {

    /**
     * 查询后置任务。
     *
     * @param projectId       项目 ID
     * @param predecessorGUID 前置任务 GUID
     * @param pageable        分页参数
     * @return 前置任务分页数据
     */
    Page<WBSEntrySuccessorDetail> findByProjectIdAndPredecessorIdAndDeletedIsFalse(
        Long projectId,
        String predecessorGUID,
        Pageable pageable
    );

}
