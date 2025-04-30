package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.QrCodeType;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 在库材料二维码创建DTO
 */
public class MmMaterialInStockQrCodeCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -3372565958414917888L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "项目ID")
    public Long companyId;

    @Schema(description = "在库单明细ID")
    private Long mmMaterialStockDetailId;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "运行状态")
    private EntityStatus runningStatus;

    @Schema(description = "类型（一类一码，一物一码）")
    private QrCodeType qrCodeType;

    @Schema(description = "在库数量")
    private Integer inStockQty = 0;

    @Schema(description = "仓库类型（公司、项目）")
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description = "入库数量")
    private Integer receivedQty = 0;

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

    public Long getMmMaterialStockDetailId() {
        return mmMaterialStockDetailId;
    }

    public void setMmMaterialStockDetailId(Long mmMaterialStockDetailId) {
        this.mmMaterialStockDetailId = mmMaterialStockDetailId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public EntityStatus getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(EntityStatus runningStatus) {
        this.runningStatus = runningStatus;
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

    public Integer getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Integer issuedQty) {
        this.issuedQty = issuedQty;
    }
}
