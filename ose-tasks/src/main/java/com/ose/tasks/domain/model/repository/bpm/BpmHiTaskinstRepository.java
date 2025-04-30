package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmHiTaskinst;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BpmHiTaskinstRepository
    extends PagingAndSortingWithCrudRepository<BpmHiTaskinst, Long>, BpmHiTaskinstRepositoryCustom {
    /**
     * 根据procInstId查询历史任务节点
     *
     * @param actInstId
     * @return
     */
    List<BpmHiTaskinst> findByActInstIdOrderByStartTimeAsc(Long actInstId);

    /**
     * 根据taskId查询历史任务节点
     *
     * @param actInstId
     * @return
     */
    List<BpmHiTaskinst> findByTaskIdIn(String[] actInstId);

    @Query("SELECT DISTINCT t.processCategoryId FROM BpmActivityInstanceBase t LEFT JOIN BpmHiTaskinst r ON t.id = r.actInstId "
        + "WHERE t.projectId = :projectId AND t.orgId = :orgId AND t.status = 'ACTIVE' AND "
        + "(r.owner LIKE %:assignee% OR r.assignee LIKE %:assignee%)")
    List<Long> getProcessCategoriesInHiTask(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("assignee") String assignee);

    @Query("SELECT DISTINCT t.entitySubTypeId FROM BpmActivityInstanceBase t LEFT JOIN BpmHiTaskinst r ON t.id = r.actInstId "
        + "WHERE t.projectId = :projectId AND t.orgId = :orgId AND t.status = 'ACTIVE' AND "
        + "(r.owner LIKE %:assignee% OR r.assignee LIKE %:assignee%) AND t.processCategoryId = :processCategoryId")
    List<Long> getEntitiyCategoriesInHiTask(@Param("processCategoryId") Long processCategoryId, @Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("assignee") String assignee);

    @Query("SELECT distinct t.processStageId FROM BpmActivityInstanceBase t left join BpmHiTaskinst r on t.id = r.actInstId "
        + "WHERE t.projectId = :projectId and t.orgId = :orgId " + "and t.entitySubTypeId = :entitySubTypeId "
        + "and t.status = 'ACTIVE' and (r.owner like %:assignee% or r.assignee like %:assignee%) and t.processCategoryId = :processCategoryId")
    List<Long> getProcessStagesInHiTask(@Param("processCategoryId") Long processCategoryId, @Param("entitySubTypeId") Long entitySubTypeId, @Param("orgId") Long orgId,
                                        @Param("projectId") Long projectId, @Param("assignee") String assignee);

    @Query("SELECT distinct t.processId FROM BpmActivityInstanceBase t left join BpmHiTaskinst r on t.id = r.actInstId "
        + "WHERE t.projectId = :projectId and t.processStageId = :processStageId and t.orgId = :orgId "
        + "and t.entitySubTypeId = :entitySubTypeId  "
        + "and t.status = 'ACTIVE' and (r.owner like %:assignee% or r.assignee like %:assignee%) and t.processCategoryId = :processCategoryId")
    List<Long> getProcessesInHiTask(@Param("processCategoryId") Long processCategoryId, @Param("entitySubTypeId") Long entitySubTypeId, @Param("processStageId") Long processStageId,
                                    @Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("assignee") String assignee);

    @Query("SELECT DISTINCT r.name FROM BpmActivityInstanceBase t LEFT JOIN BpmHiTaskinst r ON t.id = r.actInstId "
        + "WHERE t.projectId = :projectId AND t.processStageId = :processStageId AND t.processId = :processId AND t.orgId = :orgId "
        + "AND t.entitySubTypeId = :entitySubTypeId "
        + "AND t.status = 'ACTIVE' AND (r.owner LIKE %:assignee% OR r.assignee LIKE %:assignee%) AND t.processCategoryId = :processCategoryId")
    List<String> getTaskNodesInHiTask(@Param("processCategoryId") Long processCategoryId, @Param("entitySubTypeId") Long entitySubTypeId, @Param("processStageId") Long processStageId,
                                      @Param("processId") Long processId, @Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("assignee") String assignee);

    BpmHiTaskinst findByTaskId(Long taskId);

    BpmHiTaskinst findHiTaskinstByTaskIdAndSeq(Long taskId, int seq);

    BpmHiTaskinst findFirstByActInstIdAndTaskDefKeyOrderByEndTimeDesc(Long actInstId, String taskDefKey);

    @Query("SELECT b.taskDefKey FROM BpmHiTaskinst b WHERE b.actInstId = :actInstId")
    List<String> findTaskDefKeyByActInstId(@Param("actInstId") Long actInstId);

    List<BpmHiTaskinst> findByActInstId(Long actInstId);

    List<BpmHiTaskinst> findByActInstIdAndCategory(Long actInstId, String category);

    BpmHiTaskinst findFirstByActInstIdAndCategoryOrderByEndTimeAsc(Long actInstId, String taskDefKey);

    List<BpmHiTaskinst> findByActInstIdAndName(Long actInstId,String name);

    List<BpmHiTaskinst> findByActInstIdAndEndTimeIsNull(Long actInstId);

    @Query("SELECT r FROM BpmHiTaskinst r "
        + "WHERE r.actInstId = :actInstId AND r.startTime >= :endTime ")
    List<BpmHiTaskinst> getAfterHiTask(@Param("actInstId") Long actInstId, @Param("endTime") Date endTime);

    @Query("SELECT r FROM BpmHiTaskinst r "
        + "WHERE r.actInstId = :actInstId AND r.endTime is null ")
    List<BpmHiTaskinst> getNextHiTask(@Param("actInstId") Long actInstId);

    @Query(value = "select " +
        "DISTINCT u.email  " +
        "from " +
        "saint_whale_tasks.bpm_hi_taskinst bht " +
        "left join saint_whale_auth.users u on " +
        "bht.assignee_ = u.id " +
        "where " +
        "bht.act_inst_id = :actInstId " +
        "and u.email is not null", nativeQuery = true)
    List<String> findEmailByHiAssignee(@Param("actInstId") Long actInstId);
}
