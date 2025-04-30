package com.ose.tasks.domain.model.repository.wbs.piping;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.wbs.WeldEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.WeldEntity;
import com.ose.tasks.entity.wbs.entity.WeldEntityBase;
import com.ose.tasks.vo.qc.NDTExecuteFlag;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 焊口实体 CRUD 操作接口。
 */
public interface WeldEntityRepository extends
    WBSEntityBaseRepository<WeldEntity>, WBSEntityCustomRepository<WeldEntityBase, WeldEntryCriteriaDTO>, WeldEntityRepositoryCustom {

    /**
     * 根据焊口实体ID查询焊口实体信息。
     *
     * @param weldId 焊口实体ID
     * @return 焊口实体信息
     */
    WeldEntity findByIdAndDeletedIsFalse(Long weldId);

    /**
     * 根据焊口实体ID查询焊口实体信息。
     *
     * @param weldId 焊口实体ID
     * @return 焊口实体信息
     */
    WeldEntity findByProjectIdAndIdAndDeletedIsFalse(Long projectId, Long weldId);

    /**
     * 根据焊口实体ID查询焊口实体信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param no        焊口编号
     * @return 焊口实体信息
     */
    Optional<WeldEntity> findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(Long orgId, Long projectId, String no);

    /**
     * 根据焊口ID列表获取焊口列表。
     *
     * @param weldIds 焊口ID列表
     * @return 焊口列表
     */
    List<WeldEntity> findByIdInAndDeletedIsFalse(List<Long> weldIds);

    /**
     * 根据焊口ID获取焊口数量。
     *
     * @param weldIds 焊口ID列表
     * @return 焊口数量
     */
    int countByIdInAndDeletedIsFalse(List<Long> weldIds);

    /**
     * 根据焊工ID和是否执行NDT获取焊口数量。
     *
     * @param welderId       焊工ID
     * @param ndtExecuteFlag 是否执行NDT
     * @return 焊口数量
     */
    int countByWelderIdAndNdtExecuteFlagAndDeletedIsFalse(Long welderId, NDTExecuteFlag ndtExecuteFlag);

    List<WeldEntity> findByProjectIdAndDeletedIsFalse(Long projectId);

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
    @Query(value = "update entity_welds set wps_no = :wpsNo,wps_id = :wpsId,wps_edited=1,last_modified_by = :userId where deleted = 0 and org_id =:orgId and project_id = :projectId and no = :weldFullNo",
        nativeQuery = true)
    void updateWeldWpsNo(@Param("orgId") Long orgId,
                         @Param("projectId") Long projectId,
                         @Param("userId") Long userId,
                         @Param("weldFullNo") String weldFullNo,
                         @Param("wpsNo") String wpsNo,
                         @Param("wpsId") String wpsId);


    /**
     * 更新 PN，WELD 上的 ISO id和NO
     *
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE" +
        "      entity_welds AS e" +
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
        "      e.spool_entity_id = sp.id," +
        "      e.spool_no = sp.no," +
        "      pn.iso_entity_id = IFNULL(sp.iso_entity_id, ip.id)," +
        "      pn.iso_no = IFNULL(sp.iso_no, ip.no)," +
        "      pn.spool_entity_id = sp.id," +
        "      pn.spool_no = sp.no" +
        "    WHERE" +
        "      e.project_id = :projectId",
        nativeQuery = true)
    void updateWeldAndProjectNodeIds(@Param("projectId") Long projectId);

    /**
     * 更新 PN，WELD 上的 ISO id和NO
     *
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE" +
        "      entity_welds AS e" +
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

        "      pn.iso_entity_id = IFNULL(sp.iso_entity_id, ip.id)," +
        "      pn.iso_no = IFNULL(sp.iso_no, ip.no)," +
        "      pn.spool_entity_id = sp.id," +
        "      pn.spool_no = sp.no" +
        "    WHERE" +
        "      e.project_id = :projectId " +
        "       AND e.id = :entityIds",
        nativeQuery = true)
    void updateWeldAndProjectNodeIds(@Param("projectId") Long projectId, @Param("entityIds") Long entityId);

    List<WeldEntity> findByOrgIdAndProjectIdAndStatus(
        Long orgId,
        Long projectId,
        EntityStatus status
    );
}
