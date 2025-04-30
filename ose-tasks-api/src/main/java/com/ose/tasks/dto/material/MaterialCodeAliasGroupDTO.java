package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料代码别称与分组对应关系 数据传输对象
 */
public class MaterialCodeAliasGroupDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7865715460931126379L;

    @Schema(description = "材料代码别称")
    private String materialCodeAlias;

    @Schema(description = "材料分组代码")
    private String groupCode;

    @Schema(description = "备注")
    private String remark;

    public String getMaterialCodeAlias() {
        return materialCodeAlias;
    }

    public void setMaterialCodeAlias(String materialCodeAlias) {
        this.materialCodeAlias = materialCodeAlias;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
