package com.ose.tasks.domain.model.repository.plan;

import com.ose.tasks.entity.wbs.entry.WBSEntryAddLog;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface WBSEntryAddLogRepository extends PagingAndSortingRepository<WBSEntryAddLog, Long> {


    List<WBSEntryAddLog> findByProjectIdAndBatchTaskId(Long projectId, Long originalBatchTaskId);

    List<WBSEntryAddLog> findByProjectIdAndWorkWbsEntryIdAndBatchTaskId(Long projectId, Long workWbsEntryId, Long originalBatchTaskId);
}
