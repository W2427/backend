package com.ose.tasks.domain.model.repository.bpm;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ose.tasks.entity.bpm.BpmActTask;

public interface BpmActTaskRepository extends PagingAndSortingWithCrudRepository<BpmActTask, Long> {

    @Query("SELECT sum(t.costHour) FROM BpmActTask t where t.actInstId = :id")
    Object getSumCostHourByActInstId(@Param("id") Long actInstId);

    BpmActTask findByTaskId(Long taskId);

    List<BpmActTask> findByActInstId(Long actInstId);

    @Query("SELECT t FROM BpmActTask t "
        + "left join BpmHiTaskinst h on t.taskId = h.taskId "
        + "where t.actInstId = :actInstId and h.taskDefKey = :taskDefKey "
        + "order by t.createdAt desc")
    List<BpmActTask> findByActInstIdAndTaskDefKey(@Param("actInstId") Long actInstId, @Param("taskDefKey") String taskDefKey);

    List<BpmActTask> findByActInstIdOrderByCreatedAtAsc(Long actInstId);

    List<BpmActTask> findByActInstIdAndExecutorRoleInAndExecutorsIsNotNullOrderByCreatedAtDesc(Long actInstId, Collection executorRoles);

    List<BpmActTask> findByActInstIdOrderByCreatedAtDesc(Long actInstId);

    @Query("SELECT r FROM BpmActTask r "
        + "WHERE r.actInstId = :actInstId AND r.createdAt >= :endTime ")
    List<BpmActTask> getAfterBpmActTask(Long actInstId, Date endTime);
}
