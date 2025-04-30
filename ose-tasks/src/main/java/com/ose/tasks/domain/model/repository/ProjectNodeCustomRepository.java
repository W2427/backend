package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.taskpackage.TaskPackageProjectNodeEntitySearchDTO;
import com.ose.tasks.dto.wbs.WBSEntryExecutionHistoryDTO;
import com.ose.tasks.entity.ProjectNode;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 项目节点 CRUD 操作接口。
 */
public interface ProjectNodeCustomRepository {

    /**
     * 设置实体信息及项目节点的所属管线及单管实体的 ID。
     *
     * @param projectId 项目 ID
     */
    void setParentISOAndSpoolEntityIDs(Long projectId);

    /**
     * 取得要插入的 新增实体 到 wbs_entry_execution_history
     *
     * @param operatorId
     * @param projectId  项目ID
     * @param entityId
     * @return
     */
    List<WBSEntryExecutionHistoryDTO> getWbsEntryExecutionHistory(Long operatorId, Long projectId, Long entityId);

    /**
     * 设置WP实体信息被删除后更新其他 WP表
     *
     * @param projectId 项目ID
     * @param wpName
     * @param entityId
     */
    void setDeletedParentEntityIdOnStructureEntities(Long projectId, String wpName, Long entityId);


    /**
     * 设置结构实体的 WP01/02/03/04/05
     *
     * @param projectId 项目ID
     * @param wpName
     * @param entityId
     */
    void setParentEntityIdOnStructureEntity(Long projectId, String wpName, Long entityId);

    /**
     * 设置结构实体的 WP01/02/03/04/05
     *
     * @param projectId 项目ID
     */

    void setParentEntityIdOnStructureEntities(Long projectId);


    /**
     * 查询指定父级下指定实体。
     *
     * @param orgId
     * @param projectId
     * @param taskPackageProjectNodeEntitySearchDTO
     * @return
     */
    Page<ProjectNode> taskPackageEntities(Long orgId, Long projectId, TaskPackageProjectNodeEntitySearchDTO taskPackageProjectNodeEntitySearchDTO);

}
