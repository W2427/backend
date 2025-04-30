package com.ose.tasks.entity.material;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "mat_biz_code_table")
public class MaterialBizCodeTable extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //表
    private String bizTable;

    //表描述
    private String bizTableDesc;

    public String getBizTable() {
        return bizTable;
    }

    public void setBizTable(String bizTable) {
        this.bizTable = bizTable;
    }

    public String getBizTableDesc() {
        return bizTableDesc;
    }

    public void setBizTableDesc(String bizTableDesc) {
        this.bizTableDesc = bizTableDesc;
    }

}
