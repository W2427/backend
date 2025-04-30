package com.ose.tasks.domain.model.repository;

import java.util.List;

/**
 * 项目 CRUD 操作接口。
 */
public interface ExportExcelRepositoryCustom {


    /**
     * 获取行数。
     *
     * @param rowCountSql 行数的SQL
     * @return 行数
     */
    Long findRowCount(String rowCountSql);

    /**
     * 获取列名。
     *
     * @param columnSql 焊接工艺规程ID
     * @return 列名称列表
     */
    List<String> findColumnName(String columnSql);


    /**
     * 获取数据。
     *
     * @param sql 焊接工艺规程ID
     * @return 列名称列表
     */
    List<List<Object>> findViewData(String sql);
}
