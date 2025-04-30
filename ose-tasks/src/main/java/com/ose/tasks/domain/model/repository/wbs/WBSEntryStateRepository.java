package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.dto.wbs.WBSEntryChildStatisticsDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageEntity;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.tasks.vo.wbs.WBSEntryType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * WBS State 条目 CRUD 操作接口。
 */
@Transactional
public interface WBSEntryStateRepository extends PagingAndSortingWithCrudRepository<WBSEntryState, Long> {


    List<WBSEntryState> findByProjectIdAndEntityId(Long projectId, Long entityId);

    List<WBSEntryState> findByProjectIdAndWbsEntryIdIn(Long projectId, Set<Long> parentIDs);


    /**
     * 更新子条目的工作组。
     *
     * @param wbsEntryAncestorId     计划wbsEntryAncestorId
     * @param teamId                 工作组 ID
     * @param teamPath               工作组路径
     */
    @Query(value = "UPDATE wbs_entry_state we JOIN wbs_entry_plain_relation wr ON we.wbs_entry_id = wr.wbs_entry_id " +
        " SET we.team_id = :teamId, we.team_path = :teamPath, we.team_name = :teamName " +
        " we.work_site_id = :workSiteId, we.work_site_name = :workSiteName " +
        " WHERE wr.wbs_entry_ancestor_id = :wbsEntryAncestorId",
        nativeQuery = true)
    @Modifying
    @Transactional
    void updateTeamAndSite(
        @Param("wbsEntryAncestorId") Long wbsEntryAncestorId,
        @Param("teamId") Long teamId,
        @Param("teamPath") String teamPath,
        @Param("teamName") String teamName,
        @Param("workSiteId") Long workSiteId,
        @Param("workSiteName") String workSiteName
    );

    /**
     * 用于取得项目的所有三级计划及其上级节点。
     *
     * @param projectId     项目 ID
     * @param wbsEntryId    条目所在路径
     * @param types         类型
     * @return 计划条目列表
     */
    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryPlainRelation wr ON w.projectId = wr.projectId AND w.id = wr.wbsEntryId " +
        " JOIN WBSEntryState ws ON w.projectId = ws.projectId AND w.id = ws.wbsEntryId " +
        " WHERE wr.projectId = :projectId AND wr.wbsEntryAncestorId = :wbsEntryId AND w.type IN :types " +
        "AND ws.runningStatus NOT IN :excludedRunningStatus AND w.active = TRUE AND w.deleted = FALSE")
    List<WBSEntryState> findByProjectIdAndPathLikeAndTypeInAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("wbsEntryId") Long wbsEntryId,
        @Param("types") Set<WBSEntryType> types,
        @Param("excludedRunningStatus") Set<WBSEntryRunningStatus> excluededRunningStatus
    );

    @Query("SELECT ws FROM WBSEntry w JOIN WBSEntryState ws ON w.projectId = ws.projectId AND w.id = ws.wbsEntryId " +
        " WHERE w.projectId = :projectId AND w.parentId = :parentId AND w.active = TRUE AND w.deleted = FALSE")
    List<WBSEntryState> findByProjectIdAndParentIdAndActiveIsTrueAndDeletedIsFalse(Long projectId, Long parentId);

    @Query("SELECT ws FROM WBSEntry w JOIN WBSEntryState ws ON w.projectId = ws.projectId AND w.id = ws.wbsEntryId " +
        " WHERE w.projectId = :projectId AND w.entityId = :entityId " +
        " AND w.stage = :stage AND w.process = :process AND w.active = TRUE AND w.deleted = FALSE")
    Optional<WBSEntryState> findByProjectIdAndEntityIdAndStageAndProcessAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("stage") String stage,
        @Param("process") String process);

    @Query("SELECT ws FROM WBSEntry w JOIN WBSEntryState ws ON w.projectId = ws.projectId AND w.id = ws.wbsEntryId " +
        " WHERE w.projectId = :projectId AND w.entityId = :entityId " +
        " AND w.processId = :processId AND w.active = TRUE AND w.deleted = FALSE")
    WBSEntryState findByProjectIdAndEntityIdAndProcessIdAndDeletedIsFalse(@Param("projectId") Long projectId,
                                                                          @Param("entityId") Long entityId,
                                                                          @Param("processId") Long processId);

    /**
     * 更新 三级计划 物量
     *
     * @param wlId
     */

    @Query(value = "UPDATE wbs_entry_state SET work_load = :wl WHERE wbs_entry_id = :wlId",
        nativeQuery = true)
    @Modifying
    @Transactional
    void updateWorkLoad(@Param("wlId") Long wlId,
                        @Param("wl") Double wl);


    @Query(value = "SELECT SUM(work_load) AS totalWorkLoad, " +
        " SUM(CASE WHEN running_status = 'APPROVED' THEN work_load ELSE 0 END) AS finishedWorkLoad, " +
        " SUM(CASE WHEN running_status = 'APPROVED' THEN 1 ELSE 0 END) AS finishedCount, " +
        " COUNT(0) AS totalCount " +
        " FROM wbs_entry_state ws JOIN wbs_entry we ON ws.wbs_entry_id = we.id " +
        " WHERE ws.project_id = :projectId AND ws.task_package_id = :taskPackageId AND we.deleted = 0 GROUP BY ws.task_package_id",
        nativeQuery = true)
    Tuple getWbsWorkLoadFromTp(@Param("projectId") Long projectId, @Param("taskPackageId") Long taskPackageId);

    @Transactional
    @Modifying
    @Query("UPDATE WBSEntryState e SET e.runningStatus = :runningStatus WHERE e.projectId = :projectId AND e.wbsEntryId = :id")
    void updateRunningStatusById(@Param("projectId") Long projectId,
                                 @Param("runningStatus") WBSEntryRunningStatus runningStatus,
                                 @Param("id") long id);


    @Query(value = "SELECT SUM(work_load) AS totalWorkLoad, " +
        " SUM(CASE WHEN running_status = 'APPROVED' THEN work_load ELSE 0 END) AS finishedWorkLoad, " +
        " SUM(CASE WHEN running_status = 'APPROVED' THEN 1 ELSE 0 END) AS finishedCount, " +
        " COUNT(0) AS totalCount " +
        " FROM wbs_entry_state WHERE project_id = :projectId AND task_package_id = :taskPackageId GROUP BY task_package_id",
        nativeQuery = true)
    Tuple findTpsByProjectIdAndEntityId(@Param("projectId") Long projectId,
                                        @Param("taskPackageId") Long taskPackageId);

    /**
     * 更新工作场地名称。
     *
     * @param workSiteId   工作场地 ID
     * @param workSiteName 工作场地名称
     */
    @Query("UPDATE WBSEntryState e SET e.workSiteName = :workSiteName WHERE e.workSiteId = :workSiteId")
    @Modifying
    @Transactional
    void updateWorkSiteName(
        @Param("workSiteId") Long workSiteId,
        @Param("workSiteName") String workSiteName
    );

    /**
     * 更新子条目的工作场地
     */
    @Query(value = "UPDATE wbs_entry_state we JOIN wbs_entry_plain_relation wr ON we.id = wr.wbs_entry_id " +
        " SET we.work_site_id = :workSiteId, we.work_site_name = :workSiteName " +
        " WHERE wr.wbs_entry_ancestor_id = :wbsEntryAncestorId AND we.deleted = 0",
        nativeQuery = true)
    @Modifying
    @Transactional
    void updateWorkSite(
        @Param("wbsEntryAncestorId") Long wbsEntryAncestorId,
        @Param("workSiteId") Long workSiteId,
        @Param("workSiteName") String workSiteName
    );

    /**
     * 计算直接子条目的已完成权重之和。
     *
     * @param projectId 项目 ID
     * @param parentId  条目 ID
     * @return 直接子条目的已完成权重之和
     */
    @Query("SELECT new com.ose.tasks.dto.wbs.WBSEntryChildStatisticsDTO(COUNT(e.id), SUM(e.totalScore), " +
        "SUM(e.finishedScore), SUM(e.actualDuration)) FROM WBSEntryState e JOIN WBSEntry w ON e.projectId = w.projectId AND e.wbsEntryId = w.id " +
        "WHERE w.projectId = :projectId AND w.parentId = :parentId AND w.deleted = false AND w.active = true")
    WBSEntryChildStatisticsDTO sumFinishedScore(
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId
    );


    /**
     * 清除四级计划的任务包信息。
     *
     * @param taskPackage 任务包信息
     */
    @Modifying
    @Transactional
    @Query(
        "UPDATE WBSEntryState e"
            + " SET"
            + "   e.taskPackageId = null"
            + " WHERE"
            + "   e.projectId = :#{#taskPackage.projectId}"
            + "   AND e.taskPackageId = :#{#taskPackage.id}"
    )
    void unsetTaskPackageInfo(@Param("taskPackage") TaskPackageEntity taskPackage);

    WBSEntryState findByWbsEntryId(Long wbsEntryId);

    @Modifying
    @Transactional
    @Query("DELETE FROM WBSEntryState ws WHERE ws.wbsEntryId = :wbsEntryId")
    void deleteByWbsId(@Param("wbsEntryId") Long wbsEntryId);
}
