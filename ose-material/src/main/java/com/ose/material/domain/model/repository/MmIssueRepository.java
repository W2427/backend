package com.ose.material.domain.model.repository;

import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmIssueEntity;
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
 * 材料出库单库。
 */
@Transactional
public interface MmIssueRepository extends PagingAndSortingWithCrudRepository<MmIssueEntity, Long>, MmIssueRepositoryCustom {

    Page<MmIssueEntity> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status, Pageable pageable);

    MmIssueEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    /**
     * 获取流水号。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmIssueEntity e WHERE e.orgId = :orgId AND e.projectId = :projectId GROUP BY e.projectId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

}
