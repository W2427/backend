package com.ose.tasks.domain.model.repository.qc;

import com.ose.tasks.entity.report.ExInspDetailActInstRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * EXINSP DETAIL ACTINST RELATION CRUD 操作接口。
 */
public interface ExInspDetailActInstRelationRepository extends PagingAndSortingRepository<ExInspDetailActInstRelation, Long> {


    @Transactional
    @Modifying
    @Query("UPDATE ExInspDetailActInstRelation r SET r.status = 'DELETED' WHERE r.actInstId = :actInstId")
    void deleteByActInstId(@Param("actInstId") Long actInstId);
}
