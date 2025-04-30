package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.entity.bpm.BpmActTaskAssigneeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * 实体 CRUD 操作接口。
 */
@Transactional
public interface BpmActTaskAssigneeHistoryRepository extends PagingAndSortingRepository<BpmActTaskAssigneeHistory, Long> {


    Page<BpmActTaskAssigneeHistory> findByActInstIdOrderByTaskName(Long actInstId, Pageable pageable);


    Page<BpmActTaskAssigneeHistory> findByActInstIdAndTaskNameOrderByTaskName(Long actInstId, String taskName, Pageable pageable);

}
