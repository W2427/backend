package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmProcessCategory;
import com.ose.vo.EntityStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工序阶段 CRUD 操作接口。
 */
@Transactional
public interface BpmProcessCategoryRepository extends PagingAndSortingWithCrudRepository<BpmProcessCategory, Long> {

    Page<BpmProcessCategory> findByStatusAndProjectIdAndOrgId(EntityStatus active, Long projectId, Long orgId,
                                                              Pageable pageable);

    BpmProcessCategory findByOrgIdAndProjectIdAndNameCnAndStatus(Long orgId, Long projectId, String nameCn,
                                                                 EntityStatus status);


    @Transactional
    @Modifying
    @Query(value = "DELETE FROM bpm_process_category WHERE org_id = :orgId", nativeQuery = true)
    void deleteByOrgId(@Param("orgId") Long orgId);
}
