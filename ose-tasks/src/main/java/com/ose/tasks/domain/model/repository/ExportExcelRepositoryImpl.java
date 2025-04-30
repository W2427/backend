package com.ose.tasks.domain.model.repository;

import com.ose.repository.BaseRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 批处理任务状态记录查询操作。
 */
public class ExportExcelRepositoryImpl extends BaseRepository implements ExportExcelRepositoryCustom {


    /**
     * 获取行数。
     *
     * @param rowCountSql 行数的SQL
     * @return 行数
     */
    @Override
    public Long findRowCount(String rowCountSql) {
        EntityManager entityManager = getEntityManager();

        Query countQuery = entityManager.createNativeQuery(rowCountSql);







        Long count = (Long) countQuery.getSingleResult();

        return count.longValue();
    }

    ;

    /**
     * 获取列名。
     *
     * @param columnSql 焊接工艺规程ID
     * @return 列名称列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<String> findColumnName(String columnSql) {
        EntityManager entityManager = getEntityManager();


        Query columnQuery = entityManager.createNativeQuery(columnSql);

        columnQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.TO_LIST);


        @SuppressWarnings("unchecked")

            List<List<String>> queryResultList = columnQuery.getResultList();

        List<String> columnTitles = new ArrayList<>();


        for (List<String> title : queryResultList) {
            String tl = "";

            tl = title.get(0);
            columnTitles.add(tl);
        }


        return columnTitles;
    }


    /**
     * 获取数据。
     *
     * @param sql 焊接工艺规程ID
     * @return 列名称列表
     * NOTE 返回数据中不能有空字符串
     */
    @Override
    @Transactional(readOnly = true)
    public List<List<Object>> findViewData(String sql) {


        EntityManager entityManager = getEntityManager();

        Query columnQuery = entityManager.createNativeQuery(sql);
        columnQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.TO_LIST);

        List<List<Object>> queryResultList = columnQuery.getResultList();


        return queryResultList;

    }

}
