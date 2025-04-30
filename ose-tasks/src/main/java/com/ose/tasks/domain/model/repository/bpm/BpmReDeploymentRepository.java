package com.ose.tasks.domain.model.repository.bpm;

import java.util.List;
import java.util.Optional;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.tasks.entity.bpm.BpmReDeployment;
import com.ose.tasks.vo.SuspensionState;

/**
 * 部署流程的 CRUD 操作接口。
 */
@Transactional
public interface BpmReDeploymentRepository extends PagingAndSortingWithCrudRepository<BpmReDeployment, Long>, BpmReDeploymentRepositoryCustom {

    Optional<BpmReDeployment> findByProcDefId(String procDefId);

    List<BpmReDeployment> findByCategoryAndOrgIdAndProjectIdAndSuspensionState(String category, Long orgId,
                                                                               Long projectId, SuspensionState state);

    BpmReDeployment findFirstByProjectIdAndProcessIdOrderByVersionDesc(Long projectId, Long bpmProcessId);

    BpmReDeployment findByProjectIdAndProcessIdAndVersion(Long projectId, Long processId, int bpmnVersion);

    @Query("SELECT r FROM BpmReDeployment r WHERE r.procDefId NOT LIKE '1%' AND r.procDefId NOT LIKE 'B%'")
    List<BpmReDeployment> findPatchedBpmn();

    BpmReDeployment findFirstByProjectIdAndProcessIdAndVersionOrderByIdDesc(Long projectId, Long processId, int bpmnVersion);
}
