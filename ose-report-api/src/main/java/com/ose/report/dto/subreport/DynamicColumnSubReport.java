package com.ose.report.dto.subreport;

import java.util.List;
import java.util.Map;

/**
 * 子报表数据模型（动态报表）（明细）
 */
public class DynamicColumnSubReport extends SubReport {

    /**
     * 报表项目
     */
    private DynamicReportItem[] items;

    /**
     * Mapping数据（LIST）
     */
    private List<Map<String, Object>> mappingData;

    /**
     * 默认构造器
     */
    public DynamicColumnSubReport() {
    }

    /**
     * 参数构造器
     *
     * @param items       报表项目
     * @param mappingData Mapping数据（LIST）
     */
    public DynamicColumnSubReport(DynamicReportItem[] items, List<Map<String, Object>> mappingData) {
        this.items = items;
        this.mappingData = mappingData;
    }

    /**
     * Gets the value of items.
     *
     * @return the value of items
     */
    public DynamicReportItem[] getItems() {
        return items;
    }

    /**
     * Sets the items.
     *
     * <p>You can use getItems() to get the value of items</p>
     *
     * @param items items
     */
    public void setItems(DynamicReportItem[] items) {
        this.items = items;
    }

    /**
     * Gets the value of mappingData.
     *
     * @return the value of mappingData
     */
    public List<Map<String, Object>> getMappingData() {
        return mappingData;
    }

    /**
     * Sets the mappingData.
     *
     * <p>You can use getMappingData() to get the value of mappingData</p>
     *
     * @param mappingData mappingData
     */
    public void setMappingData(List<Map<String, Object>> mappingData) {
        this.mappingData = mappingData;
    }
}
