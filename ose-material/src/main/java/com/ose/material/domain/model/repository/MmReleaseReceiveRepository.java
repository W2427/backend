package com.ose.material.domain.model.repository;

import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmReleaseReceiveEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 材料入库单库。
 */
@Transactional
public interface MmReleaseReceiveRepository extends PagingAndSortingWithCrudRepository<MmReleaseReceiveEntity, Long>, MmReleaseReceiveRepositoryCustom {

    MmReleaseReceiveEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    MmReleaseReceiveEntity findByOrgIdAndProjectIdAndNameAndStatus(Long orgId, Long projectId, String name, EntityStatus status);

    Page<MmReleaseReceiveEntity> findByOrgIdAndProjectIdAndMmShippingIdAndStatus(Long orgId, Long projectId, Long mmShippingId, EntityStatus status, Pageable pageable);

    /**
     * 获取流水号。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmReleaseReceiveEntity e WHERE e.orgId = :orgId AND e.projectId = :projectId GROUP BY e.projectId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("orgId") Long orgId, @Param("projectId") Long projectId);
}
