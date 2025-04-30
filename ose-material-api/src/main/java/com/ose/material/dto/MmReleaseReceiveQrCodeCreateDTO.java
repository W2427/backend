package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import com.ose.material.vo.QrCodeType;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 入库材料二维码创建DTO
 */
public class MmReleaseReceiveQrCodeCreateDTO extends BaseDTO {

    private static final long serialVersionUID = -4452042832865713901L;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "入库单明细ID")
    private Long materialReceiveNoteDetailId;

    @Schema(description = "二维码")
    private String qrCode;

    @Schema(description = "运行状态")
    private EntityStatus status;

    @Schema(description = "数量")
    private Integer qty;

    @Schema(description = "类型")
    private QrCodeType type;

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

    public Long getMaterialReceiveNoteDetailId() {
        return materialReceiveNoteDetailId;
    }

    public void setMaterialReceiveNoteDetailId(Long materialReceiveNoteDetailId) {
        this.materialReceiveNoteDetailId = materialReceiveNoteDetailId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public EntityStatus getStatus() {
        return status;
    }

    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public QrCodeType getType() {
        return type;
    }

    public void setType(QrCodeType type) {
        this.type = type;
    }
}
