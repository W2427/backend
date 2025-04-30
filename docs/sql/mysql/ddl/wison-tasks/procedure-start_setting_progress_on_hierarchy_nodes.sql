USE saint_whale_tasks;
-- ----------------------------
-- Procedure structure for shift_welder
-- ----------------------------
DROP PROCEDURE IF EXISTS `start_setting_progress_on_hierarchy_nodes`;
delimiter ;;
CREATE DEFINER=`backend`@`%` PROCEDURE `start_setting_progress_on_hierarchy_nodes`()
BEGIN
    CALL `set_progress_on_hierarchy_nodes`();
  END;
;;
delimiter ;
