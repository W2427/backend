package com.ose.tasks.domain.model.service.xlsximport;

import com.ose.tasks.entity.xlsximport.ColumnImportConfig;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface XlsxImportInterface {

    /**
     * 取得所有自定义属性信息。
     *
     * @param sheet     工作表信息 数据类型，TEXT,"INTEGER,DOUBLE,FLOAT,BOOLEAN,DATE
     * @return 列号-自定义属性映射表
     */
    List<ColumnImportConfig> getColumnDefinitions(
            final Sheet sheet,
            final String dbName,
            final String tableName,
            final Long orgId,
            final Long projectId,
            final String clazzName,
            final int columnHeaderRowNo// COLUMN_HEADER_ROW_NO
    );
}
