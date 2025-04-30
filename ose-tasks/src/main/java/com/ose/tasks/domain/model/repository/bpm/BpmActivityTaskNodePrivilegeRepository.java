package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ose.tasks.entity.bpm.BpmActivityTaskNodePrivilege;
import com.ose.vo.EntityStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface BpmActivityTaskNodePrivilegeRepository extends PagingAndSortingWithCrudRepository<BpmActivityTaskNodePrivilege, Long> {

    List<BpmActivityTaskNodePrivilege> findByOrgIdAndProjectIdAndProcessIdAndStatus(Long orgId, Long projectId, Long processId, EntityStatus status);

    BpmActivityTaskNodePrivilege findByOrgIdAndProjectIdAndProcessIdAndTaskDefKeyAndStatus(Long orgId,
                                                                                           Long projectId, Long processId, String taskDefKey, EntityStatus status);

    List<BpmActivityTaskNodePrivilege> findByOrgIdAndProjectIdAndProcessIdAndCategoryAndStatus(Long orgId,
                                                                                               Long projectId, Long processId, String category, EntityStatus status);

    List<BpmActivityTaskNodePrivilege> findByOrgIdAndProjectIdAndProcessIdInAndStatus(Long orgId, Long projectId,
                                                                                      List<Long> processIDs, EntityStatus status);
}
