package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.CheckOutHourDTO;
import com.ose.tasks.dto.LeaveDataDTO;
import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.tasks.entity.drawing.DingTalkLeaveData;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DingTalkLeaveDataRepositoryImpl extends BaseRepository implements DingTalkLeaveDataRepositoryCustom {

    @Override
    public List<DingTalkLeaveData> searchReview(
        ReviewCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT DISTINCT ");
        sql.append(" dtl.duration_percent, ");
        sql.append(" dtl.duration_unit, ");
        sql.append(" dtl.start_leave_time, ");
        sql.append(" dtl.end_leave_time, ");
        sql.append(" dtl.job_number, ");
        sql.append(" dtl.leave_code, ");
        sql.append(" dtl.user_id, ");
        sql.append(" dtl.status, ");
        sql.append(" u.name AS userName, ");
        sql.append(" ei.company AS userCompany ");
        sql.append(" FROM saint_whale_tasks.drawing_record dr ");
        sql.append(" LEFT JOIN saint_whale_auth.users u ON u.id = dr.engineer_id ");
        sql.append(" LEFT JOIN saint_whale_tasks.dingtalk_leavedata dtl ON u.username = dtl.job_number ");
        sql.append(" LEFT JOIN saint_whale_tasks.employee_information ei ON u.username = ei.employee_id ");
        sql.append(" WHERE  ");
        sql.append(" u.deleted IS FALSE ");
        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate()!=null) {
            sql.append(" AND dtl.start_leave_time <= CONCAT(:endDate, ' 23:59:59') ");
            sql.append(" AND dtl.end_leave_time >= CONCAT(:startDate, ' 00:00:00')  ");
        }

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
        List<DingTalkLeaveData> dingTalkLeaveData = new ArrayList<>();
        for (Map<String, Object> resultMap : queryResultList) {
            DingTalkLeaveData dingTalkLeaveData1 = new DingTalkLeaveData();
            EntityStatus status = dingTalkLeaveData1.getStatus();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, dingTalkLeaveData1,"status");
            if (status!= null) {
                String statusStr = status.toString();
                newMap.put("status", statusStr);
            }

            dingTalkLeaveData.add(dingTalkLeaveData1);
        }
        return dingTalkLeaveData;
    }


}
