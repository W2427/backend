package com.ose.test.domain.model.repository;

import java.util.List;

/**
 * 业务代码数据仓库。
 */
public interface ColumnEntityRepositoryCustom {


    /**
     *  删除 表 ID。
     *
     */
    void delTable(String db, String tb);

    void delColumn(String db, String tb, String column);


    void changeColumn(String db, String tb, String column, String nll);

    void changeColumnType(String db, String tb, String column, String nll, String clType);


    void updateColumn(String db, String tb, String column);

    void updateDuplicateColumn(String db, String tb, String column, String oldValue, String newValue);

    List<List<Object>> findData(String sql);

    void updatePathColumn(String db, String tb, String cl, Long id, String newValue);

    void updateAssigneeColumn(String db, String tb, String assignee_, String assigneeStr, String newAssigneeStr);
}
