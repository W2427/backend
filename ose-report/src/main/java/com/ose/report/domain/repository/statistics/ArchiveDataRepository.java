package com.ose.report.domain.repository.statistics;

import com.ose.report.entity.statistics.ArchiveData;
import com.ose.report.vo.ArchiveDataType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 统计数据归档记录数据仓库。
 */
public interface ArchiveDataRepository extends CrudRepository<ArchiveData, String>, ArchiveDataCustomRepository {

    /**
     * 取得所有归档时间。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据业务类型
     * @return 归档时间列表
     */
    @Query(
        value = "SELECT"
            + "  a.archive_year  AS archiveYear,"
            + "  a.archive_month AS archiveMonth,"
            + "  a.archive_day   AS archiveDay,"
            + "  a.archive_week  AS archiveWeek"
            + " FROM"
            + "  statistics a"
            + " WHERE"
            + "  a.project_id = :projectId"
            + "  AND a.archive_type = :archiveType"
            + " GROUP BY"
            + "  a.archive_year,"
            + "  a.archive_month,"
            + "  a.archive_day,"
            + "  a.archive_week"
            + " ORDER BY"
            + "  a.archive_year DESC,"
            + "  a.archive_month DESC,"
            + "  a.archive_day DESC",
        nativeQuery = true
    )
    List<Map<String, Object>> archiveDates(
        @Param("projectId") Long projectId,
        @Param("archiveType") String archiveType
    );

    /**
     * 取得所有归档时间。
     *
     * @param archiveType 归档数据业务类型
     * @return 归档时间列表
     */
    @Query(
        value = "SELECT"
            + "  a.archive_year  AS archiveYear,"
            + "  a.archive_month AS archiveMonth,"
            + "  a.archive_day   AS archiveDay,"
            + "  a.archive_week  AS archiveWeek"
            + " FROM"
            + "  statistics a"
            + " WHERE"
            + "  a.archive_type = :archiveType"
            + " GROUP BY"
            + "  a.archive_year,"
            + "  a.archive_month,"
            + "  a.archive_day,"
            + "  a.archive_week"
            + " ORDER BY"
            + "  a.archive_year DESC,"
            + "  a.archive_month DESC,"
            + "  a.archive_day DESC",
        nativeQuery = true
    )
    List<Map<String, Object>> archiveDatesNoProject(
        @Param("archiveType") String archiveType
    );

    /**
     * 取得最后归档数据。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据类型
     * @return 归档数据
     */
    ArchiveData findFirstByProjectIdAndArchiveTypeOrderByArchiveYearDescArchiveMonthDescArchiveDayDesc(
        Long projectId,
        ArchiveDataType archiveType
    );

    /**
     * 检查是否存在归档数据。
     *
     * @param projectId    项目 ID
     * @param archiveType  归档数据类型
     * @param archiveYear  归档时间（年）
     * @param archiveMonth 归档时间（月）
     * @param archiveDay   归档时间（日）
     * @return 归档数据
     */
    boolean existsByProjectIdAndArchiveTypeAndArchiveYearAndArchiveMonthAndArchiveDay(
        Long projectId,
        ArchiveDataType archiveType,
        int archiveYear,
        int archiveMonth,
        int archiveDay
    );

    /**
     * 取得最后归档日期。
     *
     * @param projectId   项目 ID
     * @param archiveType 归档数据类型
     * @return 最后归档日期
     */
    @Query(
        value = "SELECT"
            + "  CASE WHEN a.m IS NOT NULL AND a.m > a.c THEN a.m ELSE a.c END"
            + " FROM ("
            + "  SELECT"
            + "   MAX(created_at) AS c,"
            + "   MAX(last_modified_at) AS m"
            + "  FROM statistics"
            + "  WHERE"
            + "   project_id = :projectId"
            + "   AND archive_type = :archiveType"
            + " ) AS a",
        nativeQuery = true
    )
    Date lastArchiveDateTime(
        @Param("projectId") Long projectId,
        @Param("archiveType") String archiveType
    );


    /**
     * 取得所Tag重量统计基础数据。
     *
     * @param projectId 项目 ID
     * @return
     */
    @Query(
        value = "SELECT " +
            "tt.module AS module, " +
            "tt.deck AS deck, " +
            "tt.panel AS panel, " +
            "tt.wp04_no AS wp04_no, " +
            "tt.entity_category AS entity_category, " +
            "tt.process AS process, " +
            "tt.work_group AS work_group, " +
            "tt.address AS address, " +
            "YEAR ( CURRENT_DATE ) AS group_year, " +
            "MONTH ( CURRENT_DATE ) AS group_month, " +
            "DAY ( CURRENT_DATE ) AS group_day, " +
            "YEAR ( CURRENT_DATE ) * 10000 + MONTH ( CURRENT_DATE ) * 100 + DAY ( CURRENT_DATE ) AS group_date, " +
            "ose_tasks.WEEK_OF_YEAR ( CURRENT_DATE ) AS group_week, " +
            "tt.org_id AS org_id, " +
            "tt.project_id AS project_id, " +
            "( " +
            "CASE " +
            "WHEN ( tt.entity_category = 'WP04' ) THEN " +
            "round( wp04.weight_direct_subset / 1000, 2 ) ELSE ( " +
            "CASE " +
            " " +
            "WHEN ( tt.entity_category = 'WP03' ) THEN " +
            "round( wp03.weight_direct_subset / 1000, 2 ) ELSE ( CASE WHEN ( tt.entity_category = 'WP02' ) THEN round( wp02.weight_direct_subset / 1000, 2 ) ELSE 0 END )  " +
            "END  " +
            ")  " +
            "END  " +
            ") AS weight_total, " +
            "round( " +
            "( " +
            "CASE " +
            " " +
            "WHEN ( tt.entity_category = 'WP04' ) THEN " +
            "wp04.weight_direct_subset / 1000 ELSE ( " +
            "CASE " +
            " " +
            "WHEN ( tt.entity_category = 'WP03' ) THEN " +
            "wp03.weight_direct_subset / 1000 ELSE ( CASE WHEN ( tt.entity_category = 'WP02' ) THEN wp02.weight_direct_subset / 1000 ELSE 0 END )  " +
            "END  " +
            ")  " +
            "END  " +
            ") * sum( tt.done_length ) / count( tt.length ), " +
            "2  " +
            ") AS weight_done,  " +
            "1 AS wbs_entry_total, " +
            "(CASE WHEN ( tt.running_status = 'APPROVED' ) THEN 1 ELSE 0 END ) AS wbs_entry_done, " +
            "round(tt.length/1000,2) AS length_total, " +
            "round((CASE WHEN (tt.done_length = 1 ) THEN tt.length ELSE 0 END)/1000,2) AS length_done " +
            "FROM " +
            "( " +
            "SELECT " +
            "ew.NO AS NO, " +
            "ew.hierachy_parent AS entity_category, " +
            "pn.wp01_no AS module, " +
            "substring( pn.wp02_no, 6, 1 ) AS deck, " +
            "pn.wp03_no AS panel, " +
            "pn.wp04_no AS wp04_no, " +
            "we.process AS process, " +
            "wes.team_name AS work_group, " +
            "wes.work_site_address AS address, " +
            "wes.running_status AS running_status, " +
            "wes.finished_at AS finished_at, " +
            "ew.length_text AS length, " +
            "( CASE WHEN ( wes.running_status = 'APPROVED' ) THEN 1 ELSE 0 END ) AS done_length, " +
            "ew.org_id AS org_id, " +
            "ew.project_id AS project_id  " +
            "FROM " +
            "ose_tasks.entity_structure_weld ew " +
            "LEFT JOIN ose_tasks.project_node pn ON pn.entity_id = ew.id " +
            "LEFT JOIN ose_tasks.wbs_entry we ON ew.id = we.entity_id " +
            "LEFT JOIN ose_tasks.wbs_entry_state wes ON wes.wbs_entry_id = we.id  " +
            "WHERE " +
            "ew.project_id = :projectId  " +
            "AND pn.project_id = :projectId  " +
            "AND ew.deleted IS FALSE  " +
            "AND we.process IN ( 'FITUP', 'WELD' )  " +
            ") AS tt " +
            "LEFT JOIN ose_tasks.entity_wp04 wp04 ON tt.wp04_no = wp04.NO  " +
            "AND wp04.deleted " +
            "IS FALSE LEFT JOIN ose_tasks.entity_wp03 wp03 ON tt.panel = wp03.NO  " +
            "AND wp03.deleted " +
            "IS FALSE LEFT JOIN ose_tasks.entity_wp02 wp02 ON tt.deck = wp02.NO  " +
            "AND wp02.deleted IS FALSE  " +
            "WHERE tt.project_id = :projectId  " +
            "GROUP BY " +
            "tt.module, " +
            "tt.deck, " +
            "tt.panel, " +
            "tt.wp04_no, " +
            "tt.entity_category, " +
            "tt.process, " +
            "tt.work_group, " +
            "tt.address",
        nativeQuery = true
    )
    List<Map<String, Object>> archiveTagWeightDates(
        @Param("projectId") Long projectId
    );

    /**
     * 取得焊口统计基础数据。
     *
     * @param projectId 项目 ID
     * @return
     */
    @Query(
        value = "SELECT " +
            "tt.module AS module, " +
            "tt.deck AS deck, " +
            "tt.panel AS panel, " +
            "tt.wp04_no AS wp04_no, " +
            "tt.entity_category AS entity_category, " +
            "tt.process AS process, " +
            "tt.work_group AS work_group, " +
            "tt.address AS address, " +
            "YEAR ( CURRENT_DATE ) AS group_year, " +
            "MONTH ( CURRENT_DATE ) AS group_month, " +
            "DAY ( CURRENT_DATE ) AS group_day, " +
            "YEAR ( CURRENT_DATE ) * 10000 + MONTH ( CURRENT_DATE ) * 100 + DAY ( CURRENT_DATE ) AS group_date, " +
            "ose_tasks.WEEK_OF_YEAR ( CURRENT_DATE ) AS group_week, " +
            "tt.org_id AS org_id, " +
            "tt.project_id AS project_id, " +
            "tt.length  AS length, " +
            "tt.done_length  AS done_length" +
            "FROM " +
            "( " +
            "SELECT " +
            "ew.NO AS NO, " +
            "ew.hierachy_parent AS entity_category, " +
            "pn.wp01_no AS module, " +
            "substring( pn.wp02_no, 6, 1 ) AS deck, " +
            "pn.wp03_no AS panel, " +
            "pn.wp04_no AS wp04_no, " +
            "we.process AS process, " +
            "wes.team_name AS work_group, " +
            "wes.work_site_address AS address, " +
            "wes.running_status AS running_status, " +
            "wes.finished_at AS finished_at, " +
            "ew.length_text AS length, " +
            "( CASE WHEN ( wes.running_status = 'APPROVED' ) THEN 1 ELSE 0 END ) AS done_length, " +
            "ew.org_id AS org_id, " +
            "ew.project_id AS project_id  " +
            "FROM " +
            "ose_tasks.entity_structure_weld ew " +
            "LEFT JOIN ose_tasks.project_node pn ON pn.entity_id = ew.id " +
            "LEFT JOIN ose_tasks.wbs_entry we ON ew.id = we.entity_id " +
            "LEFT JOIN ose_tasks.wbs_entry_state wes ON wes.wbs_entry_id = we.id  " +
            "WHERE " +
            "ew.project_id = 1624840920575068904  " +
            "AND ew.deleted IS FALSE  " +
            "AND we.process IN ( 'FITUP', 'WELD' )  " +
            ") AS tt " +
            "LEFT JOIN ose_tasks.entity_wp04 wp04 ON tt.wp04_no = wp04.NO  " +
            "AND wp04.deleted " +
            "IS FALSE LEFT JOIN ose_tasks.entity_wp03 wp03 ON tt.panel = wp03.NO  " +
            "AND wp03.deleted " +
            "IS FALSE LEFT JOIN ose_tasks.entity_wp02 wp02 ON tt.deck = wp02.NO  " +
            "AND wp02.deleted IS FALSE  " +
            "WHERE tt.project_id = :projectId  " +
            "GROUP BY " +
            "tt.module, " +
            "tt.deck, " +
            "tt.panel, " +
            "tt.wp04_no, " +
            "tt.entity_category, " +
            "tt.process, " +
            "tt.work_group, " +
            "tt.address",
        nativeQuery = true
    )
    List<Map<String, Object>> archiveWeldDates(
        @Param("projectId") Long projectId
    );

    List<ArchiveData> findByArchiveType(ArchiveDataType archiveType);


    /**
     * 取得工时统计基础数据
     *
     * @return
     */
    @Query(
        value = "SELECT " +
            "dr.project_id AS projectId, " +
            "p.NAME AS projectName, " +
            "dr.weekly AS weekly, " +
            "sum( dr.work_hour ) AS workHour, " +
            "sum( dr.out_hour ) AS outHour  " +
            "FROM " +
            "saint_whale_tasks.drawing_record dr " +
            "INNER JOIN saint_whale_tasks.project p ON dr.project_id = p.id  " +
            "WHERE " +
            "dr.deleted IS FALSE  " +
            "GROUP BY " +
            "dr.project_id, " +
            "dr.weekly",
        nativeQuery = true
    )
    List<Map<String, Object>> archiveManHourStatisticsDates();

    /**
     * 取得工时统计基础数据
     *
     * @return
     */
    @Query(
        value = "SELECT " +
            "dr.project_id AS projectId, " +
            "p.NAME AS projectName, " +
            "dr.weekly AS weekly, " +
            "u.division AS division, " +
            "IFNULL( sum( work_hour ), 0 ) + IFNULL( sum( out_hour ), 0 ) AS totalHour, " +
            "count( DISTINCT u.id ) AS count  " +
            "FROM " +
            "saint_whale_tasks.drawing_record dr " +
            "LEFT JOIN saint_whale_auth.users u ON dr.engineer_id = u.id " +
            "LEFT JOIN saint_whale_tasks.project p ON dr.project_id = p.id  " +
            "WHERE " +
            "dr.deleted IS FALSE  " +
            "GROUP BY " +
            "dr.project_id, " +
            "dr.weekly, " +
            "u.division  " +
            "ORDER BY " +
            "totalHour",
        nativeQuery = true
    )
    List<Map<String, Object>> archiveDivisionStatisticsDates();

    /**
     * 取得工时统计基础数据
     *
     * @return
     */
    @Query(
        value = "SELECT " +
            "dr.project_id AS projectId, " +
            "p.NAME AS projectName, " +
            "dr.weekly AS weekly, " +
            "IFNULL( sum( dr.work_hour ), 0 ) + IFNULL( sum( dr.out_hour ), 0 ) AS totalHour, " +
            "count( DISTINCT dr.engineer_id ) AS manPower  " +
            "FROM " +
            "saint_whale_tasks.drawing_record dr " +
            "LEFT JOIN saint_whale_tasks.project p ON dr.project_id = p.id  " +
            "WHERE " +
            "dr.deleted IS FALSE  " +
            "GROUP BY " +
            "dr.project_id, " +
            "dr.weekly",
        nativeQuery = true
    )
    List<Map<String, Object>> archiveManHourPowerStatisticsDates();
}
