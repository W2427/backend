package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目层级结构数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaterialInfoDTO extends BaseDTO {

    private static final long serialVersionUID = -8383215936668489120L;

    @Schema(description = "材料代码")
    private String materialCode;

    @Schema(description = "材料描述")
    private String materialDesc;

    @Schema(description = "数量")
    private String quantity;

    @Schema(description = "实体类型")
    private String type;

    public MaterialInfoDTO() {
    }

    public MaterialInfoDTO(String materialCode, String materialDesc, Double quantity) {
        this.materialCode = materialCode;
        this.materialDesc = materialDesc;
        this.quantity = String.valueOf(quantity);
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
