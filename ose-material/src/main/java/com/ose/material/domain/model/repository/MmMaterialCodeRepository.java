package com.ose.material.domain.model.repository;


import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 材料编码库。
 */
@Transactional
public interface MmMaterialCodeRepository extends PagingAndSortingWithCrudRepository<MmMaterialCodeEntity, Long> {

    @Query(value = "SELECT * FROM mm_material_code m WHERE m.company_id = :companyId AND m.material_organization_type = 'COMPANY' AND m.STATUS = 'ACTIVE' AND m.no NOT IN ( SELECT m.no FROM mm_material_code m WHERE m.material_organization_type = 'PROJECT' AND m.STATUS = 'ACTIVE' AND m.org_id = :orgId AND m.project_id = :projectId )", nativeQuery = true)
    Page<MmMaterialCodeEntity> getNoAdd(
        @Param("companyId") Long companyId,
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        Pageable pageable
    );

    Page<MmMaterialCodeEntity> findByOrgIdAndProjectIdAndMaterialOrganizationTypeAndStatusOrderByIdentCode(Long orgId, Long projectId, MaterialOrganizationType materialOrganizationType, EntityStatus status, Pageable pageable);

    Page<MmMaterialCodeEntity> findByOrgIdAndProjectIdAndNoLikeAndMaterialOrganizationTypeAndStatusOrderByIdentCode(Long orgId, Long projectId, String no, MaterialOrganizationType materialOrganizationType, EntityStatus status, Pageable pageable);

    Page<MmMaterialCodeEntity> findByOrgIdAndProjectIdAndDescriptionLikeAndMaterialOrganizationTypeAndStatusOrderByIdentCode(Long orgId, Long projectId, String description, MaterialOrganizationType materialOrganizationType, EntityStatus status, Pageable pageable);

    MmMaterialCodeEntity findByOrgIdAndProjectIdAndNoAndMaterialOrganizationTypeAndStatus(Long orgId, Long projectId, String no, MaterialOrganizationType materialOrganizationType, EntityStatus status);

    MmMaterialCodeEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    MmMaterialCodeEntity findByOrgIdAndProjectIdAndNoAndStatus(Long orgId, Long projectId, String no, EntityStatus status);


    /**
     * 获取流水号。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmMaterialCodeEntity e WHERE e.orgId = :orgId AND e.projectId = :projectId AND e.materialOrganizationType = :materialOrganizationType GROUP BY e.projectId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("materialOrganizationType") MaterialOrganizationType materialOrganizationType);

    Page<MmMaterialCodeEntity> findByCompanyIdAndMaterialOrganizationTypeAndStatusOrderByNo(Long companyId, MaterialOrganizationType materialOrganizationType, EntityStatus status, Pageable pageable);

    Page<MmMaterialCodeEntity> findByCompanyIdAndNoLikeAndMaterialOrganizationTypeAndStatusOrderByNo(Long companyId, String no, MaterialOrganizationType materialOrganizationType, EntityStatus status, Pageable pageable);

    MmMaterialCodeEntity findByCompanyIdAndNoAndMaterialOrganizationTypeAndStatus(Long companyId, String no, MaterialOrganizationType materialOrganizationType, EntityStatus status);

    MmMaterialCodeEntity findByCompanyIdAndIdAndStatus(Long companyId, Long id, EntityStatus status);

    /**
     * 获取流水号。
     *
     * @param companyId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmMaterialCodeEntity e WHERE e.companyId = :companyId AND e.materialOrganizationType = :materialOrganizationType GROUP BY e.companyId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("companyId") Long companyId, @Param("materialOrganizationType") MaterialOrganizationType materialOrganizationType);

//    List<MmMaterialCodeEntity> findByMmWareHouseIdAndStatus(Long companyId, EntityStatus status);

    List<MmMaterialCodeEntity> findByMmMaterialCodeTypeNameAndStatus(String mmMaterialCodeTypeName, EntityStatus status);

    List<MmMaterialCodeEntity> findByProjectIdAndNoAndStatus(Long projectId, String no, EntityStatus status);
}
