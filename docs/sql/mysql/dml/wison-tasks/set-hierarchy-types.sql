/*******************************************************************************
  批量设置层级节点的层级类型。

  DATE: 2018-10-18

  区域的项目节点类型（Project Node Type）与层级类型（Hierarchy Type）的对应关系：
  ------------------------------------------------
    Project Node Type      Hierarchy Type
  ------------------------------------------------
    AREA                   AREA
    SUB_AREA               AREA
    MODULE                 AREA
    SUB_MODULE             AREA
    LAYER_PACKAGE          LAYER_PACKAGE
    PRESSURE_TEST_PACKAGE  PRESSURE_TEST_PACKAGE
    CLEAN_PACKAGE          CLEAN_PACKAGE
    SYSTEM                 SYSTEM
    SUB_SYSTEM             SYSTEM
    SECTOR                 SECTOR
    SUB_SECTOR             SECTOR
    FEATURES               FEATURES
  ------------------------------------------------

  实体的项目节点类型与层级类型的对应关系：
    • 父级为 AREA 的 ISO 的层级类型为 ISO
    • 其他实体的层级类型继承自父级

 ******************************************************************************/

SET @@max_sp_recursion_depth = 256;
SET @@sql_safe_updates = 0;

UPDATE `ose_tasks`.`hierarchy_node` SET `hierarchy_type` = NULL;

DROP PROCEDURE IF EXISTS `ose_tasks`.`set_hierarchy_type_of_areas`;
DROP PROCEDURE IF EXISTS `ose_tasks`.`set_hierarchy_type_of_isos`;
DROP PROCEDURE IF EXISTS `ose_tasks`.`set_hierarchy_type_of_entities`;

DELIMITER $$

/* 设置区域层级节点的层级类型 */
CREATE PROCEDURE `ose_tasks`.`set_hierarchy_type_of_areas`(
  IN v_hierarchy_type VARCHAR(45),
  IN v_project_node_type VARCHAR(45)
)
BEGIN
  UPDATE
    `ose_tasks`.`hierarchy_node` AS `hn`
  SET
    `hn`.`hierarchy_type` = v_hierarchy_type
  WHERE
    `hn`.`node_id` IN (
      SELECT
        `pn`.`id`
      FROM
        `ose_tasks`.`project_node` AS `pn`
      WHERE
        `pn`.`node_type` = v_project_node_type
    );
END$$

/* 设置区域下 ISO 层级节点的层级类型 */
CREATE PROCEDURE `ose_tasks`.`set_hierarchy_type_of_isos`()
BEGIN

  DECLARE v_hierarchy_node_id VARCHAR(16);
  DECLARE v_finished INTEGER DEFAULT 0;

  -- 取得上级为区域的 ISO 的层级节点
  DECLARE iso_cursor CURSOR FOR (
    SELECT
      `hn`.`id`
    FROM
      `ose_tasks`.`hierarchy_node` AS `hn`
      INNER JOIN `ose_tasks`.`project_node` AS `pn`
        ON `pn`.`id` = `hn`.`node_id`
        AND `pn`.`node_type` = 'ENTITY'
        AND `pn`.`entity_type` = 'ISO'
      INNER JOIN `ose_tasks`.`hierarchy_node` AS `phn`
        ON `phn`.`id` = `hn`.`parent_id`
        AND `phn`.`hierarchy_type` = "PIPING"
  );

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

  -- 将上级为区域的 ISO 的层级节点的层级类型设置为 ISO
  OPEN iso_cursor;

    REPEAT

      FETCH iso_cursor INTO v_hierarchy_node_id;

      UPDATE
        `ose_tasks`.`hierarchy_node` AS `hn`
      SET
        `hn`.`hierarchy_type` = 'ISO'
      WHERE
        `hn`.`id` = v_hierarchy_node_id
      ;

    UNTIL v_finished END REPEAT;

  CLOSE iso_cursor;

END$$

/* 设置实体层级节点的层级类型 */
CREATE PROCEDURE `ose_tasks`.`set_hierarchy_type_of_entities`()
BEGIN

  DECLARE v_entity_count INTEGER DEFAULT 0;
  DECLARE v_finished INTEGER DEFAULT 0;
  DECLARE v_hierarchy_node_id VARCHAR(16);
  DECLARE v_hierarchy_type VARCHAR(45);

  -- 取得除区域下的 ISO 的实体层级节点
  DECLARE entity_cursor CURSOR FOR (
    SELECT
      `hn`.`id`,
      `phn`.`hierarchy_type`
    FROM
      `ose_tasks`.`hierarchy_node` AS `hn`
      INNER JOIN `ose_tasks`.`project_node` AS `pn`
        ON `pn`.`id` = `hn`.`node_id`
        AND `pn`.`node_type` = 'ENTITY'
      INNER JOIN `ose_tasks`.`hierarchy_node` AS `phn`
        ON `phn`.`id` = `hn`.`parent_id`
        AND `phn`.`hierarchy_type` IS NOT NULL
    WHERE
      NOT (`pn`.`entity_type` = 'ISO' AND `phn`.`hierarchy_type` = "PIPING")
  );

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

  -- 将取得的实体层级节点的层级类型设置为父级层级节点的层级类型
  OPEN entity_cursor;

    REPEAT

      FETCH entity_cursor INTO v_hierarchy_node_id, v_hierarchy_type;

      UPDATE
        `ose_tasks`.`hierarchy_node` AS `hn`
      SET
        `hn`.`hierarchy_type` = v_hierarchy_type
      WHERE
        `hn`.`id` = v_hierarchy_node_id
      ;

    UNTIL v_finished END REPEAT;

  CLOSE entity_cursor;

  -- 检查是否存在未设置的实体层级节点
  SELECT
    `hn`.`id`
  FROM
    `ose_tasks`.`hierarchy_node` AS `hn`
    INNER JOIN `ose_tasks`.`project_node` AS `pn`
      ON `pn`.`id` = `hn`.`node_id`
      AND `pn`.`node_type` = 'ENTITY'
    INNER JOIN `ose_tasks`.`hierarchy_node` AS `phn`
      ON `phn`.`id` = `hn`.`parent_id`
      AND `phn`.`hierarchy_type` IS NOT NULL
  WHERE
    `hn`.`hierarchy_type` IS NULL
  ;

  SET v_entity_count = FOUND_ROWS();

  IF v_entity_count > 0 THEN
    CALL `ose_tasks`.`set_hierarchy_type_of_entities`();
  END IF;

END$$

DELIMITER ;

CALL `ose_tasks`.`set_hierarchy_type_of_areas`("PIPING", 'AREA');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`("PIPING", 'SUB_AREA');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`("PIPING", 'MODULE');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`("PIPING", 'SUB_MODULE');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`('LAYER_PACKAGE', 'LAYER_PACKAGE');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`('PRESSURE_TEST_PACKAGE', 'PRESSURE_TEST_PACKAGE');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`('CLEAN_PACKAGE', 'CLEAN_PACKAGE');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`('SYSTEM', 'SYSTEM');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`('SUB_SYSTEM', 'SYSTEM');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`('SECTOR', 'SECTOR');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`('SUB_SECTOR', 'SECTOR');
CALL `ose_tasks`.`set_hierarchy_type_of_areas`('FEATURES', 'FEATURES');
CALL `ose_tasks`.`set_hierarchy_type_of_isos`();
CALL `ose_tasks`.`set_hierarchy_type_of_entities`();

DROP PROCEDURE `ose_tasks`.`set_hierarchy_type_of_areas`;
DROP PROCEDURE `ose_tasks`.`set_hierarchy_type_of_isos`;
DROP PROCEDURE `ose_tasks`.`set_hierarchy_type_of_entities`;

/* 确认更新结果 */
SELECT
  `phn`.`hierarchy_type` AS `parent_hierarchy_type`,
  `hn`.`hierarchy_type`,
  `pn`.`node_type`,
  `pn`.`entity_type`,
  CASE
    WHEN `pn`.`node_type` = 'ENTITY' AND `pn`.`entity_type` = 'ISO' AND `phn`.`hierarchy_type` = "PIPING" AND `hn`.`hierarchy_type` <> 'ISO' THEN 'ERROR'
    WHEN `pn`.`node_type` = 'ENTITY' AND `pn`.`entity_type` <> 'ISO' AND `hn`.`hierarchy_type` <> `phn`.`hierarchy_type` THEN 'ERROR'
    ELSE 'OK'
  END AS `matched`
FROM
  `ose_tasks`.`hierarchy_node` AS `hn`
  INNER JOIN `ose_tasks`.`hierarchy_node` AS `phn`
    ON `phn`.`id` = `hn`.`parent_id`
  INNER JOIN `ose_tasks`.`project_node` AS `pn`
    ON `pn`.`id` = `hn`.`node_id`
ORDER BY
  `hn`.`hierarchy_type`
;
