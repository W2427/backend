package com.ose.test.dto;

import com.ose.dto.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * POH list查询DTO
 */
public class ReceiveReceiptDTO extends PageDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "SPM 项目ID")
    private String spmProjId = "";

    @Schema(description = "SPM 订单编号")
    private String poNumber = "";

    @Schema(description = "SPM 放行单")
    private String relnNumber = "";

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

    public String getRelnNumber() {
        return relnNumber;
    }

    public void setRelnNumber(String relnNumber) {
        this.relnNumber = relnNumber;
    }


}
