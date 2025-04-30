package com.ose.report.entity.dynamicreport;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class ModelTableSource {
    /**
     * 注入table组件的数据源
     */
    private JRBeanCollectionDataSource tableData;
    /**
     * 当前时间(查询时间)
     */
    private String date;

    public JRBeanCollectionDataSource getTableData() {
        return tableData;
    }

    public void setTableData(JRBeanCollectionDataSource tableData) {
        this.tableData = tableData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
