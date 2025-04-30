package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.wbs.SpoolEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.SpoolEntity;
import com.ose.tasks.entity.wbs.entity.SpoolEntityBase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 单管实体 CRUD 操作接口。
 */
public interface SpoolEntityRepository extends WBSEntityBaseRepository<SpoolEntity>, WBSEntityCustomRepository<SpoolEntityBase, SpoolEntryCriteriaDTO>,SpoolEntityRepositoryCustom {

    /**
     * 根据 实体ID 取得实体列表。
     *
     * @param entryIDs  节点 ID 列表
     * @param projectId 项目 ID
     * @return 实体列表
     */
    List<SpoolEntity> findByIdInAndProjectIdAndDeletedIsFalse(List<Long> entryIDs, Long projectId);

    List<SpoolEntity> findByProjectIdAndDeletedIsFalse(Long projectId);
    /**
     * 根据二维码查询单管
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param qrcode
     * @return
     */
    SpoolEntity findByOrgIdAndProjectIdAndQrCode(Long orgId, Long projectId, String qrcode);

//    /**
//     * 查询单管所属试压包
//     *
//     * @param entityId
//     * @return
//     */
//    @Query("SELECT eptp.no FROM "
//        + "PressureTestPackageEntityBase eptp, ProjectNode pn, HierarchyNode hn, HierarchyNode hniso, ProjectNode pniso, SpoolEntity s "
//        + "WHERE eptp.id = pn.entityId "
//        + "AND hn.node.id = pn.id "
//        + "AND hn.id = hniso.parentId "
//        + "AND hniso.node.id = pniso.id "
//        + "AND pniso.isoEntityId = s.isoEntityId "
//        + "AND s.id = :entityId ")
//    String getPressureTestPackageBySpoolEntityId(@Param("entityId") Long entityId);


/*    @Query("SELECT isoNo FROM SpoolEntity WHERE no = :no AND projectId = :projectId AND deleted = false ")
    String findIsoNoByNoAndProjectId(@Param("no") String no, @Param("projectId") Long projectId);*/


    @Query(value = "SELECT DISTINCT " +
        " ppn.no FROM " +
        " project_nodes pn LEFT JOIN " +
        " hierarchy_nodes hn ON pn.id = hn.node_id " +
        " LEFT JOIN hierarchy_nodes phn ON phn.id = hn.parent_id " +
        " LEFT JOIN project_nodes ppn ON ppn.id = phn.node_id " +
        " WHERE pn.no = :no " +
        " AND pn.deleted = 0 " +
        " AND hn.deleted = 0 " +
        " AND phn.deleted =0 " +
        " AND ppn.deleted = 0 " +
        " AND pn.project_id = :projectId"
        , nativeQuery = true)
    String findIsoNoByNoAndProjectId(@Param("no") String no, @Param("projectId") Long projectId);

    SpoolEntity findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId,Long id);


    /**
     * 更新 PN，SPOOL 上的 ISO id和NO
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE " +
        "      entity_spools AS e " +
        "      INNER JOIN project_nodes AS pn" +
        "        ON pn.entity_id = e.id" +
        "        AND pn.deleted = 0" +
        "      INNER JOIN hierarchy_nodes AS hn" +
        "        ON hn.node_id = pn.id" +
        "        AND hn.hierarchy_type = 'PIPING'" +
        "        AND hn.deleted = 0" +
        "      INNER JOIN hierarchy_nodes AS phn" +
        "        ON phn.id = hn.parent_id" +
        "        AND phn.deleted = 0" +
        "      INNER JOIN project_nodes AS ppn" +
        "        ON ppn.id = phn.node_id" +
        "        AND ppn.deleted = 0" +
        "      INNER JOIN entity_isos AS p" +
        "        ON p.id = ppn.entity_id" +
        "    SET" +
        "      e.iso_entity_id = p.id," +
        "      e.iso_no = p.no," +
        "      pn.iso_entity_id = p.id," +
        "      pn.iso_no = p.no," +
        "      pn.spool_entity_id = e.id," +
        "      pn.spool_no = e.no" +
        "    WHERE" +
        "      e.project_id = :projectId",
        nativeQuery = true)
    void updateSpoolAndProjectNodeIds(@Param("projectId") Long projectId);

    /**
     * 更新 PN，SPOOL 上的 ISO id和NO
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE " +
        "      entity_spools AS e " +
        "      INNER JOIN project_nodes AS pn" +
        "        ON pn.entity_id = e.id" +
        "        AND pn.deleted = 0" +
        "      INNER JOIN hierarchy_nodes AS hn" +
        "        ON hn.node_id = pn.id" +
        "        AND hn.hierarchy_type = 'PIPING'" +
        "        AND hn.deleted = 0" +
        "      INNER JOIN hierarchy_nodes AS phn" +
        "        ON phn.id = hn.parent_id" +
        "        AND phn.deleted = 0" +
        "      INNER JOIN project_nodes AS ppn" +
        "        ON ppn.id = phn.node_id" +
        "        AND ppn.deleted = 0" +
        "      INNER JOIN entity_isos AS p" +
        "        ON p.id = ppn.entity_id" +
        "    SET" +
        "      e.iso_entity_id = p.id," +
        "      e.iso_no = p.no," +
        "      e.module_no = p.module_no," +
        "      pn.iso_entity_id = p.id," +
        "      pn.iso_no = p.no," +
        "      pn.spool_entity_id = e.id," +
        "      pn.spool_no = e.no" +
        "    WHERE" +
        "      e.project_id = :projectId " +
        "       AND e.id = :entityId",
        nativeQuery = true)
    void updateSpoolAndProjectNodeIds(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

}
