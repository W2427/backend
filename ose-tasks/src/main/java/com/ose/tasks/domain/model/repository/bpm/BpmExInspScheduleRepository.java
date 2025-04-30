package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.vo.qc.ReportStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.vo.EntityStatus;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


public interface BpmExInspScheduleRepository extends PagingAndSortingWithCrudRepository<BpmExInspSchedule, Long>,
    BpmExInspScheduleRepositoryCustom {

    List<BpmExInspSchedule> findByIdInAndStatusAndState(List<Long> exInspScheduleIds, EntityStatus stauts, ReportStatus state);

    @Transactional
    @Modifying
    @Query("UPDATE BpmExInspSchedule eis SET eis.status = :deleted WHERE eis.id IN (:externalInspectionApplyScheduleIds)")
    void deleteByScheduleIds(@Param("externalInspectionApplyScheduleIds") List<Long> externalInspectionApplyScheduleIds,
                             @Param("deleted") EntityStatus deleted);

    @Query(value = "SELECT * FROM bpm_external_inspection_confirm beic JOIN bpm_external_inspection_schedule beis ON beic.schedule_id = beis.id " +
        " WHERE beic.qrcode =:qrCode AND beis.status = :status", nativeQuery = true)
    BpmExInspSchedule findByReportQrCode(@Param("qrCode") String reportQrCode, @Param("status") EntityStatus status);

    BpmExInspSchedule findByIdAndStatus(Long scheduleId, EntityStatus active);


    @Query(value = "SELECT CONCAT(qr.series_no,'') FROM bpm_external_inspection_schedule beis JOIN qc_report qr ON beis.id = qr.schedule_id " +
        "WHERE beis.project_id = :projectId AND beis.id = :scheduleId", nativeQuery = true)
    List<String> findSeriesNosByProjectIdAndScheduleId(@Param("projectId") Long projectId, @Param("scheduleId") Long scheduleId);


    @Transactional
    @Modifying
    @Query("UPDATE BpmExInspSchedule eis SET eis.state = :running WHERE eis.id IN (:exInspScheduleIds)")
    void setRunningByScheduleIds(@Param("running") ReportStatus running,
                                 @Param("exInspScheduleIds") List<Long> exInspScheduleIds);


    List<BpmExInspSchedule> findByOrgIdAndProjectId(Long orgId, Long projectId, Pageable pageable);

    List<BpmExInspSchedule> findByOrgIdAndProjectIdOrderByCreatedAtDesc(Long orgId, Long projectId);

}
