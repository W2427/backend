package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmImportBatchTask;
import com.ose.material.vo.MaterialImportType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 材料批处理任务状态记录 CRUD 操作接口。
 */
@Transactional
public interface MmImportBatchTaskRepository extends PagingAndSortingWithCrudRepository<MmImportBatchTask, Long> {

    /**
     * 根据批处理任务 ID 及所属组织 ID 取得批处理任务。
     *
     * @param id    批处理任务 ID
     * @param orgId 所属组织 ID
     * @return 批处理任务信息
     */
    Optional<MmImportBatchTask> findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long id);

    boolean existsByProjectIdAndCodeAndStatus(Long projectId, MaterialImportType code, EntityStatus status);

    boolean existsByProjectIdAndCodeInAndRunningIsTrue(Long projectId, MaterialImportType[] batchTaskCode);

    Page<MmImportBatchTask> findByOrgIdAndProjectId(
        Long orgId,
        Long projectId,
        Pageable pageable
    );

    Page<MmImportBatchTask> findByOrgIdAndProjectIdAndCodeAndEntityId(
        Long orgId,
        Long projectId,
        MaterialImportType code,
        Long entityId,
        Pageable pageable
    );

    Page<MmImportBatchTask> findByOrgIdAndProjectIdAndEntityId(
        Long orgId,
        Long projectId,
        Long entityId,
        Pageable pageable
    );

    boolean existsByProjectIdAndCodeAndEntityIdAndStatus(Long projectId, MaterialImportType code, Long entityId, EntityStatus status);
}
