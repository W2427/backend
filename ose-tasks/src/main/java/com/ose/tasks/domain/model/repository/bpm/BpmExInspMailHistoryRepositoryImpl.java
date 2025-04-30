package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.ExInspTaskEmailDTO;
import com.ose.tasks.entity.bpm.BpmExInspMailHistory;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BpmExInspMailHistoryRepositoryImpl extends BaseRepository implements BpmExInspMailHistoryRepositoryCustom {


    @Override
    public Page<BpmExInspMailHistory> getTaskEmailList(Long orgId, Long projectId, ExInspTaskEmailDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * ");
        sql.append("FROM external_inspection_mail_history as e ");
        sql.append("WHERE ");
        sql.append("e.project_id = :projectId ");

        if (criteriaDTO.getOperator() != null && !"".equals(criteriaDTO.getOperator())) {
            sql.append("AND e.operator = :operator");
        }

        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (projectId != null && !"".equals(projectId)) {
            query.setParameter("projectId", projectId);
            countQuery.setParameter("projectId", projectId);
        }

        if (criteriaDTO.getOperator() != null && !"".equals(criteriaDTO.getOperator())) {
            query.setParameter("operator", criteriaDTO.getOperator());
            countQuery.setParameter("operator", criteriaDTO.getOperator());
        }

        query.setParameter("start", (criteriaDTO.getPage().getNo() - 1) * criteriaDTO.getPage().getSize());
        query.setParameter("offset", criteriaDTO.getPage().getSize());

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> list = query.getResultList();


        Long count = (Long) countQuery.getSingleResult();

        List<BpmExInspMailHistory> rsList = new ArrayList<>();

        for (Map<String, Object> m : list) {
            BpmExInspMailHistory entity = new BpmExInspMailHistory();
            if (m.get("reports") != null) {
                entity.setReports(m.get("reports").toString());
            }
            if (m.get("catalogue") != null) {
                entity.setCatalogue(m.get("catalogue").toString());
            }
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                entity.setSendTime(format.parse(m.get("send_time").toString()));
//                entity.setCreatedAt(format.parse(m.get("createdAt").toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            entity.setAttachments(m.get("attachments").toString());
            entity.setToMail(m.get("to_mail").toString());
            entity.setSubject(m.get("subject").toString());
            entity.setOperator(Long.parseLong(m.get("operator").toString()));
            entity.setCcMail(m.get("cc_mail").toString());
            entity.setId(Long.parseLong(m.get("id").toString()));
            entity.setMainContent(m.get("main_content").toString());
            rsList.add(entity);
        }
        return new PageImpl<>(rsList, criteriaDTO.toPageable(), count.longValue());
    }

    @Override
    public List<ExInspTaskEmailDTO> getOperatorList(Long orgId, Long projectId) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("e.operator ");
        sql.append("FROM external_inspection_mail_history as e ");
        sql.append("WHERE ");
        sql.append("e.project_id = :projectId ");
        sql.append("GROUP BY operator ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);
        List<BigInteger> list = query.getResultList();
        List<ExInspTaskEmailDTO> operatorList = new ArrayList<ExInspTaskEmailDTO>();

        for (BigInteger m : list) {
            ExInspTaskEmailDTO dto = new ExInspTaskEmailDTO();
            dto.setOperator(m.longValue());
            operatorList.add(dto);
        }
        return operatorList;
    }
}
