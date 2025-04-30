package com.ose.tasks.domain.model.repository.bpm;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import com.ose.tasks.dto.TaskProcessDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.util.StringUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.SuspensionState;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;

/**
 * 任务查询。
 */
public class BpmActivityInstanceRepositoryImpl extends BaseRepository implements BpmActivityInstanceRepositoryCustom {

    private static final String NDT = "NDT";

    /**
     * 查询任务流程实例列表
     */
    @Override
    public Page<BpmActivityInstanceDTO> actInstList(Long orgId, Long projectId, ActInstCriteriaDTO criteria) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlCount = new StringBuilder();
        StringBuilder sqlCountWhere = new StringBuilder();
        Boolean isBpmRuTaskRequired = false;
        Boolean isBaisRequired = false;
        Boolean isBaibRequired = false;
        sql.append("SELECT ");
        sql.append("ac.id as id, ");
        sql.append("ac.created_at as createdAt, ");
        sql.append("ac.last_modified_at as lastModifiedAt, ");
        sql.append("ac.status as status, ");

        sql.append("ac.project_id as projectId, ");
        sql.append("ac.org_id as orgId, ");
        sql.append("ac.version as version, ");
        sql.append("acs.memo as memo, ");
        sql.append("ac.entity_no as entityNo, ");
        sql.append("ac.entity_no1 as entityNo1, ");
        sql.append("ac.entity_no2 as entityNo2, ");
        sql.append("d.document_title as drawingTitle, ");
        sql.append("d.discipline as discipline, ");
        sql.append("ac.entity_type as entityType, ");
        sql.append("ac.entity_id as entityId, ");
        sql.append("ac.entity_project_node_id as entityProjectNodeId, ");

        sql.append("ac.entity_module_name as entityModuleName, ");
        sql.append("ac.entity_module_project_node_id as entityModuleProjectNodeId, ");
        sql.append("ac.entity_type_id as entityTypeId, ");
        sql.append("ac.entity_sub_type as entitySubType, ");
        sql.append("ac.entity_sub_type_id as entitySubTypeId, ");
        sql.append("ac.process_stage_name as processStage, ");
        sql.append("ac.process_stage_id as processStageId, ");
        sql.append("ac.process_name as process, ");
        sql.append("ac.process_id as processId, ");

        sql.append("ac.owner_id as ownerId, ");
        sql.append("ac.owner_name as ownerName, ");
        sql.append("ac.allocatee as allocatee, ");
        sql.append("ac.allocatee_date as allocateeDate, ");
        sql.append("ac.plan_start_date as planStartDate, ");
        sql.append("ac.plan_end_date as planEndDate, ");
        sql.append("ac.task_package_plan_start_date as taskPackagePlanStartDate, ");
        sql.append("ac.task_package_plan_end_date as taskPackagePlanEndDate, ");
        sql.append("ac.plan_hour as planHour, ");
        sql.append("acs.start_date as startDate, ");
        sql.append("acs.end_date as endDate, ");
        sql.append("acs.cost_hour as costHour, ");

        sql.append("acs.suspension_state as suspensionState, ");
        sql.append("acs.finish_state as finishState, ");
        sql.append("ac.act_category as actCategory, ");
        sql.append("ac.process_category_id as processCategoryId, ");

        sql.append("acb.reports as reports, ");
        sql.append("acb.ex_ins_issue_ids as exInsIssueIds, ");
        sql.append("acb.in_ins_issue_ids as inInsIssueIds, ");

        sql.append("acs.execute_ng_flag as executeNgFlag, ");
        sql.append("acs.current_executor as currentExecutor, ");
        sql.append("acs.work_site_id as workSiteId, ");
        sql.append("acs.work_site_name as workSiteName, ");
        sql.append("acs.work_site_address as workSiteAddress, ");
        sql.append("acs.team_name as teamName, ");
        sql.append("acs.team_id as teamId, ");
        sql.append("pnt.no AS typeNo, ");
        sql.append("pnt.description AS typeDescription, ");
        sql.append("pnf.no AS functionNo, ");
        sql.append("pnf.description AS functionDescription, ");
        sql.append("acs.task_package_name as taskPackageName, ");
        sql.append("acs.task_package_id as taskPackageId, ");
        sql.append("acs.un_accept_count as unAcceptCount ");
        if (!StringUtils.isEmpty(criteria.getTaskDefKey())) {
            sql.append(",rt.task_def_key_ as taskDefKey ");
        }
        StringBuilder sqlFrom = new StringBuilder();
        StringBuilder countSqlFrom = new StringBuilder();
        sqlFrom.append("FROM bpm_activity_instance_base ac ");
        sqlCount.append("SELECT count(0) ");

        sqlCount.append("FROM bpm_activity_instance_base ac ");

        sqlFrom.append(" JOIN bpm_activity_instance_state acs ON ac.id = acs.bai_id  ");
        sqlFrom.append(" LEFT JOIN bpm_activity_instance_blob acb ON ac.id = acb.bai_id ");
        sqlFrom.append(" LEFT JOIN hierarchy_node_relation hnrt ON hnrt.entity_id = ac.entity_id AND hnrt.ancestor_entity_type = 'TYPE' " +
            "LEFT JOIN project_node pnt ON pnt.id = hnrt.node_ancestor_id " +
            "LEFT JOIN hierarchy_node_relation hnrf ON hnrf.entity_id = ac.entity_id AND hnrf.ancestor_entity_type = 'FUNCTION' " +
            "LEFT JOIN project_node pnf ON pnf.id = hnrf.node_ancestor_id ");
        sqlFrom.append(" LEFT JOIN drawing d ON ac.entity_id = d.id ");
        sqlCount.append(" LEFT JOIN hierarchy_node_relation hnrt ON hnrt.entity_id = ac.entity_id AND hnrt.ancestor_entity_type = 'TYPE' " +
            "LEFT JOIN project_node pnt ON pnt.id = hnrt.node_ancestor_id " +
            "LEFT JOIN hierarchy_node_relation hnrf ON hnrf.entity_id = ac.entity_id AND hnrf.ancestor_entity_type = 'FUNCTION' " +
            "LEFT JOIN project_node pnf ON pnf.id = hnrf.node_ancestor_id ");
//        countSqlFrom.append(" LEFT JOIN bpm_activity_instance_blob acb ON ac.id = acb.bai_id ");
        if (!StringUtils.isEmpty(criteria.getTaskDefKey())) {
            sqlFrom.append(" JOIN bpm_ru_task rt ON ac.id = rt.act_inst_id ");
//            countSqlFrom.append(" JOIN bpm_ru_task rt ON ac.act_inst_id = rt.act_inst_id ");
            isBpmRuTaskRequired = true;
        }

        sqlFrom.append("WHERE ac.project_id = :projectId ");
        countSqlFrom.append("WHERE ac.project_id = :projectId  ");


        if (criteria.getProcessStageId() != null
            && !criteria.getProcessStageId().equals(0L)) {
            sqlFrom.append("AND ac.process_stage_id = :processStageId ");
            countSqlFrom.append("AND ac.process_stage_id = :processStageId ");
        }


        if (criteria.getProcessId() != null
            && !criteria.getProcessId().equals(0L)) {
            sqlFrom.append("AND ac.process_id = :processId ");
            countSqlFrom.append("AND ac.process_id = :processId ");
        }

        if (criteria.getProcessName() != null) {
            sqlFrom.append("AND ac.process_name = :processNameCn ");
            countSqlFrom.append("AND ac.process_name = :processNameCn ");
        }

        if (criteria.getDiscipline() != null) {
            sqlFrom.append("AND d.discipline = :discipline ");
        }

        if (!StringUtils.isEmpty(criteria.getTaskDefKey())) {
            sqlFrom.append(" AND rt.task_def_key_ = :taskDefKey ");
            countSqlFrom.append(" AND rt.task_def_key_ = :taskDefKey ");
            isBpmRuTaskRequired = true;
        }

        if (criteria.getActInstIds() != null && criteria.getActInstIds().size() > 0) {
            sqlFrom.append("AND ac.id in :actInstIds ");
            countSqlFrom.append("AND ac.id in :actInstIds ");

        }


        if (criteria.getFunction() != null
            && !"".equals(criteria.getFunction())) {
            sqlFrom.append("AND ( pnf.no LIKE :function OR pnf.description LIKE :function ) ");
            countSqlFrom.append("AND ( pnf.no LIKE :function OR pnf.description LIKE :function ) ");
            isBaisRequired = true;
        }


        if (criteria.getType() != null
            && !"".equals(criteria.getType())) {
            sqlFrom.append("AND ( pnt.no LIKE :type OR pnt.description LIKE :type ) ");
            countSqlFrom.append("AND ( pnt.no LIKE :type OR pnt.description LIKE :type ) ");
            isBaisRequired = true;
        }


        if (criteria.getWorkSiteName() != null
            && !"".equals(criteria.getWorkSiteName())) {
            sqlFrom.append("AND acs.work_site_name LIKE :workSiteName ");
            countSqlFrom.append("AND acs.work_site_name LIKE :workSiteName ");
            isBaisRequired = true;
        }


        if (criteria.getWorkSiteAddress() != null
            && !"".equals(criteria.getWorkSiteAddress())) {
            sqlFrom.append("AND acs.work_site_address = :workSiteAddress ");
            countSqlFrom.append("AND acs.work_site_address = :workSiteAddress ");
            isBaisRequired = true;
        }


        if (criteria.getEntityTypeId() != null
            && !criteria.getEntityTypeId().equals(0L)) {
            sqlFrom.append("AND ac.entity_type_id = :entityTypeId ");
            countSqlFrom.append("AND ac.entity_type_id = :entityTypeId ");
        }

        if (criteria.getEntitySubTypeId() != null
            && !criteria.getEntitySubTypeId().equals(0L)) {
            sqlFrom.append("AND ac.entity_sub_type_id = :entitySubTypeId ");
            countSqlFrom.append("AND ac.entity_sub_type_id = :entitySubTypeId ");
        }

        if (criteria.getEntityNo() != null
            && !"".equals(criteria.getEntityNo())) {
            sqlFrom.append("AND (ac.entity_no like :entityNo or ac.entity_no1 like :entityNo or ac.entity_no2 like :entityNo or ac.drawing_title like :entityNo) ");
            countSqlFrom.append("AND (ac.entity_no like :entityNo or ac.entity_no1 like :entityNo or ac.entity_no2 like :entityNo or ac.drawing_title like :entityNo) ");
        }

        if (criteria.getEntityId() != null && criteria.getEntityId() != 0L) {
            sqlFrom.append("AND ac.entity_id = :entityId ");
            countSqlFrom.append("AND ac.entity_id = :entityId ");
        }

        if (criteria.getOwnerName() != null
            && !"".equals(criteria.getOwnerName())) {
            sqlFrom.append("AND ac.owner_name = :ownerName ");
            countSqlFrom.append("AND ac.owner_name = :ownerName ");
        }

        if (criteria.getCurrentExecutor() != null
            && !"".equals(criteria.getCurrentExecutor())) {
            if ("#".equals(criteria.getCurrentExecutor())) {
                sqlFrom.append("AND (acs.current_executor is null or acs.current_executor = '') ");
                countSqlFrom.append("AND (acs.current_executor is null or acs.current_executor = '') ");
            } else {
                sqlFrom.append("AND acs.current_executor = :currentExecutor ");
                countSqlFrom.append("AND acs.current_executor = :currentExecutor ");
            }
            isBaisRequired = true;
        }

        if (criteria.getSuspensionState() != null
            && !"".equals(criteria.getSuspensionState())) {
            sqlFrom.append("AND acs.suspension_state = :suspensionState ");
            countSqlFrom.append("AND acs.suspension_state = :suspensionState ");
            isBaisRequired = true;
        }

        if (criteria.getFinishState() != null
            && !"".equals(criteria.getFinishState())) {
            sqlFrom.append("AND acs.finish_state = :finishState ");
            countSqlFrom.append("AND acs.finish_state = :finishState ");
            isBaisRequired = true;
        }

        if (criteria.getTeamName() != null
            && !"".equals(criteria.getTeamName())) {
            sqlFrom.append("AND acs.team_name like :teamName ");
            countSqlFrom.append("AND acs.team_name like :teamName ");
            isBaisRequired = true;
        }

        if (criteria.getTaskPackageName() != null
            && !"".equals(criteria.getTaskPackageName())) {
            sqlFrom.append("AND acs.task_package_name like :taskPackageName ");
            countSqlFrom.append("AND acs.task_package_name like :taskPackageName ");
            isBaisRequired = true;
        }

        if (criteria.getCreateDateFromTime() != null) {
            sqlFrom.append("AND ac.created_at >= :createDateFrom ");
            countSqlFrom.append("AND ac.created_at >= :createDateFrom ");
        }

        if (criteria.getCreateDateUntilTime() != null) {
            sqlFrom.append("AND ac.created_at <= :createDateUntil ");
            countSqlFrom.append("AND ac.created_at <= :createDateUntil ");
        }

        if (criteria.getEntityNo() != null) {
            sqlFrom.append("ORDER BY ac.entity_no ");
//            countSqlFrom.append("ORDER BY ac.entity_no ");
        } else {
            sqlFrom.append("ORDER BY ac.id desc ");
//            countSqlFrom.append("ORDER BY ac.id desc ");
        }
//        sqlFrom.append("ORDER BY ac.sort_no, ac.entity_no ");
        if (isBaisRequired) {
            sqlCount.append(" JOIN bpm_activity_instance_state acs ON ac.id = acs.bai_id  ");
        }
        if (isBpmRuTaskRequired) {
            sqlCount.append(" JOIN bpm_ru_task rt ON ac.id = rt.act_inst_id ");

        }
        Query countQuery = entityManager.createNativeQuery(sqlCount.append(countSqlFrom).toString());

        sqlFrom.append("LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.append(sqlFrom).toString());


        if (criteria.getActInstIds() != null) {
            query.setParameter("actInstIds", criteria.getActInstIds());
            countQuery.setParameter("actInstIds", criteria.getActInstIds());
        }


        if (criteria.getProcessStageId() != null) {
            query.setParameter("processStageId", criteria.getProcessStageId());
            countQuery.setParameter("processStageId", criteria.getProcessStageId());
        }

        if (criteria.getProcessId() != null) {
            query.setParameter("processId", criteria.getProcessId());
            countQuery.setParameter("processId", criteria.getProcessId());
        }

        if (criteria.getProcessName() != null) {
            query.setParameter("processNameCn", criteria.getProcessName());
            countQuery.setParameter("processNameCn", criteria.getProcessName());
        }

        if (criteria.getDiscipline() != null) {
            query.setParameter("discipline", criteria.getDiscipline());
        }

        if (criteria.getFunction() != null) {
            query.setParameter("function", criteria.getFunction());
            countQuery.setParameter("function", criteria.getFunction());
        }

        if (criteria.getType() != null) {
            query.setParameter("type", criteria.getType());
            countQuery.setParameter("type", criteria.getType());
        }

        if (criteria.getEntityTypeId() != null) {
            query.setParameter("entityTypeId", criteria.getEntityTypeId());
            countQuery.setParameter("entityTypeId", criteria.getEntityTypeId());
        }

        if (criteria.getEntitySubTypeId() != null) {
            query.setParameter("entitySubTypeId", criteria.getEntitySubTypeId());
            countQuery.setParameter("entitySubTypeId", criteria.getEntitySubTypeId());
        }

        if (criteria.getEntityNo() != null
            && !"".equals(criteria.getEntityNo())) {
            query.setParameter("entityNo", "%" + criteria.getEntityNo() + "%");
            countQuery.setParameter("entityNo", "%" + criteria.getEntityNo() + "%");
        }

        if (criteria.getEntityId() != null
            && criteria.getEntityId() != 0L) {
            query.setParameter("entityId", criteria.getEntityId());
            countQuery.setParameter("entityId", criteria.getEntityId());
        }

        if (criteria.getOwnerName() != null
            && !"".equals(criteria.getOwnerName())) {
            query.setParameter("ownerName", criteria.getOwnerName());
            countQuery.setParameter("ownerName", criteria.getOwnerName());
        }

        if (criteria.getCurrentExecutor() != null
            && !"".equals(criteria.getCurrentExecutor())) {
            if (!"#".equals(criteria.getCurrentExecutor())) {
                query.setParameter("currentExecutor", criteria.getCurrentExecutor());
                countQuery.setParameter("currentExecutor", criteria.getCurrentExecutor());
            }
        }

        if (criteria.getSuspensionState() != null
            && !"".equals(criteria.getSuspensionState())) {


            query.setParameter("suspensionState", criteria.getSuspensionState());
            countQuery.setParameter("suspensionState", criteria.getSuspensionState());
        }

        if (criteria.getFinishState() != null
            && !"".equals(criteria.getFinishState())) {


            query.setParameter("finishState", criteria.getFinishState());
            countQuery.setParameter("finishState", criteria.getFinishState());
        }

        if (criteria.getTeamName() != null
            && !"".equals(criteria.getTeamName())) {
            query.setParameter("teamName", "%" + criteria.getTeamName() + "%");
            countQuery.setParameter("teamName", "%" + criteria.getTeamName() + "%");
        }

        if (criteria.getWorkSiteName() != null
            && !"".equals(criteria.getWorkSiteName())) {
            query.setParameter("workSiteName", "%" + criteria.getWorkSiteName() + "%");
            countQuery.setParameter("workSiteName", "%" + criteria.getWorkSiteName() + "%");
        }

        if (criteria.getWorkSiteAddress() != null
            && !"".equals(criteria.getWorkSiteAddress())) {
            query.setParameter("workSiteAddress", criteria.getWorkSiteAddress());
            countQuery.setParameter("workSiteAddress", criteria.getWorkSiteAddress());
        }

        if (criteria.getTaskPackageName() != null
            && !"".equals(criteria.getTaskPackageName())) {
            query.setParameter("taskPackageName", "%" + criteria.getTaskPackageName() + "%");
            countQuery.setParameter("taskPackageName", "%" + criteria.getTaskPackageName() + "%");
        }

        if (!StringUtils.isEmpty(criteria.getTaskDefKey())) {
            query.setParameter("taskDefKey", criteria.getTaskDefKey());
            countQuery.setParameter("taskDefKey", criteria.getTaskDefKey());
        }

        if (criteria.getCreateDateFromTime() != null) {
            GregorianCalendar dateFrom = new GregorianCalendar();
            dateFrom.setTime(criteria.getCreateDateFromTime());
            dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
            dateFrom.set(GregorianCalendar.MINUTE, 0);
            dateFrom.set(GregorianCalendar.SECOND, 0);
            query.setParameter("createDateFrom", dateFrom.getTime());
            countQuery.setParameter("createDateFrom", dateFrom.getTime());
        }

        if (criteria.getCreateDateUntilTime() != null) {
            GregorianCalendar dateUntil = new GregorianCalendar();
            dateUntil.setTime(criteria.getCreateDateUntilTime());
            dateUntil.set(GregorianCalendar.HOUR_OF_DAY, 23);
            dateUntil.set(GregorianCalendar.MINUTE, 59);
            dateUntil.set(GregorianCalendar.SECOND, 59);
            query.setParameter("createDateUntil", dateUntil.getTime());
            countQuery.setParameter("createDateUntil", dateUntil.getTime());
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);


        int pageNo = criteria.getPage().getNo();
        int pageSize = criteria.getPage().getSize();
        if (criteria.getPageable()) {
            pageSize = Integer.MAX_VALUE;
            criteria.getPage().setSize(pageSize);
        }
        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        Long count = (Long) countQuery.getSingleResult();


        List<BpmActivityInstanceDTO> actInstList = new ArrayList<>();

        for (Map<String, Object> wpsMap : queryResultList) {
            BpmActivityInstanceDTO actInst = new BpmActivityInstanceDTO();

            if (wpsMap.get("status") != null) {
                actInst.setStatus(EntityStatus.valueOf((String) wpsMap.get("status")));
            }
            if (wpsMap.get("suspensionState") != null) {
                actInst.setSuspensionState(SuspensionState.valueOf((String) wpsMap.get("suspensionState")));
            }
            if (wpsMap.get("finishState") != null) {
                actInst.setFinishState(ActInstFinishState.valueOf((String) wpsMap.get("finishState")));
            }
            BeanUtils.copyProperties(wpsMap, actInst,
                "status",
                "suspensionState",
                "finishState");

            actInstList.add(actInst);
        }

        return new PageImpl<>(actInstList, criteria.toPageable(), count.longValue());

    }

    /**
     * 查询流程实例id
     */
    @Override
    public List<Long> findActInstIdsInActivityInstance(Long orgId, Long projectId,
                                                       TodoTaskCriteriaDTO taskCriteria) {

        SQLQueryBuilder<BpmActivityInstanceBase> builder = getSQLQueryBuilder(BpmActivityInstanceBase.class)
            .is("projectId", projectId)
            .is("processStageId", taskCriteria.getProcessStageId())
            .is("processId", taskCriteria.getProcessId())
            .is("processCategoryId", taskCriteria.getProcessCategoryId())
            .is("entitySubTypeId", taskCriteria.getEntitySubTypeId())
            .like("entityNo", taskCriteria.getEntityNo());

        if (taskCriteria.getSuspensionState() != null) {
            SuspensionState state = SuspensionState.valueOf(taskCriteria.getSuspensionState());
            List<SuspensionState> list = new ArrayList<SuspensionState>();
            list.add(state);
            builder.in("suspensionState", list);
        }

        if (taskCriteria.getEntityModuleNames() != null && !taskCriteria.getEntityModuleNames().equals("")) {
            String[] entityModuleNames = taskCriteria.getEntityModuleNames().split(",");
            List<String> entityModuleNamesList = new ArrayList<>(Arrays.asList(entityModuleNames));
            builder.in("entityModuleName", entityModuleNamesList);
        }

        List<ActInstFinishState> finishList = new ArrayList<ActInstFinishState>();
        finishList.add(ActInstFinishState.NOT_FINISHED);
        builder.in("finishState", finishList);

        List<BpmActivityInstanceBase> actInsts = builder.limit(Integer.MAX_VALUE).exec().list();
        List<Long> result = new ArrayList<>();
        for (BpmActivityInstanceBase actInst : actInsts) {
            result.add(actInst.getId());
        }
        return result;

    }


    @Override
    public Page<BpmActivityInstanceDTO> findCompletedTask(Long orgId, Long projectId, TodoTaskCriteriaDTO taskCriteria,
                                                          List<Long> actInstIds, PageDTO pageDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("ac.id as id, ");
        sql.append("ac.created_at as createdAt, ");
        sql.append("ac.last_modified_at as lastModifiedAt, ");
        sql.append("ac.status as status, ");

        sql.append("ac.project_id as projectId, ");
        sql.append("ac.org_id as orgId, ");
        sql.append("ac.version as version, ");
        sql.append("acs.memo as memo, ");
        sql.append("ac.entity_no as entityNo, ");
        sql.append("ac.entity_no1 as entityNo1, ");
        sql.append("ac.entity_no2 as entityNo2, ");
        sql.append("ac.drawing_title as drawingTitle, ");
        sql.append("ac.entity_type as entityType, ");
        sql.append("ac.entity_id as entityId, ");
        sql.append("ac.entity_project_node_id as entityProjectNodeId, ");

        sql.append("ac.entity_module_name as entityModuleName, ");
        sql.append("ac.entity_module_project_node_id as entityModuleProjectNodeId, ");
        sql.append("ac.entity_type_id as entityTypeId, ");
        sql.append("ac.entity_sub_type as entitySubType, ");
        sql.append("ac.entity_sub_type_id as entitySubTypeId, ");
        sql.append("ac.process_stage_name as processStage, ");
        sql.append("ac.process_stage_id as processStageId, ");
        sql.append("ac.process_name as process, ");
        sql.append("ac.process_id as processId, ");

        sql.append("ac.owner_id as ownerId, ");
        sql.append("ac.owner_name as ownerName, ");
        sql.append("ac.allocatee as allocatee, ");
        sql.append("ac.allocatee_date as allocateeDate, ");
        sql.append("ac.plan_start_date as planStartDate, ");
        sql.append("ac.plan_end_date as planEndDate, ");
        sql.append("ac.plan_hour as planHour, ");
        sql.append("acs.start_date as startDate, ");
        sql.append("acs.end_date as endDate, ");
        sql.append("acs.cost_hour as costHour, ");

        sql.append("acs.suspension_state as suspensionState, ");
        sql.append("acs.finish_state as finishState, ");
        sql.append("ac.act_category as actCategory, ");
        sql.append("ac.process_category_id as processCategoryId, ");


        sql.append("acb.reports as reports, ");
        sql.append("acb.ex_ins_issue_ids as exInsIssueIds, ");
        sql.append("acb.in_ins_issue_ids as inInsIssueIds, ");

        sql.append("acs.execute_ng_flag as executeNgFlag, ");
        sql.append("acs.current_executor as currentExecutor, ");
        sql.append("acs.work_site_id as workSiteId, ");
        sql.append("acs.work_site_name as workSiteName, ");
        sql.append("acs.work_site_address as workSiteAddress, ");
        sql.append("acs.team_name as teamName, ");
        sql.append("acs.team_id as teamId, ");
        sql.append("acs.task_package_name as taskPackageName, ");
        sql.append("acs.task_package_id as taskPackageId, ");
        sql.append("acs.un_accept_count as unAcceptCount ");

        sql.append("FROM bpm_activity_instance_base ac ");
        sql.append(" JOIN bpm_activity_instance_state acs ON ac.id = acs.bai_id ");
        sql.append("LEFT JOIN bpm_activity_instance_blob acb ON ac.id = acb.bai_id ");
        sql.append("WHERE ");

        sql.append("  ac.project_id = :projectId ");
        if (actInstIds.size() > 0) {
            sql.append(" AND ac.id IN :actInstIds ");
        }
        if (taskCriteria.getProcessStageId() != null) {
            sql.append(" AND ac.process_stage_id = :processStageId ");
        }
        if (taskCriteria.getProcessId() != null) {
            sql.append(" AND ac.process_id = :processId ");
        }
        if (taskCriteria.getProcessCategoryId() != null) {
            sql.append(" AND ac.process_category_id = :processCategoryId ");
        }
        if (taskCriteria.getEntitySubTypeId() != null) {
            sql.append(" AND ac.entity_sub_type_id = :entitySubTypeId ");
        }
//        sql.append(" AND acs.finish_state = :finishState ");

        if (taskCriteria.getCreateDateFromTime() != null) {
            GregorianCalendar dateFrom = new GregorianCalendar();
            dateFrom.setTime(taskCriteria.getCreateDateFromTime());
            dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
            dateFrom.set(GregorianCalendar.MINUTE, 0);
            dateFrom.set(GregorianCalendar.SECOND, 0);
            sql.append(" AND ac.created_at >= :dateFrom");
        }

        if (taskCriteria.getCreateDateUntilTime() != null) {
            GregorianCalendar dateUntil = new GregorianCalendar();
            dateUntil.setTime(taskCriteria.getCreateDateUntilTime());
            dateUntil.set(GregorianCalendar.HOUR_OF_DAY, 23);
            dateUntil.set(GregorianCalendar.MINUTE, 59);
            dateUntil.set(GregorianCalendar.SECOND, 59);
            sql.append(" AND ac.created_at <= :dateUntil");

        }

        if (taskCriteria.getEntityNo() != null
            && !taskCriteria.getEntityNo().equals("")) {
            Map<String, Map<String, Object>> keywordCriteria = new IdentityHashMap<>();
            Map<String, Object> operator = new IdentityHashMap<>();
            sql.append(" AND (ac.entity_no LIKE :keyWord OR ac.drawing_title LIKE :keyWord OR ac.entity_no1 LIKE :keyWord) ");
        }
        sql.append(" LIMIT :start , :offset ");


        Query query = entityManager.createNativeQuery(sql.toString());


        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";

        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (taskCriteria.getCreateDateFromTime() != null) {
            GregorianCalendar dateFrom = new GregorianCalendar();
            dateFrom.setTime(taskCriteria.getCreateDateFromTime());
            dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
            dateFrom.set(GregorianCalendar.MINUTE, 0);
            dateFrom.set(GregorianCalendar.SECOND, 0);
            query.setParameter("dateFrom", dateFrom);
            countQuery.setParameter("dateFrom", dateFrom);
        }

        if (taskCriteria.getCreateDateUntilTime() != null) {
            GregorianCalendar dateUntil = new GregorianCalendar();
            dateUntil.setTime(taskCriteria.getCreateDateUntilTime());
            dateUntil.set(GregorianCalendar.HOUR_OF_DAY, 23);
            dateUntil.set(GregorianCalendar.MINUTE, 59);
            dateUntil.set(GregorianCalendar.SECOND, 59);
            query.setParameter("dateUntil", dateUntil);
            countQuery.setParameter("dateUntil", dateUntil);
        }

        if (taskCriteria.getEntityNo() != null
            && !taskCriteria.getEntityNo().equals("")) {
            Map<String, Map<String, Object>> keywordCriteria = new IdentityHashMap<>();
            query.setParameter("keyWord", "%" + taskCriteria.getEntityNo() + "%");
            countQuery.setParameter("keyWord", "%" + taskCriteria.getEntityNo() + "%");
        }
        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);
        if (actInstIds.size() > 0) {
            query.setParameter("actInstIds", actInstIds);
            countQuery.setParameter("actInstIds", actInstIds);
        }
        if (taskCriteria.getProcessName() != null) {
            query.setParameter("processName", taskCriteria.getProcessName());
            countQuery.setParameter("processName", taskCriteria.getProcessName());
        }
        if (taskCriteria.getProcessStageId() != null) {
            query.setParameter("processStageId", taskCriteria.getProcessStageId());
            countQuery.setParameter("processStageId", taskCriteria.getProcessStageId());
        }
        if (taskCriteria.getProcessId() != null) {
            query.setParameter("processId", taskCriteria.getProcessId());
            countQuery.setParameter("processId", taskCriteria.getProcessId());
        }
        if (taskCriteria.getProcessCategoryId() != null) {
            query.setParameter("processCategoryId", taskCriteria.getProcessCategoryId());
            countQuery.setParameter("processCategoryId", taskCriteria.getProcessCategoryId());
        }
        if (taskCriteria.getEntitySubTypeId() != null) {
            query.setParameter("entitySubTypeId", taskCriteria.getEntitySubTypeId());
            countQuery.setParameter("entitySubTypeId", taskCriteria.getEntitySubTypeId());
        }
//        query.setParameter("finishState", ActInstFinishState.FINISHED.toString());
//        countQuery.setParameter("finishState", ActInstFinishState.FINISHED.toString());


        int pageNo = pageDTO.getPage().getNo();
        int pageSize = pageDTO.getPage().getSize();
        if (pageDTO.getFetchAll()) {
            pageDTO.setFetchAll(false);
            pageSize = Integer.MAX_VALUE;
            pageDTO.getPage().setSize(pageSize);
        }
        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        Long count = (Long) countQuery.getSingleResult();


        List<BpmActivityInstanceDTO> actInstList = new ArrayList<>();

        for (Map<String, Object> wpsMap : queryResultList) {
            BpmActivityInstanceDTO actInst = new BpmActivityInstanceDTO();

            if (wpsMap.get("status") != null) {
                actInst.setStatus(EntityStatus.valueOf((String) wpsMap.get("status")));
            }
            if (wpsMap.get("suspensionState") != null) {
                actInst.setSuspensionState(SuspensionState.valueOf((String) wpsMap.get("suspensionState")));
            }
            if (wpsMap.get("finishState") != null) {
                actInst.setFinishState(ActInstFinishState.valueOf((String) wpsMap.get("finishState")));
            }

            BeanUtils.copyProperties(wpsMap, actInst,
                "status",
                "suspensionState",
                "finishState");

            actInstList.add(actInst);
        }

        return new PageImpl<>(actInstList, pageDTO.toPageable(), count.longValue());

    }

    @Override
    public List<EntityNoBpmActivityInstanceDTO> getEntitiyTodo(Long orgId, Long projectId, Long processId, Long entitySubTypeId, String entityCategoryNameEn, String entityCategoryTypeNameEn, String keyWord, PageDTO pageDTO) {

        int pageNo = pageDTO.getPage().getNo();
        int size = pageDTO.getPage().getSize();

        EntityManager entityManager = getEntityManager();
        List<EntityNoBpmActivityInstanceDTO> rsList = new ArrayList<>();
        String sqlTemp = "select  ec.entity_id as entity_id, ec.no as entity_no from ";
        boolean pipleEntity = false;
        boolean dwgEntity = false;
        if (!entityCategoryTypeNameEn.toUpperCase().contains("PUNCH") &&
            !entityCategoryNameEn.toUpperCase().contains("DRAWING")
        ) {
            pipleEntity = true;
            sqlTemp += "project_node ec ";
        } else if ("SHOP_DRAWING".equals(entityCategoryTypeNameEn)) {
            pipleEntity = true;
            dwgEntity = true;
            sqlTemp = "select  ec.id as entity_id, ec.dwg_no as entity_no from drawing ec ";
        }

        if (pipleEntity) {

            StringBuffer sql = new StringBuffer()
                .append(sqlTemp);

            if (dwgEntity) {
                sql.append(" where ec.`status` = 'ACTIVE' and ec.org_id = :orgId and ec.project_id = :projectId and ec.drawing_category_id = :entitySubTypeId");
            } else {
                sql.append(" where ec.`status` = 'ACTIVE' and ec.org_id = :orgId and ec.project_id = :projectId and ec.entity_sub_type = :entityCategoryNameEn");
            }

            if (keyWord != null && !"".equals(keyWord)) {
                if (dwgEntity) {
                    sql.append(" and ec.dwg_no like CONCAT(:keyWord,'%' ) ");
                } else {
                    sql.append(" and ec.no like CONCAT(:keyWord,'%' ) ");
                }

            }
            sql.append(" and not exists (select 1 from bpm_activity_instance_base actIns  where actIns.project_id = :projectId ");
            if (dwgEntity) {
                sql.append("and ec.id = actIns.entity_id ");
            } else {
                sql.append("and ec.entity_id = actIns.entity_id ");
            }
            sql.append("and actIns.entity_sub_type_id = :entitySubTypeId and actIns.process_id = :processId and actIns.`status` = 'ACTIVE' ")
                .append(" )");
            String strSql = sql.toString();

            Query query = entityManager.createNativeQuery(strSql);
            query.setParameter("orgId", orgId);
            if (keyWord != null && !"".equals(keyWord)) {
                query.setParameter("keyWord", keyWord);
            }
            if (!dwgEntity) {
                query.setParameter("entityCategoryNameEn", entityCategoryNameEn);
            }
            query.setParameter("projectId", projectId);
            query.setParameter("processId", processId);
            query.setParameter("entitySubTypeId", entitySubTypeId);

            query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            List<Map<String, Object>> list = query.getResultList();
            int i = 1;
            for (Map<String, Object> m : list) {
                i++;
                if (i > 20) {
                    break;
                }
                EntityNoBpmActivityInstanceDTO dto = new EntityNoBpmActivityInstanceDTO();
                dto.setId(((BigInteger) m.get("entity_id")).longValue());
                dto.setEntityNo((String) m.get("entity_no"));
                rsList.add(dto);
            }
        }

        return rsList;
    }


    @Override
    public List<TasksCategoryAssigneeDTO> batchFindTaskCategoryAssignee(List<Long> actInstIds) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT GROUP_CONCAT(DISTINCT a.`assignee_name`) AS assigneeName, a.`task_category` AS category FROM bpm_act_task_assignee a ")
            .append(" LEFT JOIN bpm_activity_instance_base i ON  i.`id` = a.`act_inst_id`")
            .append(" WHERE i.`id` in(:actInstIds) AND a.`task_category` IS NOT NULL")
            .append(" AND a.`status` = 'ACTIVE'")
            .append(" GROUP BY a.`task_category`");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("actInstIds", actInstIds);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = query.getResultList();
        System.out.println("list");
        System.out.println(list.toString());
        List<TasksCategoryAssigneeDTO> rsList = new ArrayList<>();

        for (Map<String, Object> wpsMap : list) {
            TasksCategoryAssigneeDTO dto = new TasksCategoryAssigneeDTO();
            BeanUtils.copyProperties(wpsMap, dto);
            rsList.add(dto);
        }

        System.out.println("rslist");
        System.out.println(rsList.toString());
        return rsList;
    }

    @Override
    public Integer getDailySummary(Long orgId, Long projectId, SummaryCriteriaDTO criteriaDTO) {
        return null;
    }

    @Override
    public List<TaskProcessDTO> findProcess(Long projectId) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("ac.process_name AS processName, ");
        sql.append("ac.process_id AS processId ");
        sql.append("FROM ");
        sql.append("bpm_activity_instance_base ac ");
        sql.append("JOIN bpm_activity_instance_state acs ON ac.id = acs.bai_id ");
        sql.append("LEFT JOIN bpm_activity_instance_blob acb ON ac.id = acb.bai_id  ");
        sql.append("WHERE ");
        sql.append("ac.project_id = :projectId ");
        sql.append("GROUP BY processId ");
        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectId", projectId);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        // 获取查询结果
        List<Map<String, Object>> list = query.getResultList();
        // 设置结果（结构为MAP）到WPS对象中
        List<TaskProcessDTO> rsList = new ArrayList<TaskProcessDTO>();

        for (Map<String, Object> m : list) {
            TaskProcessDTO dto = new TaskProcessDTO();
            if (m.get("processId") != null && !"".equals(m.get("processId"))) {
                dto.setProcessId(Long.parseLong(m.get("processId").toString()));
            }
            if (m.get("processName") != null && !"".equals(m.get("processName"))) {
                dto.setProcessName((String) m.get("processName"));
                rsList.add(dto);
            }

        }
        return rsList;
    }

}
