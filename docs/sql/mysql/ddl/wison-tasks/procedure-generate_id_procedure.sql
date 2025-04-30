USE saint_whale_tasks;
-- ----------------------------
-- Procedure structure for generate_id_procedure
-- ----------------------------
DROP PROCEDURE IF EXISTS `generate_id_procedure`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generate_id_procedure`()
BEGIN
		DECLARE cur_id bigint(19);
		DECLARE _TABLE_SCHEMA VARCHAR(100);
		DECLARE _TABLE_NAME VARCHAR(100);
		DECLARE _COLUMN_NAME VARCHAR(100);
		DECLARE done INT DEFAULT FALSE; -- 自定义控制游标循环变量，默认false



    DECLARE mycur CURSOR FOR SELECT TABLE_SCHEMA, TABLE_NAME, COLUMN_NAME FROM `saint_whale_test`.`columns_new` limit 0,9000; -- 查询字符串

		DECLARE CONTINUE handler FOR NOT FOUND set done =true; -- 绑定控制变量到游标

		SET @tmp_id := FLOOR(UNIX_TIMESTAMP(CURTIME(6)) * 1000000000);


		SET @cur_milli := FLOOR(UNIX_TIMESTAMP(CURTIME(6)) * 1000000);
		SET @cur_seq := 1;

	  OPEN mycur;
    myloop:LOOP -- 开始循环

      FETCH mycur INTO _TABLE_SCHEMA,_TABLE_NAME,_COLUMN_NAME;
      if done then
        leave myloop;
      END if;
--       			select @cur_seq;

			IF @cur_milli = FLOOR(UNIX_TIMESTAMP(CURTIME(6)) * 1000000) THEN
			SELECT @cur_seq;
				IF @cur_seq < 999 THEN
					SET @cur_seq := (@cur_seq + 1);
					select @cur_seq;

				ELSEIF @cur_seq = 999 THEN
					select benchmark(100,sha(1));
					SET @cur_milli := FLOOR(UNIX_TIMESTAMP(CURTIME(6)) * 1000000);
					SET @cur_seq := 1;

				END IF;

			ELSE
						SET @cur_milli := FLOOR(UNIX_TIMESTAMP(CURTIME(6)) * 1000000);

			END IF;


			SET cur_id := @cur_milli * 1000 + @cur_seq;

			UPDATE `saint_whale_test`.`columns_new` set id = cur_id where TABLE_SCHEMA = _TABLE_SCHEMA AND TABLE_NAME = _TABLE_NAME and COLUMN_NAME = _COLUMN_NAME;

    END loop myloop;

    CLOSE mycur;

END;
;;
delimiter ;

