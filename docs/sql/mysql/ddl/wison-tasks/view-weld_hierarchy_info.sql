USE `saint_whale_tasks`;
-- -----------------------------------------------------------------------------
-- 焊口层级信息
-- View structure for weld_hierarchy_info
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `weld_hierarchy_info`  AS
    SELECT
        `module_pn`.`no`                            AS  `module_parent_no`,
        `parent_pn_on_piping`.`no`            AS  `parent_no`,
        `hn_on_piping`.`path`                 AS  `path_on_piping`,

        `hn_on_ptp`.`path`                    AS  `path_on_ptp`,
        `hn_on_cp`.`path`                     AS  `path_on_cp`,

        `hn_on_ss`.`path`                     AS  `path_on_ss`,
        `hn_on_piping`.`id`                   AS  `hierarchy_id_on_piping`,
        `parent_hn_on_piping`.`id`            AS  `parent_hierarchy_id_on_piping`,


        `parent_pn_on_piping`.`entity_type`   AS `parent_entity_type`,
        `parent_pn_on_piping`.`entity_id`     AS `parent_entity_id`,
        `w`.*
    FROM
        `entity_weld`                              AS  `w`
--  查找焊口节点信息
    INNER JOIN `project_node`                      AS  `pn`
        ON `pn`.`project_id`                       =   `w`.`project_id`
	    AND   `pn`.`org_id`                        =   `w`.`org_id`
        AND `pn`.`entity_type`                      =   'WELD_JOINT'
        AND `pn`.`deleted`                          =    0
        AND `pn`.`discipline`                       =   "PIPING"
	    AND   `pn`.`entity_id`                        =   `w`.`id`

    LEFT JOIN `hierarchy_node`                     AS  `hn_on_piping`
        ON `hn_on_piping`.`project_id`              =   `pn`.`project_id`
        AND `pn`.`id`                               =   `hn_on_piping`.`node_id`
        AND `hn_on_piping`.`hierarchy_type`         =   "PIPING"
        AND `hn_on_piping`.`deleted`                =    FALSE

    LEFT JOIN `hierarchy_node`                     AS  `parent_hn_on_piping`
      ON `hn_on_piping`.`parent_id`                 =   `parent_hn_on_piping`.`id`
      AND `parent_hn_on_piping`.`project_id`        =   `hn_on_piping`.`project_id`
      AND `parent_hn_on_piping`.`hierarchy_type`    =   "PIPING"
      AND `parent_hn_on_piping`.`deleted`           =   FALSE

    LEFT JOIN `project_node`                       AS  `parent_pn_on_piping`
      ON `parent_pn_on_piping`.`id`                 =   `parent_hn_on_piping`.`node_id`
        AND `parent_pn_on_piping`.`project_id`      =   `parent_hn_on_piping`.`project_id`
        AND `parent_pn_on_piping`.`deleted`         =   FALSE

    LEFT JOIN `hierarchy_node`                     AS  `hn_on_ptp`
      ON `hn_on_ptp`.`project_id`                   =   `pn`.`project_id`
        AND `pn`.`id`                               =   `hn_on_ptp`.`node_id`
        AND `hn_on_ptp`.`hierarchy_type`            =   'PRESSURE_TEST_PACKAGE'
        AND `hn_on_ptp`.`deleted`                   =   FALSE

    LEFT JOIN `hierarchy_node`                     AS  `hn_on_cp`
      ON `hn_on_cp`.`project_id`                    =   `pn`.`project_id`
        AND `pn`.`id`                               =   `hn_on_cp`.`node_id`
        AND `hn_on_cp`.`hierarchy_type`             =   'CLEAN_PACKAGE'
        AND `hn_on_cp`.`deleted`                    =   FALSE

    LEFT JOIN `hierarchy_node`                     AS  `hn_on_ss`
      ON `hn_on_ss`.`project_id`                    =   `pn`.`project_id`
        AND `pn`.`id`                               =   `hn_on_ss`.`node_id`
        AND `hn_on_ss`.`hierarchy_type`             =   'SUB_SYSTEM'
        AND `hn_on_ss`.`deleted`                    =   FALSE

    LEFT JOIN `hierarchy_node`                     AS  `module_hn_on_piping`
      ON `module_hn_on_piping`.`path`               LIKE CONCAT(`hn_on_piping`.`path`,`hn_on_piping`.`id`,'%')
        AND `module_hn_on_piping`.`project_id`      =   `hn_on_piping`.`project_id`
        AND `module_hn_on_piping`.`hierarchy_type`  =   "PIPING"
        AND `module_hn_on_piping`.`deleted`         =   FALSE

    LEFT JOIN `project_node`                       AS  `module_pn`
      ON `module_pn`.`id`                           =   `module_hn_on_piping`.`node_id`
        AND `module_pn`.`project_id`                =   `module_hn_on_piping`.`project_id`
        AND `module_pn`.`node_type`                 =   'MODULE'
        AND `module_pn`.`deleted`                   =   FALSE
;
