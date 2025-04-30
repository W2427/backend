package com.ose.tasks.domain.model.repository.qc;

import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.report.ExInspActInstRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * EXINSP ACTINST RELATION CRUD 操作接口。
 */
public interface ExInspActInstRelationRepository extends PagingAndSortingWithCrudRepository<ExInspActInstRelation, Long> {

    List<ExInspActInstRelation> findByOrgIdAndProjectIdAndExInspScheduleIdAndActInstId(Long orgId, Long projectId, Long scheduleId, Long actInstId);


    @Query("SELECT new com.ose.dto.jpql.TaskProcQLDTO(rt.id, rt.actInstId, rt.inspectResult) FROM ExInspActInstRelation r JOIN BpmRuTask rt ON r.actInstId = rt.actInstId " +
        "WHERE r.orgId = :orgId AND r.projectId = :projectId AND r.exInspScheduleId = :scheduleId AND r.status = 'ACTIVE'")
    List<TaskProcQLDTO> findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(@Param("orgId") Long orgId,
                                                                         @Param("projectId") Long projectId,
                                                                         @Param("scheduleId") Long scheduleId);

    @Transactional
    @Modifying
    @Query("UPDATE ExInspActInstRelation r SET r.status = 'DELETED' WHERE r.actInstId = :actInstId")
    void deleteByActInstId(@Param("actInstId") Long actInstId);

    @Query("SELECT new com.ose.dto.jpql.TaskProcQLDTO(rt.id, rt.actInstId, rt.inspectResult) FROM ExInspActInstRelation r JOIN BpmRuTask rt ON r.actInstId = rt.actInstId " +
        "WHERE r.orgId = :orgId AND r.projectId = :projectId AND r.reportId = :reportId AND r.status = 'ACTIVE'")
    List<TaskProcQLDTO> findTaskIdByOrgIdAndProjectIdAndReportId(@Param("orgId") Long orgId,
                                                                 @Param("projectId") Long projectId,
                                                                 @Param("reportId") Long reportId);

    @Query("SELECT new com.ose.dto.jpql.TaskProcQLDTO(rt.id, rt.actInstId, rt.inspectResult) FROM ExInspActInstRelation r JOIN BpmRuTask rt ON r.actInstId = rt.actInstId " +
        "WHERE r.orgId = :orgId AND r.projectId = :projectId AND r.exInspScheduleId IN :scheduleIds AND r.status = 'ACTIVE'")
    List<TaskProcQLDTO> findTaskIdByOrgIdAndProjectIdAndExInspScheduleIdIn(@Param("orgId") Long orgId,
                                                                           @Param("projectId") Long projectId,
                                                                           @Param("scheduleIds") List<Long> exInspApplyScheduleIds);

    List<ExInspActInstRelation> findByActInstId(Long actInstId);

    List<ExInspActInstRelation> findByExInspScheduleId(Long scheduleId);

    ExInspActInstRelation findByOrgIdAndProjectIdAndReportId(Long orgId,Long projectId,Long reportId);
}
