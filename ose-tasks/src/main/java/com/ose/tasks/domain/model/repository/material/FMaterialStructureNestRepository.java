package com.ose.tasks.domain.model.repository.material;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.dto.material.SeqNumberDTO;
import com.ose.tasks.entity.material.FMaterialStructureNest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface FMaterialStructureNestRepository extends PagingAndSortingWithCrudRepository<FMaterialStructureNest, Long>, FMaterialStructureNestRepositoryCustom {

    /**
     * 获取流水号。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Query("SELECT new com.ose.tasks.dto.material.SeqNumberDTO(MAX(e.seqNumber)) FROM FMaterialStructureNest e WHERE e.orgId = :orgId AND e.projectId = :projectId GROUP BY e.projectId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    /**
     * 通过组织id，项目id，结构套料id查找结构套料方案详情。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @return
     */
    FMaterialStructureNest findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long fMaterialStructureNestId);

    /**
     * 通过组织id，项目id，结构套料编号查找结构套料方案详情。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param name      结构套料方案名称
     * @return
     */
    FMaterialStructureNest findByOrgIdAndProjectIdAndNameAndDeletedIsFalse(Long orgId, Long projectId, String name);

    /**
     * 通过组织id，项目id，领料单id查找结构套料方案详情。
     *
     * @param orgId                  组织id
     * @param projectId              项目id
     * @param materialRequisitionsId 领料单id
     * @return
     */
    List<FMaterialStructureNest> findByOrgIdAndProjectIdAndMaterialRequisitionsIdAndDeletedIsFalse(Long orgId, Long projectId, String materialRequisitionsId);

    /**
     * 通过组织id，项目id，领料单id查找结构套料方案详情。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param fmirId    出库单id
     * @return
     */
    FMaterialStructureNest findByOrgIdAndProjectIdAndFmirIdAndDeletedIsFalse(Long orgId, Long projectId, Long fmirId);
}
