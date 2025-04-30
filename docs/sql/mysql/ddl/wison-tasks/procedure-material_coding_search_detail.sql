USE saint_whale_tasks;
-- ----------------------------
-- Procedure structure for material_coding_search_detail
-- ----------------------------
DROP PROCEDURE IF EXISTS `material_coding_search_detail`;
delimiter ;;
CREATE DEFINER=`oseadmin`@`%` PROCEDURE `material_coding_search_detail`(IN input_code VARCHAR(100), IN template_id VARCHAR(100),OUT out_code VARCHAR(500))
BEGIN
    DECLARE code_length INT DEFAULT 0;
    DECLARE section_no INT DEFAULT 0;
    DECLARE codee  VARCHAR(64);
    DECLARE code_desc  VARCHAR(100);
    DECLARE discipline_lib  VARCHAR(100);
    DECLARE description VARCHAR(100);
    DECLARE temp_input VARCHAR(64);
    DECLARE least_input VARCHAR(64);
    DECLARE biz_desc VARCHAR(64);
    DECLARE d_section_no INT DEFAULT 2;
    DECLARE temp_out VARCHAR(500);
    DECLARE done INT DEFAULT FALSE;
    -- 游标
    DECLARE template CURSOR FOR SELECT d.`code_length`,c.`code`,c.`description`,d.`code_desc`,d.`section_no`,d.`discipline_lib`
  FROM mat_coding_template_detail d LEFT JOIN mat_code c ON d.`template_detail_id` = c.`template_detail_id`
  WHERE d.`template_id`= template_id AND  d.`section_no`>1 ORDER BY d.`section_no`;

    -- 将结束标志绑定到游标
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    SET least_input = input_code;
    SET temp_out = '';

    OPEN  template;
     read_loop: LOOP
            -- 取值 取多个字段
            FETCH  NEXT FROM template INTO code_length,codee,description,code_desc,section_no,discipline_lib;
            IF done THEN
                LEAVE read_loop;
            END IF;



           IF (d_section_no = section_no) THEN

    IF CHAR_LENGTH(least_input)<code_length THEN
      SET done = TRUE;
    ELSE
      SET temp_input = SUBSTRING(least_input, 1, code_length);

      IF discipline_lib IS NOT NULL THEN

        SET d_section_no = d_section_no + 1;

        SET least_input = SUBSTRING(least_input, code_length+1);

        SELECT IFNULL(t.short_desc,t.long_desc) INTO biz_desc  FROM mat_biz_code t WHERE t.`biz_table` = discipline_lib AND t.biz_code = temp_input;

        IF biz_desc IS NOT NULL THEN

          SELECT CONCAT(temp_out,',',biz_desc) INTO temp_out;

          SET biz_desc = NULL;
        ELSE

          SELECT CONCAT(temp_input,least_input) INTO least_input;
          SET done = TRUE;
        END IF;

      ELSEIF(temp_input = codee) THEN

        SET least_input = SUBSTRING(least_input, code_length+1);

        SET d_section_no = d_section_no + 1;

        SELECT CONCAT(temp_out,',',description) INTO temp_out;
      END IF;
    END IF;
          END IF;


    END LOOP;


    CLOSE template;

SELECT CONCAT(temp_out,'`',least_input)INTO out_code;

END;
;;
delimiter ;
