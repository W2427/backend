USE saint_whale_tasks;
DROP PROCEDURE IF EXISTS `set_parent_entity_id_on_wbs_entries`;
delimiter ;;
CREATE DEFINER=`backend`@`%` PROCEDURE `set_parent_entity_id_on_wbs_entries`(IN v_project_id BIGINT(20))
BEGIN

    -- 更新四级计划实体的所属管线实体 ID 和所属单管实体 ID
    UPDATE
      `wbs_entry` AS `wbs`
      INNER JOIN `project_node` AS `pn`
        ON `pn`.`entity_id` = `wbs`.`entity_id`
        AND `pn`.`deleted` = 0
    SET
      `wbs`.`iso_entity_id` = `pn`.`iso_entity_id`,
      `wbs`.`iso_no` = `pn`.`iso_no`,
      `wbs`.`spool_entity_id` = `pn`.`spool_entity_id`,
      `wbs`.`spool_no` = `pn`.`spool_no`
    WHERE
      `wbs`.`project_id` = v_project_id
    ;

  END

;;
delimiter ;
