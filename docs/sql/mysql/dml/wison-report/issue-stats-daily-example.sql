SELECT
  FLOOR(date / 10000)           AS year,
  FLOOR(MOD(date, 10000) / 100) AS month,
  MOD(date, 100)                AS day,
  open_1_week,
  open_1_4_weeks,
  open_4_weeks,
  closed
FROM
  (
    SELECT
      w1.date,
      w1.count  AS open_1_week,
      w14.count AS open_1_4_weeks,
      w4.count  AS open_4_weeks,
      c.count   AS closed
    FROM
      (
        -- 一周内未处理问题合计
        SELECT
          d.date,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_dates AS d
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= d.time_to))
            AND i.create_timestamp >= (d.time_from - 518400)
            AND i.create_timestamp < d.time_to
        GROUP BY
          d.date
      ) AS w1
      INNER JOIN (
        -- 一周以上四周以内未处理问题合计
        SELECT
          d.date,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_dates AS d
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= d.time_to))
            AND i.create_timestamp >= (d.time_from - 2332800)
            AND i.create_timestamp < (d.time_from - 518400)
        GROUP BY
          d.date
      ) AS w14
        ON w14.date = w1.date
      INNER JOIN (
        -- 四周以上未处理问题合计
        SELECT
          d.date,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_dates AS d
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= d.time_to))
            AND i.create_timestamp < (d.time_from - 2332800)
        GROUP BY
          d.date
      ) AS w4
        ON w4.date = w1.date
      INNER JOIN (
        -- 已关闭问题合计
        SELECT
          d.date,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_dates AS d
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND i.status = 'CLOSED'
            AND i.close_year = d.year
            AND i.close_month = d.month
            AND i.close_day = d.day
        GROUP BY
          d.date
      ) AS c
        ON c.date = w1.date
  ) AS i
WHERE
  i.date >= 20190217
  AND i.date <= 20190403
;
