package com.ose.tasks.domain.model.service;

import com.ose.tasks.domain.model.repository.RatedTimeImportRecordRepository;
import com.ose.tasks.entity.RatedTimeImportRecord;
import org.springframework.beans.factory.annotation.Autowired;

public class RatedTimeImportRecordService implements RatedTimeImportRecordInterface {

    private final RatedTimeImportRecordRepository ratedTimeImportRecordRepository;

    @Autowired
    public RatedTimeImportRecordService(
        RatedTimeImportRecordRepository ratedTimeImportRecordRepository
    ) {
        this.ratedTimeImportRecordRepository = ratedTimeImportRecordRepository;
    }

    /**
     * 获取定额工时详情。
     *
     * @param orgId                   组织ID
     * @param projectId               项目ID
     * @param ratedTimeImportRecordId 定额工时ID
     * @return 定额工时详情
     */
    @Override
    public RatedTimeImportRecord get(Long orgId, Long projectId, Long ratedTimeImportRecordId) {

        return ratedTimeImportRecordRepository.findByIdAndDeletedIsFalse(ratedTimeImportRecordId);
    }
}
