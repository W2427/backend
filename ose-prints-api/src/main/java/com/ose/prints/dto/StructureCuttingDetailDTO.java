package com.ose.prints.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 结构下料打印信息。
 */
public class StructureCuttingDetailDTO extends BaseDTO {
    private static final long serialVersionUID = -374484949094598842L;

    @Schema(description = "零件号")
    private String partNo;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "项目名")
    private String projectNo;

    @Schema(description = "数量")
    private String qty;

    @Schema(description = "材质")
    private String material;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "打印次数")
    private Integer printCount;

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
