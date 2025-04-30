package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 套料清单更新DTO
 */
public class FMaterialNestPatchDTO extends BaseDTO {

    private static final long serialVersionUID = 7548521809152380829L;

    @Schema(description = "备注")
    private String fmnrDesc;

    @Schema(description = "网关")
    private String gateway;

    @Schema(description = "网关名称")
    private String gatewayName;

    public String getFmnrDesc() {
        return fmnrDesc;
    }

    public void setFmnrDesc(String fmnrDesc) {
        this.fmnrDesc = fmnrDesc;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

}
