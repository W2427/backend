package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 出库清单DTO
 */
public class FMaterialIssueReceiptPostFromPrepareDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "领料单ID")
    private Long fmreqId;

    @Schema(description = "网关")
    private String gatewayCommand;

    public Long getFmreqId() {
        return fmreqId;
    }

    public void setFmreqId(Long fmreqId) {
        this.fmreqId = fmreqId;
    }

    public String getGatewayCommand() {
        return gatewayCommand;
    }

    public void setGatewayCommand(String gatewayCommand) {
        this.gatewayCommand = gatewayCommand;
    }

}
