package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmMaterialCertificate;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 材料证书操作接口。
 */
@Transactional
public interface MmMaterialCertificateRepository extends PagingAndSortingWithCrudRepository<MmMaterialCertificate, Long>, MmMaterialCertificateRepositoryCustom {

    MmMaterialCertificate findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long materialCertificateId,
        EntityStatus status
    );

    MmMaterialCertificate findByOrgIdAndProjectIdAndNoAndStatus(
        Long orgId,
        Long projectId,
        String materialCertificateNo,
        EntityStatus status
    );

}
