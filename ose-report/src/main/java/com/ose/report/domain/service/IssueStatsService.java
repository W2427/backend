package com.ose.report.domain.service;

import com.ose.report.domain.repository.statistics.IssueStatsRepository;
import com.ose.report.dto.statistics.ArchiveDataDateRangeDTO;
import com.ose.report.dto.statistics.ArchiveDataGroupDTO;
import com.ose.report.dto.statistics.ArchiveTimeDTO;
import com.ose.report.entity.statistics.ArchiveDataBase;
import com.ose.report.entity.statistics.WBSIssuesArchiveData;
import com.ose.report.vo.ArchiveDataGroupKey;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.util.BeanUtils;
import com.ose.util.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 遗留问题统计数据服务接口。
 */
@Component
public class IssueStatsService implements IssueStatsInterface {

    private final IssueStatsRepository issueStatsRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public IssueStatsService(
        final IssueStatsRepository issueStatsRepository
    ) {
        this.issueStatsRepository = issueStatsRepository;
    }

    /**
     * 取得数据聚合期间列表。
     *
     * @param projectId    项目 ID
     * @param scheduleType 期间视图类型
     * @return 数据聚合期间列表
     */
    @Override
    public List<ArchiveTimeDTO> periods(
        final Long projectId,
        final ArchiveScheduleType scheduleType
    ) {
        List<ArchiveTimeDTO> result = new ArrayList<>();

        if (scheduleType == ArchiveScheduleType.DAILY) {
            issueStatsRepository.dates(projectId).forEach(group -> result.add(new ArchiveTimeDTO(
                NumberUtils.toInteger(group.get("year")),
                NumberUtils.toInteger(group.get("month")),
                NumberUtils.toInteger(group.get("dayOfMonth")),
                (Integer) group.get("week")
            )));
        } else if (scheduleType == ArchiveScheduleType.WEEKLY) {
            issueStatsRepository.weeks(projectId).forEach(group -> result.add(
                new ArchiveTimeDTO(0, 0, 0, (Integer) group.get("week"))
            ));
        }

        return result;
    }

    /**
     * 取得聚合 KEY 的值的列表。
     *
     * @param projectId 项目 ID
     * @param keyName   聚合 KEY 名称
     * @param groupDTO  分组参数
     * @return 聚合 KEY 的值的列表
     */
    @Override
    public List<String> groupKeys(
        final Long projectId,
        final ArchiveDataGroupKey keyName,
        final ArchiveDataGroupDTO groupDTO
    ) {
        switch (keyName) {
            case ISSUE_LEVEL:
                return issueStatsRepository.levels(projectId);
            case AREA:
                return issueStatsRepository.areas(projectId);
            case PRESSURE_TEST_PACKAGE:
                return issueStatsRepository.pressureTestPackages(projectId);
            case SUB_SYSTEM:
                return issueStatsRepository.subSystems(projectId);
            case DEPARTMENT_ID:
                return issueStatsRepository.departments(projectId);
            default:
                return new ArrayList<>();
        }
    }

    /**
     * 取得遗留问题归档数据。
     *
     * @param projectId    项目 ID
     * @param scheduleType 期间视图类型
     * @param groupKeys    聚合字段列表
     * @param dateRangeDTO 对象数据期间范围
     * @param groupDTO     聚合字段条件
     * @return 统计数据
     */
    @Override
    public List<ArchiveDataBase> periodData(
        final Long projectId,
        final ArchiveScheduleType scheduleType,
        final List<String> groupKeys,
        final ArchiveDataDateRangeDTO dateRangeDTO,
        final ArchiveDataGroupDTO groupDTO
    ) {
        List<ArchiveDataBase> result = new ArrayList<>();

        issueStatsRepository
            .periodData(projectId, scheduleType, groupKeys, dateRangeDTO, groupDTO)
            .forEach(period -> result.add(BeanUtils.clone(period, WBSIssuesArchiveData.class)));

        return result;
    }
}
