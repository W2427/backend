package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class MaterialRequirementItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -8494196249889826897L;

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "规格/参数")
    private String parameter;

    @Schema(description = "材质")
    private String material;

    @Schema(description = "标准/等级")
    private String standard;

    @Schema(description = "证书要求")
    private String cert;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "需用数量")
    private String requireQuantity;

    @Schema(description = "托盘号")
    private String trayNo;

    @Schema(description = "使用部位")
    private String position;

    @Schema(description = "需用日期")
    private Date requiredDate;

    @Schema(description = "审核量")
    private String check;

    @Schema(description = "备注")
    private String remark;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRequireQuantity() {
        return requireQuantity;
    }

    public void setRequireQuantity(String requireQuantity) {
        this.requireQuantity = requireQuantity;
    }

    public String getTrayNo() {
        return trayNo;
    }

    public void setTrayNo(String trayNo) {
        this.trayNo = trayNo;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(Date requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
