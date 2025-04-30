package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.DesignChangeReviewRegister;
import com.ose.vo.EntityStatus;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DesignChangeReviewRegisterRepository extends PagingAndSortingWithCrudRepository<DesignChangeReviewRegister, Long>, DesignChangeReviewRegisterRepositoryCustom {

    DesignChangeReviewRegister findByOrgIdAndProjectIdAndVorNoAndStatus(Long orgId, Long projectId, String vorNo,
                                                                        EntityStatus status);

    @Query("SELECT count(t) FROM DesignChangeReviewRegister t where t.orgId = :orgId and t.projectId = :projectId and t.status = 'ACTIVE'")
    Long getCountByOrgIdAndProjectId(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    @Query("SELECT max(t.vorNo) FROM DesignChangeReviewRegister t where t.projectId = :projectId and t.status = 'ACTIVE'")
    String getMaxVorNoByProjectId(@Param("projectId") Long projectId);


}
