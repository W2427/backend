package com.ose.report.domain.service;

import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.report.domain.repository.statistics.ArchiveDataRepository;
import com.ose.report.dto.statistics.ArchiveDataDateRangeDTO;
import com.ose.report.dto.statistics.ArchiveDataGroupDTO;
import com.ose.report.dto.statistics.ArchiveTimeDTO;
import com.ose.report.entity.statistics.ArchiveData;
import com.ose.report.entity.statistics.ArchiveDataBase;
import com.ose.report.vo.ArchiveDataGroupKey;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.util.BeanUtils;
import com.ose.util.DateUtils;
import com.ose.util.DingTalkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 统计数据归档服务。
 */
@Component
@EnableScheduling
public class ArchiveDataService implements ArchiveDataInterface {

    // 归档数据记录数据仓库
    private final ArchiveDataRepository archiveDataRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public ArchiveDataService(
        ArchiveDataRepository archiveDataRepository
    ) {
        this.archiveDataRepository = archiveDataRepository;
    }

    /**
     * 归档统计数据。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据类型
     */
    public void archiveData(
        final Long projectId,
        final ArchiveDataType archiveType
    ) {
        archiveDataRepository.archiveData(projectId, archiveType);
    }

    /**
     * 取得所有归档时间。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据业务类型
     * @return 归档时间列表
     */
    @Override
    public List<ArchiveTimeDTO> archiveDates(
        final Long projectId,
        final ArchiveDataType archiveType
    ) {
        List<ArchiveTimeDTO> result = new ArrayList<>();

        if (projectId == 0L) {
            archiveDataRepository
                .archiveDatesNoProject(archiveType.name())
                .forEach(archiveTime -> {
                    ArchiveTimeDTO archiveTimeDTO = new ArchiveTimeDTO();
                    archiveTimeDTO.setYear((Number) archiveTime.get("archiveYear"));
                    archiveTimeDTO.setMonth((Number) archiveTime.get("archiveMonth"));
                    archiveTimeDTO.setDay((Number) archiveTime.get("archiveDay"));
                    archiveTimeDTO.setWeek((Number) archiveTime.get("archiveWeek"));
                    result.add(archiveTimeDTO);
                });
        } else {
            archiveDataRepository
                .archiveDates(projectId, archiveType.name())
                .forEach(archiveTime -> {
                    ArchiveTimeDTO archiveTimeDTO = new ArchiveTimeDTO();
                    archiveTimeDTO.setYear((Number) archiveTime.get("archiveYear"));
                    archiveTimeDTO.setMonth((Number) archiveTime.get("archiveMonth"));
                    archiveTimeDTO.setDay((Number) archiveTime.get("archiveDay"));
                    archiveTimeDTO.setWeek((Number) archiveTime.get("archiveWeek"));
                    result.add(archiveTimeDTO);
                });
        }



        return result;
    }

    /**
     * 取得指定的归档时间，若未指定归档时间则取得最后归档时间。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据类型
     * @param archiveDate 归档时间字符串
     * @return 归档时间
     */
    @Override
    public ArchiveTimeDTO getArchiveDate(
        final Long projectId,
        final ArchiveDataType archiveType,
        final String archiveDate
    ) {
        // 指定归档日期时检查指定的归档日期是否执行过归档处理
        if (archiveDate != null) {

            Calendar calendar = DateUtils.toDate(archiveDate);

            if (calendar == null) {
                throw new ValidationError("归档日期格式不正确");
            }

            if (!archiveDataRepository
                .existsByProjectIdAndArchiveTypeAndArchiveYearAndArchiveMonthAndArchiveDay(
                    projectId,
                    archiveType,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                ) {
                throw new NotFoundError("指定的归档日期无归档数据");
            }

            return new ArchiveTimeDTO(calendar);
        }

        // 否则，取得最后的归档记录
        ArchiveData archiveData = archiveDataRepository
            .findFirstByProjectIdAndArchiveTypeOrderByArchiveYearDescArchiveMonthDescArchiveDayDesc(
                projectId,
                archiveType
            );

        if (archiveData == null) {
            throw new NotFoundError("无此类型数据的归档记录");
        }

        return new ArchiveTimeDTO(archiveData);
    }

    /**
     * 取得最后归档时间。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档类型
     * @return 最后归档时间
     */
    public Date lastArchiveDateTime(
        final Long projectId,
        final ArchiveDataType archiveType
    ) {
        return archiveDataRepository.lastArchiveDateTime(projectId, archiveType.name());
    }

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
    @Override
    public List<String> groupKeys(
        final Long projectId,
        final ArchiveDataType archiveType,
        final Integer archiveYear,
        final Integer archiveMonth,
        final Integer archiveDay,
        final ArchiveDataGroupKey keyName,
        final ArchiveDataGroupDTO groupDTO
    ) {
        return archiveDataRepository.groupKeys(
            projectId,
            archiveType,
            archiveYear, archiveMonth, archiveDay,
            keyName,
            groupDTO
        );
    }

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
    @Override
    public List<ArchiveTimeDTO> periods(
        final Long projectId,
        final ArchiveDataType archiveType,
        final ArchiveScheduleType scheduleType,
        final Integer archiveYear,
        final Integer archiveMonth,
        final Integer archiveDay
    ) {
        return archiveDataRepository.periods(
            projectId,
            archiveType, scheduleType,
            archiveYear, archiveMonth, archiveDay
        );
    }

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
    @Override
    public ArchiveDataBase sum(
        final Long projectId,
        final ArchiveDataType archiveType,
        final ArchiveScheduleType scheduleType,
        final Integer archiveYear,
        final Integer archiveMonth,
        final Integer archiveDay,
        final ArchiveDataDateRangeDTO dateRangeDTO,
        final ArchiveDataGroupDTO groupDTO
    ) {
        ArchiveData archiveData = archiveDataRepository.sum(
            projectId,
            archiveType, scheduleType,
            archiveYear, archiveMonth, archiveDay,
            dateRangeDTO, groupDTO
        );

        if (archiveData == null) {
            return null;
        }

        return BeanUtils.clone(archiveData, archiveType.getType());
    }

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
    @Override
    public List<ArchiveDataBase> periodData(
        final Long projectId,
        final ArchiveDataType archiveType,
        final ArchiveScheduleType scheduleType,
        final List<String> groupKeys,
        final List<String> fetchLast,
        final Integer archiveYear,
        final Integer archiveMonth,
        final Integer archiveDay,
        final ArchiveDataDateRangeDTO dateRangeDTO,
        final ArchiveDataGroupDTO groupDTO
    ) {
        List<ArchiveDataBase> result = new ArrayList<>();

        archiveDataRepository
            .periodData(
                projectId,
                archiveType, scheduleType, groupKeys, fetchLast,
                archiveYear, archiveMonth, archiveDay,
                dateRangeDTO, groupDTO
            )
            .forEach(period -> result.add(BeanUtils.clone(period, archiveType.getType())));

        result.sort((a, b) -> {
            if (a.getGroupYear() != null && !a.getGroupYear().equals(b.getGroupYear())) {
                return a.getGroupYear() - b.getGroupYear();
            }
            if (a.getGroupMonth() != null && !a.getGroupMonth().equals(b.getGroupMonth())) {
                return a.getGroupMonth() - b.getGroupMonth();
            }
            if (a.getGroupWeek() != null && !a.getGroupWeek().equals(b.getGroupWeek())) {
                return a.getGroupWeek() - b.getGroupWeek();
            }
            if (a.getGroupDay() != null && !a.getGroupDay().equals(b.getGroupDay())) {
                return a.getGroupDay() - b.getGroupDay();
            }
            return 0;
        });

        return result;
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void manHourStatistics() {

        List<ArchiveData> archiveDatas = archiveDataRepository.findByArchiveType(ArchiveDataType.MAN_HOUR_STATISTICS);
        if (archiveDatas.size() > 0) {
            archiveDataRepository.deleteAll(archiveDatas);
        }

        List<Map<String, Object>> archiveDataList = archiveDataRepository.archiveManHourStatisticsDates();
        for (Map<String, Object> data : archiveDataList) {

            ArchiveData archiveData = new ArchiveData();

            archiveData.setArchiveDay(24);
            archiveData.setArchiveWeek(202152);
            archiveData.setArchiveMonth(12);
            archiveData.setArchiveYear(2021);

            archiveData.setGroupDay(18);
            archiveData.setGroupWeek(202316);
            archiveData.setGroupMonth(4);
            archiveData.setGroupYear(2023);
            archiveData.setGroupDate(20230418);

            archiveData.setWeekly(data.get("weekly") == null ? null : data.get("weekly").toString());
            archiveData.setProjectId(data.get("projectId") == null ? 0L : Long.parseLong(data.get("projectId").toString()));
            archiveData.setProjectName(data.get("projectName") == null ? null : data.get("projectName").toString());

            archiveData.setValue01(data.get("workHour") == null ? 0.0 : Double.valueOf(data.get("workHour").toString()));
            archiveData.setValue02(data.get("outHour") == null ? 0.0 : Double.valueOf(data.get("outHour").toString()));

            archiveData.setArchiveType(ArchiveDataType.MAN_HOUR_STATISTICS);
            archiveData.setScheduleType(ArchiveScheduleType.DAILY);

            archiveDataRepository.save(archiveData);


        }
        return;

    }

    @Override
    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void divisionStatistics() {

        List<ArchiveData> archiveDatas = archiveDataRepository.findByArchiveType(ArchiveDataType.DIVISION_STATISTICS);
        if (archiveDatas.size() > 0) {
            archiveDataRepository.deleteAll(archiveDatas);
        }

        List<Map<String, Object>> archiveDataList = archiveDataRepository.archiveDivisionStatisticsDates();
        for (Map<String, Object> data : archiveDataList) {

            ArchiveData archiveData = new ArchiveData();

            archiveData.setArchiveDay(24);
            archiveData.setArchiveWeek(202152);
            archiveData.setArchiveMonth(12);
            archiveData.setArchiveYear(2021);

            archiveData.setGroupDay(18);
            archiveData.setGroupWeek(202316);
            archiveData.setGroupMonth(4);
            archiveData.setGroupYear(2023);
            archiveData.setGroupDate(20230418);
            archiveData.setWeekly(data.get("weekly") == null ? null : data.get("weekly").toString());
            archiveData.setDivision(data.get("division") == null ? null : data.get("division").toString());
            archiveData.setProjectId(data.get("projectId") == null ? 0L : Long.parseLong(data.get("projectId").toString()));
            archiveData.setProjectName(data.get("projectName") == null ? null : data.get("projectName").toString());

            archiveData.setValue01(data.get("totalHour") == null ? 0.0 : Double.valueOf(data.get("totalHour").toString()));
            archiveData.setValue02(data.get("count") == null ? 0.0 : Double.valueOf(data.get("count").toString()));
            archiveData.setArchiveType(ArchiveDataType.DIVISION_STATISTICS);
            archiveData.setScheduleType(ArchiveScheduleType.DAILY);

            archiveDataRepository.save(archiveData);


        }
        return;

    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?")
//    @Scheduled(cron = "0 0/1 9-19 * * ?")
    public void manHourAndPowerStatistics() {

        List<ArchiveData> archiveDatas = archiveDataRepository.findByArchiveType(ArchiveDataType.MAN_HOUR_POWER_STATISTICS);
        if (archiveDatas.size() > 0) {
            archiveDataRepository.deleteAll(archiveDatas);
        }

        List<Map<String, Object>> archiveDataList = archiveDataRepository.archiveManHourPowerStatisticsDates();
        for (Map<String, Object> data : archiveDataList) {

            ArchiveData archiveData = new ArchiveData();

            archiveData.setArchiveDay(24);
            archiveData.setArchiveWeek(202152);
            archiveData.setArchiveMonth(12);
            archiveData.setArchiveYear(2021);

            archiveData.setGroupDay(18);
            archiveData.setGroupWeek(202316);
            archiveData.setGroupMonth(4);
            archiveData.setGroupYear(2023);
            archiveData.setGroupDate(20230418);

            archiveData.setWeekly(data.get("weekly") == null ? null : data.get("weekly").toString());
            archiveData.setProjectId(data.get("projectId") == null ? 0L : Long.parseLong(data.get("projectId").toString()));
            archiveData.setProjectName(data.get("projectName") == null ? null : data.get("projectName").toString());

            archiveData.setValue01(data.get("totalHour") == null ? 0.0 : Double.valueOf(data.get("totalHour").toString()));
            archiveData.setValue02(data.get("manPower") == null ? 0.0 : Double.valueOf(data.get("manPower").toString()));

            archiveData.setArchiveType(ArchiveDataType.MAN_HOUR_POWER_STATISTICS);
            archiveData.setScheduleType(ArchiveScheduleType.DAILY);

            archiveDataRepository.save(archiveData);
        }
    }




}
