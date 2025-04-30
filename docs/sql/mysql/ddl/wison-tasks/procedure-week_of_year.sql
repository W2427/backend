USE saint_whale_tasks;
-- ----------------------------
-- Function structure for week_of_year
-- ----------------------------
DROP FUNCTION IF EXISTS `week_of_year`;
delimiter ;;
CREATE DEFINER=`oseadmin`@`%` FUNCTION `week_of_year`(v_timepstame TIMESTAMP) RETURNS int(6)
  DETERMINISTIC
BEGIN
    RETURN `week_of_year_ymd`(YEAR(v_timepstame), MONTH(v_timepstame), DAYOFMONTH(v_timepstame));
  END;
;;
delimiter ;
