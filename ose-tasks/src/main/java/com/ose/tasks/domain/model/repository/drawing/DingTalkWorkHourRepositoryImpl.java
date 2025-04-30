package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.*;
import com.ose.tasks.entity.drawing.DingTalkWorkHour;
import com.ose.tasks.entity.drawing.DrawingRecord;
import com.ose.util.BeanUtils;
import com.ose.vo.DrawingRecordStatus;
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

public class DingTalkWorkHourRepositoryImpl extends BaseRepository implements DingTalkWorkHourRepositoryCustom {

    @Override
    public List<CheckOutHourDTO> searchReview(
        ReviewCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" dr.work_hour_date AS workHourDate, ");
        sql.append(" dr.engineer AS engineer, ");
        sql.append(" SUM( dr.work_hour ) AS workHour, ");
        sql.append(" SUM( dr.out_hour ) AS outHour, ");
        sql.append(" dtw.start_check_time AS startHour, ");
        sql.append(" dtw.end_check_time AS endHour, ");
        sql.append(" u.username AS username, ");
        sql.append(" dtw.work_duration AS workDuration, ");
        sql.append(" ei.company AS company ");
        sql.append(" FROM saint_whale_tasks.drawing_record dr ");
        sql.append(" LEFT JOIN saint_whale_auth.users u ON u.id = dr.engineer_id ");
        sql.append(" LEFT JOIN saint_whale_tasks.dingtalk_workhour dtw ON ( u.username = dtw.job_number AND dr.work_hour_date = dtw.work_hour_date ) ");
        sql.append(" LEFT JOIN saint_whale_tasks.employee_information ei ON u.username = ei.employee_id ");
        sql.append(" WHERE  ");
        sql.append(" dr.deleted = 0 AND dr.out_hour IS NOT NULL  ");
        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate()!=null) {
            sql.append(" and dr.work_hour_date between :startDate and :endDate  ");
        }
        sql.append(" GROUP BY  ");
        sql.append(" u.id,  ");
        sql.append(" dr.work_hour_date  ");
        sql.append(" ORDER BY  ");
        sql.append(" u.username DESC,  ");
        sql.append(" dr.work_hour_date DESC  ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate() !=null) {
            query.setParameter("startDate", criteriaDTO.getStartDate());
            countQuery.setParameter("startDate", criteriaDTO.getStartDate());
            query.setParameter("endDate", criteriaDTO.getEndDate());
            countQuery.setParameter("endDate", criteriaDTO.getEndDate());
        }




        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<CheckOutHourDTO> checkOutHourDTOS = new ArrayList<CheckOutHourDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            CheckOutHourDTO checkOutHourDTO = new CheckOutHourDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, checkOutHourDTO);
            checkOutHourDTOS.add(checkOutHourDTO);

        }
        return checkOutHourDTOS;

    }

}
