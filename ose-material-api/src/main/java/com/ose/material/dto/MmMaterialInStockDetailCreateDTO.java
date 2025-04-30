package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.QrCodeType;
import com.ose.material.vo.MaterialOrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 在库材料明细创建DTO
 */
public class MmMaterialInStockDetailCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -2564435336773946795L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "公司Id")
    private Long companyId;

    @Schema(description = "材料主表ID")
    private Long materialStockId;

    @Schema(description = "Ident码")
    private String identCode;

    @Schema(description = "规格量")
    private Integer specQty;

    @Schema(description = "材料编码")
    private String mmMaterialCodeNo;

    @Schema(description = "材料描述")
    private String mmMaterialCodeDescription;

    @Schema(description = "计量单位")
    private String designUnit;

    @Schema(description = "设计量")
    private Integer designQty;

    @Schema(description = "类型（一类一码，一物一码）")
    private QrCodeType qrCodeType;

    @Schema(description = "在库数量")
    private Integer inStockQty = 0;

    @Schema(description = "出库数量")
    private Integer issuedQty = 0;

    @Schema(description = "仓库类型（公司、项目）")
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description = "入库数量")
    private Integer receivedQty = 0;

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

    public Long getMaterialStockId() {
        return materialStockId;
    }

    public void setMaterialStockId(Long materialStockId) {
        this.materialStockId = materialStockId;
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

    public String getMmMaterialCodeNo() {
        return mmMaterialCodeNo;
    }

    public void setMmMaterialCodeNo(String mmMaterialCodeNo) {
        this.mmMaterialCodeNo = mmMaterialCodeNo;
    }

    public String getMmMaterialCodeDescription() {
        return mmMaterialCodeDescription;
    }

    public void setMmMaterialCodeDescription(String mmMaterialCodeDescription) {
        this.mmMaterialCodeDescription = mmMaterialCodeDescription;
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

    public QrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(QrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public Integer getInStockQty() {
        return inStockQty;
    }

    public void setInStockQty(Integer inStockQty) {
        this.inStockQty = inStockQty;
    }

    public Integer getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Integer issuedQty) {
        this.issuedQty = issuedQty;
    }

    public MaterialOrganizationType getWareHouseType() {
        return materialOrganizationType;
    }

    public void setWareHouseType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public Integer getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Integer receivedQty) {
        this.receivedQty = receivedQty;
    }
}
