package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目层级结构数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialMatchDTO extends BaseDTO {


    private static final long serialVersionUID = 8376843630358423644L;


    @Schema(description = "材料代码")
    private String materialCode;

    @Schema(description = "材料描述")
    private String materialDesc;

    @Schema(description = "bom数量")
    private Double bomQuantity;

    @Schema(description = "已匹配数量")
    private Double matchedQuantity;

    @Schema(description = "匹配率")
    private Double matchPercent;

    public MaterialMatchDTO() {
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public Double getBomQuantity() {
        return bomQuantity;
    }

    public void setBomQuantity(Double bomQuantity) {
        this.bomQuantity = bomQuantity;
    }

    public Double getMatchedQuantity() {
        return matchedQuantity;
    }

    public void setMatchedQuantity(Double matchedQuantity) {
        this.matchedQuantity = matchedQuantity;
    }

    public Double getMatchPercent() {
        return matchPercent;
    }

    public void setMatchPercent(Double matchPercent) {
        this.matchPercent = matchPercent;
    }
}
