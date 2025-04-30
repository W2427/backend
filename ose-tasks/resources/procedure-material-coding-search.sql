DROP PROCEDURE IF EXISTS `material_coding_search_detail`;
DELIMITER ;;
CREATE PROCEDURE `material_coding_search_detail`(IN input_code VARCHAR(100), IN template_id VARCHAR(100),OUT out_code VARCHAR(500))
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

END
;;
DELIMITER ;



DROP PROCEDURE IF EXISTS `material_coding_search`;
DELIMITER ;;
CREATE PROCEDURE `material_coding_search`(IN input_code VARCHAR(100), OUT out_code VARCHAR(500))
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
END
;;
DELIMITER ;
