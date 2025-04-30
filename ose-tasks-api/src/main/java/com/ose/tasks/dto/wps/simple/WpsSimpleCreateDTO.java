package com.ose.tasks.dto.wps.simple;

import com.ose.tasks.vo.WeldMaterialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;

public class WpsSimpleCreateDTO {

    private static final long serialVersionUID = -2913409237271159152L;

    @Schema(description = "WPS编号")
    private String no;

    @Schema(description = "焊材类型")
    @Enumerated(EnumType.STRING)
    private List<WeldMaterialType> weldMaterialType;

    @Schema(description = "备注")
    private String remark;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public List<WeldMaterialType> getWeldMaterialType() {
        return weldMaterialType;
    }

    public void setWeldMaterialType(List<WeldMaterialType> weldMaterialType) {
        this.weldMaterialType = weldMaterialType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
