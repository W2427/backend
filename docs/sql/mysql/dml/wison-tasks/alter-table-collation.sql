/*******************************************************************************
  更新表的字符集。

  DATE: 2019-02-19
 ******************************************************************************/

DROP PROCEDURE IF EXISTS `ose_tasks`.`alter_table_collation`;
SET FOREIGN_KEY_CHECKS = 0;

DELIMITER $$

CREATE PROCEDURE `ose_tasks`.`alter_table_collation`()
BEGIN

  DECLARE v_schema   VARCHAR(255);
  DECLARE v_table    VARCHAR(255);
  DECLARE v_finished INTEGER;

  DECLARE table_cursor CURSOR FOR (
    SELECT
      `table_schema` AS `schema`,
      `table_name`   AS `name`
    FROM
      `information_schema`.`tables`
    WHERE
      `table_schema` LIKE 'ose_%'
      AND `table_collation` <> 'utf8_general_ci'
  );

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

  OPEN table_cursor;
    REPEAT
      FETCH table_cursor INTO v_schema, v_table;
      SET @statement = CONCAT('alter table `', v_schema, '`.`', v_table, '` convert to character set utf8 collate utf8_general_ci');
      PREPARE v_statement FROM @statement;
      EXECUTE v_statement;
      DEALLOCATE PREPARE v_statement;
    UNTIL v_finished END REPEAT;
  CLOSE table_cursor;

END$$

DELIMITER ;

CALL `ose_tasks`.`alter_table_collation`();

DROP PROCEDURE `ose_tasks`.`alter_table_collation`;
SET FOREIGN_KEY_CHECKS = 1;
