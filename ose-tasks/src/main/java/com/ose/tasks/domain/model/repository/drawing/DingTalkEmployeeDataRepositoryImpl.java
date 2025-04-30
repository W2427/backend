package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.CheckOutHourDTO;
import com.ose.tasks.dto.LeaveDataDTO;
import com.ose.tasks.dto.ReviewCriteriaDTO;
import com.ose.tasks.entity.drawing.DingTalkEmployeeData;
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
public class DingTalkEmployeeDataRepositoryImpl extends BaseRepository implements DingTalkEmployeeDataRepositoryCustom {
    @Override
    public List<DingTalkEmployeeData> searchReview() {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT DISTINCT ");
//        sql.append(" dte.* ");
        sql.append(" dte.job_number, ");
        sql.append(" dte.name, ");
        sql.append(" dte.employee_status, ");
        sql.append(" dte.confirm_join_time, ");
        sql.append(" dte.regular_time, ");
        sql.append(" ei.initial_employment_date ");
        sql.append(" FROM ");
        sql.append(" saint_whale_tasks.dingtalk_employeedata dte ");
        sql.append(" LEFT JOIN saint_whale_tasks.employee_information ei ON dte.job_number = ei.employee_id ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        // 设置结果（结构为MAP）到WPS对象中
        List<DingTalkEmployeeData> dingTalkEmployeeDatas = new ArrayList<DingTalkEmployeeData>();
        for (Map<String, Object> resultMap : queryResultList) {
            DingTalkEmployeeData dingTalkEmployeeData = new DingTalkEmployeeData();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            BeanUtils.copyProperties(newMap, dingTalkEmployeeData);
            dingTalkEmployeeDatas.add(dingTalkEmployeeData);

        }
        return dingTalkEmployeeDatas;
    }

}
