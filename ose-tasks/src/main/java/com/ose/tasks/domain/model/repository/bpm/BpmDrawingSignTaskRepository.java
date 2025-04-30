package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmDrawingSignTask;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BpmDrawingSignTaskRepository extends PagingAndSortingWithCrudRepository<BpmDrawingSignTask, Long> {

    BpmDrawingSignTask findByTaskIdAndAssignee(Long taskId, Long userId);

    List<BpmDrawingSignTask> findByTaskId(Long taskId);

    List<BpmDrawingSignTask> findByTaskIdAndFinished(Long taskId, boolean isFinished);

    List<BpmDrawingSignTask> findByActInstIdAndFinished(Long actInstId, boolean isFinished);

    BpmDrawingSignTask findByActInstIdAndAssignee(Long actInstId, Long userId);
}
