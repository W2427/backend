package com.ose.tasks.domain.model.repository.holiday;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.holiday.WorkDaySearchDTO;
import com.ose.tasks.entity.holiday.WorkDayData;
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

public class WorkDayRepositoryImpl extends BaseRepository implements WorkDayRepositoryCustom {

    @Override
    public Page<WorkDayData> search(
        WorkDaySearchDTO dto
    ) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ha.* ");
        sql.append(" from work_day_data ha ");
        sql.append(" where ha.deleted is false ");


        if (dto.getCountry() != null && !"".equals(dto.getCountry())) {
            sql.append(" and ha.country =:country ");
        }

        sql.append(" order by ha.work_date ");
        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);


        if (dto.getCountry() != null && !"".equals(dto.getCountry())) {
            query.setParameter("country", dto.getCountry());
            countQuery.setParameter("country", dto.getCountry());
        }

        // 查询结果
        int pageNo = dto.getPage().getNo();
        int pageSize = dto.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<WorkDayData> workDayDatas = new ArrayList<WorkDayData>();
        for (Map<String, Object> resultMap : queryResultList) {
            WorkDayData workDayData = new WorkDayData();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            BeanUtils.copyProperties(newMap, workDayData);


            workDayDatas.add(workDayData);

        }
        return new PageImpl<>(workDayDatas, dto.toPageable(), count.longValue());

    }

}
