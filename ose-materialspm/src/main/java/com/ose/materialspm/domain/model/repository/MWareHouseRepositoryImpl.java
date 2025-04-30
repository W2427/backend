package com.ose.materialspm.domain.model.repository;

import com.ose.materialspm.entity.MWareHouseEntity;
import com.ose.repository.BaseRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 查询接口。
 */
public class MWareHouseRepositoryImpl extends BaseRepository implements MWareHouseRepositoryCustom {

    @Override
    public List<MWareHouseEntity> findByProjId(String projId) {
        System.out.println("进入查询sql方法：" + new Date());

        EntityManager entityManager = getEntityManager();

        String sql = new StringBuffer()
            .append(" SELECT W.WH_ID, W.PROJ_ID, W.WH_CODE, WN.SHORT_DESC, WN.DESCRIPTION ")
            .append("   FROM  M_WAREHOUSE_NLS WN  ")
            .append("   INNER JOIN M_WAREHOUSES W ")
            .append("   ON W.WH_ID = WN.WH_ID ")
            .append("   WHERE W.PROJ_ID = :projId ")
            .append("  ORDER BY W.WH_CODE ")
            .toString();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("projId", projId);

        System.out.println("转换sql查询方法开始：" + new Date());
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();
        System.out.println("转换sql查询方法结束：" + new Date());
        MWareHouseEntity rs = null;
        List<MWareHouseEntity> rsList = new ArrayList<>();
        for (Map<String, Object> m : list) {
            rs = new MWareHouseEntity();
            rs.setWhId(Long.valueOf(m.get("WH_ID").toString()));
            rs.setProjId(m.get("PROJ_ID").toString());
            rs.setWhCode(m.get("WH_CODE").toString());
            rs.setShortDesc(m.get("SHORT_DESC").toString());
            rs.setDescription(m.get("DESCRIPTION").toString());

            rsList.add(rs);
        }

        return rsList;
    }

    @Override
    public List<MWareHouseEntity> findByCompanyId(Integer companyId) {

        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT M_WAREHOUSES.WH_ID , M_WAREHOUSES.WH_CODE");
        sql.append(" FROM M_WH_TO_USERS");
        sql.append(" INNER JOIN M_WAREHOUSES ");
        sql.append(" ON M_WH_TO_USERS.WH_ID = M_WAREHOUSES.WH_ID");
        sql.append(" WHERE M_WH_TO_USERS.M_USR_ID = 'SUQING'");
        if (companyId != null) {
            sql.append(" AND M_WAREHOUSES.COMPANY_ID = :companyId");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        if (companyId != null) {
            query.setParameter("companyId", companyId);
        }

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        MWareHouseEntity rs = null;
        List<MWareHouseEntity> rsList = new ArrayList<>();
        for (Map<String, Object> m : list) {
            rs = new MWareHouseEntity();
            rs.setWhId(Long.valueOf(m.get("WH_ID").toString()));
            rs.setWhCode(m.get("WH_CODE").toString());
            rsList.add(rs);
        }

        return rsList;
    }
}
