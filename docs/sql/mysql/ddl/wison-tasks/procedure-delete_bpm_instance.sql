USE saint_whale_tasks;
-- ----------------------------
-- Procedure structure for delete_bpm_instance
-- ----------------------------
DROP PROCEDURE IF EXISTS `delete_bpm_instance`;
delimiter ;;
CREATE DEFINER=`oseAdmin`@`%` PROCEDURE `delete_bpm_instance`(IN act_inst_id VARCHAR(16))
  BEGIN
  -- 存储主表 act_ge_bytearray 主键字符串的临时变量
  DECLARE byteId VARCHAR(1000);
  DECLARE byteIds VARCHAR(2000);
  set byteIds = '';

  DELETE FROM act_ru_event_subscr;

  DELETE FROM act_ru_variable;

  DELETE FROM act_ru_task;

  DELETE FROM act_ru_execution;

  DELETE FROM act_ru_identitylink;

  DELETE FROM act_ge_bytearray WHERE DEPLOYMENT_ID_ IS NULL;

END;
;;
delimiter ;
