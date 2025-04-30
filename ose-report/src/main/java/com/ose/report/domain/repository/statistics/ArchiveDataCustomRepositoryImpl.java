package com.ose.report.domain.repository.statistics;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.report.dto.statistics.ArchiveDataDateRangeDTO;
import com.ose.report.dto.statistics.ArchiveDataGroupDTO;
import com.ose.report.dto.statistics.ArchiveTimeDTO;
import com.ose.report.entity.statistics.ArchiveData;
import com.ose.report.vo.ArchiveDataGroupKey;
import com.ose.report.vo.ArchiveDataType;
import com.ose.report.vo.ArchiveScheduleType;
import com.ose.repository.BaseRepository;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 统计数据归档记录数据仓库。
 */
public class ArchiveDataCustomRepositoryImpl extends BaseRepository implements ArchiveDataCustomRepository {

    // 聚合单位列表
    private static List<Field> GROUP_KEYS = new ArrayList<>();

    // 归档数据类型的属性名与字段名映射表缓存
    private static Map<String, Map<String, String[]>> FIELD_PROPERTY_NAMES = new HashMap<>();

    // 设置聚合单位列表
    static {
        for (Field groupKey : ArchiveDataGroupDTO.class.getDeclaredFields()) {
            int modifiers = groupKey.getModifiers();
            if (!Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers)) {
                continue;
            }
            groupKey.setAccessible(true);
            GROUP_KEYS.add(groupKey);
        }
    }

    /**
     * 将聚合字段属性名列表转为聚合字段信息（枚举）列表。
     *
     * @param groupKeyNames 聚合字段属性名列表
     * @return 聚合字段信息列表
     */
    private static List<ArchiveDataGroupKey> toArchiveDataGroupKeyList(List<String> groupKeyNames) {

        if (groupKeyNames == null) {
            return null;
        }

        List<ArchiveDataGroupKey> groupKeys = new ArrayList<>();

        groupKeyNames.forEach(groupKeyName -> {
            ArchiveDataGroupKey groupKey = ArchiveDataGroupKey.getByPropertyName(groupKeyName);
            if (groupKey != null) {
                groupKeys.add(groupKey);
            }
        });

        if (groupKeys.size() == 0) {
            return null;
        }

        return groupKeys;
    }

    /**
     * 取得指定归档数据类型的聚合字段属性名与数据库字段名的映射表。
     *
     * @param type     归档数据类型
     * @param fieldMap 属性名与字段名的映射表
     */
    private static Map<String, String[]> getFieldPropertyNames(Class type, Map<String, String[]> fieldMap) {

        if (type == null) {
            return fieldMap;
        }

        getFieldPropertyNames(type.getSuperclass(), fieldMap);

        Method[] methods = type.getDeclaredMethods();
        String fieldName;
        String propertyName;
        JsonProperty annotation;

        for (Method method : methods) {

            if (!method.getName().matches("^getValue\\d{2}$")) {
                continue;
            }

            fieldName = method.getName().replaceAll("^get(Value)(\\d{2})", "$1_$2").toLowerCase();
            propertyName = fieldName.replace("_", "");
            annotation = method.getAnnotation(JsonProperty.class);

            fieldMap.put(
                fieldName,
                new String[]{
                    propertyName,
                    annotation == null ? propertyName : annotation.value()
                }
            );
        }

        return fieldMap;
    }

    /**
     * 设置值字段聚合方法。
     * 若值字段在 propertiesFetchLast 中，则仅取得聚合区间的最后一天的值，否则计算聚合区间的合计值。
     *
     * @param archiveType         聚合数据类型
     * @param propertiesFetchLast 仅取得最后日期数据的属性名列表
     * @return SELECT 子句
     */
    private static String setGroupOfValues(
        final ArchiveDataType archiveType,
        final List<String> propertiesFetchLast
    ) {
        // 取得归档数据类型的对象属性名与数据库字段名的映射表
        final Map<String, String[]> fieldPropertyNames = FIELD_PROPERTY_NAMES.computeIfAbsent(
            archiveType.getType().getName(),
            key -> getFieldPropertyNames(archiveType.getType(), new HashMap<>())
        );

        List<String> select = new ArrayList<>();

        fieldPropertyNames.keySet().forEach(fieldName -> {
            String[] names = fieldPropertyNames.get(fieldName);
            String propertyName = names[0];
            String alias = names[1];
            select.add(String.format(
                (propertiesFetchLast != null && propertiesFetchLast.contains(alias))
                    ? "SUM(CASE WHEN d.group_date IS NULL THEN 0 ELSE a.%s END) AS %s"
                    : "SUM(a.%s) AS %s",
                fieldName,
                propertyName
            ));
        });

        return String.join(", ", select);
    }

    /**
     * 生成查询 SQL 的 SELECT 子句和 GROUP BY 子句。
     *
     * @param scheduleType 归档月
     * @return 查询 SQL 的 SELECT 子句和 GROUP BY 子句
     */
    private static StringBuilder[] buildSelectAndGroupSQL(
        ArchiveScheduleType scheduleType,
        List<ArchiveDataGroupKey> groupKeys
    ) {
        StringBuilder select = new StringBuilder("SELECT");
        StringBuilder groupBy = new StringBuilder();

        scheduleType = scheduleType == null ? ArchiveScheduleType.DAILY : scheduleType;

        // 设置取得的字段和聚合单位
        switch (scheduleType) {
            case WEEKLY:
                select
                    .append(" FLOOR(a.group_week / 100) AS groupYear,")
                    .append(" MOD(a.group_week, 100)    AS groupWeek");
                groupBy
                    .append(" GROUP BY")
                    .append(" a.group_week");
                break;
            case MONTHLY:
                select
                    .append(" a.group_year  AS groupYear,")
                    .append(" a.group_month AS groupMonth");
                groupBy
                    .append(" GROUP BY")
                    .append(" a.group_year,")
                    .append(" a.group_month");
                break;
            case ANNUALLY:
                select
                    .append(" a.group_year AS groupYear");
                groupBy.append(" GROUP BY").append(" a.group_year");
                break;
            default:
                select
                    .append(" a.group_year  AS groupYear,")
                    .append(" a.group_month AS groupMonth,")
                    .append(" a.group_day   AS groupDay,")
                    .append(" a.weekly   AS weekly,")
                    .append(" a.division   AS division,")
                    .append(" a.project_name   AS projectName");
                groupBy
                    .append(" GROUP BY")
                    .append(" a.group_year,")
                    .append(" a.group_month,")
                    .append(" a.group_day,")
                    .append(" a.weekly,")
                    .append(" a.division,")
                    .append(" a.project_name");
        }

        if (groupKeys != null && groupKeys.size() > 0) {
            groupKeys.forEach(groupKey -> {
                select.append(String.format(
                    ", a.%s AS %s",
                    groupKey.getFieldName(),
                    groupKey.getPropertyName()
                ));
                groupBy.append(", a.").append(groupKey.getFieldName());
            });
        }

        return new StringBuilder[]{select, groupBy};
    }

    /**
     * 归档统计数据。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据类型
     */
    @Override
    public void archiveData(
        final Long projectId,
        final ArchiveDataType archiveType
    ) {
        final EntityManager em = getEntityManager();

        (new Thread(() -> em
            .createStoredProcedureQuery(archiveType.getStoredProcedureName())
            .registerStoredProcedureParameter("timestamp", Date.class, ParameterMode.IN)
            .registerStoredProcedureParameter("projectId", String.class, ParameterMode.IN)
            .setParameter("timestamp", new Date())
            .setParameter("projectId", projectId)
            .execute()
        )).start();
    }

    /**
     * 设置聚合单位查询条件。
     *
     * @param groupDTO           聚合单位参数
     * @param groupKeyConditions 聚合单位查询条件 SQL
     * @param parameters         参数表
     */
    public static void setGroupKeyConditions(
        final ArchiveDataGroupDTO groupDTO,
        final StringBuilder groupKeyConditions,
        final Map<String, Object> parameters
    ) {
        setGroupKeyConditions(groupDTO, groupKeyConditions, parameters, "a");
    }

    /**
     * 设置聚合单位查询条件。
     *
     * @param groupDTO           聚合单位参数
     * @param groupKeyConditions 聚合单位查询条件 SQL
     * @param parameters         参数表
     * @param alias              别名
     */
    public static void setGroupKeyConditions(
        final ArchiveDataGroupDTO groupDTO,
        final StringBuilder groupKeyConditions,
        final Map<String, Object> parameters,
        final String alias
    ) {
        if (groupDTO != null) {
            for (Field groupKey : GROUP_KEYS) {
                try {
                    if (groupKey.get(groupDTO) == null) {
                        continue;
                    }

                    ArchiveDataGroupKey groupKeyValue = ArchiveDataGroupKey
                        .getByPropertyName(groupKey.getName());

                    if (groupKeyValue == null) {
                        continue;
                    }

                    if ("null".equals(groupKey.get(groupDTO))) {
                        groupKeyConditions.append(String.format(
                            " AND %s.%s IS NULL",
                            alias,
                            groupKeyValue.getFieldName()
                        ));
                    } else {
                        if (groupKey.getName().equals("weekly") || groupKey.getName().equals("division") || groupKey.getName().equals("projectName")) {
                            groupKeyConditions.append(String.format(
                                " AND %s.%s in (:%s)",
                                alias,
                                groupKeyValue.getFieldName(),
                                groupKeyValue.getPropertyName()
                            ));
                            parameters.put(groupKeyValue.getPropertyName(), groupKey.get(groupDTO));
                        } else {
                            groupKeyConditions.append(String.format(
                                " AND %s.%s = :%s",
                                alias,
                                groupKeyValue.getFieldName(),
                                groupKeyValue.getPropertyName()
                            ));
                            parameters.put(groupKeyValue.getPropertyName(), groupKey.get(groupDTO));
                        }

                    }
                } catch (IllegalAccessException e) {
                    // nothing to do
                }
            }
        }
    }

    /**
     * 查询统计数据归档记录。
     *
     * @param <T>          返回的类型范型
     * @param projectId    项目 ID
     * @param archiveType  归档类型
     * @param joinLastDay  是否需要连接聚合区间最后日期的查询子句
     * @param archiveYear  归档年
     * @param archiveMonth 归档月
     * @param archiveDay   归档日
     * @param dateRangeDTO 对象数据期间范围
     * @param groupDTO     聚合单位参数
     * @param select       SQL 查询语句
     * @param groupBy      SQL 分组语句
     * @param type         返回的类型
     * @return 查询结果
     */
    //noinspection unchecked
    @SuppressWarnings({"JpaQlInspection", "unchecked"})
    private <T> List<T> queryForList(
        final Long projectId,
        final ArchiveDataType archiveType,
        final boolean joinLastDay,
        final Integer archiveYear,
        final Integer archiveMonth,
        final Integer archiveDay,
        final ArchiveDataDateRangeDTO dateRangeDTO,
        final ArchiveDataGroupDTO groupDTO,
        final StringBuilder select,
        final StringBuilder groupBy,
        final Class<T> type
    ) {
        StringBuilder dateRangeConditions = new StringBuilder();
        StringBuilder groupKeyConditions = new StringBuilder();

        // 设置参数
        final Map<String, Object> parameters = new HashMap<>();
        if (projectId != 0L) {
            parameters.put("projectId", projectId);
        }
        parameters.put("archiveType", archiveType.name());
        parameters.put("archiveYear", archiveYear);
        parameters.put("archiveMonth", archiveMonth);
        parameters.put("archiveDay", archiveDay);

        // 起止时间查询条件
        if (dateRangeDTO != null) {

            ArchiveTimeDTO dateFrom = dateRangeDTO.getDateFromTime();

            if (dateFrom != null) {
                dateRangeConditions.append(String.format(
                    " AND a.group_date %s :groupDateFrom",
                    dateFrom.isInclude() ? ">=" : ">"
                ));
                parameters.put("groupDateFrom", dateFrom.toInteger());
            }

            ArchiveTimeDTO dateUntil = dateRangeDTO.getDateUntilTime();

            if (dateUntil != null) {
                dateRangeConditions.append(String.format(
                    " AND a.group_date %s :groupDateUntil",
                    dateUntil.isInclude() ? "<=" : "<"
                ));
                parameters.put("groupDateUntil", dateUntil.toInteger());
            }
        }

        // 设置聚合单位查询条件
        setGroupKeyConditions(groupDTO, groupKeyConditions, parameters);

        StringBuilder whereAndGroupBy = (new StringBuilder())
//            .append(" WHERE a.project_id = :projectId")
            .append(" WHERE a.archive_type = :archiveType")
            .append(" AND a.archive_year = :archiveYear")
            .append(" AND a.archive_month = :archiveMonth")
            .append(" AND a.archive_day = :archiveDay");

        if (projectId != 0L) {
            whereAndGroupBy.append(" AND a.project_id = :projectId");
        }

        whereAndGroupBy.append(dateRangeConditions);
        whereAndGroupBy.append(groupKeyConditions);
        whereAndGroupBy.append(groupBy == null ? "" : groupBy.toString());

        if (archiveType.equals(ArchiveDataType.DIVISION_STATISTICS)){
            whereAndGroupBy.append(" ORDER BY a.weekly, a.division, a.project_name");
        } else if (archiveType.equals(ArchiveDataType.MAN_HOUR_STATISTICS) || archiveType.equals(ArchiveDataType.MAN_HOUR_POWER_STATISTICS)) {
            whereAndGroupBy.append(" ORDER BY a.weekly, a.project_name");
        }

        StringBuilder leftOuterJoinLastDay = new StringBuilder();

        if (joinLastDay) {
            leftOuterJoinLastDay
                .append(" LEFT OUTER JOIN (")
                .append("  SELECT DISTINCT MAX(group_date) AS group_date")
                .append("  FROM statistics")
                .append(whereAndGroupBy.toString().replaceAll(" a\\.", " "))
                .append(" ) AS d ON d.group_date = a.group_date");
        }

        Query query = getEntityManager()
            .createNativeQuery(
                select
                    .append(" FROM statistics a")
                    .append(leftOuterJoinLastDay)
                    .append(whereAndGroupBy)
                    .toString()
            )
            .unwrap(org.hibernate.query.Query.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        parameters.keySet().forEach(key -> query.setParameter(key, parameters.get(key)));

        List<T> result = new ArrayList<>();

        // 取得聚合数据时
        if (type == ArchiveData.class) {
            query.getResultList().forEach(record ->
                result.add((T) new ArchiveData((Map<String, Object>) record))
            );
            // 取得聚合期间时
        } else if (type == ArchiveTimeDTO.class) {
            query.getResultList().forEach(record ->
                result.add((T) new ArchiveTimeDTO((Map<String, Object>) record))
            );
            // 取得聚合 KEY 时
        } else if (type == String.class) {
            query.getResultList().forEach(record ->
                ((Map<String, Object>) record).forEach((key, value) -> {
                    if (value != null) {
                        result.add((T) value);
                    }
                })
            );
        }

        return result;
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
        /*----------------------------------------------------------------------
          执行的 SQL 语句：

            SELECT DISTINCT
              `聚合单位字段`
            FROM
              `ose_report`.`statistics`
            WHERE
              `project_id`        = :projectId
              AND `archive_type`  = :archiveType
              AND `archive_year`  = :archiveYear
              AND `archive_month` = :dayOfMonth.month
              AND `archive_day`   = :dayOfMonth.day
              AND 聚合单位条件...

          * dayOfMonth 代表所选区间的最后日期的月/日
         ---------------------------------------------------------------------*/
        return queryForList(
            projectId,
            archiveType, false,
            archiveYear, archiveMonth, archiveDay,
            null, groupDTO,
            new StringBuilder(String.format(
                "SELECT DISTINCT a.%s AS %s",
                keyName.getFieldName(),
                keyName.getPropertyName()
            )),
            null,
            String.class
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

        /*----------------------------------------------------------------------
          执行的 SQL 语句：

            SELECT
              `group_year`,
              `group_month`, -- 取得日/月次数据时设置
              `group_day`,   -- 取得日次数据时设置
              `group_week`   -- 取得周次数据时设置
            FROM
              `ose_report`.`statistics`
            WHERE
              `project_id`        = :projectId
              AND `archive_type`  = :archiveType
              AND `archive_year`  = :archiveYear
              AND `archive_month` = :dayOfMonth.month
              AND `archive_day`   = :dayOfMonth.day
            GROUP BY
              `group_year`,
              `group_month`, -- 取得日/月次数据时设置
              `group_day`,   -- 取得日次数据时设置
              `group_week`   -- 取得周次数据时设置

          * dayOfMonth 代表所选区间的最后日期的月/日
         ---------------------------------------------------------------------*/
        StringBuilder[] selectAndGroupBySQL = buildSelectAndGroupSQL(scheduleType, null);
        StringBuilder select = selectAndGroupBySQL[0];
        StringBuilder groupBy = selectAndGroupBySQL[1];

        return queryForList(
            projectId,
            archiveType, false,
            archiveYear, archiveMonth, archiveDay,
            null, null,
            select, groupBy,
            ArchiveTimeDTO.class
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
    public ArchiveData sum(
        final Long projectId,
        final ArchiveDataType archiveType,
        final ArchiveScheduleType scheduleType,
        final Integer archiveYear,
        final Integer archiveMonth,
        final Integer archiveDay,
        final ArchiveDataDateRangeDTO dateRangeDTO,
        final ArchiveDataGroupDTO groupDTO
    ) {
        /*----------------------------------------------------------------------
          执行的 SQL 语句：

            SELECT DISTINCT
              SUM(`value_01`) AS `value_01`,
              SUM(`value_02`) AS `value_02`,
              SUM(`value_03`) AS `value_03`,
              SUM(`value_04`) AS `value_04`,
              SUM(`value_05`) AS `value_05`,
              SUM(`value_06`) AS `value_06`,
              SUM(`value_07`) AS `value_07`,
              SUM(`value_08`) AS `value_08`,
              SUM(`value_09`) AS `value_09`
            FROM
              `ose_report`.`statistics`
            WHERE
              `project_id`        = :projectId
              AND `archive_type`  = :archiveType
              AND `archive_year`  = :archiveYear
              AND `archive_month` = :dayOfMonth.month
              AND `archive_day`   = :dayOfMonth.day
              AND 聚合单位条件...

          * dayOfMonth 代表所选区间的最后日期的月/日
         ---------------------------------------------------------------------*/
        List<ArchiveData> result = queryForList(
            projectId,
            archiveType, false,
            archiveYear, archiveMonth, archiveDay,
            dateRangeDTO, groupDTO,
            (new StringBuilder())
                .append("SELECT ")
                .append(setGroupOfValues(archiveType, null)),
            null,
            ArchiveData.class
        );

        return result.size() > 0 ? result.get(0) : null;
    }

    /**
     * 取得数据聚合期间统计数据列表。
     *
     * @param projectId     项目 ID
     * @param archiveType   归档类型
     * @param scheduleType  期间视图类型
     * @param groupKeyNames 聚合字段列表
     * @param fetchLast     仅取得最后一天统计值的字段列表
     * @param archiveYear   归档年
     * @param archiveMonth  归档月
     * @param archiveDay    归档日
     * @param dateRangeDTO  对象数据期间范围
     * @param groupDTO      分组参数
     * @return 数据聚合期间列表
     */
    @Override
    public List<ArchiveData> periodData(
        final Long projectId,
        final ArchiveDataType archiveType,
        final ArchiveScheduleType scheduleType,
        final List<String> groupKeyNames,
        final List<String> fetchLast,
        final Integer archiveYear,
        final Integer archiveMonth,
        final Integer archiveDay,
        final ArchiveDataDateRangeDTO dateRangeDTO,
        final ArchiveDataGroupDTO groupDTO
    ) {
        /*----------------------------------------------------------------------
          执行的 SQL 语句：

            SELECT
              `group_year`,
              `group_month`, -- 取得日次及月次数据时返回
              `group_day`,   -- 取得日次数据时返回
              `group_week`,  -- 取得周次数据时返回
              SUM(`value_01`) AS `value_01`,
              SUM(`value_02`) AS `value_02`,
              SUM(`value_03`) AS `value_03`,
              SUM(`value_04`) AS `value_04`,
              SUM(`value_05`) AS `value_05`,
              SUM(`value_06`) AS `value_06`,
              SUM(`value_07`) AS `value_07`,
              SUM(`value_08`) AS `value_08`,
              SUM(`value_09`) AS `value_09`
            FROM
              `ose_report`.`statistics`
            WHERE
              `project_id`        = :projectId
              AND `archive_type`  = :archiveType
              AND `archive_year`  = :archiveYear
              AND `archive_month` = :dayOfMonth.month
              AND `archive_day`   = :dayOfMonth.day
              AND 聚合单位条件...
            GROUP BY
              `group_year`,
              `group_month`, -- 取得日/月次数据时设置
              `group_day`,   -- 取得日次数据时设置
              `group_week`   -- 取得周次数据时设置

          * dayOfMonth 代表所选区间的最后日期的月/日
         ---------------------------------------------------------------------*/
        StringBuilder[] selectAndGroupBySQL = buildSelectAndGroupSQL(
            scheduleType,
            toArchiveDataGroupKeyList(groupKeyNames)
        );

        StringBuilder select = selectAndGroupBySQL[0]
            .append(",")
            .append(setGroupOfValues(archiveType, fetchLast));

        StringBuilder groupBy = selectAndGroupBySQL[1];

        return queryForList(
            projectId,
            archiveType, (fetchLast != null && fetchLast.size() > 0),
            archiveYear, archiveMonth, archiveDay,
            dateRangeDTO, groupDTO,
            select, groupBy,
            ArchiveData.class
        );
    }
}
