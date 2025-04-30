package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.ose.tasks.entity.bpm.BpmPlanExecutionHistory;
import com.ose.tasks.vo.bpm.BpmPlanExecutionState;

public interface BpmPlanExecutionHistoryRepository extends PagingAndSortingWithCrudRepository<BpmPlanExecutionHistory, Long> {

    List<BpmPlanExecutionHistory> findByProjectIdAndExecutionStateAndServerUrl(Long projectId,
                                                                               BpmPlanExecutionState undo, String url);

    List<BpmPlanExecutionHistory> findByProjectIdAndProcessIdInAndExecutionStateAndServerUrl(
        Long projectId,
        List<Long> processIds,
        BpmPlanExecutionState undo,
        String url);

    List<BpmPlanExecutionHistory> findByProjectIdAndVersionAndExecutionStateAndServerUrl(Long projectId, Long version, BpmPlanExecutionState undo, String url);
}
