package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.report.QCReport;
import com.ose.vo.InspectResult;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.util.List;
import java.util.Set;


public interface QCReportRepository extends PagingAndSortingWithCrudRepository<QCReport, Long>, QCReportRepositoryCustom {

    QCReport findByQrcodeAndReportStatus(String qrcode, ReportStatus status);

    @Query("SELECT MAX(e.seriesNo) FROM QCReport e WHERE e.projectId = :projectId GROUP BY e.projectId")
    Long getMaxSeriesNo(@Param("projectId") Long projectId);

    QCReport findByQrcode(String qrcode);


    @Query("SELECT qr FROM QCReport qr JOIN ExInspActInstRelation er ON " +
        " qr.id = er.reportId WHERE qr.projectId = :projectId AND er.actInstId = :actInstId " +
        "AND er.status = 'ACTIVE' AND qr.reportStatus <> :reportStatus")
    List<QCReport> findByProjectIdAndActInstIdsLikeAndReportStatusNot(
        @Param("projectId") Long projectId,
        @Param("actInstId") String actInstIds,
        @Param("reportStatus") ReportStatus reportStatus
    );

    List<QCReport> findByProjectIdAndProcessAndReportNoLikeAndReportStatusNot(
        Long projectId,
        String process,
        String reportNo,
        ReportStatus reportStatus
    );

    List<QCReport> findByProjectIdAndReportSubTypeAndModuleNameAndReportStatusNot(
        Long projectId,
        ReportSubType reportSubType,
        String moduleName,
        ReportStatus reportStatus
    );

    List<QCReport> findByProjectIdAndReportSubTypeAndReportStatusNot(
        Long projectId,
        ReportSubType reportSubType,
        ReportStatus reportStatus
    );

    List<QCReport> findByOrgIdAndProjectId(
        Long orgId,
        Long projectId
    );

    List<QCReport> findByProjectId(Long projectId, Pageable pageable);

    QCReport findByProjectIdAndSeriesNo(Long projectId, String seriesNo);

    @Query("SELECT r FROM QCReport r WHERE r.projectId = :projectId AND r.seriesNo = :seriesNo AND r.reportStatus IN :statusSet")
    QCReport findByProjectIdAndSeriesNoAndReportStatus(@Param("projectId") Long projectId,
                                                       @Param("seriesNo") String seriesNo,
                                                       @Param("statusSet") Set<ReportStatus> statusSet);

    @Transactional
    @Modifying
    @Query("UPDATE QCReport qcr SET qcr.reportStatus = :status WHERE qcr.scheduleId IN (:externalInspectionApplyScheduleIds) AND qcr.status = com.ose.vo.EntityStatus.ACTIVE " +
        " AND (NOT qcr.reportStatus = com.ose.tasks.vo.qc.ReportStatus.CANCEL)")
    void updateReportStatus(@Param("externalInspectionApplyScheduleIds") List<Long> externalInspectionApplyScheduleIds,
                            @Param("status") ReportStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE QCReport qcr SET qcr.reportStatus = :status WHERE qcr.id IN (:reportIds) AND qcr.status = com.ose.vo.EntityStatus.ACTIVE AND (NOT qcr.reportStatus = com.ose.tasks.vo.qc.ReportStatus.CANCEL)")
    void updateReportStatusByIds(@Param("reportIds") Set<Long> reportIds,
                                 @Param("status") ReportStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE QCReport qcr SET qcr.reportStatus = :reportStatus WHERE qcr.scheduleId = :exInspScheduleId AND qcr.status = com.ose.vo.EntityStatus.ACTIVE " +
        " AND (NOT qcr.reportStatus = com.ose.tasks.vo.qc.ReportStatus.CANCEL)")
    void updateReportStatusByScheduleId(@Param("exInspScheduleId") Long exInspScheduleId,
                                        @Param("reportStatus") ReportStatus reportStatus);

//    QCReport findByProjectIdAndSeriesNo(Long projectId, Integer integer);

    List<QCReport> findByProjectIdAndScheduleId(Long projectId, Long scheduleId);

    @Query("SELECT MAX(r.seriesNum) FROM QCReport r WHERE r.projectId = :projectId AND r.status = com.ose.vo.EntityStatus.ACTIVE " +
        " AND (NOT r.reportStatus = com.ose.tasks.vo.qc.ReportStatus.CANCEL)")
    Integer getReportSnByProjectId(@Param("projectId") Long projectId);

    QCReport findByScheduleIdAndReportStatus(Long scheduleDetailId, ReportStatus status);

    QCReport findByScheduleId(Long scheduleDetailId);

    QCReport findByProjectIdAndQrcodeAndStatus(Long projectId, String qrcode, EntityStatus status);

    QCReport findByQrcodeAndStatus(String reportQrCode, EntityStatus status);

    @Query(value = "SELECT SUM(1) AS totalCount, " +
        "SUM(CASE WHEN report_status IN :statusSet THEN 0 ELSE 1 END) AS unConfirmedCount, " +
        "MAX(inspect_result) AS inspectResult " +
        "FROM " +
        "qc_report AS report " +
        "WHERE schedule_id = :scheduleId AND status = 'ACTIVE' AND report_status <> 'CANCEL'",
        nativeQuery = true)
    Tuple findUploadedCount(@Param("scheduleId") Long scheduleId,
                            @Param("statusSet") Set<String> statusSet);

    @Query(value = "SELECT SUM(1) AS totalCount, " +
        "SUM(CASE WHEN report_status IN :statusSet THEN 1 ELSE 0 END) AS unConfirmedCount, " +
        "MAX(inspect_result) AS inspectResult " +
        "FROM " +
        "qc_report AS report " +
        "WHERE schedule_id = :scheduleId AND status = 'ACTIVE' AND report_status <> 'CANCEL'",
        nativeQuery = true)
    Tuple findRehandleCount(@Param("scheduleId") Long scheduleId,
                            @Param("statusSet") Set<String> statusSet);

    List<QCReport> findByScheduleIdAndReportStatusNot(Long scheduleId, ReportStatus cancel);

    @Transactional
    @Modifying
    @Query("UPDATE QCReport qcr SET qcr.reportStatus = :reportStatus WHERE qcr.scheduleId = :scheduleId " +
        "AND qcr.status = com.ose.vo.EntityStatus.ACTIVE AND (NOT qcr.reportStatus = :status)")
    void updateReportStatusByScheduleIdAndReportStatusNot(@Param("reportStatus") ReportStatus reportStatus,
                                                          @Param("scheduleId") Long scheduleId,
                                                          @Param("status") ReportStatus status);

    @Query("SELECT qcr FROM QCReport qcr JOIN BpmExInspSchedule ei ON qcr.scheduleId = ei.id" +
        " WHERE qcr.projectId = :projectId AND (qcr.reportNo = :reportNo OR qcr.seriesNo = :reportNo) AND qcr.operator = :operator " +
        "AND qcr.reportStatus IN :statuses AND ei.state = 'UPLOADED'")
    Page<QCReport> findByProjectIdAndReportNoAndReportStatusIn(@Param("projectId") Long projectId,
                                                                       @Param("reportNo") String reportNo,
                                                                       @Param("statuses") List<ReportStatus> statuses,
                                                                       @Param("operator") Long operator,
                                                                       Pageable pageable);

    @Query("SELECT qcr FROM QCReport qcr JOIN BpmExInspSchedule ei ON qcr.scheduleId = ei.id" +
        " WHERE qcr.projectId = :projectId AND qcr.reportStatus IN :statuses AND qcr.operator = :operator AND ei.state = 'UPLOADED'")
    Page<QCReport> findByProjectIdAndReportStatusIn(@Param("projectId") Long projectId,
                                                    @Param("statuses") List<ReportStatus> statuses,
                                                    @Param("operator") Long operator,
                                                    Pageable pageable);

    @Query(value = "SELECT CONCAT(report_type, '_', IFNULL(report_sub_type,'')) AS _key, id AS reportId, series_num AS reportNum " +
        "FROM qc_report WHERE project_id = :projectId AND schedule_id = :exInspScheduleId AND status = 'ACTIVE' " +
        " AND report_type = :reportType AND report_sub_type = :reportSubType",
        nativeQuery = true)
    List<Tuple> findOldReportIds(@Param("projectId") Long projectId,
                                 @Param("exInspScheduleId") Long exInspScheduleId,
                                 @Param("reportType") String reportType,
                                 @Param("reportSubType") String reportSubType);

    @Transactional
    @Modifying
    @Query("UPDATE QCReport qcr SET qcr.isReportBatchConfirmed = :reportBatchConfirmed WHERE " +
        " qcr.scheduleId = :exInspScheduleId AND qcr.status = com.ose.vo.EntityStatus.ACTIVE " +
        " AND (NOT qcr.reportStatus = com.ose.tasks.vo.qc.ReportStatus.CANCEL)")
    void updateBatchConfirmed(@Param("exInspScheduleId") Long exInspScheduleId,
                              @Param("reportBatchConfirmed") Boolean reportBatchConfirmed);

    List<QCReport> findByProjectIdAndQrcodeInAndStatus(Long projectId, Set<String> qrCodes, EntityStatus status);


    @Query(value = "SELECT " +
        "series_no " +
        "FROM " +
        "qc_report " +
        "WHERE project_id = :projectId " +
        "ORDER BY id DESC LIMIT 0,1", nativeQuery = true)
    String findMaxSn(@Param("projectId") Long projectId);

    List<QCReport> findByProjectIdAndReportStatusNot(Long projectId, ReportStatus cancel);


    @Query("SELECT qr FROM QCReport qr JOIN ExInspActInstRelation er ON qr.scheduleId = er.exInspScheduleId WHERE " +
        "er.actInstId = :actInstId AND er.status = 'ACTIVE' AND qr.status = :status AND qr.reportStatus = :reportStatus")
    List<QCReport> findByActInstIdAndStatusAndReportStatus(@Param("actInstId") Long actInstId,
                                                            @Param("status") EntityStatus active,
                                                            @Param("reportStatus") ReportStatus reportStatus);

    @Query("SELECT qr FROM QCReport qr JOIN ExInspActInstRelation er ON qr.scheduleId = er.exInspScheduleId WHERE " +
        "er.actInstId = :actInstId AND er.status = 'ACTIVE' AND qr.status = :status AND qr.reportStatus = :reportStatus")
    List<QCReport> findByActInstIdAndStatusAndReportStatusNot(@Param("actInstId") Long actInstId,
                                                            @Param("status") EntityStatus active,
                                                            @Param("reportStatus") ReportStatus reportStatus);

    @Query("SELECT r FROM QCReport r WHERE r.projectId = :projectId AND r.status = com.ose.vo.EntityStatus.ACTIVE AND r.process = :process AND r.actInstIds LIKE :actInstIds ")
    List<QCReport> findByProjectIdAndProcessAndActInstIds(@Param("projectId") Long projectId,
                                                           @Param("process") String process,
                                                           @Param("actInstIds") String actInstIds);



    @Query("SELECT qr FROM QCReport qr JOIN ExInspActInstRelation er ON qr.id = er.reportId WHERE " +
        "er.actInstId = :actInstId AND er.status = 'ACTIVE' AND qr.status = :status AND qr.reportStatus = :reportStatus")
    List<QCReport> findByActInstIdAndStatusAndReportStatusViaReportId(@Param("actInstId") Long actInstId,
                                                            @Param("status") EntityStatus active,
                                                            @Param("reportStatus") ReportStatus reportStatus);

    @Query("SELECT r FROM QCReport r WHERE r.projectId = :projectId AND r.status = com.ose.vo.EntityStatus.ACTIVE " +
        " AND (NOT r.reportStatus = com.ose.tasks.vo.qc.ReportStatus.CANCEL) AND r.process = :process AND r.entityNos LIKE :entityNo ")
    List<QCReport> findByProjectIdAndProcessAndEntityNo(
        @Param("projectId") Long projectId,
        @Param("process") String process,
        @Param("entityNo") String entityNo);


    @Query("SELECT qr FROM QCReport qr JOIN ExInspActInstRelation er ON er.reportId = qr.id " +
        " WHERE er.actInstId = :actInstId AND qr.process = :process AND qr.reportStatus <> :reportStatus AND qr.status = :status")
    List<QCReport> findByProcessAndReportStatusNotAndActInstId(@Param("process") String processType,
                                                                @Param("status") EntityStatus status,
                                                                @Param("reportStatus") ReportStatus reportStatus,
                                                                @Param("actInstId") Long actInstId);

    List<QCReport> findByOrgIdAndProjectIdAndScheduleId(
        Long orgId,
        Long projectId,
        Long scheduleId
    );

    List<QCReport> findByProcessAndStatusAndReportStatusNotAndEntityNosLike(String processType, EntityStatus active, ReportStatus reportStatus, String entityNo);


    List<QCReport> findByProjectIdAndEntityNosLike(
        Long projectId,
        String entityNos
    );

    QCReport findByUploadFileId(Long fileId);


    List<QCReport> findByProjectIdAndProcessInAndReportStatusNotAndEntityNosLike(
        Long projectId,
        List<String> processes,
        ReportStatus reportStatus,
        String entityNos
    );

    @Query("SELECT qr FROM QCReport qr " +
        " WHERE qr.projectId = :projectId AND qr.reportStatus <> :reportStatus AND qr.entityNos like :entityNo AND qr.process in :processes")
    List<QCReport> findByProjectIdAndReportStatusNotAndEntityNosLike(@Param("projectId") Long projectId,
                                                                @Param("reportStatus") ReportStatus reportStatus,
                                                                @Param("entityNo") String entityNo,
                                                                @Param("processes") List<String> processes);

}
