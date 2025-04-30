package com.ose.prints.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class StructurePartDetailDTO extends BaseDTO {

    private static final long serialVersionUID = 7056896313461124349L;

    @Schema(description = "零件号")
    private String partNo;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "项目名")
    private String projectNo;

    @Schema(description = "实体类型")
    private String entityType;

    @Schema(description = "炉批号")
    private String heatNo;

    @Schema(description = "材质")
    private String material;

    @Schema(description = "长度")
    private String length;

    @Schema(description = "打印次数")
    private Integer printCount;

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getPrintCount() {
        return printCount;
    }

    public void setPrintCount(Integer printCount) {
        this.printCount = printCount;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
}
