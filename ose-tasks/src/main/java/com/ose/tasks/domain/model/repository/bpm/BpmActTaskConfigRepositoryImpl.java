package com.ose.tasks.domain.model.repository.bpm;

import com.ose.dto.PageDTO;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.BpmActTaskConfigDTO;
import com.ose.tasks.entity.bpm.BpmActTaskConfig;

import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateStage;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateType;
import com.ose.vo.BpmTaskType;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 任务处理流程case库
 */
public class BpmActTaskConfigRepositoryImpl extends BaseRepository implements BpmActTaskConfigRepositoryCustom {


    /**
     * 查找任务代理设置清单。
     *
     * @param bpmActTaskConfigDTO 查询参数
     * @return
     */
    @Override
    public Page<BpmActTaskConfig> search(Long orgId, Long projectId, BpmActTaskConfigDTO bpmActTaskConfigDTO, PageDTO pageDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT * ");

        sql.append(" FROM bpm_act_task_config ")
            .append(" where 1=1 ");


        if (orgId != null && !orgId.equals(0L)) {
            sql.append(" AND `org_id` = :orgId");
        }


        if (projectId != null && !projectId.equals(0L)) {
            sql.append(" AND `project_id` = :projectId");
        }


        if (bpmActTaskConfigDTO.getDelegateStage() != null && !"".equals(bpmActTaskConfigDTO.getDelegateStage().name())) {
            sql.append(" AND `delegate_stage` = :delegateStage");
        }


        if (bpmActTaskConfigDTO.getDelegateType() != null && !"".equals(bpmActTaskConfigDTO.getDelegateType().name())) {
            sql.append(" AND `delegate_type` = :delegateType");
        }


        if (bpmActTaskConfigDTO.getOrderNo() != null && bpmActTaskConfigDTO.getOrderNo() != 0) {
            sql.append(" AND `order_no` = :orderNo");
        }







        if (bpmActTaskConfigDTO.getProcessCategory() != null) {
            sql.append(" AND `process_category` = :processCategory");
        }


        if (bpmActTaskConfigDTO.getProcessType() != null && !"".equals(bpmActTaskConfigDTO.getProcessType().name())) {
            sql.append(" AND `process_type` = :processType");
        }


        if (bpmActTaskConfigDTO.getProxy() != null && !"".equals(bpmActTaskConfigDTO.getProxy())) {
            sql.append(" AND `proxy` = :proxy");
        }



        if (bpmActTaskConfigDTO.getTaskDefKey() != null && !"".equals(bpmActTaskConfigDTO.getTaskDefKey())) {
            sql.append(" AND `task_def_key` = :taskDefKey");
        }



        if (bpmActTaskConfigDTO.getTaskName() != null && !"".equals(bpmActTaskConfigDTO.getTaskName())) {
            sql.append(" AND `proxy` = :proxy");
        }



        if (bpmActTaskConfigDTO.getProcessKey() != null && !"".equals(bpmActTaskConfigDTO.getProcessKey())) {
            sql.append(" AND `process_key` = :processKey");
        }


        if (bpmActTaskConfigDTO.getTaskType() != null && !"".equals(bpmActTaskConfigDTO.getTaskType())) {
            sql.append(" AND `task_type` = :taskType");
        }




        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());


        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        Query countQuery = entityManager.createNativeQuery(countSQL);


        if (orgId != null && !"".equals(orgId)) {
            query.setParameter("orgId", orgId);
            countQuery.setParameter("orgId", orgId);
        }


        if (projectId != null && !"".equals(projectId)) {
            query.setParameter("projectId", projectId);
            countQuery.setParameter("projectId", projectId);
        }


        if (bpmActTaskConfigDTO.getDelegateStage() != null && !"".equals(bpmActTaskConfigDTO.getDelegateStage().name())) {
            query.setParameter("delegateStage", bpmActTaskConfigDTO.getDelegateStage().name());
            countQuery.setParameter("delegateStage", bpmActTaskConfigDTO.getDelegateStage().name());
        }


        if (bpmActTaskConfigDTO.getDelegateType() != null && !"".equals(bpmActTaskConfigDTO.getDelegateType().name())) {
            query.setParameter("delegateType", bpmActTaskConfigDTO.getDelegateType().name());
            countQuery.setParameter("delegateType", bpmActTaskConfigDTO.getDelegateType().name());
        }


        if (bpmActTaskConfigDTO.getOrderNo() != null && bpmActTaskConfigDTO.getOrderNo() != 0) {
            query.setParameter("orderNo", bpmActTaskConfigDTO.getOrderNo());
            countQuery.setParameter("orderNo", bpmActTaskConfigDTO.getOrderNo());
        }








        if (bpmActTaskConfigDTO.getProcessCategory() != null) {
            query.setParameter("processCategory", bpmActTaskConfigDTO.getProcessCategory());
            countQuery.setParameter("processCategory", bpmActTaskConfigDTO.getProcessCategory());
        }


        if (bpmActTaskConfigDTO.getProcessType() != null && !"".equals(bpmActTaskConfigDTO.getProcessType().name())) {
            query.setParameter("processType", bpmActTaskConfigDTO.getProcessType().name());
            countQuery.setParameter("processType", bpmActTaskConfigDTO.getProcessType().name());
        }


        if (bpmActTaskConfigDTO.getProxy() != null && !"".equals(bpmActTaskConfigDTO.getProxy())) {
            query.setParameter("proxy", bpmActTaskConfigDTO.getProxy());
            countQuery.setParameter("proxy", bpmActTaskConfigDTO.getProxy());
        }


        if (bpmActTaskConfigDTO.getTaskDefKey() != null && !"".equals(bpmActTaskConfigDTO.getTaskDefKey())) {
            query.setParameter("taskDefKey", bpmActTaskConfigDTO.getTaskDefKey());
            countQuery.setParameter("taskDefKey", bpmActTaskConfigDTO.getTaskDefKey());
        }



        if (bpmActTaskConfigDTO.getTaskName() != null && !"".equals(bpmActTaskConfigDTO.getTaskName())) {
            query.setParameter("taskName", bpmActTaskConfigDTO.getTaskName());
            countQuery.setParameter("taskName", bpmActTaskConfigDTO.getTaskName());
        }


        if (bpmActTaskConfigDTO.getTaskName() != null && !"".equals(bpmActTaskConfigDTO.getTaskName())) {
            query.setParameter("processKey", bpmActTaskConfigDTO.getProcessKey());
            countQuery.setParameter("processKey", bpmActTaskConfigDTO.getProcessKey());
        }


        if (bpmActTaskConfigDTO.getTaskType() != null && !"".equals(bpmActTaskConfigDTO.getTaskType())) {
            query.setParameter("taskType", bpmActTaskConfigDTO.getTaskType());
            countQuery.setParameter("taskType", bpmActTaskConfigDTO.getTaskType());
        }

        query.setParameter("start", (pageDTO.getPage().getNo() - 1) * pageDTO.getPage().getSize());
        query.setParameter("offset", pageDTO.getPage().getSize());

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> list = query.getResultList();


        Long count = (Long) countQuery.getSingleResult();

        List<BpmActTaskConfig> rsList = new ArrayList<>();
        for (Map<String, Object> bpmActTaskConfigCase : list) {

            BpmActTaskConfig newBpmActTaskConfigCase = new BpmActTaskConfig();

            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(bpmActTaskConfigCase);
            if (newMap.get("taskType") != null) {
                newMap.put("taskType", BpmTaskType.valueOf((String) newMap.get("taskType")));
            }
            if (newMap.get("processType") != null) {
                newMap.put("processType", ProcessType.valueOf((String) newMap.get("processType")));
            }
            if (newMap.get("delegateType") != null) {
                newMap.put("delegateType", BpmActTaskConfigDelegateType.valueOf((String) newMap.get("delegateType")));
            }
            if (newMap.get("delegateStage") != null) {
                newMap.put("delegateStage", BpmActTaskConfigDelegateStage.valueOf((String) newMap.get("delegateStage")));
            }
            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }

            BeanUtils.copyProperties(newMap, newBpmActTaskConfigCase);

            rsList.add(newBpmActTaskConfigCase);
        }


        return new PageImpl<>(rsList, pageDTO.toPageable(), count.longValue());

    }


}
