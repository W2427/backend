package com.ose.test.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * POH list查询DTO
 */
public class PohDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 项目ID")
    private String spmProjId = "";

    @Schema(description = "SPM 订单编号")
    private String poNumber = "";

    @Schema(description = "SPM BUYER")
    private String buyer = "";

    @Schema(description = "SPM 合同编号")
    private String pohId = "";

    public String getSpmProjId() {
        return spmProjId;
    }

    public void setSpmProjId(String spmProjId) {
        this.spmProjId = spmProjId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getPohId() {
        return pohId;
    }

    public void setPohId(String pohId) {
        this.pohId = pohId;
    }

}
