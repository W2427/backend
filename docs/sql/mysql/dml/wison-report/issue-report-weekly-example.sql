SELECT
  *
FROM
  (
    SELECT
      t.week,
      t.count AS total,
      f.count AS closed,
      o.count AS open
    FROM
      (
        SELECT
          w.week,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_weeks AS w
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND i.create_timestamp < w.time_to
        GROUP BY
          w.week
      ) AS t
      INNER JOIN (
        SELECT
          w.week,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_weeks AS w
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND i.status = 'CLOSED'
            AND i.close_timestamp >= w.time_from
            AND i.close_timestamp < w.time_to
        GROUP BY
          w.week
      ) AS f
        ON f.week = t.week
      INNER JOIN (
        SELECT
          w.week,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_weeks AS w
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= w.time_to))
            AND i.create_timestamp < w.time_to
        GROUP BY
          w.week
      ) AS o
        ON o.week = t.week
  ) AS i
WHERE
  i.week >= 201908
  AND i.week <= 201912
;
