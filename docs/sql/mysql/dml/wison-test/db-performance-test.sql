USE `ose_test`;

/*
DROP TABLE IF EXISTS `test_debug`;
DROP TABLE IF EXISTS `test_01`;
DROP TABLE IF EXISTS `test_02`;
DROP TABLE IF EXISTS `test_03`;
*/
DROP PROCEDURE IF EXISTS `generate_data`;

CREATE TABLE IF NOT EXISTS `test_debug` (
  `timestamp`  VARCHAR(19) NOT NULL,
  `table_name` VARCHAR(45) NOT NULL,
  `count`      INTEGER     NOT NULL DEFAULT 0,
  `start_at`   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `finish_at`  TIMESTAMP   NULL,
  PRIMARY KEY (`timestamp`, `table_name`)
);

CREATE TABLE IF NOT EXISTS `test_01` (
  `id`        INTEGER NOT NULL AUTO_INCREMENT,
  `column_01` VARCHAR(45), `column_02` VARCHAR(45), `column_03` TIMESTAMP,   `column_04` DOUBLE,      `column_05` INTEGER,
  `column_06` VARCHAR(45), `column_07` VARCHAR(45), `column_08` VARCHAR(45), `column_09` VARCHAR(45), `column_10` VARCHAR(45),
  `column_11` VARCHAR(45), `column_12` VARCHAR(45), `column_13` VARCHAR(45), `column_14` VARCHAR(45), `column_15` VARCHAR(45),
  `column_16` VARCHAR(45), `column_17` VARCHAR(45), `column_18` VARCHAR(45), `column_19` VARCHAR(45), `column_20` VARCHAR(45),
  `column_21` VARCHAR(45), `column_22` VARCHAR(45), `column_23` VARCHAR(45), `column_24` VARCHAR(45), `column_25` VARCHAR(45),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `test_02` (
  `id`        INTEGER NOT NULL AUTO_INCREMENT,
  `column_01` VARCHAR(45), `column_02` VARCHAR(45), `column_03` TIMESTAMP,   `column_04` DOUBLE,      `column_05` INTEGER,
  `column_06` VARCHAR(45), `column_07` VARCHAR(45), `column_08` VARCHAR(45), `column_09` VARCHAR(45), `column_10` VARCHAR(45),
  `column_11` VARCHAR(45), `column_12` VARCHAR(45), `column_13` VARCHAR(45), `column_14` VARCHAR(45), `column_15` VARCHAR(45),
  `column_16` VARCHAR(45), `column_17` VARCHAR(45), `column_18` VARCHAR(45), `column_19` VARCHAR(45), `column_20` VARCHAR(45),
  `column_21` VARCHAR(45), `column_22` VARCHAR(45), `column_23` VARCHAR(45), `column_24` VARCHAR(45), `column_25` VARCHAR(45),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `test_03` (
  `id`        INTEGER NOT NULL AUTO_INCREMENT,
  `column_01` VARCHAR(45), `column_02` VARCHAR(45), `column_03` TIMESTAMP,   `column_04` DOUBLE,      `column_05` INTEGER,
  `column_06` VARCHAR(45), `column_07` VARCHAR(45), `column_08` VARCHAR(45), `column_09` VARCHAR(45), `column_10` VARCHAR(45),
  `column_11` VARCHAR(45), `column_12` VARCHAR(45), `column_13` VARCHAR(45), `column_14` VARCHAR(45), `column_15` VARCHAR(45),
  `column_16` VARCHAR(45), `column_17` VARCHAR(45), `column_18` VARCHAR(45), `column_19` VARCHAR(45), `column_20` VARCHAR(45),
  `column_21` VARCHAR(45), `column_22` VARCHAR(45), `column_23` VARCHAR(45), `column_24` VARCHAR(45), `column_25` VARCHAR(45),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE OR REPLACE VIEW `test_progress` AS (
  SELECT
    `start_at`,
    `table_name`,
    `total`,
    `count` AS `processed`,
    `remainder`,
    CONCAT(ROUND(`count` * 100 / `total`, 2), '%') AS `progress`,
    CONCAT(ROUND(`time_passed` / 3600, 4), ' H') AS `time_passed`,
    CONCAT(ROUND(`time_passed` * `remainder` / `count` / 3600, 4), ' H') AS `time_remainder`,
    CONCAT(ROUND(`time_passed` * `total` / `count` / 3600, 4), ' H') AS `time_total`
  FROM
    (
      SELECT
        *,
        `total` - `count` AS `remainder`
      FROM
        (
          SELECT
            *,
            CASE
              WHEN `table_name` = 'test_01' THEN 369
              WHEN `table_name` = 'test_02' THEN 369 * 369
              WHEN `table_name` = 'test_03' THEN 369 * 369 * 369
              ELSE 1
            END AS `total`,
            CASE
              WHEN `finish_at` IS NOT NULL THEN UNIX_TIMESTAMP(`finish_at`) - UNIX_TIMESTAMP(`start_at`)
              ELSE UNIX_TIMESTAMP(CURRENT_TIMESTAMP()) - UNIX_TIMESTAMP(`start_at`)
            END AS `time_passed`
          FROM
            `ose_test`.`test_debug`
        ) AS `a`
    ) AS `b`
);

TRUNCATE TABLE `test_debug`;
TRUNCATE TABLE `test_01`;
TRUNCATE TABLE `test_02`;
TRUNCATE TABLE `test_03`;

DELIMITER $$

CREATE PROCEDURE `generate_data`()
  BEGIN

    SET @v_timestamp   = DATE_FORMAT(CURRENT_TIMESTAMP(), '%Y-%m-%d %H:%i:%s');

    INSERT INTO `test_debug` (`timestamp`, `table_name`, `count`) VALUES (@v_timestamp, 'test_01', 0);
    INSERT INTO `test_debug` (`timestamp`, `table_name`, `count`) VALUES (@v_timestamp, 'test_02', 0);
    INSERT INTO `test_debug` (`timestamp`, `table_name`, `count`) VALUES (@v_timestamp, 'test_03', 0);

    SET @v_count_01    = 1;
    SET @v_finished_01 = 0;

    REPEAT

      SET @v_test_01_key = MD5(RAND());

      INSERT INTO `test_01`
        (
          `column_01`, `column_02`, `column_03`, `column_04`, `column_05`,
          `column_06`, `column_07`, `column_08`, `column_09`, `column_10`,
          `column_11`, `column_12`, `column_13`, `column_14`, `column_15`,
          `column_16`, `column_17`, `column_18`, `column_19`, `column_20`,
          `column_21`, `column_22`, `column_23`, `column_24`, `column_25`
        )
      VALUES
        (
          @v_test_01_key, MD5(RAND()), CURRENT_TIMESTAMP(), RAND() * 1000, ROUND(RAND() * 1000, 0),
          MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()),
          MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()),
          MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()),
          MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND())
        );

      SET @v_count_02    = 1;
      SET @v_finished_02 = 0;

      REPEAT

        SET @v_test_02_key = MD5(RAND());

        INSERT INTO `test_02`
          (
            `column_01`, `column_02`, `column_03`, `column_04`, `column_05`,
            `column_06`, `column_07`, `column_08`, `column_09`, `column_10`,
            `column_11`, `column_12`, `column_13`, `column_14`, `column_15`,
            `column_16`, `column_17`, `column_18`, `column_19`, `column_20`,
            `column_21`, `column_22`, `column_23`, `column_24`, `column_25`
          )
        VALUES
          (
            @v_test_01_key, @v_test_02_key, CURRENT_TIMESTAMP(), RAND() * 1000, ROUND(RAND() * 1000, 0),
            MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()),
            MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()),
            MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()),
            MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND())
          );

        SET @v_count_03    = 1;
        SET @v_finished_03 = 0;

        REPEAT

          INSERT INTO `test_03`
            (
              `column_01`, `column_02`, `column_03`, `column_04`, `column_05`,
              `column_06`, `column_07`, `column_08`, `column_09`, `column_10`,
              `column_11`, `column_12`, `column_13`, `column_14`, `column_15`,
              `column_16`, `column_17`, `column_18`, `column_19`, `column_20`,
              `column_21`, `column_22`, `column_23`, `column_24`, `column_25`
            )
          VALUES
            (
              @v_test_01_key, @v_test_02_key, CURRENT_TIMESTAMP(), RAND() * 1000, ROUND(RAND() * 1000, 0),
              MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()),
              MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()),
              MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()),
              MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND()), MD5(RAND())
            );

          SET @v_count_03 = @v_count_03 + 1;

          IF @v_count_03 > 369 THEN
            UPDATE `test_debug` SET `count` = `count` + 369 WHERE `timestamp` = @v_timestamp AND `table_name` = 'test_03';
            SET @v_finished_03 = 1;
          END IF;

        UNTIL @v_finished_03 END REPEAT;

        SET @v_count_02 = @v_count_02 + 1;
        IF @v_count_02 > 369 THEN SET @v_finished_02 = 1; END IF;
        UPDATE `test_debug` SET `count` = `count` + 1 WHERE `timestamp` = @v_timestamp AND `table_name` = 'test_02';

      UNTIL @v_finished_02 END REPEAT;

      SET @v_count_01 = @v_count_01 + 1;
      IF @v_count_01 > 369 THEN
        SET @v_finished_01 = 1;
        UPDATE `test_debug` SET `finish_at` = CURRENT_TIMESTAMP() WHERE `timestamp` = @v_timestamp;
      END IF;
      UPDATE `test_debug` SET `count` = `count` + 1 WHERE `timestamp` = @v_timestamp AND `table_name` = 'test_01';

    UNTIL @v_finished_01 END REPEAT;

  END $$

DELIMITER ;

CALL `generate_data`();

/*
CREATE INDEX `c1` ON `test_01` (`column_01`);
CREATE INDEX `c1c2` ON `test_02` (`column_01`, `column_02`);
CREATE INDEX `c1c2` ON `test_03` (`column_01`, `column_02`);
*/

DROP PROCEDURE IF EXISTS `generate_data`;
