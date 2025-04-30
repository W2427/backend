package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体二维码数据传输对象。
 */
public class SpoolQrCodeCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 6199416384146098650L;

    @Schema(description = "实体id")
    private Long entityId;

    @Schema(description = "实体类型")
    private String entityType;

    //交接单id
    private Long deliveryId;

    //交接单no
    private String deliveryNo;

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
