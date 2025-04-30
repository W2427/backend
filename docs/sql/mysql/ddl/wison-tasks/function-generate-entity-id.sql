-- -----------------------------------------------------------------------------
-- 生成全局唯一的实体 ID。
-- 实体 ID 由两部分组成：
--  • 第一部分为【精度为百分之一毫秒的 Unix 纪元时间 * 10000】+【0~9999 之间的随机数】
--  • 第二部分为【0~1679615 之间的随机数】
-- 将以上各部分转为 36 进制并保证第二部分长度为 4 后拼接得到最终的实体 ID 字符串。
-- -----------------------------------------------------------------------------

USE `saint_whale_tasks`;

DROP FUNCTION IF EXISTS `generate_entity_id`;

DELIMITER $$

CREATE DEFINER=`backend`@`%` FUNCTION `generate_entity_id`() RETURNS varchar(255) CHARSET utf8 COLLATE utf8_bin
    DETERMINISTIC
BEGIN
    RETURN CONCAT(
      CONV(FLOOR(UNIX_TIMESTAMP(CURTIME(6)) * 1000000000) + FLOOR(RAND() * 10000), 10, 36),
      SUBSTR(CONCAT('000', CONV(FLOOR(RAND() * 1679616), 10, 36)), -4, 4)
    );
  END $$

DELIMITER ;
