package com.ose.tasks.domain.model.repository.bpm;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.BpmExInspApply;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.BpmProcessStageEnum;
import com.ose.tasks.vo.bpm.BpmsProcessNameEnum;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.util.BeanUtils;
import com.ose.vo.BpmTaskDefKey;
import com.ose.vo.BpmTaskType;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

/**
 * 任务查询。
 */
public class BpmRuTaskRepositoryImpl extends BaseRepository implements BpmRuTaskRepositoryCustom {

    private final List<String> MOBILE_TASK_NODES = TaskRuleCheckService.MOBILE_TASK_NODES;


    private final static Set<String> NDT_GENERATE_REPORT_TASK_TYPES = new HashSet<String>() {{
        add(BpmTaskType.GENERATE_NDT_REPORT.name());
        add(BpmTaskType.GENERATE_NG_NDT_REPORT.name());
    }};

    private final static Set<String> NDT_PROCESSES = new HashSet<String>() {{
        add("RT");
        add("UT");
        add("MT");
        add("PT");
        add("UT_MT");
    }};


    @Override
    public Page<BpmRuTask> getRuTaskList(Long orgId, Long projectId, String assignee,
                                         TodoTaskCriteriaDTO taskCriteria, List<Long> actInstIds, PageDTO pageDTO) {

        System.out.println(pageDTO.getPage().getSize());

        SQLQueryBuilder<BpmRuTask> builder = getSQLQueryBuilder(BpmRuTask.class).is("name", taskCriteria.getTaskNode())
            .like("assignee", assignee).in("actInstId", actInstIds);

        builder.sort(new Sort.Order(Sort.Direction.DESC, "createTime"));
        if (taskCriteria.isPageable()) {
            return builder.paginate(pageDTO.toPageable()).exec().page();
        } else
            return builder.limit(Integer.MAX_VALUE).exec().page();

    }

    @Override
    public List<TaskPackageDTO> getTaskPackageList(Long orgId, Long projectId, String assignee) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("ac.task_package_id as taskPackageId, ");
        sql.append("ac.task_package_name as taskPackageName ");
        sql.append("FROM bpm_activity_instance AS ac ");
        sql.append("inner join bpm_ru_task AS ru on ac.id = ru.act_inst_id ");
        sql.append("WHERE ");
        sql.append(" ac.project_id = :projectId ");
        sql.append("AND ru.assignee_ = :assignee ");
        sql.append("GROUP BY task_package_id");

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectId", projectId);
        query.setParameter("assignee", assignee);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        // 获取查询结果
        List<Map<String, Object>> list = query.getResultList();
        // 设置结果（结构为MAP）到WPS对象中
        List<TaskPackageDTO> rsList = new ArrayList<TaskPackageDTO>();;

        for (Map<String, Object> m : list) {
            TaskPackageDTO dto = new TaskPackageDTO();
            if (m.get("taskPackageId") != null && !"".equals(m.get("taskPackageId"))) {
                dto.setTaskPackageId(Long.parseLong(m.get("taskPackageId").toString()));
            }
            if (m.get("taskPackageName") != null && !"".equals(m.get("taskPackageName"))) {
                dto.setTaskPackageName((String) m.get("taskPackageName"));
                rsList.add(dto);
            }

        }
        return rsList;

    }

    @Override
    public Page<TodoTaskDTO> getTodoTaskList(Long orgId, Long projectId, String assignee, List<String> taskPackageList,
                                             TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO,
                                             List<Long> entityIdList) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("ac.id as actInstId, ");
        sql.append("ru.id as taskId, ");
        sql.append("ac.entity_sub_type as entitySubType, ");
        sql.append("ac.entity_no as entityNo, ");
        sql.append("ac.entity_no1 as entityNo1, ");
        sql.append("ac.entity_no2 as entityNo2, ");
        sql.append("ru.assignee_ as assignee, ");
        sql.append("ac.drawing_title as drawingTitle, ");
        sql.append("ru.suspension_state_ as suspensionState, ");
        sql.append("ac.process_name as process, ");
        sql.append("ac.process_stage_name as processStage, ");
        sql.append("ac.act_category as processCategory, ");
        sql.append("ac.entity_id as entityId, ");
        sql.append("ac.version as version, ");
        sql.append("acs.task_package_id as taskPackageId, ");
        sql.append("acs.task_package_name as taskPackageName, ");
        sql.append("acs.team_id as teamId, ");
        sql.append("acs.team_name as teamName, ");
        sql.append("acs.work_site_id as workSiteId, ");
        sql.append("acs.work_site_name as workSiteName, ");
        sql.append("acs.work_site_address as workSiteAddress, ");
        sql.append("ac.owner_name as ownerName, ");
        sql.append("ec.sub_drawing_flg as subDrawingFlg, ");
        sql.append("ru.create_time_ as taskCreatedTime, ");
        sql.append("ru.name_ as taskNode, ");
        sql.append("ru.task_def_key_ as taskDefKey, ");
        sql.append("ru.is_handling as isHandling, ");
        sql.append("acs.memo as memo ");
        sql.append("FROM bpm_activity_instance_base AS ac ");
        sql.append("INNER JOIN bpm_activity_instance_state AS acs on ac.project_id = acs.project_id AND ac.id = acs.bai_id ");
        sql.append("LEFT JOIN bpm_activity_instance_blob AS acb on ac.project_id = acb.project_id AND ac.id = acb.bai_id ");
        sql.append("INNER JOIN bpm_ru_task AS ru on ac.id = ru.act_inst_id ");
        sql.append("INNER JOIN bpm_entity_sub_type AS ec on ec.id = ac.entity_sub_type_id ");
        sql.append("WHERE ");
        sql.append(" ac.project_id = :projectId ");
        sql.append("AND ru.assignee_ LIKE :assignee ");

        if (entityIdList != null && entityIdList.size() > 0) {
            sql.append("AND ac.entity_id in (:entityIds) ");
        }

        if (taskCriteria.getHandling() != null) {
            if (!taskCriteria.getHandling()) {
                sql.append("AND ru.is_handling is null ");
            } else {
                sql.append("AND ru.is_handling = :isHandling ");
            }
        }

        if (taskCriteria.getProcessStageId() != null) {
            sql.append("AND ac.process_stage_id = :processStageId ");
        }

        if (taskCriteria.getProcessCategoryId() != null) {
            sql.append("AND ac.process_category_id = :processCategoryId ");
        }

        if (taskCriteria.getEntitySubTypeId() != null) {
            sql.append("AND ac.entity_sub_type_id = :entitySubTypeId ");
        }

        if (taskCriteria.getProcessId() != null) {
            sql.append("AND ac.process_id = :processId ");
        }

        if (taskCriteria.getEntityNo() != null
            && !"".equals(taskCriteria.getEntityNo())) {
            sql.append("AND (ac.entity_no LIKE :entityNo or ac.entity_no1 LIKE :entityNo or ac.entity_no2 LIKE :entityNo or ac.drawing_title LIKE :entityNo) ");
        }

        if (taskCriteria.getTaskNode() != null
            && !"".equals(taskCriteria.getTaskNode())) {
            sql.append("AND ru.name_ LIKE :taskNode ");
        }

        if (taskCriteria.getTaskDefKey() != null
            && !"".equals(taskCriteria.getTaskDefKey())) {
            sql.append("AND ru.task_def_key_ = :taskDefKey ");
        }

        if (taskCriteria.getEntityModuleNames() != null
            && !"".equals(taskCriteria.getEntityModuleNames())) {
            sql.append("AND ac.entity_module_name LIKE :entityModuleName ");
        }
        if (taskCriteria.getTeamName() != null
            && !"".equals(taskCriteria.getTeamName())) {

            sql.append("AND acs.team_name LIKE :teamName ");
        }

        if (taskCriteria.getTaskPackageName() != null
            && !"".equals(taskCriteria.getTaskPackageName())) {
            sql.append("AND acs.task_package_name LIKE :taskPackageName ");
        }

        if (taskCriteria.getWorkSiteName() != null
            && !"".equals(taskCriteria.getWorkSiteName())) {
            sql.append("AND acs.work_site_name LIKE :workSiteName ");
        }

        if (taskCriteria.getWorkSiteAddress() != null
            && !"".equals(taskCriteria.getWorkSiteAddress())) {
            sql.append("AND acs.work_site_address = :workSiteAddress ");
        }

        if (taskCriteria.getCreateDateFromTime() != null) {
            sql.append("AND ru.create_time_ >= :createDateFrom ");
        }

        if (taskCriteria.getCreateDateUntilTime() != null) {
            sql.append("AND ru.create_time_ <= :createDateUntil ");
        }

        if (taskPackageList != null && taskPackageList.size() > 0) {
            sql.append("AND acs.task_package_name in (:taskPackageName) ");
        }

        if (taskCriteria.getStateSearch() != null && !"".equals(taskCriteria.getStateSearch())) {
            sql.append("AND acs.suspension_state = :suspensionState ");
        }

        if (taskCriteria.getClientType() != null && !"".equals(taskCriteria.getClientType())) {
            if (taskCriteria.getClientType().equals("pc-batch")) {
                sql.append(" ORDER BY ac.entity_no ");
            } else {
                sql.append(" ORDER BY ru.id desc ");
            }
        }
        sql.append("LIMIT :start , :offset ");

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (taskCriteria.getProcessCategoryId() != null
            && !"".equals(taskCriteria.getProcessCategoryId())) {
            query.setParameter("processCategoryId", taskCriteria.getProcessCategoryId());
            countQuery.setParameter("processCategoryId", taskCriteria.getProcessCategoryId());
        }

        if (taskCriteria.getEntitySubTypeId() != null
            && !"".equals(taskCriteria.getEntitySubTypeId())) {
            query.setParameter("entitySubTypeId", taskCriteria.getEntitySubTypeId());
            countQuery.setParameter("entitySubTypeId", taskCriteria.getEntitySubTypeId());
        }

        if (taskCriteria.getProcessStageId() != null
            && !"".equals(taskCriteria.getProcessStageId())) {
            query.setParameter("processStageId", taskCriteria.getProcessStageId());
            countQuery.setParameter("processStageId", taskCriteria.getProcessStageId());
        }

        if (taskCriteria.getProcessId() != null
            && !"".equals(taskCriteria.getProcessId())) {
            query.setParameter("processId", taskCriteria.getProcessId());
            countQuery.setParameter("processId", taskCriteria.getProcessId());
        }

//        if (taskCriteria.getTeamName() != null
//            && !"".equals(taskCriteria.getTeamName())) {
//            query.setParameter("teamName", taskCriteria.getTeamName());
//            countQuery.setParameter("teamName", taskCriteria.getTeamName());
//        }

        if (taskCriteria.getEntityNo() != null
            && !"".equals(taskCriteria.getEntityNo())) {
            query.setParameter("entityNo", "%" + taskCriteria.getEntityNo() + "%");
            countQuery.setParameter("entityNo", "%" + taskCriteria.getEntityNo() + "%");
        }

        if (taskCriteria.getTaskNode() != null
            && !"".equals(taskCriteria.getTaskNode())) {
            query.setParameter("taskNode", taskCriteria.getTaskNode());
            countQuery.setParameter("taskNode", taskCriteria.getTaskNode());
        }

        if (taskCriteria.getEntityModuleNames() != null
            && !"".equals(taskCriteria.getEntityModuleNames())) {
            query.setParameter("entityModuleName", "%" + taskCriteria.getEntityModuleNames() + "%");
            countQuery.setParameter("entityModuleName", "%" + taskCriteria.getEntityModuleNames() + "%");
        }
        if (taskCriteria.getTeamName() != null
            && !"".equals(taskCriteria.getTeamName())) {
            query.setParameter("teamName", "%" + taskCriteria.getTeamName() + "%");
            countQuery.setParameter("teamName", "%" + taskCriteria.getTeamName() + "%");
        }

        if (taskCriteria.getTaskPackageName() != null
            && !"".equals(taskCriteria.getTaskPackageName())) {
            query.setParameter("taskPackageName", "%" + taskCriteria.getTaskPackageName() + "%");
            countQuery.setParameter("taskPackageName", "%" + taskCriteria.getTaskPackageName() + "%");
        }
        if (taskCriteria.getWorkSiteName() != null
            && !"".equals(taskCriteria.getWorkSiteName())) {
            query.setParameter("workSiteName", "%" + taskCriteria.getWorkSiteName() + "%");
            countQuery.setParameter("workSiteName", "%" + taskCriteria.getWorkSiteName() + "%");
        }

        if (taskCriteria.getWorkSiteAddress() != null
            && !"".equals(taskCriteria.getWorkSiteAddress())) {
            query.setParameter("workSiteAddress", taskCriteria.getWorkSiteAddress());
            countQuery.setParameter("workSiteAddress", taskCriteria.getWorkSiteAddress());
        }

        if (taskCriteria.getHandling() != null && taskCriteria.getHandling() == true) {
            query.setParameter("isHandling", taskCriteria.getHandling());
            countQuery.setParameter("isHandling", taskCriteria.getHandling());
        }

        if (taskCriteria.getStateSearch() != null) {
            query.setParameter("suspensionState", taskCriteria.getStateSearch());
            countQuery.setParameter("suspensionState", taskCriteria.getStateSearch());
        }


        if (taskCriteria.getCreateDateFromTime() != null) {
            GregorianCalendar dateFrom = new GregorianCalendar();
            dateFrom.setTime(taskCriteria.getCreateDateFromTime());
            dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
            dateFrom.set(GregorianCalendar.MINUTE, 0);
            dateFrom.set(GregorianCalendar.SECOND, 0);
            query.setParameter("createDateFrom", dateFrom.getTime());
            countQuery.setParameter("createDateFrom", dateFrom.getTime());
        }

        if (taskCriteria.getCreateDateUntilTime() != null) {
            GregorianCalendar dateUntil = new GregorianCalendar();
            dateUntil.setTime(taskCriteria.getCreateDateUntilTime());
            dateUntil.set(GregorianCalendar.HOUR_OF_DAY, 23);
            dateUntil.set(GregorianCalendar.MINUTE, 59);
            dateUntil.set(GregorianCalendar.SECOND, 59);
            query.setParameter("createDateUntil", dateUntil.getTime());
            countQuery.setParameter("createDateUntil", dateUntil.getTime());
        }

        if (taskCriteria.getTaskDefKey() != null
            && !"".equals(taskCriteria.getTaskDefKey())) {
            query.setParameter("taskDefKey", taskCriteria.getTaskDefKey());
            countQuery.setParameter("taskDefKey", taskCriteria.getTaskDefKey());
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        query.setParameter("assignee", "%" + assignee + "%");
        countQuery.setParameter("assignee", "%" + assignee + "%");

        if (entityIdList != null && entityIdList.size() > 0) {
            query.setParameter("entityIds", entityIdList);
            countQuery.setParameter("entityIds", entityIdList);
        }

        if (taskPackageList != null && taskPackageList.size() > 0) {
            query.setParameter("taskPackageId", taskPackageList);
            countQuery.setParameter("taskPackageId", taskPackageList);
        }


        // 分页参数
        int pageNo = pageDTO.getPage().getNo();
        int pageSize = pageDTO.getPage().getSize();
        if (!taskCriteria.isPageable()) {
            pageSize = Integer.MAX_VALUE;
            pageDTO.getPage().setSize(pageSize);
        }
        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count =(Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<TodoTaskDTO> ruTaskList = new ArrayList<TodoTaskDTO>();
//        Set<String> keys = new HashSet<String>();
        // String[] keyy = {"taskId", "assignee", "category", "createTime", "delegation", "description", "name" , "owner", "parentTaskId", "suspensionState", "taskDefKey", "tenantId", "actInstId", "documents"};
        // keys.addAll(Arrays.asList(keyy));

        for (Map<String, Object> ruTaskMap : queryResultList) {
            TodoTaskDTO ruTask = new TodoTaskDTO();
            if (ruTaskMap.get("ndeType") != null) {
                ruTask.setNdeType(NDEType.getByDisplayName((String) ruTaskMap.get("ndeType")));
            }
            if (ruTaskMap.get("suspensionState") != null) {
                if (ruTaskMap.get("suspensionState").equals(1)) {
                    ruTask.setSuspensionState(SuspensionState.ACTIVE);
                } else {
                    ruTask.setSuspensionState(SuspensionState.SUSPEND);
                }
            }
            BeanUtils.copyProperties(ruTaskMap, ruTask, "ndeType", "suspensionState");
            ruTaskList.add(ruTask);
        }

        return new PageImpl<>(ruTaskList, pageDTO.toPageable(), count);

    }

    @Override
    public Page<TodoTaskDTO> getAllTodoTaskList(Long orgId,
                                                Long projectId,
                                                TodoTaskCriteriaDTO taskCriteria,
                                                PageDTO pageDTO,
                                                List<Long> entityIdList) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("ac.id as actInstId, ");
        sql.append("ru.id as actTaskId, ");
        sql.append("ac.entity_sub_type as entitySubType, ");
        sql.append("ac.entity_no as entityNo, ");
        sql.append("ac.drawing_title as drawingTitle, ");
        sql.append("ru.suspension_state_ as suspensionState, ");
        sql.append("ac.process_name as process, ");
        sql.append("ac.process_stage_name as processStage, ");
        sql.append("ac.act_category as processCategory, ");
        sql.append("ac.entity_id as entityId, ");
        sql.append("ac.version as version, ");
        sql.append("ac.task_package_id as taskPackageId, ");
        sql.append("ac.task_package_name as taskPackageName, ");
        sql.append("ac.team_id as teamId, ");
        sql.append("ac.team_name as teamName, ");
        sql.append("ac.weld_repair_count as weldRepairCount, ");
        sql.append("ac.weld_penalty_count as weldPenaltyCount, ");
        sql.append("ac.nde_type as ndeType, ");
        sql.append("ac.work_site_id as workSiteId, ");
        sql.append("ac.work_site_name as workSiteName, ");
        sql.append("ac.work_site_address as workSiteAddress, ");
        sql.append("ac.owner_name as ownerName, ");
        sql.append("ec.sub_drawing_flg as subDrawingFlg, ");
        sql.append("ru.create_time_ as taskCreatedTime, ");
        sql.append("ru.name_ as taskNode, ");
        sql.append("ru.task_id as taskId, ");
        sql.append("ru.task_def_key_ as taskDefKey, ");
        sql.append("ru.is_handling as isHandling, ");
        sql.append("ru.assignee_ as taskAssignName, ");
        sql.append("ac.memo as memo ");
        sql.append("FROM bpm_activity_instance AS ac ");
        sql.append("inner join bpm_ru_task AS ru on ac.id = ru.act_inst_id ");
        sql.append("inner join bpm_entity_sub_type AS ec on ec.id = ac.entity_sub_type_id ");
        sql.append("WHERE ");
        sql.append(" ac.project_id = :projectId ");

        if (entityIdList != null && entityIdList.size() > 0) {
            sql.append("AND ac.entity_id in (:entityIds) ");
        }

        if (taskCriteria.getHandling() != null) {
            if (taskCriteria.getHandling() == false) {
                sql.append("AND ru.is_handling is null ");
            } else {
                sql.append("AND ru.is_handling = :isHandling ");
            }
        }

        if (taskCriteria.getProcessCategoryId() != null) {
            sql.append("AND ac.process_category_id = :processCategoryId ");
        }

        if (taskCriteria.getEntitySubTypeId() != null) {
            sql.append("AND ac.entity_sub_type_id = :entitySubTypeId ");
        }

        if (taskCriteria.getProcessStageId() != null) {
            sql.append("AND ac.process_stage_id = :processStageId ");
        }

        if (taskCriteria.getProcessId() != null) {
            sql.append("AND ac.process_id = :processId ");
        }

        if (taskCriteria.getEntityNo() != null
            && !"".equals(taskCriteria.getEntityNo())) {
            sql.append("AND (ac.entity_no LIKE :entityNo or ac.drawing_title LIKE :entityNo) ");
        }

        if (taskCriteria.getTaskNode() != null
            && !"".equals(taskCriteria.getTaskNode())) {
            sql.append("AND ru.name_ LIKE :taskNode ");
        }

        if (taskCriteria.getTaskDefKey() != null
            && !"".equals(taskCriteria.getTaskDefKey())) {
            sql.append("AND ru.task_def_key_ = :taskDefKey ");
        }

        if (taskCriteria.getEntityModuleNames() != null
            && !"".equals(taskCriteria.getEntityModuleNames())) {
            sql.append("AND ac.entity_module_name LIKE :entityModuleName ");
        }
        if (taskCriteria.getTeamName() != null
            && !"".equals(taskCriteria.getTeamName())) {
            sql.append("AND ac.team_name LIKE :teamName ");
        }

        if (taskCriteria.getTaskPackageName() != null
            && !"".equals(taskCriteria.getTaskPackageName())) {
            sql.append("AND ac.task_package_name LIKE :taskPackageName ");
        }

        if (taskCriteria.getWorkSiteName() != null
            && !"".equals(taskCriteria.getWorkSiteName())) {
            sql.append("AND ac.work_site_name LIKE :workSiteName ");
        }

        if (taskCriteria.getWorkSiteAddress() != null
            && !"".equals(taskCriteria.getWorkSiteAddress())) {
            sql.append("AND ac.work_site_address = :workSiteAddress ");
        }

        if (!taskCriteria.isPageable()) {
//            sql.append("AND ((ru.task_def_key_ = 'usertask-ENGINEER-INPUT-NDT-REPORT' AND ac.weld_repair_count = 0 AND ac.weld_penalty_count = 0) OR (ru.task_def_key_ <> 'usertask-ENGINEER-INPUT-NDT-REPORT' )) ");
            sql.append("AND ((ru.task_def_key_ = '");
            sql.append(BpmTaskDefKey.USERTASK_INPUT_NDT_TEST_REPORT.getType());// usertask-ENGINEER-INPUT-NDT-REPORT);
            sql.append("' AND ac.weld_repair_count = 0 AND ac.weld_penalty_count = 0) OR (ru.task_def_key_ <> '");
            sql.append(BpmTaskDefKey.USERTASK_INPUT_NDT_TEST_REPORT.getType());//usertask-ENGINEER-INPUT-NDT-REPORT);
            sql.append("' )) ");
        }

        if (taskCriteria.getCreateDateFromTime() != null) {
            sql.append("AND ru.create_time_ >= :createDateFrom ");
        }

        if (taskCriteria.getCreateDateUntilTime() != null) {
            sql.append("AND ru.create_time_ <= :createDateUntil ");
        }

        if (taskCriteria.getStateSearch() != null && !"".equals(taskCriteria.getStateSearch())) {
            sql.append("AND ac.suspension_state = :suspensionState ");
        }

        if (taskCriteria.getClientType() != null && !"".equals(taskCriteria.getClientType())) {
            if (taskCriteria.getClientType().equals("pc-batch")) {
                sql.append(" ORDER BY ac.entity_no ");
            } else {
                sql.append(" ORDER BY ru.create_time_ desc ");
            }
        }
        sql.append("LIMIT :start , :offset ");

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (taskCriteria.getProcessCategoryId() != null
            && !"".equals(taskCriteria.getProcessCategoryId())) {
            query.setParameter("processCategoryId", taskCriteria.getProcessCategoryId());
            countQuery.setParameter("processCategoryId", taskCriteria.getProcessCategoryId());
        }

        if (taskCriteria.getEntitySubTypeId() != null
            && !"".equals(taskCriteria.getEntitySubTypeId())) {
            query.setParameter("entitySubTypeId", taskCriteria.getEntitySubTypeId());
            countQuery.setParameter("entitySubTypeId", taskCriteria.getEntitySubTypeId());
        }

        if (taskCriteria.getProcessStageId() != null
            && !"".equals(taskCriteria.getProcessStageId())) {
            query.setParameter("processStageId", taskCriteria.getProcessStageId());
            countQuery.setParameter("processStageId", taskCriteria.getProcessStageId());
        }

        if (taskCriteria.getProcessId() != null
            && !"".equals(taskCriteria.getProcessId())) {
            query.setParameter("processId", taskCriteria.getProcessId());
            countQuery.setParameter("processId", taskCriteria.getProcessId());
        }

//        if (taskCriteria.getTeamName() != null
//            && !"".equals(taskCriteria.getTeamName())) {
//            query.setParameter("teamName", taskCriteria.getTeamName());
//            countQuery.setParameter("teamName", taskCriteria.getTeamName());
//        }

        if (taskCriteria.getEntityNo() != null
            && !"".equals(taskCriteria.getEntityNo())) {
            query.setParameter("entityNo", "%" + taskCriteria.getEntityNo() + "%");
            countQuery.setParameter("entityNo", "%" + taskCriteria.getEntityNo() + "%");
        }

        if (taskCriteria.getTaskNode() != null
            && !"".equals(taskCriteria.getTaskNode())) {
            query.setParameter("taskNode", taskCriteria.getTaskNode());
            countQuery.setParameter("taskNode", taskCriteria.getTaskNode());
        }

        if (taskCriteria.getEntityModuleNames() != null
            && !"".equals(taskCriteria.getEntityModuleNames())) {
            query.setParameter("entityModuleName", "%" + taskCriteria.getEntityModuleNames() + "%");
            countQuery.setParameter("entityModuleName", "%" + taskCriteria.getEntityModuleNames() + "%");
        }
        if (taskCriteria.getTeamName() != null
            && !"".equals(taskCriteria.getTeamName())) {
            query.setParameter("teamName", "%" + taskCriteria.getTeamName() + "%");
            countQuery.setParameter("teamName", "%" + taskCriteria.getTeamName() + "%");
        }

        if (taskCriteria.getTaskPackageName() != null
            && !"".equals(taskCriteria.getTaskPackageName())) {
            query.setParameter("taskPackageName", "%" + taskCriteria.getTaskPackageName() + "%");
            countQuery.setParameter("taskPackageName", "%" + taskCriteria.getTaskPackageName() + "%");
        }
        if (taskCriteria.getWorkSiteName() != null
            && !"".equals(taskCriteria.getWorkSiteName())) {
            query.setParameter("workSiteName", "%" + taskCriteria.getWorkSiteName() + "%");
            countQuery.setParameter("workSiteName", "%" + taskCriteria.getWorkSiteName() + "%");
        }

        if (taskCriteria.getWorkSiteAddress() != null
            && !"".equals(taskCriteria.getWorkSiteAddress())) {
            query.setParameter("workSiteAddress", taskCriteria.getWorkSiteAddress());
            countQuery.setParameter("workSiteAddress", taskCriteria.getWorkSiteAddress());
        }

        if (taskCriteria.getHandling() != null && taskCriteria.getHandling() == true) {
            query.setParameter("isHandling", taskCriteria.getHandling());
            countQuery.setParameter("isHandling", taskCriteria.getHandling());
        }

        if (taskCriteria.getStateSearch() != null) {
            query.setParameter("suspensionState", taskCriteria.getStateSearch());
            countQuery.setParameter("suspensionState", taskCriteria.getStateSearch());
        }


        if (taskCriteria.getCreateDateFromTime() != null) {
            GregorianCalendar dateFrom = new GregorianCalendar();
            dateFrom.setTime(taskCriteria.getCreateDateFromTime());
            dateFrom.set(GregorianCalendar.HOUR_OF_DAY, 0);
            dateFrom.set(GregorianCalendar.MINUTE, 0);
            dateFrom.set(GregorianCalendar.SECOND, 0);
            query.setParameter("createDateFrom", dateFrom.getTime());
            countQuery.setParameter("createDateFrom", dateFrom.getTime());
        }

        if (taskCriteria.getCreateDateUntilTime() != null) {
            GregorianCalendar dateUntil = new GregorianCalendar();
            dateUntil.setTime(taskCriteria.getCreateDateUntilTime());
            dateUntil.set(GregorianCalendar.HOUR_OF_DAY, 23);
            dateUntil.set(GregorianCalendar.MINUTE, 59);
            dateUntil.set(GregorianCalendar.SECOND, 59);
            query.setParameter("createDateUntil", dateUntil.getTime());
            countQuery.setParameter("createDateUntil", dateUntil.getTime());
        }

        if (taskCriteria.getTaskDefKey() != null
            && !"".equals(taskCriteria.getTaskDefKey())) {
            query.setParameter("taskDefKey", taskCriteria.getTaskDefKey());
            countQuery.setParameter("taskDefKey", taskCriteria.getTaskDefKey());
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        if (entityIdList != null && entityIdList.size() > 0) {
            query.setParameter("entityIds", entityIdList);
            countQuery.setParameter("entityIds", entityIdList);
        }

        // 分页参数
        int pageNo = pageDTO.getPage().getNo();
        int pageSize = pageDTO.getPage().getSize();
        if (!taskCriteria.isPageable()) {
            pageSize = Integer.MAX_VALUE;
            pageDTO.getPage().setSize(pageSize);
        }
        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<TodoTaskDTO> ruTaskList = new ArrayList<TodoTaskDTO>();
//        Set<String> keys = new HashSet<String>();
        // String[] keyy = {"taskId", "assignee", "category", "createTime", "delegation", "description", "name" , "owner", "parentTaskId", "suspensionState", "taskDefKey", "tenantId", "actInstId", "documents"};
        // keys.addAll(Arrays.asList(keyy));

        for (Map<String, Object> ruTaskMap : queryResultList) {
            TodoTaskDTO ruTask = new TodoTaskDTO();
            if (ruTaskMap.get("ndeType") != null) {
                ruTask.setNdeType(NDEType.getByDisplayName((String) ruTaskMap.get("ndeType")));
            }
            if (ruTaskMap.get("suspensionState") != null) {
                if (ruTaskMap.get("suspensionState").equals(1)) {
                    ruTask.setSuspensionState(SuspensionState.ACTIVE);
                } else {
                    ruTask.setSuspensionState(SuspensionState.SUSPEND);
                }
            }
            BeanUtils.copyProperties(ruTaskMap, ruTask, "ndeType", "suspensionState");
            ruTaskList.add(ruTask);
        }

        return new PageImpl<>(ruTaskList, pageDTO.toPageable(), count.longValue());

    }


    @Override
    public List<TodoTaskMobileCriteriaDTO> getTodoTaskForMobile(Long orgId, Long projectId, String assignee) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append("bp.id              AS  processId,");
        sql.append("bps.name_en        AS  stageNameEn, ");
        sql.append("bps.name_cn        AS  stageNameCn, ");
        sql.append("bp.name_en         AS  processNameEn, ");
        sql.append("bp.name_cn         AS  processNameCn, ");
        sql.append("ru.name_           AS  taskNode, ");
        sql.append("ru.task_def_key_   AS  taskDefKey, ");
        sql.append("count(ac.id)       AS  amount ");
        sql.append("FROM ");
        sql.append("bpm_activity_instance_base ac, ");
        sql.append("bpm_process          bp, ");
        sql.append("bpm_process_stage     bps, ");
        sql.append("bpm_ru_task            ru ");
        sql.append("WHERE ");
        sql.append("AND ac.project_id = :projectId ");
        sql.append("AND bp.process_stage_id = bps.id ");
        sql.append("AND ac.process_id = bp.id ");

        sql.append("ru.act_inst_id = ac.id ");
        sql.append("AND ru.task_def_key_ in (:mobileTaskNodes) ");

        sql.append("AND ac.`status` = 'ACTIVE' ");
        if (assignee != null && !"".equals(assignee)) {
            sql.append("AND ru.assignee_ LIKE :assignee ");
        }
        sql.append("GROUP BY bp.id , bp.name_en, bp.name_cn , ru.name_, ru.task_def_key_");

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectId", projectId);
        query.setParameter("mobileTaskNodes", MOBILE_TASK_NODES);
        if (assignee != null && !"".equals(assignee)) {
            query.setParameter("assignee", "%" + assignee + "%");
        }

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        // 设置结果（结构为MAP）到WPS对象中
        List<TodoTaskMobileCriteriaDTO> ruTaskList = new ArrayList<TodoTaskMobileCriteriaDTO>();
        for (Map<String, Object> wpsMap : queryResultList) {
            TodoTaskMobileCriteriaDTO ruTask = new TodoTaskMobileCriteriaDTO();
            BeanUtils.copyProperties(wpsMap, ruTask);
            ruTaskList.add(ruTask);
        }
        return ruTaskList;

    }


    @Override
    public Page<BpmRuTask> getExternalInspectionRuTaskList(Long orgId, Long projectId, String assignee,
                                                           List<Long> actInstIds, PageDTO pageDTO) {
        SQLQueryBuilder<BpmRuTask> builder = getSQLQueryBuilder(BpmRuTask.class).like("name", "外检安排")
            .like("assignee", assignee).in("actInstId", actInstIds);
        builder.sort(new Sort.Order(Sort.Direction.DESC, "createTime"));
        return builder.paginate(pageDTO.toPageable()).exec().page();
    }

    @Override
    public Page<BpmExInspApply> getExternalInspectionApplyList(Long orgId, Long projectId,
                                                               List<String> assignees, ExInspApplyCriteriaDTO criteriaDTO) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ")
            .append(" `bai`.`org_id` AS `orgId`,")
            .append(" `bai`.`project_id` AS `projectId`,")
            .append(" `bai`.`id` AS `actInstId`,")
            .append(" `bai`.`entity_no` AS `entityNo`,")
            .append(" `brt`.`create_time_` AS `createTime`,")
            .append(" `brt`.`assignee_` AS `assignee`,")
            .append(" `bai`.`process_stage_name_cn` AS `process_stage_name_en`,")
            .append(" `bai`.`a.process_name` AS `process_name_en`,")
            .append(" `bai`.`entity_module_name` AS `entityModuleName`,")
            .append(" CONCAT(`bai`.`process_stage_name_cn`, '-', `bai`.`a.process_name`) AS `processStageName`,")
            .append(" `bai`.`entity_sub_type` AS `entityCategoryNameEn`,")
            .append(" `bai`.`entity_sub_type_id` AS `entitySubTypeId`,")
            .append(" `brt`.`task_id` AS `taskId`,")
            .append(" `baib`.`inspect_parties` AS `inspectParties` ")
            .append(" FROM `bpm_activity_instance_base` `bai` ")
            .append(" INNER JOIN `bpm_activity_instance_state` `acs` on `bai`.project_id = `acs`.project_id AND `bai`.id = `acs`.bai_id ")
            .append(" LEFT JOIN `bpm_activity_instance_blob` `baib` ")
            .append(" ON `bai`.`id` =`baib`.`bai_id` ")
            .append(" JOIN bpm_ru_task brt ")
            .append(" ON `brt`.`act_inst_id` = `bai`.`id` ")
            .append(" WHERE bai.project_id = :projectId ")
            .append(" AND `brt`.`task_type` = :taskType")
            .append(" AND ISNULL(`brt`.`apply_status`) ")
            .append(" AND brt.assignee_ LIKE :assignee ");


        String entityNo = criteriaDTO.getEntityNo();
        if (entityNo != null && !"".equals(entityNo)) {
            sql.append("AND bai.entity_no LIKE :entityNo ");
        }


        String processStart = criteriaDTO.getProcessStart();
        if (processStart != null && !"".equals(processStart)) {
            sql.append("AND bai.process_name = :processStart ");
        }

        String processStage = criteriaDTO.getProcessStage();
        if (processStage != null && !"".equals(processStage)) {
            sql.append("AND bai.process_stage_name = :processStage ");
        }
        String entityModuleNames = criteriaDTO.getEntityModuleNames();
        if (entityModuleNames == null || "".equals(entityModuleNames) || "无".equals(entityModuleNames)) {
            sql.append("AND bai.entity_module_name is null ");
        } else {
            sql.append("AND bai.entity_module_name = :entityModuleName ");
        }

        String entityCategories = criteriaDTO.getEntitySubTypeNameCns();
        if (entityCategories != null && !"".equals(entityCategories)) {
            sql.append("AND bai.entity_sub_type in (:entityCategories) ");
        }

        sql.append("order by entityNo ");

        sql.append("LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        query.setParameter("projectId", projectId);
        query.setParameter("assignee", "%" + assignees.get(0) + "%");

        String taskType = BpmTaskType.EX_INSP_APPLY.name();

        query.setParameter("taskType", taskType);

        countQuery.setParameter("projectId", projectId);
        countQuery.setParameter("assignee", "%" + assignees.get(0) + "%");
        countQuery.setParameter("taskType", taskType);

        processStage = criteriaDTO.getProcessStage();
        if (processStage != null && !"".equals(processStage)) {
            query.setParameter("processStage", processStage);
            countQuery.setParameter("processStage", processStage);
        }

        entityNo = criteriaDTO.getEntityNo();
        if (entityNo != null && !"".equals(entityNo)) {
            query.setParameter("entityNo", "%" + entityNo + "%");
            countQuery.setParameter("entityNo", "%" + entityNo + "%");
        }

        processStart = criteriaDTO.getProcessStart();
        if (processStart != null && !"".equals(processStart)) {
            query.setParameter("processStart", processStart);
            countQuery.setParameter("processStart", processStart);
        }

        if (entityModuleNames != null && !"".equals(entityModuleNames) && !"无".equals(entityModuleNames)) {
            query.setParameter("entityModuleName", entityModuleNames);
            countQuery.setParameter("entityModuleName", entityModuleNames);
        }

        if (entityCategories != null && !"".equals(entityCategories)) {
            if (entityCategories.contains(",")) {
                String[] entityCategoriesArray = entityCategories.split(",");
                List<String> valueEntityCategoryList = new ArrayList<String>();
                valueEntityCategoryList.addAll(Arrays.asList(entityCategoriesArray));
                query.setParameter("entityCategories", valueEntityCategoryList);
                countQuery.setParameter("entityCategories", valueEntityCategoryList);
            } else {
                query.setParameter("entityCategories", entityCategories);
                countQuery.setParameter("entityCategories", entityCategories);
            }
        }

        int pageNo = criteriaDTO.getPage().getNo();
        int pageSize = criteriaDTO.getPage().getSize();
        if (criteriaDTO.getFetchAll()) {
            pageSize = Integer.MAX_VALUE;
            criteriaDTO.getPage().setSize(pageSize);
        }
        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> list = query.getResultList();
        Long count = (Long) countQuery.getSingleResult();

        List<BpmExInspApply> rsList = new ArrayList<BpmExInspApply>();
        for (Map<String, Object> wpsMap : list) {
            BpmExInspApply apply = new BpmExInspApply();
            BeanUtils.copyProperties(wpsMap, apply);
            rsList.add(apply);
        }

        return new PageImpl<>(rsList, criteriaDTO.toPageable(), count.longValue());


    }

    @Override
    public List<HierarchyBaseDTO> getEntitiyCategoriesInRuTask(List<String> entityModuleNames, String taskNode, String taskDefKey,
                                                               Long processStageId, Long processId, Long orgId, Long projectId, String assignee) {

        EntityManager entityManager = getEntityManager();
        StringBuffer sql = new StringBuffer()
            .append(" SELECT distinct t.entity_sub_type_id, s.name_cn, s.name_en FROM bpm_activity_instance_base t ")
            .append(" INNER JOIN bpm_activity_instance_state ts on t.id = ts.bai_id ")
            .append(" INNER JOIN bpm_ru_task r on t.id = r.act_inst_id ")
            .append(" INNER JOIN bpm_entity_sub_type s on t.entity_sub_type_id = s.id ")
            .append(" WHERE t.project_id = :projectId ")
            .append(" AND t.process_stage_id = :processStageId AND t.process_id = :processId")
            .append(" AND t.status = 'ACTIVE' ")
            .append(" AND ts.suspension_state = 'ACTIVE' AND ts.finish_state = 'NOT_FINISHED'")
            .append(" AND r.assignee_ LIKE :assignee ");

        if (entityModuleNames != null && !entityModuleNames.isEmpty()) {
            sql.append(" AND t.entity_module_name in (:entityModuleNames)");
        }
        if (taskNode != null) {
            sql.append(" AND r.name_ = :taskName");
        }
        if (taskDefKey != null) {
            sql.append(" AND r.task_def_key_  = :taskDefKey");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);
        query.setParameter("processStageId", processStageId);
        query.setParameter("processId", processId);
        query.setParameter("assignee", "%" + assignee + "%");

        if (entityModuleNames != null && !entityModuleNames.isEmpty()) {
            query.setParameter("entityModuleNames", entityModuleNames);
        }
        if (taskNode != null) {
            query.setParameter("taskName", taskNode);
        }
        if (taskDefKey != null) {
            query.setParameter("taskDefKey", taskDefKey);
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<HierarchyBaseDTO> rsList = new ArrayList<>();
        for (Map<String, Object> m : list) {
            HierarchyBaseDTO dto = new HierarchyBaseDTO();
            dto.setId(Long.parseLong(m.get("entity_sub_type_id").toString()));
            dto.setNameCn((String) m.get("name_cn"));
            dto.setNameEn((String) m.get("name_en"));
            rsList.add(dto);
        }

        return rsList;
    }

    @Override
    public List<HierarchyBaseCountDTO> getProcessStagesInRuTask(Long orgId, Long projectId, String assignee, HierarchyCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        String sql = new StringBuffer()
            .append(" SELECT t.process_stage_id, s.name_cn, s.name_en, count(0) count FROM bpm_activity_instance_base t ")
            .append(" INNER JOIN bpm_activity_instance_state ts ON t.id = ts.bai_id ")
            .append(" INNER JOIN bpm_ru_task r ON t.id = r.act_inst_id ")
            .append(" INNER JOIN bpm_process_stage s ON t.process_stage_id = s.id ")
            .append(" WHERE t.project_id = :projectId AND ts.finish_state = 'NOT_FINISHED'")
            .append(" AND ts.suspension_state = 'ACTIVE' AND t.status = 'ACTIVE'")
            .append(" AND r.assignee_ = :assignee ")
            .append(" GROUP BY t.process_stage_id, s.name_cn, s.name_en ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter("orgId", orgId);
        query.setParameter("projectId", projectId);
        query.setParameter("assignee", assignee);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<HierarchyBaseCountDTO> rsList = new ArrayList<>();
        for (Map<String, Object> m : list) {
            HierarchyBaseCountDTO dto = new HierarchyBaseCountDTO();
            dto.setId(Long.parseLong(m.get("process_stage_id").toString()));
            dto.setNameCn((String) m.get("name_cn"));
            dto.setNameEn((String) m.get("name_en"));
            dto.setCount(Long.parseLong(m.get("count").toString()));
            rsList.add(dto);
        }

        return rsList;
    }

    @Override
    public List<HierarchyBaseDTO> getProcessesInRuTask(Long processStageId, Long orgId, Long projectId, String assignee) {

        EntityManager entityManager = getEntityManager();
        String sql = new StringBuffer()
            .append(" SELECT distinct t.process_id, s.name_cn, s.name_en FROM bpm_activity_instance_base t ")
            .append(" JOIN bpm_activity_instance_state ts ON t.id = ts.bai_id ")
            .append(" JOIN bpm_ru_task r ON t.id = r.act_inst_id ")
            .append(" JOIN bpm_process s ON t.process_id = s.id ")
            .append(" WHERE t.project_id = :projectId ")
            .append(" AND t.process_stage_id = :processStageId")
            .append(" AND ts.finish_state = 'NOT_FINISHED' AND ts.suspension_state = 'ACTIVE' AND t.status = 'ACTIVE'")
            .append(" AND r.assignee_ = :assignee ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter("orgId", orgId);
        query.setParameter("projectId", projectId);
        query.setParameter("assignee", assignee);
        query.setParameter("processStageId", processStageId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<HierarchyBaseDTO> rsList = new ArrayList<HierarchyBaseDTO>();
        for (Map<String, Object> m : list) {
            HierarchyBaseDTO dto = new HierarchyBaseDTO();
            dto.setId(Long.parseLong(m.get("process_id").toString()));
            dto.setNameCn((String) m.get("name_cn"));
            dto.setNameEn((String) m.get("name_en"));
            rsList.add(dto);
        }

        return rsList;
    }


    @Override
    public List<HierarchyBaseCountDTO> getProcessesInRuTask(Long projectId, String assignee, HierarchyCriteriaDTO criteriaDTO) {
        EntityManager entityManager = getEntityManager();
        StringBuffer shortBuffer = new StringBuffer()
            .append(" SELECT t.process_id, s.name_cn, s.name_en, count(0) count, t.process_stage_id ")
            .append(" FROM bpm_activity_instance_base t ")
            .append(" JOIN bpm_activity_instance_state ts ")
            .append(" ON t.id = ts.bai_id ")
            .append(" JOIN bpm_ru_task r ON t.id = r.act_inst_id ")
            .append(" JOIN bpm_process s ON t.process_id = s.id ")
            .append(" WHERE t.project_id = :projectId ")
            .append(" AND ts.finish_state = 'NOT_FINISHED' AND ts.suspension_state = 'ACTIVE' AND t.status = 'ACTIVE'")
            .append(" AND r.assignee_ = :assignee ");

        if (criteriaDTO.getProcessStageId() != null) {
            shortBuffer.append(" AND t.process_stage_id = :processStageId ");
        }
        if (criteriaDTO.getDiscipline() != null) {
            shortBuffer.append(" AND s.discipline = :discipline ");
        }
        shortBuffer.append(" GROUP BY t.process_id, s.name_cn, s.name_en,t.process_stage_id ");

        String sql = shortBuffer.toString();

        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("projectId", projectId);
        query.setParameter("assignee", assignee);
        if (criteriaDTO.getProcessStageId() != null) {
            query.setParameter("processStageId", criteriaDTO.getProcessStageId());
        }
        if (criteriaDTO.getDiscipline() != null) {
            query.setParameter("discipline", criteriaDTO.getDiscipline());
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<HierarchyBaseCountDTO> rsList = new ArrayList<>();
        for (Map<String, Object> m : list) {
            HierarchyBaseCountDTO dto = new HierarchyBaseCountDTO();
            dto.setId(Long.parseLong(m.get("process_id").toString()));
            dto.setNameCn((String) m.get("name_cn"));
            dto.setNameEn((String) m.get("name_en"));
            dto.setCount(Long.parseLong(m.get("count").toString()));
            dto.setProcessStageId(Long.parseLong(m.get("process_stage_id").toString()));
            rsList.add(dto);
        }

        return rsList;
    }

    @Override
    public List<TaskNodeDTO> getTaskNodesInRuTask(Long processStageId, Long processId, Long orgId,
                                                  Long projectId, String assignee) {

        EntityManager entityManager = getEntityManager();
        String sql = new StringBuffer()
            .append(" SELECT DISTINCT r.name_, r.task_def_key_  FROM bpm_activity_instance_base t ")
            .append("INNER JOIN bpm_activity_instance_state ts ON t.id = ts.bai_id ")
            .append("INNER JOIN bpm_ru_task r ON t.id = r.act_inst_id ")
            .append(" WHERE t.project_id = :projectId ")
            .append(" AND t.process_stage_id = :processStageId AND t.process_id = :processId")
            .append(" AND ts.suspension_state = 'ACTIVE' AND ts.finish_state = 'NOT_FINISHED'  AND t.status = 'ACTIVE'")
            .append(" AND r.assignee_ LIKE :assignee ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("projectId", projectId);
        query.setParameter("assignee", "%" + assignee + "%");
        query.setParameter("processStageId", processStageId);
        query.setParameter("processId", processId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<TaskNodeDTO> rsList = new ArrayList<TaskNodeDTO>();
        for (Map<String, Object> m : list) {
            rsList.add(new TaskNodeDTO((String) m.get("task_def_key_"), (String) m.get("name_")));
        }

        return rsList;
    }

    @Override
    public List<String> getProcessCategoriesInRuTask(Long orgId, Long projectId, String assignee) {
        EntityManager entityManager = getEntityManager();
        String sql = new StringBuffer()
            .append(" SELECT DISTINCT t.process_category_id FROM bpm_activity_instance_base t ")
            .append(" inner join bpm_activity_instance_state ts on t.id = ts.bai_id ")
            .append(" inner join bpm_ru_task r on t.id = r.act_inst_id ")
            .append(" WHERE t.project_id = :projectId AND t.status = 'ACTIVE'")
            .append(" AND ts.suspension_state = 'ACTIVE' AND ts.finish_state = 'NOT_FINISHED'")
            .append(" AND r.assignee_ LIKE :assignee ").toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projectId", projectId);
        query.setParameter("assignee", "%" + assignee + "%");

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<String> rsList = new ArrayList<String>();
        for (Map<String, Object> m : list) {
            rsList.add((String) m.get("process_category_id"));
        }

        return rsList;
    }


    @Override
    public List<String> getEntityModuleNameInRuTask(String taskNode, String taskDefKey, Long processStageId, Long processId,
                                                    Long orgId, Long projectId, String assignee) {

        EntityManager entityManager = getEntityManager();
        String sql = new StringBuffer()
            .append(" SELECT distinct t.entity_module_name FROM bpm_activity_instance_base t")
            .append(" INNER JOIN bpm_activity_instance_state ts on t.id = ts.bai_id ")
            .append(" inner join bpm_ru_task r on t.id = r.act_inst_id ")
            .append(" WHERE t.project_id = :projectId AND t.status = 'ACTIVE'")
            .append(" AND ts.suspension_state = 'ACTIVE' AND ts.finish_state = 'NOT_FINISHED'")
            .append(" AND t.process_stage_id = :processStageId AND t.process_id = :processId")
            .append(" AND r.assignee_ LIKE :assignee ")
            .toString();

        if (taskNode != null) {
            sql += " AND r.name_ = :taskName";
        }
        if (taskDefKey != null) {
            sql += " AND r.task_def_key_  = :taskDefKey";
        }

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projectId", projectId);
        query.setParameter("assignee", "%" + assignee + "%");
        query.setParameter("processStageId", processStageId);
        query.setParameter("processId", processId);
        if (taskNode != null) {
            query.setParameter("taskName", taskNode);
        }
        if (taskDefKey != null) {
            query.setParameter("taskDefKey", taskDefKey);
        }

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<String> rsList = new ArrayList<String>();
        for (Map<String, Object> m : list) {
            if (m.get("entity_module_name") != null) {
                rsList.add((String) m.get("entity_module_name"));
            }
        }

        return rsList;
    }


    /**
     * 设置查询结果到对象中。
     *
     * @param result 查询
     */
    private void copyProperties(Map<String, Object> result, BpmRuTask ruTask) {

        if (result == null) {
            return;
        }

        if (result.get("id") != null) {
            ruTask.setId(Long.parseLong(result.get("id").toString()));
        }

        if (result.get("act_inst_id") != null) {
            ruTask.setActInstId((Long) result.get("act_inst_id"));
        }

        if (result.get("task_id") != null) {
            ruTask.setId((Long) result.get("task_id"));
        }

        if (result.get("create_time_") != null) {
            ruTask.setCreateTime((Timestamp) result.get("create_time_"));
        }

        if (result.get("name_") != null) {
            ruTask.setName((String) result.get("name_"));
        }
    }

    @Override
    public ExInspApplyFilterConditionDTO getExternalInspectionApplyFilterCondition(Long orgId,
                                                                                   Long projectId, List<Long> assignees) {
        EntityManager entityManager = getEntityManager();
        String sql = new StringBuffer().append(
            " SELECT t2.`entity_module_name`, t2.`entity_sub_type`, t2.`process_stage_name_cn`, t2.`a.process_name` ")
            .append(" FROM `bpm_activity_instance_base` `t2` ")
            .append(" INNER JOIN bpm_ru_task t1 ")
            .append(" ON  `t1`.`act_inst_id` = `t2`.`id`")
            .append(" WHERE t2.project_id = :projectId ")
            .append(" AND `t1`.`task_type` = :taskType ")
            .append(" AND (ISNULL(`t1`.`apply_status`) OR `t1`.`apply_status` = '') ")
            .append(" AND t1.assignee_ LIKE :assignee").toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projectId", projectId);
        query.setParameter("assignee", "%" + assignees.get(0).toString() + "%");
//        List<String> taskTypes = new ArrayList<>(
//            Arrays.asList(
//                BpmTaskType.EX_INSP_APPLY.getCode()
//            )
//        );
//        query.setParameter("taskTypes", taskTypes);
        query.setParameter("taskType", BpmTaskType.EX_INSP_APPLY.getCode());

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        ExInspApplyFilterConditionDTO dto = new ExInspApplyFilterConditionDTO();
        Set<String> processes = new HashSet<String>();
        Set<String> entityCategoryNameCns = new HashSet<String>();
        Set<String> entityModuleNames = new HashSet<String>();
        for (Map<String, Object> m : list) {
            if (m.get("a.process_name") != null && m.get("process_stage_name_cn") != null) {
                processes.add((String) m.get("process_stage_name_cn") + "-" + (String) m.get("a.process_name"));
            }
            if (m.get("entity_sub_type") != null) {
                entityCategoryNameCns.add((String) m.get("entity_sub_type"));
            }
            if (m.get("entity_module_name") != null) {
                entityModuleNames.add((String) m.get("entity_module_name"));
            }
        }
        dto.setProcesses(processes);
        dto.setEntityModuleNames(entityModuleNames);
        dto.setEntitySubTypeNameCns(entityCategoryNameCns);
        return dto;
    }

    @Override
    public List<HierarchyStageProcessDTO> getStageProcessesInRuTask(Long orgId, Long projectId, String assignee,
                                                                    String taskDefKey) {
        EntityManager entityManager = getEntityManager();
        String sql = new StringBuffer()
            .append(" SELECT DISTINCT t.process_stage_id, s.name_en AS stage_name, t.process_id, ss.name_en AS process_name ")
            .append("FROM bpm_activity_instance_base t ")
            .append(" JOIN bpm_activity_instance_state ts ON t.id = ts.bai_id ")
            .append(" JOIN bpm_ru_task r ON t.id = r.act_inst_id ")
            .append(" JOIN bpm_process_stage s ON t.process_stage_id = s.id ")
            .append(" JOIN bpm_process ss ON t.process_id = ss.id ")
            .append(" WHERE t.project_id = :projectId AND ts.finish_state = 'NOT_FINISHED' ")
            .append(" AND ts.suspension_state = 'ACTIVE' AND t.status = 'ACTIVE' ")
            .append(" AND r.assignee_ = :assignee ")
            .append(" AND r.task_def_key_ = :taskDefKey")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projectId", projectId);
        query.setParameter("assignee", assignee);
        query.setParameter("taskDefKey", taskDefKey);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<HierarchyStageProcessDTO> rsList = new ArrayList<HierarchyStageProcessDTO>();
        for (Map<String, Object> m : list) {
            HierarchyStageProcessDTO dto = new HierarchyStageProcessDTO();
            String displayName =
                (m.get("stage_name") == null ? "" : (String) m.get("stage_name")) + "-" +
                    (m.get("process_name") == null ? "" : (String) m.get("process_name"));
            dto.setDisplayName(displayName);
            dto.setProcessId((Long) m.get("process_id"));
            dto.setStageId((Long) m.get("process_stage_id"));
            rsList.add(dto);
        }

        return rsList;
    }

    @Override
    public Page<TodoTaskForemanDispatchDTO> searchForemanDispatchTodo(Long orgId, Long projectId, String assignee,
                                                                      TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO, List<Long> entityIdList) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("  GROUP_CONCAT(r.`id`) AS ruTaskIds,");
        sql.append("  ppn.`entity_type` AS parentType, ");

        sql.append("  CASE");
        sql.append("    ppn.`entity_type` ");
        sql.append("    WHEN 'ISO' ");
        sql.append("    THEN i.no ");
        sql.append("    WHEN 'SPOOL' ");
        sql.append("    THEN s.no ");
        sql.append("    ELSE 'parent not spool or iso' ");
        sql.append("  END AS parentNo,");

        sql.append("  act.`a.process_name` AS processName,");
        sql.append("  act.`process_stage_name_cn` AS stageName, ");
        sql.append("  MAX(r.create_time_) AS create_time_ ");
        sql.append("FROM");
        sql.append("  bpm_ru_task r ");
        sql.append("  JOIN bpm_activity_instance_base act ");
        sql.append("    ON r.`act_inst_id` = act.`id` ");
        sql.append("  JOIN entity_welds e ");
        sql.append("    ON e.`id` = act.`entity_id`  ");

        sql.append("  JOIN project_node pn ON e.`id` = pn.`entity_id` ");
        sql.append("  JOIN hierarchy_node hn ON hn.`node_id` = pn.`id` ");
        sql.append("  JOIN hierarchy_node phn ON hn.`parent_id` = phn.`id` ");
        sql.append("  JOIN project_node ppn ON ppn.`id` = phn.`node_id` ");
        sql.append("  LEFT JOIN entity_isos i ON ppn.`entity_id` = i.`id` AND ppn.`entity_type` = 'ISO' ");
        sql.append("  LEFT JOIN entity_spools s ON ppn.`entity_id` = s.`id` AND ppn.`entity_type` = 'SPOOL' ");

        sql.append("WHERE r.`task_type` = '" + BpmTaskType.FOREMAN_DISPATCH.name() + "'  ");
        sql.append("AND act.project_id = :projectId ");
        if (entityIdList != null && entityIdList.size() > 0) {
            sql.append("AND act.entity_id in (:entityIds) ");
        }
        sql.append("  AND (");
        sql.append("    act.`a.process_name` = '" + BpmsProcessNameEnum.WELD.getType() + "' ");
        sql.append("    OR act.`a.process_name` = '" + BpmsProcessNameEnum.FITUP.getType() + "'");
        sql.append("  ) ");
        sql.append("  AND (");
        sql.append("    act.`process_stage_name_cn` = '" + BpmProcessStageEnum.FABRICATION.getType() + "' ");
        sql.append("    OR act.`process_stage_name_cn` = '" + BpmProcessStageEnum.INSTALLATION.getType() + "'");
        sql.append("  ) ");

        if (assignee != null) {
            sql.append("AND r.assignee_ LIKE :assignee ");
        }

        if (entityIdList != null && entityIdList.size() > 0) {
            sql.append("AND act.entity_id in (:entityIds) ");
        }

        if (taskCriteria.getEntityNo() != null
            && !"".equals(taskCriteria.getEntityNo())) {
            sql.append("AND (act.entity_no LIKE :entityNo or act.drawing_title LIKE :entityNo) ");
        }

        sql.append(" GROUP BY parentType, parentNo, processName, stageName ");

        sql.append(" ORDER BY create_time_ desc ");
        sql.append("LIMIT :start , :offset ");

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);


        if (taskCriteria.getEntityNo() != null
            && !"".equals(taskCriteria.getEntityNo())) {
            query.setParameter("entityNo", "%" + taskCriteria.getEntityNo() + "%");
            countQuery.setParameter("entityNo", "%" + taskCriteria.getEntityNo() + "%");
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        if (assignee != null) {
            query.setParameter("assignee", "%" + assignee + "%");
            countQuery.setParameter("assignee", "%" + assignee + "%");
        }

        if (entityIdList != null && entityIdList.size() > 0) {
            query.setParameter("entityIds", entityIdList);
            countQuery.setParameter("entityIds", entityIdList);
        }

        // 分页参数
        int pageNo = pageDTO.getPage().getNo();
        int pageSize = pageDTO.getPage().getSize();
        if (!taskCriteria.isPageable()) {
            pageSize = Integer.MAX_VALUE;
            pageDTO.getPage().setSize(pageSize);
        }
        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<TodoTaskForemanDispatchDTO> ruTaskList = new ArrayList<>();

        for (Map<String, Object> wpsMap : queryResultList) {
            TodoTaskForemanDispatchDTO ruTask = new TodoTaskForemanDispatchDTO();
            BeanUtils.copyProperties(wpsMap, ruTask, "welds");
            String ruTaskIds = (String) wpsMap.get("ruTaskIds");
            String[] ruTasksIdArray = ruTaskIds.split(",");
            String[] ruTasksIdArrayNoRepeat = removeRepeatElem(ruTasksIdArray);
            List<TodoTaskDTO> welds = new ArrayList<>();
            for (int i = 0; i < ruTasksIdArrayNoRepeat.length; i++) {
                welds.add(getTodoTaskById(ruTasksIdArrayNoRepeat[i]));
            }
            ruTask.setWelds(welds);
            ruTaskList.add(ruTask);
        }

        return new PageImpl<>(ruTaskList, pageDTO.toPageable(), count.longValue());
    }

    private String[] removeRepeatElem(String[] ruTasksIdArray) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < ruTasksIdArray.length; i++) {
            if (!list.contains(ruTasksIdArray[i])) {
                list.add(ruTasksIdArray[i]);
            }
        }
        return list.toArray(new String[0]);
    }

    private TodoTaskDTO getTodoTaskById(String ruTaskId) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("ac.id AS actInstId, ");
        sql.append("ru.id AS actTaskId, ");
        sql.append("ac.entity_sub_type AS entitySubType, ");
        sql.append("ac.entity_no AS entityNo, ");
        sql.append("ac.drawing_title AS drawingTitle, ");
        sql.append("ac.process_name AS process, ");
        sql.append("ac.process_stage_name AS processStage, ");
        sql.append("ac.act_category AS processCategory, ");
        sql.append("ac.entity_id AS entityId, ");
        sql.append("ac.version AS version, ");
        sql.append("asc.task_package_id AS taskPackageId, ");
        sql.append("asc.task_package_name AS taskPackageName, ");
        sql.append("asc.team_id AS teamId, ");
        sql.append("asc.team_name AS teamName, ");
        sql.append("asc.weld_repair_count AS weldRepairCount, ");
        sql.append("asc.weld_penalty_count AS weldPenaltyCount, ");
        sql.append("asc.nde_type AS ndeType, ");
        sql.append("asc.work_site_id AS workSiteId, ");
        sql.append("asc.work_site_name AS workSiteName, ");
        sql.append("ac.owner_name AS ownerName, ");
        sql.append("ec.sub_drawing_flg AS subDrawingFlg, ");
        sql.append("ru.create_time_ AS taskCreatedTime, ");
        sql.append("ru.name_ AS taskNode, ");
        sql.append("ru.task_id AS taskId, ");
        sql.append("ru.task_def_key_ AS taskDefKey, ");
        sql.append("acs.memo AS memo ");
        sql.append("FROM bpm_activity_instance_base AS ac ");
        sql.append("inner join bpm_activity_instance_state AS acs on ac.project_id = acs.project_id AND ac.id = acs.bai_id ");
        sql.append("inner join bpm_ru_task AS ru on ac.id = ru.act_inst_id ");
        sql.append("inner join bpm_entity_sub_type AS ec on ec.id = ac.entity_sub_type_id ");
        sql.append("WHERE 1 = 1 ");
        sql.append("AND ru.id = :id ");

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("id", ruTaskId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        // 设置结果（结构为MAP）到WPS对象中
        TodoTaskDTO ruTask = new TodoTaskDTO();

        for (Map<String, Object> wpsMap : queryResultList) {
            if (wpsMap.get("ndeType") != null) {
                ruTask.setNdeType(NDEType.getByDisplayName((String) wpsMap.get("ndeType")));
            }
            BeanUtils.copyProperties(wpsMap, ruTask, "ndeType");
        }

        return ruTask;
    }

    @Override
    public HierarchyBaseCountDTO findDTOById(Long processId) {
        EntityManager entityManager = getEntityManager();
        String sql = new StringBuffer().append(
            " SELECT '0' count, id, name_cn, name_en, process_stage_id FROM bpm_process ")
            .append(" WHERE id = :processId AND status = 'ACTIVE'")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("processId", processId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        for (Map<String, Object> m : list) {
            HierarchyBaseCountDTO dto = new HierarchyBaseCountDTO();
            dto.setId(Long.parseLong(m.get("id").toString()));
            dto.setNameCn((String) m.get("name_cn"));
            dto.setNameEn((String) m.get("name_en"));
            dto.setCount(Long.parseLong(m.get("count").toString()));
            dto.setProcessStageId(Long.parseLong(m.get("process_stage_id").toString()));
            return dto;
        }

        return new HierarchyBaseCountDTO();
    }
}
