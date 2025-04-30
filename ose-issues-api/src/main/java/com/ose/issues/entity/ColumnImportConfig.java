package com.ose.issues.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.ose.entity.ColumnImportConfigBase;

import com.ose.util.StringUtils;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义属性定义Excel导入列。
 */
@Entity
@Table(name = "column_import_config")
public class ColumnImportConfig extends ColumnImportConfigBase {

    private static final long serialVersionUID = 5585564235857490027L;

    private String columns;

    public ColumnImportConfig() {
    }

    @JsonCreator
    public ColumnImportConfig(@JsonProperty("columns") List<Integer> columns) {
        this.columns = StringUtils.toJSON(columns);
    }

    @JsonProperty(value = "columns", access = JsonProperty.Access.READ_ONLY)
    public List<Integer> getJsonColumns() {
        if (columns != null && !"".equals(columns)) {
            return StringUtils.decode(columns, new TypeReference<List<Integer>>() {
            });
        } else {
            return new ArrayList();
        }
    }

    @JsonIgnore
    public void setJsonColumns(List<Integer> columns) {
        if (columns != null) {
            this.columns = StringUtils.toJSON(columns);
        }
    }
    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }
}
