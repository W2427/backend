package com.ose.report.domain.repository.statistics;

import com.ose.report.entity.statistics.IssueStats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

/**
 * 遗留问题统计数据仓库。
 */
public interface IssueStatsRepository extends CrudRepository<IssueStats, String>, IssueStatsCustomRepository {

    @Query(
        value = "SELECT DISTINCT * FROM ((SELECT create_year AS year, create_month AS month, create_day AS dayOfMonth, create_week AS week FROM ose_issues.issue_stats WHERE project_id = :projectId GROUP BY create_year, create_month, create_day, create_week)"
            + "UNION (SELECT close_year AS year, close_month AS month, close_day AS dayOfMonth, close_week AS week FROM ose_issues.issue_stats WHERE project_id = :projectId GROUP BY close_year, close_month, close_day, close_week)"
            + "UNION (SELECT YEAR(CURRENT_TIMESTAMP()) AS year, MONTh(CURRENT_TIMESTAMP()) AS month, DAYOFMONTH(CURRENT_TIMESTAMP()) AS dayOfMonth, WEEK_OF_YEAR(CURRENT_TIMESTAMP()) AS week FROM DUAL)) AS d ORDER BY year, month, dayOfMonth",
        nativeQuery = true
    )
    List<Map<String, Number>> dates(@Param("projectId") Long projectId);

    @Query(
        value = "SELECT DISTINCT * FROM ((SELECT create_week AS week FROM ose_issues.issue_stats WHERE project_id = :projectId GROUP BY create_week)"
            + "UNION (SELECT close_week AS week FROM ose_issues.issue_stats WHERE project_id = :projectId GROUP BY close_week)"
            + "UNION (SELECT WEEK_OF_YEAR(CURRENT_TIMESTAMP()) AS week FROM DUAL)) AS w ORDER BY week",
        nativeQuery = true
    )
    List<Map<String, Number>> weeks(@Param("projectId") Long projectId);

    @Query(
        value = "SELECT level FROM ose_issues.issue_stats WHERE project_id = :projectId GROUP BY level",
        nativeQuery = true
    )
    List<String> levels(@Param("projectId") Long projectId);

    @Query(
        value = "SELECT area FROM ose_issues.issue_stats WHERE project_id = :projectId GROUP BY area",
        nativeQuery = true
    )
    List<String> areas(@Param("projectId") Long projectId);

    @Query(
        value = "SELECT pressure_test_package FROM ose_issues.issue_stats WHERE project_id = :projectId GROUP BY pressure_test_package",
        nativeQuery = true
    )
    List<String> pressureTestPackages(@Param("projectId") Long projectId);

    @Query(
        value = "SELECT sub_system FROM ose_issues.issue_stats WHERE project_id = :projectId GROUP BY sub_system",
        nativeQuery = true
    )
    List<String> subSystems(@Param("projectId") Long projectId);

    @Query(
        value = "SELECT department_id FROM ose_issues.issue_stats WHERE project_id = :projectId GROUP BY department_id",
        nativeQuery = true
    )
    List<String> departments(@Param("projectId") Long projectId);
}
