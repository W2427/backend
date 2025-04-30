package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmSurplusMaterialEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 余料库。
 */
@Transactional
public interface MmSurplusMaterialRepository extends PagingAndSortingWithCrudRepository<MmSurplusMaterialEntity, Long>, MmSurplusMaterialRepositoryCustom {

    Page<MmSurplusMaterialEntity> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status, Pageable pageable);

    MmSurplusMaterialEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    MmSurplusMaterialEntity findByOrgIdAndProjectIdAndNameAndStatus(Long orgId, Long projectId, String name, EntityStatus status);


}
