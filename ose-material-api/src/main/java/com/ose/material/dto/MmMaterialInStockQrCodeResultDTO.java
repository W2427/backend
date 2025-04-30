package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 在库材料二维码创建DTO
 */
public class MmMaterialInStockQrCodeResultDTO extends BaseDTO {

    private static final long serialVersionUID = 1302940222130820037L;

    public MmMaterialInStockQrCodeResultDTO(
        Long orgId,
        Long projectId,
        Long materialStockDetailQrCodeEntityId,
        Long materialStockDetailEntityId,
        String qrCode,
        Integer issueQty
    ) {
        this.orgId = orgId;
        this.projectId = projectId;
        this.materialStockDetailQrCodeEntityId = materialStockDetailQrCodeEntityId;
        this.materialStockDetailEntityId = materialStockDetailEntityId;
        this.qrCode = qrCode;
        this.issueQty = issueQty;
    }

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "入库单明细ID")
    private Long materialStockDetailQrCodeEntityId;

    @Schema(description = "入库单明细ID")
    private Long materialStockDetailEntityId;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "出库盘点数量")
    private Integer issueQty;

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

    public Long getMaterialStockDetailQrCodeEntityId() {
        return materialStockDetailQrCodeEntityId;
    }

    public void setMaterialStockDetailQrCodeEntityId(Long materialStockDetailQrCodeEntityId) {
        this.materialStockDetailQrCodeEntityId = materialStockDetailQrCodeEntityId;
    }

    public Long getMaterialStockDetailEntityId() {
        return materialStockDetailEntityId;
    }

    public void setMaterialStockDetailEntityId(Long materialStockDetailEntityId) {
        this.materialStockDetailEntityId = materialStockDetailEntityId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Integer getIssueQty() {
        return issueQty;
    }

    public void setIssueQty(Integer issueQty) {
        this.issueQty = issueQty;
    }
}
