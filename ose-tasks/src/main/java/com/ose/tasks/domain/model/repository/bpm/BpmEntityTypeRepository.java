package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 实体 CRUD 操作接口。
 */
@Transactional
public interface BpmEntityTypeRepository extends PagingAndSortingWithCrudRepository<BpmEntityType, Long> {

    Page<BpmEntityType> findByStatusAndProjectIdAndOrgIdAndType(EntityStatus status,
                                                                Long projectId,
                                                                Long orgId,
                                                                String type,
                                                                Pageable pageable);

    Optional<BpmEntityType> findByNameEnAndProjectIdAndOrgIdAndStatus(String name,
                                                                      Long projectId,
                                                                      Long orgId,
                                                                      EntityStatus status);

    Optional<BpmEntityType> findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(String name,
                                                                             Long projectId,
                                                                             Long orgId,
                                                                             String type,
                                                                             EntityStatus status);
    BpmEntityType findByProjectIdAndNameEnAndStatus(Long projectId,
                                                    String name,
                                                    EntityStatus status);

    List<BpmEntityType> findByProjectIdAndTypeAndStatus(Long projectId,
                                                        String name,
                                                        EntityStatus status);

    List<BpmEntityType> findByOrgIdAndProjectIdAndStatus(Long oldOrgId, Long oldProjectId, EntityStatus status);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM bpm_entity_type WHERE org_id = :orgId", nativeQuery = true)
    void deleteByOrgId(@Param("orgId") Long orgId);


    @Query(value = "SELECT name_en FROM bpm_entity_type WHERE org_id = :orgId AND " +
        "project_id = :projectId AND status = 'ACTIVE'",
    nativeQuery = true)
    List<String> findEntityTypeNames(
                                    @Param("orgId") Long orgId,
                                    @Param("projectId") Long projectId);

    @Query(value = "SELECT bect FROM BpmEntityType AS bect " +
        "WHERE bect.projectId = :projectId AND bect.type = :type AND bect.isFixedLevel = true AND bect.status = :status")
    List<BpmEntityType> findByProjectIdAndTypeAndFixedLevelAndStatus(Long projectId, String type, EntityStatus status);
}
