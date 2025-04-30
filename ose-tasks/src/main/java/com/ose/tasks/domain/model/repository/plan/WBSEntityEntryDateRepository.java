package com.ose.tasks.domain.model.repository.plan;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wbs.entry.WBSEntityEntryDate;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface WBSEntityEntryDateRepository extends PagingAndSortingWithCrudRepository<WBSEntityEntryDate, Long>, WBSEntityEntryDateRepositoryCustom {


    @Query("SELECT e FROM WBSEntityEntryDate e WHERE e.projectId = :projectId AND e.entityId = :entityId " +
        " AND e.processId = :processId AND e.deleted = false")
    WBSEntityEntryDate findByProjectIdAndEntityIdAndProcessId(@Param("projectId") Long projectId,
                                                              @Param("entityId") Long entityId,
                                                              @Param("processId") Long processId);


    @Query("SELECT e FROM WBSEntityEntryDate e WHERE e.projectId = :projectId AND e.entityId = :entityId AND e.deleted = false ")
    List<WBSEntityEntryDate> findByProjectIdAndEntityId(@Param("projectId") Long projectId,
                                                        @Param("entityId") Long entityId);
}
