package com.ose.report.domain.repository;

import com.ose.report.entity.ChecklistSimulation;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 模拟检查单（测试） CRUD 操作接口。
 */
@Transactional
public interface ChecklistSimulationRepository extends PagingAndSortingWithCrudRepository<ChecklistSimulation, Long> {

    /**
     * 根据项目ID查询模拟检查单
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageable  分页信息
     * @return 模拟检查单
     */
    Page<ChecklistSimulation> findAllByOrgIdAndProjectId(Long orgId, Long projectId, Pageable pageable);
}
