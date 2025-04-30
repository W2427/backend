package com.ose.tasks.domain.model.repository.performance;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.CheckOutHourDTO;
import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.tasks.dto.performance.PerformanceAppraisalListDetailDTO;
import com.ose.util.BeanUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PerformanceAppraisalListDetailRepositoryImpl extends BaseRepository implements PerformanceAppraisalListDetailRepositoryCustom {

    @Override
    public List<PerformanceAppraisalListDetailDTO> searchReview(
        ReviewCriteriaDTO criteriaDTO) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM ");
        sql.append(" saint_whale_tasks.performance_appraisal_list_detail pald ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";

        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<PerformanceAppraisalListDetailDTO> performanceAppraisalListDetailDTOS = new ArrayList<PerformanceAppraisalListDetailDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            PerformanceAppraisalListDetailDTO performanceAppraisalListDetailDTO = new PerformanceAppraisalListDetailDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, performanceAppraisalListDetailDTO);
            performanceAppraisalListDetailDTOS.add(performanceAppraisalListDetailDTO);
        }
        return performanceAppraisalListDetailDTOS;
    }
}
