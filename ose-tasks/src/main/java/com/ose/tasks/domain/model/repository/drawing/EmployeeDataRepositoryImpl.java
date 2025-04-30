package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.EmployeeDataDTO;
import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.tasks.entity.drawing.DingTalkEmployeeData;
import com.ose.tasks.entity.drawing.EmployeeData;
import com.ose.util.BeanUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeDataRepositoryImpl extends BaseRepository implements EmployeeDataRepositoryCustom {
    @Override
    public List<EmployeeDataDTO> searchReview(
        ReviewCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" u.id, ");
        sql.append(" ei.employee_id, ");
        sql.append(" u.name, ");
        sql.append(" ei.company, ");
        sql.append(" ei.contract_company, ");
        sql.append(" ei.division, ");
        sql.append(" ei.department, ");
        sql.append(" ei.team, ");
        sql.append(" ei.initial_employment_date, ");
        sql.append(" dte.employee_status, ");
        sql.append(" dte.regular_time, ");
        sql.append(" dte.confirm_join_time, ");
        sql.append(" dte.length_of_career, ");

        sql.append(" dte.total_annual_leave, ");
        sql.append(" dte.remaining_annual, ");
        sql.append(" dte.remaining_annual_last_mth, ");
        sql.append(" dte.remaining_ot, ");
        sql.append(" dte.remaining_ot_last_mth, ");
        sql.append(" dte.absence, ");

        sql.append(" dte.special_annual_leave, ");
        sql.append(" dte.total_annual, ");

        sql.append(" SUM( CASE WHEN ( dr.project_name != 'Leave' ) THEN IFNULL( dr.work_hour, 0 ) ELSE 0 END ) AS totalNormalManHour, ");
        sql.append(" SUM( IFNULL( dr.out_hour, 0 )) AS totalOvertime ");
        sql.append(" FROM saint_whale_tasks.employee_information ei ");
        sql.append(" LEFT JOIN saint_whale_auth.users u ON u.username = ei.employee_id ");
        sql.append(" LEFT JOIN saint_whale_tasks.dingtalk_employeedata dte ON dte.job_number = ei.employee_id ");
        sql.append(" LEFT JOIN saint_whale_tasks.drawing_record dr ON dr.engineer_id = u.id ");
        sql.append(" WHERE  ");
        sql.append(" dte.employee_status is NOT NULL ");
        sql.append(" AND dr.deleted IS FALSE ");
        if (criteriaDTO.getStartDate() != null && criteriaDTO.getEndDate()!=null) {
            sql.append(" AND dr.work_hour_date between :startDate and :endDate  ");
        }
        sql.append(" AND u.deleted IS FALSE ");

        sql.append(" GROUP BY  ");
        sql.append(" ei.employee_id,  ");
        sql.append(" u.name  ");


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
        List<EmployeeDataDTO> employeeDataDTOS = new ArrayList<EmployeeDataDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            EmployeeDataDTO employeeDataDTO = new EmployeeDataDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, employeeDataDTO);
            employeeDataDTOS.add(employeeDataDTO);

        }
        return employeeDataDTOS;
    }

    public EmployeeDataDTO findById(String id) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" u.id, ");
        sql.append(" ei.employee_id, ");
        sql.append(" u.name, ");
        sql.append(" ei.company, ");
        sql.append(" ei.contract_company, ");
        sql.append(" ei.division, ");
        sql.append(" ei.department, ");
        sql.append(" ei.team, ");
        sql.append(" ei.initial_employment_date, ");
        sql.append(" dte.employee_status, ");
        sql.append(" dte.regular_time, ");
        sql.append(" dte.confirm_join_time, ");
        sql.append(" dte.length_of_career, ");
        sql.append(" dte.total_annual_leave, ");
        sql.append(" dte.remaining_annual, ");
        sql.append(" dte.remaining_annual_last_mth, ");
        sql.append(" dte.remaining_ot, ");
        sql.append(" dte.remaining_ot_last_mth, ");
        sql.append(" dte.absence, ");
        sql.append(" dte.special_annual_leave, ");
        sql.append(" dte.total_annual, ");
        sql.append(" SUM( CASE WHEN ( dr.project_name != 'Leave' ) THEN IFNULL( dr.work_hour, 0 ) ELSE 0 END ) AS totalNormalManHour, ");
        sql.append(" SUM( IFNULL( dr.out_hour, 0 )) AS totalOvertime ");
        sql.append(" FROM saint_whale_tasks.employee_information ei ");
        sql.append(" LEFT JOIN saint_whale_auth.users u ON u.username = ei.employee_id ");
        sql.append(" LEFT JOIN saint_whale_tasks.dingtalk_employeedata dte ON dte.job_number = ei.employee_id ");
        sql.append(" LEFT JOIN saint_whale_tasks.drawing_record dr ON dr.engineer_id = u.id ");
        sql.append(" WHERE  ");
        sql.append(" dte.employee_status is NOT NULL ");
        sql.append(" AND dr.deleted IS FALSE ");
        sql.append(" AND u.id = :id "); // 添加根据 id 过滤的条件
        sql.append(" GROUP BY  ");
        sql.append(" ei.employee_id,  ");
        sql.append(" u.name  ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.setParameter("id", id);

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);
        // 为 countQuery 设置 id 参数
        countQuery.setParameter("id", id);

        // 获取查询结果
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        if (!queryResultList.isEmpty()) {
            Map<String, Object> resultMap = queryResultList.get(0);
            EmployeeDataDTO employeeDataDTO = new EmployeeDataDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, employeeDataDTO);
            return employeeDataDTO;
        }
        return null;
    }

}
