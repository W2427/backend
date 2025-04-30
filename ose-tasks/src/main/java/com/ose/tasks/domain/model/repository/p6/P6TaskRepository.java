package com.ose.tasks.domain.model.repository.p6;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.p6.P6Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 托盘下实体操作接口。
 */
@Transactional
public interface P6TaskRepository extends PagingAndSortingWithCrudRepository<P6Task, Integer> {


    @Query("SELECT MAX(pw.taskId) FROM P6Task pw")
    Integer getMaxId();


    @Query("SELECT MAX(pw.taskCode) FROM P6Task pw WHERE taskCode LIKE 'A%'")
    String getMaxCode();

    P6Task findByProjIdAndTaskCode(Integer projId, String no);

//    P6Task findByProjIdAndWbsShortName(Integer projId, String no);

    @Query("SELECT w FROM P6Task w WHERE w.projId = :projId AND " +
        "SUBSTR(w.taskName,1,5) = :no")
    List<P6Task> findByProjIdAndSubSysNo(@Param("projId") Integer origProjId,
                                    @Param("no") String no);


}
