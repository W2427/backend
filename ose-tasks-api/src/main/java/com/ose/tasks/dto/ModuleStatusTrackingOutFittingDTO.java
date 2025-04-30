package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 当前项目中的模块信息。
 */
public class ModuleStatusTrackingOutFittingDTO extends BaseDTO {

    private static final long serialVersionUID = 8602407949204455226L;

    @Schema(description = "模块编号")
    private String moduleNo;

    @Schema(description = "设计数量")
    private double designQty = 0.0;

    @Schema(description = "检验数量")
    private double inspectedQty = 0.0;

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public double getDesignQty() {
        return designQty;
    }

    public void setDesignQty(double designQty) {
        this.designQty = designQty;
    }

    public double getInspectedQty() {
        return inspectedQty;
    }

    public void setInspectedQty(double inspectedQty) {
        this.inspectedQty = inspectedQty;
    }
}

