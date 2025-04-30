-- -----------------------------------------------------------------------------
-- 生成全局唯一的实体 ID。
-- 实体 ID 由两部分组成：
--  • 第一部分为【精度为百分之一毫秒的 Unix 纪元时间 * 10000】
-- -----------------------------------------------------------------------------

USE `saint_whale_tasks`;

DROP FUNCTION IF EXISTS `generate_entity_id`;

DELIMITER $$

CREATE DEFINER=`backend`@`%` FUNCTION `generate_entity_dec_id`() RETURNS bigint(19)
    DETERMINISTIC
BEGIN
    RETURN (FLOOR(UNIX_TIMESTAMP(CURTIME(6)) * 1000000000) + FLOOR(RAND() * 10000));
  END $$

DELIMITER ;
