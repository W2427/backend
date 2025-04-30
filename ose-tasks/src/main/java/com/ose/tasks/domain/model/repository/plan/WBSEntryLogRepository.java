package com.ose.tasks.domain.model.repository.plan;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wbs.entry.WBSEntryLog;
import com.ose.tasks.vo.bpm.ActivityExecuteResult;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WBSEntryLogRepository extends PagingAndSortingWithCrudRepository<WBSEntryLog, Long> {


    WBSEntryLog findByBatchTaskIdAndResult(Long originalBatchTaskId, ActivityExecuteResult init);

    Boolean existsByProjectIdAndResult(Long projectId, ActivityExecuteResult result);
}
