package com.ose.report.domain.service;

import com.ose.report.dto.statistics.ArchiveDataDateRangeDTO;
import com.ose.report.dto.statistics.ArchiveDataGroupDTO;
import com.ose.report.dto.statistics.ArchiveTimeDTO;
import com.ose.report.entity.statistics.ArchiveDataBase;
import com.ose.report.vo.ArchiveDataGroupKey;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;

import java.util.Date;
import java.util.List;

/**
 * 统计数据归档服务接口。
 */
public interface ArchiveDataInterface {

    /**
     * 归档统计数据。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据类型
     */
    void archiveData(
        Long projectId,
        ArchiveDataType archiveType
    );

    /**
     * 取得所有归档时间。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据业务类型
     * @return 归档时间列表
     */
    List<ArchiveTimeDTO> archiveDates(
        Long projectId,
        ArchiveDataType archiveType
    );

    /**
     * 取得指定的归档时间，若未指定归档时间则取得最后归档时间。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据类型
     * @param archiveDate 归档时间字符串
     * @return 归档时间
     */
    ArchiveTimeDTO getArchiveDate(
        Long projectId,
        ArchiveDataType archiveType,
        String archiveDate
    );

    /**
     * 取得最后归档时间。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档类型
     * @return 最后归档时间
     */
    Date lastArchiveDateTime(
        final Long projectId,
        final ArchiveDataType archiveType
    );

    /**
     * 取得聚合 KEY 的值的列表。
     *
     * @param projectId    项目 ID
     * @param archiveType  归档类型
     * @param archiveYear  归档年
     * @param archiveMonth 归档月
     * @param archiveDay   归档日
     * @param keyName      聚合 KEY 名称
     * @param groupDTO     分组参数
     * @return 聚合 KEY 的值的列表
     */
    List<String> groupKeys(
        Long projectId,
        ArchiveDataType archiveType,
        Integer archiveYear,
        Integer archiveMonth,
        Integer archiveDay,
        ArchiveDataGroupKey keyName,
        ArchiveDataGroupDTO groupDTO
    );

    /**
     * 取得数据聚合期间列表。
     *
     * @param projectId    项目 ID
     * @param archiveType  归档类型
     * @param scheduleType 期间视图类型
     * @param archiveYear  归档年
     * @param archiveMonth 归档月
     * @param archiveDay   归档日
     * @return 数据聚合期间列表
     */
    List<ArchiveTimeDTO> periods(
        Long projectId,
        ArchiveDataType archiveType,
        ArchiveScheduleType scheduleType,
        Integer archiveYear,
        Integer archiveMonth,
        Integer archiveDay
    );

    /**
     * 取得总和。
     *
     * @param projectId    项目 ID
     * @param archiveType  归档类型
     * @param scheduleType 期间视图类型
     * @param archiveYear  归档年
     * @param archiveMonth 归档月
     * @param archiveDay   归档日
     * @param dateRangeDTO 对象数据期间范围
     * @param groupDTO     分组参数
     * @return 总和
     */
    ArchiveDataBase sum(
        Long projectId,
        ArchiveDataType archiveType,
        ArchiveScheduleType scheduleType,
        Integer archiveYear,
        Integer archiveMonth,
        Integer archiveDay,
        ArchiveDataDateRangeDTO dateRangeDTO,
        ArchiveDataGroupDTO groupDTO
    );

    /**
     * 取得数据聚合期间聚合数据列表。
     *
     * @param projectId    项目 ID
     * @param archiveType  归档类型
     * @param scheduleType 期间视图类型
     * @param groupKeys    聚合字段列表
     * @param fetchLast    仅取得最后一天统计值的字段列表
     * @param archiveYear  归档年
     * @param archiveMonth 归档月
     * @param archiveDay   归档日
     * @param dateRangeDTO 对象数据期间范围
     * @param groupDTO     聚合字段条件
     * @return 数据聚合期间列表
     */
    List<ArchiveDataBase> periodData(
        Long projectId,
        ArchiveDataType archiveType,
        ArchiveScheduleType scheduleType,
        List<String> groupKeys,
        List<String> fetchLast,
        Integer archiveYear,
        Integer archiveMonth,
        Integer archiveDay,
        ArchiveDataDateRangeDTO dateRangeDTO,
        ArchiveDataGroupDTO groupDTO
    );

    /**
     * 对每个项目的工时进行统计。
     */
    void manHourStatistics();

    /**
     * 对每个项目的工时进行统计。
     */
    void divisionStatistics();

    void manHourAndPowerStatistics();
}
