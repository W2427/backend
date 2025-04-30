USE saint_whale_tasks;
-- ----------------------------
-- Procedure structure for shift_pipe_piece
-- ----------------------------
DROP PROCEDURE IF EXISTS `shift_pipe_piece`;
delimiter ;;
CREATE DEFINER=`oseadmin`@`%` PROCEDURE `shift_pipe_piece`(
																												IN userName varchar(50),
																												IN spOldName varchar(50),
																												IN spNewName varchar(50),
																												IN ppName varchar(50),
																												IN newRev varchar(50),
																												IN projectName varchar(50))
leave_proc:
BEGIN
  #Routine body goes here...

-- 	declare oldWelderId varchar(50);
	declare newWelderId BIGINT(20);
	declare orgId BIGINT(20));
	declare projectId BIGINT(20);
	declare userId BIGINT(20);
	declare ppId BIGINT(20);
	declare spNewId BIGINT(20);
	declare ppNewName varchar(50);
	declare ppNo varchar(50);
	declare ppHierarchyParentId BIGINT(20);
	declare pnId BIGINT(20); #pp的project_nodes 的Id
	declare spNewISOHnId BIGINT(20);
	declare spNewISOHnPath varchar(2000);
	declare spNewLpHnId BIGINT(20);
	declare spNewLpHnPath varchar(2000);

	-- 取得projectId，orgId
	select id,org_id into projectId,orgId from projects where name=@projectName and deleted=0;

	-- pp 移动 oldSpoolName to new SpoolName 在系统中

-- 	select concat('select id into userId from saint_whale_auth.users where username = ',userName);


	-- 取得用户ID
-- 	select id into userId from entity_weld where id='BRI5N2V5NWW55LQI';
-- 	select ;
	select id into userId from saint_whale_auth.users where saint_whale_auth.users.username = 'fengtieji';
-- 	select userId is null;

	if isnull(userId) then
		select 'step1';
		leave leave_proc;
	end if;
-- 		select 'fyx-ftj';

	-- 根据PP的no，取得pp_id
	select id,display_name into ppId,ppNo from entity_pipe_piece where no=@ppName and project_id = projectId and deleted=0;
-- 		select ppName,ppId,ppNo;
		if isnull(ppId) then
		select 'step2';
		leave leave_proc;
		end if;

	-- 取得SP_new在entity_spools中的id，sp_new_id和sp_new_no。
	select id into spNewId from entity_spool where no = spNewName and project_id = projectId and deleted=0;
		if 'step3' then
		#select @userName;
		leave leave_proc;
		end if;

		--
-- 		select 'fftj';

	-- 建立新的 PP的名字为 SP_NEW+P+000NO。
	select concat(spNewName,substr('-P0000',1,6-length(ppNo)),ppNo) into ppNewName;
 	select ppNewName;

	-- 取得pp在hierarchy_nodes中的“LAYER_PACKAGE”（hierarchy_type）的 parent_id。
-- 	select hn.parent_id,pn.id into ppHierarchyParentId,pnId from entity_pipe_piece epp left join project_node pn on pn.entity_id = epp.id left join hierarchy_node hn on hn.node_id = pn.id and hn.hierarchy_type = 'LAYER_PACKAGE' where pn.project_id = projectId and epp.id=ppId;
-- 	select ppHierarchyParentId;

	-- 新SP的hierarchyNode的id和path
		select hn.id,hn.path into spNewISOHnId,spNewISOHnPath from hierarchy_node hn left join project_node pn on hn.node_id = pn.id and hn.hierarchy_type = 'ISO' where pn.no = spNewName and pn.project_id = projectId;

	-- 新SP的hierarchyNode的Layer维度的id和path
		select hn.id,hn.path into spNewLpHnId,spNewLpHnPath from hierarchy_node hn left join project_node pn on hn.node_id = pn.id and hn.hierarchy_type = 'LAYER_PACKAGE' where pn.no = spNewName and pn.project_id = projectId;
	-- pp的project_nodes的id
		select id into pnId from project_node where entity_id = ppId;

	-- 更新pp的hierarchy_nodes上ISO的parent_id和path
		update hierarchy_node set parent_id = spNewISOHnId,path = concat(spNewISOHnPath,spNewISOHnId,'/') where node_id = pnId and hierarchy_type = 'ISO' and project_id = projectId;

	-- 更新pp的hierarchy_nodes 上 LAYER_PACKAGE的parent_id 和path
		update hierarchy_node set parent_id = spNewLpHnId,path = concat(spNewLpHnPath,spNewLpHnId,'/') where node_id = pnId and hierarchy_type = 'LAYER_PACKAGE' and project_id = projectId;


	-- 更新PP在entity_pipe_pieces中的no，为SP_new+P+000No。
	update entity_pipe_piece set no = ppNewName,last_modified_at = now(),revision = @newRev where id = ppId;

	-- 更新 wbs_entries中的parent_hierarchy_node_id,spool_no,spool_entity_id
	update wbs_entry set parent_hierarchy_node_id = spNewLpHnId,spool_no=spNewName,spool_entity_id=spNewId,name=ppNewName where project_id = projectId and entity_id = ppId;

	-- 更新bpm_cutting_enttiy表中的Spool_no，Spool_id
	update bpm_cutting_entity set pipe_piece_entity_no=ppNewName, spool_entity_no=spNewName,spool_entity_id=spNewId where pipe_piece_entity_id=ppId;

	-- 更新bpm_delivery_entity表中的pp_no为新名称。
	update bpm_delivery_entity set entity_no = ppNewName where entity_id = ppId;

	-- 修改project_node的node_no
	update project_node set no = ppNewName,spool_no=spNewName,spool_entity_id=spNewId where id = pnId;

	-- 更新entity_qr_code的entity_no
	update entity_qr_code set entity_no = ppNewName where entity_id = ppId;

	--
	select 'ftj';

END;
;;
delimiter ;
