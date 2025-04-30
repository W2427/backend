CREATE
    ALGORITHM = UNDEFINED
    DEFINER = `backend`@`%`
    SQL SECURITY DEFINER
VIEW `ose_tasks`.`iso_test_result` AS
    SELECT
        `ei`.`id` AS `id`,
        `ei`.`org_id` AS `org_id`,
        `ei`.`project_id` AS `project_id`,
        `ei`.`no` AS `iso_no`,
        `ei`.`design_pressure_text` AS `design_pressure`,
        `ei`.`design_temperature_text` AS `design_temperature`,
        `ei`.`nps_text` AS `nps`,
        `ei`.`insulation_code` AS `insulation_code`,
        `lc`.`test_result` AS `line_check_result`,
        `lc`.`comment` AS `line_check_comment`,
        `lc`.`id` AS `line_check_id`,
        `bf`.`test_result` AS `blind_Fabricate_result`,
        `bf`.`comment` AS `blind_Fabricate_comment`,
        `bf`.`id` AS `blind_Fabricate_id`,
        `bi`.`test_result` AS `blind_install_result`,
        `bi`.`comment` AS `blind_install_comment`,
        `bi`.`id` AS `blind_install_id`,
        `pt`.`test_result` AS `pressure_test_result`,
        `pt`.`comment` AS `pressure_test_comment`,
        `pt`.`id` AS `pressure_test_id`,
        `br`.`test_result` AS `blind_remove_result`,
        `br`.`comment` AS `blind_remove_comment`,
        `br`.`id` AS `blind_remove_id`,
        `blow`.`test_result` AS `blow_result`,
        `blow`.`comment` AS `blow_comment`,
        `blow`.`id` AS `blow_id`,
        `oil`.`test_result` AS `oil_flush_result`,
        `oil`.`comment` AS `oil_flush_comment`,
        `oil`.`id` AS `oil_flush_id`,
        `wf`.`test_result` AS `water_flush_result`,
        `wf`.`comment` AS `water_flush_comment`,
        `wf`.`id` AS `water_flush_id`,
        `sf`.`test_result` AS `shield_fabricate_result`,
        `sf`.`comment` AS `shield_fabricate_comment`,
        `sf`.`id` AS `shield_fabricate_id`,
        `fi`.`test_result` AS `filling_install_result`,
        `fi`.`comment` AS `filling_install_comment`,
        `fi`.`id` AS `filling_install_id`,
        `si`.`test_result` AS `shield_install_result`,
        `si`.`comment` AS `shield_install_comment`,
        `si`.`id` AS `shield_install_id`
    FROM
        (((((((((((`ose_tasks`.`entity_iso` `ei`
        LEFT JOIN `ose_tasks`.`iso_process_log` `lc` ON (((`lc`.`latest_test` = TRUE)
            AND (`ei`.`line_check_id` = `lc`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `bf` ON (((`bf`.`latest_test` = TRUE)
            AND (`ei`.`blind_fabricate_id` = `bf`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `bi` ON (((`bi`.`latest_test` = TRUE)
            AND (`ei`.`blind_install_id` = `bi`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `pt` ON (((`pt`.`latest_test` = TRUE)
            AND (`ei`.`pressure_test_id` = `pt`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `br` ON (((`br`.`latest_test` = TRUE)
            AND (`ei`.`blind_remove_id` = `br`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `blow` ON (((`blow`.`latest_test` = TRUE)
            AND (`ei`.`blow_id` = `blow`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `oil` ON (((`oil`.`latest_test` = TRUE)
            AND (`ei`.`oil_flush_id` = `oil`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `wf` ON (((`wf`.`latest_test` = TRUE)
            AND (`ei`.`water_flush_id` = `wf`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `sf` ON (((`sf`.`latest_test` = TRUE)
            AND (`ei`.`shield_fabricate_id` = `sf`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `fi` ON (((`fi`.`latest_test` = TRUE)
            AND (`ei`.`filling_install_id` = `fi`.`id`))))
        LEFT JOIN `ose_tasks`.`iso_process_log` `si` ON (((`si`.`latest_test` = TRUE)
            AND (`ei`.`shield_install_id` = `si`.`id`))))
    WHERE
        (`ei`.`deleted` = FALSE)
