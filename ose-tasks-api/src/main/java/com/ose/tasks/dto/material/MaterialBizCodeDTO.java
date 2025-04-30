package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class MaterialBizCodeDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "代码表")
    private String bizTable;

    @Schema(description = "代码表描述")
    private String bizTableDesc;

    @Schema(description = "代码")
    private String bizCode;

    @Schema(description = "代码短描述")
    private String shortDesc;

    @Schema(description = "代码长描述")
    private String longDesc;

    @Schema(description = "备注")
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
