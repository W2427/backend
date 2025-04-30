package com.ose.tasks.domain.model.repository.repairdata;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.rapairData.RepairWbsListDTO;
import com.ose.tasks.dto.rapairData.RepairWbsSearchDTO;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepairDataRepositoryImpl extends BaseRepository implements RepairDataRepositoryCustom {

    @Transactional(readOnly = true)
    public Page<RepairWbsListDTO> searchUndoWbs(
        Long orgId,
        Long projectId,
        RepairWbsSearchDTO repairWbsSearchDTO
    ) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * ");
        sql.append(" FROM ( ");
        sql.append(" SELECT ");
        sql.append(" se.project_id     AS project_id, ");
        sql.append(" se.id    AS wbs_entry_id, ");
        sql.append(" se.name  AS wbs_name, ");
        sql.append(" se.stage, ");
        sql.append(" se.process, ");
        sql.append(" se.entity_type, ");
        sql.append(" se.start_at, ");
        sql.append(" wes.started_at, ");
        sql.append(" wes.finished_at, ");
        sql.append(" wes.running_status, ");
        sql.append(" SUM(CASE WHEN (pes.running_status = 'APPROVED' or r.optional) THEN 1 ELSE 0 END)  AS predecessor_finished, ");
        sql.append(" COUNT(0) AS predecessor_total ");
        sql.append(" FROM ");
        sql.append(" wbs_entry AS se ");

        sql.append(" INNER JOIN wbs_entry_state AS wes ");
        sql.append(" ON wes.wbs_entry_id = se.id ");

        sql.append(" INNER JOIN wbs_entry_relation AS r ");
        sql.append(" ON r.project_id = se.project_id ");
        sql.append(" AND r.successor_id = se.guid ");
        sql.append(" INNER JOIN wbs_entry AS pe ");
        sql.append(" ON pe.project_id = se.project_id ");
        sql.append(" AND pe.guid = r.predecessor_id ");
        sql.append(" AND pe.active = se.active ");
        sql.append(" AND pe.deleted = 0 ");

        sql.append(" INNER JOIN wbs_entry_state AS pes  ");
        sql.append(" ON pes.wbs_entry_id = pe.id  ");

        sql.append(" WHERE ");
        sql.append(" se.project_id = :projectId ");
        sql.append(" AND se.deleted = 0 ");
        sql.append(" AND se.active = 1 ");
        sql.append(" AND wes.running_status IS NULL ");
        sql.append(" GROUP BY se.project_id, se.guid ");
        sql.append("  ) AS se ");
        sql.append(" WHERE ");
        sql.append(" se.predecessor_finished = se.predecessor_total ");

        if (repairWbsSearchDTO.getProcess() != null) {
            sql.append(" AND se.process = :process ");
        }
        if (repairWbsSearchDTO.getStage() != null) {
            sql.append(" AND se.stage = :stage ");
        }
        System.out.println(sql);
        System.out.println(projectId);
        String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ") AS `W`";
        Query query = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql);

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        if (repairWbsSearchDTO.getProcess() != null) {
            query.setParameter("process", repairWbsSearchDTO.getProcess());
            countQuery.setParameter("process", repairWbsSearchDTO.getProcess());
        }
        if (repairWbsSearchDTO.getStage() != null) {
            query.setParameter("stage", repairWbsSearchDTO.getStage());
            countQuery.setParameter("stage", repairWbsSearchDTO.getStage());
        }



        int pageNo = repairWbsSearchDTO.getPage().getNo();
        int pageSize = 600;

        if (repairWbsSearchDTO.getFetchAll()) {
            pageSize = Integer.MAX_VALUE;
            repairWbsSearchDTO.getPage().setSize(pageSize);
            repairWbsSearchDTO.setFetchAll(false);
        }

        query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        List<Map<String, Object>> queryResultList = query.getResultList();

        Long count = (Long) countQuery.getSingleResult();


        List<RepairWbsListDTO> resultList = new ArrayList<RepairWbsListDTO>();

        for (Map<String, Object> resultMap : queryResultList) {
            RepairWbsListDTO repairWbsListDTO = new RepairWbsListDTO();

            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("startAt") != null) {
                newMap.put("startAt", String.valueOf(newMap.get("startAt")));
            }
            if (newMap.get("startedAt") != null) {
                newMap.put("startedAt", String.valueOf(newMap.get("startAt")));
            }
            if (newMap.get("finishedAt") != null) {
                newMap.put("finishedAt", String.valueOf(newMap.get("finishedAt")));
            }

            if (newMap.get("runningStatus") != null) {
                newMap.put("runningStatus", EntityStatus.valueOf((String) newMap.get("runningStatus")));
            }
            if (newMap.get("predecessorFinished") != null) {
                newMap.put("predecessorFinished", Boolean.parseBoolean(String.valueOf(newMap.get("predecessorFinished"))));
            }
            if (newMap.get("predecessorTotal") != null) {
                newMap.put("predecessorTotal", Integer.valueOf(String.valueOf(newMap.get("predecessorTotal"))));
            }

            BeanUtils.copyProperties(newMap, repairWbsListDTO);
            resultList.add(repairWbsListDTO);
        }
        return new PageImpl<>(resultList, repairWbsSearchDTO.toPageable(), count.longValue());
    }

    @Transactional(readOnly = true)
    public Page<RepairWbsListDTO> searchPreWbs(
        Long orgId,
        Long projectId,
        RepairWbsSearchDTO repairWbsSearchDTO
    ) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("select we.id ");
        sql.append(" from wbs_entry as we");
        sql.append(" INNER JOIN wbs_entry_state AS wes ");
        sql.append(" on we.id = wes.wbs_entry_id ");
        sql.append(" where we.active = 1 ");
        sql.append(" and we.deleted = 0 ");
        sql.append(" and  wes.running_status = 'PENDING' ");
        sql.append(" and  we.project_id = :projectId ");
        sql.append(" and  wes.task_package_id is not null ");

        if (repairWbsSearchDTO.getProcess() != null) {
            sql.append(" and we.process = :process ");
        }

        if (repairWbsSearchDTO.getStage() != null) {
            sql.append(" and stage = :stage ");
        }

        String countSql = "SELECT COUNT(*) FROM (" + sql.toString() + ") AS `W`";
        Query query = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSql);

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        if (repairWbsSearchDTO.getProcess() != null) {
            query.setParameter("process", repairWbsSearchDTO.getProcess());
            countQuery.setParameter("process", repairWbsSearchDTO.getProcess());
        }

        if (repairWbsSearchDTO.getStage() != null) {
            query.setParameter("stage", repairWbsSearchDTO.getStage());
            countQuery.setParameter("stage", repairWbsSearchDTO.getStage());
        }


        int pageNo = repairWbsSearchDTO.getPage().getNo();
        int pageSize = 600;

        if (repairWbsSearchDTO.getFetchAll()) {
            pageSize = Integer.MAX_VALUE;
            repairWbsSearchDTO.getPage().setSize(pageSize);
            repairWbsSearchDTO.setFetchAll(false);
        }

        query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);


        List<Map<String, Object>> queryResultList = query.getResultList();

        Long count = (Long) countQuery.getSingleResult();


        List<RepairWbsListDTO> resultList = new ArrayList<RepairWbsListDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            RepairWbsListDTO repairWbsListDTO = new RepairWbsListDTO();

            if (resultMap.get("id") != null) {
                Long id = Long.valueOf(String.valueOf(resultMap.get("id")));
                repairWbsListDTO.setWbsEntryId(id);
            }
            resultList.add(repairWbsListDTO);
        }
        return new PageImpl<>(resultList, repairWbsSearchDTO.toPageable(), count.longValue());
    }

}
