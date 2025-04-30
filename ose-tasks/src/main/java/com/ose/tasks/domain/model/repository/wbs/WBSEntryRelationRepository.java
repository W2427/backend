package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.wbs.entry.WBSEntryRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * WBS 条目关系 CRUD 操作接口。
 */
public interface WBSEntryRelationRepository extends CrudRepository<WBSEntryRelation, Long> {

    /**
     * 取得 WBS 条目关系。
     *
     * @param projectId     项目 ID
     * @param predecessorId 前置任务 ID
     * @param successorId   后置任务 ID
     * @return WBS 条目关系数据
     */
    Optional<WBSEntryRelation> findByProjectIdAndPredecessorIdAndSuccessorId(
        Long projectId,
        String predecessorId,
        String successorId
    );

    /**
     * 取得后置任务关系列表。
     *
     * @param projectId     项目 ID
     * @param predecessorId 前置任务 GUID
     * @return 任务关系数据列表
     */
    List<WBSEntryRelation> findByProjectIdAndPredecessorId(Long projectId, String predecessorId);

    List<WBSEntryRelation> findByProjectIdAndSuccessorId(Long projectId, String successorId);

    /**
     * 删除 WBS 条目及其子条目的关系数据。
     *
     * @param entryId WBS 条目 ID
     */
    @Transactional
    @Modifying
    @Query(
        value = "DELETE FROM"
            + "   wbs_entry_relation"
            + " WHERE"
            + "   project_id = :projectId"
            + "   AND id IN ("
            + "     SELECT"
            + "       id"
            + "     FROM ("
            + "       SELECT"
            + "         r.id"
            + "       FROM"
            + "         wbs_entry AS e"
            + "         INNER JOIN wbs_entry AS c"
            + "           ON c.project_id = e.project_id"
            + "           AND (c.id = e.id OR c.path LIKE CONCAT(e.path, e.id, '/%'))"
            + "         INNER JOIN wbs_entry_relation AS r"
            + "           ON r.project_id = c.project_id"
            + "           AND (r.predecessor_id = c.guid OR r.successor_id = c.guid)"
            + "       WHERE"
            + "         e.id = :entryId"
            + "     ) AS r"
            + "   )",
        nativeQuery = true
    )
    void deleteByEntryId(
        @Param("projectId") Long projectId,
        @Param("entryId") Long entryId
    );

    /**
     * 删除指定任务的所有关系。
     *
     * @param projectId 项目 ID
     * @param taskGUID  任务的 GUID
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM WBSEntryRelation r WHERE r.projectId = :projectId AND (r.predecessorId = :taskGUID OR r.successorId = :taskGUID)")
    void deleteAllByTaskGUID(
        @Param("projectId") Long projectId,
        @Param("taskGUID") String taskGUID
    );

    /**
     * 查找指定任务的所有关系。
     *
     * @param projectId 项目 ID
     * @param taskGUID  任务的 GUID
     */
//    @Modifying
//    @Transactional
    @Query(value = "SELECT r.id FROM wbs_entry_relation r WHERE r.project_id = :projectId AND r.predecessor_id = :taskGUID " +
        " UNION ALL " +
        " SELECT r.id FROM wbs_entry_relation r WHERE r.successor_id = :taskGUID ",nativeQuery = true)
    Set<BigInteger> findAllByTaskGUID(
        @Param("projectId") Long projectId,
        @Param("taskGUID") String taskGUID
    );

    /**
     * 根据前置任务 GUID 删除关系。
     *
     * @param projectId       项目 ID
     * @param predecessorGUID 前置任务的 GUID
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM WBSEntryRelation r WHERE r.projectId = :projectId AND r.predecessorId = :predecessorGUID")
    void deleteByPredecessorGUID(
        @Param("projectId") Long projectId,
        @Param("predecessorGUID") String predecessorGUID
    );

    /**
     * 根据后置任务 GUID 删除关系。
     *
     * @param projectId     项目 ID
     * @param successorGUID 后置任务的 GUID
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM WBSEntryRelation r WHERE r.projectId = :projectId AND r.successorId = :successorGUID")
    void deleteBySuccessorGUID(
        @Param("projectId") Long projectId,
        @Param("successorGUID") String successorGUID
    );

    /**
     * 删除指定 WBS 下所有四级计划的所有关系。
     *
     * @param projectId 项目 ID
     * @param path      WBS 路径
     */
    @Transactional
    @Modifying
    @Query(
        nativeQuery = true,
        value = "DELETE FROM wbs_entry_relation WHERE id IN (SELECT er1.id FROM (SELECT er2.id FROM wbs_entry AS e INNER JOIN wbs_entry_relation er2 ON er2.project_id = e.project_id AND (er2.predecessor_id = e.guid OR er2.successor_id = e.guid) WHERE e.project_id = :projectId AND e.path LIKE :path AND e.type = 'ENTITY') AS er1)"
    )
    void deleteByParentWBS(@Param("projectId") Long projectId, @Param("path") String path);

    /**
     * 删除冗余关系。
     *
     * @param projectId 项目 ID
     */
    @Transactional
    @Modifying
    @Query(
        nativeQuery = true,
        value = "DELETE FROM wbs_entry_relation WHERE project_id = :projectId AND optional = 0 AND id IN (SELECT id FROM (SELECT id FROM wbs_entry_redundant_relation) AS err)"
    )
    void deleteRedundantRelations(@Param("projectId") Long projectId);

    /**
     * 删除冗余关系。
     *
     * @param projectId 项目 ID
     */
//    @Transactional
//    @Modifying
    @Query(
        nativeQuery = true,
        value = "SELECT id FROM wbs_entry_relation WHERE project_id = :projectId AND id IN (SELECT id FROM (SELECT id FROM wbs_entry_redundant_relation) AS err) AND optional = 0 "
    )
    List<BigInteger> getRedundantRelations(@Param("projectId") Long projectId);

    /**
     * 取得冗余关系。
     *
     * @param projectId 项目 ID
     * @param
     */
//    @Transactional
//    @Modifying
    @Query(
        nativeQuery = true,
        value = "SELECT id FROM wbs_entry_redundant_relation WHERE project_id = :projectId AND successor_id = :successorId"
    )
    Set<BigInteger> getRedundantRelations(@Param("projectId") Long projectId, @Param("successorId") String successorId);

    @Query(nativeQuery = true,
        value = "SELECT guid FROM wbs_entry WHERE org_id = :orgId AND project_id = :projectId AND parent_id = :parentId")
    Set<String> findGuids(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("parentId") Long parentId);


    @Query(nativeQuery = true,
        value = "SELECT guid FROM wbs_entry WHERE project_id = :projectId AND entity_id = :entityId")
    Set<String> findGuids(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId);


    @Query(value = "SELECT wer.id " +
        "FROM wbs_entry we JOIN wbs_entry_relation wer " +
        " ON we.guid = wer.predecessor_id AND we.project_id = wer.project_id " +
        "WHERE we.org_id = :orgId " +
        "AND we.project_id = :projectId " +
        "AND we.parent_id = :parentId " +
        "AND wer.version < :version " +
        "UNION " +
        "SELECT wer.id " +
        "FROM wbs_entry we JOIN wbs_entry_relation wer " +
        " ON we.guid = wer.successor_id AND we.project_id = wer.project_id " +
        "WHERE we.org_id = :orgId " +
        "AND we.project_id = :projectId " +
        "AND we.parent_id = :parentId " +
        "AND wer.version < :version ",
        nativeQuery = true
    )
    Set<BigInteger> findGuidsLessThanVersion(@Param("orgId") Long orgId,
                                             @Param("projectId") Long projectId,
                                             @Param("parentId") Long workWbsId,
                                             @Param("version") Long version);

    @Query(value = "SELECT r.* FROM wbs_entry_relation r WHERE r.project_id = :projectId AND r.predecessor_id = :taskGUID " +
        " UNION ALL " +
        " SELECT r.* FROM wbs_entry_relation r WHERE r.project_id = :projectId AND r.successor_id = :taskGUID ",nativeQuery = true)
    Set<WBSEntryRelation> findAllWbByTaskGUID(@Param("projectId") Long projectId, @Param("taskGUID") String guid);
}
