package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.QrCodeType;
import com.ose.material.vo.MaterialOrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

/**
 * 发货单详情字段添加DTO
 */
public class MmShippingDetailAddDetailsDTO extends BaseDTO {

    private static final long serialVersionUID = -3664044488321037697L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "请购单ID")
    private Long companyId;

    @Schema(description = "请购单ID")
    private Long mmRequisitionId;

    @Schema(description = "请购单编号")
    private String mmRequisitionNo;

    @Schema(description = "请购单详情ID")
    private Long mmRequisitionDetailId;

    @Schema(description = "材料编码")
    private String mmMaterialCodeNo;

    @Schema(description = "ident码")
    private String identCode;

    @Schema(description = "规格量")
    private Integer specQty;

    @Schema(description = "规格名称")
    private String specName;

    @Schema(description = "设计单位")
    private String designUnit;

    @Schema(description = "设计请购数量")
    private Integer designQty = 0;

    @Schema(description = "描述")
    private String mmMaterialCodeDescription;

    @Schema(description = "提交部门")
    private String department;

    @Schema(description = "提交人")
    private String submitter;

    @Schema(description = "材料分类")
    public QrCodeType qrCodeType;

    @Schema(description = "仓库类型（公司、项目）")
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "是否已采购")
    private Boolean purchased;

    @Schema(description = "计划到货时间")
    private Date planReceiveDate;

    @Schema(description = "采购数量")
    private Double purchaseQty = 0.0;

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

    public Long getMmRequisitionId() {
        return mmRequisitionId;
    }

    public void setMmRequisitionId(Long mmRequisitionId) {
        this.mmRequisitionId = mmRequisitionId;
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

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
    }

    public Integer getDesignQty() {
        return designQty;
    }

    public void setDesignQty(Integer designQty) {
        this.designQty = designQty;
    }

    public String getMmMaterialCodeDescription() {
        return mmMaterialCodeDescription;
    }

    public void setMmMaterialCodeDescription(String mmMaterialCodeDescription) {
        this.mmMaterialCodeDescription = mmMaterialCodeDescription;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getPurchased() {
        return purchased;
    }

    public void setPurchased(Boolean purchased) {
        this.purchased = purchased;
    }

    public Date getPlanReceiveDate() {
        return planReceiveDate;
    }

    public void setPlanReceiveDate(Date planReceiveDate) {
        this.planReceiveDate = planReceiveDate;
    }

    public Double getPurchaseQty() {
        return purchaseQty;
    }

    public void setPurchaseQty(Double purchaseQty) {
        this.purchaseQty = purchaseQty;
    }

    public Long getMmRequisitionDetailId() {
        return mmRequisitionDetailId;
    }

    public void setMmRequisitionDetailId(Long mmRequisitionDetailId) {
        this.mmRequisitionDetailId = mmRequisitionDetailId;
    }

    public String getMmRequisitionNo() {
        return mmRequisitionNo;
    }

    public void setMmRequisitionNo(String mmRequisitionNo) {
        this.mmRequisitionNo = mmRequisitionNo;
    }
}
