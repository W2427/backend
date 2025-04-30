package com.ose.material.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 发货单详情DTO
 */
public class MmShippingDetailsDTO extends BaseDTO {

    private static final long serialVersionUID = -1011920362705816248L;

    @Schema(description = "请购单ID")
    private Long mmRequisitionId;

    @Schema(description = "请购单编号")
    private String mmRequisitionNo;

    @Schema(description = "请购单详情ID")
    private Long mmRequisitionDetailId;

    @Schema(description = "已发货数量")
    private Integer shippedQty = 0;

    @Schema(description = "未发货数量")
    private Integer unShippedQty = 0;

    @Schema(description = "设计请购数量")
    private Integer designQty = 0;

    public Long getMmRequisitionId() {
        return mmRequisitionId;
    }

    public void setMmRequisitionId(Long mmRequisitionId) {
        this.mmRequisitionId = mmRequisitionId;
    }

    public String getMmRequisitionNo() {
        return mmRequisitionNo;
    }

    public void setMmRequisitionNo(String mmRequisitionNo) {
        this.mmRequisitionNo = mmRequisitionNo;
    }

    public Long getMmRequisitionDetailId() {
        return mmRequisitionDetailId;
    }

    public void setMmRequisitionDetailId(Long mmRequisitionDetailId) {
        this.mmRequisitionDetailId = mmRequisitionDetailId;
    }

    public Integer getShippedQty() {
        return shippedQty;
    }

    public void setShippedQty(Integer shippedQty) {
        this.shippedQty = shippedQty;
    }

    public Integer getUnShippedQty() {
        return unShippedQty;
    }

    public void setUnShippedQty(Integer unShippedQty) {
        this.unShippedQty = unShippedQty;
    }

    public Integer getDesignQty() {
        return designQty;
    }

    public void setDesignQty(Integer designQty) {
        this.designQty = designQty;
    }
}
