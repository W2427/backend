/*******************************************************************************
  更改 work-package 相关 列名 表的字符集。

  DATE: 2020-01-11
 ******************************************************************************/

DROP PROCEDURE IF EXISTS `ose_tasks`.`alter-workpackage-column-names`;
-- SET FOREIGN_KEY_CHECKS = 0;

DELIMITER $$

CREATE DEFINER=`root`@`localhost` PROCEDURE `alter-workpackage-column-names`()
BEGIN

  DECLARE v_change   VARCHAR(255);
  DECLARE v_finished INTEGER;

  DECLARE change_cursor CURSOR FOR (
      SELECT
        concat(
        "ALTER TABLE ",
        cl.TABLE_SCHEMA,
        ".",
        cl.TABLE_NAME,
        " CHANGE COLUMN ",
        cl.COLUMN_NAME,
				" ",
        REPLACE(cl.COLUMN_NAME,'work_package','task_package'),
				" ",
				cl.COLUMN_TYPE,
				";"
      )
      FROM
        information_schema.COLUMNS cl left join information_schema.TABLES tb on cl.table_name = tb.table_name
      WHERE
        cl.COLUMN_NAME LIKE '%work_package%' and tb.table_type = 'BASE TABLE'
  );

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

  OPEN change_cursor;
    REPEAT
      FETCH change_cursor INTO v_change;
			select v_change;
      SET @statement = v_change;
      PREPARE v_statement FROM @statement;
      EXECUTE v_statement;
      DEALLOCATE PREPARE v_statement;
    UNTIL v_finished END REPEAT;
  CLOSE change_cursor;

END$$

DELIMITER ;

CALL `ose_tasks`.`alter-workpackage-column-names`();


