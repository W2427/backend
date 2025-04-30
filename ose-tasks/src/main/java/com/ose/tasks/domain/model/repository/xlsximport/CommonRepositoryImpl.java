package com.ose.tasks.domain.model.repository.xlsximport;

import com.ose.repository.BaseRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.*;

public class CommonRepositoryImpl extends BaseRepository implements CommonRepositoryCustom {

    @Transactional(readOnly = true)
    public List<List<Object>> getColumnInfos(String dbName, String tableName) {
        EntityManager entityManager = getEntityManager();
            String sql = " SELECT  `COLUMN_NAME` AS name, " +
            "        `DATA_TYPE` AS dataType  " +
            "      FROM  " +
            "        `information_schema`.`columns`  " +
            "        WHERE `table_schema` = '" + dbName + "' " +
            "          AND `table_name` = '" + tableName  + "'";

        Query columnQuery = entityManager.createNativeQuery(sql);

        columnQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.TO_LIST);
        List<List<Object>> queryResultList = columnQuery.getResultList();

        return queryResultList;
    }

    @Override
    public List<List<Object>> getXlsList(Long orgId, Long projectId, String sql) {
        EntityManager entityManager = getEntityManager();

        // 查询结果
        Query query = entityManager.createNativeQuery(sql);

        query.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);

//        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.TO_LIST);


        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<List<Object>> queryResultList = query.getResultList();
        return queryResultList;
    }



}
