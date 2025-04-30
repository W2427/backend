package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class MaterialCodeSearchDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "代码")
    private String code;

    @Schema(description = "代码描述")
    private String desc;

    @Schema(description = "无法解析代码")
    private String leastCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLeastCode() {
        return leastCode;
    }

    public void setLeastCode(String leastCode) {
        this.leastCode = leastCode;
    }

}
