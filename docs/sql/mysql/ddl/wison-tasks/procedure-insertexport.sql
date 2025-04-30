USE saint_whale_tasks;
-- ----------------------------
-- Procedure structure for INSERTEXPORT
-- ----------------------------
DROP PROCEDURE IF EXISTS `INSERTEXPORT`;
delimiter ;;
CREATE DEFINER=`oseadmin`@`%` PROCEDURE `INSERTEXPORT`(IN userName varchar(50),
																												IN projectName varchar(50),
																												IN viewName varchar(100),
																												IN viewDesc varchar(500),
																												IN async tinyint(1))
leave_proc:
BEGIN
  #Routine body goes here...

	declare userId varchar(50);
	declare orgId varchar(50);
	declare projectId varchar(50);

	#取得projectId，orgId
	select id,org_id into projectId,orgId from projects where name=@projectName and deleted=0;

	#取得用户ID
	select id into userId from saint_whale_auth.users where users.username=userName and deleted=0;
	if userId is NULL then
		#select @userName;
		leave leave_proc;
	end if;

	#插入值到export_excel
	insert into export_excel (id,created_at,last_modified_at,status,created_by,deleted,deleted_at,
						deleted_by,last_modified_by,version,excel_desc,excel_view_name,org_id,project_id,async,generating)
			values (generate_entity_id(),now(),now(),'ACTIVE',userId,0,NULL,
						NULL,userId,1,viewDesc,viewName,orgId,projectId,async,false);


END;
;;
delimiter ;
