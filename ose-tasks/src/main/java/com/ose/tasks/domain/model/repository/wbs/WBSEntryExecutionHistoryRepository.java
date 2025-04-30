package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wbs.entry.WBSEntryExecutionHistory;
import com.ose.tasks.vo.wbs.WBSEntryExecutionState;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * WBSEntryExecutionHistory CRUD 操作接口。
 */
public interface WBSEntryExecutionHistoryRepository extends PagingAndSortingWithCrudRepository<WBSEntryExecutionHistory, Long> {

    /**
     * 取得变更实体后 需要更新四级计划 对应的模块名称集合
     *
     * @param projectId 项目ID
     * @return
     */
    List<WBSEntryExecutionHistory> findByProjectIdAndExecutionStateOrderByModuleId(
        @Param("projectId") Long projectId,
        @Param("executionState") WBSEntryExecutionState wbsEntryExecutionState
    );

    /**
     * 查找是否有 entityId + projectId + UNDO 的条目
     *
     * @param projectId      项目ID
     * @param entityId
     * @param executionState
     * @return
     */
    WBSEntryExecutionHistory findByProjectIdAndEntityIdAndExecutionState(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("executionState") WBSEntryExecutionState executionState);
}
