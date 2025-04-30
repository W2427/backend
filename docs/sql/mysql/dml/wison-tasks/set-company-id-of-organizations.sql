/*******************************************************************************
  补全组织的公司信息。

  DATE: 2018-12-07
 ******************************************************************************/

USE `ose_auth`;

DROP PROCEDURE IF EXISTS `set_company_id_of_organizations`;
DROP PROCEDURE IF EXISTS `set_company_id_of_tables`;
DROP PROCEDURE IF EXISTS `set_company_id_of_records`;

DELIMITER $$

/* 取得所有公司信息，设置组织的公司 ID。 */
CREATE PROCEDURE `set_company_id_of_organizations`()
  BEGIN

    DECLARE v_company_id VARCHAR(16);
    DECLARE v_finished   INTEGER;
    DECLARE v_companies CURSOR FOR (SELECT `id` FROM `organizations` WHERE `type` = 'COMPANY');
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

    SET v_finished = 0;

    OPEN v_companies;

    REPEAT

      FETCH v_companies INTO v_company_id;

      IF !v_finished THEN

        -- 设置组织的公司 ID
        UPDATE
          `organizations`
        SET
          `company_id` = v_company_id
        WHERE
          `id` = v_company_id
           OR `path` LIKE CONCAT('/', v_company_id, '/%')
        ;

        -- 设置项目的公司 ID
        UPDATE
          `ose_tasks`.`projects` AS `p`
        SET
          `p`.`company_id` = v_company_id
        WHERE
          `p`.`org_id` IN (SELECT `id` FROM `ose_auth`.`organizations` WHERE `company_id` = v_company_id)
        ;

        -- 设置其他拥有公司 ID 字段的表的数据的公司 ID
        CALL `set_company_id_of_tables`(v_company_id);

        SET v_finished = 0;

      END IF;

    UNTIL v_finished END REPEAT;

    CLOSE v_companies;

  END $$

/* 取得所有有公司 ID 字段的表。 */
CREATE PROCEDURE `set_company_id_of_tables`(IN v_company_id VARCHAR(16))
  BEGIN

    DECLARE v_table_name     VARCHAR(45);
    DECLARE v_finished       INTEGER;
    DECLARE v_tables CURSOR FOR (
      SELECT
        `cc`.`table_name`
      FROM
        `information_schema`.`columns` AS `cc`
        INNER JOIN `information_schema`.`tables` AS `t`
          ON `t`.`table_schema` = 'ose_tasks'
          AND `t`.`table_name` = `cc`.`table_name`
          AND `t`.`table_type` = 'BASE TABLE'
        LEFT OUTER JOIN `information_schema`.`columns` AS `co`
          ON `co`.`table_schema` = `cc`.`table_schema`
          AND `co`.`table_name` = `cc`.`table_name`
          AND `co`.`column_name` = 'org_id'
        LEFT OUTER JOIN `information_schema`.`columns` AS `cp`
          ON `cp`.`table_schema` = `cc`.`table_schema`
          AND `cp`.`table_name` = `cc`.`table_name`
          AND `cp`.`column_name` = 'project_id'
      WHERE
        `cc`.`column_name` = 'company_id'
        AND `co`.`column_name` IS NOT NULL
        AND `cp`.`column_name` IS NOT NULL
    );
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

    SET v_finished = 0;

    OPEN v_tables;

    REPEAT

      FETCH v_tables INTO v_table_name;

      IF !v_finished THEN
        CALL `set_company_id_of_records`(v_company_id, v_table_name);
      END IF;

    UNTIL v_finished END REPEAT;

    CLOSE v_tables;

  END $$

/* 更新有公司 ID 的表中数据的公司 ID。 */
CREATE PROCEDURE `set_company_id_of_records`(
  IN v_company_id VARCHAR(16),
  IN v_table_name VARCHAR(45)
)
  BEGIN

    SET @company_id = v_company_id;
    SET @statement = CONCAT(
      'UPDATE\r\n',
      '  `ose_tasks`.`', v_table_name, '` AS `t`\r\n',
      'SET\r\n',
      '  `t`.`company_id` = ?\r\n',
      'WHERE\r\n',
      '  `t`.`org_id` IN (SELECT `id` FROM `ose_auth`.`organizations` WHERE `company_id` = ?)\r\n',
      '  OR `t`.`project_id` IN (SELECT `id` FROM `ose_tasks`.`projects` WHERE `company_id` = ?)'
    );

    PREPARE statement FROM @statement;
    EXECUTE statement USING @company_id, @company_id, @company_id;
    DEALLOCATE PREPARE statement;

  END $$

DELIMITER ;

CALL `set_company_id_of_organizations`();

DROP PROCEDURE `set_company_id_of_organizations`;
DROP PROCEDURE `set_company_id_of_tables`;
DROP PROCEDURE `set_company_id_of_records`;
