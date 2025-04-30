package com.ose.tasks.domain.model.repository.wbs;

import com.ose.auth.vo.UserPrivilege;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wbs.entry.WBSEntryDelegate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * WBS 任务分配记录  CRUD 操作接口。
 */
public interface WBSEntryDelegateRepository extends PagingAndSortingWithCrudRepository<WBSEntryDelegate, Long> {

    /**
     * 取得 WBS WBS 任务分配记录。
     *
     * @return WBS 任务分配记录
     */
    List<WBSEntryDelegate> findByWbsEntryIdAndDeletedIsFalse(Long wbsEntryId);

    /**
     * 通过WBS条目与操作权限获取任务分配记录。
     *
     * @param wbsEntryId WBS条目ID
     * @param privilege  操作权限
     * @return 任务分配记录
     */
    WBSEntryDelegate findByWbsEntryIdAndPrivilegeAndDeletedIsFalse(Long wbsEntryId, UserPrivilege privilege);

    /**
     * 根据ID获取任务分配记录。
     *
     * @param delegateId 任务分类记录ID
     * @return 任务分配记录
     */
    WBSEntryDelegate findByIdAndDeletedIsFalse(Long delegateId);


    /**
     * 根据任务包设定的信息，定义四级计划权限。
     *
     */
    @Query(
        value = "  SELECT"
            + "     wbs.id AS id,"
            + "     wbs.id AS wbs_entry_id,"
            + "     :privilege AS privilege,"
            + "     null AS team_id,"
            + "     :userId AS user_id,"
            + "     CURRENT_TIMESTAMP() AS created_at,"
            + "     :operatorId AS created_by,"
            + "     CURRENT_TIMESTAMP() AS last_modified_at,"
            + "     :operatorId AS last_modified_by,"
            + "     UNIX_TIMESTAMP() AS version,"
            + "     0 AS deleted,"
            + "     'ACTIVE' AS status,"
            + "     null AS deleted_at,"
            + "     null AS deleted_by"
            + "    FROM"
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
            + "   WHERE"
            + "     wp.project_id = :projectId"
            + "     AND wp.id = :taskPackageId"
            + "     AND p.id = :processId"
            + "     AND (wes.running_status IS NULL OR (wes.running_status <> 'RUNNING' AND wes.running_status <> 'APPROVED'))",
        nativeQuery = true
    )
    List<WBSEntryDelegate> defineWBSEntryDelegates (
        @Param("operatorId") Long operatorId,
        @Param("projectId") Long projectId,
        @Param("taskPackageId") Long taskPackageId,
        @Param("processId") Long processId,
        @Param("privilege") String privilege,
        @Param("userId") Long userId
    );


    /**
     * 根据工序信息查找已经存在的四级计划权限。
     *
     */
    @Modifying
    @Transactional
    @Query(
        value = "   SELECT" +
            "   wed.* " +
            "   FROM" +
            "   wbs_entry_delegate AS wed" +
            "   INNER JOIN wbs_entry AS we " +
            "   ON " +
            "   wed.wbs_entry_id = we.id" +
            "   INNER JOIN wbs_entry_state AS wes " +
            "   ON " +
            "   wes.wbs_entry_id = we.id " +
            "   WHERE" +
            "   we.project_id = :projectId " +
            "   AND wes.task_package_id = :taskPackageId" +
            "   AND wed.privilege = :privilege" +
            "   AND we.process_id = :processId",

        nativeQuery = true
    )
    List<WBSEntryDelegate> findExistingDelegates (
        @Param("projectId") Long projectId,
        @Param("taskPackageId") Long taskPackageId,
        @Param("processId") Long processId,
        @Param("privilege") String privilege
    );

}
