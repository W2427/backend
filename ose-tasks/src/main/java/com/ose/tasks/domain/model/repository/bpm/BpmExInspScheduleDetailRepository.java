package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmExInspScheduleDetail;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.transaction.annotation.Transactional;

public interface BpmExInspScheduleDetailRepository extends PagingAndSortingWithCrudRepository<BpmExInspScheduleDetail, Long>, BpmExInspScheduleDetailRepositoryCustom {

    List<BpmExInspScheduleDetail> findByScheduleId(Long scheduleId);


    @Transactional
    @Modifying
    @Query("UPDATE BpmExInspScheduleDetail eisd SET eisd.status = :deleted WHERE eisd.id IN (:externalInspectionApplyScheduleIds)")
    void deleteByScheduleIds(@Param("externalInspectionApplyScheduleIds") List<Long> externalInspectionApplyScheduleIds,
                             @Param("deleted") EntityStatus deleted);


    List<BpmExInspScheduleDetail> findByScheduleIdInAndStatus(List<Long> exInspScheduleIdList, EntityStatus status);

    List<BpmExInspScheduleDetail> findByScheduleIdAndStatus(Long exInspScheduleId, EntityStatus status);
}


