USE saint_whale_tasks;
-- ----------------------------
-- Procedure structure for shift_welder
-- ----------------------------
DROP PROCEDURE IF EXISTS `shift_welder`;
delimiter ;;
CREATE DEFINER=`oseadmin`@`%` PROCEDURE `shift_welder`(
																												IN newWelderNo varchar(50),
																												IN projectName varchar(50),
																												IN weldNo varchar(100))
leave_proc:
BEGIN
  #Routine body goes here...

-- 	declare oldWelderId varchar(50);
	declare newWelderId BIGINT(20);
	declare orgId BIGINT(20);
	declare projectId BIGINT(20);

-- 	#取得projectId，orgId
	select id,org_id into projectId,orgId from projects where name=@projectName and deleted=0;

-- 	#取得用户ID
-- 	select id into oldWelderId from welder where no = olderWelderNo and deleted=0 and project_id = projectId;
-- 	if oldWelderId is NULL then
-- 		#select @userName;
-- 		leave leave_proc;
-- 	end if;

-- 	#取得用户ID
	select id into newWelderId from welder where no = newWelderNo and deleted=0 and project_id = projectId;
	if newWelderId is NULL then
		#select @userName;
		leave leave_proc;
	end if;


-- 	#修改焊工号
-- 	select * from entity_weld where no = weldNo and project_id = projectId;
	update entity_weld set welder_id = newWelderId where no = weldNo and project_id = projectId;




END;
;;
delimiter ;
