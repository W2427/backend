package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;
import java.util.Optional;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ose.tasks.entity.bpm.BpmActTaskAssignee;


/**
 * 实体 CRUD 操作接口。
 */
@Transactional
public interface BpmActTaskAssigneeRepository extends PagingAndSortingWithCrudRepository<BpmActTaskAssignee, Long> {

    List<BpmActTaskAssignee> findByActInstId(Long actInstId);

    Optional<BpmActTaskAssignee> findByActInstIdAndTaskDefKeyAndTaskName(Long actInstId, String taskDefKey, String taskName);

    BpmActTaskAssignee findByActInstIdAndTaskDefKey(Long actInstId, String taskDefKey);

    BpmActTaskAssignee findByActInstIdAndTaskDefKeyAndAssignee(Long actInstId, String taskDefKey, Long assignee);

    BpmActTaskAssignee findByActInstIdAndTaskDefKeyAndAssigneeAndSeq(Long actInstId, String taskDefKey, Long assignee, int seq);

    BpmActTaskAssignee findByActInstIdAndTaskDefKeyAndStatus(Long actInstId, String taskDefKey, EntityStatus entityStatus);

    List<BpmActTaskAssignee> findAllByActInstIdAndTaskDefKeyAndStatus(Long actInstId, String taskDefKey, EntityStatus entityStatus);


    @Query(
        value = "select t1 from" +
            " BpmActTaskAssignee t1," +
            " BpmActivityTaskNodePrivilege t2," +
            " BpmActivityInstanceBase t3" +
            " where t1.actInstId = t3.id and t3.processId = t2.processId and t1.taskDefKey = t2.taskDefKey" +
            " and t2.status = 'ACTIVE' and t1.status = 'ACTIVE' and t3.status = 'ACTIVE'" +
            " and t3.id = :actInstId" +
            " and t2.category in (" +
            " select t4.category" +
            " from BpmActivityTaskNodePrivilege t4 where t4.processId = t3.processId and t4.taskDefKey = :defKey and t3.status = 'ACTIVE'" +
            " )"
    )
    List<BpmActTaskAssignee> query(@Param("actInstId") Long actInstId, @Param("defKey") String defKey);


    @Query(
        value = "select t1 from" +
            " BpmActTaskAssignee t1 " +
            " RIGHT JOIN BpmActivityInstanceBase t2 on t1.actInstId = t2.id" +
            " where t2.id in (:actInstIds) and t1.taskCategory = :category"
    )
    List<BpmActTaskAssignee> findByTaskCategoryAndActInstIdIn(@Param("category") String category, @Param("actInstIds") List<Long> actInstIds);

    List<BpmActTaskAssignee> findByActInstIdAndTaskCategory(Long actInstId, String category);
}
