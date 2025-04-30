package com.ose.test.domain.model.repository;


import com.ose.repository.BaseRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务查询。
 */
public class ColumnEntityRepositoryCustomImpl extends BaseRepository implements ColumnEntityRepositoryCustom {


    @Override
    public void delTable(String db, String tb) {
        getEntityManager()
            .createStoredProcedureQuery("DEL_TABLE")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .setParameter(1, db)
            .setParameter(2, tb)
            .execute();
    }

    @Override
    public void delColumn(String db, String tb, String column) {
        getEntityManager()
            .createStoredProcedureQuery("DEL_COLUMN")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
            .setParameter(1, db)
            .setParameter(2, tb)
            .setParameter(3, column)
            .execute();
    }

    @Override
    public void changeColumn(String db, String tb, String column, String nll) {
        getEntityManager()
            .createStoredProcedureQuery("CHANGE_COLUMN")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(4, String.class, ParameterMode.IN)
            .setParameter(1, db)
            .setParameter(2, tb)
            .setParameter(3, column)
            .setParameter(4, nll)
            .execute();
    }

    @Override
    public void changeColumnType(String db, String tb, String column, String nll, String clType) {
        getEntityManager()
            .createStoredProcedureQuery("CHANGE_COLUMN_TYPE")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(4, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(5, String.class, ParameterMode.IN)
            .setParameter(1, db)
            .setParameter(2, tb)
            .setParameter(3, column)
            .setParameter(4, nll)
            .setParameter(5, clType)
            .execute();
    }

    @Override
    public void updateColumn(String db, String tb, String column) {
        getEntityManager()
            .createStoredProcedureQuery("UPDATE_COLUMN")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
            .setParameter(1, db)
            .setParameter(2, tb)
            .setParameter(3, column)
            .execute();

    }

    @Override
    public void updateDuplicateColumn(String db, String tb, String column, String oldValue, String newValue) {
        getEntityManager()
            .createStoredProcedureQuery("UPDATE_DUPLICATE_COLUMN")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(4, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(5, String.class, ParameterMode.IN)
            .setParameter(1, db)
            .setParameter(2, tb)
            .setParameter(3, column)
            .setParameter(4, oldValue)
            .setParameter(5, newValue)
            .execute();

    }

    /**
     * 获取数据。
     *
     * @param sql 焊接工艺规程ID
     * @return 列名称列表
     * NOTE 返回数据中不能有空字符串
     */
    @Override
    public List<List<Object>> findData(String sql) {


        EntityManager entityManager = getEntityManager();
        // 查询结果
        Query columnQuery = entityManager.createNativeQuery(sql);
        columnQuery.unwrap(NativeQuery.class).setResultTransformer(Transformers.TO_LIST);

        try {
            List<List<Object>> queryResultList = columnQuery.getResultList();


            return queryResultList;
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    @Override
    public void updatePathColumn(String db, String tb, String cl, Long id, String newValue) {
        getEntityManager()
            .createStoredProcedureQuery("UPDATE_PATH_COLUMN")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(4, Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter(5, String.class, ParameterMode.IN)
            .setParameter(1, db)
            .setParameter(2, tb)
            .setParameter(3, cl)
            .setParameter(4, id)
            .setParameter(5, newValue)
            .execute();
    }

    @Override
    public void updateAssigneeColumn(String db, String tb, String assignee_, String assigneeStr, String newAssigneeStr) {

        getEntityManager()
            .createStoredProcedureQuery("UPDATE_ASSIGNEE_COLUMN")
            .registerStoredProcedureParameter(1, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(4, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(5, String.class, ParameterMode.IN)
            .setParameter(1, db)
            .setParameter(2, tb)
            .setParameter(3, assignee_)
            .setParameter(4, assigneeStr)
            .setParameter(5, newAssigneeStr)
            .execute();
    }
}
