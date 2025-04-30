package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.process.ProcessEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 工序-实体关系视图 CRUD 操作接口。
 */
public interface ProcessEntityRepository extends PagingAndSortingRepository<ProcessEntity, String> {

    /**
     * 取得实体项目节点列表。
     *
     * @param projectId             项目 ID
     * @param stage                 工序阶段
     * @param process               工序
     * @param hierarchyAncestorId   上级层级节点 ID
     * @return 实体的项目节点列表
     */
    @Query("SELECT p FROM ProcessEntity p LEFT JOIN WBSEntry we ON p.projectId = we.projectId AND p.entityId = we.entityId AND we.processId = p.processId " +
        "WHERE p.projectId = :projectId " +
        "AND p.stage = :stage AND p.process = :process AND p.hierarchyAncestorId = :hierarchyAncestorId AND we.id IS NULL ORDER BY " +
        "p.projectNodeNo ASC, p.hierarchyDepth DESC")
    List<ProcessEntity> findAddedProcessEntities(
        @Param("projectId") Long projectId,
        @Param("stage") String stage,
        @Param("process") String process,
        @Param("hierarchyAncestorId") Long hierarchyAncestorId
    );

    List<ProcessEntity> findByProjectIdAndHierarchyAncestorIdAndStageAndProcessAndFuncPartOrderByProjectNodeNoAscHierarchyDepthDesc(
        Long projectId,
        Long hierarchyAncestorId,
        String stage,
        String process,
        String funcPart
    );



    /**
     * 根据项目节点 ID 取得工序-实体关系信息。
     *
     * @param projectId     项目 ID
     * @param projectNodeId 实体的项目节点 ID
     * @param stage         工序阶段名称
     * @param process       工序名称
     * @return 工序-实体关系信息
     */
    Optional<ProcessEntity> findByProjectIdAndProjectNodeIdAndStageAndProcess(
        Long projectId,
        Long projectNodeId,
        String stage,
        String process
    );

    /**
     * 根据实体类型及 ID 取得工序-实体关系信息。
     *
     * @param projectId  项目 ID
     * @param entityType 实体类型
     * @param entityId   实体 ID
     * @param stage      工序阶段名称
     * @param process    工序名称
     * @return 工序-实体关系信息
     */
    Optional<ProcessEntity> findFirstByProjectIdAndEntityTypeAndEntityIdAndStageAndProcessOrderByIdAsc(
        Long projectId,
        String entityType,
        Long entityId,
        String stage,
        String process
    );

    /**
     * 查询实体工序关系分页数据。
     *
     * @param projectId   项目 ID
     * @param stageName   工序阶段名称
     * @param processName 工序名称
     * @param entityType  实体类型
     * @param pageable    分页参数
     * @return 实体工序关系分页数据
     */
    Page<ProcessEntity> findByProjectIdAndStageAndProcessAndEntityType(
        Long projectId,
        String stageName,
        String processName,
        String entityType,
        Pageable pageable
    );

    /**
     * 查询补加的实体
     *
     * @param projectId     项目ID
     * @param stage
     * @param process
     * @param entityId
     * @param hierarchyType
     * @return
     */

    ProcessEntity findFirstByProjectIdAndStageAndProcessAndEntityIdAndHierarchyType(
        Long projectId,
        String stage,
        String process,
        Long entityId,
        String hierarchyType
    );

    ProcessEntity findByProjectIdAndProcessIdAndEntityId(Long projectId, Long processId, Long entityId);
}
