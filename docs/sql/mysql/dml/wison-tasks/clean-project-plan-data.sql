USE `ose_tasks`;
SET @v_project_id='BRFSJG8AA394UXOJ';
DELETE FROM `wbs_entry_relation`    WHERE `project_id` = @v_project_id;
DELETE FROM `wbs_entry`            WHERE `project_id` = @v_project_id AND `type` = 'ENTITY';
DELETE FROM `bpm_activity_instance_base` WHERE `project_id` = @v_project_id;
