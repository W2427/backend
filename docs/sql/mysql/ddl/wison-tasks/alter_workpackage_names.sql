-- -----------------------------------------------------------------------------
-- 更改工作包 表名 work_package -> task_package
-- -----------------------------------------------------------------------------

USE `saint_whale_tasks`;

ALTER TABLE work_package_assign_node_privileges RENAME TO task_package_assign_node_privilege;

ALTER TABLE work_package_assign_site_teams RENAME TO task_package_assign_site_team;

ALTER TABLE work_package_categories RENAME TO task_package_category;

ALTER TABLE work_package_category_process_relations RENAME TO task_package_category_process_relation;

ALTER TABLE work_package_drawing_relations RENAME TO task_package_drawing_relation;

ALTER TABLE work_package_entity_relations RENAME TO task_package_entity_relation;

-- ALTER TABLE work_package_percent RENAME TO task_package_percent;

ALTER TABLE work_packages RENAME TO task_package;

