package com.ose.report.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.report.api.ArchiveDataAPI;
import com.ose.report.domain.service.ArchiveDataInterface;
import com.ose.report.domain.service.IssueStatsInterface;
import com.ose.report.dto.statistics.*;
import com.ose.report.entity.statistics.ArchiveDataBase;
import com.ose.report.vo.ArchiveDataGroupKey;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.util.BeanUtils;
import com.ose.util.DateUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyEditorSupport;
import java.util.*;

@Tag(name = "归档数据查询接口")
@RestController
public class ArchiveDataController extends BaseController implements ArchiveDataAPI {

    private static final Logger logger = LoggerFactory.getLogger(BaseReportController.class);

    // 统计数据归档服务
    private final ArchiveDataInterface archiveDataService;

    // 遗留问题统计数据服务
    private final IssueStatsInterface issueStatsService;

    /**
     * 构造方法。
     */
    @Autowired
    public ArchiveDataController(
        ArchiveDataInterface archiveDataService,
        IssueStatsInterface issueStatsService
    ) {
        this.archiveDataService = archiveDataService;
        this.issueStatsService = issueStatsService;
    }

    /**
     * 重写 ArchiveDataGroupKey 枚举的转换规则。
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ArchiveDataGroupKey.class, new ArchiveDataGroupKeyConverter());
    }

    /**
     * ArchiveDataGroupKey 枚举转换器。
     */
    public static class ArchiveDataGroupKeyConverter extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            setValue(ArchiveDataGroupKey.getByPathVariableName(text));
        }
    }

    @Override
    @Operation(description = "手动归档统计数据")
    @WithPrivilege
    public JsonResponseBody archive(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "归档数据业务类型") ArchiveDataType archiveType
    ) {
        logger.info("ArchiveDataController archive start");
        archiveDataService.archiveData(projectId, archiveType);
        logger.info("ArchiveDataController archive end");
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "取得归档日期列表")
    @WithPrivilege
    public JsonListResponseBody<ArchiveTimeDTO> archiveDates(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "归档数据业务类型") ArchiveDataType archiveType
    ) {
        logger.info("ArchiveDataController archiveDates start");
        switch (archiveType) {
            case WBS_ISSUE_PROGRESS:
                List<ArchiveTimeDTO> dates = new ArrayList<>();
                dates.add(new ArchiveTimeDTO(Calendar.getInstance()));
                logger.info("ArchiveDataController archiveDates end");
                return new JsonListResponseBody<>(dates);
            default:
                logger.info("ArchiveDataController archiveDates end");
                return new JsonListResponseBody<>(archiveDataService.archiveDates(projectId, archiveType));
        }
    }

    @Override
    @Operation(description = "取得指定名称的聚合 KEY 的列表")
    @WithPrivilege
    public JsonObjectResponseBody<ArchiveDataPeriodsDTO> groupKeys(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "归档类型") ArchiveDataType archiveType,
        @PathVariable @Parameter(description = "归档时间（年）") int archiveYear,
        @PathVariable @Parameter(description = "归档时间（月）") int archiveMonth,
        @PathVariable @Parameter(description = "归档时间（日）") int archiveDay,
        @PathVariable @Parameter(description = "聚合 KEY 名称（多个值通过逗号分隔）") ArchiveDataGroupKey[] keyNames,
        @RequestParam @Parameter(description = "期间视图类型") ArchiveScheduleType scheduleType,
        ArchiveDataGroupDTO groupDTO
    ) {
        logger.info("ArchiveDataController groupKeys start");
        List<ArchiveTimeDTO> periods;
        List<ArchiveDataGroupKeyDTO> groupKeys = new ArrayList<>();

        // 遗留问题统计数据时
        if (archiveType == ArchiveDataType.WBS_ISSUE_PROGRESS) {
            periods = issueStatsService.periods(projectId, scheduleType);
            for (ArchiveDataGroupKey keyName : keyNames) {
                if (keyName == null) {
                    continue;
                }
                groupKeys.add(new ArchiveDataGroupKeyDTO(
                    keyName,
                    issueStatsService.groupKeys(
                        projectId, keyName,
                        BeanUtils.copyProperties(groupDTO, new ArchiveDataGroupDTO(), keyName.getPropertyName())
                    )
                ));
            }
            // 通用统计数据时
        } else {
            periods = archiveDataService.periods(
                projectId,
                archiveType, scheduleType,
                archiveYear, archiveMonth, archiveDay
            );
            for (ArchiveDataGroupKey keyName : keyNames) {
                if (keyName == null) {
                    continue;
                }
                groupKeys.add(new ArchiveDataGroupKeyDTO(
                    keyName,
                    archiveDataService.groupKeys(
                        projectId, archiveType, archiveYear, archiveMonth, archiveDay, keyName,
                        BeanUtils.copyProperties(groupDTO, new ArchiveDataGroupDTO(), keyName.getPropertyName())
                    )
                ));
            }
        }
        logger.info("ArchiveDataController groupKeys end");
        return new JsonObjectResponseBody<>(new ArchiveDataPeriodsDTO(periods, groupKeys));
    }

    /**
     * 取得归档统计数据。
     *
     * @param projectId    项目 ID
     * @param archiveType  归档数据类型
     * @param scheduleType 期间视图类型
     * @param groupKeys    聚合字段名称列表
     * @param fetchLast    仅取得最后日期值的字段名称列表
     * @param archiveYear  归档时间（年）
     * @param archiveMonth 归档时间（月）
     * @param archiveDay   归档时间（日）
     * @param dateRangeDTO 统计对象期间
     * @param groupDTO     聚合字段过滤条件
     * @return 统计数据列表
     */
    private JsonObjectResponseBody<ArchiveDataPeriodDataDTO> periodData(
        final Long projectId,
        final ArchiveDataType archiveType,
        final ArchiveScheduleType scheduleType,
        final List<String> groupKeys,
        final List<String> fetchLast,
        final int archiveYear,
        final int archiveMonth,
        final int archiveDay,
        final ArchiveDataDateRangeDTO dateRangeDTO,
        final ArchiveDataGroupDTO groupDTO
    ) {
        logger.info("ArchiveDataController private periodData start");
        // 非归档统计数据
        switch (archiveType) {
            // 遗留问题统计
            case WBS_ISSUE_PROGRESS:
                return new JsonObjectResponseBody<>(
                    new ArchiveDataPeriodDataDTO(
                        null, null,
                        issueStatsService.periodData(projectId, scheduleType, groupKeys, dateRangeDTO, groupDTO),
                        null
                    )
                );
            default:
        }

        // 取得总和
        ArchiveDataBase sum = archiveDataService.sum(
            projectId,
            archiveType, scheduleType,
            archiveYear, archiveMonth, archiveDay,
            null, groupDTO
        );

        if (sum == null) {
            return new JsonObjectResponseBody<>(new ArchiveDataPeriodDataDTO());
        }

        ArchiveDataBase accumulation = null;

        // 取得选取期间范围之前的累计值
        if (dateRangeDTO.getDateFromTime() != null) {

            ArchiveDataDateRangeDTO previousDateRangeDTO = new ArchiveDataDateRangeDTO();

            previousDateRangeDTO.setDateUntilTime(BeanUtils.clone(
                dateRangeDTO.getDateFromTime(),
                ArchiveTimeDTO.class
            ));

            previousDateRangeDTO.getDateUntilTime().setInclude(false);

            accumulation = archiveDataService.sum(
                projectId,
                archiveType, scheduleType,
                archiveYear, archiveMonth, archiveDay,
                previousDateRangeDTO, groupDTO
            );
        }

        // 取得归档数据
        List<ArchiveDataBase> result = archiveDataService.periodData(
            projectId,
            archiveType, scheduleType, groupKeys, fetchLast,
            archiveYear, archiveMonth, archiveDay,
            dateRangeDTO, groupDTO
        );

        // 补全周次期间
        if (groupKeys == null && scheduleType == ArchiveScheduleType.WEEKLY && result.size() > 0) {

            HashMap<Integer, ArchiveDataBase> weekData = new HashMap<>();

            result.forEach(archiveData -> weekData.put(
                archiveData.getGroupYear() * 100 + archiveData.getGroupWeek(),
                archiveData
            ));

            result.clear();

            Set<Integer> weeks = weekData.keySet();
            int weekFrom = Collections.min(weeks);
            int weekUntil = Collections.max(weeks);

            for (
                int nextWeek = weekFrom;
                nextWeek <= weekUntil;
                nextWeek = DateUtils.getNextYearWeek(nextWeek)
                ) {
                if (weekData.containsKey(nextWeek)) {
                    ArchiveDataBase archiveData = weekData.get(nextWeek);
                    archiveData.setGroupMonth(null);
                    archiveData.setGroupDay(null);
                    result.add(archiveData);
                } else {
                    ArchiveDataBase archiveData = new ArchiveDataBase();
                    archiveData.setGroupYear(nextWeek / 100);
                    archiveData.setGroupWeek(nextWeek % 100);
                    result.add(archiveData);
                }
            }
        }
        logger.info("ArchiveDataController private periodData end");
        return new JsonObjectResponseBody<>(
            new ArchiveDataPeriodDataDTO(
                sum, accumulation, result,
                archiveDataService.lastArchiveDateTime(projectId, archiveType)
            )
        );
    }

    @Override
    @Operation(
        summary = "取得归档数据",
        description = "将根据归档数据的类型返回相应的聚合值，返回的值及值的字段名称参照<a href=\"/specifications/statistics-archive-data.xlsx\">统计数据说明文件</a>。"
    )
    @WithPrivilege
    public JsonObjectResponseBody<ArchiveDataPeriodDataDTO> periodData(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "归档类型") ArchiveDataType archiveType,
        @PathVariable @Parameter(description = "归档时间（年）") int archiveYear,
        @PathVariable @Parameter(description = "归档时间（月）") int archiveMonth,
        @PathVariable @Parameter(description = "归档时间（日）") int archiveDay,
        @RequestParam @Parameter(description = "期间视图类型") ArchiveScheduleType scheduleType,
        @RequestParam(required = false) @Parameter(description = "取得最后日期统计值的字段名的列表") List<String> fetchLast,
        ArchiveDataDateRangeDTO dateRangeDTO,
        ArchiveDataGroupDTO groupDTO
    ) {
        logger.info("ArchiveDataController periodData start");
        return periodData(
            projectId,
            archiveType, scheduleType, null, fetchLast,
            archiveYear, archiveMonth, archiveDay,
            dateRangeDTO, groupDTO
        );
    }

    @Override
    @Operation(description = "取得归档数据报告")
    @WithPrivilege
    public JsonObjectResponseBody<ArchiveDataPeriodDataDTO> archiveSummary(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "归档类型") ArchiveDataType archiveType,
        @RequestParam @Parameter(description = "期间视图类型") ArchiveScheduleType scheduleType,
        @RequestParam(name = "groupKey", required = false) @Parameter(description = "聚合 KEY 列表") List<String> groupKeys,
        @RequestParam(required = false) @Parameter(description = "取得最后日期统计值的字段名的列表") List<String> fetchLast,
        @RequestParam(required = false) @Parameter(description = "归档日期（yyyyMMdd）") String archiveDate,
        ArchiveDataDateRangeDTO dateRangeDTO
    ) {
        logger.info("ArchiveDataController archiveSummary start");

        ArchiveTimeDTO archiveTime;

        switch (archiveType) {
            // 遗留问题统计
            case WBS_ISSUE_PROGRESS:
                archiveTime = new ArchiveTimeDTO(DateUtils.toDate(archiveDate));
                break;
            // 归档数据时，检查指定的归档时间是否存在归档，未指定归档时间时使用最后的归档时间
            default:
                archiveTime = archiveDataService.getArchiveDate(projectId, archiveType, archiveDate);
        }

        // 设置默认起止时间
        if (dateRangeDTO.getDateFrom() == null && dateRangeDTO.getDateUntil() == null) {
            Calendar calendar = Calendar.getInstance();
            switch (scheduleType) {
                case DAILY:
                    dateRangeDTO.setDateFrom(String.format(
                        "%d-%02d-%02d",
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH)
                    ));
                    break;
                case WEEKLY:
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    dateRangeDTO.setDateFrom(String.format(
                        "%d/%02d",
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.WEEK_OF_YEAR)
                    ));
                    break;
                case MONTHLY:
                    dateRangeDTO.setDateFrom(String.format(
                        "%d-%02d",
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1
                    ));
                    break;
                case ANNUALLY:
                    dateRangeDTO.setDateFrom("" + calendar.get(Calendar.YEAR));
                    break;
                default:
            }
            dateRangeDTO.setDateUntil(dateRangeDTO.getDateFrom());
        }

        groupKeys = groupKeys == null ? new ArrayList<>() : groupKeys;

        logger.info("ArchiveDataController archiveSummary end");
        return periodData(
            projectId,
            archiveType, scheduleType, groupKeys, fetchLast,
            archiveTime.getYear(), archiveTime.getMonth(), archiveTime.getDay(),
            dateRangeDTO, null
        );
    }
}
