package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmHeatBatchNoEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


/**
 * 炉批号库。
 */
@Transactional
public interface HeatBatchNoRepository extends PagingAndSortingWithCrudRepository<MmHeatBatchNoEntity, Long>, HeatBatchNoRepositoryCustom {

    Page<MmHeatBatchNoEntity> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status, Pageable pageable);

    MmHeatBatchNoEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    MmHeatBatchNoEntity findByOrgIdAndProjectIdAndHeatNoCodeAndBatchNoCodeAndStatus(Long orgId, Long projectId, String heatNoCode, String batchNoCode, EntityStatus status);

}
