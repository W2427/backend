ALTER TABLE ose_report.statistics ADD COLUMN group_date INTEGER COMMENT '聚合对象期间（年月日）';

UPDATE
  ose_report.statistics
SET
  group_date = group_year * 10000 + group_month * 100 + group_day
;
