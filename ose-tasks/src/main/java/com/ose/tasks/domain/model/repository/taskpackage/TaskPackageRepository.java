package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.tasks.entity.taskpackage.TaskPackage;
import com.ose.tasks.entity.taskpackage.TaskPackagePercent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.math.BigInteger;
import java.util.*;

/**
 * 任务包数据仓库。
 */
public interface TaskPackageRepository extends CrudRepository<TaskPackage, Long>, TaskPackageCustomRepository {

    Optional<TaskPackage> findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long id);

    List<TaskPackage> findByOrgIdAndProjectIdAndDeletedIsFalse(Long orgId, Long projectId);

    List<TaskPackage> findByIdIn(List<Long> ids);

    @Transactional
    @Modifying
    @Query(
        value = "UPDATE"
            + "   task_package AS wp"
            + "   INNER JOIN task_package_category AS wpc"
            + "     ON wpc.id = wp.category_id"
            + "     AND wpc.deleted = 0"
            + "   INNER JOIN task_package_category_process_relation AS wpcp"
            + "     ON wpcp.category_id = wpc.id"
            + "     AND wpcp.status = 'ACTIVE'"
            + "   INNER JOIN bpm_process AS p"
            + "     ON p.id = wpcp.process_id"
            + "     AND p.status = 'ACTIVE'"
            + "   INNER JOIN bpm_process_stage AS ps"
            + "     ON ps.id = p.process_stage_id"
            + "     AND ps.status = 'ACTIVE'"
            + "   INNER JOIN wbs_entry_state AS wes"
            + "     ON wp.id = wes.task_package_id"
            + "   INNER JOIN wbs_entry AS wbs"
            + "     ON wbs.id = wes.wbs_entry_id"
            + "     AND wbs.stage = ps.name_en"
            + "     AND wbs.process = p.name_en,"
            + "   saint_whale_auth.organization AS o"
            + "   LEFT OUTER JOIN saint_whale_auth.organization AS t"
            + "     ON (t.id = o.id OR t.path LIKE CONCAT(o.path, o.id, '/%'))"
            + "     AND t.id = IFNULL(:teamId, '')"
            + "   LEFT OUTER JOIN work_site AS ws"
            + "     ON ws.company_id = o.company_id"
            + "     AND ws.id = :workSiteId"
            + "     AND (ws.project_id IS NULL OR ws.project_id = :projectId)"
            + "     AND ws.deleted = 0"
            + " SET"
            + "   wes.team_id = t.id,"
            + "   wes.work_site_id = ws.id,"
            + "   wes.work_site_name = ws.name"
            + " WHERE"
            + "   wp.project_id = :projectId"
            + "   AND wp.id = :taskPackageId"
            + "   AND (p.id = :processId OR CONCAT(ps.name_en, '/', p.name_en) = :processId)"
            + "   AND (wes.running_status IS NULL OR (wes.running_status <> 'RUNNING' AND wes.running_status <> 'APPROVED'))"
            + "   AND o.id = wp.org_id",
        nativeQuery = true
    )
    void updateProcessTeamByProjectIdAndWokPackageId(
        @Param("projectId") Long projectId,
        @Param("taskPackageId") Long taskPackageId,
        @Param("processId") Long processId,
        @Param("teamId") Long teamId,
        @Param("workSiteId") Long workSiteId
    );

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(
        value = "SELECT"
            + "   p.id,"
            + "   p.project_id     AS projectId,"
            + "   ps.name_en       AS stageName,"
            + "   p.name_en        AS processName,"
            + "   t.id             AS teamId,"
            + "   t.name           AS teamName,"
            + "   wes.work_site_id AS workSiteId,"
            + "   ws.name          AS workSiteName,"
            + "   COUNT(wbs.id)    AS wbsCount,"
            + "   SUM(CASE wes.running_status WHEN 'APPROVED' THEN 1 WHEN 'RUNNING' THEN 1 ELSE 0 END) AS wbsApprovedCount"
            + " FROM"
            + "   task_package AS wp"
            + "   INNER JOIN task_package_category AS wpc"
            + "     ON wpc.id = wp.category_id"
            + "     AND wpc.deleted = 0"
            + "   INNER JOIN task_package_category_process_relation AS wpcp"
            + "     ON wpcp.category_id = wpc.id"
            + "   INNER JOIN bpm_process AS p"
            + "     ON p.project_id = wp.project_id"
            + "     AND p.id = wpcp.process_id"
            + "     AND p.status = 'ACTIVE'"
            + "   INNER JOIN bpm_process_stage AS ps"
            + "     ON ps.project_id = wp.project_id"
            + "     AND ps.id = p.process_stage_id"
            + "     AND ps.status = 'ACTIVE'"
            + "   INNER JOIN wbs_entry_state AS wes"
            + "     ON wes.task_package_id = wp.id"
            + "   INNER JOIN wbs_entry AS wbs"
            + "     ON wbs.id = wes.wbs_entry_id"
            + "     AND wbs.stage = ps.name_en"
            + "     AND wbs.process = p.name_en"
            + "     AND wbs.deleted = 0"
            + "   LEFT OUTER JOIN saint_whale_auth.organizations AS t"
            + "     ON t.id = wes.team_id"
            + "     AND t.deleted = 0"
            + "   LEFT OUTER JOIN work_site AS ws"
            + "     ON ws.id = wes.work_site_id"
            + "     AND ws.deleted = 0"
            + " WHERE"
            + "   wp.project_id = :projectId"
            + "   AND wp.id = :taskPackageId"
            + " GROUP BY"
            + "   p.id,"
            + "   wes.team_id,"
            + "   wes.work_site_id"
            + " ORDER BY"
            + "   p.id",
        nativeQuery = true
    )
    List<Map<String, Object>> findProcessTeamByProjectIdAndTaskPackageId(
        @Param("projectId") Long projectId,
        @Param("taskPackageId") Long taskPackageId
    );

    @Modifying
    @Transactional
    @Query(
        value = "INSERT INTO wbs_entry_delegate ("
            + "   id,"
            + "   wbs_entry_id,"
            + "   privilege,"
            + "   team_id,"
            + "   user_id,"
            + "   created_at,"
            + "   created_by,"
            + "   last_modified_at,"
            + "   last_modified_by,"
            + "   version,"
            + "   deleted,"
            + "   status"
            + " )"
            + " ("
            + "   SELECT"
            + "     GENERATE_ENTITY_DEC_ID(),"
            + "     wbs.id,"
            + "     :privilege,"
            + "     t.id,"
            + "     u.id,"
            + "     CURRENT_TIMESTAMP(),"
            + "     :operatorId,"
            + "     CURRENT_TIMESTAMP(),"
            + "     :operatorId,"
            + "     UNIX_TIMESTAMP(),"
            + "     0,"
            + "     'ACTIVE'"
            + "   FROM"
            + "     task_package AS wp"
            + "     INNER JOIN task_package_category AS wpc"
            + "       ON wpc.id = wp.category_id"
            + "       AND wpc.deleted = 0"
            + "     INNER JOIN task_package_category_process_relation AS wpcp"
            + "       ON wpcp.category_id = wpc.id"
            + "       AND wpcp.status = 'ACTIVE'"
            + "     INNER JOIN bpm_process AS p"
            + "       ON p.id = wpcp.process_id"
            + "       AND p.status = 'ACTIVE'"
            + "     INNER JOIN bpm_process_stage AS ps"
            + "       ON ps.id = p.process_stage_id"
            + "       AND ps.status = 'ACTIVE'"
            + "     INNER JOIN wbs_entry_state AS wes"
            + "       ON wes.task_package_id = wp.id"
            + "     INNER JOIN wbs_entry AS wbs"
            + "       ON wbs.id = wes.wbs_entry_id"
            + "       AND wbs.stage = ps.name_en"
            + "       AND wbs.process = p.name_en"
            + "     LEFT OUTER JOIN saint_whale_auth.user AS u"
            + "       ON u.id = IFNULL(:userId, ''),"
            + "     saint_whale_auth.organization AS o"
            + "     LEFT OUTER JOIN saint_whale_auth.organization AS t"
            + "       ON (t.id = o.id OR t.path LIKE CONCAT(o.path, o.id, '/%'))"
            + "       AND t.id = IFNULL(:teamId, '')"
            + "   WHERE"
            + "     wp.project_id = :projectId"
            + "     AND wp.id = :taskPackageId"
            + "     AND (p.id = :processId OR CONCAT(ps.name_en, '/', p.name_en) = :processId)"
            + "     AND (wes.running_status IS NULL OR (wes.running_status <> 'RUNNING' AND wes.running_status <> 'APPROVED'))"
            + "     AND o.id = wp.org_id"
            + " )"
            + " ON DUPLICATE KEY UPDATE"
            + "   team_id = :teamId,"
            + "   user_id = :userId,"
            + "   last_modified_at = CURRENT_TIMESTAMP(),"
            + "   last_modified_by = :operatorId",
        nativeQuery = true
    )
    void updateProcessDelegateByProjectIdAndTaskPackageId(
        @Param("operatorId") Long operatorId,
        @Param("projectId") Long projectId,
        @Param("taskPackageId") Long taskPackageId,
        @Param("processId") Long processId,
        @Param("privilege") String privilege,
        @Param("teamId") Long teamId,
        @Param("userId") Long userId
    );

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(
        value = "SELECT"
            + "   p.id,"
            + "   p.project_id          AS projectId,"
            + "   ps.name_en            AS stageName,"
            + "   p.name_en             AS processName,"
            + "   wed.privilege,"
            + "   t.id                   AS teamId,"
            + "   t.name                 AS teamName,"
            + "   u.id                   AS userId,"
            + "   u.name                 AS userName,"
            + "   COUNT(DISTINCT wbs.id) AS wbsCount,"
            + "   SUM(CASE wes.running_status WHEN 'APPROVED' THEN 1 WHEN 'RUNNING' THEN 1 ELSE 0 END) AS wbsApprovedCount"
            + " FROM"
            + "   task_package AS wp"
            + "   INNER JOIN task_package_category AS wpc"
            + "     ON wpc.id = wp.category_id"
            + "     AND wpc.deleted = 0"
            + "   INNER JOIN task_package_category_process_relation AS wpcp"
            + "     ON wpcp.category_id = wpc.id"
            + "   INNER JOIN bpm_process AS p"
            + "     ON p.project_id = wp.project_id"
            + "     AND p.id = wpcp.process_id"
            + "     AND p.status = 'ACTIVE'"
            + "   INNER JOIN bpm_process_stage AS ps"
            + "     ON ps.project_id = wp.project_id"
            + "     AND ps.id = p.process_stage_id"
            + "     AND ps.status = 'ACTIVE'"
            + "   INNER JOIN wbs_entry_state AS wes"
            + "     ON wes.task_package_id = wp.id"
            + "   INNER JOIN wbs_entry AS wbs"
            + "     ON wbs.id = wes.wbs_entry_id"
            + "     AND wbs.stage = ps.name_en"
            + "     AND wbs.process = p.name_en"
            + "     AND wbs.deleted = 0"
            + "   INNER JOIN wbs_entry_delegate AS wed"
            + "     ON wed.wbs_entry_id = wbs.id"
            + "     AND wed.deleted = 0"
            + "   LEFT OUTER JOIN saint_whale_auth.organization AS t"
            + "     ON t.id = wed.team_id"
            + "     AND t.deleted = 0"
            + "   LEFT OUTER JOIN saint_whale_auth.user AS u"
            + "     ON u.id = wed.user_id"
            + "     AND u.deleted = 0"
            + " WHERE"
            + "   wp.project_id = :projectId"
            + "   AND wp.id = :taskPackageId"
            + " GROUP BY "
            + "   p.id,"
            + "   wed.privilege,"
            + "   wed.team_id,"
            + "   wed.user_id"
            + " ORDER BY"
            + "   p.id",
        nativeQuery = true
    )
    List<Map<String, Object>> findProcessDelegateByProjectIdAndTaskPackageId(
        @Param("projectId") Long projectId,
        @Param("taskPackageId") Long taskPackageId
    );

    @Query(
        value = "SELECT tp.id FROM "
            + "   task_package AS tp"
            + "   INNER JOIN task_package_category_process_relation AS tpcp"
            + "      ON tpcp.category_id = tp.category_id"
            + "   INNER JOIN task_package_entity_relation AS tpe"
            + "      ON tpe.task_package_id = tp.id"
            + "   INNER JOIN project_node pn on tpe.entity_id = pn.id "
            + "   INNER JOIN hierarchy_node_relation hnr on pn.id = hnr.node_ancestor_id "
            + "   INNER JOIN wbs_entry AS e ON e.project_id = tp.project_id AND e.process_id = tpcp.process_id "
            + "     AND e.entity_id = hnr.entity_id AND e.deleted = 0"
            + " WHERE"
            + "   hnr.project_id = :projectId"
            + "   AND hnr.entity_id = :entityId",
        nativeQuery = true
    )
    List<BigInteger> getTaskPackageId(@Param("projectId") Long projectId,
                                      @Param("entityId") Long entityId);


    @Transactional
    @Modifying
    @Query(value = "UPDATE TaskPackage tp " +
        " SET tp.totalCount = :count, tp.totalWorkLoad = :workLoads WHERE tp.id = :taskPackageId")
    void updateCountAndWorkLoad(@Param("count") Integer count,
                                @Param("workLoads") Double workLoads,
                                @Param("taskPackageId") Long taskPackageId);

    //    @Transactional
//    @Modifying
//    @Query(value = "SELECT tp FROM TaskPackagePercent tp " +
//        " WHERE tp.id IN :entityId")
    Page<TaskPackagePercent> findByOrgIdAndProjectIdAndIdInAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        Collection<Long> entityId,
        Pageable pageable);

    @Query(value = "SELECT " +
        " wp.project_id projectId, hnr.entity_id entityId, bcp.process_id processId, bcp.entity_sub_type_id entitySubTypeId " +
        " FROM " +
        "    task_package AS wp" +
        "    JOIN task_package_category AS wpc " +
        "       ON wpc.id = wp.category_id AND wpc.deleted = 0 " +
        "    JOIN task_package_category_process_relation AS wpcp " +
        "       ON wpcp.category_id = wpc.id " +
        "    JOIN bpm_entity_type_process_relation bcp " +
        "       ON bcp.process_id = wpcp.process_id " +
        "   JOIN bpm_entity_sub_type bec " +
        "       ON bec.id = bcp.entity_sub_type_id " +
        "   JOIN task_package_entity_relation tper " +
        "       ON tper.task_package_id = wp.id " +
        "   JOIN hierarchy_node_relation hnr " +
        "       ON hnr.ancestor_entity_id = tper.entity_id " +
        "       AND hnr.entity_sub_type = bec.name_en " +
        "   LEFT JOIN wbs_entry AS wbs " +
        "       ON wbs.project_id = wp.project_id " +
        "       AND wbs.entity_id = hnr.entity_id " +
        "       AND wbs.process_id = bcp.process_id " +
        "       AND wbs.deleted = 0 " +
        "       AND wbs.active = 1 " +
        " WHERE wp.id = :taskPackageId AND wbs.id IS NULL",
        nativeQuery = true)
    List<Tuple> findMissingWbs(@Param("taskPackageId") Long taskPackageId);
}
