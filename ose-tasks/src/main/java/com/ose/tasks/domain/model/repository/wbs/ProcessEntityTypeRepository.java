package com.ose.tasks.domain.model.repository.wbs;

import com.ose.tasks.entity.process.ProcessEntityType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

/**
 * 工序-实体类型关系视图 CRUD 操作接口。
 */
public interface ProcessEntityTypeRepository extends CrudRepository<ProcessEntityType, String> {

    /**
     * 取得工序-实体类型关系列表。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @return 工序-实体类型关系列表
     */
    List<ProcessEntityType> findByOrgIdAndProjectId(
        Long orgId,
        Long projectId
    );

    /**
     * 取得 实体类型-工序 关系列表
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param entityType
     * @param entitySubType
     * @return
     */
    List<ProcessEntityType> findByOrgIdAndProjectIdAndEntityTypeAndEntitySubType(
        Long orgId,
        Long projectId,
        String entityType,
        String entitySubType);

    /**
     * 取得 实体类型-工序 关系列表
     *
     * @param orgId         组织ID
     * @param projectId     项目ID
     * @param entityType
     * @param entitySubType
     * @return
     */
    List<ProcessEntityType> findByProjectIdAndEntityTypeAndEntitySubType(
        Long projectId,
        String entityType,
        String entitySubType);

    Set<Long> findByProjectIdAndStageNameAndProcessNameAndEntityTypeAndEntitySubTypeIn(
        Long projectId,
        String stage,
        String process,
        String entityType,
        Set<String> entitySubTypes
    );

    Long findByProjectIdAndStageNameAndProcessNameAndEntityTypeAndEntitySubType(
        Long projectId,
        String stage,
        String process,
        String entityType,
        String entitySubType
    );
}
