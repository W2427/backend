package com.ose.tasks.domain.model.repository.bpm;

import com.ose.tasks.entity.bpm.BpmCutting;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface BpmCuttingRepository extends PagingAndSortingRepository<BpmCutting, Long> {

    @Query("SELECT count(t) FROM BpmCutting t where t.orgId = :orgId and t.projectId = :projectId")
    Long getCountByOrgIdAndProjectId(@Param("orgId") Long orgId, @Param("projectId") Long projectId);

    BpmCutting findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);
}
