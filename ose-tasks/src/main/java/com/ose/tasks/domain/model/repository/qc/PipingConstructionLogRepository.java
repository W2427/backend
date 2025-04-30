package com.ose.tasks.domain.model.repository.qc;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.qc.PipingTestLog;
import com.ose.tasks.vo.wbs.EntityTestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PipingConstructionLogRepository extends PagingAndSortingWithCrudRepository<PipingTestLog, Long> {

    /**
     * 获取焊接日志列表。
     *
     * @param weldId   焊口ID
     * @param pageable 分页参数
     * @return 焊接日志列表
     */
    Page<PipingTestLog> findByEntityId(Long weldId, Pageable pageable);

    PipingTestLog findByEntityIdAndActInstIdAndDeletedIsFalse(Long entityId, Long actInstId);

    List<PipingTestLog> findByActInstIdAndTestResultAndDeletedIsFalse(Long actInstId, EntityTestResult finishState);

    List<PipingTestLog> findByProcessIdAndTestResultAndExecutorsIsNull(Long processId, EntityTestResult finished);

    List<PipingTestLog> findByProcessAndReportNosIsNullAndDeletedIsFalse(String process);

}
