USE saint_whale_tasks;
-- ----------------------------
-- Function structure for week_of_year_ymd
-- ----------------------------
DROP FUNCTION IF EXISTS `week_of_year_ymd`;
delimiter ;;
CREATE DEFINER=`oseadmin`@`%` FUNCTION `week_of_year_ymd`(v_year INTEGER, v_month INTEGER, v_day_of_month INTEGER) RETURNS int(6)
  DETERMINISTIC
BEGIN

    SET @first_day_of_next_year = (v_year + 1) * 10000 + 101;
    SET @current_date           = v_year * 10000 + v_month * 100 + v_day_of_month;

    -- 若指定日期在所在年的最后一周且后一年的第一天在第零周，则将该日期记为下一年的第一周
    IF WEEK(@first_day_of_next_year) = 0 AND YEARWEEK(@current_date) = YEARWEEK(@first_day_of_next_year) THEN
      RETURN (v_year + 1) * 100 + 1;
    END IF;

    SET @week_offset  = 0;

    -- 否则，若当年存在第零周，则将第零周作为第一周，第一周作为第二周，依此类推
    IF WEEK(v_year * 10000 + 101) = 0 THEN
      SET @week_offset = 1;
    END IF;

    RETURN v_year * 100 + WEEK(@current_date) + @week_offset;
  END;
;;
delimiter ;
