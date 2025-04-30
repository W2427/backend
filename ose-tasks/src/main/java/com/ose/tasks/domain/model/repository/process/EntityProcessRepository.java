package com.ose.tasks.domain.model.repository.process;

import com.ose.tasks.entity.process.EntityProcess;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * 实体工序视图数据仓库。
 */
public interface EntityProcessRepository extends CrudRepository<EntityProcess, Long> {

    /**
     * 取得实体工序列表。
     *
     * @param projectId 项目 ID
     * @return 实体工序列表
     */
    List<EntityProcess> findByProjectId(Long projectId);

    /**
     * 取得工序信息。
     *
     * @param projectId 项目 ID
     * @param processId 工序 ID
     * @return 工序信息
     */
    Optional<EntityProcess> findByProjectIdAndProcessId(Long projectId, Long processId);

    /**
     * 取得工序信息。
     *
     * @param projectId   项目 ID
     * @param stageName   工序阶段名称
     * @param processName 工序名称
     * @return 工序信息
     */
    Optional<EntityProcess> findByProjectIdAndStageNameAndProcessName(Long projectId, String stageName, String processName);

}
