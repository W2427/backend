package com.ose.tasks.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.tasks.entity.HierarchyNode;

public interface HierarchyNodeRelationInterface {

    /**
     * 创建 层级关系
     *
     * @param operatorId   操作人ID
     * @param orgId        组织ID
     * @param projectId    项目ID
     */
    void create(Long operatorId, Long orgId, Long projectId, ContextDTO contextDTO);


    /**
     * 将 hierarchy path 转换成 扁平结构 存储
     * @param projectId
     * @param node
     */
    void saveHierarchyPath(Long orgId, Long projectId, HierarchyNode node);
}
