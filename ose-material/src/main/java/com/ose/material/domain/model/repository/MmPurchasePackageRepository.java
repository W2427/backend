package com.ose.material.domain.model.repository;

import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmPurchasePackageEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 采购包 库。
 */
@Transactional
public interface MmPurchasePackageRepository extends PagingAndSortingWithCrudRepository<MmPurchasePackageEntity, Long>,MmPurchasePackageRepositoryCustom{

    MmPurchasePackageEntity findByOrgIdAndProjectIdAndPwpsNumberAndStatus(Long orgId, Long projectId, String PwpsNumber, EntityStatus status);

    MmPurchasePackageEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    /**
     * 获取流水号。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmPurchasePackageEntity e WHERE e.orgId = :orgId AND e.projectId = :projectId GROUP BY e.projectId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    @Query(value = "SELECT t.name_cn AS nameCn, t.name_en AS nameEn, t.id AS id FROM saint_whale_tasks.bpm_entity_type b " +
        "LEFT JOIN saint_whale_tasks.bpm_entity_sub_type t ON b.id = t.entity_type_id " +
        "WHERE b.org_id = :orgId AND b.project_id = :projectId AND b.name_en = :entityType " +
        "AND t.org_id = :orgId AND t.project_id = :projectId GROUP BY t.name_cn, t.name_en, t.id",
    nativeQuery = true)
    List<Map<String, Object>> findEntitySubTypeByEntityTypeAndProjectId(
        @Param("orgId")Long orgId,
        @Param("projectId")Long projectId,
        @Param("entityType")String entityType);


}
