package com.ose.tasks.domain.model.repository.wbs.structure;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.StructureWeldEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldEntity;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * 结构 weld 实体 CRUD 操作接口。
 */
public interface StructureWeldEntityRepository extends WBSEntityBaseRepository<StructureWeldEntity>, WBSEntityCustomRepository<StructureWeldEntity, StructureWeldEntryCriteriaDTO> {


    /**
     * 根据焊口实体ID查询焊口实体信息。
     *
     * @param weldId 焊口实体ID
     * @return 焊口实体信息
     */
    StructureWeldEntity findByIdAndDeletedIsFalse(Long weldId);

    /**
     * 根据焊口实体ID查询焊口实体信息。
     *
     * @param weldId 焊口实体ID
     * @return 焊口实体信息
     */
    StructureWeldEntity findByProjectIdAndIdAndDeletedIsFalse(Long projectId,Long weldId);

    /**
     * 根据焊口ID列表获取焊口列表。
     *
     * @param weldIds 焊口ID列表
     * @return 焊口列表
     */
    List<StructureWeldEntity> findByIdInAndDeletedIsFalse(List<Long> weldIds);
    Optional<StructureWeldEntity> findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        String weldFullNo
    );

    /**
     * 更新焊口的wps信息
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param userId
     * @param weldFullNo
     * @param wpsNo
     * @param wpsId
     */
    @Transactional
    @Modifying
    @Query(value = "update entity_structure_weld set wps_no = :wpsNo,wps_id = :wpsId,last_modified_by = :userId where deleted = 0 and org_id =:orgId and project_id = :projectId and no = :weldFullNo",
        nativeQuery = true)
    void updateWeldWpsNo(@Param("orgId") Long orgId,
                         @Param("projectId") Long projectId,
                         @Param("userId") Long userId,
                         @Param("weldFullNo") String weldFullNo,
                         @Param("wpsNo") String wpsNo,
                         @Param("wpsId") String wpsId);


    /**
     * 更新 PN 上的 Wp id和NO
     *
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "    UPDATE                  " +
        "entity_structure_weld AS structure_weld                   " +
        "JOIN project_nodes AS pn               " +
        "ON structure_weld.id = pn.entity_id              " +
        "AND structure_weld.project_id = pn.project_id    " +
        "JOIN hierarchy_nodes AS hn             " +
        "ON hn.node_id = pn.id                  " +
        "JOIN hierarchy_nodes AS phn            " +
        "ON hn.parent_id = phn.id               " +
        "JOIN project_nodes AS ppn              " +
        "ON ppn.id = phn.node_id                " +
        "SET                                    " +










        "pn.wp01_id = ppn.wp01_id,              " +
        "pn.wp01_no = ppn.wp01_no,              " +
        "pn.wp02_id = ppn.wp02_id,              " +
        "pn.wp02_no = ppn.wp02_no,               " +
        "pn.wp03_id = ppn.wp03_id,              " +
        "pn.wp03_no = ppn.wp03_no,                " +
        "pn.wp04_id = ppn.wp04_id,              " +
        "pn.wp05_no = ppn.wp04_no                " +
        "    WHERE  " +
        "      structure_weld.project_id = :projectId        " +
        "      AND structure_weld.id = :entityId             ",
        nativeQuery = true)
    void updateWpAndProjectNodeIds(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId);

    StructureWeldEntity findByNoAndDeletedIsFalse(String weldNo);

    List<StructureWeldEntity> findByOrgIdAndProjectIdAndStatus(
        Long orgId,
        Long projectId,
        EntityStatus status
    );

    @Query("SELECT w FROM StructureWeldEntity w JOIN HierarchyNodeRelation r ON w.id = r.entityId "
        + "WHERE w.projectId = :projectId AND r.ancestorEntityId = :entityId "
        + " AND w.status = 'ACTIVE'")
    List<StructureWeldEntity> findByProjectIdAndParentId(
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId
    );
}
