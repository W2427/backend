package com.ose.material.domain.model.repository;

import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmVendorEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 供货商 库。
 */
@Transactional
public interface MmVendorRepository extends PagingAndSortingWithCrudRepository<MmVendorEntity, Long> {

    Page<MmVendorEntity> findByOrgIdAndProjectIdAndSupplierCodeLikeAndStatus(Long orgId, Long projectId, String supplierCode, EntityStatus status, Pageable pageable);

    Page<MmVendorEntity> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status, Pageable pageable);

    MmVendorEntity findByOrgIdAndProjectIdAndSupplierCodeAndStatus(Long orgId, Long projectId, String supplierCode, EntityStatus status);

    MmVendorEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    Page<MmVendorEntity> findByCompanyIdAndSupplierCodeLikeAndStatus(Long companyId, String supplierCode, EntityStatus status, Pageable pageable);

    Page<MmVendorEntity> findByCompanyIdAndStatus(Long companyId, EntityStatus status, Pageable pageable);

    MmVendorEntity findByCompanyIdAndSupplierCodeAndStatus(Long companyId, String supplierCode, EntityStatus status);

    MmVendorEntity findByCompanyIdAndIdAndStatus(Long companyId, Long id, EntityStatus status);

    List<MmVendorEntity> findByCompanyVendorIdAndStatus(Long companyVendorId, EntityStatus status);

    /**
     * 获取项目级流水号。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmVendorEntity e WHERE e.orgId = :orgId AND e.projectId = :projectId GROUP BY e.projectId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    /**
     * 获取公司级流水号。
     *
     * @param companyId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmVendorEntity e WHERE e.companyId = :companyId GROUP BY e.companyId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("companyId") Long companyId);
}
