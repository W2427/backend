package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.wbs.ISOEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.ISOEntity;
import com.ose.tasks.entity.wbs.entity.ISOEntityBase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 管线实体 CRUD 操作接口。
 */
public interface ISOEntityRepository extends WBSEntityBaseRepository<ISOEntity>, WBSEntityCustomRepository<ISOEntityBase, ISOEntryCriteriaDTO>,ISOEntityRepositoryCustom {

    /**
     * 根据ISO实体ID查询ISO实体信息。
     *
     * @param isoId ISO实体ID
     * @return ISO实体信息
     */
    ISOEntity findByIdAndDeletedIsFalse(Long isoId);

    ISOEntity findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long isoId);

    /**
     * 根据SpmMatchLnNode 节点信息更新 ISO 上的 BOM Match 信息
     *
     * @param matchPercent
     * @param matchDate
     * @param bomLnId
     * @param bomLnCode
     * @param projectId    项目ID
     * @param orgId        组织ID
     * @return 如果 更新成功 返回更新个数，如果没找到或失败 返回0
     */
    @Modifying
    @Transactional
    @Query(
        value = "UPDATE"
            + " entity_isos set match_percent = :matchPercent,"
            + " match_date = :matchDate,"
            + " bom_ln_id = :bomLnId"
            + " where bom_ln_code = :bomLnCode and "
            + " project_id = :projectId and org_id = :orgId",
        nativeQuery = true
    )
    int updateSpmMatchLnNode(@Param(value = "matchPercent") Float matchPercent,
                             @Param(value = "matchDate") Date matchDate,
                             @Param(value = "bomLnId") BigDecimal bomLnId,
                             @Param(value = "bomLnCode") String bomLnCode,
                             @Param(value = "projectId") Long projectId,
                             @Param(value = "orgId") Long orgId);

    List<ISOEntity> findByProjectIdAndDeletedIsFalse(Long projectId);

    /**
     * 查询项目中 bom_ln_code为NULL的所有ISO的数量
     *
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @return ISO实体信息
     */
    @Query("SELECT count(e) from ISOEntity e where e.bomLnCode is null and e.projectId=:projectId and e.orgId = :orgId")
    Long getIsoBomListCount(@Param(value = "projectId") Long projectId,
                            @Param(value = "orgId") Long orgId);

    /**
     * 查询项目中 bom_ln_code为NULL的所有ISO的列表
     *
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @param lLimit    limit下限
     * @param uLimit    limit上限
     * @return ISO实体信息
     */
    @Query(
        value = "SELECT id, bom_ln_code, created_at from entity_isos where isnull(bom_ln_code) and project_id=:projectId and org_id = :orgId limit :lLimit,:uLimit",
        nativeQuery = true
    )
    List<ISOEntity> findAllByBomLnCodeNotNull(@Param(value = "projectId") Long projectId,
                                              @Param(value = "orgId") Long orgId,
                                              @Param(value = "lLimit") Long lLimit,
                                              @Param(value = "uLimit") Long uLimit);

    /**
     * 查询项目中 bom_ln_code为NULL的所有ISO的列表
     *
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @param pageable  limit上下限
     * @return ISO实体信息
     */
    Page<ISOEntity> findAllByProjectIdAndOrgIdAndBomLnCodeIsNotNullAndDeletedIsFalse(Long projectId,
                                                                                     Long orgId,
                                                                                     Pageable pageable);


    /**
     * 根据SpmMatchLnNode 节点信息更新 ISO 上的 BOM Match 信息
     *
     * @param matchPercent
     * @param matchDate
     * @param bomLnId
     * @param isoId
     * @return 如果 更新成功 返回更新个数，如果没找到或失败 返回0
     */
    @Modifying
    @Transactional
    @Query(
        value = "UPDATE"
            + " entity_isos set match_percent = :matchPercent,"
            + " match_date = :matchDate,"
            + " bom_ln_id = :bomLnId"
            + " where id=:isoId",
        nativeQuery = true
    )
    int updateSpmMatchLnNodeByIsoId(@Param(value = "matchPercent") Float matchPercent,
                                    @Param(value = "matchDate") Date matchDate,
                                    @Param(value = "bomLnId") BigDecimal bomLnId,
                                    @Param(value = "isoId") Long isoId);


    /**
     * 更新 PN 上的 ISO id和NO
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE " +
        "      entity_isos AS e " +
        "      INNER JOIN project_nodes AS pn " +
        "        ON pn.entity_id = e.id " +
        "    SET " +
        "      pn.iso_entity_id = e.id, " +
        "      pn.iso_no = e.no, " +
        "      pn.spool_entity_id = NULL, " +
        "      pn.spool_no = NULL " +
        "    WHERE" +
        "      e.project_id = :projectId",
    nativeQuery = true)
    void updateIsoAndProjectNodeIds(@Param("projectId") Long projectId);

    /**
     * 更新 PN 上的 ISO id和NO
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE " +
        "      entity_isos AS e " +
        "      INNER JOIN project_nodes AS pn " +
        "        ON pn.entity_id = e.id " +
        "    SET " +
        "      pn.iso_entity_id = e.id, " +
        "      pn.iso_no = e.no, " +
        "      pn.spool_entity_id = NULL, " +
        "      pn.spool_no = NULL " +
        "    WHERE" +
        "      e.project_id = :projectId " +
        "       AND e.id = :entityId",
        nativeQuery = true)
    void updateIsoAndProjectNodeIds(@Param("projectId") Long projectId, @Param("entityId") Long entityId);
}
