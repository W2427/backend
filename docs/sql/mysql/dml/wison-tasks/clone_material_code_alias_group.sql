/*******************************************************************************
  拷贝现存材料代码和材料分组对应关系

  DATE: 2018-12-03
 ******************************************************************************/

DROP PROCEDURE IF EXISTS `ose_tasks`.`CLONE_MATERIAL_CODE_ALIAS_GROUP`;

CREATE PROCEDURE `ose_tasks`.`CLONE_MATERIAL_CODE_ALIAS_GROUP`(IN oldProjectId varchar(16), IN newProjectId varchar(16),IN userId varchar(16))
BEGIN

DECLARE done INT DEFAULT FALSE;
declare _company_id varchar(16);
declare _oldOrgId varchar(50);
declare _newOrgId varchar(50);
declare _id varchar(16);
declare _new_id varchar(16);
declare _new_group_id varchar(16);
declare _group_code varchar(16);
declare mycur cursor for select id, group_code from material_code_alias_group
	where project_Id=oldProjectId;


DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = true;


select org_id into _oldOrgId from projects where id=oldProjectId;
select org_id, company_id into _newOrgId, _company_id from projects where id=newProjectId;


	OPEN mycur;
	myloop:LOOP #开始循环

		fetch mycur into _id,_group_code;
		IF done THEN
            LEAVE myloop;
        END IF;

        select id into _new_group_id from wps_base_metal_group where org_id = _newOrgId and project_id = newProjectId and code = _group_code;

        select generate_entity_id() into _new_id from dual;

		#增加记录
		INSERT INTO `material_code_alias_group`
			SELECT	_new_id,
					current_timestamp(),
					current_timestamp(),
					`status`,
					userId,
					`deleted`,
					`deleted_at`,
					`deleted_by`,
					userId,
					REPLACE(UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)), '.', ''), -- version
					`material_code_alias`,
					_new_group_id,
					_group_code,
					_newOrgId,
					newProjectId,
					`remark`,
					_company_id
			FROM `material_code_alias_group` where project_id = oldProjectId and org_id = _oldOrgId and id = _id;

		#记录新老ID对照
		insert into clone_tmp (old_id,new_id,table_name,new_project_id) values(_id,_new_id,"material_code_alias_group",newProjectId);


	end loop myloop;

	CLOSE mycur;

END
