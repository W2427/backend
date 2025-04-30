package com.ose.tasks.domain.model.repository.bpm;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import com.ose.tasks.vo.bpm.BpmsProcessNameEnum;
import com.ose.vo.BpmTaskType;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.TodoTaskCriteriaDTO;

/**
 * 任务查询。
 */
public class BpmHiTaskinstRepositoryImpl extends BaseRepository implements BpmHiTaskinstRepositoryCustom {

    @Override
    public List<Long> findActInstIdsInHiTaskinst(Long projectId, TodoTaskCriteriaDTO taskCriteria, String assignee) {
        EntityManager entityManager = getEntityManager();
        String sql = new StringBuilder().append("SELECT " +
            " act_inst_id " +
            " FROM " +
            " bpm_hi_taskinst  " +
            " WHERE" +
            " tenant_id_ = :projectId AND assignee_ = :assignee " +
            " UNION  " +
            " SELECT" +
            " act_inst_id " +
            " FROM" +
            " bpm_hi_taskinst  " +
            " WHERE" +
            " tenant_id_ = :projectId AND owner_ =:assignee ").toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projectId", projectId.toString());
        query.setParameter("assignee", assignee);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<Long> result = new ArrayList<>();
        for (Map<String, Object> m : list) {
            if (m.get("act_inst_id") != null) {
                Object actInstIdObj = m.get("act_inst_id");
                Long actInstId = (Long) actInstIdObj;
                Long longValue = actInstId.longValue();
                result.add(longValue);
            }
        }

        return result;
    }

    @Override
    public Integer countWeldByOperator(Long orgId, Long projectId, Long operatorId) {

        EntityManager entityManager = getEntityManager();
        String sql = new StringBuffer()
            .append(" SELECT count(*) c FROM bpm_hi_taskinst t LEFT JOIN bpm_activity_instance_base a ON t.act_inst_id = a.act_inst_id")
            .append(" WHERE a.project_id = :projectId")
            .append(" AND a.process_name = :process")
            .append(" AND t.task_type = :taskType")
            .append(" AND t.operator = :operator")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projectId", projectId);
        query.setParameter("process", BpmsProcessNameEnum.WELD.getType());
        query.setParameter("taskType", BpmTaskType.WELD_EXECUTE.name());
        query.setParameter("operator", operatorId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<Integer> rsList = new ArrayList<Integer>();
        for (Map<String, Object> m : list) {
            if (m.get("c") != null) {
                rsList.add(((BigInteger) m.get("c")).intValue());
            }
        }

        return rsList.get(0);
    }

}
