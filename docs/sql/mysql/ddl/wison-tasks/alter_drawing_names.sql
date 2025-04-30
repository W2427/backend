-- -----------------------------------------------------------------------------
-- 更改图纸文件名
-- drawing_list_piping                  drawing
-- drawing_list_piping_detail           drawing_list_detail
-- drawing_piping_histories             drawing_history
-- drawing_sub_piping                   drawing_sub
-- drawing_sub_piping_config            drawing_sub_config
-- drawing_sub_piping_histories         drawing_sub_histories
-- drawing_sub_piping_review_opinions   drawing_sub_review_opinions
-- detail_design_list                   detail_design_drawing
-- detail_design_list_detail            detail_design_drawing_detail
-- -----------------------------------------------------------------------------

USE `saint_whale_tasks`;

DELIMITER $$

ALTER TABLE drawing_list_piping RENAME TO drawing;

ALTER TABLE drawing_list_piping_detail RENAME TO drawing_detail;

ALTER TABLE drawing_piping_histories RENAME TO drawing_history;

ALTER TABLE drawing_sub_piping RENAME TO sub_drawing;

ALTER TABLE drawing_sub_piping_config RENAME TO sub_drawing_config;

ALTER TABLE drawing_sub_piping_histories RENAME TO sub_drawing_history;

ALTER TABLE drawing_sub_piping_review_opinions RENAME TO sub_drawing_review_opinion;

ALTER TABLE detail_design_list RENAME TO detail_design_drawing;

ALTER TABLE detail_design_list_detail RENAME TO detail_design_drawing_detail;

ALTER TABLE `saint_whale_tasks`.`drawing_detail`
CHANGE COLUMN `drawing_list_piping_id` `drawing_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `design_change_review_form`;
DELIMITER ;
