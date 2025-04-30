package com.ose.tasks.domain.model.service.qc;

public interface ExInspActInstRelationInterface {

    /**
     * 创建外检和流程的关系数据。
     *
     * @param operatorId 操作人ID
     * @param orgId      组织ID
     * @param projectId  项目ID
     */
    void init(
        Long operatorId,
        Long orgId,
        Long projectId
    );

    void initScheduleId(Long orgId, Long projectId, Long scheduleId);
}
