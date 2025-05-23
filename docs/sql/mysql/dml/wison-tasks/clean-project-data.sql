USE `ose_tasks`;
SET @v_project_id='BRABYOC2NA8PLR5L';
DELETE FROM `wbs_entry_relation`          WHERE `project_id` = @v_project_id;
DELETE FROM `wbs_entry`                  WHERE `project_id` = @v_project_id;
DELETE FROM `entity_components`            WHERE `project_id` = @v_project_id;
DELETE FROM `entity_pipe_piece`           WHERE `project_id` = @v_project_id;
DELETE FROM `entity_weld`                 WHERE `project_id` = @v_project_id;
DELETE FROM `entity_spool`                WHERE `project_id` = @v_project_id;
DELETE FROM `entity_iso`                  WHERE `project_id` = @v_project_id;
DELETE FROM `entity_pressure_test_package` WHERE `project_id` = @v_project_id;
DELETE FROM `entity_clean_package`         WHERE `project_id` = @v_project_id;
DELETE FROM `entity_sub_system`            WHERE `project_id` = @v_project_id;
DELETE FROM `hierarchy_node`              WHERE `project_id` = @v_project_id;
DELETE FROM `project_node`                WHERE `project_id` = @v_project_id;
DELETE FROM `batch_task`                  WHERE `project_id` = @v_project_id;
