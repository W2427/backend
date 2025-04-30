package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.entity.wbs.entity.ComponentEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 管件实体 CRUD 操作接口。
 */
public interface ComponentEntityRepository extends WBSEntityBaseRepository<ComponentEntity>, ComponentEntityRepositoryCustom {

    Optional<ComponentEntity> findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(
        @Param("id") Long id,
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId);

    List<ComponentEntity> findByIdInAndProjectIdAndDeletedIsFalse(List<Long> entryIDs, Long projectId);

    List<ComponentEntity> findByProjectIdAndDeletedIsFalse(Long projectId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE entity_components SET qty = :qty WHERE project_id = :projectId " +
        "AND org_id = :orgId AND no = :componentNo AND deleted = 0",
        nativeQuery = true)
    void updateQtyByNodeNo(@Param("orgId") Long orgId,
                           @Param("projectId") Long projectId,
                           @Param("qty") Integer qty,
                           @Param("componentNo") String componentNo);

//    Page<ComponentEntity> search(
//        Long orgId,
//        Long projectId,
//        ComponentEntryCriteriaDTO criteriaDTO,
//        PageDTO pageDTO);



    /**
     * 更新 PN，WELD 上的 ISO id和NO
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE" +
        "      entity_components AS e" +
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
        "      LEFT OUTER JOIN entity_isos AS ip" +
        "        ON ip.id = ppn.entity_id" +
        "      LEFT OUTER JOIN entity_spools AS sp" +
        "        ON sp.id = ppn.entity_id" +
        "    SET" +
        "      e.iso_entity_id = IFNULL(sp.iso_entity_id, ip.id)," +
        "      e.iso_no = IFNULL(sp.iso_no, ip.no)," +
        "      e.module_no = IFNULL(sp.module_no, ip.module_no)," +
        "      e.spool_entity_id = sp.id," +
        "      e.spool_no = sp.no," +
        "      pn.iso_entity_id = IFNULL(sp.iso_entity_id, ip.id)," +
        "      pn.iso_no = IFNULL(sp.iso_no, ip.no)," +
        "      pn.spool_entity_id = sp.id," +
        "      pn.spool_no = sp.no" +
        "    WHERE" +
        "      e.project_id = :projectId" +
        "       AND e.id = :entityId",
        nativeQuery = true)
    void updateComponentAndProjectNodeIds(@Param("projectId") Long projectId, @Param("entityId") Long entityId);
}
