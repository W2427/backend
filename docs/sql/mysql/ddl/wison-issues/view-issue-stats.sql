-- 遗留问题统计视图
DROP VIEW IF EXISTS `ose_issues`.`issue_stats`;
CREATE VIEW `ose_issues`.`issue_stats` AS
  SELECT DISTINCT
    `i`.`id`,
    `i`.`project_id`,
    `i`.`level`,
    `ie`.`area`,
    `ie`.`pressure_test_package`,
    `ie`.`sub_system`,
    `ie`.`department` AS `department_id`,
    UNIX_TIMESTAMP(`i`.`created_at`)                  AS `create_timestamp`,
    YEAR(`i`.`created_at`)                            AS `create_year`,
    MONTH(`i`.`created_at`)                           AS `create_month`,
    DAYOFMONTH(`i`.`created_at`)                      AS `create_day`,
    ose_report.week_of_year(`i`.`created_at`)       AS `create_week`,
    UNIX_TIMESTAMP(`i`.`last_modified_at`)            AS `close_timestamp`,
    YEAR(`i`.`last_modified_at`)                      AS `close_year`,
    MONTH(`i`.`last_modified_at`)                     AS `close_month`,
    DAYOFMONTH(`i`.`last_modified_at`)                AS `close_day`,
    ose_report.week_of_year(`i`.`last_modified_at`) AS `close_week`,
    `i`.`status`
  FROM
    `ose_issues`.`issues` AS `i`
    LEFT OUTER JOIN `ose_issues`.`issue_entities` AS `ie`
      ON `ie`.`issue_id` = `i`.`id`
  WHERE
    `i`.`deleted` = 0
;

-- 日期列表视图，最大可取得包括当前日期在内的过去 11^3 天
DROP VIEW IF EXISTS `ose_issues`.`issue_dates`;
CREATE VIEW `ose_issues`.`issue_dates` AS
  SELECT
    YEAR(`date`)                          AS `year`,
    MONTH(`date`)                         AS `month`,
    DAYOFMONTH(`date`)                    AS `day`,
    YEAR(`date`) * 10000 + MONTH(`date`) * 100 + DAYOFMONTH(`date`)
                                          AS `date`,
    UNIX_TIMESTAMP(`date`)                AS `time_from`,
    (UNIX_TIMESTAMP(`date`) + 86400)      AS `time_to`,
    `ose_report`.`week_of_year`(`date`) AS `week`
  FROM
    (
      SELECT
        ADDDATE(CURDATE(), -1 * (`i`.`n` * 121 + `j`.`n` * 11 + `k`.`n`)) `date`
      FROM
        (SELECT 0 `n` UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) `i`,
        (SELECT 0 `n` UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) `j`,
        (SELECT 0 `n` UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10) `k`
    ) AS `dates`
;

-- 周列表视图
DROP VIEW IF EXISTS `ose_issues`.`issue_weeks`;
CREATE VIEW `ose_issues`.`issue_weeks` AS
  SELECT
    d.week,
    MIN(d.time_from) AS time_from,
    MAX(d.time_to)   AS time_to
  FROM
    ose_issues.issue_dates AS d
  GROUP BY
    d.week
;
