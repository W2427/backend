package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.BpmExecuteCaseDTO;
import com.ose.tasks.entity.bpm.BpmExecuteCase;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 任务处理流程case库
 */
public class BpmExecuteCaseRepositoryImpl extends BaseRepository implements BpmExecuteCaseRepositoryCustom {

    /**
     * 查找任务处理case，分页、筛选。
     *
     * @param bpmExecuteCaseDTO 查询参数
     * @param pageable          分页参数
     * @return
     */
    @Override
    public Page<BpmExecuteCase> list(BpmExecuteCaseDTO bpmExecuteCaseDTO, Pageable pageable) {

        SQLQueryBuilder<BpmExecuteCase> builder = getSQLQueryBuilder(BpmExecuteCase.class)
            .is("orgId", bpmExecuteCaseDTO.getOrgId())
            .is("projectId", bpmExecuteCaseDTO.getProjectId())
            .is("status", EntityStatus.ACTIVE);


        if (bpmExecuteCaseDTO.getDisplayName() != null) {
            builder.is("displayName", bpmExecuteCaseDTO.getDisplayName());
        }


        if (bpmExecuteCaseDTO.getDescription() != null) {
            builder.is("description", bpmExecuteCaseDTO.getDescription());
        }


        if (bpmExecuteCaseDTO.getActCategory() != null) {
            builder.is("actCategory", bpmExecuteCaseDTO.getActCategory());
        }


        if (bpmExecuteCaseDTO.getProcessNames() != null) {
            builder.is("processNames", bpmExecuteCaseDTO.getProcessNames());
        }


        if (bpmExecuteCaseDTO.getEntitySubType() != null) {
            builder.is("entitySubType", bpmExecuteCaseDTO.getEntitySubType());
        }


        if (bpmExecuteCaseDTO.getTaskDefKey() != null) {
            builder.is("taskDefKey", bpmExecuteCaseDTO.getTaskDefKey());
        }


        if (bpmExecuteCaseDTO.getPrivateCategory() != null) {
            builder.is("privateCategory", bpmExecuteCaseDTO.getPrivateCategory());
        }


        if (bpmExecuteCaseDTO.getExecClass() != null) {
            builder.is("execClass", bpmExecuteCaseDTO.getExecClass());
        }

        if (bpmExecuteCaseDTO.getCountersignFlag() != null) {
            builder.is("countersignFlag", bpmExecuteCaseDTO.getCountersignFlag());
        }




        return builder.paginate(pageable).exec().page();

    }

    /**
     * 查找特殊的任务处理case。
     *
     * @param bpmExecuteCaseDTO 查询参数
     * @return
     */
    @Override
    public List<BpmExecuteCase> search(BpmExecuteCaseDTO bpmExecuteCaseDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT *")
            .append(" FROM `bpm_execute_case`")
            .append(" WHERE `deleted` = FALSE AND 1=1");


        if (bpmExecuteCaseDTO.getOrgId() != null && !bpmExecuteCaseDTO.getOrgId().equals(0L)) {
            sql.append(" AND `org_id` = :orgId");
        }


        if (bpmExecuteCaseDTO.getProjectId() != null && !bpmExecuteCaseDTO.getProjectId().equals(0L)) {
            sql.append(" AND `project_id` = :projectId");
        }


        if (bpmExecuteCaseDTO.getDisplayName() != null && !"".equals(bpmExecuteCaseDTO.getDisplayName())) {
            sql.append(" AND `display_name` = :displayName");
        }


        if (bpmExecuteCaseDTO.getDescription() != null && !"".equals(bpmExecuteCaseDTO.getDescription())) {
            sql.append(" AND `description` = :description");
        }


        if (bpmExecuteCaseDTO.getActCategory() != null && !"".equals(bpmExecuteCaseDTO.getActCategory())) {
            sql.append(" AND `act_category` = :actCategory");
        }


        if (bpmExecuteCaseDTO.getProcessNames() != null && !"".equals(bpmExecuteCaseDTO.getProcessNames())) {
            sql.append(" AND `process_names` = :processNames");
        }


        if (bpmExecuteCaseDTO.getEntitySubType() != null && !"".equals(bpmExecuteCaseDTO.getEntitySubType())) {
            sql.append(" AND `entity_sub_type` = :entitySubType");
        }


        if (bpmExecuteCaseDTO.getTaskDefKey() != null && !"".equals(bpmExecuteCaseDTO.getTaskDefKey())) {
            sql.append(" AND `task_def_key` = :taskDefKey");
        }


        if (bpmExecuteCaseDTO.getMatchingKey() != null && !"".equals(bpmExecuteCaseDTO.getMatchingKey()) && bpmExecuteCaseDTO.getMatchingWord() != null && !"".equals(bpmExecuteCaseDTO.getMatchingWord())) {
            if (bpmExecuteCaseDTO.getMatchingFlag() != null && !"".equals(bpmExecuteCaseDTO.getMatchingFlag())) {
                switch (bpmExecuteCaseDTO.getMatchingFlag()) {


                    case "START_WITH":
                        sql.append(" AND " + bpmExecuteCaseDTO.getMatchingWord() + " LIKE concat( :matchingKey ,'%')");
                        break;


                    case "CONTAIN":
                        sql.append(" AND " + bpmExecuteCaseDTO.getMatchingWord() + " LIKE concat('%', :matchingKey ,'%')");
                        break;


                    case "END_WITH":
                        sql.append(" AND " + bpmExecuteCaseDTO.getMatchingWord() + " LIKE concat('%', :matchingKey )");
                        break;


                    case "START_WITH_NOT":
                        sql.append(" AND " + bpmExecuteCaseDTO.getMatchingWord() + " NOT LIKE concat( :matchingKey ,'%')");
                        break;


                    case "CONTAIN_NOT":
                        sql.append(" AND " + bpmExecuteCaseDTO.getMatchingWord() + " NOT LIKE concat('%', :matchingKey ,'%')");
                        break;


                    case "END_WITH_NOT":
                        sql.append(" AND " + bpmExecuteCaseDTO.getMatchingWord() + " NOT LIKE concat('%', :matchingKey )");
                        break;


                    default:
                        sql.append(" AND " + bpmExecuteCaseDTO.getMatchingWord() + " = :matchingKey");
                        break;

                }
            } else {

                sql.append(" AND " + bpmExecuteCaseDTO.getMatchingWord() + " = :matchingKey");
            }
        }


        if (bpmExecuteCaseDTO.getPrivateCategory() != null && !"".equals(bpmExecuteCaseDTO.getPrivateCategory())) {
            sql.append(" AND `private_category` = :privateCategory");
        }


        if (bpmExecuteCaseDTO.getExecClass() != null && !"".equals(bpmExecuteCaseDTO.getExecClass())) {
            sql.append(" AND `exec_class` = :execClass");
        }


        if (bpmExecuteCaseDTO.getCountersignFlag() != null && bpmExecuteCaseDTO.getCountersignFlag()) {
            sql.append(" AND `countersign_flag` = :countersignFlag");
        }

        Query query = entityManager.createNativeQuery(sql.toString());


        if (bpmExecuteCaseDTO.getOrgId() != null && !"".equals(bpmExecuteCaseDTO.getOrgId())) {
            query.setParameter("orgId", bpmExecuteCaseDTO.getOrgId());
        }


        if (bpmExecuteCaseDTO.getProjectId() != null && !"".equals(bpmExecuteCaseDTO.getProjectId())) {
            query.setParameter("projectId", bpmExecuteCaseDTO.getProjectId());
        }


        if (bpmExecuteCaseDTO.getDisplayName() != null && !"".equals(bpmExecuteCaseDTO.getDisplayName())) {
            query.setParameter("displayName", bpmExecuteCaseDTO.getDisplayName());
        }


        if (bpmExecuteCaseDTO.getDescription() != null && !"".equals(bpmExecuteCaseDTO.getDescription())) {
            query.setParameter("description", bpmExecuteCaseDTO.getDescription());
        }


        if (bpmExecuteCaseDTO.getActCategory() != null && !"".equals(bpmExecuteCaseDTO.getActCategory())) {
            query.setParameter("actCategory", bpmExecuteCaseDTO.getActCategory());
        }


        if (bpmExecuteCaseDTO.getProcessNames() != null && !"".equals(bpmExecuteCaseDTO.getProcessNames())) {
            query.setParameter("processNames", bpmExecuteCaseDTO.getProcessNames());
        }


        if (bpmExecuteCaseDTO.getEntitySubType() != null && !"".equals(bpmExecuteCaseDTO.getEntitySubType())) {
            query.setParameter("entitySubType", bpmExecuteCaseDTO.getEntitySubType());
        }


        if (bpmExecuteCaseDTO.getTaskDefKey() != null && !"".equals(bpmExecuteCaseDTO.getTaskDefKey())) {
            query.setParameter("taskDefKey", bpmExecuteCaseDTO.getTaskDefKey());
        }
        if (bpmExecuteCaseDTO.getMatchingKey() != null && !"".equals(bpmExecuteCaseDTO.getMatchingKey()) && bpmExecuteCaseDTO.getMatchingWord() != null && !"".equals(bpmExecuteCaseDTO.getMatchingWord())) {
            query.setParameter("matchingKey", bpmExecuteCaseDTO.getMatchingKey());
        }


        if (bpmExecuteCaseDTO.getPrivateCategory() != null && !"".equals(bpmExecuteCaseDTO.getPrivateCategory())) {
            query.setParameter("privateCategory", bpmExecuteCaseDTO.getPrivateCategory());
        }


        if (bpmExecuteCaseDTO.getExecClass() != null && !"".equals(bpmExecuteCaseDTO.getExecClass())) {
            query.setParameter("execClass", bpmExecuteCaseDTO.getExecClass());
        }


        if (bpmExecuteCaseDTO.getCountersignFlag() != null && bpmExecuteCaseDTO.getCountersignFlag()) {
            query.setParameter("countersignFlag", bpmExecuteCaseDTO.getCountersignFlag());
        }

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> list = query.getResultList();
        List<BpmExecuteCase> rsList = new ArrayList<>();
        for (Map<String, Object> bpmExecuteCase : list) {
            BpmExecuteCase newBpmExecuteCase = new BpmExecuteCase();
            copyProperties(bpmExecuteCase, newBpmExecuteCase);

            rsList.add(newBpmExecuteCase);
        }

        return rsList;

    }

    /**
     * 设置查询结果到对象中
     *
     * @param result         查询
     * @param bpmExecuteCase 焊工对象
     */
    private void copyProperties(Map<String, Object> result, BpmExecuteCase bpmExecuteCase) {

        if (result == null) {
            return;
        }


        if (result.get("id") != null) {
            bpmExecuteCase.setId((Long) result.get("id"));
        }

        if (result.get("org_id") != null) {
            bpmExecuteCase.setOrgId((Long) result.get("org_id"));
        }

        if (result.get("project_id") != null) {
            bpmExecuteCase.setProjectId((Long) result.get("project_id"));
        }


        if (result.get("display_name") != null) {
            bpmExecuteCase.setDisplayName((String) result.get("display_name"));
        }


        if (result.get("description") != null) {
            bpmExecuteCase.setDescription((String) result.get("description"));
        }


        if (result.get("actCategory") != null) {
            bpmExecuteCase.setActCategory((String) result.get("actCategory"));
        }

        if (result.get("process_names") != null) {
            bpmExecuteCase.setProcessNames((String) result.get("process_names"));
        }

        if (result.get("entity_sub_type") != null) {
            bpmExecuteCase.setEntitySubType((String) result.get("entity_sub_type"));
        }

        if (result.get("task_def_key") != null) {
            bpmExecuteCase.setTaskDefKey((String) result.get("task_def_key"));
        }

        if (result.get("private_category") != null) {
            bpmExecuteCase.setPrivateCategory((String) result.get("private_category"));
        }


        if (result.get("exec_class") != null) {
            bpmExecuteCase.setExecClass((String) result.get("exec_class"));
        }


        if (result.get("countersign_flag") != null) {
            bpmExecuteCase.setCountersignFlag((Boolean) result.get("countersign_flag"));
        }


    }
}
