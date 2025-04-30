package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmActivityNodePrivilege;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface BpmActivityNodePrivilegeRepository extends PagingAndSortingWithCrudRepository<BpmActivityNodePrivilege, Long> {

    List<BpmActivityNodePrivilege> findByOrgIdAndProjectIdAndProcessIdAndStatus(Long orgId, Long projectId, Long processId, EntityStatus status);

    void deleteByOrgIdAndProjectIdAndProcessId(Long orgId, Long projectId, Long processId);

    @Transactional
    @Modifying
    @Query(
        value = "Update BpmActivityNodePrivilege banp SET banp.status = com.ose.vo.EntityStatus.DELETED Where banp.orgId=:orgId and banp.projectId=:projectId and banp.processId=:processId"
    )
    void updateByOrgIdAndProjectIdAndProcessId(@Param("orgId") Long orgId, @Param("projectId") Long projectId, @Param("processId") Long processId);
}
