package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 材料代码别称与分组对应关系 数据传输对象
 */
public class MaterialCodeAliasGroupCriteriaDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -4788672148907507005L;

    @Schema(description = "材料分组代码")
    private String groupCode;

    @Schema(description = "材料代码别称")
    private String materialCodeAlias;

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getMaterialCodeAlias() {
        return materialCodeAlias;
    }

    public void setMaterialCodeAlias(String materialCodeAlias) {
        this.materialCodeAlias = materialCodeAlias;
    }
}
