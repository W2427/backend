package com.ose.tasks.domain.model.repository.wps;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.wps.WeldWelderRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 项目 CRUD 操作接口。
 */
@Transactional
public interface WeldWelderRelationRepository extends PagingAndSortingWithCrudRepository<WeldWelderRelation, Long>, WeldWelderRelationRepositoryCustom {

    /**
     * 查找焊口焊工关系详情。
     *
     * @param orgId
     * @param projectId
     * @param id
     * @param status
     * @return
     */
    WeldWelderRelation findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long id,
        EntityStatus status
    );

    /**
     * 查找焊口焊。
     *
     * @param orgId
     * @param projectId
     * @param weldId
     * @param repairCount
     * @param status
     * @return
     */
    List<WeldWelderRelation> findByOrgIdAndProjectIdAndWeldIdAndRepairCountAndStatus(
        Long orgId,
        Long projectId,
        Long weldId,
        Integer repairCount,
        EntityStatus status
    );

    /**
     * 查找焊口焊工。
     *
     * @param orgId
     * @param projectId
     * @param weldId
     * @param status
     * @return
     */
    List<WeldWelderRelation> findByOrgIdAndProjectIdAndWeldIdAndStatus(
        Long orgId,
        Long projectId,
        Long weldId,
        EntityStatus status
    );

    /**
     * 查找焊口焊工。
     *
     * @param orgId
     * @param projectId
     * @param weldNo
     * @param status
     * @return
     */
    List<WeldWelderRelation> findByOrgIdAndProjectIdAndWeldNoAndStatus(
        Long orgId,
        Long projectId,
        String weldNo,
        EntityStatus status
    );

    /** 查找对应焊口的所有焊工 IDs
     *
     */
    @Query(value = "SELECT welder_id FROM weld_welder_relation WHERE weld_id = :weldId AND org_id = :orgId AND project_id = :projectId",
    nativeQuery = true)
    Set<Long> findAllWelderIds(@Param("orgId") Long orgId,
                               @Param("projectId") Long projectId,
                               @Param("weldId") Long weldId);

}
