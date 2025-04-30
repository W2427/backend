package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmReleaseReceiveFileEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库单文件库。
 */
@Transactional
public interface MmReleaseReceiveFileRepository extends PagingAndSortingWithCrudRepository<MmReleaseReceiveFileEntity, Long>, MmReleaseReceiveFileCustom {

    MmReleaseReceiveFileEntity findByOrgIdAndProjectIdAndReleaseReceiveIdAndIdAndStatus(Long orgId, Long projectId, Long releaseReceiveId, Long id, EntityStatus status);

}
