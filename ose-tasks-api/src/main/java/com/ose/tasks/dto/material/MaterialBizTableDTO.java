package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class MaterialBizTableDTO extends BaseDTO {

    public MaterialBizTableDTO() {
        super();
    }

    public MaterialBizTableDTO(String bizTable, String bizTableDesc) {
        super();
        this.bizTable = bizTable;
        this.bizTableDesc = bizTableDesc;
    }

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "码表")
    private String bizTable;

    @Schema(description = "描述")
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
