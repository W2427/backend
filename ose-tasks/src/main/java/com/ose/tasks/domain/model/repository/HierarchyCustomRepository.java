package com.ose.tasks.domain.model.repository;

import com.ose.tasks.dto.ModuleDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;

import java.util.List;

public interface HierarchyCustomRepository {

    /**
     * 启动实体工序计划实施进度设置处理。
     *
     * @param projectId 项目 ID
     */
    void startSettingWBSProgress(Long projectId);

    List<HierarchyNode> searchChild(
        Long projectId,
        Long entityId
    );

    List<ModuleDTO> searchEntity(
        Long orgId,
        Long projectId,
        Long entityId,
        String hierarchyType
    );

    List<ModuleDTO> searchEntityTest(
        Long orgId,
        Long projectId,
        Long processId,
        String moduleNo,
        WBSEntryRunningStatus runningStatus
    );

    /**
     * 查找模块下所有管段长度总和
     *
     * @param orgId
     * @param projectId
     * @param entityNo
     * @return
     */
    Double searchPip(
        Long orgId,
        Long projectId,
        String entityNo
    );

    /**
     * 查找模块下所有管附件数量
     *
     * @param orgId
     * @param projectId
     * @param entityNo
     * @return
     */
    Integer searchComponent(
        Long orgId,
        Long projectId,
        String entityNo
    );

    /**
     * 查找模块下所有零件重量。
     *
     * @param orgId
     * @param projectId
     * @param wp01EntityId
     * @return
     */
    Double sumTotalWeight(Long orgId,
                          Long projectId,
                          Long wp01EntityId);
}
