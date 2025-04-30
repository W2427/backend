package com.ose.tasks.vo.wbs;

import com.ose.vo.ValueObject;

import java.util.Arrays;
import java.util.List;

/**
 * 实体类型设定规则。
 */
public enum EntityTypeRule implements ValueObject {

    WELD_TYPE_IS_AND_WELD_STAGE_IS("WELD_JOINT", new String[]{"weldType", "shopField"}),
    STRUCT_WELD_TYPE_IS_AND_WELD_STAGE_IS("STRUCT_WELD_JOINT", new String[]{"weldType", "hierarchyParentNo"}),
    COMPONENT("COMPONENT", new String[]{"shortCode"}),
    ELEC_COMPONENT("ELEC_COMPONENT", new String[]{"shortCode"}),
    SHORT_CODE_CONTAINS("COMPONENT", new String[]{"shortCode"});


    private List<String> fieldNames;

    private String entityType;

    /**
     * 构造方法。
     *
     * @param entityClass 数据实体类型
     * @param fieldNames  条件数据字段
     */
    EntityTypeRule(String entityClass, String[] fieldNames) {
        this.entityType = entityClass;
        this.fieldNames = Arrays.asList(fieldNames);
    }

    /**
     * 取得代码的表示名。
     *
     * @return 代码表示名
     */
    public String getDisplayName() {
        return this.name();
    }

    public String getEntityType() {
        return this.entityType;
    }

    /**
     * 取得代码的表示名。
     *
     * @return 代码表示名
     */
    List<String> getFieldNames() {
        return this.fieldNames;
    }

}
