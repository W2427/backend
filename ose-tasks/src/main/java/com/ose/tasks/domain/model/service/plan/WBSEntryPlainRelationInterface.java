package com.ose.tasks.domain.model.service.plan;

import com.ose.dto.ContextDTO;
import com.ose.tasks.entity.wbs.entry.WBSEntry;

public interface WBSEntryPlainRelationInterface {

    /**
     * 创建 层级关系
     *
     * @param operatorId   操作人ID
     * @param orgId        组织ID
     * @param projectId    项目ID
     */
    void create(Long operatorId, Long orgId, Long projectId, ContextDTO context);


    /**
     * 将 wbsEntry path 转换成 扁平结构 存储
     * @param projectId p1
     * @param wbsEntry  p2
     */
    void saveWBSEntryPath(Long orgId, Long projectId, WBSEntry wbsEntry);
}
