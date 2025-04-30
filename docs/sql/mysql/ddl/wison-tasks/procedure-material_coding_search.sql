USE saint_whale_tasks;
-- ----------------------------
-- Procedure structure for material_coding_search
-- ----------------------------
DROP PROCEDURE IF EXISTS `material_coding_search`;
delimiter ;;
CREATE DEFINER=`oseadmin`@`%` PROCEDURE `material_coding_search`(IN input_code VARCHAR(100), OUT out_code VARCHAR(500))
BEGIN
    DECLARE f_code_length INT DEFAULT 0;
    DECLARE f_code  VARCHAR(64);
    DECLARE template_id VARCHAR(100);
    DECLARE f_code_desc  VARCHAR(100);
    DECLARE f_description VARCHAR(100);
    DECLARE temp_input VARCHAR(64);
    DECLARE least_input VARCHAR(64);
    DECLARE temp_out VARCHAR(500);
    DECLARE temp_d_out VARCHAR(500);

    DECLARE done INT DEFAULT FALSE;
    -- 游标
    DECLARE template CURSOR FOR SELECT d.`code_length`,c.`code`,d.`template_id`,d.`code_desc`,c.`description`
  FROM mat_coding_template_detail d LEFT JOIN mat_code c ON d.`template_detail_id` = c.`template_detail_id` WHERE d.`section_no` = 1;

    -- 将结束标志绑定到游标
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;


    SET temp_out = '';

    OPEN  template;
     read_loop: LOOP
            -- 取值 取多个字段
            FETCH  NEXT FROM template INTO f_code_length,f_code,template_id,f_code_desc,f_description;
            IF done THEN
                LEAVE read_loop;
            END IF;

     SET temp_input = SUBSTRING(input_code, 1, f_code_length);
  IF (temp_input = f_code) THEN

     SET least_input = SUBSTRING(input_code, f_code_length+1);

      SELECT f_description INTO temp_d_out;

      CALL material_coding_search_detail(least_input,template_id,@aaa);

      SELECT CONCAT(temp_d_out,@aaa) INTO temp_d_out;

            IF(CHAR_LENGTH(temp_d_out)>CHAR_LENGTH(temp_out)) THEN

             SET temp_out = temp_d_out;

            END IF;

  END IF;

    END LOOP;


    CLOSE template;

    SELECT temp_out INTO out_code;
END;
;;
delimiter ;
