SELECT
  *
FROM
  (
    SELECT
      w1.week,
      w1.count  AS open_1_week,
      w14.count AS open_1_4_weeks,
      w4.count  AS open_4_weeks,
      c.count   AS closed
    FROM
      (
        -- 一周内未处理问题合计
        SELECT
          w.week,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_weeks AS w
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= w.time_to))
            AND i.create_timestamp >= w.time_from
            AND i.create_timestamp < w.time_to
        GROUP BY
          w.week
      ) AS w1
      INNER JOIN (
        -- 一周以上四周以内未处理问题合计
        SELECT
          w.week,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_weeks AS w
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= w.time_to))
            AND i.create_timestamp >= (w.time_from - 1814400)
            AND i.create_timestamp < w.time_from
        GROUP BY
          w.week
      ) AS w14
        ON w14.week = w1.week
      INNER JOIN (
        -- 四周以上未处理问题合计
        SELECT
          w.week,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_weeks AS w
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= w.time_to))
            AND i.create_timestamp < (w.time_from - 1814400)
        GROUP BY
          w.week
      ) AS w4
        ON w4.week = w1.week
      INNER JOIN (
        -- 已关闭问题合计
        SELECT
          w.week,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_weeks AS w
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND i.status = 'CLOSED'
            AND i.close_week = w.week
        GROUP BY
          w.week
      ) AS c
        ON c.week = w1.week
  ) AS i
WHERE
  i.week >= 201909
  AND i.week <= 201914
;
