package com.ose.tasks.domain.model.service.archivedata;

import java.io.File;

import com.ose.dto.OperatorDTO;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.tasks.dto.archivedata.ArchiveDataCriteriaDTO;

/**
 * 服务接口。
 */
public interface ArchiveDataDetailInterface {

    /**
     * 导出归档数据明细
     *
     * @param operatorDTO
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param archiveType
     * @param scheduleType
     * @param criteriaDTO
     * @return
     */
    File downloadDataDetailsFile(
        OperatorDTO operatorDTO,
        Long orgId,
        Long projectId,
        ArchiveDataType archiveType,
        ArchiveScheduleType scheduleType,
        ArchiveDataCriteriaDTO criteriaDTO
    );

}
