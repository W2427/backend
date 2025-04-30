package com.ose.tasks.domain.model.repository.wbs.structure;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.Wp03EntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp03Entity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;


/**
 * 结构 Panel 实体 CRUD 操作接口。
 */
public interface Wp03EntityRepository extends WBSEntityBaseRepository<Wp03Entity>, WBSEntityCustomRepository<Wp03Entity, Wp03EntryCriteriaDTO> {


    /**
     * 更新 PN 上的 Wp id和NO
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "    UPDATE                  " +
        "entity_wp03 AS wp03                   " +
        "JOIN project_nodes AS pn               " +
        "ON wp03.id = pn.entity_id              " +
        "AND wp03.project_id = pn.project_id    " +
        "JOIN hierarchy_nodes AS hn             " +
        "ON hn.node_id = pn.id                  " +
        "JOIN hierarchy_nodes AS phn            " +
        "ON hn.parent_id = phn.id               " +
        "JOIN project_nodes AS ppn              " +
        "ON ppn.id = phn.node_id                " +
        "SET                                    " +




        "pn.wp03_id = wp03.id,                  " +
        "pn.wp03_no = wp03.no,                  " +
        "pn.wp01_id = ppn.wp01_id,              " +
        "pn.wp01_no = ppn.wp01_no,              " +
        "pn.wp02_id = ppn.id,                   " +
        "pn.wp02_no = ppn.no                    " +
        "    WHERE  " +
        "      wp03.project_id = :projectId        " +
        "      AND wp03.id = :entityId             ",
        nativeQuery = true)
    void updateWpAndProjectNodeIds(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    Optional<Wp03Entity> findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(Long id, Long orgId, Long projectId);

    @Query("SELECT w.no FROM Wp03Entity w WHERE w.projectId = :projectId AND w.deleted = false ")
    Set<String> findNosByProjectIdAndDeletedIsFalse(@Param("projectId") Long projectId);}
