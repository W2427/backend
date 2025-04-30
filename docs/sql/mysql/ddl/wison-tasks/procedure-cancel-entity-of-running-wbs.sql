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

DROP PROCEDURE IF EXISTS `cancel_entity_of_running_wbs`;

DELIMITER $$

CREATE PROCEDURE `cancel_entity_of_running_wbs`(
  IN v_project_id BIGINT(20),
  IN v_table_name VARCHAR(255),
  IN v_entity_id  BIGINT(20)
)
  BEGIN

    SET @v_project_id = v_project_id;
    SET @v_entity_id = v_entity_id;

    -- 对于已删除的实体，若存在【未删除】且【执行中】的 WBS 则将实体标记为【已取消】
    SET @v_entity_cancel_sql = CONCAT(
      'UPDATE ',
      v_table_name,
      ' AS e INNER JOIN wbs_entry AS w ON w.entity_id = e.id AND w.deleted = 0 AND w.running_status IS NOT NULL SET e.cancelled = 1 WHERE e.id = ? AND e.deleted = 1 AND e.cancelled = 0'
    );
    PREPARE v_entity_cancel_statement FROM @v_entity_cancel_sql;
    EXECUTE v_entity_cancel_statement USING @v_entity_id;
    DEALLOCATE PREPARE v_entity_cancel_statement;

  END $$

DELIMITER ;
