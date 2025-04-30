USE saint_whale_tasks;
DROP PROCEDURE IF EXISTS `set_deleted_parent_entity_id_on_structure_entities`;
delimiter ;;
CREATE DEFINER=`backend`@`%` PROCEDURE `set_deleted_parent_entity_id_on_structure_entities`(IN v_project_id BIGINT(20),
																																															IN v_wp_name VARCHAR(20),
																																															IN v_wp_id BIGINT(20))
Lable:BEGIN

		-- WP05 不需要设置
			if v_wp_name <> 'wp01' and v_wp_name <> 'wp02' and v_wp_name <> 'wp03' and v_wp_name <> 'wp04' then
				leave Lable;
			end if;

		-- ****************** WP 01 设置 id ************************
    -- 设置WP01的项目节点的wp0? id no 字段为NULL

		set @v_tb :=CONCAT("UPDATE
      `entity_wp01` AS `wp01`
      INNER JOIN `project_node` AS `pn`
        ON `wp01`.`id` = `pn`.`entity_id`
				AND `wp01`.`project_id` = `pn`.`project_id`
    SET
			`pn`.`",v_wp_name,"_id` = NULL,
			`pn`.`",v_wp_name,"_no` = NULL
    WHERE
      `wp01`.`project_id` = ? AND `pn`.`",v_wp_name,"_id` = ? AND `wp01`.`deleted` = 0
    ");
		SET @p=v_project_id;
		SET @d=v_wp_id;
		PREPARE stmt from @v_tb;
    EXECUTE stmt USING @p,@d;
    DEALLOCATE PREPARE stmt;


		-- *********** WP 02 设置 id *********************
    -- 设置WP02的项目节点的wp0? id no 字段为NULL

    set @v_tb :=CONCAT("UPDATE
      `entity_wp02` AS `wp02`
      INNER JOIN `project_node` AS `pn`
        ON `wp02`.`id` = `pn`.`entity_id`
				AND `wp02`.`project_id` = `pn`.`project_id`
    SET
			`pn`.`",v_wp_name,"_id` = NULL,
			`pn`.`",v_wp_name,"_no` = NULL
    WHERE
      `wp02`.`project_id` = ? AND `pn`.`",v_wp_name,"_id` = ? AND `wp02`.`deleted` = 0
    ");

		SET @p=v_project_id;
		SET @d=v_wp_id;
		PREPARE stmt from @v_tb;
    EXECUTE stmt USING @p,@d;
    DEALLOCATE PREPARE stmt;

		-- ****************** WP 03 设置 id ************************
    -- 设置WP03的项目节点的wp0? id no 字段为NULL

		set @v_tb :=CONCAT("UPDATE
      `entity_wp03` AS `wp03`
      INNER JOIN `project_node` AS `pn`
        ON `wp03`.`id` = `pn`.`entity_id`
				AND `wp03`.`project_id` = `pn`.`project_id`
    SET
			`pn`.`",v_wp_name,"_id` = NULL,
			`pn`.`",v_wp_name,"_no` = NULL
    WHERE
      `wp03`.`project_id` = ? AND `pn`.`",v_wp_name,"_id` = ? AND `wp03`.`deleted` = 0
    ");

		SET @p=v_project_id;
		SET @d=v_wp_id;
		PREPARE stmt from @v_tb;
    EXECUTE stmt USING @p,@d;
    DEALLOCATE PREPARE stmt;

		-- ****************** WP 04 设置 id ************************
    -- 设置WP04的项目节点的wp0? id no 字段为NULL

		set @v_tb :=CONCAT("UPDATE
      `entity_wp04` AS `wp04`
      INNER JOIN `project_node` AS `pn`
        ON `wp04`.`id` = `pn`.`entity_id`
				AND `wp04`.`project_id` = `pn`.`project_id`
    SET
			`pn`.`",v_wp_name,"_id` = NULL,
			`pn`.`",v_wp_name,"_no` = NULL
    WHERE
      `wp04`.`project_id` = ? AND `pn`.`",v_wp_name,"_id` = ? AND `wp04`.`deleted` = 0
    ");

		SET @p=v_project_id;
		SET @d=v_wp_id;
		PREPARE stmt from @v_tb;
    EXECUTE stmt USING @p,@d;
    DEALLOCATE PREPARE stmt;


  END

;;
delimiter ;
