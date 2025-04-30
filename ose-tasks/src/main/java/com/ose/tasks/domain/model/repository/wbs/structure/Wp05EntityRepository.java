package com.ose.tasks.domain.model.repository.wbs.structure;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.Wp05EntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp05Entity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * 结构 Part 实体 CRUD 操作接口。
 */
public interface Wp05EntityRepository extends WBSEntityBaseRepository<Wp05Entity>, WBSEntityCustomRepository<Wp05Entity, Wp05EntryCriteriaDTO> {


    /**
     * 更新 PN 上的 Wp id和NO
     *
     * @param projectId
     */
    @Modifying
    @Transactional
    @Query(value = "    UPDATE                  " +
        "entity_wp05 AS wp05                   " +
        "JOIN project_nodes AS pn               " +
        "ON wp05.id = pn.entity_id              " +
        "AND wp05.project_id = pn.project_id    " +
        "JOIN hierarchy_nodes AS hn             " +
        "ON hn.node_id = pn.id                  " +
        "JOIN hierarchy_nodes AS phn            " +
        "ON hn.parent_id = phn.id               " +
        "JOIN project_nodes AS ppn              " +
        "ON ppn.id = phn.node_id                " +
        "SET                                    " +








        "pn.wp05_id = wp05.id,                  " +
        "pn.wp05_no = wp05.no,                  " +
        "pn.wp01_id = ppn.wp01_id,              " +
        "pn.wp01_no = ppn.wp01_no,              " +
        "pn.wp02_id = ppn.wp02_id,              " +
        "pn.wp02_no = ppn.wp02_no,               " +
        "pn.wp03_id = ppn.wp03_id,              " +
        "pn.wp03_no = ppn.wp03_no,                " +
        "pn.wp04_id = ppn.wp04_id,              " +
        "pn.wp05_no = ppn.wp04_no                " +
        "    WHERE  " +
        "      wp05.project_id = :projectId        " +
        "      AND wp05.id = :entityId             ",
        nativeQuery = true)
    void updateWpAndProjectNodeIds(@Param("projectId") Long projectId, @Param("entityId") Long entityId);
    @Query(value="SELECT " +
        " no " +
        "FROM " +
        " entity_wp05  " +
        "WHERE " +
        " project_id = :projectId  " +
        " AND `no` = :no  " +
        " AND deleted = FALSE",nativeQuery=true)
    String findNoByProjectIdAndNoAndDeletedIsFalse(@Param("projectId") Long projectId,@Param("no")String no);

    Wp05Entity findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long id);

    @Override
    Optional<Wp05Entity> findByIdAndOrgIdAndProjectIdAndDeletedIsFalse(Long id, Long orgId, Long projectId);

    Optional<Wp05Entity> findByNoAndProjectIdAndDeletedIsFalse(String no, Long projectId);
}
