package com.ose.tasks.dto.wps;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class WpsBaseMetalDTO extends BaseDTO {

    private static final long serialVersionUID = 4971893988096855371L;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "别称")
    private String codeAlias;

    @Schema(description = "规范强度")
    private String strength;

    @Schema(description = "标准成分")
    private String materialComposition;

    @Schema(description = "产品构造")
    private String materialForm;

    @Schema(description = "章节")
    private String partNo;

    @Schema(description = "材质标准")
    private String criterion;

    @Schema(description = "组别")
    private String groupNo;

    public String getCode() {
        return code;
    }

    public String getCodeAlias() {
        return codeAlias;
    }

    public void setCodeAlias(String codeAlias) {
        this.codeAlias = codeAlias;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getMaterialComposition() {
        return materialComposition;
    }

    public void setMaterialComposition(String materialComposition) {
        this.materialComposition = materialComposition;
    }

    public String getMaterialForm() {
        return materialForm;
    }

    public void setMaterialForm(String materialForm) {
        this.materialForm = materialForm;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getCriterion() {
        return criterion;
    }

    public void setCriterion(String criterion) {
        this.criterion = criterion;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }
}
