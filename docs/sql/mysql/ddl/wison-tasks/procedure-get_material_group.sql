USE saint_whale_tasks;
-- ----------------------------
-- Procedure structure for get_material_group
-- ----------------------------
DROP PROCEDURE IF EXISTS `get_material_group`;
delimiter ;;
CREATE DEFINER=`backend`@`%` PROCEDURE `get_material_group`(IN userName varchar(50),IN projectName varchar(50))
leave_proc:
BEGIN
  #Routine body goes here...

	declare done int default false; #自定义控制游标循环变量，默认false
	declare userId varchar(50);
	declare orgId varchar(50);
	declare projectId varchar(50);
	declare materialCodeAlias varchar(500);
	declare groupCode varchar(50);
	declare entityId varchar(50);

	declare mycur1 cursor for select material_code_alias,group_code
		from material_code_alias_group where project_id=projectId and deleted=0;

	declare mycur2 cursor for select id
		from entity_weld where project_id=projectId and deleted=0 and material_group_code is NULL;

	declare continue handler for NOT FOUND set done =true; #绑定控制变量到游标


	#取得projectId，orgId
	select id,org_id into projectId,orgId from projects where name=@projectName and deleted=0;

	if projectId is NULL then
		leave leave_proc;
	end if;

	#取得用户ID
	select id into userId from saint_whale_auth.users where users.username=userName and deleted=0;
	if userId is NULL then
		#select @userName;
		leave leave_proc;
	end if;

  #select templateId,sectionNo;
    OPEN mycur1;
    myloop:LOOP #开始循环

      fetch mycur1 into materialCodeAlias,groupCode;
      if done then
        leave myloop;
      end if;

			OPEN mycur2;
			myloop1:LOOP #开始循环

      fetch mycur2 into entityId;
      if done then
        leave myloop1;
      end if;

				#更新记录
				update entity_weld set material_group_code = groupCode,last_modified_at = now() where material_group_code is null and project_id = projectId and org_id= orgId and deleted=0 and material1 like concat('%',materialCodeAlias,'%');

			end loop myloop1;
      CLOSE mycur2;
			set done=false;


    end loop myloop;

    CLOSE mycur1;

END;
;;
delimiter ;
