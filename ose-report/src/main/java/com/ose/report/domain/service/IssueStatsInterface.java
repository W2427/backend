package com.ose.report.domain.service;

import com.ose.report.dto.statistics.ArchiveDataDateRangeDTO;
import com.ose.report.dto.statistics.ArchiveDataGroupDTO;
import com.ose.report.dto.statistics.ArchiveTimeDTO;
import com.ose.report.entity.statistics.ArchiveDataBase;
import com.ose.report.vo.ArchiveDataGroupKey;
import com.ose.report.vo.ArchiveScheduleType;

import java.util.List;

/**
 * 遗留问题统计数据服务接口。
 */
public interface IssueStatsInterface {

    /**
     * 取得数据聚合期间列表。
     *
     * @param projectId    项目 ID
     * @param scheduleType 期间视图类型
     * @return 数据聚合期间列表
     */
    List<ArchiveTimeDTO> periods(
        Long projectId,
        ArchiveScheduleType scheduleType
    );

    /**
     * 取得聚合 KEY 的值的列表。
     *
     * @param projectId 项目 ID
     * @param keyName   聚合 KEY 名称
     * @param groupDTO  分组参数
     * @return 聚合 KEY 的值的列表
     */
    List<String> groupKeys(
        Long projectId,
        ArchiveDataGroupKey keyName,
        ArchiveDataGroupDTO groupDTO
    );

    /**
     * 取得数据聚合期间聚合数据列表。
     *
     * @param projectId    项目 ID
     * @param scheduleType 期间视图类型
     * @param groupKeys    聚合字段列表
     * @param dateRangeDTO 对象数据期间范围
     * @param groupDTO     聚合字段条件
     * @return 数据聚合期间列表
     */
    List<ArchiveDataBase> periodData(
        Long projectId,
        ArchiveScheduleType scheduleType,
        List<String> groupKeys,
        ArchiveDataDateRangeDTO dateRangeDTO,
        ArchiveDataGroupDTO groupDTO
    );
}
