package com.ose.tasks.domain.model.repository.p6;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.p6.P6Taskpred;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 实体操作接口。
 */
@Transactional
public interface P6TaskPredRepository extends PagingAndSortingWithCrudRepository<P6Taskpred, Integer> {

    @Query("SELECT MAX(pw.taskPredId) FROM P6Taskpred pw")
    Integer getMaxId();
}
