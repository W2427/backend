package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.EmployeeDataDTO;
import com.ose.tasks.dto.EmployeeDataTestDTO;
import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.util.BeanUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmployeeDataTestRepositoryImpl extends BaseRepository implements EmployeeDataTestRepositoryCustom {
    @Override
    public List<EmployeeDataTestDTO> searchReview(
        ReviewCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append(" * ");
        sql.append(" FROM saint_whale_tasks.employee_information_test ");

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
        List<EmployeeDataTestDTO> employeeDataTestDTOS = new ArrayList<EmployeeDataTestDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            EmployeeDataTestDTO employeeDataTestDTO = new EmployeeDataTestDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, employeeDataTestDTO);
            employeeDataTestDTOS.add(employeeDataTestDTO);

        }
        return employeeDataTestDTOS;
    }

}
