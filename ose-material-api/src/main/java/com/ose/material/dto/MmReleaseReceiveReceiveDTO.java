package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 材料入库DTO
 */
public class MmReleaseReceiveReceiveDTO extends BaseDTO {

    private static final long serialVersionUID = 8955646981041566531L;

    @Schema(description = "是否入库")
    public Boolean received;

    @Schema(description = "公司ID")
    public Long companyId;

    @Schema(description = "数量")
    public Integer qty;

    @Schema(description = "仓库ID")
    public Long wareHouseLocationId;

    @Schema(description = "外检入库")
    public Boolean inExternalQuality;

    public Boolean getReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getQty() {
        return qty;
    }

    public Long getWareHouseLocationId() {
        return wareHouseLocationId;
    }

    public void setWareHouseLocationId(Long wareHouseLocationId) {
        this.wareHouseLocationId = wareHouseLocationId;
    }

    public Boolean getInExternalQuality() {
        return inExternalQuality;
    }

    public void setInExternalQuality(Boolean inExternalQuality) {
        this.inExternalQuality = inExternalQuality;
    }
}
