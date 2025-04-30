USE `ose_tasks`;

DROP PROCEDURE IF EXISTS `find_out_invalid_datetime_format`;

DELIMITER $$

CREATE PROCEDURE `find_out_invalid_datetime_format`()
  BEGIN

    DECLARE v_schema   VARCHAR(255);
    DECLARE v_table    VARCHAR(255);
    DECLARE v_column   VARCHAR(255);
    DECLARE v_id_name  VARCHAR(255);
    DECLARE v_finished INTEGER;

    -- 查询数据库中所有类型为日期的字段
    DECLARE v_columns CURSOR FOR (
      SELECT
        `c`.`table_schema`,
        `c`.`table_name`,
        `c`.`column_name`,
        `cid`.`column_name` AS `id_column_name`
      FROM
        `information_schema`.`columns` AS `c`
        INNER JOIN `information_schema`.`tables` AS `t`
          ON `t`.`table_type` = 'BASE TABLE'
          AND `t`.`table_schema` = `c`.`table_schema`
          AND `t`.`table_name` = `c`.`table_name`
        LEFT OUTER JOIN `information_schema`.`columns` AS `cid`
          ON `cid`.`table_schema` = `c`.`table_schema`
          AND `cid`.`table_name` = `c`.`table_name`
          AND `cid`.`column_name` = 'id'
      WHERE
        `c`.`table_schema` LIKE 'ose\_%'
        AND (`c`.`data_type` = 'datetime'
            OR `c`.`data_type` = 'DATETIME'
            OR `c`.`data_type` = 'timestamp'
            OR `c`.`data_type` = 'TIMESTAMP'
          )
    );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

    SET v_finished = 0;

    SET @v_index       = 0;
    SET @v_create_view = 'CREATE OR REPLACE VIEW `temp_20181207` AS ';

    OPEN v_columns;

    -- 取得字段信息，构造视图 DDL
    -- CREATE OR REPLACE VIEW `temp_YYYYMMDD` AS
    --   (
    --     SELECT
    --       ':tableName'  AS `table_name`,
    --       ':columnName' AS `column_name`,
    --       ':recordId'   AS `record_id`
    --       ':datetime'   AS `value`
    --     FROM
    --        :schemaName.:tableName
    --     WHERE
    --        :columnName IN ('0000-00-00', '0000-00-00 00:00:00', '0000-00-00 00:00:00.000')
    --   ) UNION ...
    REPEAT

      FETCH v_columns INTO v_schema, v_table, v_column, v_id_name;

      IF !v_finished THEN

        SET @v_index = @v_index + 1;

        IF @v_index > 1 THEN
          SET @v_create_view = CONCAT(@v_create_view, ' UNION ');
        END IF;

        SET @v_create_view = CONCAT(
          @v_create_view,
          '(SELECT \'', v_schema, '\' AS `schema`, \'', v_table, '\' AS `table`, \'', v_column, '\' AS `column`'
        );

        IF v_id_name IS NULL THEN
          SET @v_create_view = CONCAT(
            @v_create_view,
            ', NULL AS `record_id`, `', v_column, '` AS `value`'
          );
        ELSE
          SET @v_create_view = CONCAT(
            @v_create_view,
            ', `', v_id_name,'` AS `record_id`, `', v_column, '` AS `value`'
          );
        END IF;

        SET @v_create_view = CONCAT(
          @v_create_view,
          ' FROM `', v_schema, '`.`', v_table, '`',
          -- ' WHERE `', v_column, '` IS NOT NULL)'
          ' WHERE `', v_column, '` IN (\'0000-00-00\', \'0000-00-00 00:00:00\', \'0000-00-00 00:00:00.000\'))'
        );

      END IF;

    UNTIL v_finished END REPEAT;

    CLOSE v_columns;

    -- 创建视图
    PREPARE v_create_view FROM @v_create_view;
    EXECUTE v_create_view;
    DEALLOCATE PREPARE v_create_view;

  END $$

DELIMITER ;

CALL `find_out_invalid_datetime_format`();

DROP PROCEDURE `find_out_invalid_datetime_format`;
