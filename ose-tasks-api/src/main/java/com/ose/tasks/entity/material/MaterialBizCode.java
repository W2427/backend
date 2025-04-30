package com.ose.tasks.entity.material;

import jakarta.persistence.*;

import com.ose.entity.BaseBizEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "mat_biz_code")
public class MaterialBizCode extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    //表
    private String bizTable;

    //表描述
    private String bizTableDesc;

    //代码
    private String bizCode;

    //描述
    @Column(length = 500)
    private String shortDesc;

    //描述
    private String longDesc;

    //备注
    private String remark;

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

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public void setLongDesc(String longDesc) {
        this.longDesc = longDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
