package com.ose.report.domain.repository.statistics;

import com.ose.exception.ValidationError;
import com.ose.report.dto.statistics.ArchiveDataDateRangeDTO;
import com.ose.report.dto.statistics.ArchiveDataGroupDTO;
import com.ose.report.dto.statistics.ArchiveTimeDTO;
import com.ose.report.entity.statistics.ArchiveData;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.repository.BaseRepository;
import org.hibernate.transform.Transformers;

import jakarta.persistence.Query;
import java.util.*;

/**
 * 统计数据归档记录数据仓库。
 */
public class IssueStatsCustomRepositoryImpl extends BaseRepository implements IssueStatsCustomRepository {

    /**
     * 取得遗留问题日次统计数据。
     *
     * @param joinCondition 连接条件
     * @param where         查询条件
     * @param parameters    参数表
     * @return 遗留问题统计数据
     */
    @SuppressWarnings("unchecked")
    private List<ArchiveData> dailyData(
        final String joinCondition,
        final String where,
        final Map<String, Object> parameters
    ) {
        Query query = getEntityManager()
            .createNativeQuery("SELECT"
                + "   FLOOR(date / 10000)           AS groupYear,"
                + "   FLOOR(MOD(date, 10000) / 100) AS groupMonth,"
                + "   MOD(date, 100)                AS groupDay,"
                + "   value01," // 一周内未处理问题合计
                + "   value02," // 一周以上四周以内未处理问题合计
                + "   value03," // 四周以上未处理问题合计
                + "   value04"  // 已关闭问题合计
                + " FROM"
                + "   ("
                + "     SELECT"
                + "       w1.date,"
                + "       w1.count  AS value01,"
                + "       w14.count AS value02,"
                + "       w4.count  AS value03,"
                + "       c.count   AS value04"
                + "     FROM"
                + "       (" // 一周内未处理问题合计
                + "         SELECT"
                + "           d.date,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_dates AS d"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= d.time_to))"
                + "             AND i.create_timestamp >= (d.time_from - 518400)" // 518400 = (7 - 1) * 24 * 60 * 60
                + "             AND i.create_timestamp < d.time_to"
                + "         GROUP BY"
                + "           d.date"
                + "       ) AS w1"
                + "       INNER JOIN (" // 一周以上四周以内未处理问题合计
                + "         SELECT"
                + "           d.date,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_dates AS d"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= d.time_to))"
                + "             AND i.create_timestamp >= (d.time_from - 2332800)" // 2332800 = (4 * 7 - 1) * 24 * 60 * 60
                + "             AND i.create_timestamp < (d.time_from - 518400)"
                + "         GROUP BY"
                + "           d.date"
                + "       ) AS w14"
                + "         ON w14.date = w1.date"
                + "       INNER JOIN (" // 四周以上未处理问题合计
                + "         SELECT"
                + "           d.date,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_dates AS d"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= d.time_to))"
                + "             AND i.create_timestamp < (d.time_from - 2332800)"
                + "         GROUP BY"
                + "           d.date"
                + "       ) AS w4"
                + "         ON w4.date = w1.date"
                + "       INNER JOIN (" // 已关闭问题合计
                + "         SELECT"
                + "           d.date,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_dates AS d"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND i.status = 'CLOSED'"
                + "             AND i.close_year = d.year"
                + "             AND i.close_month = d.month"
                + "             AND i.close_day = d.day"
                + "         GROUP BY"
                + "           d.date"
                + "       ) AS c"
                + "         ON c.date = w1.date"
                + "   ) AS i"
                + where // WHERE i.date >= :dateFrom AND i.date <= :dateUntil
            )
            .unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        parameters.keySet().forEach(key -> query.setParameter(key, parameters.get(key)));

        List<ArchiveData> result = new ArrayList<>();

        query.getResultList().forEach(record ->
            result.add(new ArchiveData((Map<String, Object>) record))
        );

        return result;
    }

    /**
     * 取得遗留问题周次统计数据。
     *
     * @param joinCondition 连接条件
     * @param where         查询条件
     * @param parameters    参数表
     * @return 遗留问题统计数据
     */
    @SuppressWarnings("unchecked")
    private List<ArchiveData> weeklyData(
        final String joinCondition,
        final String where,
        final Map<String, Object> parameters
    ) {
        Query query = getEntityManager()
            .createNativeQuery("SELECT"
                + "  FLOOR(week / 100) AS groupYear,"
                + "  MOD(week, 100) AS groupWeek,"
                + "  value01,"
                + "  value02,"
                + "  value03,"
                + "  value04"
                + " FROM"
                + "   ("
                + "     SELECT"
                + "       w1.week,"
                + "       w1.count  AS value01," // 一周内未处理问题合计
                + "       w14.count AS value02," // 一周以上四周以内未处理问题合计
                + "       w4.count  AS value03," // 四周以上未处理问题合计
                + "       c.count   AS value04"  // 已关闭问题合计
                + "     FROM"
                + "       (" // 一周内未处理问题合计
                + "         SELECT"
                + "           w.week,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_weeks AS w"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= w.time_to))"
                + "             AND i.create_timestamp >= w.time_from"
                + "             AND i.create_timestamp < w.time_to"
                + "         GROUP BY"
                + "           w.week"
                + "       ) AS w1"
                + "       INNER JOIN (" // 一周以上四周以内未处理问题合计
                + "         SELECT"
                + "           w.week,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_weeks AS w"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= w.time_to))"
                + "             AND i.create_timestamp >= (w.time_from - 1814400)"
                + "             AND i.create_timestamp < w.time_from"
                + "         GROUP BY"
                + "           w.week"
                + "       ) AS w14"
                + "         ON w14.week = w1.week"
                + "       INNER JOIN (" // 四周以上未处理问题合计
                + "         SELECT"
                + "           w.week,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_weeks AS w"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= w.time_to))"
                + "             AND i.create_timestamp < (w.time_from - 1814400)"
                + "         GROUP BY"
                + "           w.week"
                + "       ) AS w4"
                + "         ON w4.week = w1.week"
                + "       INNER JOIN (" // 已关闭问题合计
                + "         SELECT"
                + "           w.week,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_weeks AS w"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND i.status = 'CLOSED'"
                + "             AND i.close_week = w.week"
                + "         GROUP BY"
                + "           w.week"
                + "       ) AS c"
                + "         ON c.week = w1.week"
                + "   ) AS i"
                + where // WHERE i.date >= :dateFrom AND i.date <= :dateUntil
            )
            .unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        parameters.keySet().forEach(key -> query.setParameter(key, parameters.get(key)));

        List<ArchiveData> result = new ArrayList<>();

        query.getResultList().forEach(record ->
            result.add(new ArchiveData((Map<String, Object>) record))
        );

        return result;
    }

    /**
     * 日报。
     *
     * @param joinCondition 连接条件
     * @param where         查询条件
     * @param parameters    参数表
     * @return 日报
     */
    @SuppressWarnings("unchecked")
    private List<ArchiveData> dailyReport(
        final String joinCondition,
        final String where,
        final Map<String, Object> parameters
    ) {
        Query query = getEntityManager()
            .createNativeQuery("SELECT"
                + "   FLOOR(date / 10000)           AS groupYear,"
                + "   FLOOR(MOD(date, 10000) / 100) AS groupMonth,"
                + "   MOD(date, 100)                AS groupDay,"
                + "   value05,"
                + "   value06,"
                + "   value07"
                + " FROM"
                + "   ("
                + "     SELECT"
                + "       t.date,"
                + "       t.count AS value05,"
                + "       f.count AS value06,"
                + "       o.count AS value07"
                + "     FROM"
                + "       ("
                + "         SELECT"
                + "           d.date,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_dates AS d"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND i.create_timestamp < d.time_to"
                + "         GROUP BY"
                + "           d.date"
                + "       ) AS t"
                + "       INNER JOIN ("
                + "         SELECT"
                + "           d.date,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_dates AS d"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND i.status = 'CLOSED'"
                + "             AND i.close_timestamp >= d.time_from"
                + "             AND i.close_timestamp < d.time_to"
                + "         GROUP BY"
                + "           d.date"
                + "       ) AS f"
                + "         ON f.date = t.date"
                + "       INNER JOIN ("
                + "         SELECT"
                + "           d.date,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_dates AS d"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= d.time_to))"
                + "             AND i.create_timestamp < d.time_to"
                + "         GROUP BY"
                + "           d.date"
                + "       ) AS o"
                + "         ON o.date = t.date"
                + "   ) AS i"
                + where // WHERE i.date >= :dateFrom AND i.date <= :dateUntil
            )
            .unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        parameters.keySet().forEach(key -> query.setParameter(key, parameters.get(key)));

        List<ArchiveData> result = new ArrayList<>();

        query.getResultList().forEach(record ->
            result.add(new ArchiveData((Map<String, Object>) record))
        );

        return result;
    }

    /**
     * 周报。
     *
     * @param joinCondition 连接条件
     * @param where         查询条件
     * @param parameters    参数表
     * @return 周报
     */
    @SuppressWarnings("unchecked")
    private List<ArchiveData> weeklyReport(
        final String joinCondition,
        final String where,
        final Map<String, Object> parameters
    ) {
        Query query = getEntityManager()
            .createNativeQuery("SELECT"
                + "   FLOOR(week / 100) AS groupYear,"
                + "   MOD(week, 100) AS groupWeek,"
                + "   value05,"
                + "   value06,"
                + "   value07"
                + " FROM"
                + "   ("
                + "     SELECT"
                + "       t.week,"
                + "       t.count AS value05,"
                + "       f.count AS value06,"
                + "       o.count AS value07"
                + "     FROM"
                + "       ("
                + "         SELECT"
                + "           w.week,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_weeks AS w"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND i.create_timestamp < w.time_to"
                + "         GROUP BY"
                + "           w.week"
                + "       ) AS t"
                + "       INNER JOIN ("
                + "         SELECT"
                + "           w.week,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_weeks AS w"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND i.status = 'CLOSED'"
                + "             AND i.close_timestamp >= w.time_from"
                + "             AND i.close_timestamp < w.time_to"
                + "         GROUP BY"
                + "           w.week"
                + "       ) AS f"
                + "         ON f.week = t.week"
                + "       INNER JOIN ("
                + "         SELECT"
                + "           w.week,"
                + "           COUNT(DISTINCT i.id) AS count"
                + "         FROM"
                + "           ose_issues.issue_weeks AS w"
                + "           LEFT OUTER JOIN ose_issues.issue_stats AS i"
                + joinCondition // ON i.project_id = :projectId ...
                + "             AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= w.time_to))"
                + "             AND i.create_timestamp < w.time_to"
                + "         GROUP BY"
                + "           w.week"
                + "       ) AS o"
                + "         ON o.week = t.week"
                + "   ) AS i"
                + where // WHERE i.date >= :dateFrom AND i.date <= :dateUntil
            )
            .unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        parameters.keySet().forEach(key -> query.setParameter(key, parameters.get(key)));

        List<ArchiveData> result = new ArrayList<>();

        query.getResultList().forEach(record ->
            result.add(new ArchiveData((Map<String, Object>) record))
        );

        return result;
    }

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
    @Override
    public List<ArchiveData> periodData(
        final Long projectId,
        final ArchiveScheduleType scheduleType,
        final List<String> groupKeys,
        final ArchiveDataDateRangeDTO dateRangeDTO,
        final ArchiveDataGroupDTO groupDTO
    ) {
        StringBuilder joinConditions = new StringBuilder().append(" ON i.project_id = :projectId");
        StringBuilder whereClause = new StringBuilder();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projectId", projectId);

        // 设置聚合单位查询条件
        ArchiveDataCustomRepositoryImpl
            .setGroupKeyConditions(groupDTO, joinConditions, parameters, "i");

        Calendar calendar = Calendar.getInstance();
        ArchiveTimeDTO dateFrom = dateRangeDTO.getDateFromTime();
        ArchiveTimeDTO dateUntil = dateRangeDTO.getDateUntilTime();

        switch (scheduleType) {
            // 日次数据时
            case DAILY:
                if (dateUntil == null) {
                    dateUntil = new ArchiveTimeDTO(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH),
                        0,
                        ArchiveTimeDTO.FROM
                    );
                }
                if (dateFrom == null) {
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
                    dateFrom = new ArchiveTimeDTO(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.DAY_OF_MONTH),
                        0,
                        ArchiveTimeDTO.UNTIL
                    );
                }
                whereClause
                    .append(" WHERE")
                    .append(String.format(
                        " i.date >= %d%02d%02d",
                        dateFrom.getYear(), dateFrom.getMonth(), dateFrom.getDay()
                    ))
                    .append(String.format(
                        " AND i.date <= %d%02d%02d",
                        dateUntil.getYear(), dateUntil.getMonth(), dateUntil.getDay()
                    ));
                break;
            // 周次数据时
            case WEEKLY:
                int year;
                if (dateUntil == null) {
                    year = calendar.get(Calendar.YEAR);
                    if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER
                        && calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
                        year += 1;
                    }
                    dateUntil = new ArchiveTimeDTO(
                        year, 0, 0,
                        calendar.get(Calendar.WEEK_OF_YEAR) + 1,
                        ArchiveTimeDTO.FROM
                    );
                }
                if (dateFrom == null) {
                    calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
                    year = calendar.get(Calendar.YEAR);
                    if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER
                        && calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
                        year += 1;
                    }
                    dateFrom = new ArchiveTimeDTO(
                        year, 0, 0,
                        calendar.get(Calendar.WEEK_OF_YEAR) + 1,
                        ArchiveTimeDTO.UNTIL
                    );
                }
                whereClause
                    .append(" WHERE")
                    .append(" i.week >= ").append(dateFrom.toYearWeek())
                    .append(" AND i.week <= ").append(dateUntil.toYearWeek());
                break;
            // 返回期间视图类型无效错误
            default:
                throw new ValidationError("遗留问题期间视图类型无效");
        }

        // 数据统计
        if (groupKeys == null) {
            switch (scheduleType) {
                // 日次数据时
                case DAILY:
                    return dailyData(
                        joinConditions.toString(),
                        whereClause.toString(),
                        parameters
                    );
                // 周次数据时
                case WEEKLY:
                    return weeklyData(
                        joinConditions.toString(),
                        whereClause.toString(),
                        parameters
                    );
                default:
                    return new ArrayList<>();
            }
            // 数据报告
        } else {
            switch (scheduleType) {
                // 日次数据时
                case DAILY:
                    return dailyReport(
                        joinConditions.toString(),
                        whereClause.toString(),
                        parameters
                    );
                // 周次数据时
                case WEEKLY:
                    return weeklyReport(
                        joinConditions.toString(),
                        whereClause.toString(),
                        parameters
                    );
                default:
                    return new ArrayList<>();
            }
        }
    }
}
