package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库材料二维码查询DTO
 */
public class MmQrCodeResultDTO extends BaseDTO {

    private static final long serialVersionUID = -3296794427584062119L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "公司ID")
    public Long companyId;

    @Schema(description = "二维码")
    public String qrCode;

    @Schema(description = "类型（一类一码，一物一码）")
    private String qrCodeType;

    @Schema(description = "已盘点数量")
    private Integer inventoryQty = 0;

    @Schema(description = "材料编码")
    private String mmMaterialCodeNo;

    @Schema(description = "Ident码")
    private String identCode;

    @Schema(description = "规格量")
    private Integer specQty= 0;

    @Schema(description = "规格名称")
    private String specName;

    @Schema(description = "计量单位")
    private String designUnit;

    @Schema(description = "仓库类型（公司、项目）")
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description = "材料描述")
    private String mmMaterialCodeDescription;

    @Schema(description = "运行状态")
    private EntityStatus runningStatus;

    @Schema(description = "在库数量")
    private Integer inStockQty = 0;

    @Schema(description = "出库数量")
    private Integer issuedQty = 0;

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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(String qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public Integer getInventoryQty() {
        return inventoryQty;
    }

    public void setInventoryQty(Integer inventoryQty) {
        this.inventoryQty = inventoryQty;
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

    public MaterialOrganizationType getWareHouseType() {
        return materialOrganizationType;
    }

    public void setWareHouseType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public String getMmMaterialCodeDescription() {
        return mmMaterialCodeDescription;
    }

    public void setMmMaterialCodeDescription(String mmMaterialCodeDescription) {
        this.mmMaterialCodeDescription = mmMaterialCodeDescription;
    }

    public EntityStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(EntityStatus runningStatus) {
        this.runningStatus = runningStatus;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
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
}
