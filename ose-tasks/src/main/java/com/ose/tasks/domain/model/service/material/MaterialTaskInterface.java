package com.ose.tasks.domain.model.service.material;

import com.ose.dto.ContextDTO;
import com.ose.tasks.dto.material.MaterialTaskDTO;

public interface MaterialTaskInterface {

    /**
     * 创建采购包任务。
     *
     * @param orgId                          组织ID
     * @param projectId                      项目ID
     * @param materialTaskDTO 采购包任务创建信息
     */
    void createPurchasePackageTask(
        Long orgId,
        Long projectId,
        ContextDTO context,
        MaterialTaskDTO materialTaskDTO
    );

    /**
     * 创建入库单任务。
     *
     * @param orgId                          组织ID
     * @param projectId                      项目ID
     * @param materialTaskDTO 采购包任务创建信息
     */
    void createReceiveTask(
        Long orgId,
        Long projectId,
        ContextDTO context,
        MaterialTaskDTO materialTaskDTO
    );

}
