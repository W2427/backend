package com.ose.tasks.domain.model.repository.wbs.structure;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.Wp01EntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp01Entity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Module 实体 CRUD 操作接口。
 */
public interface Wp01EntityRepository extends WBSEntityBaseRepository<Wp01Entity>, WBSEntityCustomRepository<Wp01Entity, Wp01EntryCriteriaDTO> {


    /**
     * 更新 PN 上的 Wp id和NO
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE " +
        "      entity_wp01 AS wp01 " +
        "      INNER JOIN project_nodes AS pn " +
        "        ON pn.entity_id = wp01.id " +
        "    SET " +
        "      pn.wp01_id = wp01.id, " +
        "      pn.wp01_no = wp01.no, " +
        "      pn.wp02_id = NULL, " +
        "      pn.wp02_no = NULL, " +
        "      pn.wp03_id = NULL, " +
        "      pn.wp03_no = NULL, " +
        "      pn.wp04_id = NULL, " +
        "      pn.wp04_no = NULL " +
        "    WHERE" +
        "      wp01.project_id = :projectId " +
        "       AND wp01.id = :entityId",
        nativeQuery = true)
    void updateWpAndProjectNodeIds(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    Optional<Wp01Entity> findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(Long id, Long orgId, Long projectId);

    @Query("SELECT w.no FROM Wp01Entity w WHERE w.projectId = :projectId AND w.deleted = false ")
    Set<String> findNosByProjectIdAndDeletedIsFalse(@Param("projectId") Long projectId);

    List<Wp01Entity > findByOrgIdAndProjectIdAndDeletedIsFalseOrderByNo (
        Long orgId,
        Long projectId
    );

    List<Wp01Entity > findByOrgIdAndProjectIdAndNoNotLikeAndDeletedIsFalse (
        Long orgId,
        Long projectId,
        String no
    );
}
