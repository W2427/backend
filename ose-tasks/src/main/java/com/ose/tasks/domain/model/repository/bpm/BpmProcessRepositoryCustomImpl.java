package com.ose.tasks.domain.model.repository.bpm;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.ProcessCriteriaDTO;
import com.ose.tasks.dto.timesheet.SelectDataDTO;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.*;

/**
 * 工序查询。
 */
public class BpmProcessRepositoryCustomImpl extends BaseRepository implements BpmProcessRepositoryCustom {

    @Override
    public Page<BpmProcess> search(ProcessCriteriaDTO criteriaDTO, Long projectId, Long orgId) {
        SQLQueryBuilder<BpmProcess> builder = getSQLQueryBuilder(BpmProcess.class)
            .is("orgId", orgId)
            .is("projectId", projectId)
            .is("status", EntityStatus.ACTIVE);

        if (!LongUtils.isEmpty(criteriaDTO.getProcessStageId())) {
            builder.is("processStage.id", criteriaDTO.getProcessStageId());
        }

        if (!LongUtils.isEmpty(criteriaDTO.getProcessCategoryId())) {
            builder.is("processCategory.id", criteriaDTO.getProcessCategoryId());
        }

        if (!StringUtils.isEmpty(criteriaDTO.getName())) {
            Map<String, Map<String, Object>> keywordCriteria = new IdentityHashMap<>();
            Map<String, Object> operator = new IdentityHashMap<>();
            operator.put("$like", criteriaDTO.getName());
            keywordCriteria.put("nameEn", operator);
            keywordCriteria.put("nameCn", operator);
            builder.orObj(keywordCriteria);
        }


        return builder.paginate(criteriaDTO.toPageable())
            .exec().page();
    }

    @Override
    public List<SelectDataDTO> findByDrawingIdAndUserId(Long drawingId, Long userId) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT " +
            "  dr.id AS drawingId, " +
            "  dr.no AS drawingNo, " +
            "  bps.name_en AS stage, " +
            "  bps.id AS stageId, " +
            "  bp.name_en AS process, " +
            "  bp.id AS processId " +
            "FROM " +
            "  bpm_activity_instance_base bai " +
            "  LEFT JOIN bpm_activity_instance_state bais ON bais.bai_id = bai.id " +
            "  LEFT JOIN bpm_act_task_assignee bata ON bata.act_inst_id = bai.id  " +
            "  LEFT JOIN drawing dr ON dr.id = bai.entity_id  " +
            "  LEFT JOIN bpm_process_stage bps ON bps.id = bai.process_stage_id " +
            "  LEFT JOIN bpm_process bp ON bp.id = bai.process_id " +
            "WHERE " +
            "  dr.id = :drawingId " +
            "  AND bai.act_category = 'Design'  " +
            "  AND bais.finish_state IS NOT NULL  " +
            "  AND bata.assignee = :userId " +
            "GROUP BY " +
            "  bps.id,bp.id ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("drawingId", drawingId);
        query.setParameter("userId", userId);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        List<SelectDataDTO> result = new ArrayList<SelectDataDTO>();


        for (Map<String, Object> map : queryResultList) {
            SelectDataDTO data = new SelectDataDTO();
            BeanUtils.copyProperties(map, data);
            result.add(data);
        }
        return result;
    }

}
