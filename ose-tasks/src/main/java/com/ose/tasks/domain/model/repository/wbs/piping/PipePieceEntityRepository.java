package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.MaterialInfoDTO;
import com.ose.tasks.dto.wbs.PipePieceEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.PipePieceEntity;
import com.ose.tasks.entity.wbs.entity.PipePieceEntityBase;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管段实体 CRUD 操作接口。
 */
public interface PipePieceEntityRepository extends WBSEntityBaseRepository<PipePieceEntity>, WBSEntityCustomRepository<PipePieceEntityBase, PipePieceEntryCriteriaDTO>, PipePieceEntityRepositoryCustom {

    /**
     * 根据 实体ID 取得实体列表。
     *
     * @param entryIDs  节点 ID 列表
     * @param projectId 项目 ID
     * @return 实体列表
     */
    @Query("SELECT new com.ose.tasks.dto.MaterialInfoDTO(materialCode, material, SUM(length)) "
        + " FROM PipePieceEntity "
        + " WHERE projectId = :projectId "
        + " AND id IN :entryIDs "
        + " GROUP BY materialCode, material")
    List<MaterialInfoDTO> findByIdInAndProjectIdAndDeletedIsFalse(@Param("entryIDs") List<Long> entryIDs, @Param("projectId") Long projectId);

    PipePieceEntityBase findByIdAndOrgIdAndProjectId(Long pipePieceEntityId, Long orgId, Long projectId);

    PipePieceEntityBase findByOrgIdAndProjectIdAndIdAndDeletedIsFalse( Long orgId, Long projectId,Long pipePieceEntityId);

    List<PipePieceEntity> findByProjectIdAndDeletedIsFalse(Long projectId);

    /**
     * 更新 PN，PP 上的 ISO id和NO
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE" +
        "      entity_pipe_pieces AS e"+
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
        "      INNER JOIN entity_spools AS p" +
        "        ON p.id = ppn.entity_id" +
        "    SET" +
        "      e.iso_entity_id = p.iso_entity_id," +
        "      e.iso_no = p.iso_no," +
        "      e.spool_entity_id = p.id," +
        "      e.spool_no = p.no," +
        "      pn.iso_entity_id = p.iso_entity_id," +
        "      pn.iso_no = p.iso_no," +
        "      pn.spool_entity_id = p.id," +
        "      pn.spool_no = p.no" +
        "    WHERE" +
        "      e.project_id = :projectId",
        nativeQuery = true)
    void updatePpAndProjectNodeIds(@Param("projectId") Long projectId);

    /**
     * 更新 PN，PP 上的 ISO id和NO
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE" +
        "      entity_pipe_pieces AS e"+
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
        "      INNER JOIN entity_spools AS p" +
        "        ON p.id = ppn.entity_id" +
        "    SET" +
        "      e.iso_entity_id = p.iso_entity_id," +
        "      e.iso_no = p.iso_no," +
        "      e.module_no = p.module_no," +
        "      e.spool_entity_id = p.id," +
        "      e.spool_no = p.no," +
        "      pn.iso_entity_id = p.iso_entity_id," +
        "      pn.iso_no = p.iso_no," +
        "      pn.spool_entity_id = p.id," +
        "      pn.spool_no = p.no" +
        "    WHERE" +
        "      e.project_id = :projectId" +
        "       AND e.id = :entityId",
        nativeQuery = true)
    void updatePpAndProjectNodeIds(@Param("projectId") Long projectId, @Param("entityId") Long entityId);
}
