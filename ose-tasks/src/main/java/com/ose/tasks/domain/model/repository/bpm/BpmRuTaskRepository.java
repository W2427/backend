package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.vo.bpm.ExInspApplyStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public interface BpmRuTaskRepository extends PagingAndSortingWithCrudRepository<BpmRuTask, Long>, BpmRuTaskRepositoryCustom {

    BpmRuTask findByActInstIdAndTaskDefKeyAndName(Long actInstId, String taskDefKey, String taskName);

    BpmRuTask findByActInstIdAndTaskDefKeyAndNameAndAssignee(Long actInstId, String taskDefKey, String taskName, String assignee);

    List<BpmRuTask> findByIdIn(Long[] taskId);

    List<BpmRuTask> findByActInstId(Long actInstId);

    List<BpmRuTask> findByActInstIdAndCategory(Long actInstId, String category);

    List<BpmRuTask> findByActInstIdIn(Long[] actInstIds);

    /**
     * 更新运行中的申请状态
     */
    @Modifying
    @Transactional
    @Query("UPDATE BpmRuTask n SET n.applyStatus = :applyStatus WHERE n.id in (:taskIds) and n.applyStatus is null")
    void updateStatus(
        @Param("applyStatus") ExInspApplyStatus applyStatus,
        @Param("taskIds") List<Long> taskIds
    );

    /**
     * 更新待办任务状态
     */
    @Modifying
    @Transactional
    @Query("UPDATE BpmRuTask n SET n.isHandling = :isHandling WHERE n.id in (:taskIds) ")
    void updateRunningStatus(
        @Param("isHandling") Boolean isHandling,
        @Param("taskIds") List<Long> taskIds
    );

    BpmRuTask findByIdAndAssignee(Long taskId, String assignee);

    BpmRuTask findByIdAndSignFlag(Long taskId, boolean flag);

    List<BpmRuTask> findByIdOrderByCreateTime(Long taskId);

    BpmRuTask findByActInstIdAndTaskDefKey(Long actInstId, String taskDefKey);

    BpmRuTask findByActInstIdAndTaskDefKeyAndSeq(Long actInstId, String taskDefKey, int seq);

    /**
     * 更新运行中任务的报告信息
     */
    @Modifying
    @Transactional
    @Query("UPDATE BpmRuTask n SET n.reports = :reportJson WHERE n.actInstId in (:actInstIds) and n.tenantId = :projectId")
    void updateTaskReportInfo(@Param("projectId") String projectId,
                              @Param("reportJson") String reportJson, @Param("actInstIds") Set<Long> actInstIds);

    BpmRuTask findFirstByIdAndSignFlag(Long taskId, boolean flag);

    BpmRuTask findFirstByActInstId(Long actInstId);


    @Query(value = "SELECT SUM(1) AS totalCount, " +
        "MIN(task_def_key_) AS minKey, " +
        "MAX(task_def_key_) AS maxKey " +
        "FROM " +
        "bpm_ru_task AS rt " +
        "WHERE id IN :taskIds AND suspension_state_ = 1",
        nativeQuery = true)
    Tuple getStatisticData(@Param("taskIds") List<Long> taskIds);

    @Query("SELECT r FROM BpmRuTask r WHERE r.suspensionState = :suspensionState")
    List<BpmRuTask> findBySuspensionState(@Param("suspensionState") int suspensionState,
                                          Pageable pageable);

    @Query("SELECT COUNT(1) FROM BpmRuTask rt WHERE rt.actInstId = :actInstId AND rt.taskDefKey = :taskDefKey")
    BigInteger getRunTaskCount(@Param("actInstId") Long actInstId, @Param("taskDefKey") String taskDefKey);

    @Query("SELECT COUNT(1) FROM BpmActivityInstanceBase baib LEFT JOIN BpmRuTask rt ON baib.id = rt.actInstId" +
        " WHERE baib.orgId = :orgId AND baib.projectId = :projectId AND rt.assignee = :assigneeId")
    BigInteger getCurrentExecutorRunTaskCount(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("assigneeId") String assigneeId
    );
}
