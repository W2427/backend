package com.ose.material.domain.model.repository;

import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmShippingEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


/**
 * 发货单库。
 */
@Transactional
public interface MmShippingRepository extends PagingAndSortingWithCrudRepository<MmShippingEntity, Long>, MmShippingRepositoryCustom{

    MmShippingEntity findByOrgIdAndProjectIdAndNameAndStatus(Long orgId, Long projectId, String name, EntityStatus entityStatus);

    MmShippingEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus entityStatus);

    /**
     * 获取流水号。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmShippingEntity e WHERE e.orgId = :orgId AND e.projectId = :projectId GROUP BY e.projectId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("orgId") Long orgId, @Param("projectId") Long projectId);
}
