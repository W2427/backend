USE `saint_whale_tasks`;
-- ----------------------------
-- Procedure structure for set_parent_entity_id_on_wbs_structure_entries
-- ----------------------------
DROP PROCEDURE IF EXISTS `set_parent_entity_id_on_wbs_structure_entries`;
delimiter ;;
CREATE DEFINER=`oseadmin`@`%` PROCEDURE `set_parent_entity_id_on_wbs_structure_entries`(IN v_project_id BIGINT(20))
BEGIN

    -- 更新四级计划实体的所属结构实体 ID 和所属 wp01 02 03 04
    UPDATE
      `wbs_entry` AS `wbs`
      INNER JOIN `project_node` AS `pn`
        ON `pn`.`entity_id` = `wbs`.`entity_id`
        AND `pn`.`deleted` = 0
    SET
      `wbs`.`wp01_no` = `pn`.`wp01_no`,
      `wbs`.`wp01_id` = `pn`.`wp01_id`,
			`wbs`.`wp02_no` = `pn`.`wp02_no`,
      `wbs`.`wp02_id` = `pn`.`wp02_id`,
			`wbs`.`wp03_no` = `pn`.`wp03_no`,
      `wbs`.`wp03_id` = `pn`.`wp03_id`,
			`wbs`.`wp04_no` = `pn`.`wp04_no`,
      `wbs`.`wp04_id` = `pn`.`wp04_id`
    WHERE
      `wbs`.`project_id` = v_project_id
    ;

  END;
;;
delimiter ;
