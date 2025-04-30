package com.ose.report.domain.repository.statistics;

import com.ose.report.dto.statistics.ArchiveDataDateRangeDTO;
import com.ose.report.dto.statistics.ArchiveDataGroupDTO;
import com.ose.report.entity.statistics.ArchiveData;
import com.ose.report.vo.ArchiveScheduleType;

import java.util.List;

/**
 * 统计数据归档记录数据仓库。
 */
public interface IssueStatsCustomRepository {

    /**
     * 取得数据聚合期间列表。
     *
     * @param projectId    项目 ID
     * @param scheduleType 期间视图类型
     * @param groupKeys    聚合字段列表
     * @param dateRangeDTO 对象数据期间范围
     * @param groupDTO     分组参数
     * @return 数据聚合期间列表
     */
    List<ArchiveData> periodData(
        Long projectId,
        ArchiveScheduleType scheduleType,
        List<String> groupKeys,
        ArchiveDataDateRangeDTO dateRangeDTO,
        ArchiveDataGroupDTO groupDTO
    );
}
