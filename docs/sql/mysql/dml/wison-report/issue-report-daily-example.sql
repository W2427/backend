SELECT
  *
FROM
  (
    SELECT
      t.date,
      t.count AS total,
      f.count AS closed,
      o.count AS open
    FROM
      (
        SELECT
          d.date,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_dates AS d
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND i.create_timestamp < d.time_to
        GROUP BY
          d.date
      ) AS t
      INNER JOIN (
        SELECT
          d.date,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_dates AS d
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND i.status = 'CLOSED'
            AND i.close_timestamp >= d.time_from
            AND i.close_timestamp < d.time_to
        GROUP BY
          d.date
      ) AS f
        ON f.date = t.date
      INNER JOIN (
        SELECT
          d.date,
          COUNT(DISTINCT i.id) AS count
        FROM
          ose_issues.issue_dates AS d
          LEFT OUTER JOIN ose_issues.issue_stats AS i
            ON i.project_id = 'BPT10E9OZQUB6NRA'
            AND (i.status <> 'CLOSED' OR (i.status = 'CLOSED' AND i.close_timestamp >= d.time_to))
            AND i.create_timestamp < d.time_to
        GROUP BY
          d.date
      ) AS o
        ON o.date = t.date
  ) AS i
WHERE
  i.date >= 20190331
  AND i.date <= 20190403
;
