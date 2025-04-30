package com.ose.tasks.domain.model.service;

import com.ose.tasks.entity.RatedTimeImportRecord;

public interface RatedTimeImportRecordInterface {
    /**
     * 获取定额工时详情。
     *
     * @param orgId                   组织ID
     * @param projectId               项目ID
     * @param ratedTimeImportRecordId 定额工时导入报告ID
     * @return 定额工时详情
     */
    RatedTimeImportRecord get(Long orgId, Long projectId, Long ratedTimeImportRecordId);
}
