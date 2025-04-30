package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entry.WBSEntryPredecessorStatistics;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 四级计划前置任务统计视图。
 */
public interface WBSEntryPredecessorStatisticsRepository extends CrudRepository<WBSEntryPredecessorStatistics, Long> {

    /**
     * 取得四级计划的前置任务统计。
     *
     * @param projectId 项目 ID
     * @param id        四级计划 ID
     * @return 前置任务统计
     */
    Optional<WBSEntryPredecessorStatistics> findByProjectIdAndId(Long projectId, Long id);


    @Query(value =
        "SELECT " +
            "0                   AS id," +
            "p.project_id           AS project_id," +
            "NULL                   AS entity_type," +
            "GROUP_CONCAT( DISTINCT `p`.`entity_type` SEPARATOR ',' ) AS predecessor_entity_types," +
            "COUNT(0)               AS total," +
            "SUM(IF(wes.finished = 0, 1, 0)) AS not_finished," +
            "SUM(IF(ISNULL(wes.running_status) OR (wes.running_status <> 'APPROVED'), 1, 0)) AS not_approved " +
            "FROM " +
            "wbs_entry_relation r " +
            "JOIN wbs_entry p ON " +
            "p.project_id = r.project_id " +
            "AND p.guid = r.predecessor_id " +
            "AND p.deleted = 0 " +
            "AND p.active = 1 " +
            "AND r.optional = 0 " +
            "INNER JOIN wbs_entry_state wes " +
            "ON wes.wbs_entry_id = p.id " +
            "WHERE " +
            "p.deleted = 0 AND r.project_id = :projectId " +
            "AND r.successor_id = :guid " +
            "GROUP BY r.project_id, r.successor_id",
        nativeQuery = true)
    WBSEntryPredecessorStatistics findStatisticByProjectIdAndId(@Param("projectId") Long projectId,
                                                                @Param("guid") String guid);
}
