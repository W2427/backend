package com.ose.material.domain.model.repository;

import com.ose.material.dto.SeqNumberDTO;
import com.ose.material.entity.MmWareHouseLocationEntity;
import com.ose.material.vo.WareHouseLocationType;
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
 * 仓库货位 库。
 */
@Transactional
public interface MmWareHouseLocationRepository extends PagingAndSortingWithCrudRepository<MmWareHouseLocationEntity, Long>, MmWareHouseLocationRepositoryCustom {

    Page<MmWareHouseLocationEntity> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status, Pageable pageable);

    MmWareHouseLocationEntity findByOrgIdAndProjectIdAndNameAndStatus(Long orgId, Long projectId, String name, EntityStatus status);

    MmWareHouseLocationEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    List<MmWareHouseLocationEntity> findByOrgIdAndProjectIdAndTypeAndStatus(Long orgId, Long projectId, WareHouseLocationType type, EntityStatus status);

    List<MmWareHouseLocationEntity> findByOrgIdAndProjectIdAndParentWareHouseIdAndStatus(Long orgId, Long projectId, Long parentWareHouseId, EntityStatus status);

    /**
     * 获取流水号。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Query("SELECT new com.ose.material.dto.SeqNumberDTO(MAX(e.seqNumber)) FROM MmWareHouseLocationEntity e WHERE e.orgId = :orgId AND e.projectId = :projectId AND e.type = :type GROUP BY e.projectId")
    Optional<SeqNumberDTO> getMaxSeqNumber(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("type") WareHouseLocationType type);

}
