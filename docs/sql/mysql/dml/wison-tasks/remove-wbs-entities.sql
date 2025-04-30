/*******************************************************************************
  删除项目的实体信息及其层级结构信息。

  DATE: 2018-10-19
 ******************************************************************************/

SET @@sql_safe_updates = 0;

DROP PROCEDURE IF EXISTS `ose_tasks`.`remove_wbs_entities`;

DELIMITER $$

CREATE PROCEDURE `ose_tasks`.`remove_wbs_entities`(IN v_project_id VARCHAR(16))
BEGIN

  DECLARE v_hierarchy_node_id VARCHAR(16);
  DECLARE v_project_node_id VARCHAR(16);
  DECLARE v_finished INTEGER DEFAULT 0;

  DECLARE hierarchy_cursor CURSOR FOR (
    SELECT
      `hn`.`id` AS `hierarchy_node_id`,
      `pn`.`id` AS `project_node_id`
    FROM
      `ose_tasks`.`hierarchy_node` AS `hn`
      INNER JOIN `ose_tasks`.`project_node` AS `pn`
        ON `pn`.`id` = `hn`.`node_id`
        AND `pn`.`node_type` = 'ENTITY'
    WHERE
      `hn`.`project_id` = v_project_id
  );

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_finished = 1;

  OPEN hierarchy_cursor;

    REPEAT
      FETCH hierarchy_cursor INTO v_hierarchy_node_id, v_project_node_id;
      DELETE FROM `ose_tasks`.`hierarchy_node` WHERE `id` = v_hierarchy_node_id;
      DELETE FROM `ose_tasks`.`project_node` WHERE `id` = v_project_node_id;
    UNTIL v_finished END REPEAT;

  CLOSE hierarchy_cursor;

  DELETE FROM `ose_tasks`.`entity_components` WHERE `project_id` = v_project_id;
  DELETE FROM `ose_tasks`.`entity_pipe_piece` WHERE `project_id` = v_project_id;
  DELETE FROM `ose_tasks`.`entity_weld` WHERE `project_id` = v_project_id;
  DELETE FROM `ose_tasks`.`entity_spool` WHERE `project_id` = v_project_id;
  DELETE FROM `ose_tasks`.`entity_iso` WHERE `project_id` = v_project_id;

END$$

DELIMITER ;

CALL `ose_tasks`.`remove_wbs_entities`('BP0OQ21PSQCJTYON');

DROP PROCEDURE `ose_tasks`.`remove_wbs_entities`;
