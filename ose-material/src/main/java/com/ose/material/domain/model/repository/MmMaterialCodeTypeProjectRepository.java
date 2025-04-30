package com.ose.material.domain.model.repository;

import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmMaterialCodeTypeEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * 仓库 库。
 */
@Transactional
public interface MmMaterialCodeTypeProjectRepository extends PagingAndSortingWithCrudRepository<MmMaterialCodeTypeEntity, Long> {

    Page<MmMaterialCodeTypeEntity> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status, Pageable pageable);

    Page<MmMaterialCodeTypeEntity> findByOrgIdAndProjectIdAndNameLikeAndStatus(Long orgId, Long projectId, String name, EntityStatus status, Pageable pageable);

    MmMaterialCodeTypeEntity findByOrgIdAndProjectIdAndNameAndStatus(Long orgId, Long projectId, String name, EntityStatus status);

    MmMaterialCodeTypeEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    List<MmMaterialCodeTypeEntity> findByOrgIdAndProjectIdAndMaterialOrganizationTypeAndStatus(Long orgId, Long projectId, MaterialOrganizationType materialOrganizationType, EntityStatus status);

    List<MmMaterialCodeTypeEntity> findByProjectIdAndMaterialOrganizationTypeAndNameAndStatus(Long projectId, MaterialOrganizationType materialOrganizationType, String name, EntityStatus status);

    /**
     * 获取流水号。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmMaterialCodeTypeEntity e WHERE e.orgId = :orgId AND e.projectId = :projectId AND e.materialOrganizationType = :materialOrganizationType GROUP BY e.projectId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("materialOrganizationType") MaterialOrganizationType materialOrganizationType);



    Page<MmMaterialCodeTypeEntity> findByCompanyIdAndMaterialOrganizationTypeAndStatus(Long companyId, MaterialOrganizationType materialOrganizationType, EntityStatus status, Pageable pageable);

    Page<MmMaterialCodeTypeEntity> findByCompanyIdAndNameLikeAndMaterialOrganizationTypeAndStatus(Long companyId, String name, MaterialOrganizationType materialOrganizationType, EntityStatus status, Pageable pageable);

    MmMaterialCodeTypeEntity findByCompanyIdAndNameAndMaterialOrganizationTypeAndStatus(Long companyId, String name, MaterialOrganizationType materialOrganizationType, EntityStatus status);

    MmMaterialCodeTypeEntity findByCompanyIdAndIdAndStatus(Long companyId, Long id, EntityStatus status);

    /**
     * 获取流水号。
     *
     * @param companyId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmMaterialCodeTypeEntity e WHERE e.companyId = :companyId AND e.materialOrganizationType = :materialOrganizationType GROUP BY e.companyId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("companyId") Long companyId, @Param("materialOrganizationType") MaterialOrganizationType materialOrganizationType);

}
