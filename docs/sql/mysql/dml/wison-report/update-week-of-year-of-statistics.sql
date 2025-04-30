UPDATE
  ose_report.statistics
SET
  archive_week = ose_report.week_of_year_ymd(archive_year, archive_month, archive_day),
  group_week = ose_report.week_of_year_ymd(group_year, group_month, group_day)
;
