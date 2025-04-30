package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.tasks.entity.taskpackage.TaskPackageAssignSiteTeams;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * 任务包数据仓库。
 */
public interface TaskPackageAssignSiteTeamsRepository extends CrudRepository<TaskPackageAssignSiteTeams, Long> {

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(
        value = "SELECT  " +
            "  p.id, " +
            "  p.project_id AS projectId, " +
            "  ps.name_en AS stageName, " +
            "  p.name_en AS processName, " +
            "  wpast.team_id AS teamId, " +
            "  wpast.plan_hours AS planHours, " +
            "  wpast.plan_start_date AS planStartDate, " +
            "  wpast.plan_end_date AS planEndDate, " +
            "  ws.id AS workSiteId, " +
            "  ws.name AS workSiteName " +
            "FROM " +
            "  task_package AS wp  " +
            "  INNER JOIN task_package_category AS wpc  " +
            "    ON wpc.id = wp.category_id  " +
            "    AND wpc.deleted = 0  " +
            "  INNER JOIN task_package_category_process_relation AS wpcp  " +
            "    ON wpcp.category_id = wpc.id  " +
            "  INNER JOIN bpm_process AS p  " +
            "    ON p.project_id = wp.project_id  " +
            "    AND p.id = wpcp.process_id  " +
            "    AND p.status = 'ACTIVE'  " +
            "  INNER JOIN bpm_process_stage AS ps  " +
            "    ON ps.project_id = wp.project_id  " +
            "    AND ps.id = p.process_stage_id  " +
            "    AND ps.status = 'ACTIVE'  " +
            "  LEFT JOIN task_package_assign_site_team AS wpast  " +
            "    ON wpast.project_id = wp.project_id  " +
            "    AND wpast.task_package_id = wp.id" +
            "    AND wpast.process_id = p.id  " +
            "    AND wpast.status = 'ACTIVE'  " +
            "  LEFT JOIN work_site AS ws  " +
            "    ON ws.id = wpast.work_site_id  " +
            "    AND ws.deleted = 0  " +
            "WHERE wp.project_id = :projectId  " +
            "  AND wp.id = :taskPackageId  " +
            "GROUP BY p.id, " +
            "  wpast.team_id, " +
            "  wpast.work_site_id,  " +
            "  ws.id,   " +
            "  p.project_id,   " +
            "  ps.name_en,   " +
            "  p.name_en,   " +
            "  wpast.plan_hours,   " +
            "  wpast.plan_start_date,   " +
            "  wpast.plan_end_date,   " +
            "  ws.name   " +
        "ORDER BY p.id ",
        nativeQuery = true
    )
    List<Map<String, Object>> findProcessTeamByProjectIdAndTaskPackageId(@Param("projectId") Long projectId, @Param("taskPackageId") Long taskPackageId);

    TaskPackageAssignSiteTeams findByProjectIdAndTaskPackageIdAndProcessId(Long projectId, Long taskPackageId,
                                                                           Long processId);

}
