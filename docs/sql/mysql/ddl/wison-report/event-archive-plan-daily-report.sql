-- -----------------------------------------------------------------------------
-- 数据统计定时任务。

-- 创建时间　：2019-04-30
-- 创建者　　：卢杨
-- -----------------------------------------------------------------------------

USE `ose_report`;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_plan_daily_report_daily`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 为计划日报和计划周报归档当日数据使用。
-- 执行频率：每日下午04:00
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_plan_daily_report_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2018-11-01 16:00:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间

    -- 建造计划 / 完成率 + 工时
    CALL `archive_wbs_progress`(ADDTIME(NOW(), 0));

    -- 建造计划 / 焊接完成量
    CALL `archive_weld`(ADDTIME(NOW(), 0));

    -- 质量控制 / 报验合格率
    CALL `archive_pass_rate`(ADDTIME(NOW(), 0));

  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
