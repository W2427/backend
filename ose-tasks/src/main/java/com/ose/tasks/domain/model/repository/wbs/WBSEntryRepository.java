package com.ose.tasks.domain.model.repository.wbs;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.dto.deliverydoc.DeliveryDocModulesDTO;
import com.ose.tasks.entity.wbs.entry.*;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.tasks.vo.wbs.WBSEntryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Tuple;
import java.math.BigInteger;
import java.util.*;

/**
 * WBS 条目 CRUD 操作接口。
 */
@Transactional
public interface WBSEntryRepository extends PagingAndSortingWithCrudRepository<WBSEntry, Long>, WBSEntryCustomRepository {

    /**
     * 取得类型为 WORK 的 WBS 条目。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @return WBS 条目列表
     */
    @Query("SELECT e FROM WBSEntry e " +
        " WHERE e.projectId = :projectId AND e.id = :wbsEntryId AND e.deleted = false AND e.type = 'WORK' " +
        "AND e.sector IS NOT NULL AND e.stage IS NOT NULL AND e.process IS NOT NULL "
    )
    List<WBSEntry> findWorksByProjectIdAndParentId(
        @Param("projectId") Long projectId,
        @Param("wbsEntryId") Long wbsEntryId
    );

    /**
     * 取得类型为 WORK 的 WBS 条目。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId WBS 条目 ID
     * @return WBS 条目列表
     */
    @Query("SELECT e FROM WBSEntry e JOIN WBSEntryPlainRelation wr ON e.projectId = wr.projectId AND e.id = wr.wbsEntryId " +
        " WHERE e.projectId = :projectId AND wr.wbsEntryAncestorId = :wbsEntryId AND e.deleted = false AND e.type = 'WORK' " +
        "AND e.sector IS NOT NULL AND e.stage IS NOT NULL AND e.process IS NOT NULL ")
    List<WBSEntry> findDescentWorksByProjectIdAndParentId(
        @Param("projectId") Long projectId,
        @Param("wbsEntryId") Long wbsEntryId
    );

    /**
     * 取得 WBS 更新版本号。
     *
     * @param projectId 项目 ID
     * @return WBS 更新版本号信息
     */
    @Query(value = "SELECT version FROM wbs_entry WHERE project_id = :projectId ORDER BY version DESC LIMIT 0,1", nativeQuery = true)
    BigInteger getWBSVersion(
        @Param("projectId") Long projectId
    );

    /**
     * 根据项目 ID 及条目 ID 取得条目信息。
     *
     * @param projectId 项目 ID
     * @param id        条目 ID
     * @return 条目信息
     */
    Optional<WBSEntry> findByProjectIdAndIdAndDeletedIsFalse(Long projectId, Long id);

    /**
     * 根据项目 ID 及条目 GUID 取得条目信息。
     *
     * @param projectId 项目 ID
     * @param guid      条目 GUID
     * @return 条目信息
     */
    Optional<WBSEntry> findByProjectIdAndGuid(Long projectId, String guid);

    /**
     * 根据项目 ID 及条目编号 取得条目信息。
     *
     * @param projectId 项目 ID
     * @param no      条目 no
     * @return 条目信息
     */
    Optional<WBSEntry> findByProjectIdAndNo(Long projectId, String no);
    /**
     * 根据项目 ID 及条目编号 取得条目信息。
     *
     * @param projectId 项目 ID
     * @param stage     工序阶段
     * @param sector    层级
     * @param name      名称
     * @return 条目信息
     */
    Optional<WBSEntry> findByProjectIdAndStageAndSectorAndName(Long projectId, String stage, String sector, String name);
    /**
     * 取得 WBS 条目列表。
     *
     * @param projectId 项目 ID
     * @param entryIDs  条目 ID 集合
     * @return 条目列表
     */
    List<WBSEntry> findByProjectIdAndIdInOrderByDepthAsc(Long projectId, Collection<Long> entryIDs);


    /**
     * 取得 WBS 条目。
     *
     * @param projectId 项目 ID
     * @param id        条目 ID
     * @return WBS 条目
     */
    Optional<WBSEntry> findByProjectIdAndIdAndActiveIsTrueAndDeletedIsFalse(
        Long projectId,
        Long id
    );

    /**
     * 取得四级计划条目。
     *
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     * @param stage     工序阶段
     * @param process   工序
     * @return 四级计划条目
     */
    Optional<WBSEntry> findByProjectIdAndEntityIdAndStageAndProcessAndDeletedIsFalse(
        Long projectId,
        Long entityId,
        String stage,
        String process
    );

    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryState ws ON w.id = ws.wbsEntryId WHERE " +
        " w.projectId = :projectId AND w.entityId = :entityId AND w.stage = :stage AND w.process = :process  " +
        " AND ws.runningStatus =:runningStatus AND w.deleted = FALSE")
    Optional<WBSEntry> findByProjectIdAndEntityIdAndStageAndProcessAndStateAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("stage") String stage,
        @Param("process") String process,
        @Param("runningStatus") WBSEntryRunningStatus runningStatus
    );


    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryState ws ON w.id = ws.wbsEntryId WHERE " +
        " w.projectId = :projectId AND w.entityId = :entityId AND w.processId = :processId AND w.deleted = FALSE " +
        " AND ws.runningStatus = 'RUNNING'")
    Optional<WBSEntry> findByProjectIdAndEntityIdAndProcessIdAndDeletedIsFalseAndFinishState(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("processId") Long processId
    );

    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryState ws ON w.id = ws.wbsEntryId WHERE " +
        " w.projectId = :projectId AND w.entityId = :entityId AND w.processId = :processId AND w.deleted = FALSE " +
        " AND ws.runningStatus IS NULL")
    Optional<WBSEntry> findByProjectIdAndEntityIdAndProcessIdAndDeletedIsFalseAndFinishStateAndIsPartOut(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId,
        @Param("processId") Long processId
    );

    /**
     * 取得指定项目的指定类型模块下的所有实体工序四级计划条目。
     *
     * @param projectId  项目 ID
     * @param parentId  父级ID
     * @param type       WBS 条目类型
     * @param pageable   分页参数
     * @return 实体工序四级计划条目
     */
    @Query("SELECT w FROM WBSEntry w WHERE w.projectId = :projectId AND w.parentId = :parentId AND " +
        "w.type = :type " +
        "AND w.active = true AND w.deleted = false")
    List<WBSEntry> findByProjectIdAndParentIdAndTypeAndActiveIsTrueAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId,
        @Param("type") WBSEntryType type,
        Pageable pageable
    );


    /**
     * 取得指定项目的指定类型模块下的所有实体工序四级计划条目。
     *
     * @param projectId  项目 ID
     * @param moduleType 模块类型
     * @param type       WBS 条目类型
     * @param pageable   分页参数
     * @return 实体工序四级计划条目
     */
    @Query("SELECT w FROM WBSEntry w WHERE w.projectId = :projectId AND w.funcPart = :funcPart AND " +
        "w.type = :type " +
        "AND w.active = true AND w.deleted = false")
    List<WBSEntry> findByProjectIdAndFuncPartAndTypeAndActiveIsTrueAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("funcPart") String funcPart,
        @Param("type") WBSEntryType type,

        Pageable pageable
    );

    /**
     * 取得指定项目的指定类型模块下的所有实体工序四级计划条目。
     *
     * @param projectId  项目 ID
     * @param moduleType 模块类型
     * @param type       WBS 条目类型
     * @param moduleName 模块名称
     * @param pageable   分页参数
     * @return 实体工序四级计划条目
     */
    @Query("SELECT w FROM WBSEntry w WHERE w.projectId = :projectId AND w.funcPart = :funcPart AND " +
        "w.type = :type AND w.sector = :moduleName " +
        "AND w.active = true AND w.deleted = false")
    List<WBSEntry> findByProjectIdAndSectorAndFuncPartAndTypeAndActiveIsTrueAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("funcPart") String funcPart,
        @Param("type") WBSEntryType type,
        @Param("moduleName") String moduleName,

        Pageable pageable);


    /**
     * 用于在生成四级计划后取得四级计划的前置任务。
     *
     * @param projectId     项目 ID
     * @param processId     工序
     * @param entityType    实体类型
     * @param entitySubType 实体子类型
     * @param entityIDs     实体 ID 集合
     * @return 四级计划列表
     */
    @Query("SELECT e FROM WBSEntry e " +
        "WHERE e.projectId = :projectId " +
        "AND e.processId = :processId " +
        "AND e.entityId IN :entityIDs " +
        "AND e.entityType = :entityType AND e.entitySubType = :entitySubType " +
        "AND e.active = TRUE AND e.deleted = FALSE")
    List<WBSEntry> findPredecessors(
        @Param("projectId") Long projectId,
        @Param("processId") Long processId,
        @Param("entityType") String entityType,
        @Param("entitySubType") String entitySubType,
        @Param("entityIDs") Collection<Long> entityIDs
    );


    /**
     * 用于在生成四级计划后取得四级计划的前置任务。
     *
     * @param projectId      项目 ID
     * @param processId      工序
     * @param entityType     实体类型
     * @param subEntityTypes 实体子类型s
     * @param entityIDs      实体 ID 集合
     * @return 四级计划列表
     */
    @Query("SELECT e FROM WBSEntry e " +
        "WHERE e.projectId = :projectId " +
        "AND e.processId = :processId " +
        "AND e.entityId IN :entityIDs " +
        "AND e.entityType = :entityType AND e.entitySubType IN :subEntityTypes " +
        "AND e.active = TRUE AND e.deleted = FALSE")
    List<WBSEntry> findPredecessors(
        @Param("projectId") Long projectId,
        @Param("processId") Long processId,
        @Param("entityType") String entityType,
        @Param("subEntityTypes") Set<String> subEntityTypes,
        @Param("entityIDs") Collection<Long> entityIDs
    );

    /**
     * 取得指定上级下所有启用的任务。
     *
     * @param projectId 项目 ID
     * @param parentId  上级条目 ID
     * @return 条目列表
     */
    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryState ws ON w.id = ws.wbsEntryId " +
        " WHERE w.projectId = :projectId AND w.parentId = :parentId AND (ws.runningStatus IS NULL OR ws.runningStatus = 'PENDING') " +
        " AND w.deleted = FALSE AND w.active = TRUE")
    List<WBSEntry> findNotStartedByProjectIdAndParentIdAndRunningStatusAndDeletedIsFalse(
        Long projectId,
        Long parentId
    );

    @Query("SELECT e FROM WBSEntry e WHERE e.id IN :parentIDs AND e.projectId = :projectId AND (e.startAt > :startAt OR e.finishAt < :finishAt) AND e.deleted = false")
    List<WBSEntry> findParentsByStartAtAndFinishAt(
        @Param("projectId") Long projectId,
        @Param("parentIDs") Set<Long> parentIDs,
        @Param("startAt") Date startAt,
        @Param("finishAt") Date finishAt
    );

    /**
     * 根据计划起止时间聚合计划的子条目。
     *
     * @param projectId          项目 ID
     * @param wbsEntryAncestorId 条目所在路径
     * @return 子条目计划起止时间聚合结果
     */
    @Query("SELECT new WBSEntry(e.startAt, e.finishAt) FROM WBSEntry e JOIN WBSEntryPlainRelation wr ON e.id = wr.wbsEntryId " +
        "WHERE e.projectId = :projectId AND wr.wbsEntryAncestorId = :wbsEntryAncestorId AND (e.startAt < :startAt OR e.finishAt > :finishAt) AND e.deleted = false GROUP BY e.startAt, e.finishAt")
    List<WBSEntry> groupChildByStartAtAndFinishAt(
        @Param("projectId") Long projectId,
        @Param("wbsEntryAncestorId") Long wbsEntryAncestorId,
        @Param("startAt") Date startAt,
        @Param("finishAt") Date finishAt
    );


    /**
     * 更新起止时间。
     *
     * @param projectId          项目 ID
     * @param wbsEntryAncestorId 父级 ID
     * @param startAt            计划开始时间
     * @param finishAt           计划截止时间
     * @param newStartAt         调整后计划开始时间
     * @param newFinishAt        调整后计划截止时间
     */
    @Transactional
    @Query(value = "UPDATE wbs_entry we JOIN wbs_entry_plain_relation wr ON we.id = wr.wbs_entry_id " +
        "SET we.start_at = :newStartAt, we.finish_at = :newFinishAt " +
        "WHERE we.project_id = :projectId AND we.deleted = 0 AND wr.wbs_entry_ancestor_id = :wbsEntryAncestorId " +
        "AND we.start_at = :startAt AND we.finish_at = :finishAt", nativeQuery = true)
    @Modifying
    void updateDuration(
        @Param("projectId") Long projectId,
        @Param("wbsEntryAncestorId") Long wbsEntryAncestorId,
        @Param("startAt") Date startAt,
        @Param("finishAt") Date finishAt,
        @Param("newStartAt") Date newStartAt,
        @Param("newFinishAt") Date newFinishAt
    );

    /**
     * 查找重复的四级计划的任务委派设置。
     */
//    @Query("SELECT w " +
//        " FROM WBSEntryDelegate w " +
//        " WHERE CONCAT(w.wbsEntryId,w.privilege) IN " +
//        " (" +
//        " SELECT CONCAT(wbsEntryId,privilege) " +
//        " FROM WBSEntryDelegate " +
//        " GROUP BY wbsEntryId,privilege " +
//        " HAVING COUNT(id) > 1 " +
//        " ) " +
//        " AND w.id NOT IN " +
//        " (" +
//        " SELECT MIN(id) " +
//        " FROM WBSEntryDelegate " +
//        " GROUP BY wbsEntryId,privilege HAVING COUNT(id)>1 )"
//
//    )
//    List<WBSEntryDelegate> searchEntityProcessDelegates();

    /**
     * 查找重复的四级计划的任务委派设置。
     */
    @Query(value = "SELECT wbs_entry_id wbsEntryId, privilege, cnt, mid, gid " +
        " FROM " +
        " (" +
        " SELECT wbs_entry_id,privilege,count(0) AS cnt, min(id) AS mid, group_concat(id) AS gid " +
        " FROM wbs_entry_delegate " +
        " GROUP BY wbs_entry_id, privilege " +
        " ) w WHERE cnt > 1 ", nativeQuery = true

    )
    List<Tuple> searchRepeatEntityProcessDelegates(@Param("projectId") Long projectId);

    /**
     * 清理无关联四级计划的任务委派设置。
     */
    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM"
            + "   wbs_entry_delegate"
            + " WHERE"
            + "   id IN ("
            + "     SELECT"
            + "       id"
            + "     FROM ("
            + "       SELECT"
            + "         ed.id"
            + "       FROM"
            + "         wbs_entry_delegate AS ed"
            + "         LEFT OUTER JOIN wbs_entry AS e"
            + "           ON e.id = ed.wbs_entry_id"
            + "           AND e.deleted = 0"
            + "       WHERE"
            + "         e.id IS NULL"
            + "     ) AS ed"
            + "   )",
        nativeQuery = true
    )
    void deleteDelegateWithNoEntityProcess();

    /**
     * 对于无运行状态值的四级计划，如果其所有前置任务的状态均为【已完成】则将其运行状态更新为【待启动】。
     *
     * @param projectId   项目 ID
     * @param batchTaskId 四级计划导入批处理任务 ID
     */
    @Query(
        value =
            "SELECT id FROM "
                + "   ("
                + "     SELECT"
                + "       se.id,"
                + "       COUNT(0) AS predecessors,"
                + "       SUM(CASE pes.running_status WHEN 'APPROVED' THEN 1 ELSE 0 END) AS approved_predecessors"
                + "     FROM"
                + "       wbs_entry AS se"
                + "       INNER JOIN wbs_entry_relation AS r"
                + "         ON r.project_id = se.project_id"
                + "         AND r.successor_id = se.guid"
                + "       INNER JOIN wbs_entry_state AS ses "
                + "         ON se.project_id = ses.project_id "
                + "         AND se.id = ses.wbs_entry_id "
                + "       INNER JOIN wbs_entry AS pe"
                + "         ON pe.project_id = se.project_id"
                + "         AND pe.guid = r.predecessor_id"
                + "         AND pe.deleted = 0"
                + "         AND pe.active = 1"
                + "       INNER JOIN wbs_entry_state pes"
                + "         ON pe.project_id = pes.project_id"
                + "         AND pe.id = pes.wbs_entry_id "
                + "     WHERE"
                + "       se.project_id = :projectId"
                + "       AND ses.running_status IS NULL"
                + "       AND se.deleted = 0"
                + "       AND se.active = 1"
                + "     GROUP BY"
                + "       se.id"
                + "   ) AS s"
                + " WHERE"
                + "   s.id > :batchTaskId"
                + "   AND s.predecessors = s.approved_predecessors",
        nativeQuery = true
    )
    List<BigInteger> getRunningStatusAsPending(@Param("projectId") Long projectId, @Param("batchTaskId") Long batchTaskId);


    /**
     * 对于无运行状态值的四级计划，如果其所有前置任务的状态均为【已完成】则将其运行状态更新为【待启动】。
     *
     * @param projectId   项目 ID
     * @param batchTaskId 四级计划导入批处理任务 ID
     */
    @Query(
        value =
            "SELECT id FROM "
                + "   ("
                + "     SELECT"
                + "       se.id,"
                + "       COUNT(0) AS predecessors,"
                + "       SUM(CASE pes.running_status WHEN 'APPROVED' THEN 1 ELSE 0 END) AS approved_predecessors"
                + "     FROM"
                + "       wbs_entry AS se"
                + "       INNER JOIN wbs_entry_relation AS r"
                + "         ON r.project_id = se.project_id"
                + "         AND r.successor_id = se.guid"
                + "       INNER JOIN wbs_entry_state AS ses "
                + "         ON se.project_id = ses.project_id "
                + "         AND se.id = ses.wbs_entry_id "
                + "       INNER JOIN wbs_entry AS pe"
                + "         ON pe.project_id = se.project_id"
                + "         AND pe.guid = r.predecessor_id"
                + "         AND pe.deleted = 0"
                + "         AND pe.active = 1"
                + "       INNER JOIN wbs_entry_state pes"
                + "         ON pe.project_id = pes.project_id"
                + "         AND pe.id = pes.wbs_entry_id "
                + "     WHERE"
                + "       se.project_id = :projectId"
                + "       AND ses.running_status IS NULL"
                + "       AND se.deleted = 0"
                + "       AND se.active = 1"
                + "     GROUP BY"
                + "       se.id"
                + "   ) AS s"
                + " WHERE s.predecessors = s.approved_predecessors",
        nativeQuery = true
    )
    List<BigInteger> getRunningStatusAsPending(@Param("projectId") Long projectId);


    /**
     * 更新子条目的工作组。
     *
     * @param wbsEntryAncestorId 计划wbsEntryAncestorId
     * @param teamId             工作组 ID
     * @param teamPath           工作组路径
     */
    @Transactional
    @Query(value = "UPDATE wbs_entry we JOIN wbs_entry_plain_relation wr ON we.id = wr.wbs_entry_id " +
        " SET we.team_id = :teamId, we.team_path = :teamPath, we.team_name = :teamName " +
        " WHERE wr.wbs_entry_ancestor_id = :wbsEntryAncestorId AND we.deleted = 0",
        nativeQuery = true)
    @Modifying
    void updateTeam(
        @Param("wbsEntryAncestorId") Long wbsEntryAncestorId,
        @Param("teamId") Long teamId,
        @Param("teamPath") String teamPath,
        @Param("teamName") String teamName
    );

    /**
     * 更新子条目的提前开始时长。
     *
     * @param wbsEntryAncestorId 父级ID
     * @param startBeforeHours   提前开始时长
     */
    @Transactional
    @Query(value = "UPDATE wbs_entry we JOIN wbs_entry_plain_relation wr ON we.id = wr.wbs_entry_id " +
        "SET we.start_before_hours = :startBeforeHours " +
        "WHERE wr.wbs_entry_ancestor_id = :wbsEntryAncestorId AND we.start_before_hours IS NULL",
        nativeQuery = true)
    @Modifying
    void updateStartBeforeHours(
        @Param("wbsEntryAncestorId") Long wbsEntryAncestorId,
        @Param("startBeforeHours") Integer startBeforeHours
    );

    /**
     * 将子节点更新为已挂起。
     *
     * @param wbsEntryId 条目所在路径
     */
    @Query(value = "UPDATE wbs_entry e JOIN wbs_entry_plain_relation wr ON e.id = wr.wbs_entry_id " +
        " JOIN wbs_entry_state ws ON e.id = ws.wbs_entry_id " +
        " SET ws.running_status = :runningStatus, e.last_modified_at = :lastModifiedAt, e.last_modified_by = :lastModifiedBy " +
        "WHERE e.project_id = :projectId AND wr.wbs_entry_ancestor_id = :wbsEntryId " +
        "AND ws.finished = 0 AND e.active = 0 AND e.deleted = 0 AND ws.running_status <> :runningStatus",
        nativeQuery = true)
    @Modifying
    @Transactional
    void updateChildrenRunningStatus(
        @Param("projectId") Long projectId,
        @Param("wbsEntryId") Long wbsEntryId,
        @Param("runningStatus") WBSEntryRunningStatus runningStatus,
        @Param("lastModifiedAt") Date lastModifiedAt,
        @Param("lastModifiedBy") Long lastModifiedBy
    );

    /**
     * 取得指定执行状态的子节点的信息。
     *
     * @param wbsEntryId    条目所在路径
     * @param runningStatus 执行状态
     * @return 子节点列表
     */
    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryPlainRelation wr ON w.id = wr.wbsEntryAncestorId " +
        " JOIN WBSEntryState ws ON w.id = ws.wbsEntryId " +
        " WHERE w.id = :wbsEntryId AND ws.runningStatus = :runningStatus AND ws.finished = FALSE AND w.active = TRUE AND w.deleted = FALSE")
    List<WBSEntry> findByPathLikeAndRunningStatusIsAndFinishedIsFalseAndActiveIsTrueAndDeletedIsFalse(Long wbsEntryId, WBSEntryRunningStatus runningStatus);

    /**
     * 根据项目ID，实体ID判断是否已经启动工作流
     *
     * @param entityId  实体ID
     * @param projectId 项目ID
     * @return 实体已启动的工作流的个数
     */
    @Query("SELECT COUNT(we) FROM WBSEntry we JOIN WBSEntryState ws ON we.id = ws.wbsEntryId " +
        "WHERE we.projectId = :projectId AND we.entityId = :entityId AND ws.startedAt IS NOT NULL AND we.deleted = FALSE AND we.active = TRUE")
    int existsByProjectIdAndEntityIdAndOrgIdAndDeletedIsFalse(
        @Param("entityId") Long entityId,
        @Param("projectId") Long projectId);

    /**
     * 取得最后一个子节点。
     *
     * @param parentId 上级节点 ID
     * @param type     子节点类型
     * @return 子节点信息
     */
    Optional<WBSEntry> findFirstByParentIdAndTypeAndDeletedIsFalseOrderBySortDesc(Long parentId, WBSEntryType type);

    /**
     * 用于取得项目的所有三级计划及其上级节点。
     *
     * @param projectId  项目 ID
     * @param wbsEntryId 条目所在路径
     * @param types      类型
     * @return 计划条目列表
     */
    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryPlainRelation wr ON w.id = wr.wbsEntryId " +
        " WHERE w.projectId = :projectId AND wr.wbsEntryAncestorId = :wbsEntryId AND w.type IN :types AND w.active = TRUE AND w.deleted = FALSE")
    List<WBSEntry> findByProjectIdAndPathLikeAndTypeInAndDeletedIsFalseOrderByPathAsc(
        @Param("projectId") Long projectId,
        @Param("wbsEntryId") Long wbsEntryId,
        @Param("types") Set<WBSEntryType> types
    );

    /**
     * 取得指定上级下的所有直接子节点。
     *
     * @param parentId 上级节点 ID
     * @param type     节点类型
     * @return 节点列表
     */
    List<WBSEntry> findByParentIdAndTypeAndDeletedIsFalseOrderBySortAsc(Long parentId, WBSEntryType type);


    @Query("SELECT w.id FROM WBSEntry w JOIN WBSEntryPlainRelation wr ON w.id = wr.wbsEntryId " +
        " JOIN WBSEntryState ws ON w.id = ws.wbsEntryId " +
        " WHERE w.projectId = :projectId AND wr.wbsEntryAncestorId = :wbsEntryId AND ws.runningStatus IN :runningStatus AND w.deleted = FALSE")
    List<Long> findChildrenByPathAndRunningStatusInAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("wbsEntryId") Long wbsEntryId,
        @Param("runningStatus") Set<WBSEntryRunningStatus> runningStatus
    );

    /**
     * 删除指定路径下的所有子条目。
     *
     * @param wbsEntryId 条目路径
     * @param version    更新版本号
     * @param deletedAt  删除时间
     * @param deletedBy  删除者 ID
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE wbs_entry e JOIN wbs_entry_plain_relation wr ON e.id = wr.wbs_entry_id " +
        " SET e.version = :version, e.deleted_at = :deletedAt, " +
        " e.deleted_by = :deletedBy, e.deleted = 1, e.guid = CONCAT(e.guid, '[deleted:', :version, ']') " +
        " WHERE wr.project_id = :projectId AND wr.wbs_entry_ancestor_id = :wbsEntryId AND e.deleted = 0",
        nativeQuery = true)
    void deleteByPathLikeAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("wbsEntryId") Long wbsEntryId,
        @Param("version") long version,
        @Param("deletedAt") Date deletedAt,
        @Param("deletedBy") Long deletedBy
    );

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query("SELECT e.entityId FROM WBSEntry e JOIN WBSEntryState ws ON e.id = ws.wbsEntryId" +
        " WHERE e.projectId = :projectId AND e.parentId = :parentId AND e.deleted = FALSE AND ws.runningStatus IN :runningStatus")
    List<Long> findEntityIDsByParentId(
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId,
        @Param("runningStatus") Set<WBSEntryRunningStatus> runningStatus);

    /**
     * 取得尚未开始的计划条目。
     *
     * @param projectId     项目 ID
     * @param runningStatus 运行状态
     * @param timestamp     Unix 时间戳（秒）
     * @param fromEntryId   分页起始条目 ID
     * @param pageable      分页参数
     * @return 四级计划列表
     */
    @Query("SELECT e FROM WBSEntry e JOIN WBSEntryState ws ON e.id = ws.wbsEntryId " +
        " WHERE e.projectId = :projectId AND e.type = 'ENTITY' AND e.active = TRUE AND e.deleted = FALSE AND ws.runningStatus IN :runningStatus " +
        " AND ws.taskPackageId IS NOT NULL AND e.startUnixTime < :timestamp AND e.id > :fromEntryId ORDER BY e.id ASC")
    List<WBSEntry> findNotStartedEntityProcesses(
        @Param("projectId") Long projectId,
        @Param("runningStatus") Set<WBSEntryRunningStatus> runningStatus,
        @Param("timestamp") Double timestamp,
        @Param("fromEntryId") Long fromEntryId,
        Pageable pageable
    );

    /**
     * 取得尚未开始的计划条目的数量。
     *
     * @param projectId 项目 ID
     * @param timestamp Unix 时间戳（秒）
     * @return 尚未开始的计划条目的数量
     */
    @Query("SELECT COUNT(e) FROM WBSEntry e JOIN WBSEntryState ws ON e.id = ws.wbsEntryId " +
        " WHERE e.projectId = :projectId AND e.type = 'ENTITY' AND ws.startedAt IS NULL AND e.active = TRUE AND e.deleted = FALSE AND e.startUnixTime < :timestamp")
    int countNotStartedEntityProcesses(
        @Param("projectId") Long projectId,
        @Param("timestamp") Double timestamp
    );

    /**
     * 根据实体和工序获取四级计划列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entityId  实体ID
     * @param process   工序信息
     * @return 四级计划列表
     */
    List<WBSEntry> findByOrgIdAndProjectIdAndEntityIdAndProcessAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        Long entityId,
        String process
    );

    List<WBSEntry> findByOrgIdAndProjectIdAndEntityIdAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        Long entityId
    );


    @Query(
        value = "SELECT e.id FROM "
            + "   task_package AS tp"
            + "   INNER JOIN task_package_category_process_relation AS tpcp"
            + "      ON tpcp.category_id = tp.category_id"
            + "   INNER JOIN task_package_entity_relation AS tpe"
            + "      ON tpe.task_package_id = tp.id"
            + "   INNER JOIN hierarchy_node_relation AS hnr on tpe.entity_id = hnr.node_ancestor_id "
            + "   INNER JOIN wbs_entry AS e ON e.project_id = tp.project_id AND e.process_id = tpcp.process_id "
            + "     AND e.entity_id = hnr.entity_id AND e.deleted = 0"
            + " WHERE"
            + "   tp.project_id = :projectId"
            + "   AND tp.id = :taskPackageId",
        nativeQuery = true
    )
    Set<BigInteger> findWbsEntryByTaskPackageId(@Param("projectId") Long projectId,
                                                @Param("taskPackageId") Long taskPackageId);


    @Query(
        value = "SELECT  " +
            "  sector   " +
            "FROM  " +
            "  wbs_entries_plain   " +
            "WHERE  " +
            "  sector IS NOT NULL   " +
            "  AND org_id = :orgId   " +
            "  AND project_id = :projectId   " +
            "  AND task_package_id = :taskPackageId  " +
            "GROUP BY  " +
            "  sector   " +
            "ORDER BY  " +
            "  sector ASC",
        nativeQuery = true
    )
    List<String> findSectorsByTaskPackageId(@Param("orgId") Long orgId,
                                               @Param("projectId") Long projectId,
                                               @Param("taskPackageId") Long taskPackageId);


    @Query(
        value = "SELECT e.id FROM "
            + "   task_package AS tp"
            + "   INNER JOIN task_package_category_process_relation AS tpcp"
            + "      ON tpcp.category_id = tp.category_id"
            + "   INNER JOIN task_package_entity_relation AS tpe"
            + "      ON tpe.task_package_id = tp.id"
            + "   INNER JOIN hierarchy_node_relation hnr on tpe.entity_id = hnr.ancestor_entity_id "
            + "   INNER JOIN wbs_entry AS e ON e.project_id = tp.project_id AND e.process_id = tpcp.process_id "
            + "     AND e.entity_id = hnr.entity_id AND e.deleted = 0"
            + " WHERE"
            + "   tp.project_id = :projectId"
            + "   AND tp.id = :taskPackageId",
        nativeQuery = true
    )
    Set<BigInteger> findStructureWbsEntryByTaskPackageId(@Param("projectId") Long projectId,
                                                         @Param("taskPackageId") Long taskPackageId);


    WBSEntry findFirstByProjectIdAndDepthAndDeletedIsFalse(Long projectId, int depth);

    /**
     * 设置计划的手动进度
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param wbsEntryId
     * @param mpFigure
     */
    @Transactional
    @Modifying
    @Query(value = "update wbs_entry set progress = :mpFigure where org_id =:orgId and project_id = :projectId and id = :wbsEntryId",
        nativeQuery = true)
    void setManualProgress(@Param("orgId") Long orgId,
                           @Param("projectId") Long projectId,
                           @Param("wbsEntryId") Long wbsEntryId,
                           @Param("mpFigure") double mpFigure);

    @Transactional
    @Modifying
    @Query(value = "update wbs_entry set progress = null where org_id =:orgId and project_id = :projectId and id = :wbsEntryId",
        nativeQuery = true)
    void setManualProgress(@Param("orgId") Long orgId,
                           @Param("projectId") Long projectId,
                           @Param("wbsEntryId") Long wbsEntryId);

    List<WBSEntry> findByOrgIdAndProjectIdAndProcessAndSectorAndTypeAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        String process,
        String module,
        WBSEntryType work
    );

    @Query(value = "SELECT " +
        "  COUNT(w) " +
        "FROM " +
        "  WBSEntry w " +
        "WHERE w.projectId = :projectId " +
        "  AND w.parentId = :parentId " +
        "  AND w.sector = :module " +
        "  AND w.process = :process " +
        "  AND w.deleted = false")
    Long findCountByOrgIdAndProjectIdAndParentIdAndSectorAndProcessAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId,
        @Param("module") String module,
        @Param("process") String process
    );

    @Query(value = "SELECT " +
        "  COUNT(w) " +
        "FROM " +
        "  WBSEntry w JOIN WBSEntryState ws ON w.id = ws.wbsEntryId " +
        "WHERE w.projectId = :projectId " +
        "  AND w.parentId = :parentId " +
        "  AND w.sector = :module " +
        "  AND w.process = :process " +
        "  AND w.deleted = false " +
        "  AND ws.runningStatus = 'APPROVED'")
    Long findApprovedCountByOrgIdAndProjectIdAndParentIdAndSectorAndProcessAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId,
        @Param("module") String module,
        @Param("process") String process
    );

    @Query(value = "SELECT " +
        "  new com.ose.tasks.dto.deliverydoc.DeliveryDocModulesDTO(w.sector)  " +
        "FROM " +
        "  WBSEntry w " +
        "WHERE w.orgId = :orgId " +
        "  AND w.projectId = :projectId " +
        "  AND w.deleted = false " +
        "  AND w.type = 'ENTITY' " +
        "GROUP BY w.sector")
    List<DeliveryDocModulesDTO> getModulesByOrgIdAndProjectIdAndDeletedIsFalse(
        @Param("orgId") Long orgId, @Param("projectId") Long projectId);

    @Query(value = "SELECT " +
        "  new com.ose.tasks.dto.deliverydoc.DeliveryDocModulesDTO(w.sector)  " +
        "FROM " +
        "  WBSEntry w " +
        "WHERE w.orgId = :orgId " +
        "  AND w.projectId = :projectId " +
        "  AND w.sector = :sector " +
        "  AND w.deleted = false " +
        "  AND w.type = 'ENTITY' " +
        "GROUP BY w.sector")
    List<DeliveryDocModulesDTO> getModulesByOrgIdAndProjectIdAndSectorAndDeletedIsFalse(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("sector") String sector
    );

    /**
     * 查找当前计划的父级WBS
     *
     * @param ids
     * @param projectId 项目ID
     * @return
     */
    @Query(value = "SELECT " +
        "e.parentId " +
        "FROM WBSEntry e " +
        "WHERE e.id in (:ids) " +
        "AND e.projectId = :projectId"
    )
    Set<Long> getParentWorkLoadIds(@Param("ids") Set<Long> ids, @Param("projectId") Long projectId);


    @Query("SELECT SUM(ws.workLoad) FROM WBSEntry e JOIN WBSEntryState ws ON e.id = ws.wbsEntryId " +
        " WHERE e.projectId = :projectId AND e.parentId = :wlId AND e.deleted = false")
    Double getWbsWorkLoad(@Param("wlId") Long wlId, @Param("projectId") Long projectId);


    /**
     * 取得实体增加后 模块 对应的 三级计划的 WBSEntryId集合
     *
     * @param module
     * @return
     */
    @Query(value = "SELECT " +
        "id " +
        "FROM wbs_entry " +
        "WHERE deleted=0 " +
        "AND type='UNITS' " +
        "AND project_id=:projectId " +
        "AND org_id=:orgId " +
        "AND wbs like concat('%.',:module) " +
        "LIMIT 0,1",
        nativeQuery = true)
    String getWbsEntryIds(@Param("projectId") Long projectId,
                          @Param("orgId") Long orgId,
                          @Param("module") String module);


    /**
     * 根据下列参数 取得 三级计划
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param stageName
     * @param processName
     * @param work
     * @param module
     * @param layer
     * @return
     */
    List<WBSEntry> findByOrgIdAndProjectIdAndStageAndProcessAndTypeAndSectorAndLayerPackageAndActiveIsTrueAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        String stageName,
        String processName,
        WBSEntryType work,
        String module,
        String layer);

    /**
     * 根据下列参数 取得 三级计划
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param stageName
     * @param processName
     * @param work
     * @param module
     * @return
     */
    List<WBSEntry> findByOrgIdAndProjectIdAndStageAndProcessAndTypeAndSectorAndActiveIsTrueAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        String stageName,
        String processName,
        WBSEntryType work,
        String module);

    /**
     * 根据四级计划Ids进行删除
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entryIds
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE " +
        "FROM WBSEntry e " +
        "WHERE e.id in (:entryIds) " +
        "AND e.orgId = :orgId " +
        "AND e.projectId = :projectId"
    )
    void deleteByOrgIdAndProjectIdAndIdIn(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entryIds") Set<Long> entryIds);


    /**
     * 根据四级计划Ids进行删除,删除 wbs_entry_relation
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entryIds
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE wer " +
        "FROM wbs_entry we LEFT JOIN wbs_entry_relation wer " +
        " ON we.guid = wer.predecessor_id AND we.project_id = wer.project_id " +
        "WHERE we.org_id = :orgId " +
        "AND we.project_id = :projectId " +
        "AND we.id IN (:entryIds) ",
        nativeQuery = true
    )
    void deleteRelationsByOrgIdAndProjectIdAndIdInPre(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entryIds") Set<Long> entryIds);

    /**
     * 根据四级计划Ids进行删除,删除 wbs_entry_relation
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entryIds
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE wer " +
        "FROM wbs_entry we LEFT JOIN wbs_entry_relation wer " +
        " ON we.guid = wer.successor_id AND we.project_id = wer.project_id " +
        "WHERE we.org_id = :orgId " +
        "AND we.project_id = :projectId " +
        "AND we.id IN (:entryIds) ",
        nativeQuery = true
    )
    void deleteRelationsByOrgIdAndProjectIdAndIdInSuf(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entryIds") Set<Long> entryIds);

    /**
     * 根据实体Ids查找在四级计划中出现的所有的父级实体Ids
     *
     * @param projectId 项目ID
     * @param entityIds
     */
    @Query(value = "SELECT DISTINCT pn.entity_id from " +
        " wbs_entry we left join hierarchy_node hn on we.parent_hierarchy_node_id = hn.id " +
        " left join project_node pn on pn.id = hn.node_id " +
        " where we.entity_id in (:entityIds) and we.project_id = :projectId",
        nativeQuery = true
    )
    List<Long> getParentIds(@Param("projectId") Long projectId,
                            @Param("entityIds") Set<Long> entityIds);

    /**
     * 根据实体Ids查找在四级计划中出现的所有的子集实体Ids
     *
     * @param projectId 项目ID
     * @param entityIds
     */
    @Query(value = "SELECT DISTINCT pn.entity_id from " +
        " wbs_entry we left join hierarchy_node hn on we.hierarchy_node_id = hn.parent_id " +
        " left join project_node pn on pn.id = hn.node_id " +
        " where we.entity_id in (:entityIds) and we.project_id = :projectId",
        nativeQuery = true
    )
    List<Long> getSonIds(@Param("projectId") Long projectId,
                         @Param("entityIds") Set<Long> entityIds);

    /**
     * 根据实体Ids查找在四级计划中出现的所有的子集实体Ids
     *
     * @param projectId  项目ID
     * @param entityIds  实体Id集合
     * @param type
     * @param funcPart
     * @param pageable
     */
    List<WBSEntry> findByProjectIdAndFuncPartAndTypeAndEntityIdInAndActiveIsTrueAndDeletedIsFalse(
        Long projectId,
        String funcPart,
        WBSEntryType type,
        Set<Long> entityIds,
        Pageable pageable);

    /**
     * 根据四级计划Ids进行删除,删除 wbs_entry_relation
     *
     * @param projectId 项目ID
     * @param entityIds
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE wer " +
        "FROM wbs_entry we LEFT JOIN wbs_entry_relation wer " +
        " ON we.project_id = wer.project_id AND we.guid = wer.predecessor_id " +
        " WHERE we.project_id = :projectId " +
        " AND we.entity_id IN (:entityIds) ",
        nativeQuery = true
    )
    void deleteRelationsByProjectIdAndEntityIdInPre(
        @Param("projectId") Long projectId,
        @Param("entityIds") Set<Long> entityIds);

    /**
     * 根据四级计划Ids进行删除,删除 wbs_entry_relation
     *
     * @param projectId 项目ID
     * @param entityIds
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE wer " +
        "FROM wbs_entry we LEFT JOIN wbs_entry_relation wer " +
        " ON  we.project_id = wer.project_id AND we.guid = wer.successor_id " +
        " WHERE we.project_id = :projectId " +
        " AND we.entity_id in (:entityIds) ",
        nativeQuery = true
    )
    void deleteRelationsByProjectIdAndEntityIdInSuf(
        @Param("projectId") Long projectId,
        @Param("entityIds") Set<Long> entityIds);


    /**
     * 设置四级计划的所属管线及单管实体的 ID。
     *
     * @param projectId 项目 ID
     */


    @Query(value = "SELECT DISTINCT entity_id FROM wbs_entry WHERE parent_id = :wbsId AND deleted = 0", nativeQuery = true)
    Set<BigInteger> findIdsByWorkWbsId(@Param("wbsId") Long wbsId);


    @Query("SELECT e FROM WBSEntry e " +
        "WHERE e.projectId = :projectId " +
        "AND e.parentHierarchyNodeId = :parentHierarchyNodeId " +
        "AND e.processId = :processId " +
        "AND e.entityType = :entityType AND e.entitySubType = :entitySubType " +
        "AND e.active = TRUE AND e.deleted = FALSE")
    List<WBSEntry> findPredecessorsByParentHierarchyNodeId(@Param("projectId") Long projectId,
                                                           @Param("processId") Long processId,
                                                           @Param("entityType") String entityType,
                                                           @Param("entitySubType") String entitySubType,
                                                           @Param("parentHierarchyNodeId") Long parentHierarchyNodeId);


    @Query("SELECT e FROM WBSEntry e " +
        "WHERE e.projectId = :projectId " +
        "AND e.parentHierarchyNodeId = :parentHierarchyNodeId " +
        "AND e.processId = :processId " +
        "AND e.entityType = :entityType AND e.entitySubType IN :subEntityTypes " +
        "AND e.active = TRUE AND e.deleted = FALSE")
    List<WBSEntry> findPredecessorsByParentHierarchyNodeId(@Param("projectId") Long projectId,
                                                           @Param("processId") Long processId,
                                                           @Param("entityType") String entityType,
                                                           @Param("subEntityTypes") Set<String> subEntityTypes,
                                                           @Param("parentHierarchyNodeId") Long parentHierarchyNodeId);

    @Query("SELECT e FROM WBSEntry e " +
        "WHERE e.projectId = :projectId " +
        "AND e.moduleHierarchyNodeId = :moduleHierarchyNodeId " +
        "AND e.processId = :processId " +
        "AND e.entityType = :entityType AND e.entitySubType IN :subEntityTypes " +
        "AND e.active = TRUE AND e.deleted = FALSE")
    List<WBSEntry> findPredecessorsByModuleHierarchyNodeId(@Param("projectId") Long projectId,
                                                           @Param("processId") Long processId,
                                                           @Param("entityType") String entityType,
                                                           @Param("subEntityTypes") Set<String> subEntityTypes,
                                                           @Param("moduleHierarchyNodeId") Long moduleHierarchyNodeId);


    @Query(value = "SELECT we.id,we.guid FROM wbs_entry we JOIN wbs_entry_state ws ON we.id = ws.wbs_entry_id " +
        " WHERE we.project_id = :projectId " +
        " AND we.parent_id = :parentId AND we.version < :version AND we.deleted = 0 AND ws.running_status NOT IN :runningStatus",
        nativeQuery = true)
    List<Tuple> findToBeDeletedWbsEntryIds(@Param("projectId") Long projectId,
                                           @Param("version") Long version,
                                           @Param("parentId") Long wkWbsEntryId,
                                           @Param("runningStatus") Set<String> runningStatus
    );


    @Transactional
    @Modifying
    @Query(value = "UPDATE wbs_entry_state " +
        " SET task_package_id = :taskPackageId " +
        "WHERE wbs_entry_id = :wbsEntryId",
        nativeQuery = true)
    void updateTaskPackageId(@Param("wbsEntryId") Long wbsEntryId, @Param("taskPackageId") Long taskPackageId);


    @Query(
        value = "SELECT e.id FROM "
            + "   task_package AS tp"
            + "   INNER JOIN task_package_category AS tpc"
            + "      ON tpc.id = tp.category_id"
            + "     AND tpc.deleted = 0"
            + "   INNER JOIN task_package_category_process_relation AS tpcp"
            + "      ON tpcp.category_id = tpc.id"
            + "   INNER JOIN bpm_process AS p"
            + "      ON p.id = tpcp.process_id"
            + "     AND p.status = 'ACTIVE'"
            + "   INNER JOIN bpm_process_stage AS ps"
            + "      ON ps.id = p.process_stage_id"
            + "     AND ps.status = 'ACTIVE'"
            + "   INNER JOIN task_package_entity_relation AS tpe"
            + "      ON tpe.task_package_id = tp.id"
            + "   INNER JOIN wbs_entry AS e"
            + "      ON (e.entity_id = tpe.entity_id"
            + "          OR (tpe.entity_type = 'WP01' AND e.wp01_id = tpe.entity_id)"
            + "          OR (tpe.entity_type = 'WP02' AND e.wp02_id = tpe.entity_id)"
            + "          OR (tpe.entity_type = 'WP03' AND e.wp03_id = tpe.entity_id)"
            + "          OR (tpe.entity_type = 'WP04' AND e.wp04_id = tpe.entity_id))"
            + "     AND e.stage = ps.name_en"
            + "     AND e.process = p.name_en"
            + " WHERE"
            + "   tp.project_id = :projectId"
            + "   AND e.id = :wbsEntryId "
            + " LIMIT 0,1",
        nativeQuery = true
    )
    BigInteger findTaskPackageByStructureWbsEntryId(@Param("projectId") Long projectId,
                                                    @Param("wbsEntryId") Long wbsEntryId);

    List<WBSEntry> findByProjectIdAndStageAndProcessAndDeletedIsFalse(Long projectId, String stage, String fitup, Pageable pageable);

    @Query(value = "SELECT " +
        "wer.id " +
        "FROM wbs_entry se JOIN wbs_entry_relation wer ON se.guid = wer.successor_id " +
        "JOIN wbs_entry pe ON pe.guid = wer.predecessor_id " +
        "WHERE " +
        "pe.name NOT IN :parts " +
        "AND " +
        "se.id = :wbsEntryId", nativeQuery = true)
    Set<BigInteger> getRedundantWbsRelationIds(@Param("wbsEntryId") Long wbsEntryId,
                                               @Param("parts") List<String> parts);

    @Transactional
    @Modifying
    @Query(value = "DELETE " +
        "wer " +
        "FROM wbs_entry se JOIN wbs_entry_relation wer ON se.guid = wer.successor_id " +
        "JOIN wbs_entry pe ON pe.project_id = wer.project_id AND pe.guid = wer.predecessor_id " +
        "WHERE " +
        "pe.name NOT IN :parts " +
        "AND " +
        "se.id = :wbsEntryId", nativeQuery = true)
    void deleteRedundantWbsRelationIds(@Param("wbsEntryId") Long wbsEntryId,
                                       @Param("parts") List<String> parts);


    @Query(value = "SELECT id AS wbsEntryId, " +
        "path, depth, sector, project_node_id AS nodeId, entity_id AS entityId, type FROM wbs_entry WHERE project_id = :projectId limit :start, :offset",
        nativeQuery = true)
    List<Tuple> getWBSInfosByPage(@Param("projectId") Long projectId,
                                  @Param("start") int start,
                                  @Param("offset") int offset);


    @Query(value = "SELECT task_package_id" +
        " FROM wbs_entry_state WHERE project_id = :projectId AND entity_id = :entityId GROUP BY task_package_id",
        nativeQuery = true)
    List<BigInteger> findTpIdsByProjectIdAndEntityId(@Param("projectId") Long projectId,
                                                     @Param("entityId") Long entityId);

    List<WBSEntry> findByProjectIdAndProjectNodeId(Long projectId, Long projectNodeId);

    WBSEntry findByProjectIdAndEntityIdAndProcessIdAndDeletedIsFalse(Long projectId, Long entityId, Long processId);

    List<WBSEntry> findByProjectIdAndProcessAndStageAndEntityIdInAndDeletedIsFalse(Long projectId, String process, String stage, List<Long> entityIds);

    @Query("SELECT w.guid FROM WBSEntry w WHERE w.parentId = :workWbsEntryId AND w.deleted = false ")
    List<String> findGuidByWorkWbsEntryId(@Param("workWbsEntryId") Long workWbsEntryId);

    /**
     * 根据关联实体的状态更新 WBS 的状态。
     *
     * @param projectId 项目 ID
     * @param entityId  实体 ID
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE wbs_entry we JOIN wbs_entry_state ws ON we.id = ws.wbs_entry_id " +
        " SET we.active = 0, we.deleted = 1, we.status = 'SUSPEND', we.deleted_at = CURRENT_TIMESTAMP(), " +
        " we.guid = CONCAT(we.guid, '[SUSPENDED:', UNIX_TIMESTAMP(), ']') " +
        " WHERE we.project_id = :projectId AND we.entity_id = :entityId AND we.deleted = 0 AND we.active = 1 " +
        " AND ws.running_status IS NOT NULL", nativeQuery = true)
    void updateStatusOfWBSOfDeletedEntity(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE wbs_entry we JOIN wbs_entry_state ws ON we.id = ws.wbs_entry_id " +
        " SET ws.running_status = 'PENDING' " +
        " WHERE we.project_id = :projectId AND we.process = :process AND we.entity_id in :entityIds AND we.deleted = 0 AND we.active = 1 " +
        " AND ws.running_status IS NULL", nativeQuery = true)
    void updateProjectIdAndProcessAndEntityIdInAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("process") String process,
        @Param("entityIds") List<Long> entityIds
    );


    List<WBSEntry> findByProjectIdAndEntityIdAndDeletedIsFalse(Long projectId, Long entityId);

    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryState ws ON w.id = ws.wbsEntryId " +
        " WHERE w.projectId = :projectId AND w.processId = :processId AND w.entityType = :entityType " +
        " AND w.entitySubType IN :entitySubTypes AND w.deleted = FALSE " +
        " AND w.active = TRUE AND ws.runningStatus IS NULL")
    List<WBSEntry> findByProjectIdAndProcessIdAndEntityTypeAndEntitySubTypeInAndActiveIsTrueAndDeletedIsFalse(
        @Param("projectId") Long projectId,
        @Param("processId") Long processId,
        @Param("entityType") String entityType,
        @Param("entitySubTypes") Set<String> entitySubTypes);

    Optional<WBSEntry> findByProjectIdAndEntityIdAndParentId(Long projectId, Long entityId, Long parentId);

    List<WBSEntry> findByProjectIdAndIdInAndDeletedIsFalse(Long projectId, Set<Long> wbsEntryIds);

    @Query("SELECT p FROM WBSEntryPredecessorDetail p WHERE p.projectId = :projectId AND p.successorId = :successorGUID AND (p.optional IS NULL OR p.optional = FALSE)")
    Page<WBSEntryPredecessorDetail> findPredecessorsByProjectIdAndSuccessorIdAndOptionalIsNotTrue(@Param("projectId") Long projectId,
                                                                                                  @Param("successorGUID") String successorGUID,
                                                                                                  Pageable pageable);

    @Query(value = "SELECT w FROM WBSEntry w LEFT JOIN WBSEntryRelation wr ON w.projectId = wr.projectId AND w.guid = wr.successorId " +
        "WHERE w.projectId = :projectId AND w.parentId = :parentWbsId AND wr.id IS NULL")
    List<WBSEntry> findByProjectIdAndParentIdAndPredecessorIsNull(@Param("projectId") Long projectId, @Param("parentWbsId") Long parentWbsId);


    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryPlainRelation wr ON w.id = wr.wbsEntryId " +
        " WHERE wr.wbsEntryAncestorId = :wbsEntryId AND wr.type = 'WORK' AND w.deleted = FALSE " +
        " AND w.active = TRUE AND w.sector IS NOT NULL " +
        " AND w.stage IS NOT NULL AND w.process IS NOT NULL")
    List<WBSEntry> findWorksByAncestorId(@Param("wbsEntryId") Long wbsEntryId);


    @Query(value = "SELECT w FROM WBSEntry w JOIN WBSEntryState ws ON w.id = ws.wbsEntryId " +
        "WHERE w.projectId = :projectId AND w.parentId = :parentWbsId AND ws.taskPackageId IS NULL")
    List<WBSEntry> findByProjectIdAndParentIdAndTpIsNull(@Param("projectId") Long projectId, @Param("parentWbsId") Long parentWbsId);

    @Query("SELECT w FROM WBSEntry w JOIN ProjectNode pn ON w.sector = pn.no JOIN HierarchyNodeRelation hnr ON pn.id = hnr.nodeAncestorId " +
        "WHERE w.projectId = :projectId AND w.process = :process AND w.stage = :stage AND w.type = 'WORK' AND hnr.entityId = :entityId")
    Set<WBSEntry> findWorkByProjectIdAndProcessIdAndEntityId(@Param("projectId") Long projectId,
                                                             @Param("process") String process,
                                                             @Param("stage") String stage,
                                                             @Param("entityId") Long entityId);

    Optional<WBSEntry> findByProjectIdAndProcessAndNameAndDeletedIsFalse(Long projectId, String process, String name);

    List<WBSEntry> findByProjectIdAndEntityIdAndProcessIdIn(Long projectId, Long entityId, Set<Long> pIds);

    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryState ws ON w.id = ws.wbsEntryId " +
        " WHERE w.projectId = :projectId AND w.deleted = FALSE " +
        " AND w.active = TRUE AND ws.runningStatus = 'APPROVED'")
    List<WBSEntry> findByProjectIdAndFinished(
        @Param("projectId") Long projectId);

    @Query("SELECT w FROM WBSEntry w JOIN WBSEntryState ws ON w.id = ws.wbsEntryId " +
        " WHERE w.projectId = :projectId AND w.deleted = FALSE " +
        " AND w.active = TRUE AND ws.runningStatus = 'APPROVED' AND ws.planEndDate > ws.finishedAt")
    List<WBSEntry> findByProjectIdAndFinishedOnTime(
        @Param("projectId") Long projectId);

    @Query("SELECT w FROM WBSEntry w WHERE w.projectId = :projectId AND w.type = 'WORK' AND " +
        "SUBSTR(w.name,1,5) = :no")
    List<WBSEntryBase> findSsypnByProjectIdAndNo(@Param("projectId") Long projectId,
                                                 @Param("no") String no);

    List<WBSEntry> findByProjectIdAndTypeAndActiveIsTrueAndDeletedIsFalse(Long projectId, WBSEntryType type);
}
