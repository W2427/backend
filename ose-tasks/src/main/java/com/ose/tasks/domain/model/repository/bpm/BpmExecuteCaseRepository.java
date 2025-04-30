package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.bpm.BpmExecuteCase;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务处理流程case库
 */
@Transactional
public interface BpmExecuteCaseRepository extends PagingAndSortingWithCrudRepository<BpmExecuteCase, Long>, BpmExecuteCaseRepositoryCustom {
    /**
     * 通过组织id，项目id，和任务处理caseId查找任务处理case。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        任务处理caseid
     * @return
     */
    BpmExecuteCase findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long id);
}
