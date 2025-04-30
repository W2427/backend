package com.ose.test.entity;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 业务代码表。
 */
@Entity
@Table(name = "COLUMNS")
public class ColumnEntity extends BaseEntity {
    private static final long serialVersionUID = 687610743316450064L;

//    `TABLE_CATALOG`,
//        `TABLE_SCHEMA`,
//        `TABLE_NAME`, `COLUMN_NAME`,
//        `ORDINAL_POSITION`, `COLUMN_DEFAULT`,
//        `IS_NULLABLE`, `DATA_TYPE`,
//        `CHARACTER_MAXIMUM_LENGTH`, `CHARACTER_OCTET_LENGTH`,
//        `NUMERIC_PRECISION`, `NUMERIC_SCALE`, `DATETIME_PRECISION`,
//        `CHARACTER_SET_NAME`, `COLLATION_NAME`, `COLUMN_TYPE`,
//        `COLUMN_KEY`, `EXTRA`,

    @Schema(description = "数据库名 db")
    @Column(name = "TABLE_SCHEMA")
    private String db;

    @Schema(description = "表名 tb")
    @Column(name = "TABLE_NAME")
    private String tb;

    @Schema(description = "列名")
    @Column(name = "COLUMN_NAME")
    private String column;

    @Schema(description = "列类型")
    @Column(name = "COLUMN_TYPE")
    private String columnType;


    @Schema(description = "新 列类型")
    @Column(name = "NEW_COLUMN_TYPE")
    private String newColumnType;

    @Column
    private String nll;

    @Column
    private String action;

    @Schema(description = "是否已经处理")
    @Column
    private Boolean isHandled;

    @Schema(description = "标记")
    @Column
    private String mark;

    private String execResult;

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTb() {
        return tb;
    }

    public void setTb(String tb) {
        this.tb = tb;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public Boolean getHandled() {
        return isHandled;
    }

    public void setHandled(Boolean handled) {
        isHandled = handled;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getNewColumnType() {
        return newColumnType;
    }

    public void setNewColumnType(String newColumnType) {
        this.newColumnType = newColumnType;
    }

    public String getNll() {
        return nll;
    }

    public void setNll(String nll) {
        this.nll = nll;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getExecResult() {
        return execResult;
    }

    public void setExecResult(String execResult) {
        this.execResult = execResult;
    }
}
