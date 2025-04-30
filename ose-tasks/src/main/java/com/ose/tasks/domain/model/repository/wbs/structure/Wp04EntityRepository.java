package com.ose.tasks.domain.model.repository.wbs.structure;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.Wp04EntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp04Entity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;


/**
 * 结构 构件 实体 CRUD 操作接口。
 */
public interface Wp04EntityRepository extends WBSEntityBaseRepository<Wp04Entity>, WBSEntityCustomRepository<Wp04Entity, Wp04EntryCriteriaDTO> {


    /**
     * 更新 PN 上的 Wp id和NO
     *
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "    UPDATE                  " +
        "entity_wp04 AS wp04                   " +
        "JOIN project_nodes AS pn               " +
        "ON wp04.id = pn.entity_id              " +
        "AND wp04.project_id = pn.project_id    " +
        "JOIN hierarchy_nodes AS hn             " +
        "ON hn.node_id = pn.id                  " +
        "JOIN hierarchy_nodes AS phn            " +
        "ON hn.parent_id = phn.id               " +
        "JOIN project_nodes AS ppn              " +
        "ON ppn.id = phn.node_id                " +
        "SET                                    " +


        "pn.wp04_id = wp04.id,                  " +
        "pn.wp04_no = wp04.no,                  " +
        "pn.wp01_id = ppn.wp01_id,              " +
        "pn.wp01_no = ppn.wp01_no,              " +
        "pn.wp02_id = ppn.wp02_id,              " +
        "pn.wp02_no = ppn.wp02_no,               " +
        "pn.wp03_id = ppn.wp03_id,              " +
        "pn.wp03_no = ppn.wp03_no                " +
        "    WHERE  " +
        "      wp04.project_id = :projectId        " +
        "      AND wp04.id = :entityId             ",
        nativeQuery = true)
    void updateWpAndProjectNodeIds(@Param("projectId") Long projectId, @Param("entityId") Long entityId);

    Optional<Wp04Entity> findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(Long id, Long orgId, Long projectId);

    @Query(value = "SELECT t.no " +
        " FROM " +
        " entity_wp04 AS t" +
        " WHERE " +
        " t.project_id = :projectId " +
        " AND id = :id "+
        "AND deleted = FALSE",nativeQuery = true)
    String findNoByProjectIdAndIdAndDeletedIsFalse(@Param("projectId") Long projectId, @Param("id") Long id);

    @Query("SELECT w.no FROM Wp04Entity w WHERE w.projectId = :projectId AND w.deleted = false ")
    Set<String> findNosByProjectIdAndDeletedIsFalse(@Param("projectId") Long projectId);
}
