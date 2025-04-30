package com.ose.materialspm.domain.model.repository;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import org.springframework.data.domain.Page;

import com.ose.materialspm.entity.DemoEntity;
import com.ose.repository.BaseRepository;


public class DemoRepositoryImpl extends BaseRepository implements DemoRepositoryCustom {

    /**
     * 查询请购单详情。
     *
     * @param id 请购单id
     * @return 请购单详情分页数据
     */
    @SuppressWarnings("unchecked")
    @Override
    public Page<DemoEntity> search(
        String id
    ) {
        EntityManager entityManager = getEntityManager();

        StringBuffer sbSql = new StringBuffer();
        String sql = sbSql
            .append("SELECT rownum as id, cc_code.ident_code as identCode " +
                "FROM mv_mxj_commodity_desc cc_code " +
                "where ROWNUM <= 2 ")

            .toString();

        System.out.println(sql);

        Query query = entityManager.createNativeQuery(sql, "ItemResults");
        // 	query.setParameter("reqId", "aaa");

        List<DemoEntity> result = query.getResultList();
        System.out.println("SQL.size=" + result.size());
        for (DemoEntity o : result) {
            System.out.println(o.getIdentCode());
            System.out.println(o.getClass().getName());
        }

        return null;
    }

}
