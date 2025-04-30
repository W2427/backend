-- -----------------------------------------------------------------------------
-- 删除已过时效的定时任务执行记录。
--
-- 数据库类型：MySQL
-- 数据库　　：saint_whale_tasks
-- 创建时间　：2018-11-28
-- 创建者　　：金海岩
-- -----------------------------------------------------------------------------

USE `saint_whale_tasks`;

-- 停用定时任务
SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `remove_scheduled_task_logs`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 定时任务执行记录定期清除。
-- 执行频率：每天凌晨 02:00:00
-- -----------------------------------------------------------------------------
CREATE EVENT `remove_scheduled_task_logs`
    ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2018-11-01 02:00:00'
    ON COMPLETION PRESERVE ENABLE
DO
    BEGIN
        SET @min_date = ADDDATE(CURRENT_DATE(), -92);
        DELETE FROM `schedule_error` WHERE `created_at` < @min_date;
        DELETE FROM `schedule_log` WHERE `started_at` < @min_date;
    END $$

DELIMITER ;

-- 启用定时任务
SET GLOBAL event_scheduler = 1;
