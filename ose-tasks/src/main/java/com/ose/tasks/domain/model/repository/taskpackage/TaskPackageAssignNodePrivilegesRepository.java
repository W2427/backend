package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.auth.vo.UserPrivilege;
import com.ose.tasks.entity.taskpackage.TaskPackageAssignNodePrivileges;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * 任务包数据仓库。
 */
public interface TaskPackageAssignNodePrivilegesRepository extends CrudRepository<TaskPackageAssignNodePrivileges, Long> {

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(
        value = "SELECT  " +
            "  p.id, " +
            "  p.project_id AS projectId, " +
            "  ps.name_en AS stageName, " +
            "  p.name_en AS processName, " +
            "  anp.privilege, " +
            "  wpanp.team_id AS teamId, " +
            "  wpanp.user_id AS userId " +
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
            "  INNER JOIN bpm_activity_node_privilege AS anp  " +
            "    ON anp.project_id = wp.project_id  " +
            "    AND anp.process_id = p.id  " +
            "    AND anp.status = 'ACTIVE'  " +
            "  LEFT JOIN task_package_assign_node_privilege AS wpanp  " +
            "    ON wpanp.project_id = wp.project_id  " +
            "    AND wpanp.task_package_id = wp.id" +
            "    AND wpanp.process_id = p.id  " +
            "    AND anp.privilege = wpanp.privilege  " +
            "    AND anp.status = 'ACTIVE'  " +
            "WHERE wp.project_id = :projectId  " +
            "  AND wp.id = :taskPackageId  " +
            "GROUP BY p.id, " +
            "  anp.privilege, " +
            "  wpanp.team_id, " +
            "  wpanp.user_id  " +
            "ORDER BY p.id",
        nativeQuery = true
    )
    List<Map<String, Object>> findProcessDelegateByProjectIdAndTaskPackageId(@Param("projectId") Long projectId, @Param("taskPackageId") Long taskPackageId);

    List<TaskPackageAssignNodePrivileges> findByProjectIdAndTaskPackageIdAndProcessId(Long projectId, Long taskPackageId, Long processId);

    TaskPackageAssignNodePrivileges findByProjectIdAndTaskPackageIdAndProcessIdAndPrivilege(Long projectId, Long taskPackageId,
                                                                                            Long processId, UserPrivilege privilege);

}
