package com.ose.repository.helper;

import jakarta.persistence.Query;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class QueryBuilder {
    private final StringBuilder sql = new StringBuilder();
    private final Map<String, Object> params = new HashMap<>();
    private boolean hasWhere = false;

    public QueryBuilder(String baseSql) {
        sql.append(baseSql);
    }

    public QueryBuilder addCondition(boolean condition, String sqlFragment, String paramName, Object paramValue) {
        if (condition && paramValue != null) {
            appendWhereKeyword();
            sql.append(sqlFragment);
            params.put(paramName, paramValue);
        }
        return this;
    }

    public QueryBuilder addLikeCondition(boolean condition, String sqlFragment, String paramName, String value) {
        if (condition && value != null && !value.isEmpty()) {
            appendWhereKeyword();
            sql.append(sqlFragment);
            params.put(paramName, "%" + value + "%"); // 自动添加通配符
        }
        return this;
    }

    private void appendWhereKeyword() {
        if (!hasWhere) {
            sql.append(" WHERE ");
            hasWhere = true;
        } else {
            sql.append(" AND ");
        }
    }

    public QueryBuilder addInCondition(boolean condition,
                                       String sqlFragment, String paramName, Collection<?> values) {
        if (condition && values != null && !values.isEmpty()) {
            appendWhereKeyword();
            sql.append(sqlFragment);
            params.put(paramName, values);
        }
        return this;
    }

    public QueryBuilder addRangeCondition(boolean condition,
                                          String sqlFragment, String startParam, String endParam,
                                          Object startValue, Object endValue) {
        if (condition && (startValue != null || endValue != null)) {
            appendWhereKeyword();
            sql.append(sqlFragment);
            if (startValue != null) params.put(startParam, startValue);
            if (endValue != null) params.put(endParam, endValue);
        }
        return this;
    }

    public String getSql() {
        return sql.toString();
    }

    public void applyParameters(Query query) {
        params.forEach(query::setParameter);
    }

}
