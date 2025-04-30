package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.QrCodeType;
import com.ose.material.vo.MaterialOrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
/**
 * 发货单详情DTO
 */
public class MmShippingDetailDTO extends BaseDTO {

    private static final long serialVersionUID = -912969964906857728L;
    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "请购单ID")
    private Long companyId;

    @Schema(description = "发货单ID")
    private Long shippingId;

    @Schema(description = "材料编码")
    private String mmMaterialCodeNo;

    @Schema(description = "ident码")
    private String identCode;

    @Schema(description = "规格")
    private Integer specQty= 0;

    @Schema(description = "规格名称")
    private String specName;

    @Schema(description = "设计单位")
    private String designUnit;

    @Schema(description = "发货数量")
    private Integer shippingQty = 0;

    @Schema(description = "材料描述")
    private String mmMaterialCodeDescription;

    @Schema(description = "材料分类")
    @Enumerated(EnumType.STRING)
    public QrCodeType qrCodeType;

    @Schema(description = "仓库类型（公司、项目）")
    @Enumerated(EnumType.STRING)
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description = "规格名称")
    private String mmRequisitionDetailSpecName;

    @Schema(description = "备注")
    private String remarks;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getMmMaterialCodeNo() {
        return mmMaterialCodeNo;
    }

    public void setMmMaterialCodeNo(String mmMaterialCodeNo) {
        this.mmMaterialCodeNo = mmMaterialCodeNo;
    }

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public Integer getSpecQty() {
        return specQty;
    }

    public void setSpecQty(Integer specQty) {
        this.specQty = specQty;
    }

    public String getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
    }

    public String getMmMaterialCodeDescription() {
        return mmMaterialCodeDescription;
    }

    public void setMmMaterialCodeDescription(String mmMaterialCodeDescription) {
        this.mmMaterialCodeDescription = mmMaterialCodeDescription;
    }

    public QrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(QrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public MaterialOrganizationType getWareHouseType() {
        return materialOrganizationType;
    }

    public void setWareHouseType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public String getMmRequisitionDetailSpecName() {
        return mmRequisitionDetailSpecName;
    }

    public void setMmRequisitionDetailSpecName(String mmRequisitionDetailSpecName) {
        this.mmRequisitionDetailSpecName = mmRequisitionDetailSpecName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public Integer getShippingQty() {
        return shippingQty;
    }

    public void setShippingQty(Integer shippingQty) {
        this.shippingQty = shippingQty;
    }
}
