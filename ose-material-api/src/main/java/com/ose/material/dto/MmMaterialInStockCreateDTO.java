package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.MaterialOrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 在库材料主表创建DTO
 */
public class MmMaterialInStockCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -21449793661669221L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "公司Id")
    public Long companyId;

    @Schema(description = "ident码")
    private String identCode;

    @Schema(description = "材料编码")
    private String mmMaterialCodeNo;

    @Schema(description = "仓库类型（公司、项目）")
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description = "材料描述")
    private String mmMaterialCodeDescription;

    @Schema(description = "在库数量")
    private Integer inStockQty = 0;

    @Schema(description = "仓库名")
    private String mmWareHouseName;

    @Schema(description = "计量单位")
    private String designUnit;

    @Schema(description = "入库数量")
    private Integer receivedQty = 0;

    @Schema(description = "出库数量")
    private Integer issuedQty = 0;

    @Schema(description = "材料编码Id")
    private Long mmMaterialCodeId;

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

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public String getMmMaterialCodeNo() {
        return mmMaterialCodeNo;
    }

    public void setMmMaterialCodeNo(String mmMaterialCodeNo) {
        this.mmMaterialCodeNo = mmMaterialCodeNo;
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

    public Integer getInStockQty() {
        return inStockQty;
    }

    public void setInStockQty(Integer inStockQty) {
        this.inStockQty = inStockQty;
    }

    public String getMmWareHouseName() {
        return mmWareHouseName;
    }

    public void setMmWareHouseName(String mmWareHouseName) {
        this.mmWareHouseName = mmWareHouseName;
    }

    public String getDesignUnit() {
        return designUnit;
    }

    public void setDesignUnit(String designUnit) {
        this.designUnit = designUnit;
    }

    public Integer getReceivedQty() {
        return receivedQty;
    }

    public void setReceivedQty(Integer receivedQty) {
        this.receivedQty = receivedQty;
    }

    public Integer getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Integer issuedQty) {
        this.issuedQty = issuedQty;
    }

    public Long getMmMaterialCodeId() {
        return mmMaterialCodeId;
    }

    public void setMmMaterialCodeId(Long mmMaterialCodeId) {
        this.mmMaterialCodeId = mmMaterialCodeId;
    }
}
