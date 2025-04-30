USE `saint_whale_tasks`;
-- ----------------------------
-- Procedure structure for set_parent_entity_id_on_structure_entity
-- ----------------------------
DROP PROCEDURE IF EXISTS `set_parent_entity_id_on_structure_entity`;
delimiter ;;
CREATE DEFINER=`backend`@`%` PROCEDURE `set_parent_entity_id_on_structure_entity`(IN v_project_id BIGINT(20),
																																										IN v_wp_name VARCHAR(32), -- wp01 wp02 wp03 wp04
																																										IN v_wp_id BIGINT(20))
BEGIN

		IF v_wp_name = 'wp01' THEN
			-- WP01 需要设置
			SET @v_tb :=CONCAT("UPDATE
				`entity_wp01` AS `wp01`
				JOIN `project_node` AS `pn`
					ON `wp01`.`id` = `pn`.`entity_id`
					AND `wp01`.`project_id` = `pn`.`project_id`
			SET
				`wp01`.`wp01_id` = `wp01`.`id`,
				`wp01`.`wp01_no` = `wp01`.`no`,
				`pn`.`wp01_id` = `wp01`.`id`,
				`pn`.`wp01_no` = `wp01`.`no`
			WHERE
				`wp01`.`project_id` = ? AND `wp01`.`deleted` = 0 AND `wp01`.`",v_wp_name,"_id` = ?
			");

			SET @p=v_project_id;
			SET @d=v_wp_id;
			PREPARE stmt from @v_tb;
			EXECUTE stmt USING @p,@d;
			DEALLOCATE PREPARE stmt;

		END IF;

		IF v_wp_name = 'wp01' OR v_wp_name = 'wp02' THEN
				-- *********** WP 02 设置 id *********************
				-- 设置WP02的项目节点的wp01 id 字段为wp01对应的实体 ID
				set @v_tb :=CONCAT("UPDATE
					`entity_wp02` AS `wp02`
					JOIN `project_node` AS `pn`
						ON `wp02`.`id` = `pn`.`entity_id`
						AND `wp02`.`project_id` = `pn`.`project_id`
					JOIN `hierarchy_node` AS `hn`
						ON `hn`.`node_id` = `pn`.`id`
					JOIN `hierarchy_node` AS `phn`
						ON `hn`.`parent_id` = `phn`.`id`
					JOIN `project_node` AS `ppn`
						ON `ppn`.`id` = `phn`.`node_id`
				SET
					`wp02`.`wp01_id` = `ppn`.`wp01_id`,
					`wp02`.`wp01_no` = `ppn`.`wp01_no`,
					`wp02`.`wp02_id` = `wp02`.`id`,
					`wp02`.`wp02_no` = `wp02`.`no`,
					`pn`.`wp01_id` = `ppn`.`wp01_id`,
					`pn`.`wp01_no` = `ppn`.`wp01_no`,
					`pn`.`wp02_id` = `wp02`.`id`,
					`pn`.`wp02_no` = `wp02`.`no`
				WHERE
					`wp02`.`project_id` = ? AND `wp02`.`deleted` = 0 AND `wp02`.`",v_wp_name,"_id` = ?
				");

				SET @p=v_project_id;
				SET @d=v_wp_id;
				PREPARE stmt from @v_tb;
				EXECUTE stmt USING @p,@d;
				DEALLOCATE PREPARE stmt;
		END IF;

		IF v_wp_name = 'wp01' OR v_wp_name = 'wp02' OR v_wp_name = 'wp03' THEN
				-- ****************** WP 03 设置 id ************************
				-- 设置WP03的项目节点的wp01 wp02 id 字段为wp01 wp02对应的实体 ID
						set @v_tb :=CONCAT("UPDATE
					`entity_wp03` AS `wp03`
					JOIN `project_node` AS `pn`
						ON `wp03`.`id` = `pn`.`entity_id`
						AND `wp03`.`project_id` = `pn`.`project_id`
					JOIN `hierarchy_node` AS `hn`
						ON `hn`.`node_id` = `pn`.`id`
					JOIN `hierarchy_node` AS `phn`
						ON `hn`.`parent_id` = `phn`.`id`
					JOIN `project_node` AS `ppn`
						ON `ppn`.`id` = `phn`.`node_id`
				SET
					`wp03`.`wp01_id` = `ppn`.`wp01_id`,
					`wp03`.`wp01_no` = `ppn`.`wp01_no`,
					`wp03`.`wp02_id` = `ppn`.`wp02_id`,
					`wp03`.`wp02_no` = `ppn`.`wp02_no`,
					`wp03`.`wp03_id` = `wp03`.`id`,
					`wp03`.`wp03_no` = `wp03`.`no`,
					`pn`.`wp01_id` = `ppn`.`wp01_id`,
					`pn`.`wp01_no` = `ppn`.`wp01_no`,
					`pn`.`wp02_id` = `ppn`.`wp02_id`,
					`pn`.`wp02_no` = `ppn`.`wp02_no`,
					`pn`.`wp03_id` = `wp03`.`id`,
					`pn`.`wp03_no` = `wp03`.`no`
				WHERE
					`wp03`.`project_id` = ? AND `wp03`.`deleted` = 0 AND `wp03`.`",v_wp_name,"_id` = ?
				");

				SET @p=v_project_id;
				SET @d=v_wp_id;
				PREPARE stmt from @v_tb;
				EXECUTE stmt USING @p,@d;
				DEALLOCATE PREPARE stmt;
		END IF;

		IF v_wp_name = 'wp01' OR v_wp_name = 'wp02' OR v_wp_name = 'wp03' OR v_wp_name = 'wp04' THEN
				-- ****************** WP 04 设置 id ************************
				-- 设置WP04的项目节点的wp01 wp02 wp03 id 字段为wp01 wp02 wp03对应的实体 ID
				set @v_tb :=CONCAT("UPDATE
					`entity_wp04` AS `wp04`
					JOIN `project_node` AS `pn`
						ON `wp04`.`id` = `pn`.`entity_id`
						AND `wp04`.`project_id` = `pn`.`project_id`
					JOIN `hierarchy_node` AS `hn`
						ON `hn`.`node_id` = `pn`.`id`
					JOIN `hierarchy_node` AS `phn`
						ON `hn`.`parent_id` = `phn`.`id`
					JOIN `project_node` AS `ppn`
						ON `ppn`.`id` = `phn`.`node_id`
				SET
					`wp04`.`wp01_id` = `ppn`.`wp01_id`,
					`wp04`.`wp01_no` = `ppn`.`wp01_no`,
					`wp04`.`wp02_id` = `ppn`.`wp02_id`,
					`wp04`.`wp02_no` = `ppn`.`wp02_no`,
					`wp04`.`wp03_id` = `ppn`.`wp03_id`,
					`wp04`.`wp03_no` = `ppn`.`wp03_no`,
					`wp04`.`wp04_id` = `wp04`.`id`,
					`wp04`.`wp04_no` = `wp04`.`no`,
					`pn`.`wp01_id` = `ppn`.`wp01_id`,
					`pn`.`wp01_no` = `ppn`.`wp01_no`,
					`pn`.`wp02_id` = `ppn`.`wp02_id`,
					`pn`.`wp02_no` = `ppn`.`wp02_no`,
					`pn`.`wp03_id` = `ppn`.`wp03_id`,
					`pn`.`wp03_no` = `ppn`.`wp03_no`,
					`pn`.`wp04_id` = `wp04`.`id`,
					`pn`.`wp04_no` = `wp04`.`no`
				WHERE
					`wp04`.`project_id` = ? AND `wp04`.`deleted` = 0 AND `wp04`.`",v_wp_name,"_id` = ?
				");

				SET @p=v_project_id;
				SET @d=v_wp_id;
				PREPARE stmt from @v_tb;
				EXECUTE stmt USING @p,@d;
				DEALLOCATE PREPARE stmt;
		END IF;

		IF v_wp_name = 'wp01' OR v_wp_name = 'wp02' OR v_wp_name = 'wp03' OR v_wp_name = 'wp04' THEN
				-- ****************** WP 05 设置 id ************************
				-- 设置WP05的项目节点的wp01 id 字段为wp01对应的实体 ID
				set @v_tb :=CONCAT("UPDATE
					`entity_wp05` AS `wp05`
					JOIN `project_node` AS `pn`
						ON `wp05`.`id` = `pn`.`entity_id`
						AND `wp05`.`project_id` = `pn`.`project_id`
					JOIN `hierarchy_node` AS `hn`
						ON `hn`.`node_id` = `pn`.`id`
					JOIN `hierarchy_node` AS `phn`
						ON `hn`.`parent_id` = `phn`.`id`
					JOIN `project_node` AS `ppn`
						ON `ppn`.`id` = `phn`.`node_id`
				SET
					`wp05`.`wp01_id` = `ppn`.`wp01_id`,
					`wp05`.`wp01_no` = `ppn`.`wp01_no`,
					`wp05`.`wp02_id` = `ppn`.`wp02_id`,
					`wp05`.`wp02_no` = `ppn`.`wp02_no`,
					`wp05`.`wp03_id` = `ppn`.`wp03_id`,
					`wp05`.`wp03_no` = `ppn`.`wp03_no`,
					`wp05`.`wp04_id` = `ppn`.`wp04_id`,
					`wp05`.`wp04_no` = `ppn`.`wp04_no`,
					`pn`.`wp01_id` = `ppn`.`wp01_id`,
					`pn`.`wp01_no` = `ppn`.`wp01_no`,
					`pn`.`wp02_id` = `ppn`.`wp02_id`,
					`pn`.`wp02_no` = `ppn`.`wp02_no`,
					`pn`.`wp03_id` = `ppn`.`wp03_id`,
					`pn`.`wp03_no` = `ppn`.`wp03_no`,
					`pn`.`wp04_id` = `ppn`.`wp04_id`,
					`pn`.`wp04_no` = `ppn`.`wp04_no`
				WHERE
					`wp05`.`project_id` = ? AND `wp05`.`deleted` = 0 AND `wp05`.`",v_wp_name,"_id` = ?
				");

				SET @p=v_project_id;
				SET @d=v_wp_id;
				PREPARE stmt from @v_tb;
				EXECUTE stmt USING @p,@d;
				DEALLOCATE PREPARE stmt;
		END IF;

		IF v_wp_name = 'wp05' THEN
				-- ****************** WP 05 设置 id ************************
				-- 设置WP05的项目节点的wp01 id 字段为wp01对应的实体 ID
				set @v_tb :=CONCAT("UPDATE
					`entity_wp05` AS `wp05`
					JOIN `project_node` AS `pn`
						ON `wp05`.`id` = `pn`.`entity_id`
						AND `wp05`.`project_id` = `pn`.`project_id`
					JOIN `hierarchy_node` AS `hn`
						ON `hn`.`node_id` = `pn`.`id`
					JOIN `hierarchy_node` AS `phn`
						ON `hn`.`parent_id` = `phn`.`id`
					JOIN `project_node` AS `ppn`
						ON `ppn`.`id` = `phn`.`node_id`
				SET
					`wp05`.`wp01_id` = `ppn`.`wp01_id`,
					`wp05`.`wp01_no` = `ppn`.`wp01_no`,
					`wp05`.`wp02_id` = `ppn`.`wp02_id`,
					`wp05`.`wp02_no` = `ppn`.`wp02_no`,
					`wp05`.`wp03_id` = `ppn`.`wp03_id`,
					`wp05`.`wp03_no` = `ppn`.`wp03_no`,
					`wp05`.`wp04_id` = `ppn`.`wp04_id`,
					`wp05`.`wp04_no` = `ppn`.`wp04_no`,
					`pn`.`wp01_id` = `ppn`.`wp01_id`,
					`pn`.`wp01_no` = `ppn`.`wp01_no`,
					`pn`.`wp02_id` = `ppn`.`wp02_id`,
					`pn`.`wp02_no` = `ppn`.`wp02_no`,
					`pn`.`wp03_id` = `ppn`.`wp03_id`,
					`pn`.`wp03_no` = `ppn`.`wp03_no`,
					`pn`.`wp04_id` = `ppn`.`wp04_id`,
					`pn`.`wp04_no` = `ppn`.`wp04_no`
				WHERE
					`wp05`.`project_id` = ? AND `wp05`.`deleted` = 0 AND `wp05`.`id` = ?
				");

				SET @p=v_project_id;
				SET @d=v_wp_id;
				PREPARE stmt from @v_tb;
				EXECUTE stmt USING @p,@d;
				DEALLOCATE PREPARE stmt;
		END IF;


		IF v_wp_name = 'wp01' OR v_wp_name = 'wp02' OR v_wp_name = 'wp03' OR v_wp_name = 'wp04' THEN
				-- ****************** WP Misc 设置 id ************************
				-- 设置WP Misc 的项目节点的wp01/2/3/4 id 字段为wp01对应的实体 ID
				SET @v_tb :=CONCAT("UPDATE
					`entity_wp_misc` AS `wpmisc`
					JOIN `project_node` AS `pn`
						ON `wpmisc`.`id` = `pn`.`entity_id`
						AND `wpmisc`.`project_id` = `pn`.`project_id`
					JOIN `hierarchy_node` AS `hn`
						ON `hn`.`node_id` = `pn`.`id`
					JOIN `hierarchy_node` AS `phn`
						ON `hn`.`parent_id` = `phn`.`id`
					JOIN `project_node` AS `ppn`
						ON `ppn`.`id` = `phn`.`node_id`
				SET
					`wpmisc`.`wp01_id` = `ppn`.`wp01_id`,
					`wpmisc`.`wp01_no` = `ppn`.`wp01_no`,
					`wpmisc`.`wp02_id` = `ppn`.`wp02_id`,
					`wpmisc`.`wp02_no` = `ppn`.`wp02_no`,
					`wpmisc`.`wp03_id` = `ppn`.`wp03_id`,
					`wpmisc`.`wp03_no` = `ppn`.`wp03_no`,
					`wpmisc`.`wp04_id` = `ppn`.`wp04_id`,
					`wpmisc`.`wp04_no` = `ppn`.`wp04_no`,
					`pn`.`wp01_id` = `ppn`.`wp01_id`,
					`pn`.`wp01_no` = `ppn`.`wp01_no`,
					`pn`.`wp02_id` = `ppn`.`wp02_id`,
					`pn`.`wp02_no` = `ppn`.`wp02_no`,
					`pn`.`wp03_id` = `ppn`.`wp03_id`,
					`pn`.`wp03_no` = `ppn`.`wp03_no`,
					`pn`.`wp04_id` = `ppn`.`wp04_id`,
					`pn`.`wp04_no` = `ppn`.`wp04_no`
				WHERE
					`wpmisc`.`project_id` = ? AND `wpmisc`.`deleted` = 0 AND `wpmisc`.`",v_wp_name,"_id` = ?
				");

				SET @p=v_project_id;
				SET @d=v_wp_id;
				PREPARE stmt from @v_tb;
				EXECUTE stmt USING @p,@d;
				DEALLOCATE PREPARE stmt;
		END IF;

		IF v_wp_name = 'wp_misc' THEN
				-- ****************** WP Misc 设置 id ************************
				-- 设置WP Misc 的项目节点的wp01/2/3/4 id 字段为wp01对应的实体 ID
				SET @v_tb :=CONCAT("UPDATE
					`entity_wp_misc` AS `wpmisc`
					JOIN `project_node` AS `pn`
						ON `wpmisc`.`id` = `pn`.`entity_id`
						AND `wpmisc`.`project_id` = `pn`.`project_id`
					JOIN `hierarchy_node` AS `hn`
						ON `hn`.`node_id` = `pn`.`id`
					JOIN `hierarchy_node` AS `phn`
						ON `hn`.`parent_id` = `phn`.`id`
					JOIN `project_node` AS `ppn`
						ON `ppn`.`id` = `phn`.`node_id`
				SET
					`wpmisc`.`wp01_id` = `ppn`.`wp01_id`,
					`wpmisc`.`wp01_no` = `ppn`.`wp01_no`,
					`wpmisc`.`wp02_id` = `ppn`.`wp02_id`,
					`wpmisc`.`wp02_no` = `ppn`.`wp02_no`,
					`wpmisc`.`wp03_id` = `ppn`.`wp03_id`,
					`wpmisc`.`wp03_no` = `ppn`.`wp03_no`,
					`wpmisc`.`wp04_id` = `ppn`.`wp04_id`,
					`wpmisc`.`wp04_no` = `ppn`.`wp04_no`,
					`pn`.`wp01_id` = `ppn`.`wp01_id`,
					`pn`.`wp01_no` = `ppn`.`wp01_no`,
					`pn`.`wp02_id` = `ppn`.`wp02_id`,
					`pn`.`wp02_no` = `ppn`.`wp02_no`,
					`pn`.`wp03_id` = `ppn`.`wp03_id`,
					`pn`.`wp03_no` = `ppn`.`wp03_no`,
					`pn`.`wp04_id` = `ppn`.`wp04_id`,
					`pn`.`wp04_no` = `ppn`.`wp04_no`
				WHERE
					`wpmisc`.`project_id` = ? AND `wpmisc`.`deleted` = 0 AND `wpmisc`.`id` = ?
				");

				SET @p=v_project_id;
				SET @d=v_wp_id;
				PREPARE stmt from @v_tb;
				EXECUTE stmt USING @p,@d;
				DEALLOCATE PREPARE stmt;
		END IF;

		IF v_wp_name = 'wp01' OR v_wp_name = 'wp02' OR v_wp_name = 'wp03' OR v_wp_name = 'wp04' THEN
				-- ****************** structure weld 设置 id ************************
				-- 设置structure weld 的项目节点的wp01/2/3/4 id 字段为wp01对应的实体 ID
				SET @v_tb :=CONCAT("UPDATE
					`entity_structure_weld` AS `sw`
					JOIN `project_node` AS `pn`
						ON `sw`.`id` = `pn`.`entity_id`
						AND `sw`.`project_id` = `pn`.`project_id`
					JOIN `hierarchy_node` AS `hn`
						ON `hn`.`node_id` = `pn`.`id`
					JOIN `hierarchy_node` AS `phn`
						ON `hn`.`parent_id` = `phn`.`id`
					JOIN `project_node` AS `ppn`
						ON `ppn`.`id` = `phn`.`node_id`
				SET
					`sw`.`wp01_id` = `ppn`.`wp01_id`,
					`sw`.`wp01_no` = `ppn`.`wp01_no`,
					`sw`.`wp02_id` = `ppn`.`wp02_id`,
					`sw`.`wp02_no` = `ppn`.`wp02_no`,
					`sw`.`wp03_id` = `ppn`.`wp03_id`,
					`sw`.`wp03_no` = `ppn`.`wp03_no`,
					`sw`.`wp04_id` = `ppn`.`wp04_id`,
					`sw`.`wp04_no` = `ppn`.`wp04_no`,
					`pn`.`wp01_id` = `ppn`.`wp01_id`,
					`pn`.`wp01_no` = `ppn`.`wp01_no`,
					`pn`.`wp02_id` = `ppn`.`wp02_id`,
					`pn`.`wp02_no` = `ppn`.`wp02_no`,
					`pn`.`wp03_id` = `ppn`.`wp03_id`,
					`pn`.`wp03_no` = `ppn`.`wp03_no`,
					`pn`.`wp04_id` = `ppn`.`wp04_id`,
					`pn`.`wp04_no` = `ppn`.`wp04_no`
				WHERE
					`sw`.`project_id` = ? AND `sw`.`deleted` = 0 AND `sw`.`",v_wp_name,"_id` = ?
				");

				SET @p=v_project_id;
				SET @d=v_wp_id;
				PREPARE stmt from @v_tb;
				EXECUTE stmt USING @p,@d;
				DEALLOCATE PREPARE stmt;
		END IF;

		IF v_wp_name = 'structure_weld' THEN
				-- ****************** WP Misc 设置 id ************************
				-- 设置WP Misc 的项目节点的wp01/2/3/4 id 字段为wp01对应的实体 ID
				SET @v_tb :=CONCAT("UPDATE
					`entity_structure_weld` AS `sw`
					JOIN `project_node` AS `pn`
						ON `sw`.`id` = `pn`.`entity_id`
						AND `sw`.`project_id` = `pn`.`project_id`
					JOIN `hierarchy_node` AS `hn`
						ON `hn`.`node_id` = `pn`.`id`
					JOIN `hierarchy_node` AS `phn`
						ON `hn`.`parent_id` = `phn`.`id`
					JOIN `project_node` AS `ppn`
						ON `ppn`.`id` = `phn`.`node_id`
				SET
					`sw`.`wp01_id` = `ppn`.`wp01_id`,
					`sw`.`wp01_no` = `ppn`.`wp01_no`,
					`sw`.`wp02_id` = `ppn`.`wp02_id`,
					`sw`.`wp02_no` = `ppn`.`wp02_no`,
					`sw`.`wp03_id` = `ppn`.`wp03_id`,
					`sw`.`wp03_no` = `ppn`.`wp03_no`,
					`sw`.`wp04_id` = `ppn`.`wp04_id`,
					`sw`.`wp04_no` = `ppn`.`wp04_no`,
					`pn`.`wp01_id` = `ppn`.`wp01_id`,
					`pn`.`wp01_no` = `ppn`.`wp01_no`,
					`pn`.`wp02_id` = `ppn`.`wp02_id`,
					`pn`.`wp02_no` = `ppn`.`wp02_no`,
					`pn`.`wp03_id` = `ppn`.`wp03_id`,
					`pn`.`wp03_no` = `ppn`.`wp03_no`,
					`pn`.`wp04_id` = `ppn`.`wp04_id`,
					`pn`.`wp04_no` = `ppn`.`wp04_no`
				WHERE
					`sw`.`project_id` = ? AND `sw`.`deleted` = 0 AND `sw`.`id` = ?
				");

				SET @p=v_project_id;
				SET @d=v_wp_id;
				PREPARE stmt from @v_tb;
				EXECUTE stmt USING @p,@d;
				DEALLOCATE PREPARE stmt;
		END IF;

  END;
;;
delimiter ;
