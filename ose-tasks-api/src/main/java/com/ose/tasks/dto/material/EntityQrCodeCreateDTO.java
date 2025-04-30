package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体二维码数据传输对象。
 */
public class EntityQrCodeCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 6199416384146098650L;

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "材料准备单id")
    private Long fItemId;

    @Schema(description = "流程实例id")
    private Long actInstId;

    @Schema(description = "材料二维码")
    private String materialQrCode;

    @Schema(description = "下料单id")
    private Long bpmCuttingId;

    @Schema(description = "下料单编号")
    private String bpmCuttingNo;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getfItemId() {
        return fItemId;
    }

    public void setfItemId(Long fItemId) {
        this.fItemId = fItemId;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getMaterialQrCode() {
        return materialQrCode;
    }

    public void setMaterialQrCode(String materialQrCode) {
        this.materialQrCode = materialQrCode;
    }

    public Long getBpmCuttingId() {
        return bpmCuttingId;
    }

    public void setBpmCuttingId(Long bpmCuttingId) {
        this.bpmCuttingId = bpmCuttingId;
    }

    public String getBpmCuttingNo() {
        return bpmCuttingNo;
    }

    public void setBpmCuttingNo(String bpmCuttingNo) {
        this.bpmCuttingNo = bpmCuttingNo;
    }

}
