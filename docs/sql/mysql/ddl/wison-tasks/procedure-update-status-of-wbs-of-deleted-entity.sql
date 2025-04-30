-- -----------------------------------------------------------------------------
-- 更新已删除的实体的四级计划的状态。
--
-- 对于已删除的实体，若存在【未删除】且【执行中】的 WBS 则将实体标记为【已取消】；
-- 对于四级计划，若未运行则将其标记为【已删除】，否则将其标记为【已停用】。
--
-- 数据库类型：MySQL
-- 数据库　　：saint_whale_tasks
-- 创建时间　：2019-02-20
-- 创建者　　：FTJ
-- -----------------------------------------------------------------------------

USE `saint_whale_tasks`;

DROP PROCEDURE IF EXISTS `update_status_of_wbs_of_deleted_entity`;

DELIMITER $$

CREATE PROCEDURE `update_status_of_wbs_of_deleted_entity`(
  IN v_project_id BIGINT(20),
  IN v_table_name VARCHAR(255),
  IN v_entity_id  BIGINT(20)
)
  BEGIN

    SET @v_project_id = v_project_id;
    SET @v_entity_id = v_entity_id;


    -- 对于四级计划，若已运行则将其标记为【已停用】
    SET @v_wbs_inactive_sql = CONCAT(
      'UPDATE wbs_entry AS w INNER JOIN ',
      v_table_name,
      ' AS e ON e.id = w.entity_id AND e.deleted = 1 SET w.active = 0, w.deleted = 1, ',
      'w.status = \'SUSPEND\', ',
      'w.deleted_at = CURRENT_TIMESTAMP(), w.guid = CONCAT(w.guid, \'[SUSPENDED:\', UNIX_TIMESTAMP(), \']\')',
       ' WHERE w.entity_id = ? AND w.deleted = 0 AND w.active = 1 AND w.running_status IS NOT NULL'
    );
    PREPARE v_wbs_inactive_statement FROM @v_wbs_inactive_sql;
    EXECUTE v_wbs_inactive_statement USING @v_entity_id;
    DEALLOCATE PREPARE v_wbs_inactive_statement;

  END $$

DELIMITER ;
