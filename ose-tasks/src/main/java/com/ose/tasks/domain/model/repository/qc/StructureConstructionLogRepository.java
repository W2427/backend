package com.ose.tasks.domain.model.repository.qc;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.qc.StructureTestLog;
import com.ose.tasks.vo.wbs.EntityTestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StructureConstructionLogRepository extends PagingAndSortingWithCrudRepository<StructureTestLog, Long> {

    /**
     * 获取焊接日志列表。
     *
     * @param weldId   焊口ID
     * @param pageable 分页参数
     * @return 焊接日志列表
     */
    Page<StructureTestLog> findByEntityId(Long weldId, Pageable pageable);

    StructureTestLog findByEntityIdAndActInstIdAndDeletedIsFalse(Long entityId, Long actInstId);

    List<StructureTestLog> findByActInstIdAndTestResultAndDeletedIsFalse(Long actInstId, EntityTestResult finishState);

    List<StructureTestLog> findByProcessIdAndTestResultAndExecutorsIsNull(Long processId, EntityTestResult finished);

    List<StructureTestLog> findByActInstIdAndDeletedIsFalse(Long actInstId);

    List<StructureTestLog> findByProcessAndHeatNoId1IsNullAndDeletedIsFalse(String process);

    List<StructureTestLog> findByProcessAndReportNosIsNullAndDeletedIsFalse(String process);

    List<StructureTestLog> findByOrgIdAndProjectIdAndWeldRepairCountIsNullAndDeletedIsFalse(Long orgId, Long projectId);

    List<StructureTestLog> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId);

    List<StructureTestLog> findByEntityIdAndDeletedIsFalse(Long entityId);

    StructureTestLog findByEntityIdAndProcessAndWeldRepairCountAndDeletedIsFalse(Long entityId, String process, String weldRepairCount);

    List<StructureTestLog> findByEntityIdAndProcessAndDeletedIsFalseOrderByCreatedAtDesc(Long entityId, String process);
}
