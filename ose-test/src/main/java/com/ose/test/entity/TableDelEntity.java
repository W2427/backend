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
@Table(name = "table_del")
public class TableDelEntity extends BaseEntity {

    @Schema(description = "数据库名 db")
    @Column(name = "TABLE_SCHEMA")
    private String db;

    @Schema(description = "表名 tb")
    @Column(name = "TABLE_NAME")
    private String tb;

    @Column(name = "exec_result")
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

    public String getExecResult() {
        return execResult;
    }

    public void setExecResult(String execResult) {
        this.execResult = execResult;
    }
}
